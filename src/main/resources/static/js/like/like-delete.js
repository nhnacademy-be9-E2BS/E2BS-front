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