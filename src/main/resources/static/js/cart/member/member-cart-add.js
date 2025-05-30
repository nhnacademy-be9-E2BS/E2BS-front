// 항목 체크에 대한 메소드
$(document).ready(function () {
    // 체크박스가 변경될 때마다 총액 재계산
    $('.cart-item-checkbox').change(function () {
        updateTotalPayment();
    });

    updateTotalPayment();
});
function updateTotalPayment() {
    let total = 0;

    $('.cart-item-checkbox:checked').each(function () {
        const productId = $(this).data('product-id');

        // 단가 가져오기
        const row = $(this).closest('tr');
        const priceText = row.find('.unit-price').text().replace(/[^0-9]/g, '');
        const unitPrice = parseInt(priceText, 10) || 0;

        // 수량 input 가져오기
        const quantity = parseInt($('#quantity-' + productId).val(), 10) || 1;

        total += unitPrice * quantity;
    });

    // 총 결제 금액 영역에 반영
    $('#totalPaymentAmount').text(total.toString() + '원');
}


// 회원 상품 장바구니에 추가
$(document).ready(function () {
    $('.member-add-cart-btn').click(function () {
        const card = $(this).closest('.card-product');
        const productId = card.data('product-id');
        let quantity = $('#quantity').val();
        if (isNaN(quantity)) {
            quantity = 1;
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
                alert('장바구니에 담겼습니다!');
            },
            error: function (xhr, status, error) {
                console.error('Error:', error);
                console.error('status:', status);
                console.error('xhr:', xhr);
                alert('장바구니 담기에 실패했습니다.');
            }
        });
    });
});