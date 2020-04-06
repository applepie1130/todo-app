define( "model/mainListModel",
		function() {

	"use strict";
	
	var MainModel = Backbone.Model.extend({
		
		url : "",
		
		
		defaults : {},

		
		initialize : function() {
			var s = this;
			s.url = "/api/v1/todo";
		},
		
		
		parse : function(response) {
			var s = this;
			var result = response;
			
			if ( !_.isEmpty(result) ) {
				_.forEach(result, function(data){
					data.isCompleted = (data.status === "COMPLETED");
				});
			}
			
			console.log(result);

			s.set({
				list : result
			});
			
			return s;
		}
	});
	
	return MainModel;
});