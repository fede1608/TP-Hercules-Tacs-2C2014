

  // This is called with the results from from FB.getLoginStatus().
  function statusChangeCallback(response) {
    console.log('statusChangeCallback');
    console.log(response);
    if (response.status === 'connected') {
      // Logged into your app and Facebook.
      window.location = "index.html";
    } else if (response.status === 'not_authorized') {
      // The person is logged into Facebook, but not your app.
    } else {
      // The person is not logged into Facebook, so we're not sure if
      // they are logged into this app or not.
    }
  }

  // This function is called when someone finishes with the Login
  // Button.  See the onlogin handler attached to it in the sample
  // code below.
  function checkLoginState() {
	debugger;
	window.location = "index.html";
    FB.getLoginStatus(function(response) {
      statusChangeCallback(response);
      
    });
    
  }

  window.fbAsyncInit = function() {
  FB.init({
    appId      : '595790490538541',
    cookie     : true,  // enable cookies to allow the server to access 
                        // the session
	status	   : true,	
    xfbml      : true,  // parse social plugins on this page
    version    : 'v2.1' // use version 2.1
  });
	FB.Event.subscribe('auth.authResponseChange', function(response) 
	{
     if (response.status === 'connected') 
     {
		deleteCookie("accessToken");
		deleteCookie("userID");
		setCookie("accessToken",response.authResponse.accessToken);
		setCookie("userID",response.authResponse.userID);
		userId=response.authResponse.userID;
		token=response.authResponse.accessToken;
         window.location = "index.html";
     }   
     else if (response.status === 'not_authorized') 
    {
        //FAILED
    } else
    {
        //UNKNOWN ERROR. Logged Out
    }
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

function Login()
    {
 
        FB.login(function(response) {
           if (response.authResponse) 
           {
                window.location = "index.html";
            } else 
            {
             console.log('User cancelled login or did not fully authorize.');
            }
         },{scope: 'public_profile,email,user_friends,publish_actions,status_update,publish_stream'});
 
    }
