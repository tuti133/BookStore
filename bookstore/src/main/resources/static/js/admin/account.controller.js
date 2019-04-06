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
                        console.log(dto);
                        update.account.createdDate = new Date(update.account.createdDate).getTime();
                        return update;
                    },
                    bookStores: function () {
                        return vm.bookStores
                    }
                }
            })
                .then(function (response) {
                    if (response.errorCode == 0) {
                        AlertService.success(response.message, 2000);
                        loadAccount();
                    } else AlertService.error(response.message, 2000);
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
                                email: null,
                                activated: false,
                            },
                            employee: {
                                id: null,
                                workShift: null,
                                gender: null,
                                name: null,
                                phone: null
                            },
                            customer: {
                                id: null,
                                address: null,
                                creditNumber: null,
                                gender: null,
                                name: null,
                                phone: null
                            }
                        }
                    },
                    bookStores: function () {
                        return vm.bookStores
                    }
                }
            }).then(function (response) {
                if (response.errorCode == 0) {
                    AlertService.success(response.message, 2000);
                    vm.accountDtos.push(response.data);
                } else AlertService.error(response.message, 2000);

            }, function (err) {

            });
        };

        function DialogController($scope, $mdDialog, accountDto, bookStores) {
            let vm = this;
            vm.accountDto = accountDto;
            console.log(vm.accountDto);
            vm.bookStores = bookStores;
            vm.role = "";
            vm.account = {};
            vm.employee = {};
            vm.customer = {};

            vm.name = null;
            vm.gender = null;
            vm.phone = null;

            if (vm.accountDto.role != null){
                switch (vm.accountDto.role) {
                    case "ROLE_CUSTOMER":{
                        vm.gender = vm.accountDto.customer.gender;
                        vm.name = vm.accountDto.customer.name;
                        vm.phone = vm.accountDto.customer.phone;
                        break;
                    }
                    case "ROLE_EMPLOYEE":{
                        vm.gender = vm.accountDto.employee.gender;
                        vm.name = vm.accountDto.employee.name;
                        vm.phone = vm.accountDto.employee.phone;
                        break;
                    }
                    case "ROLE_CHECKER":{
                        vm.gender = vm.accountDto.employee.gender;
                        vm.name = vm.accountDto.employee.name;
                        vm.phone = vm.accountDto.employee.phone;
                        break;
                    }
                    default: {
                        break;
                    }
                }
            }

            vm.save = save;
            vm.cancel = cancel;

            vm.isSelectedStore = isSelectedStore;

            vm.genders = [
                {name: "Nam", value: "MALE"},
                {name: "Nữ", value: "FEMALE"},
                {name: "Khác", value: "OTHER"},
            ];

            vm.authorities = [
                {name: "Quản trị viên", value: "ROLE_ADMIN"},
                {name: "Kiểm duyệt diên", value: "ROLE_CHECKER"},
                {name: "Nhân viên", value: "ROLE_EMPLOYEE"},
                {name: "Khách hàng", value: "ROLE_CUSTOMER"},
            ];



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
                switch (vm.accountDto.role) {
                    case "ROLE_CUSTOMER":{
                        vm.accountDto.customer.gender = vm.gender;
                        vm.accountDto.customer.name = vm.name;
                        vm.accountDto.customer.phone = vm.phone;
                        break;
                    }
                    case "ROLE_CHECKER":{
                        vm.accountDto.employee.gender = vm.gender;
                        vm.accountDto.employee.name = vm.name;
                        vm.accountDto.employee.phone = vm.phone;
                        break;
                    }
                    case "ROLE_EMPLOYEE":{
                        vm.accountDto.employee.gender = vm.gender;
                        vm.accountDto.employee.name = vm.name;
                        vm.accountDto.employee.phone = vm.phone;
                        break;
                    }
                    default: {
                        break;
                    }
                }
                console.log(vm.accountDto);
                if (vm.accountDto.account.id == null) {
                    AccountService.create(vm.accountDto).done(function (response) {
                        if (response.errorCode == 0) {
                            $mdDialog.hide(response);
                        } else AlertService.error(response.message, 2000);
                        vm.isSaving = false;
                    })
                } else {
                    AccountService.update(vm.accountDto).done(function (response) {
                        if (response.errorCode == 0) {
                            $mdDialog.hide(response);
                        } else AlertService.error(response.message, 2000);
                        vm.isSaving = false;
                    })
                }
            }

            function cancel() {
                $mdDialog.cancel();
            }
        }
    }
})();