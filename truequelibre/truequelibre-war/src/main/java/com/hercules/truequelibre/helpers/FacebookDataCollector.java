package com.hercules.truequelibre.helpers;

import java.util.Collection;
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

	public boolean informationCanBeShown(String facebookToken,
			String requestedUser) {

		User user = this.findUserWithRest(facebookToken);
		return (this.isTheUser(user, requestedUser) || this.isAFriend(
				facebookToken, requestedUser));

	}

	public User findUserWithRest(String accessToken) {

		FacebookClient faceClient = new DefaultFacebookClient(accessToken);

		return faceClient.fetchObject("me", User.class);
	}

	public boolean isTheUser(User user, String requestedUser) {
		return user.getId().equals(requestedUser);
	}

	public boolean isTheUser(String token, String requestedUser) {
		return findUserWithRest(token).getId().equals(requestedUser);
	}

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
	
	public Map<String,String> getFriendsHashMapWithUser(String token){
		User user=FacebookDataCollector.getInstance().findUserWithRest(token);
		Map<String,String> friends=FacebookDataCollector.getInstance().getFriendsAsHashMap(token);
		friends.put(user.getId(), user.getName());
		return friends;
	}
	public User getFriendData(String facebookAccessToken, String friendId) {
		Connection<User> myFriends = this.getFriends(facebookAccessToken);
		for (User friend : myFriends.getData()) {
			if (friend.getId().equals(friendId)) {
				return friend;
			}
		}
		return null;
	}
	
	public String getUserProfilePic( String userId ){
		return "http://graph.facebook.com/"+userId+"/picture?type=large";
	}
	
	public Connection<User> getFriends(String facebookAccessToken) {
		Connection<User> myFriends = null;

		final FacebookClient facebookClient = new DefaultFacebookClient(
				facebookAccessToken);

		myFriends = facebookClient
				.fetchConnection("me/friends", User.class, Parameter.with(
						"fields", "id,first_name,last_name,name,gender,picture"));

		return myFriends;
	}

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
		// recuperacion de articulos obtenidos en api/search
		JsonObject json = new JsonObject();
		json.addProperty("userId", u.getId());
		json.addProperty("friendsCount", myFriends.getData().size());
		json.add("friends", friends);
		return json.toString();
	}

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
	public void sendNotification(String externalUserId, String message) {
	    AccessToken appAccessToken = new DefaultFacebookClient()
	            .obtainAppAccessToken(FbProperties.getInstance().appId, FbProperties.getInstance().appSecret);
	    FacebookClient facebookClient = new DefaultFacebookClient(
	            appAccessToken.getAccessToken());
	    try {
	        facebookClient.publish(externalUserId
	                + "/notifications", FacebookType.class,
	                Parameter.with("template", message),
	                Parameter.with("href", "historial.html"));
	    } catch (FacebookOAuthException e) {
	        if (e.getErrorCode() == 200) {
	            //Not an app user
	        } else if (e.getErrorCode() == 100) {
	            //Message cannot be longer than 180 characters
	        }
	    }
	}

}
