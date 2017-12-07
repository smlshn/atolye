(function() {
    'use strict';

    angular
        .module('atolyeApp')
        .factory('CitySearch', CitySearch);

    CitySearch.$inject = ['$resource'];

    function CitySearch($resource) {
        var resourceUrl =  'api/_search/cities/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
