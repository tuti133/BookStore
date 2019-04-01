(function () {
    'use strict'
    angular.module("BookStoreApp")
        .controller("PasswordController", PasswordController)
    PasswordController.$inject = ["$scope", "$mdDialog", "AlertService", "AccountService"];

    function PasswordController($scope, $mdDialog, AlertService, AccountService) {
        let vm = this;
        vm.changePassword = changePassword;
        vm.dto = {
            oldPassword: "",
            newPassword: ""
        };

        vm.prevUrl = document.referrer;

        vm.confirm = "";

        function changePassword() {
            if (vm.confirm != vm.dto.newPassword){
                AlertService.error("Xác nhận mật khẩu mới không khớp", 2000);
                return;
            }
            $.ajax({
                url: "/api/account/change-password",
                type: "POST",
                contentType: "application/json",
                data: JSON.stringify(vm.dto),
                success: function (response) {
                    if (response.errorCode == 0){
                        AlertService.success(response.message, 2000);
                        setTimeout(function () {
                            location.href = "/";
                        }, 2000)
                    } else {
                        AlertService.error(response.message, 2000);
                    }

                }
            })
        }
    }
})();