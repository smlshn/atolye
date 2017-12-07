(function() {
    'use strict';
    angular
        .module('atolyeApp')
        .factory('MaintainType', MaintainType);

    MaintainType.$inject = ['$resource'];

    function MaintainType ($resource) {
        var resourceUrl =  'api/maintain-types/:id';

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
