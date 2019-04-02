(function () {
    'use strict'
    angular.module("BookStoreApp")
        .controller("BillController", BillController)
    BillController.$inject = ["$scope", "$mdDialog", "AccountService", "BillService"];

    function BillController($scope, $mdDialog, AccountService, BillService) {
        BillService.statistic(0, new Date("2019-04-01").getTime(), new Date("2019-04-02").getTime()).done(function (response) {
            console.log(response);
        })
    }
})();