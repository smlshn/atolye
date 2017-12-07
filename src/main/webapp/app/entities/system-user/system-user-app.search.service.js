(function() {
    'use strict';

    angular
        .module('atolyeApp')
        .factory('SystemUserSearch', SystemUserSearch);

    SystemUserSearch.$inject = ['$resource'];

    function SystemUserSearch($resource) {
        var resourceUrl =  'api/_search/system-users/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
