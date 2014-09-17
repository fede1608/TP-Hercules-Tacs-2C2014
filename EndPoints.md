#Endpoints
http://microformats.org/wiki/rest/urls#Java

* api/users/{userId} IMPLEMENTED - FRONTEND
  - GET: Devuelve información del usuario ‘userId’ si es el propio usuario o un amigo
* api/users/{userId}/items IMPLEMENTED - FRONTEND
  - GET: Devuelve lista de items del usuario según sea sí mismo o amigo del mismo IMPLEMENTED - FRONTEND
  - POST: Crea un item IMPLEMENTED - FRONTEND
* api/users/{userId}/items/{itemId} IMPLEMENTED
  - GET: trae la información del ítem pedido si es suyo o de un amigo IMPLEMENTED
  - DELETE: Elimina el ítem del usuario IMPLEMENTED
  - POST: Solicita trueque, si el usuario que hace el post no es el dueño 
* api/friends IMPLEMENTED - FRONTEND
  - GET: Lista de amigos del usuario ‘userId’ IMPLEMENTED - FRONTEND
* api/pendingTrades IMPLEMENTED - FRONTEND
  - GET: Devuelve 2 listas con los tratos incompletos:
    * receivedTradeRequests: solicitudes de trueque recibidas por parte de los amigos del usuario
    * sentTradeRequests: solicitudes de trueques realizadas por el usuario a uno de sus amigos
* api/pendingTrades/{pendingTradesId}
  - GET: trae la información del trueque
  - DELETE: elimina la solicitud de trueque tanto recibida como enviada.
  - POST: Confirma el trueque si la solicitud era recibida.
* api/search IMPLEMENTED - FRONTEND
  - GET: obtiene una coleccion de items de mercadolibre
* api/feed IMPLEMENTED - FRONTEND
  - GET: obtiene una coleccion de X items de nuestros amigos ordenados por ultima creacion
