define( "view/mainView",
		
		[
			"common/helper",
			"model/mainListModel"
		],
		
		function (	Common,
					MainListModel) {
	
	"use strict";

	var View = Backbone.View.extend({
		
		el: $("#container"),
		
		template: Handlebars.compile($("#mainTemplate").html()),
		
		model : new MainListModel(),
		
		selectedModel : null,

		initialize: function( options ) {
			var s = this;
			s.render();
		},
		
		events: {
		  "click #content"	: "selction",
		  "click #action"	: "action",
		  "click #cancel"	: "cancel",
		  "click #remove"	: "remove",
		  "click ._check" 	: "complete",
		  "keydown :input[id='inputArea']"	: "enter"
		},
		
		/**
		 * 일정리스트 조회
		 */
		loadData : function() {
			var s = this;
			
			s.deferred = s.model.fetch({
				xhrFields: {
					withCredentials: true
				},
				success		: function(response, status, jqXHR) {
					console.log(response);
				},
				error : function(response, status, errorThrown) {
					console.log(response);
				}
			});
			
		},

		/**
		 * 일정 신규저장 및 선택일정 수정 Key 이벤트
		 */
		enter : function(e) {
			var s = this;
			var keyCode = (e.keyCode ? e.keyCode : e.which);
			
			if ( keyCode === 13 ) {
				$("#action").trigger("click");
			}
		},
		
		
		/**
		 * 일정 선택
		 */
		selction : function(e) {
			e.preventDefault();
			var s = this;
			var id = $(e.currentTarget).parents("li").attr("data");
			
			s.selectedModel = _.find(s.model.toJSON().get("list"), function (o) {
				return o.id === id; 
			});
			
			if ( !_.isNull(s.selectedModel) ) {
				var contents = s.selectedModel.contents;
				
				// 수정모드
				s.editMode(contents);
			}
		},
		
		/**
		 * 일정수정 취소 
		 */
		cancel : function(e) {
			e.preventDefault();
			var s = this;
			
			// 저장모드
			s.saveMode();
		},
		
		/**
		 * 일정 신규저장 및 선택일정 수정 이벤트
		 */
		action : function(e) {
			e.preventDefault();
			var s = this;
			var contents = $("#inputArea").val();
			
			// 신규저장
			if (s.selectedModel == null) {
				s.deferred = Backbone.ajax({
					xhrFields: {
						withCredentials: true
					},
					url			: "/api/v1/todo",
					type		: "post",
					data		: {
						"contents" : contents
					},
					success		: function(response, status, jqXHR) {
					},
					error : function(response, status, errorThrown) {
					}
				}).always(function(){
					s.render();
				});
			} else {
				// 수정
				s.selectedModel.contents = contents;
				
				s.deferred = Backbone.ajax({
					xhrFields: {
						withCredentials: true
					},
					url			: "/api/v1/todo",
					contentType : "application/json",
					type		: "put",
					data		: JSON.stringify(s.selectedModel),
					success		: function(response, status, jqXHR) {
					},
					error : function(response, status, errorThrown) {
					}
				}).always(function(){
					s.render();
				});
			}
		},
		
		
		/**
		 * 선택일정 삭제 이벤트
		 */
		remove : function(e) {
			e.preventDefault();
			var s = this;
			var id = $(e.currentTarget).parents("li").attr("data");
			
			s.deferred = Backbone.ajax({
				xhrFields: {
					withCredentials: true
				},
				url			: "/api/v1/todo/"+id,
				type		: "delete",
				success		: function(response, status, jqXHR) {
				},
				error : function(response, status, errorThrown) {
				}
			}).always(function(){
				s.render();
			});
		},
		
		/**
		 * 선택일정 상태변경 이벤트 
		 */
		complete : function(e) {
			e.preventDefault();
			var s = this;
			var id = $(e.currentTarget).parents("li").attr("data");

			// 일정상태변경
			s.deferred = Backbone.ajax({
				xhrFields: {
					withCredentials: true
				},
				url			: "/api/v1/complete/"+id,
				type		: "put",
				success		: function(response, status, jqXHR) {
				},
				error : function(response, status, errorThrown) {
				}
			}).always(function(){
				s.render();
			});
			
		},
		
		saveMode: function() {
			var s = this;
			s.selectedModel = null;
			$("#inputArea").val("");
			$("#action").text("Add");
			$("#cancel").hide();
		},
		
		editMode: function(contents) {
			var s = this;
			$("#inputArea").val(contents);
			$("#action").text("Edit");
			$("#cancel").show();
		},
		
	    render: function() {
			var s = this;
			
			s.loadData();
			s.saveMode();
			
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