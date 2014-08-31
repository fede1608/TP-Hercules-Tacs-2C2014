package com.hercules.truequelibre.mlsdk;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class Meli {
	public static String apiUrl = "https://api.mercadolibre.com";
	private String accessToken;
	private String refreshToken;
	private Long clientId;
	private String clientSecret;
	private Client http;
	{
		ClientConfig config = new DefaultClientConfig();
		http = Client.create(config);
	}

	public Meli(Long clientId, String clientSecret) {
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		try {
			refreshAccessToken();
		} catch (AuthorizationFailure e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Meli(Long clientId, String clientSecret, String accessToken) {
		this.accessToken = accessToken;
		this.clientId = clientId;
		this.clientSecret = clientSecret;
	}

	public Meli(Long clientId, String clientSecret, String accessToken,
			String refreshToken) {
		this.accessToken = accessToken;
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.refreshToken = refreshToken;
	}

	public String getAccessToken() {
		return this.accessToken;
	}

	public String getRefreshToken() {
		return this.refreshToken;
	}

	public JsonObject get(String path) throws Exception {
		return get(path, new MultivaluedMapImpl());
	}

	private Builder prepareJSON(String path,
			MultivaluedMap<String, String> params) {
		return http
				.resource(
						UriBuilder.fromUri("https://api.mercadolibre.com")
								.build()).path(path).queryParams(params)
				.accept(MediaType.APPLICATION_JSON);
	}

	public JsonObject get(String path, MultivaluedMap<String, String> params)
			throws Exception {
		Builder r = prepareJSON(path, params);

		ClientResponse response = null;
		try {
			response = r.get(ClientResponse.class);
		} catch (Exception e) {
			throw new MeliException(e);
		}
		if (response.getStatus() == 404 && params.containsKey("access_token")
				&& this.hasRefreshToken()) {

			refreshAccessToken();

			params.putSingle("access_token", this.accessToken);
			r = prepareJSON(path, params);

			try {
				response = r.get(ClientResponse.class);
			} catch (Exception e) {
				throw new MeliException(e);
			}
		}
		return new JsonParser().parse(response.getEntity(String.class))
				.getAsJsonObject();
	}

	private void refreshAccessToken() throws AuthorizationFailure {
		MultivaluedMap<String, String> params = new MultivaluedMapImpl();
		params.add("grant_type", "refresh_token");
		params.add("client_id", String.valueOf(this.clientId));
		params.add("client_secret", this.clientSecret);
		params.add("refresh_token", this.refreshToken);
		Builder req = prepareJSON("oauth/token", params);

		parseToken(req);
	}

	public String getAuthUrl(String callback) {
		try {
			return "https://auth.mercadolibre.com.ar/authorization?response_type=code&client_id="
					+ clientId
					+ "&redirect_uri="
					+ URLEncoder.encode(callback, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return "https://auth.mercadolibre.com.ar/authorization?response_type=code&client_id="
					+ clientId + "&redirect_uri=" + callback;
		}
	}

	public void authorize(String code, String redirectUri)
			throws AuthorizationFailure {
		MultivaluedMap<String, String> params = new MultivaluedMapImpl();

		params.add("grant_type", "authorization_code");
		params.add("client_id", String.valueOf(clientId));
		params.add("client_secret", clientSecret);
		params.add("code", code);
		params.add("redirect_uri", redirectUri);

		Builder r = prepareJSON("oauth/token", params);

		parseToken(r);
	}

	private void parseToken(Builder r) throws AuthorizationFailure {
		ClientResponse response = null;
		String responseBody = "";
		try {
			response = r.post(ClientResponse.class);
			responseBody = response.getEntity(String.class);
		} catch (Exception e) {
			throw new AuthorizationFailure(e);
		}

		JsonParser p = new JsonParser();
		JsonObject object;

		try {
			object = p.parse(responseBody).getAsJsonObject();
		} catch (JsonSyntaxException e) {
			throw new AuthorizationFailure(responseBody);
		}

		if (response.getStatus() == 200) {

			this.accessToken = object.get("access_token").getAsString();
			JsonElement jsonElement = object.get("refresh_token");
			this.refreshToken = jsonElement != null ? object.get(
					"refresh_token").getAsString() : null;
		} else {
			throw new AuthorizationFailure(object.get("message").getAsString());
		}

	}

	private boolean hasRefreshToken() {
		return this.refreshToken != null && !this.refreshToken.isEmpty();
	}

	// public Response post(String path, FluentStringsMap params, String body)
	// throws MeliException {
	// BoundRequestBuilder r = preparePost(path, params, body);
	//
	// Response response;
	// try {
	// response = r.execute().get();
	// } catch (Exception e) {
	// throw new MeliException(e);
	// }
	// if (params.containsKey("access_token") && this.hasRefreshToken()
	// && response.getStatusCode() == 404) {
	// try {
	// refreshAccessToken();
	// } catch (AuthorizationFailure e1) {
	// return response;
	// }
	// params.replace("access_token", this.accessToken);
	// r = preparePost(path, params, body);
	//
	// try {
	// response = r.execute().get();
	// } catch (Exception e) {
	// throw new MeliException(e);
	// }
	// }
	// return response;
	// }
	//
	// public Response put(String path, FluentStringsMap params, String body)
	// throws MeliException {
	// BoundRequestBuilder r = preparePut(path, params, body);
	//
	// Response response;
	// try {
	// response = r.execute().get();
	// } catch (Exception e) {
	// throw new MeliException(e);
	// }
	// if (params.containsKey("access_token") && this.hasRefreshToken()
	// && response.getStatusCode() == 404) {
	// try {
	// refreshAccessToken();
	// } catch (AuthorizationFailure e1) {
	// return response;
	// }
	// params.replace("access_token", this.accessToken);
	// r = preparePut(path, params, body);
	//
	// try {
	// response = r.execute().get();
	// } catch (Exception e) {
	// throw new MeliException(e);
	// }
	// }
	// return response;
	// }
	//
	public JsonObject delete(String path, MultivaluedMap<String, String> params)
			throws MeliException {
		Builder r = prepareJSON(path, params);

		ClientResponse response;
		try {
			response = r.get(ClientResponse.class);
		} catch (Exception e) {
			throw new MeliException(e);
		}
		if (params.containsKey("access_token") && this.hasRefreshToken()
				&& response.getStatus() == 404) {
			try {
				refreshAccessToken();

				params.putSingle("access_token", this.accessToken);
				r = prepareJSON(path, params);

				response = r.get(ClientResponse.class);
			} catch (Exception e) {
				throw new MeliException(e);
			}
		}
		return new JsonParser().parse(response.getEntity(String.class))
				.getAsJsonObject();
	}

	// public BoundRequestBuilder head(String path) {
	// return null;
	// }
	//
	// public BoundRequestBuilder options(String path) {
	// return null;
	// }

	public class AuthorizationFailure extends Exception {
		private static final long serialVersionUID = 8688100047490895706L;

		public AuthorizationFailure(String message) {
			super(message);
		}

		public AuthorizationFailure(Throwable cause) {
			super(cause);
		}
	}

	public class MeliException extends Exception {
		public MeliException(Throwable cause) {
			super(cause);
		}

		private static final long serialVersionUID = 7263275678852231779L;
	}
}
