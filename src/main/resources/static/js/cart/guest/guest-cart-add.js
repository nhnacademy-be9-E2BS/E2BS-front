// 상품 장바구니에 추가
$(document).ready(function () {
    $('.guest-add-cart-btn').click(function () {
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
            url: '/guests/carts/items',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(
                {
                    productId: productId,
                    quantity: quantity
                }
            ),
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

                let message = '장바구니 담기에 실패했습니다. ' + `(${xhr.responseJSON.status})`;

                if (xhr.responseJSON) {
                    const timeStamp = xhr.responseJSON.timeStamp;
                    const errorTitle = xhr.responseJSON.title;

                    const jsonStart = errorTitle.indexOf('[{');
                    const jsonEnd = errorTitle.lastIndexOf('}]');

                    if (jsonStart !== -1 && jsonEnd !== -1) {
                        const jsonArrayStr = errorTitle.substring(jsonStart, jsonEnd + 2);
                        try {
                            const arr = JSON.parse(jsonArrayStr);
                            if (Array.isArray(arr) && arr.length > 0 && arr[0].title) {
                                extractedMessage = arr[0].title;
                            }
                        } catch (e) {
                            console.error('JSON 파싱 오류:', e);
                        }
                    }

                    message += `\n\n발생 시간: ${timeStamp}` +
                        `\n에러 메시지: ${extractedMessage}`;
                }

                alert(message);
            }
        });
    });
});