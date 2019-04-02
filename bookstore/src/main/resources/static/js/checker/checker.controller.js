(function () {
    'use strict'
    angular.module("BookStoreApp")
        .controller("CheckerController", CheckerController)
    CheckerController.$inject = ["$scope", "$mdDialog", "AlertService", "BillService"];

    function CheckerController($scope, $mdDialog, AlertService, BillService) {
        let vm = this;

        vm.bills = [];
        vm.buyStatus = 1;
        vm.status = ['', 'Đang xử lý', 'Đang giao hàng', 'Đã giao hàng', 'Đã hủy'];

        vm.allChangeStatus = [
            {id: 1, name: "Đang xử lý"},
            {id: 2, name: "Đang giao hàng"},
            {id: 3, name: "Đã giao hàng"},
            {id: 4, name: "Đã hủy"},
        ]

        vm.selected = [];

        loadData();

        function loadData() {
            vm.selected = [];
            BillService.getByStatusType(vm.buyStatus).done(function (response) {
                $scope.$apply(function () {
                    vm.bills = response.data;
                    vm.bills.forEach(function (bill) {
                        bill.status = vm.status[bill.buy.status]
                        bill.buy.createdDate = new Date(bill.buy.createdDate);
                    })
                })
            })
        }

        vm.changeStatus = changeStatus;

        function changeStatus(id, status) {
            AlertService.showConfirm("Xác nhận cập nhật trạng thái").then(function (ok) {
                BillService.update(id, status).done(function (response) {
                    if (response.errorCode == 0) {
                        AlertService.success(response.message, 2000);
                    } else {
                        AlertService.error(response.message, 2000);
                    }
                    loadData();
                })
            }, function (cancel) {
            })
        }

        vm.changeBillType = changeBillType;

        function changeBillType() {
            loadData();
        }

        vm.allBuyStatus = [
            {id: 0, name: "Tất cả"},
            {id: 1, name: "Đang xử lý"},
            {id: 2, name: "Đang giao hàng"},
            {id: 3, name: "Đã giao hàng"},
            {id: 4, name: "Đã hủy"},
        ]

        vm.logout = logout;

        function logout() {
            AlertService.showConfirm("Xác nhận đăng xuất!").then(function () {
                location.href = "/logout";
            }, function () {

            })
        }

        vm.change = change;

        function change(status) {
            AlertService.showConfirm("Xác nhận đổi trạng thái đơn hàng!").then(function () {
                vm.selected.forEach(function (e, index) {
                        BillService.update(e, status).done(function (res) {
                            if (res.errorCode == 1) AlertService.error(res.message, 2000);
                            if (index + 1 == vm.selected.length) {
                                if (res.errorCode == 0) AlertService.success(res.message, 2000);
                                loadData();
                            }
                        })
                    }
                )
            }, function () {

            })
        }


        vm.detail = function (ev, data) {
            $mdDialog.show({
                controller: BillDialogController,
                controllerAs: "vm",
                templateUrl: '/dialog/bill-dialog.html',
                parent: angular.element(document.body),
                targetEvent: ev,
                clickOutsideToClose: true,
                fullscreen: true,
                resolve: {
                    data: function () {
                        return data;
                    }
                }
            }).then(function (response) {

            }, function (err) {

            });
        };


        function BillDialogController($scope, $mdDialog, data) {
            let vm = this;
            vm.data = data;
            console.log(data);
            vm.cancel = cancel;

            function cancel() {
                $mdDialog.cancel();
            }
        }

    }
})();