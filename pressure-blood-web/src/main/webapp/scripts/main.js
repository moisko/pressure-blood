require.config({
	paths : {
		jquery : "jquery/jquery-1.11.1.min",
		underscore : "underscore/underscore-min",
		datatables : "jquery/jquery.dataTables.min",
		LocalDateTime : "pb/time/LocalDateTime",
		PbMeasure : "pb/PbMeasure",
		Dictionary : "pb/adt/Dictionary",
		MeasuresTable : "pb/table/MeasuresTable",
		MeasureForm : "pb/form/MeasureForm",
		datetimepicker : "jquery/jquery.datetimepicker",
		validate : "jquery/jquery.validate.min",
		editable : "jquery/jquery.jeditable.mini",
		DateBg : "pb/plugins/DateBg",
		MeasureInput : "pb/plugins/MeasureInput",
		async : "require/async/async",
		goog : "require/goog/goog",
		propertyParser : "require/propertyParser/propertyParser",
		Statistics : "pb/chart/Statistics"
	},
	shim : {
		underscore : {
			exports : "_"
		},
		datetimepicker : {
			deps : ["jquery"]
		},
		validate : {
			deps : ["jquery"]
		},
		editable : {
			deps : ["jquery"]
		}
	}
});

require(
		[ "jquery", "underscore", "datatables", "LocalDateTime", "PbMeasure",
				"Dictionary", "MeasuresTable", "MeasureForm", "Statistics",
				"datetimepicker", "validate", "editable", "DateBg", "MeasureInput",
				"goog!visualization,1,packages:[corechart]" ],
		function($, _, datatables, LocalDateTime, PbMeasure, Dictionary, MeasuresTable, MeasureForm, Statistics) {

			Statistics.hideStatisticsHeader();

			// Create dictionary for holding all measures

			var dictionary = new Dictionary();

			// Initialize measures table

			var dataTables = $("#measures-table").dataTable({
				"aaSorting" : [[ 4, "asc" ]],
				"aoColumnDefs" : [
					{"aTargets" : [5],// DELETE column
					"bSortable" : false
					},
					{"aTargets" : [ "datetime-column" ],
					"mRender" : function(dateTimeInMillis) {
									var localDateTimeString = LocalDateTime.toLocalDateTimeString(dateTimeInMillis);
									return localDateTimeString;
								},
					"sType" : "date-bg"
					}
				],
				"fnHeaderCallback" : function(nHead, aData, iStart, iEnd, aiDisplay) {
					nHead.getElementsByTagName("th")[0].innerHTML = (iEnd - iStart) + " Measures";
				},
				"fnDrawCallback": function () {
					$("#measures-table tbody tr td:not(.delete)").editable("/pressure-blood-web/o.updateMeasure", {
						"placeholder" : "Value must be less than or equal to 300",
						"type" : "measure",
						"onblur" : "submit",
						"callback" : function(updatedValue) {

							var td = this;

							function convertUpdatedValueToColumnType(column) {
								switch (column) {
								case 0: // SBP column
								case 1: // DBP column
								case 3: // PULSE column
									return updatedValue = parseInt(updatedValue, 10);
									break;
								case 4: // DATETIME column
									return updatedValue = LocalDateTime.parse(updatedValue);
									break;
								default: // HAND column
									return updatedValue;
									break;
								}
							}


							function updateMeasuresTable() {
								var position = dataTables.fnGetPosition(td),
									row = position[0],
									column = position[1],
									aData = dataTables.fnGetData(row);

								aData[column] = convertUpdatedValueToColumnType(column);
								dataTables.fnUpdate(aData, row);
							}

							function updateDictionary() {
								var id = td.getAttribute("id"),
									tokens = id.split("_"),
									measureProperty = tokens[0],
									measureId = tokens[1];

								dictionary.update(measureId, measureProperty, updatedValue);
							}

							function updateStatistics() {
								var beginIndex = calculateBeginIndex(),
									endIndex = calculateEndIndex(),
									chartData = dictionary.toChartData().splice(beginIndex, endIndex);

								function calculateBeginIndex() {
									var beginIndex = 0;
									return beginIndex;
								}

								function calculateEndIndex() {
									var endIndex = 10;
									if (endIndex > dictionary.count()) {
										endIndex = dictionary.count();
									}
									return endIndex;
								}

								Statistics.drawChart(chartData);
							}

							// Measures table

							updateMeasuresTable();

							// Update dictionary

							updateDictionary();

							// Statistics

							updateStatistics();
						}
					});
				},
				"fnRowCallback" : function(nRow, aData, iStart, iEnd, aiDisplay) {
					var id = $("td a", nRow).attr("id");
					var tdElements = $("td", nRow);
					var index;
					for(index = 0; index < tdElements.length; index++) {
						var tdElement = tdElements[index];
						switch (index) {
						case 0:
							tdElement.setAttribute("id", "sbp_" + id);
							tdElement.setAttribute("class", "edit");
							break;
						case 1:
							tdElement.setAttribute("id", "dbp_" + id);
							tdElement.setAttribute("class", "edit");
							break;
						case 2:
							tdElement.setAttribute("id", "hand_" + id);
							tdElement.setAttribute("class", "edit");
							break;
						case 3:
							tdElement.setAttribute("id", "pulse_" + id);
							tdElement.setAttribute("class", "edit");
							break;
						case 4:
							tdElement.setAttribute("id", "datetime_" + id);
							tdElement.setAttribute("class", "edit");
							break;
						case 5:
							tdElement.setAttribute("class", "delete");
						default:
							break;
						}
					}
				}
			});

			var measuresTable = new MeasuresTable(dataTables, dictionary, 0);
			measuresTable.populateMeasuresTable();

			dataTables.on("page.dt", function(event, oSettings) {

				function updatePageNumber() {
					var pageNumber = Math.ceil(oSettings._iDisplayStart / oSettings._iDisplayLength);
					measuresTable.setPageNumber(pageNumber);
					return pageNumber;
				}

				function updateStatistics(pageNumber) {

					function calculateBeginIndex() {
						var beginIndex = pageNumber * 10;
						return beginIndex;
					}

					function calculateEndIndex() {
						var endIndex = beginIndex + 10;
						if(endIndex > dictionary.count()) {
							endIndex = dictionary.count();
						}
						return endIndex;
					}

					var beginIndex = calculateBeginIndex(),
						endIndex = calculateEndIndex(),
						chartData = dictionary.toChartData().splice(beginIndex, endIndex);

					Statistics.showStatisticsHeader();
					Statistics.drawChart(chartData);
				}

				// Update measures table page number

				var pageNumber = updatePageNumber();

				// Statistics

				updateStatistics(pageNumber);

				event.preventDefault();
			});

			dataTables.delegate("tbody tr td a", "click", function(event) {
				if (confirm("Are you sure you want to delete this measure") == true) {
					var tableRow = $(this).parent().parent();
					measuresTable.deleteMeasure(tableRow);
				}

				event.preventDefault();
			});

			$("#datetimepicker").datetimepicker({
				format : "d/m/Y H:i",
				step : 10
			});

			$("#add-measure-form").submit(function(event) {
				MeasureForm.validateAddMeasureForm();
				if (MeasureForm.isAddMeasureFormValid()) {
					measuresTable.addMeasure(dataTables, dictionary);
				}

				event.preventDefault();
			});
		});