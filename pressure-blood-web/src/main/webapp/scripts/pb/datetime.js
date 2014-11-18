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

		var datetime = new Date(year, month, date, hh, mm);
		return datetime.getTime();
	},

	toString : function(datetimeInMillis) {
		var d = new Date(datetimeInMillis);
		var date = d.getDate();
		var month = d.getMonth();
		var fullYear = d.getFullYear();
		var hh = d.getHours();
		var mm = d.getMinutes();
		var datetimeString = date + "/" + month + "/" + fullYear + " " + hh
				+ ":" + mm;
		return datetimeString;
	}
}