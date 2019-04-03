(function () {
    'use strict'
    angular.module("BookStoreApp")
        .controller("OrderController", OrderController)
    OrderController.$inject = ["$scope", "$rootScope", "AlertService", "BookStoreService"];

    function OrderController($scope, $rootScope, AlertService, BookStoreService) {
        let vm = this;
        vm.order = order;
        vm.stores = [];
        vm.totalMoney = 0;
        BookStoreService.getAll().done(stores => {
            $scope.$apply(function () {
                vm.stores = stores.data;
            })
        })

        vm.remove = remove;

        function remove(item) {
            AlertService.showConfirm("Xác nhận xóa sản phẩm").then(function () {
                let idx = vm.cartList.indexOf(item);
                vm.cartList.splice(idx, 1);
                console.log(vm.cartList)
                resetCart();
            }, function () {

            })
        }

        vm.createBuyDto = {
            shipAddress: null,
            note: null,
            totalMoney: null,
            customerId: null,
            buyBookDtoList: null
        }

        vm.calculate = calculate;

        function calculate() {
            vm.totalMoney = 0;
            vm.cartList.forEach(item => {
                vm.totalMoney += item.quantity * item.book.price;
            })
            resetCart();
        }

        function resetCart() {
            localStorage.setItem("cartList", JSON.stringify(vm.cartList));
            $rootScope.$broadcast("RESET");
        }


        vm.cartList = [];
        getCart();

        function getCart() {
            let storage = localStorage.getItem("cartList");
            if (storage != null) {
                vm.cartList = JSON.parse(storage);
                vm.cartList.forEach(function (e) {
                    $.ajax({
                        url: "/api/bookQuantity/getOneByStore",
                        type: "GET",
                        data: {
                            bookId: e.bookId,
                            storeId: e.storeId
                        },
                        success: function (response) {
                            $scope.$apply(function () {
                                if (response.errorCode == 0) {
                                    e.amount = response.data.quantity;
                                }
                            })
                        }
                    })
                })
                calculate();
            }
        }

        vm.changeStore = changeStore;

        function changeStore(item) {
            $.ajax({
                url: "/api/bookQuantity/getOneByStore",
                type: "GET",
                data: {
                    bookId: item.bookId,
                    storeId: item.storeId
                },
                success: function (response) {
                    $scope.$apply(function () {
                        if (response.errorCode == 0) {
                            item.amount = response.data.quantity;
                        }
                    })
                }
            })
        }

        function order() {
            vm.createBuyDto.buyBookDtoList = vm.cartList;
            vm.createBuyDto.totalMoney = vm.totalMoney;
            console.log(vm.createBuyDto);
            let oversize = false;
            vm.createBuyDto.buyBookDtoList.forEach(function (e) {
                if (e.amount < e.quantity) {
                    oversize = true;

                }
            })
            if (oversize) {
                AlertService.error("Số lượng còn lại không đủ, xin kiểm tra lại giỏ hàng.", 2000);
                return;
            }
            if (vm.totalMoney == 0 || isNaN(vm.totalMoney)) {
                AlertService.error("Bạn phải chọn tối thiểu 1 sản phẩm để thanh toán", 2000, "bottom right");
                return;
            }
            if (vm.createBuyDto.shipAddress == null) {
                AlertService.error("Bạn phải nhập địa chỉ nhận hàng", 2000, "bottom right");
                return;
            }
            $.ajax({
                url: "/api/buy",
                type: "POST",
                contentType: "application/json",
                data: JSON.stringify(vm.createBuyDto),
                success: function (response) {
                    if (response.errorCode == 0) {
                        AlertService.success("Đặt hàng thành công", 2000);
                        localStorage.clear();
                        setTimeout(function () {
                            location.href = "/order/success/" + response.data.id;
                        }, 2000)
                    } else {
                        AlertService.error("Lỗi", 2000);
                    }

                }
            })
        }
    }
})();