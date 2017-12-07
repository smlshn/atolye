(function() {
    'use strict';

    angular
        .module('atolyeApp')
        .controller('MaintenanceAppDetailController', MaintenanceAppDetailController);

    MaintenanceAppDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Maintenance', 'Company', 'MaintainType'];

    function MaintenanceAppDetailController($scope, $rootScope, $stateParams, previousState, entity, Maintenance, Company, MaintainType) {
        var vm = this;

        vm.maintenance = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('atolyeApp:maintenanceUpdate', function(event, result) {
            vm.maintenance = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
