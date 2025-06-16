function toggleEditForm(textId, editId) {
    // 현재 클릭한 리뷰만 토글
    const text = document.getElementById(textId);
    const editForm = document.getElementById(editId);

    if (editForm.style.display === 'none') {
        // 모든 수정 폼과 텍스트 초기화
        document.querySelectorAll('[id^="edit-form-"]').forEach(el => el.style.display = 'none');
        document.querySelectorAll('[id^="text-"]').forEach(el => el.style.display = 'block');

        editForm.style.display = 'block';
        text.style.display = 'none';
    } else {
        text.style.display = 'block';
        editForm.style.display = 'none';
    }
}

$(document).on('change', '.form-control', function (event) {
    const input = this;
    const file = input.files[0];

    // 파일명이 있는 경우만
    if (file) {
        const fileName = file.name;
        $(input).next('.custom-file-label').text(fileName);

        console.log('input.id: ', input.id);

        // 미리보기용 img ID 선택
        let previewId = '';
        if (input.id === 'createImage') {
            previewId = '#create-imagePreview';
        } else if (input.id === 'updateImage') {
            previewId = '#update-imagePreview';
        }

        const reader = new FileReader();
        reader.onload = function (e) {
            $(previewId).attr('src', e.target.result).show();
        };
        reader.readAsDataURL(file);
    }
});

<!--리뷰 내역에서의 수정 제출 메소드-->
function submitReviewUpdate(reviewId, index) {
    console.log('리뷰 제출', reviewId, index);

    const form = $(`#edit-form-${index} form`)[0];
    const formData = new FormData(form);

    const MAX_FILE_SIZE_MB = 5;
    const MAX_FILE_SIZE = MAX_FILE_SIZE_MB * 1024 * 1024;

    const files = $('#updateImage')[0].files;
    for (let i = 0; i < files.length; i++) {
        const file = files[i];
        if (file.size > MAX_FILE_SIZE) {
            alert(`파일은 ${MAX_FILE_SIZE_MB}MB를 초과할 수 없습니다.`);
            return;
        }
    }

    $.ajax({
        url: `/reviews/${reviewId}`,
        type: 'PUT',
        data: formData,
        contentType: false,      // 중요 multipart 사용 시 false: false로 설정해야 FormData 사용 가능
        processData: false,      // 중요 multipart 사용 시 false: query string으로 변환되지 않도록 false 설정
        success: function (response) {
            console.log("서버 응답:", response);

            alert('리뷰가 수정되었습니다.');


            // 텍스트 업데이트
            if (response.reviewContent !== '') {
                $(`#text-${index}`).text(response.reviewContent);
            }

            // 이미지 업데이트
            if (response.reviewImageUrl && response.reviewImageUrl.trim() !== '') {
                const imageContainer = $(`#image-${index}`);
                if (imageContainer.length) {
                    imageContainer.find('img')
                        .attr('src', response.reviewImageUrl)
                        .show();
                } else {
                    // div가 없을 경우 직접 추가할 수도 있음
                    const newImageHtml = `<div class="preview-container mt-2 text-center" id="image-${index}">
                                                <img src="${response.reviewImageUrl}" alt="리뷰 이미지"
                                                     style="max-width:100%; height:auto; border-radius:10px; box-shadow: 0 0 10px rgba(0,0,0,0.1);"
                                                     onerror="this.src='https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRIU-UceDHDzvuE5Gp1xYX0irHtgIWTeWwzlPVvLegZoes3vFaKT736CE8&s'">
                                            </div>`;
                    $(`#text-div-${index}`).before(newImageHtml);
                }
            }

            // 폼 닫기
            toggleEditForm(`text-${index}`, `edit-form-${index}`);
        },
        error: function (xhr, status, error) {
            console.error('Error:', error);
            console.error('status:', status);
            console.error('xhr:', xhr);

            let message = '리뷰 수정 중 오류가 발생했습니다. ' +  `(${xhr.responseJSON.status})`
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

<!--주문 내역에서의 리뷰 수정 모달에서 제출 메소드-->
function submitOrderDetailReviewUpdate() {
    const reviewId = document.getElementById('updateReviewId').value;
    console.log("리뷰 ID:", reviewId);

    const form = document.querySelector('#reviewUpdateModal form');
    const formData = new FormData(form);

    const MAX_FILE_SIZE_MB = 5;
    const MAX_FILE_SIZE = MAX_FILE_SIZE_MB * 1024 * 1024;

    const files = $('#updateImage')[0].files;
    for (let i = 0; i < files.length; i++) {
        const file = files[i];
        if (file.size > MAX_FILE_SIZE) {
            alert(`파일은 ${MAX_FILE_SIZE_MB}MB를 초과할 수 없습니다.`);
            return;
        }
    }

    $.ajax({
        url: `/reviews/${reviewId}`,
        type: 'PUT',
        data: formData,
        contentType: false,      // 중요 multipart 사용 시 false: false로 설정해야 FormData 사용 가능
        processData: false,      // 중요 multipart 사용 시 false: query string으로 변환되지 않도록 false 설정
        success: function (response) {
            console.log("서버 응답:", response);
            alert('리뷰가 수정되었습니다.');

            window.location.reload();
        },
        error: function (xhr, status, error) {
            console.error('Error:', error);
            console.error('status:', status);
            console.error('xhr:', xhr);

            let message = '리뷰 수정 중 오류가 발생했습니다. ' +  `(${xhr.responseJSON.status})`
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