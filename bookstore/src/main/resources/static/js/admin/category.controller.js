(function () {
    'use strict'
    angular.module("BookStoreApp")
        .controller("CategoryController", CategoryController)
    CategoryController.$inject = ["$scope", "$mdDialog", "CategoryService", "AlertService"];

    function CategoryController($scope, $mdDialog, CategoryService, AlertService) {
        let vm = this;
        vm.categories = [];
        loadData();

        function loadData() {
            CategoryService.getAll().done(function (response) {
                console.log(response);
                vm.categories = response.data;
            })
        }

        vm.edit = edit;
        vm.create = create;

        function create() {
            let prompt = $mdDialog.prompt()
                .title('Nhập tên danh mục')
                .textContent('')
                .placeholder('')
                .ariaLabel('')
                .initialValue('')
                .required(true)
                .ok('Lưu')
                .cancel('Hủy');

            $mdDialog.show(prompt).then(function (result) {
                let category = {};
                category.name = result;
                CategoryService.create(category).done(function (response) {
                    if (response.errorCode == 0){
                        AlertService.success(response.message, 2000);
                        loadData();
                    }
                    else AlertService.error(response.message, 2000);
                }).fail(err => {
                    AlertService.error(err.message, 2000);
                })
            }, function () {
                console.log("cancel");
            });
        }

        function edit(category) {
            let prompt = $mdDialog.prompt()
                .title('Nhập tên danh mục')
                .textContent('')
                .placeholder('')
                .ariaLabel('')
                .initialValue(category.name)
                .required(true)
                .ok('Lưu')
                .cancel('Hủy');

            $mdDialog.show(prompt).then(function (result) {
                let update = JSON.parse(JSON.stringify(category));
                update.name = result;
                CategoryService.update(update).done(function (response) {
                    if (response.errorCode == 0){
                        AlertService.success(response.message, 2000);
                        loadData();
                    }
                    else AlertService.error(response.message, 2000);

                }).fail(err => {
                    AlertService.error(err.message, 2000);
                })
            }, function () {
                console.log("cancel");
            });
        }

        vm.deleteCat = deleteCat;

        function deleteCat(category) {
            let confirm = $mdDialog.confirm()
                .title('Xác nhận xóa danh mục')
                .textContent('')
                .ariaLabel('Confirm')
                .ok('OK')
                .cancel('Hủy');

            $mdDialog.show(confirm).then(function () {
                CategoryService.deleteCategory(category).done(function (response) {
                    if (response.errorCode == 0){
                        AlertService.success(response.message, 2000);
                        loadData();
                    }
                    else AlertService.error(response.message, 2000);
                });
            }, function () {
                $scope.status = 'You decided to keep your debt.';
            });
        }
    }
})();
