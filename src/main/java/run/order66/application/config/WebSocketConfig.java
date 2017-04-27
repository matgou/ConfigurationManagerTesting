package run.order66.application.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import run.order66.application.service.BroadcastEventWebSocket;
import run.order66.application.service.util.SessionWebSocketHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

	@Autowired
	BroadcastEventWebSocket broadcaster;
	
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
            registry.addHandler(echoWebSocketHandler(), "/order66");
    }

    @Bean
    public WebSocketHandler echoWebSocketHandler() {
            SessionWebSocketHandler handler = new SessionWebSocketHandler();
            broadcaster.addObserver(handler);
            return handler;
    }
}