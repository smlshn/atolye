(function() {
    'use strict';

    angular
        .module('atolyeApp')
        .controller('CarModelAppController', CarModelAppController);

    CarModelAppController.$inject = ['CarModel', 'CarModelSearch'];

    function CarModelAppController(CarModel, CarModelSearch) {

        var vm = this;

        vm.carModels = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            CarModel.query(function(result) {
                vm.carModels = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            CarModelSearch.query({query: vm.searchQuery}, function(result) {
                vm.carModels = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
