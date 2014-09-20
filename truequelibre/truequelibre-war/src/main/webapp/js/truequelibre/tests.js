QUnit.test("hello test", function(assert) {
	assert.ok(1 == "1", "Passed!");
});
QUnit.test("search test", function(assert) {
	
	var i = 0;
	
	$.ajax({
		  url: '/api/search',
		  dataType: 'json',
		  async: false, //necesario para obtener el valor y no perderlo en la variable i
		  data: {limit : 20,
				query : "motorola",
				page : 0,
				},
		  success: function callbackFunctionWithData(data){
				i = data.search.length;
		  }
	});	
	
	assert.ok(i == 20, "Expected 20, Actual "+ i);
	
	
});