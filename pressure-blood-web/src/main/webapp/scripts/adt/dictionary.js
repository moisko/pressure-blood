function Dictionary() {
	this.datastore = [];
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

Dictionary.prototype.toDataTable = function() {
	var data = [];
	this.datastore.forEach(function(measure) {
		data.push([ moment(PbMeasure.getDatetime(measure)).format("MMM D, YYYY HH:mm:ss"),
					PbMeasure.getSbp(measure),
					PbMeasure.getDbp(measure) ]);
	});
	return data.sort(function(a, b) {
		var datetimeA = new Date(a[0]);
		var datetimeB = new Date(b[0]);
		return datetimeA - datetimeB;
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