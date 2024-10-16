/*!
* Start Bootstrap - Shop Homepage v5.0.6 (https://startbootstrap.com/template/shop-homepage)
* Copyright 2013-2023 Start Bootstrap
* Licensed under MIT (https://github.com/StartBootstrap/startbootstrap-shop-homepage/blob/master/LICENSE)
*/
// This file is intentionally blank
// Use this file to add JavaScript to your project

// 위치 검색을 실행하는 함수
const getLocationList = () => {
    const locationName = document.getElementById('locationInput').value;

    if (locationName.length < 2) {
        document.getElementById('locationResult').style.display = 'none'; // 입력이 없으면 숨김
        return;
    }

    axios({
        method: 'GET',
        url: `/api/location/search`,
        params: {locationName}
    })
        .then(response => {
            const data = response.data;
            const resultsContainer = document.getElementById('locationResult');
            resultsContainer.innerHTML = ''; // 이전 결과 지우기

            if (data.length > 0) {
                data.forEach(locationName => {
                    const productElement = document.createElement('div');
                    productElement.className = 'list-group-item';
                    productElement.innerText = locationName;
                    productElement.onclick = () => selectLocation(locationName); // 클릭 이벤트 추가
                    resultsContainer.appendChild(productElement);
                });
                resultsContainer.style.display = 'block'; // 결과가 있으면 표시
            } else {
                resultsContainer.style.display = 'none'; // 결과가 없으면 숨김
            }
        })
        .catch(error => {
            console.error('Error fetching location:', error);
        });

    // 선택된 위치를 입력 필드에 추가
    const selectLocation = (locationName) => {
        console.log(locationName);
        document.getElementById('locationInput').value = locationName;
        document.getElementById('locationResult').style.display = 'none'; // 결과 숨김
    };
};

document.addEventListener("DOMContentLoaded", () => {
    // "로그인" 버튼 클릭 이벤트
    document.getElementById('btn-login').addEventListener('click', () => {
        // 폼 데이터 수집
        const username = document.getElementById('username').value;
        const password = document.getElementById('password').value;

        console.log(username, password);

        // 폼 데이터 유효성 검사
        if (username === '' || password === '') {
            alert('아이디와 비밀번호를 입력하세요.');
            return;
        }

        axios({
            method: 'POST',
            url: '/api/users/login',
            headers: {'Content-Type': 'application/json'},
            data: {
                username: username,
                password: password
            }
        })
            .then(response => {
                // 로그인 성공 처리
                if (response.status === 200) {
                    const authToken = response.data;
                    axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
                    localStorage.setItem('jwtToken', authToken)
                    console.log(response.data);
                    alert('로그인 성공!');
                    // 페이지 새로고침
                    window.location.reload();
                }
            })
            .catch(error => {
                // 로그인 실패 처리
                console.error('로그인 실패:', error);
                alert('로그인 실패. 다시 시도해주세요.');
            });

        const token = localStorage.getItem('authToken'); // 저장된 토큰 가져오기
        if (token !== null) {
            axios({
                method: 'GET', // 또는 다른 HTTP 메서드
                url: '/api/users/protected',
                headers: {
                    'Authorization': `Bearer ${token}`, // Authorization 헤더 추가
                    'Content-Type': 'application/json'
                }
            })
                .then(response => {
                    console.log('성공: ' + response);
                })
                .catch(error => {
                    // 에러 처리
                    console.log('실패: ' + error);
                });
        } else {
            console.log('로그인 토큰이 없습니다.');
        }
    });

    // 모달 닫기 버튼 클릭 이벤트
    document.getElementById('btn-close').addEventListener('click', () => {
        // 모달을 닫고 로그인 폼을 초기화
        document.getElementById('loginForm').reset();
    });

    const loginOutBtn = document.querySelector('[data-action]');
    loginOutBtn.addEventListener('click', function () {
        const action = this.getAttribute('data-action');
        if (action === 'showLoginModal') {
            showLoginModal(); // 로그인 모달 띄우기
        } else if (action === 'logout') {
            logout(); // 로그아웃 처리
        }
    });

    // 로그인 모달을 띄우는 함수
    function showLoginModal() {
        $('#loginModal').modal('show'); // 로그인 모달 띄우기
    }

    // 로그아웃 처리
    function logout() {
        localStorage.removeItem('token'); // JWT 토큰 삭제
        window.location.reload(); // 페이지 새로고침
    }
});

// axios.interceptors.request.use(
//     (config) => {
//         const token = localStorage.getItem('authToken');
//         if (token) {
//             config.headers['Authorization'] = `Bearer ${token}`;  // 헤더에 JWT 추가
//         }
//         return config;
//     },
//     (error) => {
//         return Promise.reject(error);
//     }
// );