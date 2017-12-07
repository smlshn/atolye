(function() {
    'use strict';

    angular
        .module('atolyeApp')
        .factory('MaintainTypeSearch', MaintainTypeSearch);

    MaintainTypeSearch.$inject = ['$resource'];

    function MaintainTypeSearch($resource) {
        var resourceUrl =  'api/_search/maintain-types/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
