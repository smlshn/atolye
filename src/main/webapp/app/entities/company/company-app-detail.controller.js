(function() {
    'use strict';

    angular
        .module('atolyeApp')
        .controller('CompanyAppDetailController', CompanyAppDetailController);

    CompanyAppDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Company', 'Town'];

    function CompanyAppDetailController($scope, $rootScope, $stateParams, previousState, entity, Company, Town) {
        var vm = this;

        vm.company = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('atolyeApp:companyUpdate', function(event, result) {
            vm.company = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
