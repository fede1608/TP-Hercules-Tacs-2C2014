package com.hercules.truequelibre.helpers;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.FacebookClient.AccessToken;
import com.restfb.Parameter;
import com.restfb.exception.FacebookOAuthException;
import com.restfb.types.FacebookType;
import com.restfb.types.User;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Singleton
 *<p>Obtiene los datos provenientes de Facebook para un usuario</p>
 */
public class FacebookDataCollector {
	static FacebookDataCollector instance = null;

	private FacebookDataCollector() {

	}

	public static FacebookDataCollector getInstance() {
		if (instance == null) {
			createInstance();
		}
		return instance;
	}

	private static void createInstance() {
		if (instance == null) {
			instance = new FacebookDataCollector();
		}
	}
	
	/**
	 * Busca un usuario y devuelve un valor de verdad si se trata del usuario actual o de un contacto amigo
	 * @param facebookToken : Token de facebook para autorizacion de la API
	 * @param requestedUser : Usuario a buscar
	 * @return <b>True</b> si el usuario es el actual o un amigo
	 */
	public boolean informationCanBeShown(String facebookToken,
			String requestedUser) {

		User user = this.findUserWithRest(facebookToken);
		return (this.isTheUser(user, requestedUser) || this.isAFriend(
				facebookToken, requestedUser));

	}

	/**
	 * Devuelve un usuario a partir de un token de acceso
	 * @param accessToken : Token de acceso para la API
	 * @return el usuario solicitado
	 */
	public User findUserWithRest(String accessToken) {

		FacebookClient faceClient = new DefaultFacebookClient(accessToken);

		return faceClient.fetchObject("me", User.class);
	}

	/**
	 * Compara un usuario dado y el ID de un usuario solicitado
	 * @param user : un usuario dado
	 * @param requestedUser : string que representa el ID del usuario solicitado
	 * @return <b>True</b> si el ID es del usuario dado
	 */
	public boolean isTheUser(User user, String requestedUser) {
		return user.getId().equals(requestedUser);
	}

	/**
	 * Compara un token de un usuario y el ID de un usuario solicitado
	 * @param token : token de acceso de un usuario
	 * @param requestedUser : ID del usuario solicitado
	 * @return <b>True</b> si el token y el ID corresponden al mismo usuario
	 */
	public boolean isTheUser(String token, String requestedUser) {
		return findUserWithRest(token).getId().equals(requestedUser);
	}

	/**
	 * Dado el ID de un usuario pregunta si es un contacto amigo del usuario actual
	 * @param facebookAccessToken : token de acceso de Facebook del usuario actual
	 * @param friendId : ID de un usuario
	 * @return <b>True</b> si el usuario es un contacto amigo
	 */
	public boolean isAFriend(String facebookAccessToken, String friendId) {
		FacebookClient facebookClient = new DefaultFacebookClient(
				facebookAccessToken);

		Connection<User> myFriends = facebookClient.fetchConnection(
				"me/friends", User.class, Parameter.with("fields", "id"));
		for (User friend : myFriends.getData()) {
			if (friend.getId().equals(friendId)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Obtiene un mapa de ID y nombre de un usuario y tambien el de sus contactos amigos
	 * @param token : token de acceso del usuario
	 * @return mapa con el formato {@code <String ID de usuario, String nombre de usuario> }
	 */
	public Map<String,String> getFriendsHashMapWithUser(String token){
		User user=FacebookDataCollector.getInstance().findUserWithRest(token);
		Map<String,String> friends=FacebookDataCollector.getInstance().getFriendsAsHashMap(token);
		friends.put(user.getId(), user.getName());
		return friends;
	}
	
	/**
	 * Obtiene los datos de un contacto amigo de un usuario
	 * @param facebookAccessToken : token de acceso del usuario
	 * @param friendId : ID del contacto amigo 
	 * @return Contacto amigo del usuario
	 */
	public User getFriendData(String facebookAccessToken, String friendId) {
		Connection<User> myFriends = this.getFriends(facebookAccessToken);
		for (User friend : myFriends.getData()) {
			if (friend.getId().equals(friendId)) {
				return friend;
			}
		}
		return null;
	}
	
	/**
	 * Obtiene la URI de foto perfil de un usuario
	 * @param userId : ID del usuario
	 * @return string que representa la URI de la foto perfil
	 */
	public String getUserProfilePic( String userId ){
		return "http://graph.facebook.com/"+userId+"/picture?type=large";
	}
	
	/**
	 * Obtiene el ID, nombre, apellido, nombre completo, genero y foto perfil de los contactos amigos del usuario actual que utilicen la aplicacion
	 * @param facebookAccessToken : token de acceso a Facebook del usuario actual
	 * @return lista de amigos del usuario actual que utilizan la aplicacion
	 */
	public Connection<User> getFriends(String facebookAccessToken) {
		Connection<User> myFriends = null;

		final FacebookClient facebookClient = new DefaultFacebookClient(
				facebookAccessToken);

		myFriends = facebookClient
				.fetchConnection("me/friends", User.class, Parameter.with(
						"fields", "id,first_name,last_name,name,gender,picture"));

		return myFriends;
	}

	/**
	 * Obtiene la lista de amigos del usuario actual y los convierte en JSON
	 * @param facebookAccessToken : token de acceso a Facebook del usuario actual
	 * @return JSON convertido a String
	 */
	public String findFacebookFriendsUsingRest(String facebookAccessToken) {
		JsonArray friends = new JsonArray();
		User u = FacebookDataCollector.getInstance().findUserWithRest(
				facebookAccessToken);
		Connection<User> myFriends = FacebookDataCollector.getInstance()
				.getFriends(facebookAccessToken);
		System.out
				.println("Count of my friends: " + myFriends.getData().size());
		for (User friend : myFriends.getData()) {
			System.out.println("Friends id and name: " + friend.getId() + " , "
					+ friend.getName());
			JsonObject thisFriend = new JsonObject();
			thisFriend.addProperty("id", friend.getId());
			thisFriend.addProperty("name", friend.getName());
			thisFriend.addProperty("profilePic", FacebookDataCollector.getInstance().getUserProfilePic(friend.getId()));
			friends.add(thisFriend);
		}
		JsonObject json = new JsonObject();
		json.addProperty("userId", u.getId());
		json.addProperty("friendsCount", myFriends.getData().size());
		json.add("friends", friends);
		return json.toString();
	}

	/**
	 * Obtiene un mapa de ID y nombre de los contactos amigos de un usuario
	 * @param token : token de acceso del usuario
	 * @return mapa de los contactos amigos con el formato {@code <String ID de usuario, String nombre de usuario> }
	 */
	public Map<String, String> getFriendsAsHashMap(String token) {
		Map<String,String> friends = new HashMap<String,String>();
		List<User> friendList=this.getFriends(token).getData();
		for (User friend : friendList) {
			
			if (friends.get(friend.getId())==null) {
			    friends.put(friend.getId(), friend.getName());
			}
		}
		return friends;
	}
	
	/**
	 * Envia una notificacion a un usuario de Facebook
	 * @param externalUserId : ID del usuario
	 * @param message : Mensaje a enviar
	 * @param href : URI relacionada al evento notificado
	 */
	public void sendNotification(String externalUserId, String message, String href) {
	    AccessToken appAccessToken = new DefaultFacebookClient()
	            .obtainAppAccessToken(FbProperties.getInstance().appId, FbProperties.getInstance().appSecret);
	    FacebookClient facebookClient = new DefaultFacebookClient(
	            appAccessToken.getAccessToken());
	    try {
	        facebookClient.publish(externalUserId
	                + "/notifications", FacebookType.class,
	                Parameter.with("template", message),
	                Parameter.with("href", href));
	    } catch (FacebookOAuthException e) {
	        if (e.getErrorCode() == 200) {
	            //Not an app user
	        } else if (e.getErrorCode() == 100) {
	            //Message cannot be longer than 180 characters
	        }
	    }
	}

}
