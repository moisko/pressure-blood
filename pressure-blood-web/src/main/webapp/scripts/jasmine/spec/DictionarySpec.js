define([ "main/webapp/scripts/jquery/jquery-1.11.1.min",
		"main/webapp/scripts/underscore/underscore-min",
		"main/webapp/scripts/pb/PbMeasure",
		"main/webapp/scripts/pb/adt/Dictionary" ], function($, _, PbMeasure, Dictionary) {

	describe("Dictionary test suite", function() {

		var measure = {
				"id" : 84,
				"datetime" : 1389205440000,
				"hand" : "LEFT_HAND",
				pressureBlood : {
					"sbp" : 120,
					"dbp" : 80
				},
				"pulse" : 70
			},
			measure2 = {
				"id" : 85,
				"datetime" : 1423150260000,
				"hand" : "RIGHT_HAND",
				pressureBlood : {
					"sbp" : 130,
					"dbp" : 80
				},
				"pulse" : 75
			},
			measure3 = {
				"id" : 86,
				"datetime" : 1423588260000,
				"hand" : "RIGHT_HAND",
				pressureBlood : {
					"sbp" : 140,
					"dbp" : 90
				},
				"pulse" : 60
			},
			dictionary = new Dictionary();

		it("testAddMeasureToDictionary", function() {
			dictionary.add(measure.id, measure);
			expect(dictionary.count()).toBe(1);
		});

		it("testDeleteMeasureFromDictionay", function() {
			dictionary.add(measure.id, measure);
			expect(dictionary.count()).toBe(1);
			dictionary.remove(measure.id);
			expect(dictionary.count()).toBe(0);
		});

		it("testAddTwoMeasuresWithSameKeyToDictionary", function() {
			dictionary.add(measure.id, measure);
			expect(dictionary.count()).toBe(1);
			dictionary.add(measure.id, measure);
			expect(dictionary.count()).toBe(1);
		});

		it("testRemoveMeasureFromDictionary", function() {
			dictionary.add(measure.id, measure);
			expect(dictionary.count()).toBe(1);
			dictionary.remove(measure.id);
			expect(dictionary.isEmpty()).toBe(true);
		});

		it("testUpdateMeasureToDictionary", function() {
			dictionary.add(measure.id, measure);
			expect(dictionary.find(measure.id).pulse).toBe(70);
			dictionary.update(measure.id, "pulse", 80);
			expect(dictionary.find(measure.id).pulse).toBe(80);
		});

		it("testClearMeasureFromDictionary", function() {
			dictionary.add(measure.id, measure);
			expect(dictionary.isEmpty()).toBe(false);
			dictionary.clear();
			expect(dictionary.count()).toBe(0);
		});

		it("testToMeasuresData", function() {
			var measures = [ measure, measure2, measure3 ];
			dictionary.initDictionary(measures);
			var measuresData = dictionary.toMeasuresData();
			var firstMeasure = measuresData[0];
			expect(firstMeasure[0]).toBe(120);
			expect(firstMeasure[4]).toBe(1389205440000);
			var secondMeasure = measuresData[1];
			expect(secondMeasure[0]).toBe(130);
			expect(secondMeasure[4]).toBe(1423150260000);
			var thirdMeasure = measuresData[2];
			expect(thirdMeasure[0]).toBe(140);
			expect(thirdMeasure[4]).toBe(1423588260000);
		});

	});

});
