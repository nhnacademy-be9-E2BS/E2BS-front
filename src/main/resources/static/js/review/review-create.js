$(document).ready(function () {
    $('.form-review').on('submit', function (e) {
        e.preventDefault(); // 기본 form 제출 막기

        const form = $(this)[0];
        const formData = new FormData(form);

        // hidden 으로 선택된 평점 값도 포함
        const grade = $('#ratingInput').val();
        if (grade === "0") {
            alert("평점은 최소 1점입니다.");
            return;
        }
        formData.set("reviewGrade", grade);

        $.ajax({
            url: '/reviews',
            type: 'POST',
            data: formData,
            processData: false, // 중요 multipart 사용 시 false: false로 설정해야 FormData 사용 가능
            contentType: false, // 중요 multipart 사용 시 false: query string으로 변환되지 않도록 false 설정
            success: function () {
                alert("리뷰가 등록되었습니다.");
                location.reload();
            },
            error: function (xhr) {
                alert("리뷰 등록에 실패했습니다.\n상태 코드: " + xhr.status);
            }
        });
    });
});