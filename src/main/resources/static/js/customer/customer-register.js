window.addEventListener('DOMContentLoaded', function () {
    document.getElementById('guestRegisterForm').addEventListener('submit', function (e) {
        e.preventDefault();

        const $form = $(this); // 현재 폼 스코프
        const registerData = {
            customerEmail: $form.find('input[name="customerEmail"]').val(),
            customerName: $form.find('input[name="customerName"]').val(),
            customerPassword: $form.find('input[name="customerPassword"]').val(),
            customerPasswordCheck: $form.find('input[name="customerPasswordCheck"]').val()
        };

        console.log(registerData)

        $.ajax({
            url: '/customers/register',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(registerData),
            success: function (responseData) {
                console.log("가입 응답 받기: ", JSON.stringify(responseData))

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

                // customerName
                form.append($('<input>', {
                    type: 'hidden',
                    name: 'customerName',
                    value: responseData.customerName
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
                if (xhr.status === 400) {
                    alert('입력값이 유효하지 않습니다. 다시 확인해주세요.');
                } else {
                    alert('회원가입 중 오류가 발생했습니다.');
                }
            }
        });
    });
});