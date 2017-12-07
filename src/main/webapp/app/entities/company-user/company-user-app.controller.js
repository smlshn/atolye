(function() {
    'use strict';

    angular
        .module('atolyeApp')
        .controller('CompanyUserAppController', CompanyUserAppController);

    CompanyUserAppController.$inject = ['CompanyUser', 'CompanyUserSearch'];

    function CompanyUserAppController(CompanyUser, CompanyUserSearch) {

        var vm = this;

        vm.companyUsers = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            CompanyUser.query(function(result) {
                vm.companyUsers = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            CompanyUserSearch.query({query: vm.searchQuery}, function(result) {
                vm.companyUsers = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
