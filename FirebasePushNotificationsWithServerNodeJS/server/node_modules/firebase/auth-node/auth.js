'use strict';

// TODO(dimond): This can be an npm package include eventually
var FirebaseTokenGenerator = require('./token-generator');
var fs = require('fs');
var https = require('https');
var jwt = require('jsonwebtoken');
var firebase = require('../app-node');

var GOOGLE_TOKEN_AUDIENCE = 'https://accounts.google.com/o/oauth2/token';
var GOOGLE_AUTH_TOKEN_HOST = 'accounts.google.com';
var GOOGLE_AUTH_TOKEN_PATH = '/o/oauth2/token';
var GOOGLE_AUTH_TOKEN_PORT = 443;

var ONE_HOUR_IN_SECONDS = 60 * 60;
var JWT_ALGORITHM = 'RS256';

/**
 * Server auth service bound to the provided app.
 *
 * @param {Object} app The app for this auth service
 * @constructor
 */
var Auth = function(app_) {
  if (!('options' in app_)) {
    throw new Error('First parameter to Auth constructor must be an instance of firebase.App');
  }

  var cachedToken_ = null;
  var tokenListeners_ = [];
  // Note that we don't support keys at well-known paths (yet). We can add this
  // if people ask for it
  var serviceAccountPathOrObject = app_.options.serviceAccount || process.env.GOOGLE_APPLICATION_CREDENTIALS;

  var serviceAccount;
  if (typeof serviceAccountPathOrObject === 'string') {
    try {
      serviceAccount = JSON.parse(fs.readFileSync(serviceAccountPathOrObject, 'utf8'));
    } catch (error) {
      throw new Error('Failed to parse service account key file: ' + error);
    }
  } else if (typeof serviceAccountPathOrObject === 'object') {
    // Allow both camel- and underscore-cased keys for the service account object
    serviceAccount = {};

    var projectId = serviceAccountPathOrObject.project_id || serviceAccountPathOrObject.projectId;
    if (typeof projectId !== 'undefined') {
      serviceAccount.project_id = projectId;
    }

    var privateKey = serviceAccountPathOrObject.private_key || serviceAccountPathOrObject.privateKey;
    if (typeof privateKey !== 'undefined') {
      serviceAccount.private_key = privateKey;
    }

    var clientEmail = serviceAccountPathOrObject.client_email || serviceAccountPathOrObject.clientEmail;
    if (typeof clientEmail !== 'undefined') {
      serviceAccount.client_email = clientEmail;
    }
  } else {
    throw new Error('Invalid service account provided');
  }

  if (typeof serviceAccount.private_key !== 'string' || !serviceAccount.private_key) {
    throw new Error('Service account must contain a "private_key" field');
  } else if (typeof serviceAccount.client_email !== 'string' || !serviceAccount.client_email) {
    throw new Error('Service account must contain a "client_email" field');
  }

  var serviceAccount_ = serviceAccount;
  var tokenGenerator_ = new FirebaseTokenGenerator(serviceAccount);

  /**
   * Generates a JWT that is used to retrieve an access token
   */
  function authJwt() {
    var claims = {
      scope: [
        'https://www.googleapis.com/auth/userinfo.email',
        'https://www.googleapis.com/auth/firebase.database',
      ].join(' ')
    };
    return jwt.sign(claims, serviceAccount_.private_key, {
      audience: GOOGLE_TOKEN_AUDIENCE,
      expiresIn: ONE_HOUR_IN_SECONDS,
      issuer: serviceAccount_.client_email,
      algorithm: JWT_ALGORITHM
    });
  }

  /**
   * Fetches a new access token by making a HTTP request to the
   * specified OAuth endpoint
   */
  function fetchAccessToken() {
    var token = authJwt();
    return new firebase.Promise(function(resolve, reject) {
      var postData = 'grant_type=urn%3Aietf%3Aparams%3Aoauth%3A' +
          'grant-type%3Ajwt-bearer&assertion=' +
          token;
      var options = {
        method: 'POST',
        host: GOOGLE_AUTH_TOKEN_HOST,
        port: GOOGLE_AUTH_TOKEN_PORT,
        path: GOOGLE_AUTH_TOKEN_PATH,
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
          'Content-Length': postData.length
        }
      };
      var req = https.request(options, function(res) {
        var buffers = [];
        res.on('data', function(buffer) {
          buffers.push(buffer);
        });
        res.on('end', function() {
          try {
            var json = JSON.parse(Buffer.concat(buffers));
            if (json.error) {
              var msg = 'Error refreshing access token: ' + json.error;
              if (json.error_description) {
                msg += ' (' + json.error_description + ')';
              }
              reject(new Error(msg));
            } else if (!json.access_token || !json.expires_in) {
              reject(new Error('Unexpected response from OAuth server'));
            } else {
              resolve(json);
            }
          } catch (err) {
            reject(err);
          }
        });
      });
      req.on('error', reject);
      req.write(postData);
      req.end();
    });
  }

  /**
   * Defines the app property with a getter but no setter.
   */
  Object.defineProperty(this, 'app', {
    get: function() { return app_; }
  });

  /**
   * Creates a new custom token that can be sent back to a client to use with
   * signInWithCustomToken.
   *
   * @param {string} uid The uid to use as the subject
   * @param {Object=} developerClaims Optional additional claims to include
   *                                  in the payload of the JWT
   *
   * @return {string} The JWT for the provided payload.
   */
  this.createCustomToken = function(uid, developerClaims) {
    return tokenGenerator_.createCustomToken(uid, developerClaims);
  };

  /**
   * Verifies a JWT auth token. Returns a Promise with the tokens claims. Rejects
   * the promise if the token could not be verified.
   *
   * @param {string} idToken The JWT to verify
   * @return {Object} The Promise that will be fulfilled after a successful
   *                  verification.
   */
  this.verifyIdToken = function(idToken) {
    return tokenGenerator_.verifyIdToken(idToken);
  };

  this.INTERNAL = {};

  /**
   * Deletes the service and it's associated resources
   */
  this.INTERNAL.delete = function() {
    // There are no resources to clean up
    return firebase.Promise.resolve();
  };

  /**
   * Internal method: Gets an auth token for the associated app.
   * @param {boolean} forceRefresh Forces a token refresh
   * @return {Object} The Promise that will be fulfilled with the current or new
   *                  token
   */
  this.INTERNAL.getToken = function(forceRefresh) {
    // TODO(dimond): refresh auth token on expiration
    var expired = cachedToken_ && cachedToken_.expirationTime < Date.now();
    if (cachedToken_ && !forceRefresh && !expired) {
      return firebase.Promise.resolve(cachedToken_);
    } else {
      return fetchAccessToken().then(function(result) {
        var token = {
          accessToken: result.access_token,
          expirationTime: Date.now() + (result.expires_in * 1000)
        };
        if (!cachedToken_ || cachedToken_.accessToken !== token.accessToken) {
          cachedToken_ = token;
          tokenListeners_.forEach(function(listener) {
            listener(token.accessToken);
          });
        }
        return token;
      });
    }
  };

  /**
   * Internal method: Adds a listener that is called each time a token changes.
   * @param {function(string)} listener The listener that will be called with
   *                                    each new token
   */
  this.INTERNAL.addAuthTokenListener = function(listener) {
    tokenListeners_.push(listener);
    if (cachedToken_) {
      listener(cachedToken_);
    }
  };

  /**
   * Internal method: Removes a token listener.
   * @param {function(string)} listener The listener to remove.
   */
  this.INTERNAL.removeAuthTokenListener = function(listener) {
    tokenListeners_ = tokenListeners_.filter(function(other) {
      return other !== listener;
    });
  };
};

module.exports = Auth;
