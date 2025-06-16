function getApiBaseUrl() {
    const origin = window.location.origin;

    if (origin.includes("localhost:10238")) {
        return "http://localhost:10232"; // 개발 환경
    }

    if (origin.includes("e2bs.shop")) {
        return "https://e2bs.shop";      // 운영 환경
    }

    return "http://localhost:10232";
}

function checkIdDuplicate() {
    const customerEmail = document.getElementById('customerEmail').value.trim();
    const messageElement = document.getElementById('idCheckMessage');

    if (!customerEmail) {
        messageElement.textContent = 'E-mail 주소를 입력해주세요.';
        messageElement.className = 'text-danger mt-1 d-block';
        return;
    }

    const apiBaseUrl = getApiBaseUrl();
    console.log('getApiBaseUrl(): ', apiBaseUrl)

    fetch(`${apiBaseUrl}/api/customers/register/${encodeURIComponent(customerEmail)}`)
        .then(response => {
            if (!response.ok) {
                throw new Error(`서버 오류: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            if (data.available) {
                messageElement.textContent = '사용 가능한 E-mail 주소입니다.';
                messageElement.className = 'text-success';
            } else {
                messageElement.textContent = '이미 사용 중인 E-mail 주소입니다.';
                messageElement.className = 'text-danger';
            }
        })
        .catch(error => {
            messageElement.textContent = '서버 오류가 발생했습니다.';
            messageElement.className = 'text-danger';
        });
}