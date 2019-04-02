(function () {
    'use strict'
    angular.module("BookStoreApp")
        .controller("DashboardController", DashboardController)
    DashboardController.$inject = ["$scope", "$mdDialog", "AccountService", "AlertService", "BookService", "BillService"];

    function DashboardController($scope, $mdDialog, AccountService, AlertService, BookService, BillService) {
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
                name: "Quản lý lượng sách",
                url: "/admin/book-quantity",
                icon: "view_weeks"
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

        vm.allType = [
            {id: 0, name: "Tất cả"},
            {id: 1, name: "Offline"},
            {id: 2, name: "Online"},
        ]

        vm.allBuyStatus = [
            {id: 0, name: "Tất cả"},
            {id: 1, name: "Đang xử lý"},
            {id: 2, name: "Đang giao hàng"},
            {id: 3, name: "Đã giao hàng"},
            {id: 4, name: "Đã hủy"},
        ]

        vm.from = new Date((new Date()).getTime() - 3600 * 24 * 1000);
        vm.to = new Date();
        vm.type = 0;
        vm.buyStatus = 0;

        vm.statistic = null;
        vm.listBills = null;

        vm.changeDate = changeDate;
        function changeDate() {
            loadCommonData();
            loadBillData();
        }
        
        vm.changeBillType = changeBillType;
        function changeBillType() {
            loadBillData();
        }

        loadBillData();
        loadCommonData();

        function loadCommonData() {
            let from = new Date(vm.from).getTime();
            let to = new Date(vm.to).getTime();
            if (from == to) to += 3600 * 24 * 1000
            BillService.statistic(0, from, to, 0).done(function (response) {
                $scope.$apply(function () {
                    vm.statistic = response;
                    console.log(vm.statistic)
                    vm.offlineBill = [];
                    vm.onlineBill = [];
                    vm.proccessing = [];
                    vm.shipping = [];
                    vm.completed = [];
                    vm.canceled = [];
                    response.billDtoList.forEach(function (e) {
                        if (e.type == 1) vm.offlineBill.push(e);
                        else {
                            vm.onlineBill.push(e);
                            switch (e.status) {
                                case '1': {
                                    vm.proccessing.push(e);
                                    break;
                                }
                                case '2': {
                                    vm.shipping.push(e);
                                    break;
                                }
                                case '3': {
                                    vm.completed.push(e);
                                    break;
                                }
                                case '4': {
                                    vm.canceled.push(e);
                                    break;
                                }
                            }
                        }
                    })
                    setTimeout(renderChart, 200);
                })
            })
        }

        function loadBillData() {
            let from = new Date(vm.from).getTime();
            let to = new Date(vm.to).getTime();
            if (from == to) to += 3600 * 24 * 1000
            if (isNaN(vm.type)) vm.type = 0;
            if (isNaN(vm.buyStatus)) vm.buyStatus = 0;
            BillService.statistic(vm.type, from, to, vm.buyStatus).done(function (response) {
                $scope.$apply(function () {
                    vm.listBills = response.billDtoList;
                })
            })
        }


        function renderChart() {
            // Highcharts.chart('type_chart', {
            //     chart: {
            //         type: 'pie',
            //     },
            //     title: {
            //         text: 'Thống kê loại hóa đơn'
            //     },
            //     series: [{
            //         name: 'Số lượng',
            //         colorByPoint: true,
            //         data: [{
            //             name: 'Online',
            //             y: vm.onlineBill.length,
            //         }, {
            //             name: 'Offline',
            //             y: vm.offlineBill.length
            //         }]
            //     }]
            // });
            //
            // Highcharts.chart('book_chart', {
            //     chart: {
            //         type: 'pie',
            //     },
            //     title: {
            //         text: 'Thống kê bán sách'
            //     },
            //     series: [{
            //         name: 'Số lượng',
            //         colorByPoint: true,
            //         data: [{
            //             name: 'Online',
            //             y: vm.statistic.totalBookOnline,
            //         }, {
            //             name: 'Offline',
            //             y: vm.statistic.totalBookOffline,
            //         }]
            //     }]
            // });

            Highcharts.chart('status_chart', {
                chart: {
                    type: 'column',
                },
                title: {
                    text: 'Thống kê tình trạng hóa đơn online'
                },
                xAxis: {
                    categories: ['Đang xử lý', 'Đang giao hàng', 'Đã giao hàng', 'Đã hủy']
                },
                yAxis: {
                    title: {
                        text: 'Số lượng'
                    }
                },
                series: [{
                    showInLegend: false,
                    name: 'Số lượng',
                    data: [vm.proccessing.length, vm.shipping.length, vm.completed.length, vm.canceled.length]
                }]
            });



            Highcharts.chart('common_chart', {
                chart: {
                    type: 'bar'
                },
                title: {
                    text: 'Thống kê theo hình thức bán hàng'
                },
                xAxis: {
                    categories: ['Doanh thu', 'Sách', 'Hóa đơn']
                },
                yAxis: {
                    min: 0,
                    title: {
                        text: 'Tỉ lệ'
                    }
                },
                tooltip: {
                    pointFormat: '<span style="color:{series.color}">{series.name}</span>: <b>{point.y}</b> ({point.percentage:.0f}%)<br/>',
                    shared: true
                },
                plotOptions: {
                    bar: {
                        stacking: 'percent'
                    }
                },
                series: [{
                    name: 'Online',
                    data: [vm.statistic.totalOnline, vm.statistic.totalBookOnline, vm.onlineBill.length]
                }, {
                    name: 'Offline',
                    data: [vm.statistic.totalOffline, vm.statistic.totalBookOffline, vm.offlineBill.length]
                }]
            });
        }


        vm.initBook = initBook;

        function initBook() {
            let skill = [
                {
                    id: null,
                    name: "Kỹ Năng Đi Trước Đam Mê",
                    price: 50000,
                    description: "Trong quyển sách Kỹ Năng Đi Trước Đam Mê, Cal Newport lột trần niềm tin từ trước đến nay rằng ta nên \"theo đuổi đam mê.\" Niềm tin sáo rỗng này không những sai sót ở chỗ là những đam mê tồn tại sẵn có thường hiếm hoi và không liên quan gì lắm đến việc hầu hết mọi người cuối cùng cũng yêu thích công việc họ làm, mà nó còn có thể gây nguy hiểm, sinh ra cảm giác lo lắng và hiện tượng nhảy việc liên miên.",
                    author: "Cal Newport",
                    publisher: "NXB Trẻ",
                    publishedYear: 2012,
                    favorite: 0,
                    category: {
                        id: 3,
                        name: "SKILL"
                    }
                },
                {
                    id: null,
                    name: "Cùng Con Trưởng Thành",
                    price: 60000,
                    description: "Cuốn sách Cùng con trưởng thành người đọc là cuốn sách hay và ý nghĩa về tình cha con, những người cha có vai trò vô cùng quan trọng, sự hi sinh và che chở của cha là tiền đề cho con tương lại tươi sáng, hạnh phúc",
                    author: "Đông Tử",
                    publisher: "NXB Trẻ",
                    publishedYear: 2015,
                    favorite: 0,
                    category: {
                        id: 3,
                        name: "SKILL"
                    }
                },
                {
                    id: null,
                    name: "Nghệ Thuật Đàm Phán",
                    price: 80000,
                    description: "Quyển sách cho chúng ta thấy cách Trump làm việc mỗi ngày - cách ông điều hành công việc kinh doanh và cuộc sống - cũng như cách ông trò chuyện với bạn bè và gia đình, làm ăn với đối thủ, mua thành công những sòng bạc hàng đầu ở thành phố Atlantic, thay đổi bộ mặt của những cao ốc ở thành phố New York... và xây dựng những tòa nhà chọc trời trên thế giới.",
                    author: "Donald J. Trump - Tony Schwartz",
                    publisher: "NXB Trẻ",
                    publishedYear: 2014,
                    favorite: 0,
                    category: {
                        id: 3,
                        name: "SKILL"
                    }
                },
                {
                    id: null,
                    name: "Biết Hài Lòng",
                    price: 30000,
                    description: "“Biết Hài Lòng” là một quyển sách khá hay của Leo Babauta. Nếu bạn đang gặp khó khăn, hay nổi giận, buồn bã trong cuộc sống, thì đây là quyển sách dành cho bạn.",
                    author: "Leo Babauta",
                    publisher: "NXB Trẻ",
                    publishedYear: 2011,
                    favorite: 0,
                    category: {
                        id: 3,
                        name: "SKILL"
                    }
                },
            ];

            let sport = [
                {
                    id: null,
                    name: "Bão Lửa U23 – Thường Châu Tuyết Trắng",
                    price: 60000,
                    description: "Đây là một cuốn sách rất đáng đọc vào lúc này, khi những dư âm của chiến công U23 vẫn còn, nhưng cũng là để ngẫm ngợi sâu hơn về nhiều điều liên quan đến họ nói riêng và cả nền bóng đá Việt Nam nói chung.",
                    author: "Nhiều Tác Giả",
                    publisher: "NXB Trẻ",
                    publishedYear: 2018,
                    favorite: 0,
                    category: {
                        id: 2,
                        name: "SPORT"
                    }
                },
                {
                    id: null,
                    name: "Kỹ Thuật Đánh Bóng Bàn",
                    price: 90000,
                    description: "Cùng với sự phát triển của kỹ thuật môn bóng bàn, sự biến hóa về đường bóng, biến hóa về điểm rơi khi đánh bóng cũng ngày một phát triển không ngừng. Điều này đòi hỏi vận động viên bóng bàn cần phải di chuyển bước chân nhanh hơn để đảm bảo tính chính xác của động tác chi trên và phát huy sở trường kỹ chiến thuật cá nhân. Ngược lại, nếu như di chuyển bước không tốt thì không thể đảm bảo cho chi trên thực hiện động tác đánh bóng chính xác. Tính chuẩn xác của bước chân và chất lượng đánh bóng có ảnh hưởng trực tiếp tới hiệu quảcủa việc sử dụng kỹ thuật sở trường của vận động viên. Vì vậy để đánh bóng bàn được tốt, nhất định phải nắm vững kỹ thuật di chuyển bước.",
                    author: "Thanh Long",
                    publisher: "NXB Trẻ",
                    publishedYear: 2016,
                    favorite: 0,
                    category: {
                        id: 2,
                        name: "SPORT"
                    }
                },
                {
                    id: null,
                    name: "Dạy Trẻ Tập Bơi",
                    price: 45000,
                    description: "Bơi lội là một kỹ năng cần thiết giúp đảm bảo an toàn cho trẻ khi tiếp xúc với nước, đồng thời giúp trẻ phát triển tốt về mặt thể chất và tinh thần. Cuốn sách “Dạy trẻ tập bơi” cung cấp nhiều lời khuyên cho luyện tập thực tế bà các biện pháp áp dụng nhằm đảm bảo tính ổn định của kỹ thuật. Cuốn sách đưa ra hình thức học tập và rèn luyện đúng đắn để bảo đảm các em có kỹ thuật bơi vững, thay đổi linh hoạt và đa dạng.",
                    author: "Alis Mark",
                    publisher: "NXB Trẻ",
                    publishedYear: 2018,
                    favorite: 0,
                    category: {
                        id: 2,
                        name: "SPORT"
                    }
                },
                {
                    id: null,
                    name: "Võ Thuật Aikido",
                    price: 150000,
                    description: "Aikido là một môn võ thuật có nguồn gốc từ Nhật Bản. Nó không chỉ được mệnh danh là thanh cao và khôn ngoan nhất, mà còn mang nhiều đặc điểm rất riêng so với các môn võ thuật khác. Có thể nói Aikido là một môn võ tự vệ hoàn toàn có tính chất phản công lại đối thủ khi cần thiết, nhưng thực ra chỉ với mục đích mang tính hoá giải, tránh né, chế ngự đối thủ, chứ không hoàn toàn mang tính đả thương đối thủ.",
                    author: "Oratti",
                    publisher: "NXB Trẻ",
                    publishedYear: 2013,
                    favorite: 0,
                    category: {
                        id: 2,
                        name: "SPORT"
                    }
                },
            ];

            let comic = [
                {
                    id: null,
                    name: "Người Trong Giang Hồ",
                    price: 100000,
                    description: "Người Trong Giang Hồ là bộ truyện tranh nổi tiếng Cổ Hoặc Tử - Teddy Boy! Truyện đạt kỷ lục bộ truyện tranh dài nhất thế giới! Thế giới truyện rộng lớn, tình tiết ly kỳ, cao trào, nhiều nét đột phá, truyện tả thật thế giới xã hội đen Hongkong, mặt xấu ác lòng người, hiệp nghĩa can trường, huynh đệ tình thân...",
                    author: "Ngưu Lão",
                    publisher: "NXB Kim Đồng",
                    publishedYear: 2018,
                    favorite: 0,
                    category: {
                        id: 1,
                        name: "COMIC"
                    }
                },
                {
                    id: null,
                    name: "Kingdom - Vương Giả Thiên Hạ",
                    price: 60000,
                    description: "Trải qua hàng triệu năm kể từ thời đại của các vị thần. Đây là thời mà khát vọng của một người có thể xoay chuyển cả thế giới. Thời đại của hơn 500 năm binh biến: thời Chiến Quốc. Kingdom – Vương Giả Thiên Hạ là câu chuyển kể về một thiếu niên tên Tín cùng những thử thách cam go, những trận chiến đẫm máu mà cậu cần vượt qua để có thể trở thành một vị tướng quân vĩ đại. Tác phẩm đã giành được giải thưởng văn hóa Tezuka Osamu lần thứ 17 vào năm 2013.",
                    author: "Hara Yasuhisa",
                    publisher: "NXB Kim Đồng",
                    publishedYear: 2010,
                    favorite: 0,
                    category: {
                        id: 1,
                        name: "COMIC"
                    }
                },
                {
                    id: null,
                    name: "Bleach - Sứ Giả Thần Chết",
                    price: 20000,
                    description: "Bleach là một chuỗi hành trình về Ichigo Kurosaki, cậu là một học sinh cấp ba với khả năng nhìn thấy linh hồn và một shinigami (thần chết) tên là Rukia Kuchiki, tình cờ gặp Ichigo khi đang săn lùng một linh hồn xấu được gọi là hollow (ác ma). Rukia bị thương trong cuộc chiến với linh hồn đó và bất đắc dĩ phải cho Ichigo năng lượng của mình. Thế là cuộc hành trình của Ichigo và Rukia bắt đầu từ đây.",
                    author: "Kubo Tite",
                    publisher: "NXB Kim Đồng",
                    publishedYear: 2005,
                    favorite: 0,
                    category: {
                        id: 1,
                        name: "COMIC"
                    }
                },
                {
                    id: null,
                    name: "Death Note - Cuốn Sổ Tử Thần",
                    price: 99000,
                    description: "Yagami Raito là một học sinh cấp III rất thông minh nhưng luôn cảm thấy cuộc sống xung quanh thật tẻ nhạt và phẫn nộ với những tội ác và tham nhũng xảy ra trên thế giới. Cuộc sống của Yagami chỉ thay đổi vào năm 2003 sau khi cậu tình cờ nhặt được một cuốn sổ kỳ bí có tên\"Death Note\" (Desu Nōto, sách chết). Lời hướng dẫn sử dụng trên cuốn sổ đã ghi rõ rằng những người bị viết tên vào đây sẽ chết.",
                    author: "Tsugumi Ohba - Takeshi Obata",
                    publisher: "NXB Kim Đồng",
                    publishedYear: 2011,
                    favorite: 0,
                    category: {
                        id: 1,
                        name: "COMIC"
                    }
                },
            ];

            let language = [
                {
                    id: null,
                    name: "3000 Từ Vựng Tiếng Anh Thông Dụng Nhất",
                    price: 35000,
                    description: "Từ vựng đóng một vai trò đặc biệt quan trọng, nhất là trong giao tiếp. Nhằm đáp ứng nhu cầu đó chúng tôi xin giới thiệu với bạn đọc cuốn 3000 Từ vựng Tiếng Anh thông dụng nhất. Cuốn sách bao gồm 3000 từ vựng căn bản và thông dụng nhất nhằm giúp các bạn nâng cao vốn từ vựng của mình.",
                    author: "Nhiều tác giả",
                    publisher: "NXB Trẻ",
                    publishedYear: 2011,
                    favorite: 0,
                    category: {
                        id: 4,
                        name: "LANGUAGE"
                    }
                },
                {
                    id: null,
                    name: "360 Động Từ Bất Quy Tắc Và 12 Thì Cơ Bản Trong Tiếng Anh",
                    price: 50000,
                    description: "Cuốn sách này như một người bạn luôn nhắc nhở bạn dùng chính xác các dạng nguyên thể, quá khứ và phân từ của động từ. Mỗi động từ chúng tôi có đưa ra ví dụ để bạn có thể hiểu đươc cách dùng của động từ đó.để nhớ và dùng các động từ bất quy tắc này một cách tốt nhất các bạn lên học thuộc các ví dụ, từ đó các bạn sẽ nhớ được tình huống và vận dụng các động từ này một cách hiệu quả nhất",
                    author: "Nhiều tác giả",
                    publisher: "NXB Trẻ",
                    publishedYear: 2011,
                    favorite: 0,
                    category: {
                        id: 4,
                        name: "LANGUAGE"
                    }
                },
                {
                    id: null,
                    name: "Ngữ Pháp Tiếng - Anh Ôn Thi Toeic",
                    price: 75000,
                    description: "Đây là “Hệ thống ngữ pháp” chuẩn của Bộ giáo dục ban hàng trong loạt hệ thống kiến thức trọng tâm học ôn Toeic hiệu quả. Đúng như tên gọi, mục lớn này nhằm giúp người học biết, nắm bắt và hiểu một cách có hệ thống các chuyên đề ngữ pháp chính cần có để hoàn thành tốt bài thi Toeic mới với 2 phần chính là Nghe và Đọc.",
                    author: "Bộ Giáo Dục và Đào tạo",
                    publisher: "NXB GD",
                    publishedYear: 2011,
                    favorite: 0,
                    category: {
                        id: 4,
                        name: "LANGUAGE"
                    }
                },
                {
                    id: null,
                    name: "384 Tình Huống Thực Hành Đàm Thoại Tiếng Hàn",
                    price: 65000,
                    description: "Cuốn sách 384 tình huống thực hành đàm thoại tiếng Hàn với 192 mẫu câu cơ bản, ứng dụng được trong nhiều tình huống giao tiếp khác nhau. Mỗi mẫu câu đều có hai tình huống đàm thoại. Bạn nên học thuộc các mẫu câu đàm thoại này để hiểu cách vận dụng.",
                    author: "Jung min kyung",
                    publisher: "NXB Kim Đồng",
                    publishedYear: 2011,
                    favorite: 0,
                    category: {
                        id: 4,
                        name: "LANGUAGE"
                    }
                },
            ];

            let technology = [
                {
                    id: null,
                    name: "Công Nghệ Blockchain",
                    price: 80000,
                    description: "Blockchain là chủ đề đang vô cùng nóng trên toàn cầu hiện nay. Nó cùng với Bitcoin và tiền kỹ thuật số trở thành đề tài bàn luận trên rất nhiều mặt báo và trong những cuộc trò chuyện của mọi người. Tuy nhiên, khi nói về blockchain vẫn còn nhiều tranh cãi. Có người lo lắng rằng Bitcoin có thể chỉ là bong bóng, nhiều người cho rằng công nghệ phía sau nó là một sự đột phá, và công nghệ ấy sẽ tiếp tục con đường của mình cho đến khi được chấp nhận và tích hợp với Internet.",
                    author: "Nhiều tác giả",
                    publisher: "NXB Online",
                    publishedYear: 2011,
                    favorite: 0,
                    category: {
                        id: 5,
                        name: "TECHNOLOGY"
                    }
                },
                {
                    id: null,
                    name: "Machine Learning Cơ Bản",
                    price: 150000,
                    description: "Machine Learning là một tập con của AI. Theo định nghĩa của Wikipedia, Machine learning is the subfield of computer science that “gives computers the ability to learn without being explicitly programmed”. Nói đơn giản, Machine Learning là một lĩnh vực nhỏ của Khoa Học Máy Tính, nó có khả năng tự học hỏi dựa trên dữ liệu đưa vào mà không cần phải được lập trình cụ thể. Bạn Nguyễn Xuân Khánh tại đại học Maryland đang viết một cuốn sách về Machine Learning bằng tiếng Việt khá thú vị, các bạn có thể tham khảo bài Machine Learning là gì?.",
                    author: "Vũ Hữu Tiệp",
                    publisher: "NXB Online",
                    publishedYear: 2011,
                    favorite: 0,
                    category: {
                        id: 5,
                        name: "TECHNOLOGY"
                    }
                },
                {
                    id: null,
                    name: "Learning Vue.js 2",
                    price: 99000,
                    description: "Olga Filipova là một lập trình viên có kinh nghiệm trong phát triển fontend, chính vì vậy các nội dung được viết ra trong Learning Vue.js 2 là rất sát với thực tế. Bản thân Olga Filipova cũng đang quản lý một dự án về học trực tuyến, do vậy các phần trong sách được kiến trúc có tính sư phạm cao. Với mỗi vấn đề đều có phần dẫn dắt và các ví dụ thực hành giúp cho việc nắm bắt các kiến thức framework Vue.js 2 trở lên dễ dàng.",
                    author: "Olga Filipova",
                    publisher: "NXB Online",
                    publishedYear: 2011,
                    favorite: 0,
                    category: {
                        id: 5,
                        name: "TECHNOLOGY"
                    }
                },
                {
                    id: null,
                    name: "Giải Thuật Và Lập Trình",
                    price: 45000,
                    description: "Nếu bạn là người đam mê tin học, nếu bạn là người muốn khám phá về lập trình, hẳn bạn phải biết đến một cuốn sách tin học rất nổi tiếng ở Việt Nam trong nhiều năm trở lại đây. Từ những học sinh không chuyên đến những thành viên đội tuyển thi quốc tế tin học, có lẽ không một ai chưa từng học qua cuốn sách được biên soạn bởi một thầy giáo trẻ những đầy tài năng của trường Đại học Sư phạm Hà Nội, thầy Lê Minh Hoàng.",
                    author: "Lê Minh Hoàng",
                    publisher: "NXB Online",
                    publishedYear: 2013,
                    favorite: 0,
                    category: {
                        id: 5,
                        name: "TECHNOLOGY"
                    }
                },
            ];

            let total = [];

            total = total.concat(skill);
            total = total.concat(sport);
            total = total.concat(comic);
            total = total.concat(language);
            total = total.concat(technology);

            console.log(total);

            total.forEach(function (book) {
                let blob = new Blob([JSON.stringify(book)], {type: "application/json"});
                let formData = new FormData();
                formData.append("book", blob);
                formData.append("image", "null");
                BookService.save(formData).done(function (response) {
                    $mdDialog.hide(response);
                });
            })
        }


    }
})();