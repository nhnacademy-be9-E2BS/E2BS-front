// 항목 체크에 대한 이벤트 처리
$(document).ready(function () {
    // 개별 체크 시 총액 계산
    $('.cart-item-checkbox').change(function () {
        updateTotalPayment();

        // 개별 체크 해제 시 전체 선택 체크박스도 해제
        if (!$(this).is(':checked')) {
            $('#select-all').prop('checked', false);
        } else {
            // 모든 항목이 체크되었는지 확인
            const allChecked = $('.cart-item-checkbox').length === $('.cart-item-checkbox:checked').length;
            $('#select-all').prop('checked', allChecked);
        }
    });

    // 전체 선택 체크박스 동작
    $('#select-all').change(function () {
        const isChecked = $(this).is(':checked');
        $('.cart-item-checkbox').prop('checked', isChecked);
        updateTotalPayment();
    });

    // 초기 총액 계산
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

        // 주문 정보 객체 생성
        const cartOrder = {
            productIds: selectedProductIds,
            cartQuantities: selectedCartQuantities
        };

        console.log('cartOrder: ', cartOrder)

        // 직렬화 → Base64 인코딩
        const jsonStr = JSON.stringify(cartOrder);
        const encoded = btoa(unescape(encodeURIComponent(jsonStr)));  // 한글깨짐 방지

        console.log('encoded: ' + encoded)

        // 쿠키 저장
        document.cookie = `orderCart=${encoded}; path=/; max-age=${60 * 30}; secure; samesite=strict`;

        // 주문서 페이지로 GET 이동
        window.location.href = '/members/order';
    });
});