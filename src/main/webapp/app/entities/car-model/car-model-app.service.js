(function() {
    'use strict';
    angular
        .module('atolyeApp')
        .factory('CarModel', CarModel);

    CarModel.$inject = ['$resource'];

    function CarModel ($resource) {
        var resourceUrl =  'api/car-models/:id';

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
