(function () {
    'use strict'
    angular.module("BookStoreApp")
        .controller("DashboardController", DashboardController)
    DashboardController.$inject = ["$scope", "$mdDialog", "AccountService", "AlertService"];

    function DashboardController($scope, $mdDialog, AccountService, AlertService) {
        let vm = this;

        vm.adminFunctions = [
            {
                name: "Quản lý tài khoản",
                url: "/admin/account",
                icon: "supervisor_account"
            },
            {
                name: "Quản lý sách",
                url: "/admin/book",
                icon: "library_books"
            },
            {
                name: "Quản lý hóa đơn",
                url: "/admin/bill",
                icon: "payment"
            },
            {
                name: "Quản lý danh mục",
                url: "/admin/category",
                icon: "list_alt"
            },
            {
                name: "Đổi mật khẩu",
                url: "/password",
                icon: "vpn_key"
            },
            {
                name: "Đăng xuất",
                url: "/logout",
                icon: "exit_to_app"
            },
        ]

    }
})();