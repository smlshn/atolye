(function() {
    'use strict';

    angular
        .module('atolyeApp')
        .controller('EmployeeAppDetailController', EmployeeAppDetailController);

    EmployeeAppDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Employee', 'Company'];

    function EmployeeAppDetailController($scope, $rootScope, $stateParams, previousState, entity, Employee, Company) {
        var vm = this;

        vm.employee = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('atolyeApp:employeeUpdate', function(event, result) {
            vm.employee = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
