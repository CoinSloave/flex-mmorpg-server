/**
 * Copyright 2009/9/2 com.glu Group.
 */
package com.glu.rpc.codec;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.easymock.EasyMock;
import org.testng.annotations.Test;

import com.glu.rpc.service.RpcProtobuf.Request;
import com.google.protobuf.Message;

/**
 * @author yubingxing
 * 
 */
public class ProtobufMessageEncodeMock {

	@Test
	public void testProtobufMessage() throws Exception {
		ProtocolEncoderOutput out = EasyMock
				.createMock(ProtocolEncoderOutput.class);
		IoSession session = EasyMock.createMock(IoSession.class);
		out.write(EasyMock.anyObject());
		EasyMock.replay(out, session);

//		com.glu.rpc.test.TestProto.User user = com.glu.rpc.test.TestProto.User.newBuilder().setUserName("yubingxing").build();
//		Message message = Request.newBuilder().setServiceName("TestService")
//				.setMethodName("testMethod").setRequestProto(
//						user.toByteString()).build();
		
		com.glu.rpc.service.UserProto.User user = com.glu.rpc.service.UserProto.User.newBuilder()
			.setUserName("yubingxing").setPassword("123456").build();
		Message message = Request.newBuilder().setServiceName("LoginService")
				.setMethodName("login").setRequestProto(user.toByteString()).build();
		
		ProtobufMessageEncode encode = new ProtobufMessageEncode();
		encode.encode(session, message, out);

		EasyMock.verify(out, session);
	}
}
