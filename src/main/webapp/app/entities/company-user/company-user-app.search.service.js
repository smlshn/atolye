(function() {
    'use strict';

    angular
        .module('atolyeApp')
        .factory('CompanyUserSearch', CompanyUserSearch);

    CompanyUserSearch.$inject = ['$resource'];

    function CompanyUserSearch($resource) {
        var resourceUrl =  'api/_search/company-users/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
