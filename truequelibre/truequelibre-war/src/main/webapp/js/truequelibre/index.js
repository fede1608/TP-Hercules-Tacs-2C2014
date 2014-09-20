

    function getOwnerName(item, uid){
    	var name;
    	if(item.owner == uid){
    		name = '<a href="profile.html">Agregaste</a>'; 
    	}
    	else{
    		name = '<a href="profile.html?id='+item.owner+'">'+item.ownerName + "</a> agregó";
    	}
    	return name;
    }
    
    function getAddedItemList(uid){
    	$.getJSON('/api/feed', function (data) {
    		console.log(data);
    		$('#addedItemList').empty();
    		var i = 0;
    		var w = 0;
    		var color = "red";
    		data.items.forEach(
    			function(item){
				i = i + 1;
    			w = Math.random();
				color="light-green";
    			if(w > 0.66){
    				color = "light-green";
    			}
    			else{
    				if(w > 0.33){
    					color = "purple";
    				}
    			}
    			 
				$('#addedItemList').append(
						'<article class="timeline-item'+(i % 2?"":" alt")+'">\
							<div class="timeline-desk"> \
								<div class="panel"> \
									<div class="panel-body">\
										<span class="arrow'+(i % 2?"":"-alt")+'"></span>\
										<span class="timeline-icon '+color+'"></span>\
										<span class="timeline-date">'+getFormattedDate(item.dateCreated)+'</span>\
										<div class="panel-body"> \
											<div class="task-thumb pro-thumb"> \
												<img src="'+item.img+'" > \
											</div>\
											<div class="task-thumb-details"> \
												<h1 class="'+color+'">'+getOwnerName(item, uid)+' un nuevo item:</h1> \
												<a href="profile.html?id='+item.owner+'&item='+item.itemId+'">'+item.name+'</a> \
											</div>\
										</div> \
									</div> \
								</div> \
							</div>\
						</article>');
    		});
			$('#cargandoModal').modal('hide');
    	})
    }

    $(document).ready(function() {
    	$('#cargandoModal').modal('show');
    	fbEnsureInit( function(response) {    		
			getAddedItemList(userId);
    	}); 

    });