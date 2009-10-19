package game;

// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 2008-10-22 17:20:42
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3)
// Source File Name:   XMessage.java

import java.util.Date;

import javax.wireless.messaging.BinaryMessage;
import javax.wireless.messaging.TextMessage;

public class XMessage implements TextMessage, BinaryMessage {

	public XMessage() {
	}

	public String getPayloadText() {
		return payloadText;
	}

	public void setPayloadText(String arg0) {
		payloadText = arg0;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String arg0) {
		address = arg0;
	}

	public Date getTimestamp() {
		return new Date();
	}

	public byte[] getPayloadData() {
		return payloadData;
	}

	public void setPayloadData(byte arg0[]) {
		payloadData = arg0;
	}

	public String toString() {
		return "javax.wireless.messaging.TextMessage";
	}

	String payloadText;
	String address;
	byte payloadData[];
}
