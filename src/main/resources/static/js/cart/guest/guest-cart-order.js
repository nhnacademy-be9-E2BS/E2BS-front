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

        $.ajax({
            url: '/order/auth',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                productIds: selectedProductIds,
                cartQuantities: selectedCartQuantities
            }),
            success: function () {
                window.location.href = '/order/auth';
            },
            error: function () {
                let message = '주문 요청 중 오류가 발생했습니다. ' + `(${xhr.responseJSON.status})`;

                if (xhr.responseJSON) {
                    const rawTimestamp = xhr.responseJSON.timestamp;
                    let formattedTime = rawTimestamp;

                    try {
                        const date = new Date(rawTimestamp);
                        formattedTime = date.toLocaleString('ko-KR', {
                            year: 'numeric',
                            month: '2-digit',
                            day: '2-digit',
                            hour: '2-digit',
                            minute: '2-digit',
                            second: '2-digit',
                            hour12: false,
                            timeZone: 'Asia/Seoul'
                        });
                    } catch (e) {
                        console.warn("시간 포맷 변환 실패:", e);
                    }

                    // 전체 message 또는 trace에서 Exception 이후 메시지 한 줄 추출
                    const fullText = xhr.responseJSON.message || xhr.responseJSON.trace || '';
                    let extractedMessage = fullText;

                    const match = fullText.match(/Exception:\s*(.+?)\\r?\\n/);
                    if (match && match[1]) {
                        extractedMessage = match[1];
                    }

                    message += `\n\n발생 시간: ${formattedTime}` +
                        `\n에러 메시지: ${extractedMessage}`;
                }

                alert(message);
            }
        });
    });
});