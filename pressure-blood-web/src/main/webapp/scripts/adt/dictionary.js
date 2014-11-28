function Dictionary() {
	this.datastore = [];
}

Dictionary.prototype.initDictionary = function(measures) {
	var datastoreRef = this.datastore;
	measures.forEach(function(measure) {
		datastoreRef[PbMeasure.getId(measure)] = measure;
	});
}

Dictionary.prototype.add = function(key, value) {
	this.datastore[key] = value;
}

Dictionary.prototype.find = function(key) {
	return this.datastore[key];
}

Dictionary.prototype.remove = function(key) {
	delete this.datastore[key];
}

Dictionary.prototype.update = function(key, propertyName, propertyValue) {
	var measure = this.find(key);
	switch (propertyName) {
	case "sbp":
		measure.pressureBlood.sbp = parseInt(propertyValue, 10);
		break;
	case "dbp":
		measure.pressureBlood.dbp = parseInt(propertyValue, 10);
		break;
	case "hand":
		measure.hand = propertyValue;
		break;
	case "pulse":
		measure.pulse = parseInt(propertyValue, 10);
		break;
	case "datetime":
		measure.datetime = LocalDateTime.parse(propertyValue);
		break;
	default:
		break;
	}
}

Dictionary.prototype.toChartData = function() {
	var chartData = [];
	this.datastore.forEach(function(measure) {
		chartData.push([ PbMeasure.getDatetime(measure),
				PbMeasure.getSbp(measure), PbMeasure.getDbp(measure) ]);
	});
	return chartData.sort(function(a, b) {
		return a[0] - b[0];
	});
}

Dictionary.prototype.toMeasuresData = function() {
	var measuresData = [];
	this.datastore.forEach(function(measure) {
		measuresData.push([ PbMeasure.getSbp(measure),
				PbMeasure.getDbp(measure), PbMeasure.getHand(measure),
				PbMeasure.getPulse(measure), PbMeasure.getDatetime(measure),
				PbMeasure.getRemoveLink(measure) ]);
	});
	return measuresData.sort(function(a, b) {
		return a[4] - b[4];
	});
}

Dictionary.prototype.clear = function() {
	this.datastore.forEach(function(measure) {
		delete this.datastore[measure.id];
	});
}

Dictionary.prototype.count = function() {
	var n = 0;
	this.datastore.forEach(function(measure) {
		++n;
	});
	return n;
}

Dictionary.prototype.isEmpty = function() {
	return count() == 0;
}