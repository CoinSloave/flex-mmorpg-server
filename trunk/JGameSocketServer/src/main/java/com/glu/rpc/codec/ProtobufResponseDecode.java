/**
 * 
 */
package com.glu.rpc.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderAdapter;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.glu.rpc.service.RpcProtobuf;
import com.glu.rpc.service.RpcProtobuf.Response;
import com.glu.rpc.service.RpcProtobuf.Response.Builder;

/**
 * @author yubingxing
 * 
 */
public class ProtobufResponseDecode extends ProtocolDecoderAdapter {

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
		Builder builder = RpcProtobuf.Response.newBuilder().mergeFrom(
				in.asInputStream());
		Response response = builder.build();
		out.write(response);
	}

}
