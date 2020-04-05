define( "model/mainListModel",
		function() {

	"use strict";
	
	var MainModel = Backbone.Model.extend({
		
		url : "",
		
		
		defaults : {
		},

		
		initialize : function() {
			var s = this;
			s.url = "/api/v1/todo";
		},
		
		
		parse : function(response) {
			var s = this;
			var result = response;

			s.set({
				list				: response
			});
			
			return s;
		}
	});
	
	return MainModel;
});