(function () {
    'use strict'
    angular.module("BookStoreApp")
        .controller("ProfileController", ProfileController)
    ProfileController.$inject = ["$scope", "$mdDialog"];

    function ProfileController($scope, $mdDialog) {
        let vm = this;
        vm.account = {};
        vm.employee = {};
        vm.customer = {};
        vm.role;

        getInfo();

        function getInfo() {
            $.ajax({
                url: "/api/account/current",
                method: "GET",
                success: function (response) {
                    console.log(response);
                    vm.role = response.data.role;
                    vm.account = response.data.account;
                    vm.customer = response.data.customer;
                    vm.employee = response.data.employee;
                }
            })
        }

        vm.update = update;
        function update() {
            $.ajax({
                url: "/api/account/update",
                method: "POST",
                contentType: "application/json; charset:utf-8",
                data: JSON.stringify({
                    role: vm.role,
                    account: vm.account,
                    employee: vm.employee,
                    customer: vm.customer
                }),
                success: function (data) {
                    console.log(data);
                    location.reload();
                }
            })
        }

    }
})();