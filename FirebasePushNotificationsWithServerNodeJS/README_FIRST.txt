ANTES: Por favor utiliza tus propios archivos KEY
* APP: google-services.json
En este enlace viene todo al respecto:
https://firebase.google.com/docs/android/setup#add_firebase_to_your_app
* SERVER: Archivo .json 
En este enlace viene todo al respecto: 
https://firebase.google.com/docs/server/setup#add_firebase_to_your_app

*************************************************************************
LA APP:
Para que puedas probar este proyecto 
necesitarás de 2 dispositivos corriendo al mismo tiempo.
Uno emisor y uno receptor de notificaciones.

1. Registra el dispositivo en la base de datos Firebase:
Esto se logra corriendo el proyecto en ambos dispositivos 
y presionando el botón de "Recibir Notificaciones", para
que así se guarde su respectivo token_id_device de c/u

2. Prueba las notificaciones:

El proyecto debes configurarlo de la siguiente forma
para ambos casos

A. EMISOR (Este será quien mandé la notificación al receptor):

* MainActivity.java
Configurar el tag emisor:
private static final String ANIMAL_EMISOR = "perro";

* método toqueAnimal()
Configurar la línea con el tag emisor:
Call<UsuarioResponse> usuarioResponseCall = 
endponits.toqueAnimal(usuarioResponse.getId(), ANIMAL_EMISOR);

Una vez configurado esto, corre la aplicación en el dispositivo
que fungirá como EMISOR.

B. Receptor (Este será quien reciba la notificación)

* método toqueAnimal()
Configurar el objeto receptor con su id y datos:
final UsuarioResponse usuarioResponse = 
new UsuarioResponse("-KKAqu3M5p6GFgnqL9fu", "123", ANIMAL_RECEPTOR);

Una vez configurado esto, corre la aplicación en el dispositivo
que fungirá como RECEPTOR.
__________________________________
ID perro: -KKAqHNirVTDC-H9qVv0
ID gato: -KKAqu3M5p6GFgnqL9fu

*************************************************************************
EL SERVER:
Este servidor fue construido con Heroku en Node.js siguiendo estos pasos:
https://devcenter.heroku.com/articles/getting-started-with-nodejs#introduction



