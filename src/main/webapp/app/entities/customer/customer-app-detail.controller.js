(function() {
    'use strict';

    angular
        .module('atolyeApp')
        .controller('CustomerAppDetailController', CustomerAppDetailController);

    CustomerAppDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Customer', 'Company', 'Town'];

    function CustomerAppDetailController($scope, $rootScope, $stateParams, previousState, entity, Customer, Company, Town) {
        var vm = this;

        vm.customer = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('atolyeApp:customerUpdate', function(event, result) {
            vm.customer = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
