
var userId=0;
var token="";

if(getCookie("accessToken")==null){
	window.location = "login.html";
}
  // This is called with the results from from FB.getLoginStatus().
  function statusChangeCallback(response) {
    console.log('statusChangeCallback');
    console.log(response);

    // The response object is returned with a status field that lets the
    // app know the current login status of the person.
    // Full docs on the response object can be found in the documentation
    // for FB.getLoginStatus().
    if (response.status === 'connected') {
      // Logged into your app and Facebook.
	    deleteCookie("accessToken");
		deleteCookie("userID");
		setCookie("accessToken",response.authResponse.accessToken);
		setCookie("userID",response.authResponse.userID);
		userId=response.authResponse.userID;
		token=response.authResponse.accessToken;
      testAPI();
    } else if (response.status === 'not_authorized') {
      // The person is logged into Facebook, but not your app.
      window.location = "login.html";
    } else {
      // The person is not logged into Facebook, so we're not sure if
      // they are logged into this app or not.
      window.location = "login.html";
    }
	fbApiInit = true; //init flag
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
	status	   : true,	
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
      document.getElementById('profile_name').innerHTML = response.name;
    });
	FB.api("/me/picture?width=18&height=20",  function(response) {

        document.getElementById('profile_photo').setAttribute('src', response.data.url);

	});
	loadNotifications();
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
  	FB.api('/'+userId+'/permissions', 'delete',{ access_token : token } ,function(){ });
	window.location= "login.html";
  }
function getUrlParameter(sParam)
{
    var sPageURL = window.location.search.substring(1);
    var sURLVariables = sPageURL.split('&');
    for (var i = 0; i < sURLVariables.length; i++) 
    {
        var sParameterName = sURLVariables[i].split('=');
        if (sParameterName[0] == sParam) 
        {
            return sParameterName[1];
        }
    }
	return null;
}  

function fbEnsureInit(callback) { // fuente: http://stackoverflow.com/questions/3548493/how-to-detect-when-facebooks-fb-init-is-complete
        if(!window.fbApiInit) {
            setTimeout(function() {fbEnsureInit(callback);}, 200);
        } else {
            if(callback) {
                callback();
            }
        }
    }
	
var isMobile = { //mover a commons fuente: http://www.abeautifulsite.net/detecting-mobile-devices-with-javascript/
    Android: function() {
        return navigator.userAgent.match(/Android/i);
    },
    BlackBerry: function() {
        return navigator.userAgent.match(/BlackBerry/i);
    },
    iOS: function() {
        return navigator.userAgent.match(/iPhone|iPad|iPod/i);
    },
    Opera: function() {
        return navigator.userAgent.match(/Opera Mini/i);
    },
    Windows: function() {
        return navigator.userAgent.match(/IEMobile/i);
    },
    any: function() {
        return (isMobile.Android() || isMobile.BlackBerry() || isMobile.iOS() || isMobile.Opera() || isMobile.Windows());
    }
};

function timeConverter(UNIX_timestamp){
 var a = new Date(UNIX_timestamp*1000);
 var months = ['Ene','Feb','Mar','Abr','May','Jun','Jul','Ago','Sep','Oct','Nov','Dic'];
     var year = a.getFullYear();
     var month = months[a.getMonth()];
     var date = a.getDate();
     var hour = a.getHours();
     var min = a.getMinutes();
     var sec = a.getSeconds();
     var time = date + '-' + month + '-' + year; //+ ' ' + hour + ':' + min + ':' + sec ;
     return time;
 }
 
 function getFormattedDate(UNIX_timestamp){
	 var date = new Date(UNIX_timestamp*1000);
	 var months = ['Ene','Feb','Mar','Abr','May','Jun','Jul','Ago','Sep','Oct','Nov','Dic'];
     return date.getDate()+' de '+months[date.getMonth()] +' de '+date.getFullYear()  +' a las '+(date.getHours()<10?('0'+date.getHours()):date.getHours()) + ':' + ((date.getMinutes()<10)?('0'+date.getMinutes()):date.getMinutes());
 }
 
function shareOnFb(itemId){ //fuente: http://www.walkswithme.net/facebook-share-button-with-custom-parameters
	$('#cargandoModal').modal('show');
			$.getJSON('/api/users/'+userId+'/items/'+itemId, function (data) {
					console.log(data);
					var product_name   = 	'¡He agregado un nuevo item en TruequeLibre!';
					var description	   =	'Entra a mi perfil para ver mi nuevo item! TruequeLibre es una plataforma de intercambio directo de artículos libre de impuestos. La más utilizada en LatinoAmérica!';
					var share_image	   =	data.img;
					var share_url	   =	'https://hercules-tacs.appspot.com/profile.html?id='+userId+'&item='+itemId;	
					var share_capt     =    data.name;
					$('#cargandoModal').modal('hide');
					FB.ui({
						method: 'feed',
						name: product_name,
						link: share_url,
						picture: share_image,
						caption: share_capt,
						description: description

					}, function(response){});
					
					
			});
}

function shareTradeOnFb(tradeId, tradeSolicitado){ //fuente: http://www.walkswithme.net/facebook-share-button-with-custom-parameters
	$('#cargandoModal').modal('show');
			$.getJSON('/api/pendingTrades/'+tradeId, function (data) {
					console.log(data);
					var otherPerson;
					var myItem;
					var otherItem;
					if(tradeSolicitado){
						otherPerson= data.trade.wantedItem.ownerName;
						myItem=data.trade.offeredItem.name;
						otherItem=data.trade.wantedItem.name;
					}else{
						otherPerson= data.trade.offeredItem.ownerName;
						myItem=data.trade.wantedItem.name;
						otherItem=data.trade.offeredItem.name;
					}
					var product_name   = 	'¡He intercambiado un nuevo item en TruequeLibre!';
					var description	   =	'He intercambiado a '+otherPerson+' mi "'+myItem+'" por su "'+otherItem+'"!';
					var share_image	   =	data.trade.wantedItem.img;
					var share_url	   =	'https://hercules-tacs.appspot.com/profile.html?id='+userId;	
					var share_capt     =    'TruequeLibre, la plataforma de intercambios más utilizada!';
					$('#cargandoModal').modal('hide');
					FB.ui({
						method: 'feed',
						name: product_name,
						link: share_url,
						picture: share_image,
						caption: share_capt,
						description: description

					}, function(response){});
					
					
			});
}

function loadNotifications(){
	$.getJSON('/api/pendingTrades', function (data) {
				console.log(data);
				$('#header_inbox_bar').find( "span.badge" ).html(data.receivedTradeRequests.length);
				$('#header_inbox_bar').find( "ul" ).empty();
				$('#header_inbox_bar').find( "ul" ).append('<div class="notify-arrow notify-arrow-red"></div> \
                            <li> \
                                <p class="red">Tienes '+data.receivedTradeRequests.length+' pedidos de intercambio</p> \
                            </li>');
				data.receivedTradeRequests.forEach(function(tradeReq){
					$('#header_inbox_bar').find( "ul" ).append('<li> \
					<a href="tradeRequests.html#recibidas"> \
					<span class="photo"><img  src="'+tradeReq.offeredItem.img.slice(0,-5)+'I.jpg"></span> \
					<span class="photo"><img  src="'+tradeReq.wantedItem.img.slice(0,-5)+'I.jpg" style="float: right;"></span> \
					<span class="subject"><span class="from">'+tradeReq.offeredItem.ownerName+'</span></span> \
					<span class="message"></i> '+tradeReq.offeredItem.name+'</span> \
					<span class="message"><i class="icon-exchange" style="margin-left: 48%;"></i></span> \
					<span class="message"></i> '+tradeReq.wantedItem.name+'</span> \
					</a></li><li>');
				});
				$('#header_inbox_bar').find( "ul" ).append('<li> \
                                <a href="tradeRequests.html#recibidas">Ver Todas</a> \
                            </li>');
        })
}	
	