(function() {
    'use strict';
    angular
        .module('atolyeApp')
        .factory('SystemUser', SystemUser);

    SystemUser.$inject = ['$resource'];

    function SystemUser ($resource) {
        var resourceUrl =  'api/system-users/:id';

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
