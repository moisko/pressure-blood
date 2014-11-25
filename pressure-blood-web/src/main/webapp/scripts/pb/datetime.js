var Datetime = {
	toMillis : function(datetimeString) {
		var splittedDatetime = datetimeString.split(" ");
		var date = splittedDatetime[0];
		var splittedDate = date.split("/");
		var date = splittedDate[0];
		var month = splittedDate[1];
		var year = splittedDate[2];

		var time = splittedDatetime[1];
		var splittedTime = time.split(":");
		var hh = splittedTime[0];
		var mm = splittedTime[1];

		var datetimeInMillis = new Date(year, Datetime.decreaseMonth(month),
				date, hh, mm);
		return datetimeInMillis.getTime();
	},

	decreaseMonth : function(month) {
		var m = parseInt(month, 10);
		return --m;
	},

	increaseMonth : function(month) {
		var m = parseInt(month, 10);
		return ++m;
	},

	toString : function(datetimeInMillis) {
		var d = new Date(datetimeInMillis);
		var date = d.getDate();
		var month = d.getMonth();
		var fullYear = d.getFullYear();
		var hh = d.getHours();
		var mm = d.getMinutes();
		var datetimeString = date + "/" + Datetime.increaseMonth(month) + "/"
				+ fullYear + " " + hh + ":" + mm;
		return datetimeString;
	}
}