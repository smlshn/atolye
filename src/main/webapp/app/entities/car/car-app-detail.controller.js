(function() {
    'use strict';

    angular
        .module('atolyeApp')
        .controller('CarAppDetailController', CarAppDetailController);

    CarAppDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Car', 'Company', 'CarModel', 'Customer'];

    function CarAppDetailController($scope, $rootScope, $stateParams, previousState, entity, Car, Company, CarModel, Customer) {
        var vm = this;

        vm.car = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('atolyeApp:carUpdate', function(event, result) {
            vm.car = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
