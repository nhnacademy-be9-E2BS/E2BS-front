$(document).ready(function () {
    // 좋아요 버튼 클릭 시
    $('.like-button').on('click', function () {
        // 부모 요소에서 productId 와 liked 추출
        let card = $(this).closest('.card-product');
        if (card.length === 0) {
            card = $(this).closest('.book-card');
        }

        const productId = card.data('product-id');

        const button = $(this);
        const liked = button.data('liked');          //  jQuery의 .data()는 자동으로 불리언 처리
        const method = liked ? 'DELETE' : 'POST';
        const icon = button.find('i');

        console.log('product-id: ', productId);
        console.log('liked: ', liked);

        $.ajax({
            type: method,
            url: `/products/${productId}/likes`,
            success: function () {
                button.data('liked', !liked);

                if (liked) {
                    alert('위시리스트에 취소되었습니다.')
                    icon.removeClass('liked');
                    button.text('위시리스트 담기')
                } else {
                    alert('위시리스트에 등록되었습니다.')
                    icon.addClass('liked');
                    button.text('위시리스트 취소')
                }

            },
            error: function (xhr, status, error) {
                console.error('Error:', error);
                console.error('status:', status);
                console.error('xhr:', xhr);
                console.error('xhr.responseJSON:', xhr.responseJSON);
                console.error('xhr.responseJSON message:', xhr.responseJSON.message);

                let message = '';

                if (liked) {
                    message = '위시리스트 취소 실패했습니다. ' + `(${xhr.responseJSON.status})`
                } else {
                    message = '위시리스트 등록 실패했습니다. ' + `(${xhr.responseJSON.status})`
                }

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