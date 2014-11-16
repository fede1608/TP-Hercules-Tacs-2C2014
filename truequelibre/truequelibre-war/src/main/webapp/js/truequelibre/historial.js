var app = angular.module( 'historialApp', [] );
app.config( [
	'$compileProvider',
	function( $compileProvider )  {   
		$compileProvider.aHrefSanitizationWhitelist(/^\s*(https?|ftp|mailto|chrome-extension|javascript):/);
        // Angular before v1.2 uses $compileProvider.urlSanitizationWhitelist(...)
    }
    ]);
app.filter("sanitize", ['$sce', function($sce) {
	return function(htmlCode){
		return $sce.trustAsHtml(htmlCode);
	}
}
]);
function historialController($scope) {
	$scope.formatDate=function(UNIX_timestamp){//TODO buscar forma de llamar a una funcion externa
		//getFormattedDate(data);
		var date = new Date(UNIX_timestamp*1000);
		var months = ['Ene','Feb','Mar','Abr','May','Jun','Jul','Ago','Sep','Oct','Nov','Dic'];
		return date.getDate()+' de '+months[date.getMonth()] +' de '+date.getFullYear()  +' a las '+(date.getHours()<10?('0'+date.getHours()):date.getHours()) + ':' + ((date.getMinutes()<10)?('0'+date.getMinutes()):date.getMinutes());

	};
	$scope.shareOnFB= function (tradeId, tradeSolicitado){ //fuente: http://www.walkswithme.net/facebook-share-button-with-custom-parameters
		$('#cargandoModal').modal('show');
		$.getJSON('/api/pendingTrades/'+tradeId, function (data) {
			console.log(data);
			if(data.status==404){
				alert(data.error);
			}else if(data.status==401){
				document.location.reload();
			}
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
$.getJSON('/api/pendingTrades', { state : 'accepted' } , function (data) {
	console.log(data);
	if(data.status==404){
		alert(data.error);
	}else if(data.status==401){
		document.location.reload();
	}
	$scope.receivedTrades=data.receivedTradeRequests;
	$scope.sentTrades=data.sentTradeRequests;    
	$scope.$apply();
})
$.getJSON('/api/pendingTrades', { state : 'declined' } , function (data) {
	console.log(data);
	if(data.status==404){
		alert(data.error);
	}else if(data.status==401){
		document.location.reload();
	}
	$scope.rejectedReceivedTrades=data.receivedTradeRequests;
	$scope.rejectedSentTrades=data.sentTradeRequests;
	$scope.$apply();
	$('#cargandoModal').modal('hide');
})

}

function showItemModal(itemId,uid){
	$('#cargandoModal').modal('show');
	$.getJSON('/api/users/'+uid+'/items/'+itemId, function (data) {
		console.log(data);
		if(data.status==404){
			alert(data.error);
		}else if(data.status==401){
			document.location.reload();
		}
		$('#showItemTitulo').html(data.name);
		$('#showItemImagen').attr("src",data.img);
		$('#showItemFecha').html(timeConverter(data.dateCreated));
		$('#cargandoModal').modal('hide');
		$('#showItemModal').modal('show');
	});
}

$(document).ready(function() {
	$('#cargandoModal').modal('show');
	fbEnsureInit( function(response) {
		var uid= userId;
		$.getJSON('/api/users/'+uid, function (data) {
			console.log(data);
			if(data.status==404){
				alert(data.error);
			}else if(data.status==401){
				document.location.reload();
			}
			document.getElementById('profile_name2').innerHTML = data.name;
			document.getElementById('profile_photo2').setAttribute('src', data.profilePic);
		})
	});  
});
