document.addEventListener('DOMContentLoaded', () => {
    userRegisterHandler();
    document.getElementById('checkUsernameBtn').addEventListener('click', checkUsername);
});

export const userRegisterHandler = () => {
    document.getElementById('registerForm').addEventListener('submit', (event) => {
        event.preventDefault(); // 기본 submit 동작 방지

        const formData = new FormData();
        formData.append('username', document.getElementById('username').value);
        formData.append('password', document.getElementById('password').value);
        formData.append('name', document.getElementById('name').value);
        formData.append('nickname', document.getElementById('nickname').value);
        formData.append('phone', document.getElementById('phone').value);
        formData.append('userAddress', document.getElementById('locationInput').value);
        formData.append('gender', document.getElementById('gender').value);
        formData.append('profileImage', document.getElementById('profileImage').files[0]); // 파일 추가

        axios({
            method: 'POST',
            url: '/api/users/register',
            data: formData,
            headers: {
                'Content-Type': 'multipart/form-data' // 필요 시 명시적으로 설정
            }
        })
            .then(response => {
                if (response.status === 200) {
                    alert('회원가입 성공');
                    // 리디렉션을 원한다면 여기서 처리
                    // window.location.href = '/api/products/list'; // 필요에 따라 조정
                    window.location.reload();
                }
            })
            .catch(error => {
                alert("error: " + error);
            });
    });
}

export const checkUsername = () => {
    const username = document.getElementById('username').value;
    if (username.length < 8) {
        alert('사용자 이름을 입력하세요.');
        return;
    }
    // Axios를 사용하여 아이디 중복 체크
    axios({
        method: 'POST',
        url: '/api/users/checkUsername',
        headers: {'Content-Type': 'application/json'},
        data: {username}
    })
        .then(response => {
            if (response.data) {
                alert('사용 가능한 사용자 이름입니다.');
            } else {
                alert('이미 사용 중인 사용자 이름입니다.');
            }
        })
        .catch(error => {
            console.error('서버 오류:', error);
            alert('서버 오류가 발생했습니다. 나중에 다시 시도하세요.');
        });
}
