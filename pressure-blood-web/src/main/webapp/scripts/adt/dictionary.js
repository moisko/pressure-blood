function Dictionary() {
	this.datastore = [];
}

Dictionary.prototype.addAll = function(measures) {
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

Dictionary.prototype.toChartData = function() {
	var chartData = [];
	this.datastore.forEach(function(measure) {
		var oDate = new Date(measure.datetime);
		var date = oDate.getDate();
		var month = oDate.getMonth();
		var fullYear = oDate.getFullYear();
		var hh = oDate.getHours();
		var mm = oDate.getMinutes();
		var formattedDatetime = fullYear + "/" + month + "/" + date + " " + hh
				+ ":" + mm;
		chartData.push([ formattedDatetime, PbMeasure.getSbp(measure),
				PbMeasure.getDbp(measure) ]);
	});
	return chartData.sort(function(a, b) {
		var dateA = new Date(a[0]);
		var dateB = new Date(b[0]);
		return dateA - dateB;
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