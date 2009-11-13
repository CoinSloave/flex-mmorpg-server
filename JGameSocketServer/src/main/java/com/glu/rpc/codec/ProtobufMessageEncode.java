/**
 * Copyright 2009/9/1 com.glu Group.
 */
package com.glu.rpc.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.google.protobuf.Message;

/**
 * Provide some protobuf encoder
 * 
 * @author yubingxing
 * @version $Revision$
 */
public class ProtobufMessageEncode extends ProtocolEncoderAdapter {

	/*
	 * Encode Google Protobuf message to mina encode stream
	 * 
	 * @see
	 * org.apache.mina.filter.codec.ProtocolEncoder#encode(org.apache.mina.core
	 * .session.IoSession, java.lang.Object,
	 * org.apache.mina.filter.codec.ProtocolEncoderOutput)
	 */
	@Override
	public void encode(IoSession session, Object message,
			ProtocolEncoderOutput out) throws Exception {
		Message obj = (Message) message;
		IoBuffer buf = IoBuffer.wrap(obj.toByteArray());
		out.write(buf);
	}
}
