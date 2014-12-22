/*global $*/
$.editable.addInputType("measure", {
	element : function(settings, original) {

		function createInputTypeControl(id, type) {
			var input;
			switch (type) {
			case "number":
				input = "<input id=\"" + id + "\" type=\"number\" min=\"0\" max=\"300\" size=\"3\" class=\"required\">";
				break;
			case "text":
				input = "<input id=\"" + id + "\" type=\"text\" size=\"12\" class=\"required\">";
				break;
			default:
				break;
			}
			return input;
		}

		function createDropDownControl(id) {
			var dropDown = "<select id=\"" + id + "\"> <option value=\"LEFT_HAND\">LEFT_HAND</option> <option value=\"RIGHT_HAND\">RIGHT_HAND</option> </select>";
			return dropDown;
		}

		function getMeasureProperty(id) {
			var tokens = id.split("_"),
			measureProperty = tokens[0];
			return measureProperty;
		}

		function createControl(id) {
			var control,
				measureProperty = getMeasureProperty(id);
			switch (measureProperty) {
			case "sbp":
			case "dbp":
			case "pulse":
				control = createInputTypeControl(id, "number");
				break;
			case "hand":
				control = createDropDownControl(id);
				break;
			case "datetime":
				control = createInputTypeControl(id, "text");
				break;
			default:
				break;
			}
			return $(control);
		}

		var id = original.getAttribute("id"),
			control = createControl(id);

		$(this).append(control);

		return (control);
	},
	plugin : function(settings, original) {
		var id = original.getAttribute("id");
		if(id.indexOf("datetime", 0) === 0) {
			$("input", this).datetimepicker({
				format : "d/m/Y H:i",
				step : 10
			});
		}
	}
});