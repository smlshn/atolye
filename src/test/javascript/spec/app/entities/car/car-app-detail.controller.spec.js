'use strict';

describe('Controller Tests', function() {

    describe('Car Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockCar, MockCompany, MockCarModel, MockCustomer;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockCar = jasmine.createSpy('MockCar');
            MockCompany = jasmine.createSpy('MockCompany');
            MockCarModel = jasmine.createSpy('MockCarModel');
            MockCustomer = jasmine.createSpy('MockCustomer');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Car': MockCar,
                'Company': MockCompany,
                'CarModel': MockCarModel,
                'Customer': MockCustomer
            };
            createController = function() {
                $injector.get('$controller')("CarAppDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'atolyeApp:carUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
