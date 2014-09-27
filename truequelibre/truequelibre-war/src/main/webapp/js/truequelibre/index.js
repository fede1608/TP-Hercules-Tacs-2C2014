var app = angular.module( 'feedApp', [] );
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

function feedController($scope) {
    $scope.getOwnerName = function(item){
        var name;
        if(item.owner == getCookie("userID")){
            name = '<a href="profile.html">Agregaste</a>'; 
        }
        else{
            name = '<a href="profile.html?id='+item.owner+'">'+item.ownerName + "</a> agregó";
        }
        return name;
    };
    $('#cargandoModal').modal('show');
    $.getJSON('/api/feed', function (data) {
        console.log(data);
        var i = 0;
        var w = 0;
        data.items.forEach(
            function(item){
                w = Math.random();
                item.color="blue";
                if(w > 0.66){
                    item.color = "light-green";
                }
                else{
                    if(w > 0.33){
                        item.color = "purple";
                    }
                }
            });
        $scope.items= data.items;
        $scope.$apply();
        $('#cargandoModal').modal('hide');
    })



}
