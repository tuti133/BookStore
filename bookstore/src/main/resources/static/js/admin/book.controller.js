(function () {
    'use strict'
    angular.module("BookStoreApp")
        .controller("BookController", BookController)
    BookController.$inject = ["$scope", "$mdDialog", "AccountService"];

    function BookController($scope, $mdDialog, AccountService) {

    }
})();