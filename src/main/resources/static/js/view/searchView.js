define( "view/searchView",
		
		[
			"common/helper"
		],
		
		function (	Helper	) {
	
	"use strict";

	var View = Backbone.View.extend({
		
		el: $("#top"),
		
		template: Handlebars.compile($("#searchTemplate").html()),
		
		model : "",
		
		initialize: function( options ) {
			var s = this;
			s.render();
		},
		
		events: {
			"click #search" : "search",
			"keyup :input[id='searchArea']"	: "searchEnter",
		},

		/**
		 * 검색 이벤트
		 */
		search : function(e) {
		    var s = this;
		    s.trigger("searchTrigger");
		},
		
		/**
		 * 검색영역 Key 이벤트
		 */
		searchEnter : function(e) {
			var s = this;
			s.trigger("searchTrigger");
		},
		
	    render: function() {
			var s = this;			
			s.$el.html(s.template());
	    }
	});
	
	return View;
});