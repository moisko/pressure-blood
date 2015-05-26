/*global define*/
define([ "jquery", "PbMeasure" ], function($, PbMeasure) {

	function Dictionary() {
		this.datastore = [];
	}

	Dictionary.prototype.initDictionary = function(measures) {
		var datastoreRef = this.datastore;
		measures.forEach(function(measure) {
			datastoreRef[PbMeasure.getId(measure)] = measure;
		});
	};

	Dictionary.prototype.add = function(key, value) {
		this.datastore[key] = value;
	};

	Dictionary.prototype.find = function(key) {
		return this.datastore[key];
	};

	Dictionary.prototype.remove = function(key) {
		delete this.datastore[key];
	};

	Dictionary.prototype.update = function(key, propertyName, propertyValue) {
		var measure = this.find(key);
		switch (propertyName) {
		case "sbp":
			measure.pressureBlood.sbp = propertyValue;
			break;
		case "dbp":
			measure.pressureBlood.dbp = propertyValue;
			break;
		case "hand":
			measure.hand = propertyValue;
			break;
		case "pulse":
			measure.pulse = propertyValue;
			break;
		case "datetime":
			measure.datetime = propertyValue;
			break;
		default:
			break;
		}
	};

	Dictionary.prototype.toChartData = function() {
		var chartData = [];
		this.datastore.forEach(function(measure) {
			chartData.push([ PbMeasure.getDatetime(measure),
			                 PbMeasure.getSbp(measure),
			                 PbMeasure.getDbp(measure) ]);
		});
		return chartData.sort(function(a, b) {
			return a[0] - b[0];
		});
	};

	Dictionary.prototype.toMeasuresData = function() {
		var measuresData = [];
		this.datastore.forEach(function(measure) {
			measuresData.push([ PbMeasure.getSbp(measure),
			                    PbMeasure.getDbp(measure),
			                    PbMeasure.getHand(measure),
			                    PbMeasure.getPulse(measure),
			                    PbMeasure.getDatetime(measure),
			                    PbMeasure.getRemoveLink(measure) ]);
		});
		return measuresData.sort(function(a, b) {
			return a[4] - b[4];
		});
	};

	Dictionary.prototype.clear = function() {
		var deleteCount = this.datastore.length;
		this.datastore.splice(0, deleteCount);
	};

	Dictionary.prototype.count = function() {
		var count = 0;
		this.datastore.forEach(function(measure) {
			++count;
		});
		return count;
	};

	Dictionary.prototype.isEmpty = function() {
		return this.count() === 0;
	};

	Dictionary.prototype.spliceChartData = function(beginIndex, endIndex) {
		var splicedChartData = [],
		arrayIndex = 0,
		i;
		for(i = beginIndex; i < endIndex; i++, arrayIndex++) {
			splicedChartData[arrayIndex] = this.datastore[i];
		}
		return splicedChartData;
	};

	return (Dictionary);
});
