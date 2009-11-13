/**
 * Copyright 2009/9/1 com.glu Group.
 */
package com.glu.rpc.server.io;

import java.io.IOException;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import com.glu.rpc.define.RpcException;
import com.glu.rpc.proto.RpcControllerHandler;
import com.glu.rpc.service.RpcProtobuf.ErrorReason;
import com.glu.rpc.service.RpcProtobuf.Request;
import com.glu.rpc.service.RpcProtobuf.Response;
import com.glu.rpc.service.RpcProtobuf.Response.Builder;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;
import com.google.protobuf.Service;
import com.google.protobuf.Descriptors.MethodDescriptor;

/**
 * Protocol Buffer Io Handler
 * 
 * @author yubingxing
 * @version $Revision$
 */
public class RpcIoHandler extends IoHandlerAdapter {
	// Google protobuf 地图
	private Map<String, Service> services;
	private final static Logger logger = Logger.getLogger(RpcIoHandler.class);

	public RpcIoHandler(Map<String, Service> services) {
		this.services = services;
	}

	/**
	 * 当接收消息时调用
	 */
	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		// Protobuf 请求
		Request rpcRequest = (Request) message;
		if (rpcRequest == null) {
			throw new RpcException(ErrorReason.BAD_REQUEST_DATA, "request data is null!");
		}
		// 取得protobuf请求的服务名
		Service service = services.get(rpcRequest.getServiceName());
		if (service == null) {
			throw new RpcException(ErrorReason.SERVICE_NOT_FOUND, "could not find service: "
					+ rpcRequest.getServiceName());
		}
		MethodDescriptor method = service.getDescriptorForType()
				.findMethodByName(rpcRequest.getMethodName());
		if (method == null) {
			throw new RpcException(ErrorReason.METHOD_NOT_FOUND, String.format(
					"Could not find method %s in service %s", rpcRequest.getMethodName(), 
					service.getDescriptorForType().getFullName()));
		}

		// 消息构建器
		Message.Builder builder = null;
		try {
			builder = service.getRequestPrototype(method).newBuilderForType()
					.mergeFrom(rpcRequest.getRequestProto());
			if (!builder.isInitialized()) {
				throw new RpcException(ErrorReason.BAD_REQUEST_PROTO,
						"Invalid request proto!");
			}
		} catch (InvalidProtocolBufferException e) {
			throw new RpcException(ErrorReason.BAD_REQUEST_PROTO, e);
		}
		// 构建请求
		Message request = builder.build();

		// 消息返回控制器
		RpcController controller = new RpcControllerHandler();
		// 消息回调函数
		Callback callback = new Callback();
		try {
			service.callMethod(method, controller, request, callback);
		} catch (RuntimeException e) {
			throw new RpcException(ErrorReason.RPC_ERROR, e);
		}
		// 创建并回复消息 （Callback是可选的）
		Builder responseBuilder = Response.newBuilder();
		if (callback.response != null) {
			responseBuilder.setCallback(true).setResponseProto(
					callback.response.toByteString());
		} else {
			// 设置 callback 是否被调用
			responseBuilder.setCallback(callback.invoked);
		}
		if (controller.failed()) {
			// 返回失败消息
			responseBuilder.setError(controller.errorText());
			responseBuilder.setErrorReason(ErrorReason.RPC_FAILED);
		}
		Response response = responseBuilder.build();
		this.outputResponse(session, response);
	}

	// 输出 Protobuf 回复消息
	void outputResponse(IoSession session, Response response)
			throws IOException {
		WriteFuture future = session.write(response);
		future.addListener(IoFutureListener.CLOSE);
	}

	/**
	 * Callback that just saves the response and the fact that it was invoked.
	 * 
	 * @author yubingxing
	 * 
	 */
	private class Callback implements RpcCallback<Message> {
		private Message response;
		private boolean invoked = false;

		public void run(Message response) {
			this.response = response;
			invoked = true;
		}
	}

	/**
	 * 捕获IO异常
	 */
	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		logger.warn("RPC Server Error", cause);
		// 创建回复消息构建器
		Builder builder = Response.newBuilder().setError(cause.getMessage());
		if (cause instanceof RpcException) {
			RpcException e = (RpcException) cause;
			if (e.getReason() != null) {
				builder.setErrorReason((ErrorReason) e.getReason());
			}
		}
		this.outputResponse(session, builder.build());
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
		session.close(true);
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		session.getConfig().setIdleTime(IdleStatus.BOTH_IDLE, 30);
	}

}
