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
    $('#totalPaymentAmount').text(total.toLocaleString('ko-KR') + '원');
}

// 체크된 상품 주문
$(document).ready(function () {
    $('#order-btn').click(function (e) {
        e.preventDefault();

        const confirmed = confirm("주문하시겠습니까?");
        if (!confirmed) {
            return;
        }

        // 체크된 항목들의 정보 수집
        const selectedProductIds = [];
        const selectedCartQuantities = [];
        $('.cart-item-checkbox:checked').each(function () {
            // 상품 ID
            const productId = $(this).data('product-id');
            // 해당 상품 담은 수량
            const quantityInput = $('#quantity-' + productId);
            const cartQuantity = parseInt(quantityInput.val(), 10) || 1;

            selectedProductIds.push(productId);
            selectedCartQuantities.push(cartQuantity);
        });

        if (selectedProductIds.length === 0) {
            alert("주문할 상품을 선택하세요.");
            return;
        }

        const form = document.createElement('form');
        form.method = 'POST';
        form.action = '/order';
        form.style.display = 'none';

        selectedProductIds.forEach(id => {
            const input = document.createElement('input');
            input.type = 'hidden';
            input.name = 'productIds';
            input.value = id;
            form.appendChild(input);
        });
        selectedCartQuantities.forEach(qty => {
            const input = document.createElement('input');
            input.type = 'hidden';
            input.name = 'cartQuantities';
            input.value = qty;
            form.appendChild(input);
        });

        document.body.appendChild(form);
        form.submit();
    });
});