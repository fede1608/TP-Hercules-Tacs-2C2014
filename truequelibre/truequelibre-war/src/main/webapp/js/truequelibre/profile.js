
		function getItems(uid){
		
		$.getJSON('/api/users/'+uid+'/items', function (data) {
				console.log(data);
				var itemBoxSize;
				if( isMobile.any() ) {
					if(window.innerHeight > window.innerWidth){
						itemBoxSize= ($('#panel').width()-30);
					}else{
						itemBoxSize= ($('#panel').width()-80)/4;
					}
				}else{
				   itemBoxSize= ($('#panel').width()-80)/4;
				}
				
				$('#itemList').empty();
				data.items.forEach(function(item){
					$('#itemList').append('<div class="col-md-3"><section class="panel"> \
					<div class="pro-img-box" style="width: '+itemBoxSize+'px;height: '+itemBoxSize+'px;text-align: center;"> \
					<img id="'+item.itemId+'" src="'+item.img+'" alt="" style="width: auto;height: '+itemBoxSize+'px;max-width: '+itemBoxSize+'px;"/> \
					<a href="javascript:showItemModal(\''+item.itemId+'\',\''+uid+'\')" class="adtocart"><i class="icon-search"></i></a></div> \
					<div class="panel-body text-center"><h4><a id="'+item.itemId+'" href="#" class="pro-title max-lines-2">'+item.name+'</a></h4><p class="price"> \
					</p></div></section></div>');
				});
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
				if(friendId==null || friendId==userId){
					//share on Fb
					$('#showItemLink').html('<i class="icon-facebook"></i> Compartir')
					$('#showItemLink').attr('href',"javascript:shareOnFb(\'"+data.itemId+"\');")
					$('#showItemModal').find("li").eq( 2 ).html('<a class="btn btn-danger" href="javascript:eliminarItem(\''+data.itemId+'\')"> Eliminar</a>');
				}else{
					$('#showItemLink').html('Solicitar Trade')
					$('#showItemLink').attr('href','javascript:elegirOfferedItem(\''+data.itemId+'\');');
					$('#showItemModal').find("li").eq( 2 ).html();
				}
				$('#cargandoModal').modal('hide');
				$('#showItemModal').modal('show');
		});
	}
	function elegirOfferedItem(itemId){
		$('#showItemModal').modal('hide');
		$('#cargandoModal').modal('show');
		$.getJSON('/api/users/'+userId+'/items', function (data) {
				console.log(data);
				$('#modalItemList').empty();
				data.items.forEach(function(item){
					$('#modalItemList').append('<tr> \
                              <td> \
                                  <img alt="" src="'+item.img+'" style="width:30px"> \
                                  <strong style="overflow:hidden">'+item.name+'</strong>\
                              </td> \
                              <td class="hidden-phone">'+timeConverter(item.dateCreated)+'</td> \
                              <td class="hidden-phone"><a class="btn btn-info" href="javascript:solicitarTrade(\''+item.itemId+'\',\''+itemId+'\')">Elegir</a></td> \
                          </tr>');
				});
				$('#cargandoModal').modal('hide');
				$('#offeredItemModal').modal('show');
		});
	}
	var friendId=null;
	
	function solicitarTrade(offeredItem,wantedItem){
	$('#offeredItemModal').modal('hide');
		$('#cargandoModal').modal('show');
		$.post( "/api/users/"+friendId+"/items/"+wantedItem, { offeredItemId: offeredItem }, function( data ) {
		  console.log( data ); 
			$('#cargandoModal').modal('hide');
			if(data.status == 404){
				alert(data.error);
				return;
			}
			$.gritter.add({
            // (string | mandatory) the heading of the notification
            title: 'Solicitud Enviada',
            // (string | mandatory) the text inside the notification
            text: 'Se ha enviado una solicitud al item "'+data.wantedItem+'" por el item "'+data.offeredItem+'"',
            // (string | optional) the image to display on the left
            image: 'http://graph.facebook.com/'+friendId+'/picture?type=small',
            // (bool | optional) if you want it to fade out on its own or just sit there
            sticky: true,
            // (int | optional) the time you want it to be alive for before fading out
            time: ''
			});
		}, "json");
	}
	
	function eliminarItem(itemId){
		if(confirm("¿Estas seguro de eliminar el item?")){
			$('#showItemModal').modal('hide');
			$('#cargandoModal').modal('show');
			$.ajax({
				url: "/api/users/"+userId+"/items/"+itemId,
				type: 'DELETE',
				dataType: 'json',
				data: '',
				success: function( data ) {
							  console.log( data ); 
								$('#cargandoModal').modal('hide');
								$.gritter.add({
								// (string | mandatory) the heading of the notification
								title: '¡Item eliminado con éxito!',
								// (string | mandatory) the text inside the notification
								text: 'Se ha eliminado el item seleccionado.',
								// (string | optional) the image to display on the left
								image: 'http://graph.facebook.com/'+userId+'/picture?type=small',
								// (bool | optional) if you want it to fade out on its own or just sit there
								sticky: true,
								// (int | optional) the time you want it to be alive for before fading out
								time: ''
								});
								getItems(userId);
						}
			});	
		}
	}
	
	
      $(document).ready(function() {
	  debugger;
	  $('#cargandoModal').modal('show');
 	  fbEnsureInit( function(response) {
		
			var uid=null;
			if(getUrlParameter('id')!=null){
				uid=getUrlParameter('id');
				friendId=uid;
				$('ul.nav-pills').find('li').eq(2).empty();
				$('ul.nav-pills').find('li').eq(1).empty();
			}else{
				uid=userId;
			}
				
			$.getJSON('/api/users/'+uid, function (data) {
				console.log(data);
				document.getElementById('profile_name2').innerHTML = data.name;
				document.getElementById('profile_photo2').setAttribute('src', data.profilePic);
				getItems(uid);
                //$('#cargandoModal').modal('toggle');
			})
			
			if(getUrlParameter('item')!=null){
				showItemModal(getUrlParameter('item'),uid)
			}
		
		}); 
         
      });
		