(function () {
    'use strict'
    angular.module("BookStoreApp")
        .controller("AccountController", AccountController)
    AccountController.$inject = ["$scope", "$mdDialog", "AccountService", "AlertService", "BookStoreService"];

    function AccountController($scope, $mdDialog, AccountService, AlertService, BookStoreService) {
        let vm = this;
        vm.accountDtos = [];
        vm.bookStores = [];

        function loadAccount() {
            AccountService.getAll().done(function (e) {
                $scope.$apply(function () {
                    vm.accountDtos = e.data;
                    vm.accountDtos.forEach(dto => {
                        dto.account.createdDate = new Date(dto.account.createdDate);
                    })
                })
            })

            BookStoreService.getAll().done(function (e) {
                $scope.$apply(function () {
                    vm.bookStores = e.data;
                })
            })
        }

        loadAccount();

        vm.editAccount = function (dto, ev) {
            $mdDialog.show({
                controller: DialogController,
                controllerAs: "vm",
                templateUrl: '/dialog/account-dialog.html',
                parent: angular.element(document.body),
                targetEvent: ev,
                clickOutsideToClose: true,
                fullscreen: true,
                resolve: {
                    accountDto: function () {
                        let update = JSON.parse(JSON.stringify(dto));
                        update.account.createdDate = new Date(update.account.createdDate).getTime();
                        return update;
                    },
                    bookStores: function () {
                        return vm.bookStores
                    }
                }
            })
                .then(function (response) {
                    if (response.errorCode == 0){
                        AlertService.success(response.message, 2000);
                        loadAccount();
                    }
                    else AlertService.error(response.message, 2000);
                }, function (err) {
                    
                });

        }

        vm.createAccount = function (ev) {
            $mdDialog.show({
                controller: DialogController,
                controllerAs: "vm",
                templateUrl: '/dialog/account-dialog.html',
                parent: angular.element(document.body),
                targetEvent: ev,
                clickOutsideToClose: true,
                fullscreen: true,
                resolve: {
                    accountDto: function () {
                        return {
                            role: null,
                            account: {
                                id: null,
                                username: null,
                                password: null,
                                firstName: null,
                                lastName: null,
                                phone: null,
                                email: null,
                                gender: null,
                                activated: false,
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
                    },
                    bookStores: function () {
                        return vm.bookStores
                    }
                }
            })
                .then(function (response) {
                    if (response.errorCode == 0){
                        AlertService.success(response.message, 2000);
                        vm.accountDtos.push(response.data);
                    }
                    else AlertService.error(response.message, 2000);

                }, function (err) {
                    
                });
        };

        function DialogController($scope, $mdDialog, accountDto, bookStores) {
            let vm = this;
            vm.accountDto = accountDto;
            vm.bookStores = bookStores;
            vm.role = "";
            vm.account = {};
            vm.employee = {};
            vm.customer = {};

            vm.save = save;
            vm.cancel = cancel;

            vm.isSelectedStore = isSelectedStore;

            vm.genders = ["MALE", "FEMALE", "OTHER"];
            vm.authorities = ["ROLE_ADMIN", "ROLE_EMPLOYEE",  "ROLE_CHECKER", "ROLE_CUSTOMER"];

            function isSelectedStore(bookStore) {
                vm.bookStores.forEach(function (store) {
                    console.log(store)
                    console.log(bookStore)
                    if (store.id == bookStore.id) return true;
                })
                return false;
            }

            function save() {
                vm.isSaving = true;
                if (vm.accountDto.account.id == null) {
                    AccountService.create(vm.accountDto).done(function (response) {
                        $mdDialog.hide(response);
                        vm.isSaving = true;
                    })
                } else {
                    AccountService.update(vm.accountDto).done(function (response) {
                        $mdDialog.hide(response);
                        vm.isSaving = true;
                    })
                }
            }

            function cancel() {
                $mdDialog.cancel();
            }
        }
    }
})();