require.config({
	paths : {
		jquery : "jquery/jquery-1.11.1.min",
		underscore : "underscore/underscore-min",
		datatables : "jquery/jquery.dataTables.min",
		datetimepicker : "jquery/jquery.datetimepicker",
		editable : "jquery/jquery.jeditable.mini",
		validate : "jquery/jquery.validate.min",
		statistics : "pb/chart/statistics",
		dictionary : "pb/adt/dictionary",
		measureForm : "pb/form/measure-form",
		registerForm : "pb/form/register-form",
		register : "pb/register",
		measureInput : "pb/plugins/measure-input",
		measuresTable : "pb/table/measures-table",
		pbMeasure : "pb/pb-measure",
		dateBg : "pb/plugins/date-bg",
		datetime : "pb/time/datetime"
	},
	shim : {
		jquery : {
			exports : "$"
		},
		underscore : {
			exports : "_"
		},
		editable : {
			deps : [ "jquery" ],
			exports : "jQuery.editable"
		},
		measureInput : {
			deps : [ "jquery", "editable" ]
		},
		dateBg : {
			deps : [ "jquery" ],
			exports : "jQuery.fn.dataTableExt.oSort"
		},
		datetimepicker : {
			deps : [ "jquery" ]
		},
		validate : {
			deps : [ "jquery" ]
		}
	}
});

require([ "jquery", "underscore", "datatables", "datetimepicker", "validate",
		"statistics", "dictionary", "measureForm", "registerForm", "register",
		"measureInput", "measuresTable", "pbMeasure", "dateBg", "datetime" ],
		function($, _, datatables, datetimepicker, validate, Statistics,
				Dictionary, MeasureForm, RegisterForm, Register, MeasuresTable,
				PbMeasure, LocalDateTime) {

		});