/*global jQuery,LocalDateTime*/
jQuery.extend(jQuery.fn.dataTableExt.oSort, {
	"date-bg-asc" : function(dateTimeStringA, dateTimeStringB) {
		var dateTimeInMilllisA = LocalDateTime.parse(dateTimeStringA),
			dateTimeInMilllisB = LocalDateTime.parse(dateTimeStringB);
		return dateTimeInMilllisA - dateTimeInMilllisB;
	},

	"date-bg-desc" : function(dateTimeStringA, dateTimeStringB) {
		var dateTimeInMilllisA = LocalDateTime.parse(dateTimeStringA),
			dateTimeInMilllisB = LocalDateTime.parse(dateTimeStringB);
		return dateTimeInMilllisA - dateTimeInMilllisB;
	}
});