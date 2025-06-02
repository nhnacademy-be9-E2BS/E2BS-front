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

$(document).on('change', '.custom-file-input', function (event) {
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

function submitReviewUpdate(reviewId, index) {
    const form = $(`#edit-form-${index} form`)[0];
    const formData = new FormData(form);

    $.ajax({
        url: `/reviews/${reviewId}`,
        type: 'PUT',
        data: formData,
        contentType: false,      // 중요 multipart 사용 시 false: false로 설정해야 FormData 사용 가능
        processData: false,      // 중요 multipart 사용 시 false: query string으로 변환되지 않도록 false 설정
        success: function (response) {
            console.log("서버 응답:", response);

            alert('리뷰가 성공적으로 수정되었습니다.');


            // 텍스트 업데이트
            if (response.reviewContent !== '') {
                $(`#text-${index}`).text(response.reviewContent);
            }

            // 이미지 업데이트
            if (response.reviewImageUrl !== '') {
                $(`#image-${index} img`).attr('src', response.reviewImageUrl).show();
            }

            // 폼 닫기
            toggleEditForm(`text-${index}`, `edit-form-${index}`);
        },
        error: function (xhr, status, error) {
            console.error('Error:', error);
            console.error('status:', status);
            console.error('xhr:', xhr);

            let message = '리뷰 수정 중 오류가 발생했습니다.';
            if (xhr.responseJSON) {
                message += `\n에러 메시지: ${xhr.responseJSON.title}\n` +
                           `상태 코드: ${xhr.responseJSON.status}\n` +
                           `발생 시간: ${xhr.responseJSON.timeStamp}`;
            }

            alert(message);
        }
    });
}