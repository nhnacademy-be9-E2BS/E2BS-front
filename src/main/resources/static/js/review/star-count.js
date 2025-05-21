let selectedRating = 0;

const stars = document.querySelectorAll('.star');
const selectedRatingText = document.getElementById('selectedRating');
const hiddenRatingInput = document.getElementById('ratingInput');

// 별 클릭 이벤트 처리
stars.forEach(star => {
    star.addEventListener('click', function(event) {
        // 기본 링크 동작 방지
        event.preventDefault();

        // 클릭된 별의 value 값
        const ratingValue = parseInt(this.getAttribute('data-star-value'));

        // 선택된 평점 값 업데이트
        selectedRating = ratingValue;
        selectedRatingText.innerText = `${selectedRating}점`;
        hiddenRatingInput.value = selectedRating;

        // 별의 시각적 표시 업데이트
        updateStarDisplay(ratingValue);
    });
});

// 별 표시 업데이트 함수
function updateStarDisplay(rating) {
    stars.forEach(star => {
        const starValue = parseInt(star.getAttribute('data-star-value'));
        const starIcon = star.querySelector('i');

        if (starValue <= rating) {
            // 채운 별로 설정
            starIcon.classList.add('filled');
        } else {
            // 빈 별로 설정
            starIcon.classList.remove('filled');
        }
    });
}