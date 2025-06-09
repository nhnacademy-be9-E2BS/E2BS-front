// 게스트 장바구니 항목 삭제
$(document).ready(function () {
    $('.delete-item-btn').click(function () {
        const confirmed = confirm("이 상품을 장바구니에서 삭제하시겠습니까?");
        if (!confirmed) {
            return;
        }

        const productId = $(this).data('productid');
        console.log("삭제할 productId: ", productId);

        const row = $(this).closest('tr');

        $.ajax({
            url: `/guests/carts/items`,
            type: 'DELETE',
            contentType: 'application/json',
            data: JSON.stringify({
                productId: productId,
                sessionId: ""
            }),
            beforeSend: function(xhr) {
                // CSRF 토큰이 있을 경우 설정 (Spring Security 사용 시)
                // const token = $('meta[name="_csrf"]').attr('content');
                // const header = $('meta[name="_csrf_header"]').attr('content');
                // if (token && header) {
                //     xhr.setRequestHeader(header, token);
                // }
            },
            success: function (response) {
                console.log(typeof response);
                console.log(response);
                console.log($('.nav-shop__circle').length);

                if (response === 0) {
                    $(`.nav-shop__circle`).hide();
                } else {
                    $(`.nav-shop__circle`).text(response);
                }

                alert('장바구니에 삭제 되었습니다!');

                row.remove();
                recalculateTotalPrice();
            },
            error: function (xhr, status, error) {
                console.error('Error:', error);
                console.error('status:', status);
                console.error('xhr:', xhr);

                let message = '장바구니 담기에 실패했습니다.';
                if (xhr.responseJSON) {
                    message += `\n에러 메시지: ${xhr.responseJSON.title}\n` +
                               `상태 코드: ${xhr.responseJSON.status}\n` +
                               `발생 시간: ${xhr.responseJSON.timeStamp}`;
                }

                alert(message);
            }
        });
    });
});

// 총 결제 금액 재계산 함수
function recalculateTotalPrice() {
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

    if ($('tbody .delete-item-btn').length === 0) {
        $('.total-product-price').closest('tr').hide();
        $('.out_button_area').hide();
        $('tbody').append('<tr><td colspan="6" style="text-align:center;">장바구니가 비어 있습니다.</td></tr>');
    }
}


// 게스트 장바구니 전체 삭제
$(document).ready(function () {
    $('#clear-cart-btn').click(function () {
        const confirmed = confirm("장바구니를 비우겠습니까?");
        if (!confirmed) {
            return;
        }

        $.ajax({
            url: `/guests/carts`,
            type: 'DELETE',
            contentType: 'application/json',
            beforeSend: function(xhr) {
                // CSRF 토큰이 있을 경우 설정 (Spring Security 사용 시)
                // const token = $('meta[name="_csrf"]').attr('content');
                // const header = $('meta[name="_csrf_header"]').attr('content');
                // if (token && header) {
                //     xhr.setRequestHeader(header, token);
                // }
            },
            success: function (response) {
                console.log(typeof response);
                console.log(response);

                $(`.nav-shop__circle`).hide();

                alert('장바구니를 비웠습니다!');
                $('#cart-container').hide();
                $('.table').append('<tbody><tr><td colspan="6" style="text-align:center;">장바구니가 비어 있습니다.</td></tr></tbody>');
            },
            error: function (xhr, status, error) {
                console.error('Error:', error);
                console.error('status:', status);
                console.error('xhr:', xhr);

                let message = '장바구니 비우기를 실패했습니다.';
                if (xhr.responseJSON) {
                    message += `\n에러 메시지: ${xhr.responseJSON.title}\n` +
                               `상태 코드: ${xhr.responseJSON.status}\n` +
                               `발생 시간: ${xhr.responseJSON.timeStamp}`;
                }

                alert(message);
            }
        });
    });
});