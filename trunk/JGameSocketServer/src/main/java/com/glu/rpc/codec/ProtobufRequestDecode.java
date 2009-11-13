/**
 * Copyright 2009/9/1 com.glu Group.
 */
package com.glu.rpc.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderAdapter;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.glu.rpc.service.RpcProtobuf;
import com.glu.rpc.service.RpcProtobuf.Request;
import com.glu.rpc.service.RpcProtobuf.Request.Builder;

/**
 * @author yubingxing
 * 
 */
public class ProtobufRequestDecode extends ProtocolDecoderAdapter {

	/**
	 * decode MINA socket code to Google Protobuf code
	 * 
	 * @see org.apache.mina.filter.codec.ProtocolDecoder#decode(org.apache.mina.core.session.IoSession,
	 *      org.apache.mina.core.buffer.IoBuffer,
	 *      org.apache.mina.filter.codec.ProtocolDecoderOutput)
	 */
	@Override
	public void decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out)
			throws Exception {
		Builder builder = RpcProtobuf.Request.newBuilder().mergeFrom(
				in.asInputStream());
		Request request = builder.build();
		out.write(request);
	}
}
