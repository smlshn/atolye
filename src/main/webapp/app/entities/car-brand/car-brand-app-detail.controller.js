(function() {
    'use strict';

    angular
        .module('atolyeApp')
        .controller('CarBrandAppDetailController', CarBrandAppDetailController);

    CarBrandAppDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'CarBrand'];

    function CarBrandAppDetailController($scope, $rootScope, $stateParams, previousState, entity, CarBrand) {
        var vm = this;

        vm.carBrand = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('atolyeApp:carBrandUpdate', function(event, result) {
            vm.carBrand = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
