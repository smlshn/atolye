(function() {
    'use strict';

    angular
        .module('atolyeApp')
        .controller('OperationAppDetailController', OperationAppDetailController);

    OperationAppDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Operation', 'Company', 'Car'];

    function OperationAppDetailController($scope, $rootScope, $stateParams, previousState, entity, Operation, Company, Car) {
        var vm = this;

        vm.operation = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('atolyeApp:operationUpdate', function(event, result) {
            vm.operation = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
