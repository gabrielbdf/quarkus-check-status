package org.gabriel;

import java.util.Set;

import io.vertx.core.impl.ConcurrentHashSet;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint("/status/check")
@ApplicationScoped
public class StatusSocket {

    Set<Session> sessions = new ConcurrentHashSet<>();
   
   
    @OnOpen
    public void onOpen(Session session) {
        broadcast("Session " + session.getId() + " opened");
        sessions.add(session);
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
        broadcast("Session " + session.getId() + " closed");
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        sessions.remove(session);
        broadcast("Session " + session.getId() + " left on error: " + throwable);
    }

    @OnMessage
    public void onMessage(String message) {
        broadcast(">> " + message);
    }

    public void broadcast(String message) {
        sessions.forEach(s -> {
            s.getAsyncRemote().sendObject(message, result ->  {
                if (result.getException() != null) {
                    System.out.println("Unable to send message: " + result.getException());
                }
            });
        });
    }
    
}
