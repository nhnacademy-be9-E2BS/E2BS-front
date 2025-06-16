// 리뷰 작성 모달
function openReviewCreateModal(button) {
    document.getElementById('createReviewModalProductId').value = button.getAttribute('data-product-id');

    // 모달 표시
    const modal = new bootstrap.Modal(document.getElementById('reviewCreateModal'));
    modal.show();

    // 닫는 버튼 클릭시 닫기
    $('.btn-close').off('click').on('click', function () {
        modal.hide();
    });
}

// 리뷰 수정 모달
function openReviewUpdateModal(button) {
    const orderDetailId = button.getAttribute('data-orderdetail-id');
    console.log('orderDetailId: ', orderDetailId)

    $.ajax({
        url: `/reviews/${orderDetailId}`,
        type: 'GET',
        success: function (response) {
            console.log("서버 응답:", response);

            // 필드에 값 채우기
            document.getElementById('updateReviewId').value = response.reviewId;
            document.getElementById('updateTextarea').value = response.reviewContent;

            // 별점 UI 업데이트
            document.querySelectorAll('#reviewUpdateModal .list .fa-star.fa-2x').forEach((el, index) => {
                if (index < response.reviewGrade) {
                    el.classList.add('filled');
                } else {
                    el.classList.remove('fa', 'fa-star', 'fa-2x');
                }
            });

            // 이미지가 있으면 미리보기 보여주기
            const imagePreview = document.getElementById('update-imagePreview');
            if (response.reviewImage) {
                imagePreview.src = response.reviewImage;
                imagePreview.style.display = 'block';
            } else {
                imagePreview.style.display = 'none';
            }

            // 모달 표시
            const modal = new bootstrap.Modal(document.getElementById('reviewUpdateModal'));
            modal.show();

            // 닫는 버튼 클릭시 닫기
            $('.btn-close').off('click').on('click', function () {
                modal.hide();
            });
        },
        error: function (xhr, status, error) {
            console.error('Error:', error);
            console.error('status:', status);
            console.error('xhr:', xhr);

            let message = '리뷰 수정 모달을 가져오는 중 오류가 발생했습니다. ' +  `(${xhr.responseJSON.status})`
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
}