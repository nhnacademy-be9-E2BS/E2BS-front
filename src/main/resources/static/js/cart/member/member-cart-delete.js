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
            url: `/members/carts/items`,
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
                if (response === 0) {
                    $(`.nav-shop__circle`).hide();
                } else {
                    $(`.nav-shop__circle`).text(response);
                }

                alert('장바구니에 삭제 되었습니다!');

                row.remove();
                updateTotalPayment();
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
                console.log(typeof response);
                console.log(response);
                console.log($('.nav-shop__circle').length);

                $(`.nav-shop__circle`).hide();

                alert('장바구니를 비웠습니다!');
                $('#cart-container').remove();
                $('.table').append('<tbody><tr><td colspan="6" style="text-align:center;">장바구니가 비어 있습니다.</td></tr></tbody>');
            },
            error: function (xhr, status, error) {
                console.error('Error:', error);
                console.error('status:', status);
                console.error('xhr:', xhr);

                let message = '장바구니 비우기를 실패했습니다. ' + `(${xhr.responseJSON.status})`;

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