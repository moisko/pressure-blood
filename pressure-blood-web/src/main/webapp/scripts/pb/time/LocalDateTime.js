/*global define*/
define(function() {
    return {
        parse : function(dateTimeString) {
            var splittedLocalDateTime = dateTimeString.split(" "),
                localDate = splittedLocalDateTime[0],
                splittedDate = localDate.split("/"),
                date = splittedDate[0],
                month = splittedDate[1],
                year = splittedDate[2],

                time = splittedLocalDateTime[1],
                splittedTime = time.split(":"),
                hh = splittedTime[0],
                mm = splittedTime[1],
                dateTimeInMillis = new Date(year, decreaseMonth(month), date, hh, mm);

            function decreaseMonth(month) {
                var m = parseInt(month, 10);
                return --m;
            }

            return dateTimeInMillis.getTime();
        },

        toLocalDateTimeString : function(dateTimeInMillis) {
        	var d = new Date(dateTimeInMillis),
            	date = d.getDate(),
            	month = d.getMonth(),
            	fullYear = d.getFullYear(),

            	hh = d.getHours(),
            	mm = d.getMinutes(),
            	localDateTimeString = formatDate(date) + "/"
                	+ formatMonth(increaseMonth(month)) + "/" + fullYear + " "
                	+ formatHour(hh) + ":" + formatMinute(mm);

            function formatMinute(minute) {
                if (minute >= 0 && minute <= 9) {
                    minute = "0" + minute;
                }
                return minute;
            }

            function formatHour(hour) {
                if (hour >= 0 && hour <= 9) {
                    hour = "0" + hour;
                }
                return hour;
            }

            function formatDate(date) {
                if (date >= 1 && date <= 9) {
                    date = "0" + date;
                }
                return date;
            }

            function formatMonth(month) {
                if (month >= 1 && month <= 9) {
                    month = "0" + month;
                }
                return month;
            }

            function increaseMonth(month) {
                var m = parseInt(month, 10);
                return ++m;
            }

            return localDateTimeString;
        }
    };
});