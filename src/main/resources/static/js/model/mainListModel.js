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
			var result = response.result;
			
			if ( !_.isEmpty(result) && !_.isEmpty(result.content) ) {
				_.forEach(result.content, function(data){
					data.isCompleted = (data.status === "COMPLETED");
				});
			}

			s.set({
				list : result.content,
				paginationInfoTuple : {
					pageSize : result.pageable.pageSize,
					pageNumber : result.number + 1,
					totalPages : result.totalPages,
					totalElements : result.totalElements
				}			
			});
			
			return s;
		}
	});
	
	return MainModel;
});