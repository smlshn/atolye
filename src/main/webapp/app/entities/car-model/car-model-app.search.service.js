(function() {
    'use strict';

    angular
        .module('atolyeApp')
        .factory('CarModelSearch', CarModelSearch);

    CarModelSearch.$inject = ['$resource'];

    function CarModelSearch($resource) {
        var resourceUrl =  'api/_search/car-models/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
