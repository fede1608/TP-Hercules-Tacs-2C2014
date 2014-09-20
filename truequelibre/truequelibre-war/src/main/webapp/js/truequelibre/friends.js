
		function getFriends(){
		
		$.getJSON('/api/friends', function (data) {
				console.log(data);
				//var itemBoxSize= ($('#panel').width()-80)/4;
				$('#friendList').empty();
				data.friends.forEach(function(friend){
					$('#friendList').append('<!--follower start--\>'+
							'<div class="col-md-3">'+
                              '<section class="panel">'+
                                  '<div class="follower">'+
                                      '<div class="panel-body">'+
                                          '<h4>'+friend.name+'</h4>'+
                                          '<div class="follow-ava">'+
                                              '<img src="'+friend.profilePic+'" alt="">'+
                                          '</div>'+
                                      '</div>'+
                                  '</div>'+
                                  '<footer class="follower-foot">'+
                                      '<a href="/profile.html?id='+friend.id+'" class="btn btn-shadow btn-info" style="width:70%;margin-left:15%;margin-right:14%;margin-top: 5px;margin-bottom: 10px;">Items</button>'+
                                  '</footer>'+
                              '</section>'+                        
                          '</div>'+
						  '<!--follower end--\>');
				});
                $('#cargandoModal').modal('toggle');
        })
	}
	
      $(document).ready(function() {
		  debugger;
		  $('#cargandoModal').modal('toggle');
		  getFriends();
         
      });