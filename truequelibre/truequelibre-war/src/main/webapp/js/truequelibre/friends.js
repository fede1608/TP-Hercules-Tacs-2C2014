
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
    $scope.friends = data.friends;
    $scope.$apply();
    $('#cargandoModal').modal('toggle');
  })
}
