var measure = {
	"id" : 84,
	"datetime" : 1389205440000,
	"hand" : "LEFT_HAND",
	pressureBlood : {
		"sbp" : 120,
		"dbp" : 80
	},
	"pulse" : 70
};
define([ "main/webapp/scripts/jquery/jquery-1.11.1.min",
		"main/webapp/scripts/underscore/underscore-min",
		"main/webapp/scripts/pb/PbMeasure",
		"main/webapp/scripts/pb/adt/Dictionary" ], function($, _, PbMeasure, Dictionary) {

	describe("Dictionary test suite", function() {

		it("testAddMeasureToDictionary", function() {
			var dictionary = new Dictionary();
			dictionary.add(measure.id, measure);
			expect(dictionary.count()).toBe(1);
		});

		it("testDeleteMeasureFromDictionay", function() {
			var dictionary = new Dictionary();
			dictionary.add(measure.id, measure);
			expect(dictionary.count()).toBe(1);
			dictionary.remove(measure.id);
			expect(dictionary.count()).toBe(0);
		});

		it("testAddTwoMeasuresWithSameKey", function() {
			var dictionary = new Dictionary();
			dictionary.add(measure.id, measure);
			expect(dictionary.count()).toBe(1);
			dictionary.add(measure.id, measure);
			expect(dictionary.count()).toBe(1);
		});

		it("testRemoveMeasureFromDictionary", function() {
			var dictionary = new Dictionary();
			dictionary.add(measure.id, measure);
			expect(dictionary.count()).toBe(1);
			dictionary.remove(measure.id);
			expect(dictionary.isEmpty()).toBe(true);
		});
	});

});
