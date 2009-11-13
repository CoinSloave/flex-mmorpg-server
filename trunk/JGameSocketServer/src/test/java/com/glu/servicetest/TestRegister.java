package com.glu.servicetest;

import java.io.IOException;

import org.apache.tapestry5.ioc.MappedConfiguration;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.glu.GameSocketServer;
import com.glu.rpc.define.RpcDefine;
import com.glu.rpc.proto.RpcChannelHandler;
import com.glu.rpc.service.RegisteServiceHandler;
import com.glu.rpc.service.RegisteServiceProto.FailureCause;
import com.glu.rpc.service.RegisteServiceProto.RegisteResult;
import com.glu.rpc.service.RegisteServiceProto.RegisteService;
import com.glu.rpc.service.RegisteServiceProto.RegisteService.Stub;
import com.glu.rpc.service.UserProto.User;
import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;
import com.google.protobuf.Service;

public class TestRegister {
	@Test
	public void testRegister() throws IOException {
		GameSocketServer server = new GameSocketServer(TestModule.class);
		try {
			server.start();
			// 构建客户但连接
			RpcChannelHandler channel = new RpcChannelHandler(RpcDefine.DEFAULT_HOST, RpcDefine.DEFAULT_PORT);
			RpcController controller = channel.newRpcController();
			Stub service = RegisteService.newStub(channel);
			// 模拟请求
			String username = "yubingxing";
			String password = "123456";
			String email = "yubingxing123@gmail.com";
			User request = User.newBuilder().setUserName(username).setPassword(password).setEmail(email).build();
			
			// 模拟回复
			RpcCallback<RegisteResult> done = new RpcCallback<RegisteResult>() {
				@Override
				public void run(RegisteResult result) {
					Assert.assertFalse(result.getSuccess());
					Assert.assertEquals(result.getFailureCause(), FailureCause.DUPLICATE_USERNAME);
				}
			};
			// 模拟回调函数
			service.callRegister(controller, request, done);
			Assert.assertFalse(controller.failed());
			Assert.assertEquals(controller.errorText(), null);
		} finally {
			server.stop();
		}
	}
	
	public static class TestModule {
		public static void contributeIoHandler(MappedConfiguration<String, Service> config) {
			config.add(RegisteService.getDescriptor().getFullName(), new RegisteServiceHandler());
		}
	}
}
