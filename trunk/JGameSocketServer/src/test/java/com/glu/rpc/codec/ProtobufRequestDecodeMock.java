/**
 * Copyright 2009/9/2 com.glu Group.
 */
package com.glu.rpc.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.easymock.EasyMock;
import org.testng.annotations.Test;

import com.glu.rpc.service.RpcProtobuf.Request;
import com.google.protobuf.Message;

/**
 * @author yubingxing
 * 
 */
public class ProtobufRequestDecodeMock {
	@Test
	public void testDecoder() throws Exception {
		ProtocolDecoderOutput out = EasyMock
				.createMock(ProtocolDecoderOutput.class);
		IoSession session = EasyMock.createMock(IoSession.class);
		out.write(EasyMock.anyObject());
		EasyMock.replay(out, session);

//		com.glu.rpc.test.TestProto.User user = com.glu.rpc.test.TestProto.User.newBuilder().setUserName("yubingxing").build();
//		Message message = Request.newBuilder().setServiceName("TestService")
//				.setMethodName("testMethod").setRequestProto(
//						user.toByteString()).build();
		
		com.glu.rpc.service.UserProto.User user = com.glu.rpc.service.UserProto.User.newBuilder().setUserName("yubingxing").setPassword("123456").build();
		Message message = Request.newBuilder().setServiceName("LoginService")
				.setMethodName("login").setRequestProto(user.toByteString()).build();
		
		ProtobufRequestDecode decoder = new ProtobufRequestDecode();
		IoBuffer iobuf = IoBuffer.wrap(message.toByteArray());
		decoder.decode(session, iobuf, out);

		EasyMock.verify(out, session);
	}
}
