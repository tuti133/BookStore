(function () {
    'use strict'
    angular.module("BookStoreApp")
        .controller("BillController", BillController)
    BillController.$inject = ["$scope", "$mdDialog", "AccountService", "BillService"];

    function BillController($scope, $mdDialog, AccountService, BillService) {
        BillService.getByType(0).done(function (response) {
            console.log(response);
        })
    }
})();