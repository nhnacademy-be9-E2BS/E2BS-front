// 고객 장바구니 항목 수량 변경
document.addEventListener('DOMContentLoaded', function () {
    const increaseButtons = document.querySelectorAll('.increase.items-count');
    const decreaseButtons = document.querySelectorAll('.reduced.items-count');

    increaseButtons.forEach(button => {
        button.addEventListener('click', () => updateQuantity(button.dataset.productid, button.dataset.cartitemsid, 1));
    });

    decreaseButtons.forEach(button => {
        button.addEventListener('click', () => updateQuantity(button.dataset.productid, button.dataset.cartitemsid, -1));
    });

    function updateQuantity(productId, cartItemsId, change) {
        console.log('cartItemsId: ' + cartItemsId)
        console.log('productId: ' + productId)

        const quantityInput = document.getElementById(`quantity-${productId}`);
        let quantity = parseInt(quantityInput.value);
        if (isNaN(quantity)) {
            quantity = 0;
        }

        // 음수 방지
        quantity = Math.max(0, quantity + change);
        quantityInput.value = quantity;


        const row = quantityInput.closest('tr');

        if (quantity === 0) {
            row.remove();
        } else {
            const unitPriceText = row.querySelector('.unit-price').textContent;
            const unitPrice = parseInt(unitPriceText.replace(/[^\d]/g, ''));
            const totalPrice = unitPrice * quantity;
            const totalPriceElement = row.querySelector('.total-price');
            totalPriceElement.textContent = totalPrice.toLocaleString('ko-KR') + '원'
        }

        updateTotalPaymentAmount();
        sendCartUpdate(productId, cartItemsId, quantity);
    }
});

// 상품 합계, 배송비, 결제 예정 금액 최신화 메소드
function updateTotalPaymentAmount() {
    let totalProduct = 0;
    let totalDelivery = 0;

    $('.cart-item-checkbox:checked').each(function () {
        const productId = $(this).data('product-id');
        const row = $(this).closest('tr');

        const priceText = row.find('.unit-price').text().replace(/[^0-9]/g, '');
        const unitPrice = parseInt(priceText, 10) || 0;

        const quantity = parseInt($('#quantity-' + productId).val(), 10) || 1;
        totalProduct += unitPrice * quantity;

        const deliveryText = row.find('.unit-delivery-price').text().replace(/[^0-9]/g, '');
        const deliveryFee = parseInt(deliveryText, 10) || 0;
        totalDelivery += deliveryFee;
    });

    $('#totalProductPrice').text(totalProduct.toLocaleString('ko-KR') + '원');
    $('#totalDeliveryPrice').text(totalDelivery.toLocaleString('ko-KR') + '원');
    $('#totalPaymentPrice').text((totalProduct + totalDelivery).toLocaleString('ko-KR') + '원');
}


// 수량 변경 ajax 요청 메소드
function sendCartUpdate(productId, cartItemsId, quantity) {
    $.ajax({
        url: `/members/carts/items/${cartItemsId}`,
        type: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify({
            productId: productId,
            quantity: quantity
        }),
        success: function (response) {
            console.log(typeof response);
            console.log(response);

            if (response === 0) {
                $(`.nav-shop__circle`).hide();
            } else {
                $(`.nav-shop__circle`).text(response);
            }
            console.log('장바구니 수량 업데이트 성공:', response.message);
        },
        error: function (xhr, status, error) {
            console.error('Error:', error);
            console.error('status:', status);
            console.error('xhr:', xhr);

            let message = '장바구니 수량 업데이트에 실패했습니다.';
            if (xhr.responseJSON) {
                message += `\n에러 메시지: ${xhr.responseJSON.title}\n` +
                           `상태 코드: ${xhr.responseJSON.status}\n` +
                           `발생 시간: ${xhr.responseJSON.timeStamp}`;
            }

            alert(message);
        }
    });
}