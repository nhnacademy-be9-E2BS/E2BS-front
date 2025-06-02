$(document).ready(function () {
    // 좋아요 버튼 클릭 시
    $('.like-button').on('click', function () {
        // 부모 요소에서 productId 와 liked 추출
        const card = $(this).closest('.card-product');
        const productId = card.data('product-id');

        const button = $(this);
        const liked = button.data('liked');          //  jQuery의 .data()는 자동으로 불리언 처리
        const method = liked ? 'DELETE' : 'POST';
        const icon = button.find('i');

        $.ajax({
            type: method,
            url: `/products/${productId}/likes`,
            success: function () {
                button.data('liked', !liked);

                if (liked) {
                    alert('좋아요가 취소되었습니다.')
                    icon.removeClass('liked');
                } else {
                    alert('좋아요가 등록되었습니다.')
                    icon.addClass('liked');
                }
            },
            error: function (xhr, status, error) {
                console.error('Error:', error);
                console.error('status:', status);
                console.error('xhr:', xhr);

                let message = '';

                if (liked) {
                    message = '좋아요 취소 실패했습니다.'
                } else {
                    message = '좋아요 등록 실패했습니다.'
                }

                if (xhr.responseJSON) {
                    message += `\n에러 메시지: ${xhr.responseJSON.title}\n` +
                               `상태 코드: ${xhr.responseJSON.status}\n` +
                               `발생 시간: ${xhr.responseJSON.timeStamp}`;
                }

                alert(message);
            }
        });
    });
});