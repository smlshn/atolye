(function() {
    'use strict';

    angular
        .module('atolyeApp')
        .controller('TownAppDetailController', TownAppDetailController);

    TownAppDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Town', 'City'];

    function TownAppDetailController($scope, $rootScope, $stateParams, previousState, entity, Town, City) {
        var vm = this;

        vm.town = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('atolyeApp:townUpdate', function(event, result) {
            vm.town = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
