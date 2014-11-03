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
    $scope.formatDate=function(UNIX_timestamp){//TODO buscar forma de llamar a una funcion externa
        //getFormattedDate(data);
        var date = new Date(UNIX_timestamp*1000);
        var months = ['Ene','Feb','Mar','Abr','May','Jun','Jul','Ago','Sep','Oct','Nov','Dic'];
        return date.getDate()+' de '+months[date.getMonth()] +' de '+date.getFullYear()  +' a las '+(date.getHours()<10?('0'+date.getHours()):date.getHours()) + ':' + ((date.getMinutes()<10)?('0'+date.getMinutes()):date.getMinutes());

    };
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
        if(data.status==404){
            alert(data.error);
        }
        var w = 0;
        if(data.items !=  'undefined'){
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
        }
        $('#cargandoModal').modal('hide');
    })



}
