(function () {
    'use strict'
    angular.module("BookStoreApp")
        .controller("BookQuantityController", BookQuantityController)
    BookQuantityController.$inject = ["$scope", "$mdDialog", "AccountService", "BookStoreService", "BookQuantityService", "AlertService"];

    function BookQuantityController($scope, $mdDialog, AccountService, BookStoreService, BookQuantityService, AlertService) {
        let vm = this;
        vm.site = "";
        vm.bookStores = [];
        vm.listBSQ = [];
        BookStoreService.getAll().done(function (response) {
            vm.bookStores = response.data;
            console.log(response)
        })

        vm.changeSite = changeSite;

        function changeSite() {
            if (vm.site != "" && vm.site != "Chọn khu vực")
                BookQuantityService.getByStore(vm.site).done(function (response) {
                    if (response.errorCode == 0)
                        vm.listBSQ = response.data;
                })
        }

        vm.edit = edit;

        function edit(item) {
            console.log(item);
            let prompt = $mdDialog.prompt()
                .title('Nhập số lượng sách')
                .textContent('')
                .placeholder('')
                .ariaLabel('')
                .initialValue(item.quantity)
                .required(true)
                .ok('Lưu')
                .cancel('Hủy');

            $mdDialog.show(prompt).then(function (result) {

                if (isNaN(result)){
                    AlertService.error("Số lượng phải là một số", 2000);
                    return;
                }

                let update = JSON.parse(JSON.stringify(item));
                update.quantity = result;
                BookQuantityService.update(update).done(function (response) {
                    if (response.errorCode == 0){
                        AlertService.success(response.message, 2000);
                        changeSite();
                    }
                    else AlertService.error(response.message, 2000);

                }).fail(err => {
                    AlertService.error("Có lỗi xảy ra!", 2000);
                })
            }, function () {
                console.log("cancel");
            });
        }

    }
})();