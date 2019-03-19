(function () {
    'use strict'
    angular.module('BookStoreApp').controller("AppController", AppController)

    AppController.$inject = ['$scope'];

    function AppController($scope) {
        $scope.cartList = [];
        $scope.storage = localStorage.getItem("cartList");
        $scope.price = 100000;
        if ($scope.storage != null){
            $scope.cartList = $scope.storage.split(",").map(function (e) {
                return parseInt(e);
            });
        }
        $scope.addToCart = function (item) {
            $scope.cartList.push(item);
            localStorage.setItem("cartList", $scope.cartList);
        }

        $scope.clearList = function () {
            localStorage.clear();
            $scope.cartList = [];
        }
    }
})();