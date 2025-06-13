// 회원 상품 장바구니에 추가
$(document).ready(function () {
    $('.member-add-cart-btn').click(function () {
        let card = $(this).closest('.card-product');
        if (card.length === 0) {
            card = $(this).closest('.book-card');
        }

        const productId = card.data('product-id');
        let quantity = $('#quantity').val();
        if (isNaN(quantity)) {
            quantity = $('#quantity__' + productId).val();
            if (isNaN(quantity)) {
                quantity = 1;
            }
        }

        $.ajax({
            url: '/members/carts/items',
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
                console.log(typeof response);
                console.log(response);

                $('.nav-shop__circle')
                    .text(response)
                    .css('display', response > 0 ? 'inline-block' : 'none');

                // 모달 표시
                const modal = new bootstrap.Modal(document.getElementById('cartConfirmModal'));
                modal.show();

                // 장바구니 이동 버튼 클릭 시 이동
                $('#goToCartBtn').off('click').on('click', function () {
                    window.location.href = '/members/carts';
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

                let message = '장바구니 담기에 실패했습니다. ' + `(${xhr.responseJSON.status})`

                if (xhr.responseJSON) {
                    const rawTimestamp = xhr.responseJSON.timestamp;
                    let formattedTime = rawTimestamp;

                    try {
                        const date = new Date(rawTimestamp);
                        formattedTime = date.toLocaleString('ko-KR', {
                            year: 'numeric',
                            month: '2-digit',
                            day: '2-digit',
                            hour: '2-digit',
                            minute: '2-digit',
                            second: '2-digit',
                            hour12: false,
                            timeZone: 'Asia/Seoul'
                        });
                    } catch (e) {
                        console.warn("시간 포맷 변환 실패:", e);
                    }

                    // 전체 message 또는 trace에서 Exception 이후 메시지 한 줄 추출
                    const fullText = xhr.responseJSON.message || xhr.responseJSON.trace || '';
                    let extractedMessage = fullText;

                    const match = fullText.match(/Exception:\s*(.+?)\\r?\\n/);
                    if (match && match[1]) {
                        extractedMessage = match[1];
                    }

                    message += `\n\n발생 시간: ${formattedTime}` +
                        `\n에러 메시지: ${extractedMessage}`;
                }

                alert(message);
            }
        });
    });
});