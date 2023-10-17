package com.linkedin.sockets;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@ServerEndpoint("/socket/help")
public class HelpServerSocketEndpoint {
    private static List<Session> sessions = new ArrayList<>();
    Logger logger = Logger.getLogger(HelpServerSocketEndpoint.class.getName());

    /**
     * Invoked when the session is opened
     *
     * @param session current client's session
     */
    @OnOpen
    public void onOpen(Session session) {
        logger.info("Connect to the client");
        sessions.add(session);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        sessions.forEach(s -> {
            try {
                s.getBasicRemote().sendText(message);
                logger.info("Message has been sent.");
            } catch (IOException e) {
                logger.warning("Error with a session: " + e.getMessage());
            }
        });
    }

    @OnClose
    public void onClose() {
        logger.info("Connection was closed.");

    }

}

