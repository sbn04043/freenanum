//
// let stompClient = null;
//
// // 웹소켓 연결 설정
// function connect(chatRoomId) {
//     const socket = new SockJS('/ws');  // Spring WebSocket 엔드포인트
//     stompClient = Stomp.over(socket);
//     stompClient.connect({}, function (frame) {
//         console.log("Connected: " + frame);
//
//         // 해당 채팅방의 메시지를 수신할 때
//         stompClient.subscribe(`/topic/chat/${chatRoomId}`, function (message) {
//             const chatMessage = JSON.parse(message.body);
//             showMessage(chatMessage);
//         });
//     });
// }
//
// // 새로운 메시지를 화면에 표시하는 함수
// function showMessage(chatMessage) {
//     const chatBox = document.getElementById("chatBox");
//     const messageElement = document.createElement("div");
//     messageElement.innerText = `${chatMessage.sender}: ${chatMessage.content}`;
//     chatBox.appendChild(messageElement);
// }
//
// // 메시지를 서버로 보내는 함수
// function sendMessage() {
//     const messageInput = document.getElementById("messageInput");
//     const chatRoomId = document.getElementById("chatRoomId").value;
//     const messageContent = messageInput.value;
//
//     if (messageContent && stompClient) {
//         const message = {
//             chatRoomId: chatRoomId,
//             sender: "loginUser", // 현재 로그인한 사용자로 설정
//             content: messageContent
//         };
//
//         stompClient.send(`/app/chat/sendMessage`, {}, JSON.stringify(message));
//         messageInput.value = '';
//     }
// }
function openChatModal(userId) {
    console.log('채팅하기 버튼 클릭' + "작성자: " + userId);

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
                    loadChatMessages(response.data.chatRoomId);
                    chatRoomId = response.data.chatRoomId;
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
            loadChatMessages(response.data.chatRoomId); // 생성 후 메시지 로드
            chatRoomId = response.data.chatRoomId;
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
        messageElement.innerHTML = `<strong>${message.receiverId === opponentUser.id ? opponentUser.nickname : '나'}:</strong> ${message.content}`;

        // 메시지 발송자에 따른 스타일 클래스 추가 (보낸 메시지 또는 받은 메시지)
        messageElement.classList.add(message.receiverId === opponentUser.id ? 'received' : 'sent');

        // 메시지 요소를 컨테이너에 추가
        chatMessagesContainer.appendChild(messageElement);
    });


    // 모달을 띄웁니다
    const chatModal = new bootstrap.Modal(document.getElementById('chatModal'));
    chatModal.show();
}

function sendMessage() {
    const messageContent = document.getElementById('chatInput').value;

    if (messageContent === '') {
        alert('메세지를 입력해주세요');
        return;
    }

    const chatMessage = {
        chatRoomId: chatRoomId,
        content: messageContent,
        receiverId: sellerId,
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
    messageElement.innerText = messageContent;

    // 메시지 컨테이너에 메시지 추가
    messageContainer.appendChild(messageElement);

    // 메시지가 추가되면 스크롤을 아래로 이동
    messageContainer.scrollTop = messageContainer.scrollHeight;
}

function displayMessage(message) {
    const messageContainer = document.getElementById('messageContainer');
    const messageElement = document.createElement('div');
    messageElement.innerHTML = `<strong>${message.senderId}:</strong> ${message.content}`;
    messageContainer.appendChild(messageElement);
}