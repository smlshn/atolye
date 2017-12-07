(function() {
    'use strict';

    angular
        .module('atolyeApp')
        .factory('CarBrandSearch', CarBrandSearch);

    CarBrandSearch.$inject = ['$resource'];

    function CarBrandSearch($resource) {
        var resourceUrl =  'api/_search/car-brands/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
