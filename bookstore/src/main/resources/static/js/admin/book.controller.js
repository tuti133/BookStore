(function () {
    'use strict'
    angular.module("BookStoreApp")
        .controller("BookController", BookController)
    BookController.$inject = ["$scope", "$mdDialog", "AccountService", "AlertService", "BookService", "CategoryService"];

    function BookController($scope, $mdDialog, AccountService, AlertService, BookService, CategoryService) {
        let vm = this;

        vm.books = [];
        vm.newBook = {
            id: null,
            name: null,
            price: null,
            description: null,
            author: null,
            publisher: null,
            publishedYear: null,
            favorite: 0,
            category: {
                id: 3,
                name: "SKILL"
            }
        }

        function loadData() {
            BookService.getAll().done(function (response) {
                $scope.$apply(function () {
                    vm.books = response;
                })
            })
        }

        loadData();

        vm.saveBook = function (dto) {
            $mdDialog.show({
                controller: BookDialogController,
                controllerAs: "vm",
                templateUrl: '/dialog/book-dialog.html',
                parent: angular.element(document.body),
                clickOutsideToClose: true,
                fullscreen: true,
                resolve: {
                    bookDto: function () {
                        return JSON.parse(JSON.stringify(dto))
                    }
                }
            }).then(function (response) {
                if (response.errorCode == 0){
                    AlertService.success(response.message, 2000);
                    loadData();
                }
                else AlertService.error(response.message, 2000);
            }, function (err) {

            });
        }

        function BookDialogController($scope, $mdDialog, bookDto) {
            let vm = this;
            vm.bookDto = bookDto;
            vm.save = save;
            vm.cancel = cancel;
            CategoryService.getAll().done(function (response) {
                $scope.$apply(function () {
                    vm.categories = response.data
                })
            })

            function save() {
                vm.isSaving = true;
                let blob = new Blob([JSON.stringify(vm.bookDto)], {type: "application/json"});
                let formData = new FormData();
                formData.append("book", blob);
                if ($("#image")[0].files[0] == null){
                    formData.append("image", "null");
                } else {
                    formData.append("image", $("#image")[0].files[0]);
                }
                BookService.save(formData).done(function (response) {
                    $mdDialog.hide(response);
                });
            }

            function cancel() {
                $mdDialog.cancel();
            }

        }


    }
})();