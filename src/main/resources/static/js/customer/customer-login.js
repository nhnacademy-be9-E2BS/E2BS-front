window.addEventListener('DOMContentLoaded', function () {
    document.getElementById('guestLoginForm').addEventListener('submit', function (e) {
        e.preventDefault(); // 기본 제출 막기

        const $form = $(this); // 현재 폼 스코프
        const loginData = {
            customerEmail: $form.find('input[name="customerEmail"]').val(),
            customerPassword: $form.find('input[name="customerPassword"]').val()
        };

        console.log(loginData)

        $.ajax({
            url: '/customers/login',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(loginData),
            success: function (responseData) {
                // 로그인 성공 시 주문서로 이동 요청
                console.log("로그인 응답 받기: ", JSON.stringify(responseData))

                // 가입 성공 시 주문서로 이동 요청
                // form 생성해서 responseData 전송
                const form = $('<form>', {
                    action: '/customers/order',
                    method: 'POST'
                });

                // customerId
                form.append($('<input>', {
                    type: 'hidden',
                    name: 'customerId',
                    value: responseData.customerId
                }));

                // productIds 배열
                responseData.requestCartOrder.productIds.forEach((productIds, index) => {
                    form.append($('<input>', {
                        type: 'hidden',
                        name: 'requestCartOrder.productIds[' + index + ']',
                        value: productIds
                    }));
                });

                // cartQuantities 배열
                responseData.requestCartOrder.cartQuantities.forEach((cartQuantities, index) => {
                    form.append($('<input>', {
                        type: 'hidden',
                        name: 'requestCartOrder.cartQuantities[' + index + ']',
                        value: cartQuantities
                    }));
                });

                // 페이지에 폼 추가 후 제출
                $('body').append(form);
                form.submit();
            },
            error: function (xhr) {
                if (xhr.status === 401) {
                    alert('아이디 또는 비밀번호가 일치하지 않습니다.');
                } else {
                    alert('알 수 없는 오류가 발생했습니다.');
                }
            }
        });
    });
});