##TP-Hercules-Tacs-2C2014


Repositorio del trabajo práctico del Grupo Hércules para TACS 

Para correr el sistema:

Utilizar "mvn appengine:devserver" en truequelibre/truequelibre-ear/
En Windows: usar RunOnDevserver.bat 



##Trabajo práctico TACS 2014 - 2do Cuatrimestre


El objetivo del TP es desarrollar una aplicación social interactiva que permita a los usuarios y sus amigos crear items los cuales serán canjeados mediante trueque por items de otros usuarios.
La forma de acceso a la misma será mediante una aplicación de facebook a la cual los usuarios deberán otorgar permisos.
El TP constará de 5 entregas en las cuales de forma iterativa e incremental se irán agregando funcionalidades a la aplicación.


#Restricciones y consideraciones:


* ~~La aplicación debe funcionar en Google App Engine.~~
* ~~El login de usuario debe ser con Facebook a través de OAuth.~~
* ~~Todos los items creados deberán referenciar a un artículo de Mercado Libre. Para eso la aplicación debe integrarse con la API del sitio.~~
* ~~Todas las llamadas al servidor deben ser asincrónicas.~~
* ~~Si bien se espera algo sencillo. La aplicación debe tener un frontend amigable a los usuarios.~~
* ~~Se debe utilizar maven para gestionar el life-cycle de la aplicación.~~
* ~~Se debe utilizar GIT como SCM.~~
* El nivel de cobertura de tests debe ser superior al 70%.
* Es tan importante el hecho de que la aplicación funcione como se espera como aplicar un buen diseño para la construcción de la misma.
* Todos los métodos no triviales deben tener su correspondiente javadoc explicando su función, forma de uso y cualquier otra información relevante.
* Cualquier decisión respecto del código o las soluciones utilizadas debe estar documentada, así como un howto.txt para levantar la aplicación incluído en el repositorio.
* Las entregas deberán realizarse el día pactado para la misma antes de las 19 Hs. con un tag llamado Entrega_XX correspondiente al número de entrega.
* Las entregas se realizarán indicando el link al repositorio GIT y el tag designado para la entrega.
* La aplicación debe ser capaz de correrse utilizando el comando mvn jetty:run o similar, a definir por el equipo y especificar en el documento howto.txt.
* Todo retraso en una entrega que no haya sido correctamente comunicado y justificado tendrá como penalización el agregado de nuevos requisitos para la aprobación final del TP.


##User Stories:


* ~~Como usuario quiero poder registrarme con mi cuenta de Facebook.~~
* ~~Como usuario quiero poder publicar un item, buscando y seleccionando un artículo publicado en Mercado Libre a modo de referencia.~~ [API]
* ~~Como usuario quiero ver los items publicados por mis amigos con su descripción e imagen correspondiente.~~ [API]
* Como usuario quiero enviar a un amigo una solicitud de trueque indicando un item publicado por él y uno publicado por mi. 
* Como usuario quiero poder aceptar o rechazar una solicitud de trueque que me haya sido enviada. 
* Como usuario quiero compartir la creación de un ítem en Facebook 
* Como usuario quiero compartir un trueque aceptado en mi muro de Facebook.
* Como usuario quiero ver las solicitudes de trueque que me fueron enviadas.
* Como usuario quiero retirar de circulación un item que cree. [API]
* Como usuario quiero recibir una notificación en Facebook cuando alguien acepte un trueque que propuse.


##Entregas:


#Entrega 1 - Basis (2 semanas)

* ~~Esqueleto de la aplicación WEB.~~
* ~~Se debe definir un primer approach hacia los recursos y URLs REST que se utilizarán para cumplir con las historias propuestas. Para esta entrega no es necesario que las historias funcionen sino que los recursos devuelvan respuestas ficticias estáticas.~~

#Entrega 2 - App volátil (3 semanas)

* ~~Funcionalidad principal sin diferenciar usuarios y persistiendo en memoria.~~
* ~~Se debe definir el comportamiento de los principales servicios relacionados al dominio (no sociales) y cumplir con la funcionalidad persistiendo en memoria. La integración principal con la API de Mercado Libre debe estar disponible en esta entrega~~

#Entrega 3 - Social (2 semanas)

* ~~Integración principal con Facebook.~~
* ~~Se debe realizar la primera integración con facebook. Login e interacción con amigos.~~

#Entrega 4 - GAE (3 semanas)

* ~~Persistencia utilizado Google App Engine~~
* ~~Se debe modificar la aplicación para que en vez de almacenar los datos en memoria, la misma lo haga utilizando Google App Engine.~~

#Entrega 5 - Final (2 semanas)

* Entrega final del TP. Cerrar historias que no hayan sido atacadas + bonus.
* Se deben implementar todas las historias propuestas en el backlog. A su vez se coordinará con el ayudante la inclusión de una o más historias extra.