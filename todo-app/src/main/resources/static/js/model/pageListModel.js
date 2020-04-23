define( "model/pageListModel",
		
		[ "model/pagination/paginationInfoModel" ],
		
		function(PaginationInfoModel) {

	"use strict";
	
	var MainModel = Backbone.Model.extend({
		
		url : "",
		
		defaults : {
			listCollection				: null,		// 조회된 서비스별 리스트 정보
			paginationInfoModel			: null,		// 조회된 PaginationTuple 정보
			paginationInfoCollection	: null		// 하단 페이징 네비게이션 리스트 정보
		},

		
		// 하단에 표현될 페이징 갯수 단위 (ex 5단위로 페이징 숫자를 표현)
		pagingNumberUnit : 5,
		
		
		/**
		 * 페이징 모델 초기화
		 *
		 * @constructor
		 *
		 * @param {object} attribute
		 * @param {object} options					- 페이징을 호출하는 View에서 전달해주는 공통 파라미터 object
		 * @param {number} options.pagingNumberUnit	- 하단에 표현될 페이징 갯수 단위
		 */
		initialize : function ( attribute, options ) {
			var s = this;
			
			s.options = options;
			s.pagingNumberUnit	= options.pagingNumberUnit;
			
		},
		
		parse : function (response) {
			var s = this;
			
			if ( !_.isEmpty( response.result ) ) {
				var list = response.result.content;									// 조회한 결과 리스트
				// 데이터 가공
				_.forEach(list, function(data){
					data.isCompleted = (data.status === "COMPLETED");
					
					var referIdList = [];
					_.forEach(data.referList, function(subData){
						if (!_.isNull(subData)) {
							subData.isCompleted = (subData.status === "COMPLETED");
							referIdList.push(subData.id);
						}
					});
					
					data.referIdList = _.isEmpty(referIdList) ? null : referIdList;
				});
				
				var paginationInfoModel	= {
					pageSize : response.result.pageable.pageSize,
					pageNumber : response.result.number + 1,
					totalPages : response.result.totalPages,
					totalCount : response.result.totalElements
				}
				
				var pageNumberList = [];											// 페이징 리스트
				var pageSize = Number( paginationInfoModel.pageSize );				// 페이지사이즈 (로우 수가 5개씩 보여줄지, 10개씩 보여줄지)
				var totalCount = Number( paginationInfoModel.totalCount );			// 전체데이터수
				var pageNumber = Number( paginationInfoModel.pageNumber );			// 현재페이지넘버
				

				/**
				 * 페이징 시작 숫자 구하기
				 */
				// 페이징 시작 숫자
				var startPageNumber	= 0;
				var remainderNum	= pageNumber % s.pagingNumberUnit;		// 현재페이지 % 페이지사이즈 (로우수)

				if ( remainderNum === 0 ) {							// 나머지 결과 값이 0 인경우
					startPageNumber = pageNumber - s.pagingNumberUnit + 1;
				} else {
					startPageNumber	= pageNumber - ( remainderNum ) + 1;
				}

				// 현재 페이지 숫자가 전체 페이지 사이즈보다 작거나 같으면 첫 페이지이므로 시작숫자는 1이 됨
				if ( pageNumber <= s.pagingNumberUnit ) {
					startPageNumber = 1;
				}

				/**
				 * 페이징 끝 숫자 구하기
				 */
				var endPageNumber	= startPageNumber + s.pagingNumberUnit - 1;		// 페이징 끝 숫자
				var totalPageSize	= Math.ceil( totalCount / pageSize );			// 페이지전체크기
				if ( totalPageSize < endPageNumber ) {								// 페이지 전체크기를 넘지 않도록
					endPageNumber = totalPageSize;
				}

				/**
				 * 페이지 하단 숫자표기 리스트 생성
				 *  - _isCurrentPage	: 현재 보여지는 페이지 번호인지 여부
				 *  - _index			: 하단에 노출되는 페이지 번호
				 */
				for ( var i=startPageNumber; i <= endPageNumber; i++ ) {
					var _isCurrentPage	= false;

					if ( paginationInfoModel.pageNumber === i ) {
						_isCurrentPage = true;
					}

					pageNumberList.push({
						"_isCurrentPage"	: _isCurrentPage,	// 하단의 페이징 View 영역 중 현재 선택된 페이지 여부
						"_index"			: i					// 하단의 페이징 View 영역의 1, 2, 3, 4, 5 ... 의 리스트
					});
				}

				/**
				 * 페이징 네비게이션에서 이전/다음 활성화 여부
				 */
				// 이전페이지 버튼 활성화 여부
				if ( pageNumber <= 1 ) {
					paginationInfoModel._isExistsPrevPage	= false;
					paginationInfoModel._cssPrevButton		= "off";
				} else {
					paginationInfoModel._isExistsPrevPage	= true;
				}

				// 다음페이지 버튼 활성화 여부
				if ( pageNumber === totalPageSize ) {
					paginationInfoModel._isExistsNextPage	= false;
					paginationInfoModel._cssNextButton		= "off";
				} else {
					paginationInfoModel._isExistsNextPage	= true;
				}

				s.set({
					"list"						: list,
					"paginationInfoModel"		: new PaginationInfoModel( paginationInfoModel, { parse : true } ),	// 페이징 정보
					"paginationInfoCollection"	: pageNumberList        											// 하단 페이징 네비게이션 리스트 정보
				});
			} else {
				s.set({
					"list"						: null,
					"paginationInfoModel"		: null,
					"paginationInfoCollection"	: null
				});
			}

			return s;
		}
	});
	
	return MainModel;
	
});