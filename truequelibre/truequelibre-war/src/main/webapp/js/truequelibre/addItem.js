angular.module( 'searchApp', [] )
.config( [
    '$compileProvider',
    function( $compileProvider )
    {   
        $compileProvider.aHrefSanitizationWhitelist(/^\s*(https?|ftp|mailto|chrome-extension|javascript):/);
        // Angular before v1.2 uses $compileProvider.urlSanitizationWhitelist(...)
    }
]);

function paginate(totalCount){
		$("#pagination").paging(totalCount, {
			format: '< nncnn >',
			perpage: 20, // show 10 elements per page
			lapping: 0, // don't overlap pages for the moment
			page: 1, // start at page, can also be "null" or negative
			onSelect: function (page) {
				paginateItems($('#query').val(),page-1);
				return false;
			},
			onFormat: function (type) {
				switch (type) {
				case 'block': // n and c
					if(this.value==this.page)
						return ' <li class="active"><a href="#">'+ this.value + '</a></li>' ;
					else
						return ' <li><a href="#">'+ this.value + '</a></li>' ;
				case 'next': // >
					return '<li><a href="#">»</a></li>';
				case 'prev': // <
					return '<li><a href="#">«</a></li>';
				}
			}
		});
	}
	
	function paginateItems(query, page){
		$('#cargandoModal').modal('show');
		$.getJSON('/api/search',{limit:20, query: query, page: page}, function (data) {
				showData(data);
                $('#cargandoModal').modal('hide');
        })
	}
	
	function showData(data){
		console.log(data);
		var itemBoxSize;
		if( isMobile.any() ) {
			if(window.innerHeight > window.innerWidth){
				itemBoxSize= ($('#panel').width()-30); //Vertical
			}else{
				itemBoxSize= ($('#panel').width()-80)/4; //Landscape
			}
		}else{
		   itemBoxSize= ($('#panel').width()-80)/4;
		}
		angular.element($("#itemList")).scope().$apply(function(scope){
	        scope.itemBoxSize = itemBoxSize;
	        scope.items = data.search;
	    });
	}
	
	function searchItems(query){
		$('#cargandoModal').modal('show');
		$.getJSON('/api/search',{limit:20, query: query, page: 0}, function (data) {
				$('#query').val(query);
				paginate(data.total);
        })
	}

	
	
	function addItem(id){
		$('#cargandoModal').modal('show');
		$.post( "/api/users/"+userId+"/items", { itemId: id }, function( data ) {
			console.log( data ); 
			$('#cargandoModal').modal('hide');
			if(data.status == 404){
				alert(data.error);
				return;
			}
			$.gritter.add({
            // (string | mandatory) the heading of the notification
            title: '¡Item agregado con éxito!',
            // (string | mandatory) the text inside the notification
            text: '¿Quieres compartir la creación en Facebook? <br><p><a href="javascript:shareOnFb('+data.itemId+')" class="btn btn-info pull-right"><i class="icon-facebook"></i> Compartir</a></p>',
            // (string | optional) the image to display on the left
            image: 'images/avatar-mini2.jpg',
            // (bool | optional) if you want it to fade out on its own or just sit there
            sticky: true,
            // (int | optional) the time you want it to be alive for before fading out
            time: ''
			});
		}, "json");
	}