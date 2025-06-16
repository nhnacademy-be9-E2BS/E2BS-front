// 고객 장바구니 항목 수량 변경
document.addEventListener('DOMContentLoaded', function () {
    const increaseButtons = document.querySelectorAll('.increase.items-count');
    const decreaseButtons = document.querySelectorAll('.reduced.items-count');

    increaseButtons.forEach(button => {
        button.addEventListener('click', () => updateQuantity(button.dataset.productid, 1));
    });

    decreaseButtons.forEach(button => {
        button.addEventListener('click', () => updateQuantity(button.dataset.productid, -1));
    });

    function updateQuantity(productId, change) {
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
        const checkbox = row.querySelector('.cart-item-checkbox');

        if (quantity === 0) {
            // 체크 해제 후 합계 업데이트 한 후에 그 다음 행 제거
            checkbox.checked = false;
            row.remove();
        } else {
            const unitPriceText = row.querySelector('.unit-price').textContent;
            const unitPrice = parseInt(unitPriceText.replace(/[^\d]/g, ''));
            const totalPrice = unitPrice * quantity;
            const totalPriceElement = row.querySelector('.total-price');
            totalPriceElement.textContent = totalPrice.toLocaleString('ko-KR') + '원'
        }

        updateTotalPayment();
        sendCartUpdate(productId, quantity);
    }
});

// 상품 합계, 배송비, 결제 예정 금액 최신화 메소드
function updateTotalPayment() {
    let totalProduct = 0;

    const currentDeliveryPrice = parseInt($('.delivery-price').first().text().replace(/[^0-9]/g, ''), 10) || 0;
    const deliveryFreeAmount = parseInt($('.current-delivery-free-amount').first().text().replace(/[^0-9]/g, ''), 10) || 0;

    $('.cart-item-checkbox:checked').each(function () {
        // row가 DOM에 남아있지 않으면 무시
        if (!document.body.contains(this)) return;

        const productId = $(this).data('product-id');
        const row = $(this).closest('tr');
        const unitPrice = parseInt(row.find('.unit-price').text().replace(/[^0-9]/g, ''), 10);
        const quantity = parseInt($('#quantity-' + productId).val(), 10) || 0;

        totalProduct += unitPrice * quantity;
    });

    console.log($('.cart-item-checkbox:checked').map((i, el) => el.id).get());
    console.log('총상품금액:', totalProduct);
    console.log('무료배송 기준금액:', deliveryFreeAmount);
    console.log('배송비:', currentDeliveryPrice);

    const totalDelivery = (totalProduct > 0 && totalProduct < deliveryFreeAmount) ? currentDeliveryPrice : 0;

    $('#totalProductPrice').text(totalProduct.toLocaleString('ko-KR') + '원');
    $('#totalDeliveryPrice').text(totalDelivery.toLocaleString('ko-KR') + '원');
    $('#totalPaymentPrice').text((totalProduct + totalDelivery).toLocaleString('ko-KR') + '원');

    if ($('tbody .delete-item-btn').length === 0) {
        $('.total-product-price').closest('tr').hide();
        $('.out_button_area').hide();
        $('tbody').append('<tr><td colspan="6" style="text-align:center;">장바구니가 비어 있습니다.</td></tr>');
    }
}


// 수량 변경 ajax 요청 메소드
function sendCartUpdate(productId, quantity) {
    $.ajax({
        url: `/members/carts/items`,
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

            let message = '장바구니 수량 업데이트에 실패했습니다. ' + `(${xhr.responseJSON.status})`;

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
}