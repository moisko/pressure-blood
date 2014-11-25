var LocalDateTime = {
	parse : function(dateTimeString) {
		var splittedLocalDateTime = dateTimeString.split(" ");
		var date = splittedLocalDateTime[0];
		var splittedDate = date.split("/");
		var date = splittedDate[0];
		var month = splittedDate[1];
		var year = splittedDate[2];

		var time = splittedLocalDateTime[1];
		var splittedTime = time.split(":");
		var hh = splittedTime[0];
		var mm = splittedTime[1];

		function decreaseMonth(month) {
			var m = parseInt(month, 10);
			return --m;
		}

		var dateTimeInMillis = new Date(year, decreaseMonth(month), date, hh,
				mm);
		return dateTimeInMillis.getTime();
	},

	toLocalDateTimeString : function(dateTimeInMillis) {
		var d = new Date(dateTimeInMillis);
		var date = d.getDate();
		var month = d.getMonth();
		var fullYear = d.getFullYear();
		var hh = d.getHours();
		var mm = d.getMinutes();

		increaseMonth = function(month) {
			var m = parseInt(month, 10);
			return ++m;
		}

		var localDateTimeString = date + "/" + increaseMonth(month) + "/"
				+ fullYear + " " + hh + ":" + mm;
		return localDateTimeString;
	}
}