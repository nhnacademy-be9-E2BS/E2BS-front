// 체크할 때마다 체크된 상품의 총합으로 최신화
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