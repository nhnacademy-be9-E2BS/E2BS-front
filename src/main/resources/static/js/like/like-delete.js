$(document).ready(function () {
    // 좋아요 버튼 클릭 시
    $('.like-button').on('click', function () {
        // 부모 요소에서 productId 와 liked 추출
        const button = $(this);
        const productId = button.data('product-id');

        $.ajax({
            type: 'DELETE',
            url: `/products/${productId}/likes`,
            success: function () {
                alert('좋아요가 취소되었습니다.');
                window.location.reload();
            },
            error: function (xhr, status, error) {
                console.error("에러 발생:", xhr.responseText);

                alert('좋아요 등록 실패');
            }
        });
    });
});