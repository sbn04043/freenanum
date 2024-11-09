document.addEventListener("DOMContentLoaded", () => {
    getLocationList();
    loginHandler();
    closeLoginModal();
    showLoginModal();
    logoutHandler();
    signUpHandler();
});

// 위치 검색을 실행하는 함수
const selectLocation = (locationName) => {
    console.log(locationName);
    document.getElementById('locationInput').value = locationName;
    document.getElementById('locationResult').style.display = 'none'; // 결과 숨김
};

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
};

//로그인 핸들러
const loginHandler = () => {
    document.getElementById('loginHandler').addEventListener('click', () => {
        const username = document.getElementById('username').value;
        const password = document.getElementById('password').value;

        if (username === '' || password === '') {
            alert('아이디와 비밀번호를 입력하세요.');
            return;
        }

        axios({
            method: 'POST',
            url: '/api/users/login',
            headers: {'Content-Type': 'application/json'},
            data: {username, password}
        })
            .then(response => {
                if (response.status === 200 && response.data) {
                    alert('로그인 성공!');
                    sessionStorage.setItem('loginUserId', response.data.id)
                    connectWebSocket();
                    window.location.reload();
                }
            })
            .catch(error => {
                console.error('로그인 실패:', error);
                alert('로그인 실패. 다시 시도해주세요.');
            });
    });
}

// 로그인 모달창 닫기
const closeLoginModal = () => {
    document.getElementById('btn-close').addEventListener('click', () => {
        document.getElementById('loginForm').reset();
    });
}

// 로그인 모달창 띄우기
const showLoginModal = () => {
    const loginBtn = document.getElementById('loginBtn');
    loginBtn.addEventListener('click', function () {
        $('#loginModal').modal('show'); // 로그인 모달 띄우기
    });
}

const logoutHandler = () => {
    const logoutBtn = document.getElementById('logoutBtn');
    logoutBtn.addEventListener('click', function () {
        alert('로그아웃 버튼 클릭');
        axios({
            method: 'GET',
            url: '/api/users/logout'
        })
            .then(response => {
                if (response.status === 200) {
                    alert('로그아웃 성공');
                    disconnectWebSocket();
                    sessionStorage.removeItem('loginUserId');
                    window.location.reload();
                }
            })
            .catch(error => {
                alert('로그아웃 실패: ' + error);
            });
    });
}

const signUpHandler = () => {
    const signupBtn = document.getElementById('signupBtn');
    signupBtn.addEventListener('click', function () {
        alert('회원가입 페이지로 이동');
        axios({
            method: 'GET',
            url: '/api/users/signup'
        })
            .then(response => {
                if (response.status === 200) {
                    alert('이동');
                }
            })
            .catch(error => {
                alert('이동 실패: ' + error);
            })
    })
}

let stompClient = null;

function connectWebSocket() {
    var socket = new SockJS('/ws/chat');
    let loginUserId = sessionStorage.getItem('loginUserId');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        sessionStorage.setItem('isWebSocketConnected', 'true'); // 연결 상태 저장

        stompClient.subscribe('/queue/chat/user/' + loginUserId, function (message) {
            const chatMessage = JSON.parse(message.body);
            displayIncomingMessage(chatMessage); // 받은 메시지를 화면에 표시하는 함수
        });
    }, function (error) {
        console.error('WebSocket 연결 실패:', error);
    });
}

// 페이지가 로드될 때 기존 연결이 존재하는지 확인 후 재연결
document.addEventListener("DOMContentLoaded", function () {
    // 현재 WebSocket 연결이 없거나 세션에 연결 상태가 없으면 재연결 시도
    if (sessionStorage.getItem('loginUserId')) {
        connectWebSocket();
    }
});

function disconnectWebSocket() {
    if (stompClient !== null) {
        stompClient.disconnect(() => {
            console.log("Disconnected from WebSocket");
        });
        stompClient = null;
    }
    sessionStorage.setItem('isWebSocketConnected', 'false'); // 연결 상태 저장
}


