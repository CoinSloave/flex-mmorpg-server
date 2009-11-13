/**
 * Copyright 2009/9/2 com.glu Group.
 */
package com.glu;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.mina.core.service.IoHandler;
import org.easymock.EasyMock;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.glu.rpc.proto.RpcChannelHandler;
import com.glu.rpc.server.RpcServerHandler;
import com.glu.rpc.server.io.RpcIoHandler;
import com.glu.rpc.test.TestServiceHandler;
import com.glu.rpc.test.TestProto.Result;
import com.glu.rpc.test.TestProto.TestService;
import com.glu.rpc.test.TestProto.User;
import com.glu.rpc.test.TestProto.TestService.Stub;
import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;
import com.google.protobuf.Service;

/**
 * @author yubingxing
 * 
 */
public class PerformanceTest {
	private RpcServerHandler server;
	private String host;
	private int port;

	// @Test(invocationCount = 100000, threadPoolSize = 1000)
	@Test
	public void testRpcServer() throws IOException {
		final RpcController mock = EasyMock.createMock(RpcController.class);
		mock.reset();
		EasyMock.replay(mock);
		// 构建客户端连接
		RpcChannelHandler channel = new RpcChannelHandler(host, port);
		RpcController controller = channel.newRpcController();
		Stub service = TestService.newStub(channel);
		// 模拟请求
		String reqdata = "yubingxing";
		User request = User.newBuilder().setUserName(reqdata).build();

		// 模拟回调函数
		RpcCallback<Result> done = new RpcCallback<Result>() {
			@Override
			public void run(Result result) {
				Assert.assertEquals(result.getResult(),
						"get user : yubingxing");
				Assert.assertTrue(result.getSuccess());
				mock.reset();
			}
		};
		// 执行远程方法
		service.testMethod(controller, request, done);
		Assert.assertFalse(controller.failed());
		Assert.assertEquals(controller.errorText(), null);
		EasyMock.verify(mock);
	}

	@BeforeClass
	public void setup() throws IOException {
		host = "localhost";
		port = 12345;
		Map<String, Service> services = new HashMap<String, Service>();
		services.put(TestService.getDescriptor().getFullName(),
				new TestServiceHandler());
		IoHandler handler = new RpcIoHandler(services);

		// 启动服务器
		server = new RpcServerHandler(host, port, handler);
		server.start();
	}

	@AfterClass
	public void stopServer() {
		server.stop();
	}
}
