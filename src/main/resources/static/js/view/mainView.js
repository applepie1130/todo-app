define( "view/mainView",
		
		[
			"model/mainListModel"
		],
		
		function (MainListModel) {
	
	"use strict";

	var View = Backbone.View.extend({
		el: $("#container"),
		
		template: Handlebars.compile($("#mainTemplate").html()),
		
		model : new MainListModel(),

		initialize: function( options ) {
			var s = this;
			
			s.loadData();
			s.render();
			
		},
		
		events: {
		  "click #action" : "action",
		  "click #remove" : "remove",
		  "click #complete" : "complete",
		},
		
		loadData : function() {
			var s = this;
			
			s.deferred = s.model.fetch({
				xhrFields: {
					withCredentials: true
				},
				success: function(response) {
					debugger;
				},
				error: function (response) {
					debugger;
				}
			});
			
		},
		
		action : function(e) {
			e.prevendDefault;
			console.log("action");
		},
		
		remove : function(e) {
			e.prevendDefault;
			console.log("remove");
		},
		
		complete : function(e) {
			e.prevendDefault;
			console.log("complete");
		},
		
	    render: function() {
			var s = this;
			
			$.when( s.deferred ).then(function(){
				s.$el.html( s.template() );
				if ( !_.isEmpty( s.model ) ) {
					s.$el.html(s.template( s.model.toJSON() ));
				}
			});
			
	    }
	});
	
	return View;
});