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
	$.getJSON('/api/pendingTrades', { state : 1 } , function (data) {
		console.log(data);
		$scope.receivedTrades=data.receivedTradeRequests;
		$scope.sentTrades=data.sentTradeRequests;    
		$scope.$apply();
	})
	$.getJSON('/api/pendingTrades', { state : 2 } , function (data) {
		console.log(data);
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
			document.getElementById('profile_name2').innerHTML = data.name;
			document.getElementById('profile_photo2').setAttribute('src', data.profilePic);
		})
	});  
});
