// 게스트 장바구니 항목 수량 변경
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
        sendCartUpdate(productId, quantity);

        console.log('productId: ' + productId);
    }
});

function updateTotalPaymentAmount() {
    let total = 0;
    const totalPriceElements = document.querySelectorAll('.total-price');

    totalPriceElements.forEach(elem => {
        // 숫자만 받음
        const price = parseInt(elem.textContent.replace(/[^\d]/g, ''));
        if (!isNaN(price)) {
            total += price;
        }
    });

    const totalPayment = document.getElementById('totalPaymentAmount');
    if (totalPayment) {
        totalPayment.textContent = total.toLocaleString('ko-KR') + '원'
    }
}

function sendCartUpdate(productId, quantity) {
    $.ajax({
        url: `/guests/carts/items`,
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