define ( "model/pagination/paginationInfoModel",

		[],

		function(configUtil) {

    "use strict";

	var Model = Backbone.Model.extend ({

		defaults : {
			existTotalCount : "",
			pageSize		: 0,
			pageNumber		: 0,
			totalCount		: 0,
			_totalPageSize	: 0,

			// 페이지 네비게이션설정관련
			_isExistsPrevPage : false,		// 이전페이지 버튼 활성화 여부
			_isExistsNextPage : false,		// 다음페이지 버튼 활성화 여부

		},

		initialize : function () {
			var s = this;
		},

		parse : function( response ) {
			var s = this;
			var result = response;

			try {
				if ( !_.isEmpty(result) ) {
				}
			} catch(e) {
			}


			/**
			 * 페이지전체크기 (1,2,3,4 전체 페이지사이즈)
			 */
			result._totalPageSize = ( !_.isUndefined(result.totalCount) ) ? Math.ceil( result.totalCount / result.pageSize) : 0;


			/**
			 * 페이징 네비게이션에서 이전/다음 활성화 여부
			 */
			result._isExistsPrevPage	= ( !_.isUndefined(result._isExistsPrevPage) ) ? result._isExistsPrevPage : false;
			result._isExistsNextPage	= ( !_.isUndefined(result._isExistsNextPage) ) ? result._isExistsNextPage : false;

			return result;
		}
	});

	return Model;
});