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
        console.log('quantityInput: ' + quantityInput.value);
        console.log('quantity: ' + quantity);
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
            totalPriceElement.textContent = totalPrice;
        }

        updateTotalPaymentAmount();
        sendCartUpdate(productId, cartItemsId, quantity);
    }
});

// 최종 결제 금액 최신화 메소드
function updateTotalPaymentAmount() {
    let total = 0;
    const totalPriceElements = document.querySelectorAll('.total-price');

    totalPriceElements.forEach(elem => {
        const price = parseInt(elem.textContent.replace(/[^\d]/g, '')); // 숫자만 받음
        if (!isNaN(price)) {
            total += price;
        }
    });

    const totalPayment = document.getElementById('totalPaymentAmount');
    if (totalPayment) {
        totalPayment.textContent = total.toString() + '원';
    }
}

// 수량 변경 ajax 요청 메소드
function sendCartUpdate(productId, cartItemsId, quantity) {
    $.ajax({
        url: `/customers/carts/items/${cartItemsId}`,
        type: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify({
            productId: productId,
            quantity: quantity
        }),
        success: function (response) {
            console.log('장바구니 수량 업데이트 성공:', response.message);
        },
        error: function (xhr, status, error) {
            console.error('장바구니 수량 업데이트 실패:', xhr.responseText);
            alert('장바구니 수량 업데이트 중 오류가 발생했습니다.');
        }
    });
}