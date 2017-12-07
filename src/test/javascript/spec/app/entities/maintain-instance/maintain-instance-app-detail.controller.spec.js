'use strict';

describe('Controller Tests', function() {

    describe('MaintainInstance Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockMaintainInstance, MockEmployee, MockOperation, MockMaintenance;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockMaintainInstance = jasmine.createSpy('MockMaintainInstance');
            MockEmployee = jasmine.createSpy('MockEmployee');
            MockOperation = jasmine.createSpy('MockOperation');
            MockMaintenance = jasmine.createSpy('MockMaintenance');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'MaintainInstance': MockMaintainInstance,
                'Employee': MockEmployee,
                'Operation': MockOperation,
                'Maintenance': MockMaintenance
            };
            createController = function() {
                $injector.get('$controller')("MaintainInstanceAppDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'atolyeApp:maintainInstanceUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
