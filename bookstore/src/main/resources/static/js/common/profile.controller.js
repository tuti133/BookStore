(function () {
    'use strict'
    angular.module("BookStoreApp")
        .controller("ProfileController", ProfileController)
    ProfileController.$inject = ["$scope", "$mdDialog", "AlertService"];

    function ProfileController($scope, $mdDialog, AlertService) {
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
                success: function (response) {
                    if (response.errorCode == 0){
                        AlertService.success(response.message, 2000, "bottom right");
                        setTimeout(function () {
                            location.reload();
                        }, 2000);
                    }
                    else AlertService.error(response.message, 2000, "bottom right");
                }
            })
        }

    }
})();