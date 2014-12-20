/*global _*/
var PbMeasure = {
	getId : function(measure) {
		return measure.id;
	},
	getSbp : function(measure) {
		return measure.pressureBlood.sbp;
	},
	getDbp : function(measure) {
		return measure.pressureBlood.dbp;
	},
	getHand : function(measure) {
		return measure.hand;
	},
	getPulse : function(measure) {
		var pulse = measure.pulse;
		if (_.isUndefined(pulse)) {
			pulse = "";
		}
		return pulse;
	},
	getDatetime : function(measure) {
		return measure.datetime;
	},
	getRemoveLink : function(measure) {
		var removeLink = "<a id=\"" + PbMeasure.getId(measure)
				+ "\" href=\"\">Delete measure</a>";
		return removeLink;
	}
};