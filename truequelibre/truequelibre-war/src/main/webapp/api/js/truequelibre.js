
var userId=0;
var token="";

  // This is called with the results from from FB.getLoginStatus().
  function statusChangeCallback(response) {
    console.log('statusChangeCallback');
    console.log(response);
    deleteCookie("accessToken");
    deleteCookie("userID");
    setCookie("accessToken",response.authResponse.accessToken);
    setCookie("userID",response.authResponse.userID);
    userId=response.authResponse.userID;
    token=response.authResponse.accessToken;
    // The response object is returned with a status field that lets the
    // app know the current login status of the person.
    // Full docs on the response object can be found in the documentation
    // for FB.getLoginStatus().
    if (response.status === 'connected') {
      // Logged into your app and Facebook.
      testAPI();
    } else if (response.status === 'not_authorized') {
      // The person is logged into Facebook, but not your app.
      document.getElementById('status').innerHTML = 'Please log ' +
        'into this app.';
    } else {
      // The person is not logged into Facebook, so we're not sure if
      // they are logged into this app or not.
      document.getElementById('status').innerHTML = 'Please log ' +
        'into Facebook.';
    }
  }

  // This function is called when someone finishes with the Login
  // Button.  See the onlogin handler attached to it in the sample
  // code below.
  function checkLoginState() {
    FB.getLoginStatus(function(response) {
      statusChangeCallback(response);
      
    });
    document.location.reload();
  }

  window.fbAsyncInit = function() {
  FB.init({
    appId      : '595790490538541',
    cookie     : true,  // enable cookies to allow the server to access 
                        // the session
    xfbml      : true,  // parse social plugins on this page
    version    : 'v2.1' // use version 2.1
  });

  // Now that we've initialized the JavaScript SDK, we call 
  // FB.getLoginStatus().  This function gets the state of the
  // person visiting this page and can return one of three states to
  // the callback you provide.  They can be:
  //
  // 1. Logged into your app ('connected')
  // 2. Logged into Facebook, but not your app ('not_authorized')
  // 3. Not logged into Facebook and can't tell if they are logged into
  //    your app or not.
  //
  // These three cases are handled in the callback function.

  FB.getLoginStatus(function(response) {
    statusChangeCallback(response);
  });

  };

  // Load the SDK asynchronously
  (function(d, s, id) {
    var js, fjs = d.getElementsByTagName(s)[0];
    if (d.getElementById(id)) return;
    js = d.createElement(s); js.id = id;
    js.src = "//connect.facebook.net/en_US/sdk.js";
    fjs.parentNode.insertBefore(js, fjs);
  }(document, 'script', 'facebook-jssdk'));

  // Here we run a very simple test of the Graph API after login is
  // successful.  See statusChangeCallback() for when this call is made.
  function testAPI() {
    console.log('Welcome!  Fetching your information.... ');
    debugger;
    FB.api('/me', function(response) {
      console.log('Successful login for: ' + response.name);
      console.log(response);
      debugger;
      document.getElementById('status').innerHTML =
        '' + response.name + '!'+ '<br> <a href="#" onClick="logout();">Logout</a>';
      document.getElementById('form').setAttribute('action', '/api/users/'+userId+'/items');
      document.getElementById('items').setAttribute('href', '/api/users/'+userId+'/items');
    });
  }
  
  function setCookie(cname, cvalue) {
	  document.cookie = cname + "=" + cvalue;
	}
  function getCookie(cname) {
	    var name = cname + "=";
	    var ca = document.cookie.split(';');
	    for(var i=0; i<ca.length; i++) {
	        var c = ca[i];
	        while (c.charAt(0)==' ') c = c.substring(1);
	        if (c.indexOf(name) != -1) return c.substring(name.length,c.length);
	    }
	    return "";
	}
  function deleteCookie(name) {
	    document.cookie = name + '=;expires=Thu, 01 Jan 1970 00:00:01 GMT;';
	};
  function logout() { 
  	var id=0;
  	var pageAccessToken='';
    FB.getLoginStatus(function(response) {
	  if (response.status === 'connected') {
	    pageAccessToken = response.authResponse.accessToken;
	    id = response.authResponse.userID; 
	  } 
 	});
 	console.log('id: ' + id+ ' token:' + pageAccessToken);
  	FB.api('/'+id+'/permissions', 'delete',{ access_token : pageAccessToken } ,function(){document.location.reload();})
  }

