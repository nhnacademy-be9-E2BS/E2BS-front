$('#guestLoginForm').on('submit', function (e) {
    e.preventDefault(); // 기본 제출 막기

    const $form = $(this); // 현재 폼 스코프
    const loginData = {
        email: $form.find('input[name="email"]').val(),
        password: $form.find('input[name="password"]').val()
    };

    console.log(loginData)

    $.ajax({
        url: '/customers/login',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(loginData),
        success: function () {
            // 로그인 성공 시 페이지 이동
            window.location.href = '/payment/checkout';
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