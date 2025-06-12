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
                console.error('xhr:', xhr);

                let message = '리뷰 등록에 실패했습니다. ' +  `(${xhr.responseJSON.status})`
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