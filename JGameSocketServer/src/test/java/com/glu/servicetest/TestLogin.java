/**
 * Copyright 2009/9/10 com.glu Group.
 */
package com.glu.servicetest;

import java.io.IOException;

import org.apache.tapestry5.ioc.MappedConfiguration;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.glu.GameSocketServer;
import com.glu.rpc.define.RpcDefine;
import com.glu.rpc.proto.RpcChannelHandler;
import com.glu.rpc.service.LoginServiceHandler;
import com.glu.rpc.service.LoginServiceProto.LoginResult;
import com.glu.rpc.service.LoginServiceProto.LoginService;
import com.glu.rpc.service.LoginServiceProto.LoginService.Stub;
import com.glu.rpc.service.UserProto.User;
import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;
import com.google.protobuf.Service;

/**
 * @author yubingxing
 *
 */
public class TestLogin {
	@Test
	public void testLogin() throws IOException {
		GameSocketServer server = new GameSocketServer(TestModule.class);
		try {
			server.start();
			// 构建客户端连接
			RpcChannelHandler channel = new RpcChannelHandler(RpcDefine.DEFAULT_HOST, RpcDefine.DEFAULT_PORT);
			RpcController controller = channel.newRpcController();
			Stub service = LoginService.newStub(channel);
			// 模拟请求
			String username = "yubingxing";
			String password = "123456";
			User request = User.newBuilder().setUserName(username).setPassword(password).build();
			
			// 模拟回复
			RpcCallback<LoginResult> done = new RpcCallback<LoginResult>() {
				@Override
				public void run(LoginResult result) {
					Assert.assertTrue(result.getSuccess());
				}
			};
			
			// 模拟回调函数
			service.login(controller, request, done);
			Assert.assertFalse(controller.failed());
			Assert.assertEquals(controller.errorText(), null);
		} finally {
			server.stop();
		}
	}
	
	public static class TestModule {
		public static void contributeIoHandler(MappedConfiguration<String, Service> configuration) {
			configuration.add(LoginService.getDescriptor().getFullName(), new LoginServiceHandler());
		}
	}
}
