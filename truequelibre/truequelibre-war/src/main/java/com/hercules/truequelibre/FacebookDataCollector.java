package com.hercules.truequelibre;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.User;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class FacebookDataCollector {
	static FacebookDataCollector instance = null;
	private FacebookDataCollector(){
		
	}
	public static FacebookDataCollector getInstance(){
		if (instance==null){
			createInstance();
		}
		return instance;
	}
	
	private static void createInstance() {
        if (instance == null) { 
            instance = new FacebookDataCollector();
        }
    }
	public boolean informationCanBeShown(String facebookToken,String requestedUser){

		User user = this.findUserWithRest(facebookToken);
		return (this.isTheUser(user,requestedUser) || this.isAFriend(facebookToken, requestedUser));
		
	}
	public User findUserWithRest(String accessToken) {

		FacebookClient faceClient= new DefaultFacebookClient(accessToken);
		
		return faceClient.fetchObject("me",User.class);
	}
	public boolean isTheUser(User user, String requestedUser) {
		return user.getId().equals(requestedUser);
	}
	
	public boolean isTheUser(String token, String requestedUser) {
		return findUserWithRest(token).getId().equals(requestedUser);
	}
	public boolean isAFriend(String facebookAccessToken, String friendId){
		  FacebookClient facebookClient = new DefaultFacebookClient(facebookAccessToken);
		  	 
		  Connection<User> myFriends = facebookClient.fetchConnection("me/friends", User.class,
				  Parameter.with("fields", "id"));
		  for(User friend: myFriends.getData()){
			  if (friend.getId().equals(friendId)){
				  return true; 
			  }
		  }
		  return false;	  
	}
	
	public String getFriendData(String facebookAccessToken, String friendId){
		  Connection<User> myFriends = this.getFriends(facebookAccessToken);
		  String message="";
		   for(User friend: myFriends.getData()){
			  if (friend.getId().equals(friendId)){
				  message += "Informacion de amigo encontrado, es: " + friend.getName();
				  return message; 
			  }else{
				  message += "analizado el usuario " + friend.getId() + " " + friend.getFirstName() + "\n";
			  }
		  }
		  
		  return message + "el usuario pedido no esta entre sus amigos";
	}
	public Connection<User> getFriends(String facebookAccessToken){
		Connection<User> myFriends= null;
		
		final FacebookClient facebookClient = new DefaultFacebookClient(facebookAccessToken);		
		
		myFriends = facebookClient.fetchConnection("me/friends", User.class,
				Parameter.with("fields", "id,first_name,last_name,name,gender"));
		
		return myFriends;
	}
	public String findFacebookFriendsUsingRest(String facebookAccessToken){
		  JsonArray friends = new JsonArray();
		  Connection<User> myFriends = FacebookDataCollector.getInstance().getFriends(facebookAccessToken);
		  System.out.println("Count of my friends: " + myFriends.getData().size()); 
		  for(User friend: myFriends.getData()){
			  System.out.println("Friends id and name: "+friend.getId()+" , "+friend.getName());   
			  JsonObject thisFriend = new JsonObject();
			  thisFriend.addProperty("id", friend.getId());
			  thisFriend.addProperty("name", friend.getName());
			  friends.add(thisFriend);
		  }
		  //recuperacion de articulos obtenidos en api/search	  
		  return friends.toString();
	}
}
	
