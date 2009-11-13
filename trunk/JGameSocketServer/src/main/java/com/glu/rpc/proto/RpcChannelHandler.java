/**
 * Copyright 2009/8/31 com.glu Group.
 */
package com.glu.rpc.proto;

import java.net.InetSocketAddress;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glu.rpc.codec.ProtobufMessageEncode;
import com.glu.rpc.codec.ProtobufResponseDecode;
import com.glu.rpc.define.RpcException;
import com.glu.rpc.service.RpcProtobuf.ErrorReason;
import com.glu.rpc.service.RpcProtobuf.Request;
import com.glu.rpc.service.RpcProtobuf.Response;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcChannel;
import com.google.protobuf.RpcController;
import com.google.protobuf.Descriptors.MethodDescriptor;

/**
 * implements RpcChannel
 * 
 * @author yubingxing
 * @version $Revision$
 */
public class RpcChannelHandler implements RpcChannel {

	private final static Logger logger = LoggerFactory
			.getLogger(RpcChannelHandler.class);

	private final String host;
	private final int port;

	/**
	 * 构建一个TCP/IP通道.
	 * 
	 * @param host
	 * @param port
	 */
	public RpcChannelHandler(String host, int port) {
		this.host = host;
		this.port = port;
	}

	/**
	 * 构建一个RPC 控制器控制请求数据.
	 * 
	 * @return {@link RpcChannelHandler}
	 */
	public RpcControllerHandler newRpcController() {
		return new RpcControllerHandler();
	}

	/*
	 * 调用指定服务中的函数。
	 * This method is similar to Service.callMethod() with one important difference:
	 * the caller decides the types of the Message objects, not the callee. The 
	 * request may be of any type as long as request.getDescriptor() 
	 * == method.getInputType(). The response passed to the callback will be of 
	 * the same type as responsePrototype (which must have getDescriptor() ==
	 * method.getOutputType()).
	 */
	public void callMethod(final MethodDescriptor method,
			final RpcController controller, final Message request,
			final Message responsePrototype, final RpcCallback<Message> callback) {
		// 检查初始化
		if (!request.isInitialized()) {
			throw new RpcException(ErrorReason.BAD_REQUEST_DATA, "Request uninitialized!");
		}

		// 使用 MINA IoConnector
		IoConnector connector = new NioSocketConnector();

		// 添加 protobuf 解析器
		connector.getFilterChain().addLast(
				"codec",
				new ProtocolCodecFilter(ProtobufMessageEncode.class,
						ProtobufResponseDecode.class));
		// 设置连接器的IoHandlerAdapter
		connector.setHandler(new IoHandlerAdapter() {
			@Override
			public void messageReceived(IoSession session, Object message)
					throws Exception {
				if (logger.isDebugEnabled()) {
					logger.debug("received from server");
				}
				Response response = (Response) message;
				handlerResponse(responsePrototype, response, controller,
						callback);
				// 处理完请求，立即关闭
				session.close(true);
			}

			@Override
			public void sessionOpened(IoSession session) throws Exception {
				if (logger.isDebugEnabled()) {
					logger.debug("open session");
				}
				// 创建请求
				Request rpcRequest = Request.newBuilder().setRequestProto(
						request.toByteString()).setServiceName(
						method.getService().getFullName()).setMethodName(
						method.getName()).build();
				// 向session中写入请求
				session.write(rpcRequest);
			}
		});
		// 连接远程服务器
		ConnectFuture cf = connector.connect(new InetSocketAddress(host, port));

		try {
			// 等待连接
			cf.awaitUninterruptibly();
			cf.getSession().getCloseFuture().awaitUninterruptibly();
		} finally {
			// 施放 session
			connector.dispose();
		}
	}

	/**
	 * 
	 * @param responsePrototype
	 * @param response
	 * @param controller
	 * @param callback
	 */
	private void handlerResponse(Message responsePrototype, Response response,
			RpcController controller, RpcCallback<Message> callback) {
		if (logger.isDebugEnabled()) {
			logger.debug("handler response");
		}
		// 检查错误异常
		if (response.hasError()) {
			ErrorReason reason = response.getErrorReason();
			controller.setFailed(reason.name() + " :: " + response.getError());
			return;
		}
		if ((callback == null) || !response.getCallback()) {
			// 没有回调方法
			return;
		}
		if (!response.hasResponseProto()) {
			// 回调空方法
			callback.run(null);
			return;
		}
		try {
			Message.Builder builder = responsePrototype.newBuilderForType()
					.mergeFrom(response.getResponseProto());
			Message response1 = builder.build();
			callback.run(response1);
		} catch (InvalidProtocolBufferException e) {
			logger.warn(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}
}
