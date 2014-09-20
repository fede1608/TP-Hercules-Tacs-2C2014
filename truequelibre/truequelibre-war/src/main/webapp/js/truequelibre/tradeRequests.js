function getPendingTrades(){
		
		$.getJSON('/api/pendingTrades', function (data) {
				console.log(data);

				$('#tradeList').empty();
				var requestNumber=0;

				data.sentTradeRequests.forEach(function(trade){
					requestNumber++;
					$('#tradeList').append(
							
							'<!-- Trade start -->'+
							'<section class="panel" id='+trade.id+'>'+
							
							'<header class="panel-heading">'+
	                            'Solicitud de trueque #'+requestNumber+
                       		'</header>'+
							  '<div class="col-lg-12">'+
			                      '<!--offered product info start-->'+
			                      '<section class="panel post-wrap pro-box">'+
			                          '<aside>'+
			                              '<div class="post-info">'+
			                                  '<span class="arrow-pro right"></span>'+
			                                  '<div class="panel-body">'+
			                                      '<h1><strong>oferta realizada</strong> <br> has ofrecido un trueque a '+trade.wantedItem.ownerName+'</h1>'+
			                                      '<div class="desk yellow">'+
			                                          '<h3>'+trade.offeredItem.name+'</h3>'+
			                                      '</div>'+
			                                     
			                                  '</div>'+
			                              '</div>'+
			                          '</aside>'+
			                          '<aside class="post-highlight yellow v-align">'+
			                              '<div class="panel-body text-center">'+
			                                  '<div class="pro-thumb">'+
			                                      '<img src=' + trade.offeredItem.img + 'alt="">'+
			                                  '</div>'+
			                              '</div>'+
			                          '</aside>'+
			                      '</section>'+
			                      '<!--offered product info end-->'+
			                      
			                      
			                      '<!--wanted product info start-->'+
			                      '<section class="panel post-wrap pro-box">'+
			                          '<aside class="post-highlight yellow v-align">'+
			                              '<div class="panel-body text-center">'+
			                                  '<div class="pro-thumb">'+
			                                      '<img src=' + trade.wantedItem.img + 'alt="">'+
			                                  '</div>'+
			                              '</div>'+
			                          '</aside>'+
			                          '<aside>'+
			                              '<div class="post-info">'+
			                                  '<span class="arrow-pro left"></span>'+
			                                  '<div class="panel-body">'+
			                                      '<h1><strong>Quieres este ítem</strong> <br> Espera la respuesta de '+trade.wantedItem.ownerName+'.</h1>'+
			                                      '<div class="desk yellow">'+
			                                          '<h3>'+trade.wantedItem.name+'</h3>'+			                                      
			                                      '</div>'+
			                                      
			                                  '</div>'+
			                              '</div>'+
			                          '</aside>'+
			                      '</section>'+
			                      '<!--wanted product info end-->'+
			                  '</div>'+
			                  '<div class="row">'+
			                  	  '<div class="col-md-10"></div>'+
				                  '<a href="javascript:rejectTrade(\''+trade.id+'\')" type="button" class="btn btn-round btn-danger">Cancelar</button>'+
			                  '</div>'+
			                  
			                '</section>'+
							'<!-- Trade end   -->'	
							
							)
			  			});
                
                $('#tradeList2').empty();
				requestNumber=0;

				data.receivedTradeRequests.forEach(function(trade){
					requestNumber++;
					$('#tradeList2').append(
							
							'<!-- Trade start -->'+
							'<section class="panel" id='+trade.id+'>'+
							
							'<header class="panel-heading">'+
	                            'Solicitud de trueque #'+requestNumber+
                       		'</header>'+
							  '<div class="col-lg-12">'+
			                      '<!--offered product info start-->'+
			                      '<section class="panel post-wrap pro-box">'+
			                          '<aside>'+
			                              '<div class="post-info">'+
			                                  '<span class="arrow-pro right"></span>'+
			                                  '<div class="panel-body">'+
			                                      '<h1><strong>Nueva oferta</strong> <br> '+trade.offeredItem.ownerName+' te ha ofrecido un trueque</h1>'+
			                                      '<div class="desk yellow">'+
			                                          '<h3>'+trade.offeredItem.name+'</h3>'+
			                                      '</div>'+
			                                      
			                                  '</div>'+
			                              '</div>'+
			                          '</aside>'+
			                          '<aside class="post-highlight yellow v-align">'+
			                              '<div class="panel-body text-center">'+
			                                  '<div class="pro-thumb">'+
			                                      '<img src=' + trade.offeredItem.img + 'alt="">'+
			                                  '</div>'+
			                              '</div>'+
			                          '</aside>'+
			                      '</section>'+
			                      '<!--offered product info end-->'+
			                      
			                      
			                      '<!--offered product info start-->'+
			                      '<section class="panel post-wrap pro-box">'+
			                          '<aside class="post-highlight yellow v-align">'+
			                              '<div class="panel-body text-center">'+
			                                  '<div class="pro-thumb">'+
			                                      '<img src=' + trade.wantedItem.img + 'alt="">'+
			                                  '</div>'+
			                              '</div>'+
			                          '</aside>'+
			                          '<aside>'+
			                              '<div class="post-info">'+
			                                  '<span class="arrow-pro left"></span>'+
			                                  '<div class="panel-body">'+
			                                      '<h1><strong>Quieren este ítem</strong> <br> ¿Qué te parece, '+trade.wantedItem.ownerName +'?</h1>'+
			                                      '<div class="desk yellow">'+
			                                          '<h3>'+trade.wantedItem.name+'</h3>'+			                                      
			                                      '</div>'+
			                                      
			                                  '</div>'+
			                              '</div>'+
			                          '</aside>'+
			                      '</section>'+
			                      '<!--offered product info end-->'+
			                  '</div>'+
			                  '<div class="row">'+
			                  	  '<div class="col-md-10"></div>'+
				                  '<a href="javascript:acceptTrade(\''+trade.id+'\')" type="button" class="btn btn-round btn-success">Aceptar</a>'+
				                  '<a href="javascript:rejectTrade(\''+trade.id+'\')" type="button" class="btn btn-round btn-danger">Rechazar</a>'+
			                  '</div>'+
			                  
			                '</section>'+
							'<!-- Trade end   -->'	
							
							)
			  			});
                
                $('#cargandoModal').modal('toggle');
                
                
        })
	}
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
								  	            text: 'Has rechazado el trueque',
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
      $(document).ready(function() {
		  $('#cargandoModal').modal('toggle');
		  getPendingTrades();
         
      });