// 회원 장바구니 항목 삭제
$(document).ready(function () {
    $('.delete-item-btn').click(function () {
        const confirmed = confirm("이 상품을 장바구니에서 삭제하시겠습니까?");
        if (!confirmed) {
            return;
        }

        const cartItemsId = $(this).data('cartitemsid');
        console.log("삭제할 cartItemsId: ", cartItemsId);

        const row = $(this).closest('tr');

        $.ajax({
            url: `/members/carts/items/${cartItemsId}`,
            type: 'DELETE',
            beforeSend: function(xhr) {
                // CSRF 토큰이 있을 경우 설정 (Spring Security 사용 시)
                // const token = $('meta[name="_csrf"]').attr('content');
                // const header = $('meta[name="_csrf_header"]').attr('content');
                // if (token && header) {
                //     xhr.setRequestHeader(header, token);
                // }
            },
            success: function (response) {
                alert('장바구니에 삭제 되었습니다!');

                row.remove();
                recalculateTotalPrice();
            },
            error: function (xhr, status, error) {
                console.error('Ajax Error:', error);
                alert('장바구니 삭제에 실패했습니다.');
            }
        });
    });
});

// 총 결제 금액 재계산 함수
function recalculateTotalPrice() {
    let total = 0;
    $('.total-price').each(function () {
        const priceText = $(this).text().replace(/[^\d]/g, '');  // 숫자만 추출
        const price = parseInt(priceText, 10) || 0; // parseInt(priceText, 10): 10진수 정수로 변환(일부 브라우저에서 8진수나 16진수로 처리하는 오류가 발생할 수 있어서 항상 명시하는 게 안전)
        total += price;
    });

    $('#totalPaymentAmount').text(total.toString() + '원');

    if ($('tbody .delete-item-btn').length === 0) {
        $('.total-payment-price').closest('tr').hide();  // 총 결제 금액
        $('.out_button_area').hide();                            // 주문 버튼

        $('tbody').append('<tr><td colspan="4" style="text-align:center;">장바구니가 비어 있습니다.</td></tr>');
    }
}


// 회원 장바구니 전체 삭제
$(document).ready(function () {
    $('#clear-cart-btn').click(function () {
        const confirmed = confirm("장바구니를 비우겠습니까?");
        if (!confirmed) {
            return;
        }

        $.ajax({
            url: `/members/carts`,
            type: 'DELETE',
            contentType: 'application/json',
            beforeSend: function(xhr) {
            },
            success: function (response) {
                alert('장바구니를 비웠습니다!');
                $('#cart-container').hide();
                $('.table').append('<tbody><tr><td colspan="5" style="text-align:center;">장바구니가 비어 있습니다.</td></tr></tbody>');
            },
            error: function (xhr, status, error) {
                console.error('Ajax Error:', error);
                alert('장바구니 비우기를 실패했습니다.');
            }
        });
    });
});