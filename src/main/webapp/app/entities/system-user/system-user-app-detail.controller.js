(function() {
    'use strict';

    angular
        .module('atolyeApp')
        .controller('SystemUserAppDetailController', SystemUserAppDetailController);

    SystemUserAppDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'SystemUser'];

    function SystemUserAppDetailController($scope, $rootScope, $stateParams, previousState, entity, SystemUser) {
        var vm = this;

        vm.systemUser = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('atolyeApp:systemUserUpdate', function(event, result) {
            vm.systemUser = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
