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

        $.ajax({
            url: '/order',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                productIds: selectedProductIds,
                cartQuantities: selectedCartQuantities
            }),
            success: function () {
            },
            error: function () {
                alert("주문 요청 중 오류가 발생했습니다.");
            }
        });
    });
});