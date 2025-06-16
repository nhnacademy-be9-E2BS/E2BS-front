$(document).ready(function () {
    $('.form-review').on('submit', function (e) {
        e.preventDefault(); // 기본 form 제출 막기

        const form = $(this)[0];
        const formData = new FormData(form);

        // hidden 으로 선택된 평점 값도 포함
        const grade = $('#ratingInput').val();
        if (grade === "0") {
            alert("평점은 최소 1점입니다.");
            return;
        }
        formData.set("reviewGrade", grade);

        const MAX_FILE_SIZE_MB = 5;
        const MAX_FILE_SIZE = MAX_FILE_SIZE_MB * 1024 * 1024;

        const files = $('#createImage')[0].files;
        for (let i = 0; i < files.length; i++) {
            const file = files[i];
            if (file.size > MAX_FILE_SIZE) {
                alert(`파일은 ${MAX_FILE_SIZE_MB}MB를 초과할 수 없습니다.`);
                return;
            }
        }

        $.ajax({
            url: '/reviews',
            type: 'POST',
            data: formData,
            processData: false, // 중요 multipart 사용 시 false: false로 설정해야 FormData 사용 가능
            contentType: false, // 중요 multipart 사용 시 false: query string으로 변환되지 않도록 false 설정
            success: function () {
                alert("리뷰가 등록되었습니다.");
                window.location.reload();
            },
            error: function (xhr, status, error) {
                console.error('Error:', error);
                console.error('status:', status);
                console.error('xhr:', xhr.responseJSON);
                console.error('xhr:', xhr.responseJSON.title);

                let message = '리뷰 등록에 실패했습니다. ' +  `(${xhr.responseJSON.status})`;

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