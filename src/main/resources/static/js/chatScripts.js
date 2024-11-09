let currentChatRoomId;
let isChatModalOpen = false;

function openChatModal(userId) {
    isChatModalOpen = true;
    if (!userId) {
        loadUserList();
        const chatModal = new bootstrap.Modal(document.getElementById('chatModal'));
        chatModal.show();

        return;
    }

    console.log('채팅하기 버튼 클릭' + "작성자: " + userId);
    opponentId = userId;

    // WebSocket 연결이 이미 설정된 상태인지 확인
    if (stompClient && stompClient.connected) {
        console.log("Already connected to WebSocket");

        // 특정 사용자와의 채팅 시작 로직
        axios({
            method: 'GET',
            url: `/api/chat/checkChatRoom/${userId}`,
        })
            .then(response => {
                if (response.data.exists) {
                    loadUserList();
                    loadChatMessages(response.data.chatRoomId);
                } else {
                    createNewChatRoom(userId);
                }
            })
            .catch(error => {
                console.error("채팅방 확인 중 오류 발생:", error);
            });
    } else {
        console.log("WebSocket not connected.");
        alert("로그인 후 다시 시도해 주세요.");
    }
}

function loadChatMessages(chatRoomId) {
    axios({
        method: 'GET',
        url: `/api/chat/getChatMessages/${chatRoomId}`,
    })
        .then(response => {
            const messages = response.data.chatMessages;
            const opponentUser = response.data.opponentUser
            currentChatRoomId = chatRoomId;
            displayMessagesInModal(messages, opponentUser); // 상대방 정보를 적절히 전달
        })
        .catch(error => {
            console.error("채팅 메시지 로드 중 오류 발생:", error);
        });
}

function createNewChatRoom(userId) {
    axios({
        method: 'GET',
        url: `/api/chat/createChatRoom/${userId}`,
    })
        .then(response => {
            loadUserList();
            loadChatMessages(response.data.chatRoomId); // 생성 후 메시지 로드
        })
        .catch(error => {
            console.error("새 채팅방 생성 중 오류 발생:", error);
        });
}

function displayMessagesInModal(messages, opponentUser) {
    // 채팅 메시지 컨테이너 초기화
    const chatMessagesContainer = document.getElementById('messageContainer');
    chatMessagesContainer.innerHTML = ''; // 이전 메시지 초기화

    // 각 메시지 처리
    messages.forEach(message => {
        const messageElement = document.createElement('div');
        messageElement.classList.add('message'); // 메시지 스타일을 위한 클래스 추가

        // 메시지 내용 설정
        messageElement.innerHTML = `${message.content}`;

        // 메시지 발송자에 따른 스타일 클래스 추가 (보낸 메시지 또는 받은 메시지)
        messageElement.classList.add(Number(message.senderId) === Number(sessionStorage.loginUserId) ? 'sent' : 'received');

        // 메시지 요소를 컨테이너에 추가
        chatMessagesContainer.appendChild(messageElement);

        loadUserList();
    });

    // 모달을 띄웁니다
    const chatModal = new bootstrap.Modal(document.getElementById('chatModal'));
    if (chatModalElement.classList.contains('show')) {
        console.log("모달이 이미 열려 있습니다.");
        return; // 이미 열려 있으므로 함수를 종료하여 중복 실행을 방지
    }

    chatModal.show();
}

function sendMessage() {
    const messageContent = document.getElementById('chatInput').value;

    if (messageContent === '') {
        alert('메세지를 입력해주세요');
        return;
    }

    const chatMessage = {
        chatRoomId: currentChatRoomId,
        content: messageContent,
    };

    console.log("chatMessage: " + chatMessage);

    stompClient.send("/app/sendMessage", {}, JSON.stringify(chatMessage));

    appendMessageToChat(chatMessage.content, 'sent');
    document.getElementById('chatInput').value = ''; // 입력창 초기화
}

function appendMessageToChat(messageContent, messageType) {
    const messageContainer = document.getElementById('messageContainer');

    // 메시지 요소 생성
    const messageElement = document.createElement('div');
    messageElement.classList.add('message', messageType);  // 'sent' 또는 'received' 클래스를 추가하여 스타일을 다르게 할 수 있음

    // 메시지 내용 추가
    messageElement.innerHTML = `${messageContent}`;

    // 메시지 컨테이너에 메시지 추가
    messageContainer.appendChild(messageElement);

    // 메시지가 추가되면 스크롤을 아래로 이동
    messageContainer.scrollTop = messageContainer.scrollHeight;

}

function loadUserList() {
    axios({
        method: 'GET',
        url: `/api/chat/loadUserList/${sessionStorage.getItem('loginUserId')}`,
    })
        .then(response => {
            const users = response.data;

            const userListContainer = document.getElementById("userList");
            userListContainer.innerHTML = ""; // 기존 항목 초기화

            users.forEach(user => {
                const userButton = document.createElement("button");
                userButton.className = "list-group-item list-group-item-action";
                userButton.textContent = user.nickname;
                axios({
                    method: 'GET',
                    url: `/api/chat/getChatRoomId/${user.id}`
                })
                    .then(response => {
                        userButton.onclick = () => loadChatMessages(response.data.chatRoomId);
                        if (response.data.unreadMessageCount > 0) {
                            userButton.innerHTML = `${user.nickname} <strong>(${response.data.unreadMessageCount})</strong>`;
                        }
                    })
                    .catch(error => {
                        console.error("해당 채팅방이 없음: ", error);
                    })
                userListContainer.appendChild(userButton);
            });
        })
        .catch(error => {
            console.error("채팅했던 유저 리스트 불러오던 중 오류 발생: ", error);
        });
}


// 모달이 닫힐 때 초기화 작업 수행
const chatModalElement = document.getElementById('chatModal');
chatModalElement.addEventListener('hidden.bs.modal', () => {
    console.log("모달이 닫히면서 초기화 중...");
    isChatModalOpen = false;

    // 채팅 입력 필드 초기화
    document.getElementById('chatInput').value = '';

    // 채팅 메시지 내용 초기화
    const messageContainer = document.getElementById('messageContainer');
    messageContainer.innerHTML = '';
    opponentId = 0;
});

function displayIncomingMessage(chatMessage) {
    if (!isChatModalOpen) {
        console.log('모달이 열려있지 않아 notification 실행')
        showNotification(`새 메시지: ${chatMessage.content}`);
        return;
    }

    if (Number(currentChatRoomId) !== Number(chatMessage.chatRoomId)) {
        loadUserList();
        return;
    }

    const messageContainer = document.getElementById('messageContainer');

    // 메시지 요소 생성
    const messageElement = document.createElement('div');
    messageElement.classList.add('message', 'received'); // 받은 메시지 스타일 추가

    // 메시지 내용 설정 (예시: 상대방 메시지)
    messageElement.innerHTML = `${chatMessage.content}`;

    // 메시지를 화면에 추가
    messageContainer.appendChild(messageElement);

    // 메시지가 추가되면 스크롤을 아래로 이동
    messageContainer.scrollTop = messageContainer.scrollHeight;

    axios({
        method: 'GET',
        url: `/api/chat/setChatMessageReadTrue/${chatMessage.id}`
    })
}

function showNotification(message) {
    // 알림 요소 생성
    const notification = document.createElement("div");
    notification.className = "notification";
    notification.textContent = message;

    // 알림을 컨테이너에 추가
    const notificationContainer = document.getElementById("notificationContainer");
    notificationContainer.appendChild(notification);

    // 4초 후에 알림을 자동으로 제거
    setTimeout(() => {
        notificationContainer.removeChild(notification);
    }, 3000);
}