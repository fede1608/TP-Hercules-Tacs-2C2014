<!DOCTYPE html>
<html>
<head>
<title>Facebook Login JavaScript Example</title>
<meta charset="UTF-8">
<script src="js/truequelibre.js" type="text/javascript"></script>  
</head>
<body>


<!--
  Below we include the Login Button social plugin. This button uses
  the JavaScript SDK to present a graphical Login button that triggers
  the FB.login() function when clicked.
-->

<fb:login-button scope="public_profile,email,user_friends,publish_actions,status_update,publish_stream" onlogin="checkLoginState();">
</fb:login-button>

<div id="status">
</div>

</body>
</html>
