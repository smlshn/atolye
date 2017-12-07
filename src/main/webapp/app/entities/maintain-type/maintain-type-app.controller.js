(function() {
    'use strict';

    angular
        .module('atolyeApp')
        .controller('MaintainTypeAppController', MaintainTypeAppController);

    MaintainTypeAppController.$inject = ['MaintainType', 'MaintainTypeSearch'];

    function MaintainTypeAppController(MaintainType, MaintainTypeSearch) {

        var vm = this;

        vm.maintainTypes = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            MaintainType.query(function(result) {
                vm.maintainTypes = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            MaintainTypeSearch.query({query: vm.searchQuery}, function(result) {
                vm.maintainTypes = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
