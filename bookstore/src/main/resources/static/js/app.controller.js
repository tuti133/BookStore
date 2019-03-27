(function () {
    'use strict'
    angular.module('BookStoreApp').controller("AppController", AppController)

    AppController.$inject = ['$scope'];

    function AppController($scope) {
        let vm = this;
        vm.cartList = [];
        vm.storage = localStorage.getItem("cartList");
        vm.price = 100000;
        if (vm.storage != null) {
            vm.cartList = vm.storage.split(",").map(function (e) {
                return parseInt(e);
            });
        }
        vm.addToCart = function (item) {
            vm.cartList.push(item);
            localStorage.setItem("cartList", vm.cartList);
        }

        vm.clearList = function () {
            localStorage.clear();
            vm.cartList = [];
        }

        vm.account = {};
        vm.username = "Menu"
        $.ajax({
            url: "/api/account/current",
            type: "GET",
            success: function (res) {
                if (res.data != null)
                    vm.account = res.data.account;
                if (vm.account.firstName != null) {
                    vm.username = vm.account.firstName;
                } else {
                    vm.username = vm.account.username;
                }
            }
        })
    }
})();