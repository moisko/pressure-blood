jQuery.extend(jQuery.fn.dataTableExt.oSort, {
	"date-bg-asc" : function(datetimeStringA, datetimeStringB) {
		var datetimeInMilllisA = Datetime.toMillis(datetimeStringA);
		var datetimeInMilllisB = Datetime.toMillis(datetimeStringB);
		return datetimeInMilllisA - datetimeInMilllisB;
	},

	"date-bg-desc" : function(datetimeStringA, datetimeStringB) {
		var datetimeInMilllisA = Datetime.toMillis(datetimeStringA);
		var datetimeInMilllisB = Datetime.toMillis(datetimeStringB);
		return datetimeInMilllisB - datetimeInMilllisA;
	}
});