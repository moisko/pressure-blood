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

		formatMinute = function(minute) {
			if (minute >= 0 && minute <= 9) {
				minute = "0" + minute;
			}
			return minute;
		}

		formatHour = function(hour) {
			if (hour >= 0 && hour <= 9) {
				hour = "0" + hour;
			}
			return hour;
		}

		formatDate = function(date) {
			if (date >= 1 && date <= 9) {
				date = "0" + date;
			}
			return date;
		}

		formatMonth = function(month) {
			if (month >= 1 && month <= 9) {
				month = "0" + month;
			}
			return month;
		}

		increaseMonth = function(month) {
			var m = parseInt(month, 10);
			return ++m;
		}

		var localDateTimeString = formatDate(date) + "/"
				+ formatMonth(increaseMonth(month)) + "/" + fullYear + " "
				+ formatHour(hh) + ":" + formatMinute(mm);
		return localDateTimeString;
	}
}