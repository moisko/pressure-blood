/*global define*/
define(["jquery", "LocalDateTime"], function($, LocalDateTime) {
	$.extend($.fn.dataTableExt.oSort, {
		"date-bg-asc" : function(dateTimeStringA, dateTimeStringB) {
			var dateTimeInMilllisA = LocalDateTime.parse(dateTimeStringA),
				dateTimeInMilllisB = LocalDateTime.parse(dateTimeStringB);
			return dateTimeInMilllisA - dateTimeInMilllisB;
		}
	});
});