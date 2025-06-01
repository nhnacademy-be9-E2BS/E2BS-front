$('#guestRegisterForm').on('submit', function (e) {
    e.preventDefault();

    const $form = $(this); // 현재 폼 스코프
    const registerData = {
        name: $form.find('input[name="name"]').val(),
        email: $form.find('input[name="email"]').val(),
        password: $form.find('input[name="password"]').val()
    };

    console.log(registerData)

    $.ajax({
        url: '/customers/register',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(registerData),
        success: function () {
            // 가입 성공 시 페이지 이동
            window.location.href = '/payment/checkout';
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