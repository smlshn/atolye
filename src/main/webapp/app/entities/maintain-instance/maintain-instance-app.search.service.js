(function() {
    'use strict';

    angular
        .module('atolyeApp')
        .factory('MaintainInstanceSearch', MaintainInstanceSearch);

    MaintainInstanceSearch.$inject = ['$resource'];

    function MaintainInstanceSearch($resource) {
        var resourceUrl =  'api/_search/maintain-instances/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
