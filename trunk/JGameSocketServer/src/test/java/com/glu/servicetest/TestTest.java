/**
 * 
 */
package com.glu.servicetest;

import java.io.IOException;

import junit.framework.Assert;

import org.apache.tapestry5.ioc.MappedConfiguration;
import org.testng.annotations.Test;

import com.glu.GameSocketServer;
import com.glu.rpc.define.RpcDefine;
import com.glu.rpc.proto.RpcChannelHandler;
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
public class TestTest {

	@Test
	public void testRpc() throws IOException {
		GameSocketServer server = new GameSocketServer(TestModule.class);
		try {
			server.start();
			// 模拟客户端连接
			RpcChannelHandler channel = new RpcChannelHandler(
					RpcDefine.DEFAULT_HOST, RpcDefine.DEFAULT_PORT);
			RpcController controller = channel.newRpcController();
			Stub service = TestService.newStub(channel);
			// 模拟请求
			String reqdata = "Request Data";
			User request = User.newBuilder().setUserName(reqdata).build();

			// 模拟回复
			RpcCallback<Result> done = new RpcCallback<Result>() {
				@Override
				public void run(Result result) {
					Assert.assertEquals(result.getResult(), "get user : Request Data");
					Assert.assertTrue(result.getSuccess());
				}
			};
			// 模拟回调远程函数
			service.testMethod(controller, request, done);
			Assert.assertFalse(controller.failed());
			Assert.assertEquals(controller.errorText(), null);
		} finally {
			server.stop();
		}
	}

	public static class TestModule {
		public static void contributeIoHandler(MappedConfiguration<String, Service> configuration) {
			configuration.add(TestService.getDescriptor().getFullName(), new TestServiceHandler());
		}
	}
}
