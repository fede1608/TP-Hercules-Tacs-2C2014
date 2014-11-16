angular.module( 'pendingTradesApp', [] )
.config( [
    '$compileProvider',
    function( $compileProvider )
    {   
        $compileProvider.aHrefSanitizationWhitelist(/^\s*(https?|ftp|mailto|chrome-extension|javascript):/);
        // Angular before v1.2 uses $compileProvider.urlSanitizationWhitelist(...)
    }
]);

		function acceptTrade(id)
		{
			bootbox.confirm("¿Seguro/a quieres aceptar este trueque?", 
			function(answer)
			{
				if(answer)
				{
					$('#cargandoModal').modal('show');
					$.post( "api/pendingTrades/"+id, {}, 
							function( data ) 
							{
					  			console.log( data ); 
					  			$('#'+id).remove();
					  			$.gritter.add({
					  	            // (string | mandatory) the heading of the notification
					  	            title: 'Felicitaciones!',
					  	            // (string | mandatory) the text inside the notification
					  	            text: 'Has confirmado el trueque',
					  	            // (string | optional) the image to display on the left
					  	          
					  	            // (bool | optional) if you want it to fade out on its own or just sit there
					  	            sticky: true,
					  	            // (int | optional) the time you want it to be alive for before fading out
					  	            time: ''
					  				});
								$('#cargandoModal').modal('hide');
							}				
					);
				}
			}
				);
		}
		
		function rejectTrade(id)
		{
			bootbox.confirm("¿Seguro/a quieres rechazar este trueque?", 
			function(answer)
			{
				if(answer)
				{
					$('#cargandoModal').modal('show');
					$.ajax( 
							"api/pendingTrades/"+id, 
							{ 
							  type:'DELETE', 
							  success: function( data,statusCode, jqXHR ) {
										  console.log( data ); 
										  $('#'+id).remove();
										  $.gritter.add({
								  	            // (string | mandatory) the heading of the notification
								  	            title: 'En otra oportunidad...',
								  	            // (string | mandatory) the text inside the notification
								  	            text: 'Has rechazado/cancelado el trueque',
								  	            // (string | optional) the image to display on the left
								  	          
								  	            // (bool | optional) if you want it to fade out on its own or just sit there
								  	            sticky: true,
								  	            // (int | optional) the time you want it to be alive for before fading out
								  	            time: ''
								  				});
											$('#cargandoModal').modal('hide');
										},
							 error: function( jqXHR,textStatus, message ) {
								  console.log( 'status: '+textStatus+" message: "+message ); 
									$('#cargandoModal').modal('hide');
									alert(message);
								}
							});
				}
			});
		}
	
		$(function(){
		  var hash = window.location.hash;
		  hash && $('ul.nav a[href="' + hash + '"]').tab('show');

		  $('.nav-tabs a').click(function (e) {
			$(this).tab('show');
			var scrollmem = $('body').scrollTop();
			window.location.hash = this.hash;
			$('html,body').scrollTop(scrollmem);
		  });
		});

    function pendingTradesController($scope) {
      $('#cargandoModal').modal('toggle');
      $.getJSON('/api/pendingTrades', function (data) {
      
        console.log(data);
        $scope.sentTradeRequests = data.sentTradeRequests;
        $scope.receivedTradeRequests = data.receivedTradeRequests;
        $scope.$apply();

        $('#cargandoModal').modal('toggle');
      })
    }
