(function() {
    'use strict';
    angular
        .module('atolyeApp')
        .factory('MaintainInstance', MaintainInstance);

    MaintainInstance.$inject = ['$resource'];

    function MaintainInstance ($resource) {
        var resourceUrl =  'api/maintain-instances/:id';

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
