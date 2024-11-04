
let stompClient = null;

// 웹소켓 연결 설정
function connect(chatRoomId) {
    const socket = new SockJS('/ws');  // Spring WebSocket 엔드포인트
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log("Connected: " + frame);

        // 해당 채팅방의 메시지를 수신할 때
        stompClient.subscribe(`/topic/chat/${chatRoomId}`, function (message) {
            const chatMessage = JSON.parse(message.body);
            showMessage(chatMessage);
        });
    });
}

// 새로운 메시지를 화면에 표시하는 함수
function showMessage(chatMessage) {
    const chatBox = document.getElementById("chatBox");
    const messageElement = document.createElement("div");
    messageElement.innerText = `${chatMessage.sender}: ${chatMessage.content}`;
    chatBox.appendChild(messageElement);
}

// 메시지를 서버로 보내는 함수
function sendMessage() {
    const messageInput = document.getElementById("messageInput");
    const chatRoomId = document.getElementById("chatRoomId").value;
    const messageContent = messageInput.value;

    if (messageContent && stompClient) {
        const message = {
            chatRoomId: chatRoomId,
            sender: "loginUser", // 현재 로그인한 사용자로 설정
            content: messageContent
        };

        stompClient.send(`/app/chat/sendMessage`, {}, JSON.stringify(message));
        messageInput.value = '';
    }
}
