// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 2008-10-22 17:20:40
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3)
// Source File Name:   XConnection.java
package game;

import java.io.IOException;
import java.io.InterruptedIOException;

import javax.wireless.messaging.Message;
import javax.wireless.messaging.MessageConnection;
import javax.wireless.messaging.MessageListener;

public class XConnection implements MessageConnection {

	public static boolean beHardtodieCracked = false;

	public XConnection() {
		beHardtodieCracked = true;
	}

	public void close() throws IOException {
	}

	public Message newMessage(String s) {
		return null;
	}

	public Message newMessage(String s, String s1) {
		return null;
	}

	public int numberOfSegments(Message message) {
		return 0;
	}

	public Message receive() throws IOException, InterruptedIOException {
		return null;
	}

	public void send(Message message) throws IOException,
			InterruptedIOException {
	}

	public void setMessageListener(MessageListener messagelistener)
			throws IOException {
	}

	public String toString() {
		return "javax.wireless.messaging.MessageConnection";
	}
}
