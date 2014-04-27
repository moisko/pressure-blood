function MeasuresCtrl($scope, $http) {
	$scope.measures = {};

	$http({
		method : 'GET',
		url : '/pressure-blood-web/GetMeasuresInJson'
	}).success(function(data, status, headers, config) {
		$scope.measures = data;
	}).error(function(data, status, headers, config) {
		console.log("error " + status);
	});

	$scope.size = function() {
		return $scope.measures.length;
	}
}
