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
            success: function () {
                // 로그인 성공 시 주문서로 이동 요청
                $.ajax({
                    url: '/guests/orders',
                    type: 'POST',
                    contentType: 'application/json',
                    data: responseData,
                    success: function () {
                    },
                    error: function (xhr) {
                        alert('주문서 요청에서 오류가 발생했습니다.');
                    }
                });
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