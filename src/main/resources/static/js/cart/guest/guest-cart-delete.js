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
                updateTotalPayment();
            },
            error: function (xhr, status, error) {
                console.error('Error:', error);
                console.error('status:', status);
                console.error('xhr:', xhr);

                let message = '장바구니 항목 삭제에 실패했습니다. ' + `(${xhr.responseJSON.status})`;

                if (xhr.responseJSON) {
                    const timeStamp = xhr.responseJSON.timeStamp;
                    const errorTitle = xhr.responseJSON.title;

                    const jsonStart = errorTitle.indexOf('[{');
                    const jsonEnd = errorTitle.lastIndexOf('}]');

                    if (jsonStart !== -1 && jsonEnd !== -1) {
                        const jsonArrayStr = errorTitle.substring(jsonStart, jsonEnd + 2);
                        try {
                            const arr = JSON.parse(jsonArrayStr);
                            if (Array.isArray(arr) && arr.length > 0 && arr[0].title) {
                                extractedMessage = arr[0].title;
                            }
                        } catch (e) {
                            console.error('JSON 파싱 오류:', e);
                        }
                    }

                    message += `\n\n발생 시간: ${timeStamp}` +
                        `\n에러 메시지: ${extractedMessage}`;
                }

                alert(message);
            }
        });
    });
});

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

                let message = '장바구니 비우기를 실패했습니다. ' + `(${xhr.responseJSON.status})`;

                if (xhr.responseJSON) {
                    const timeStamp = xhr.responseJSON.timeStamp;
                    const errorTitle = xhr.responseJSON.title;

                    const jsonStart = errorTitle.indexOf('[{');
                    const jsonEnd = errorTitle.lastIndexOf('}]');

                    if (jsonStart !== -1 && jsonEnd !== -1) {
                        const jsonArrayStr = errorTitle.substring(jsonStart, jsonEnd + 2);
                        try {
                            const arr = JSON.parse(jsonArrayStr);
                            if (Array.isArray(arr) && arr.length > 0 && arr[0].title) {
                                extractedMessage = arr[0].title;
                            }
                        } catch (e) {
                            console.error('JSON 파싱 오류:', e);
                        }
                    }

                    message += `\n\n발생 시간: ${timeStamp}` +
                        `\n에러 메시지: ${extractedMessage}`;
                }

                alert(message);
            }
        });
    });
});