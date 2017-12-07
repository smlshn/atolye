(function() {
    'use strict';

    angular
        .module('atolyeApp')
        .controller('MaintainTypeAppDetailController', MaintainTypeAppDetailController);

    MaintainTypeAppDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'MaintainType'];

    function MaintainTypeAppDetailController($scope, $rootScope, $stateParams, previousState, entity, MaintainType) {
        var vm = this;

        vm.maintainType = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('atolyeApp:maintainTypeUpdate', function(event, result) {
            vm.maintainType = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
