/**
 * Copyright 2009/9/2 com.glu Group.
 */
package com.glu.rpc.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.easymock.EasyMock;
import org.testng.annotations.Test;

import com.glu.rpc.service.RpcProtobuf.Response;
import com.google.protobuf.Message;

/**
 * @author yubingxing
 * 
 */
public class ProtobufResponseDecodeMock {
	@Test
	public void testDecoder() throws Exception {
		ProtocolDecoderOutput out = EasyMock
				.createMock(ProtocolDecoderOutput.class);
		IoSession session = EasyMock.createMock(IoSession.class);
		out.write(EasyMock.anyObject());
		EasyMock.replay(out, session);

//		com.glu.rpc.test.TestProto.Result result = com.glu.rpc.test.TestProto.Result.newBuilder().setResult("result").build();
//		Message message = Response.newBuilder().setCallback(true)
//				.setResponseProto(result.toByteString()).build();
		
//		com.glu.rpc.service.RegisterServiceProto.Result result = com.glu.rpc.service.RegisterServiceProto.Result.newBuilder().setSuccess(true).build();
//		Message message = Response.newBuilder().setCallback(true).setResponseProto(result.toByteString()).build();
		
		com.glu.rpc.service.LoginServiceProto.LoginResult result = com.glu.rpc.service.LoginServiceProto.LoginResult.newBuilder().setSuccess(true).build();
		Message message = Response.newBuilder().setCallback(true).setResponseProto(result.toByteString()).build();

		ProtobufResponseDecode decoder = new ProtobufResponseDecode();
		IoBuffer iobuf = IoBuffer.wrap(message.toByteArray());
		decoder.decode(session, iobuf, out);

		EasyMock.verify(out, session);
	}
}
