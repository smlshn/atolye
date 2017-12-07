(function() {
    'use strict';
    angular
        .module('atolyeApp')
        .factory('CarBrand', CarBrand);

    CarBrand.$inject = ['$resource'];

    function CarBrand ($resource) {
        var resourceUrl =  'api/car-brands/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
