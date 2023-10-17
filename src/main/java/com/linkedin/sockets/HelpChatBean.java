package com.linkedin.sockets;

import javax.faces.push.Push;
import javax.faces.push.PushContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Simple bean to handle websocket communication with a client
 */
@ViewScoped
@Named
public class HelpChatBean implements Serializable {
    // declares a PushContext that represents a websocket
    @Inject
    @Push(channel = "help")
    private PushContext helpChannel;
    private String name;
    private String messageText;

    public HelpChatBean() {
    }

    public void sendMessage() {
        this.helpChannel.send(String.format("%s: %s", name, messageText));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }
}
