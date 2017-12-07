(function() {
    'use strict';

    angular
        .module('atolyeApp')
        .controller('MaintainInstanceAppDetailController', MaintainInstanceAppDetailController);

    MaintainInstanceAppDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'MaintainInstance', 'Employee', 'Operation', 'Maintenance'];

    function MaintainInstanceAppDetailController($scope, $rootScope, $stateParams, previousState, entity, MaintainInstance, Employee, Operation, Maintenance) {
        var vm = this;

        vm.maintainInstance = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('atolyeApp:maintainInstanceUpdate', function(event, result) {
            vm.maintainInstance = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
