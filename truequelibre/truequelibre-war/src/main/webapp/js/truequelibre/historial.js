
		function getTrades(uid){
		
		$.getJSON('/api/pendingTrades', { state : 1 } , function (data) {
				console.log(data);
				var itemBoxSize;
				$('#tradeList').empty();
				data.receivedTradeRequests.forEach(function(trade){
					$('#tradeList').append('<!-- Trade --\> \
									  <div class="msg-time-chat"> \
										  <a href="#" class="message-img"><img class="avatar" src="http://graph.facebook.com/'+trade.offeredItem.owner+'/picture?type=small" alt=""></a> \
										  <div class="message-body msg-out"> \
											  <span class="arrow"></span> \
											  <div class="text" style="background-color:lightsteelblue"> \
												  <p class="attribution"><a href="profile.html?id='+trade.offeredItem.owner+'">'+trade.offeredItem.ownerName+'</a> el '+getFormattedDate(trade.dateCreated)+'</p> \
												  <p>Aceptaste cambiar su <a href="javascript:showItemModal('+trade.offeredItem.itemId+','+trade.offeredItem.owner+')">"'+trade.offeredItem.name+'"</a>por tu <a href="javascript:showItemModal('+trade.wantedItem.itemId+','+trade.wantedItem.owner+')">"'+trade.wantedItem.name+'"</a></p> \
													<div class="pro-thumb" style="margin:20px"><img src="'+trade.offeredItem.img+'" alt="" style="width: 80px;height:80px;"> \
													</div> \
													<button id="showTradeLink" class="btn btn-info" onclick="javascript:shareTradeOnFb(\''+trade.id+'\',false);" style="margin-left: 19%;"><i class="icon-facebook"></i> Compartir</button> \
													<div class="pro-thumb pull-right" style="margin:20px"><img src="'+trade.wantedItem.img+'" alt="" style="width: 80px;height:80px;"> \
													</div> \
											  </div> \
										  </div> \
									  </div> \
									  <!-- /Trade --\>');
				});
				$('#tradeList2').empty();
				data.sentTradeRequests.forEach(function(trade){
					$('#tradeList2').append('<!-- Trade --\> \
									  <div class="msg-time-chat"> \
										  <a href="#" class="message-img"><img class="avatar" src="http://graph.facebook.com/'+trade.wantedItem.owner+'/picture?type=small" alt=""></a> \
										  <div class="message-body msg-in"> \
											  <span class="arrow"></span> \
											  <div class="text" style="background-color:silver"> \
												  <p class="attribution"><a href="profile.html?id='+trade.wantedItem.owner+'">'+trade.wantedItem.ownerName+'</a> el '+getFormattedDate(trade.dateCreated)+'</p> \
												  <p>Aceptó cambiar tu <a href="javascript:showItemModal('+trade.offeredItem.itemId+','+trade.offeredItem.owner+')">"'+trade.offeredItem.name+'"</a>por su <a href="javascript:showItemModal('+trade.wantedItem.itemId+','+trade.wantedItem.owner+')">"'+trade.wantedItem.name+'"</a></p> \
													<div class="pro-thumb" style="margin:20px"><img src="'+trade.offeredItem.img+'" alt="" style="width: 80px;height:80px;"> \
													</div> \
													<button id="showTradeLink" class="btn btn-info" onclick="javascript:shareTradeOnFb(\''+trade.id+'\',true);" style="margin-left: 19%;"><i class="icon-facebook"></i> Compartir</button> \
													<div class="pro-thumb pull-right" style="margin:20px"><img src="'+trade.wantedItem.img+'" alt="" style="width: 80px;height:80px;"> \
													</div> \
											  </div> \
										  </div> \
									  </div> \
									  <!-- /Trade --\>');
				});
				
				
                
        })
		$.getJSON('/api/pendingTrades', { state : 2 } , function (data) {
				console.log(data);
				var itemBoxSize;
				$('#tradeList3').empty();
				data.receivedTradeRequests.forEach(function(trade){
					$('#tradeList3').append('<!-- Trade --\> \
									  <div class="msg-time-chat"> \
										  <a href="#" class="message-img"><img class="avatar" src="http://graph.facebook.com/'+trade.offeredItem.owner+'/picture?type=small" alt=""></a> \
										  <div class="message-body msg-out"> \
											  <span class="arrow"></span> \
											  <div class="text" style="background-color:lightsteelblue"> \
												  <p class="attribution"><a href="profile.html?id='+trade.offeredItem.owner+'">'+trade.offeredItem.ownerName+'</a> el '+getFormattedDate(trade.dateCreated)+'</p> \
												  <p>Rechazaste cambiar su <a href="javascript:showItemModal('+trade.offeredItem.itemId+','+trade.offeredItem.owner+')">"'+trade.offeredItem.name+'"</a>por tu <a href="javascript:showItemModal('+trade.wantedItem.itemId+','+trade.wantedItem.owner+')">"'+trade.wantedItem.name+'"</a></p> \
													<div class="pro-thumb" style="margin:20px"><img src="'+trade.offeredItem.img+'" alt="" style="width: 80px;height:80px;"> \
													</div> \
													<button id="showTradeLink" class="btn btn-info" onclick="javascript:shareTradeOnFb(\''+trade.id+'\',false);" style="margin-left: 19%;"><i class="icon-facebook"></i> Compartir</button> \
													<div class="pro-thumb pull-right" style="margin:20px"><img src="'+trade.wantedItem.img+'" alt="" style="width: 80px;height:80px;"> \
													</div> \
											  </div> \
										  </div> \
									  </div> \
									  <!-- /Trade --\>');
				});
				$('#tradeList4').empty();
				data.sentTradeRequests.forEach(function(trade){
					$('#tradeList4').append('<!-- Trade --\> \
									  <div class="msg-time-chat"> \
										  <a href="#" class="message-img"><img class="avatar" src="http://graph.facebook.com/'+trade.wantedItem.owner+'/picture?type=small" alt=""></a> \
										  <div class="message-body msg-in"> \
											  <span class="arrow"></span> \
											  <div class="text" style="background-color:silver"> \
												  <p class="attribution"><a href="profile.html?id='+trade.wantedItem.owner+'">'+trade.wantedItem.ownerName+'</a> el '+getFormattedDate(trade.dateCreated)+'</p> \
												  <p>Rechazó cambiar tu <a href="javascript:showItemModal('+trade.offeredItem.itemId+','+trade.offeredItem.owner+')">"'+trade.offeredItem.name+'"</a>por su <a href="javascript:showItemModal('+trade.wantedItem.itemId+','+trade.wantedItem.owner+')">"'+trade.wantedItem.name+'"</a></p> \
													<div class="pro-thumb" style="margin:20px"><img src="'+trade.offeredItem.img+'" alt="" style="width: 80px;height:80px;"> \
													</div> \
													<button id="showTradeLink" class="btn btn-info" onclick="javascript:shareTradeOnFb(\''+trade.id+'\',true);" style="margin-left: 19%;"><i class="icon-facebook"></i> Compartir</button> \
													<div class="pro-thumb pull-right" style="margin:20px"><img src="'+trade.wantedItem.img+'" alt="" style="width: 80px;height:80px;"> \
													</div> \
											  </div> \
										  </div> \
									  </div> \
									  <!-- /Trade --\>');
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
				getTrades(uid);
                //$('#cargandoModal').modal('toggle');
			})
			
		
		}); 
         
      });
		