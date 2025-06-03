// 상품 장바구니에 추가
$(document).ready(function () {
    $('.guest-add-cart-btn').click(function () {
        const card = $(this).closest('.card-product');
        const productId = card.data('product-id');
        let quantity = $('#quantity').val();
        if (isNaN(quantity)) {
            quantity = 1;
        }

        $.ajax({
            url: '/guests/carts/items',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(
                {
                    productId: productId,
                    quantity: quantity
                }
            ),
            beforeSend: function(xhr) {
                // CSRF 토큰이 있을 경우 설정 (Spring Security 사용 시)
                // const token = $('meta[name="_csrf"]').attr('content');
                // const header = $('meta[name="_csrf_header"]').attr('content');
                // if (token && header) {
                //     xhr.setRequestHeader(header, token);
                // }
            },
            success: function (response) {
                // 모달 표시
                const modal = new bootstrap.Modal(document.getElementById('cartConfirmModal'));
                modal.show();

                // 장바구니 이동 버튼 클릭 시 이동
                $('#goToCartBtn').off('click').on('click', function () {
                    window.location.href = '/guests/carts';
                });

                // 닫는 버튼 클릭시 닫기
                $('#closeBtn').off('click').on('click', function () {
                    modal.hide();
                });
                $('#continueBtn').off('click').on('click', function () {
                    modal.hide();
                });
            },
            error: function (xhr, status, error) {
                console.error('Error:', error);
                console.error('status:', status);
                console.error('xhr:', xhr);

                let message = '장바구니 담기에 실패했습니다.';
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