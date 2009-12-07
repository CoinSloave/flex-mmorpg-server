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
public class CallTest {
	private RpcServerHandler server;
	private String host;
	private int port;
	
	@BeforeClass
	void setUpServer() throws IOException {
		host = "localhost";
		port = 12345;
		Map<String, Service> services = new HashMap<String, Service>();
		services.put(TestService.getDescriptor().getFullName(), new TestServiceHandler());
		IoHandler handler = new RpcIoHandler(services);
		
		// start rpc server
		server = new RpcServerHandler(host, port, handler);
		server.start();
	}
	@AfterClass
	void stopServer() {
		server.stop();
	}
	
	/**
	 * 
	 * @throws IOException
	 */
	@Test
	public void testRpcServer() throws IOException {
		final RpcController mock = EasyMock.createMock(RpcController.class);
		mock.reset();
		EasyMock.replay(mock);
		
		try {
			// create client to call rpc
			RpcChannelHandler channel = new RpcChannelHandler(host, port);
			RpcController controller = channel.newRpcController();
			Stub service = TestService.newStub(channel);
			// request data
			String reqdata = "yubingxing";
			User request = User.newBuilder().setUserName(reqdata).build();
			
			// response callBack
			RpcCallback<Result> done = new RpcCallback<Result>() {
				@Override
				public void run(Result result) {
					Assert.assertEquals(result.getResult(), "get user : yubingxing");
					Assert.assertTrue(result.getSuccess());
					mock.reset();
				}
			};
			// excute remote method
			service.testMethod(controller, request, done);
			Assert.assertFalse(controller.failed());
			Assert.assertEquals(controller.errorText(), null);
			EasyMock.verify(mock);
		} finally {
		}
	}
	
	/**
	 * 
	 * @throws IOException
	 */
	@Test
	public void testFailRpcServer() throws IOException {
		try {
			// create client to call rpc
			RpcChannelHandler channel = new RpcChannelHandler(host, port);
			RpcController controller = channel.newRpcController();
			Stub service = TestService.newStub(channel);
			// request data
			String reqdata = "fail";
			User request = User.newBuilder().setUserName(reqdata).build();
			
			// response callback
			RpcCallback<Result> done = new RpcCallback<Result>() {
				@Override
				public void run(Result result) {
					Assert.fail("should not be here!");
				}
			};
			// execute remote method
			service.testMethod(controller, request, done);
			Assert.assertTrue(controller.failed());
			Assert.assertEquals(controller.errorText(), "RPC_ERROR : java.lang.RuntimeException: try to throw exception!");
		}finally {
		}
	}
}
