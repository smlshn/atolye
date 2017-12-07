(function() {
    'use strict';

    angular
        .module('atolyeApp')
        .controller('CarModelAppDetailController', CarModelAppDetailController);

    CarModelAppDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'CarModel', 'CarBrand'];

    function CarModelAppDetailController($scope, $rootScope, $stateParams, previousState, entity, CarModel, CarBrand) {
        var vm = this;

        vm.carModel = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('atolyeApp:carModelUpdate', function(event, result) {
            vm.carModel = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
