package run.order66.application.service.util;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import run.order66.application.service.BroadcastEventWebSocket;

public class SessionWebSocketHandler implements WebSocketHandler,Observer {

    private final Logger log = LoggerFactory.getLogger(SessionWebSocketHandler.class);
    
    WebSocketSession session;
    
	@Override
	public void afterConnectionEstablished(WebSocketSession session)
			throws Exception {
		log.info("New websocket session from : " + session.getLocalAddress().toString());
		this.session = session;
	}

	@Override
	public void handleMessage(WebSocketSession session,
			WebSocketMessage<?> message) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleTransportError(WebSocketSession session,
			Throwable exception) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterConnectionClosed(WebSocketSession session,
			CloseStatus closeStatus) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean supportsPartialMessages() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void update(Observable o, Object arg) {
		if(this.session != null) {
			TextMessage msg = new TextMessage("update rule");
			try {
				this.session.sendMessage(msg);
			} catch (IOException e) {
				log.error("Exception while sending message to client",e);
				e.printStackTrace();
			}
		}
	}

}
