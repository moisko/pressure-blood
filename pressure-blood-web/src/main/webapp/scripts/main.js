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
		register : "pb/form/register",
		measureInput : "pb/table/measure-input",
		measuresTable : "pb/table/measures-table",
		pbMeasure : "pb/table/pb-measure",
		dateBg : "pb/time/date-bg",
		datetime : "pb/time/datetime"
	},
	shim : {
		underscore : {
			exports : "_"
		},
		measureInput : {
			deps : [ "jquery" ]
		},
		dateBg : {
			deps : [ "jquery" ]
		},
		editable : {
			deps : [ "jquery" ]
		},
		datetimepicker : {
			deps : [ "jquery" ]
		},
		validate : {
			deps : [ "jquery" ]
		}
	}
});

require([ "jquery", "underscore", "datatables", "datetimepicker", "editable",
		"validate", "statistics", "dictionary", "measureForm", "registerForm",
		"register", "measureInput", "measuresTable", "pbMeasure", "dateBg",
		"datetime" ], function($, _, datetimepicker, editable, validate,
		Statistics, Dictionary, MeasureForm, RegisterForm, Register, editable,
		MeasuresTable, PbMeasure, LocalDateTime) {

});