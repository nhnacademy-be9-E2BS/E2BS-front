$(document).ready(function () {
    // 좋아요 버튼 클릭 시
    $('.like-button').on('click', function () {
        // 부모 요소에서 productId 와 liked 추출
        let card = $(this).closest('.card-product');
        if (card.length === 0) {
            card = $(this).closest('.book-card');
        }

        const productId = card.data('product-id');

        $.ajax({
            type: 'DELETE',
            url: `/products/${productId}/likes`,
            success: function () {
                alert('위시리스트에 취소되었습니다.');
                window.location.reload();
            },
            error: function (xhr, status, error) {
                console.error('Error:', error);
                console.error('status:', status);
                console.error('xhr:', xhr);

                let message = '위시리스트 등록 실패했습니다. ' +  `(${xhr.responseJSON.status})`
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