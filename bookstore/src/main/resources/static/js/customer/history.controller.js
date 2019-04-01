(function () {
    'use strict'
    angular.module("BookStoreApp")
        .controller("HistoryController", HistoryController)
    HistoryController.$inject = ["$scope", "$mdDialog", "AlertService", "BillService"];

    function HistoryController($scope, $mdDialog, AlertService, BillService) {
        let vm = this;
        vm.bills = null;
        vm.status = ['', 'Đang xử lý', 'Đang giao hàng', 'Đã giao hàng', 'Đã hủy'];


        vm.cancelBuy = cancelBuy;

        function cancelBuy(id) {
            AlertService.showConfirm("Xác nhận hủy đơn hàng!").then(function (ok) {
                BillService.update(id, 4).done(function (response) {
                    if (response.errorCode == 0){
                        AlertService.success(response.message, 2000);
                    } else {
                        AlertService.error(response.message, 2000);
                    }
                    loadData();
                })
            }, function (cancel) {
            })

        }

        loadData();

        function loadData() {
            BillService.getByCurrentUser().done(function (response) {
                $scope.$apply(function () {
                    vm.bills = response.data;
                    vm.bills.forEach(function (bill) {
                        bill.status = vm.status[bill.buy.status]
                        bill.buy.createdDate = new Date(bill.buy.createdDate);
                        console.log(bill.buy);
                    })
                })
            })
        }
    }
})();