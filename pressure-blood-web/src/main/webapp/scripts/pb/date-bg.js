jQuery.extend(jQuery.fn.dataTableExt.oSort, {
	"date-bg-asc" : function(datetimeStringA, datetimeStringB) {
		var datetimeInMilllisA = LocalDateTime.parse(datetimeStringA);
		var datetimeInMilllisB = LocalDateTime.parse(datetimeStringB);
		return datetimeInMilllisA - datetimeInMilllisB;
	},

	"date-bg-desc" : function(datetimeStringA, datetimeStringB) {
		var datetimeInMilllisA = LocalDateTime.parse(datetimeStringA);
		var datetimeInMilllisB = LocalDateTime.parse(datetimeStringB);
		return datetimeInMilllisB - datetimeInMilllisA;
	}
});