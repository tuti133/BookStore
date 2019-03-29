(function () {
    'use strict'
    angular.module("BookStoreApp")
        .controller("RegisterController", RegisterController)
    RegisterController.$inject = ["$scope", "$mdDialog", 'AccountService', "AlertService"];

    function RegisterController($scope, $mdDialog, AccountService, AlertService) {
        let vm = this;
        vm.confirm = "";
        vm.genders = ["MALE", "FEMALE", "OTHER"];
        vm.accountDto = {
            role: "ROLE_CUSTOMER",
            account: {
                id: null,
                activated: true,
                username: null,
                password: null,
                firstName: null,
                lastName: null,
                phone: null,
                email: null,
                gender: null,
            },
            employee: {
                id: null,
                workShift: null,
                salary: null
            },
            customer: {
                id: null,
                address: null,
                creditNumber: null
            }
        }

        vm.register = register;

        function register() {
            console.log(vm.confirm);
            console.log(vm.accountDto.account.password);
            if (vm.confirm != vm.accountDto.account.password){
                AlertService.error("Confirm password not match!", 2000)
            } else {
                vm.isSaving = true;
                console.log(vm.accountDto);
                AccountService.create(vm.accountDto).done(function (response) {
                    if (response.errorCode == 0){
                        AlertService.success(response.message, 2000);
                        setInterval(function(){ location.href = "/login"; }, 2000);
                    }
                    else AlertService.error(response.message, 2000);
                });
            }
        }
    }
})();