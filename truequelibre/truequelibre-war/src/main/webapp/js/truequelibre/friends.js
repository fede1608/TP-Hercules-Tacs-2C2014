
angular.module( 'friendsApp', [] )
.config( [
  '$compileProvider',
  function( $compileProvider )
  {   
    $compileProvider.aHrefSanitizationWhitelist(/^\s*(https?|ftp|mailto|chrome-extension|javascript):/);
        // Angular before v1.2 uses $compileProvider.urlSanitizationWhitelist(...)
      }
      ]);


function friendsController($scope) {
  $('#cargandoModal').modal('toggle');
  $.getJSON('/api/friends', function (data) {
    console.log(data);
    if(data.status==404){
      alert(data.error);
    }else if(data.status==401){
      document.location.reload();
    }
    $scope.friends = data.friends;
    $scope.$apply();
    $('#cargandoModal').modal('toggle');
  })
}
