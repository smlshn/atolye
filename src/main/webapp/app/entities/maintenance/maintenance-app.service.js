(function() {
    'use strict';
    angular
        .module('atolyeApp')
        .factory('Maintenance', Maintenance);

    Maintenance.$inject = ['$resource'];

    function Maintenance ($resource) {
        var resourceUrl =  'api/maintenances/:id';

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
