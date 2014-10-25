function Dictionary() {
	this.datastore = [];
	this.add = add;
	this.find = find;
	this.remove = remove;
	this.toDataTable = toDataTable;
	this.clear = clear;
	this.count = count;
	this.isEmpty = isEmpty;
}

function add(key, value) {
	this.datastore[key] = value;
}

function find(key) {
	return this.datastore[key];
}

function remove(key) {
	delete this.datastore[key];
}

function toDataTable() {
	var data = [];
	this.datastore.forEach(function(measure) {
		data.push([ PbMeasure.getDatetime(measure), PbMeasure.getSbp(measure),
				PbMeasure.getDbp(measure) ]);
	});
	return data.sort();
}

function clear() {
	this.datastore.forEach(function(measure) {
		delete this.datastore[measure.id];
	});
}

function count() {
	var n = 0;
	this.datastore.forEach(function(measure) {
		++n;
	});
	return n;
}

function isEmpty() {
	return this.count() == 0;
}