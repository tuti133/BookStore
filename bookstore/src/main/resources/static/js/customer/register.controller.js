(function () {
    'use strict'
    angular.module("BookStoreApp")
        .controller("RegisterController", RegisterController)
    RegisterController.$inject = ["$scope", "$mdDialog", 'AccountService', "AlertService"];

    function RegisterController($scope, $mdDialog, AccountService, AlertService) {
        let vm = this;
        vm.confirm = "";
        vm.genders = [
            {name: "Nam", value: "MALE"},
            {name: "Nữ", value: "FEMALE"},
            {name: "Khác", value: "OTHER"},
        ];

        vm.accountDto = {
            role: "ROLE_CUSTOMER",
            account: {
                id: null,
                activated: true,
                username: null,
                password: null,
                email: null,
            },
            employee: {
                id: null,
                name: null,
                workShift: null,
                salary: null,
                gender: null,
            },
            customer: {
                id: null,
                name: null,
                address: null,
                creditNumber: null,
                gender: null,
                phone: null,
            }
        }

        vm.register = register;

        function register() {
            if (vm.confirm != vm.accountDto.account.password) {
                AlertService.error("Confirm password not match!", 2000)
            } else {
                vm.isSaving = true;
                console.log(vm.accountDto);
                AccountService.create(vm.accountDto).done(function (response) {
                    if (response.errorCode == 0) {
                        AlertService.success(response.message, 2000);
                        setInterval(function () {
                            location.href = "/login";
                        }, 2000);
                    } else {
                        AlertService.error(response.message, 2000);
                        vm.isSaving = false;
                    }
                });
            }
        }
    }
})();