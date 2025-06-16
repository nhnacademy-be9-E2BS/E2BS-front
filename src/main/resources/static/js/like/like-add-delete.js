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

                let message;

                if (liked) {
                    message = '위시리스트 취소 실패했습니다. ' + `(${xhr.responseJSON.status})`
                } else {
                    message = '위시리스트 등록 실패했습니다. ' + `(${xhr.responseJSON.status})`
                }

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