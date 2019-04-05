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
            let idx = vm.cartList.indexOf(item);
            vm.cartList.splice(idx, 1);
            console.log(vm.cartList)
            resetCart();
        }

        vm.createBuyDto = {
            shipAddress: null,
            note: null,
            totalMoney: null,
            phone: null,
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
        function getCart(){
            let storage = localStorage.getItem("cartList");
            if (storage != null) {
                vm.cartList = JSON.parse(storage);
                calculate();
            }
        }

        function order() {
            vm.createBuyDto.buyBookDtoList = vm.cartList;
            vm.createBuyDto.totalMoney = vm.totalMoney;
            console.log(vm.createBuyDto);
            if (vm.totalMoney == 0 || isNaN(vm.totalMoney)){
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
                    AlertService.success("Đặt hàng thành công!", 2000);
                    localStorage.clear();
                    setTimeout(function () {
                        location.href = "/order/success/" + response.data.id;
                    }, 2000)
                }
            })
        }
    }
})();