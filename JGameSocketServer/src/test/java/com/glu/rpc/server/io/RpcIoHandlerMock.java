/**
 * Copyrigth 2009/9/2 com.glu Group. 
 */
package com.glu.rpc.server.io;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IoSession;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.glu.rpc.define.RpcException;
import com.glu.rpc.service.LoginServiceHandler;
import com.glu.rpc.service.RegisteServiceHandler;
import com.glu.rpc.service.RpcProtobuf.ErrorReason;
import com.glu.rpc.service.RpcProtobuf.Request;
import com.glu.rpc.service.RpcProtobuf.Response;
import com.glu.rpc.test.TestServiceHandler;
import com.google.protobuf.Service;

/**
 * @author yubingxing
 * 
 */
public class RpcIoHandlerMock {
	private IoHandler createRpcHandler() {
		Map<String, Service> services = new HashMap<String, Service>();
		services.put("TestService", new TestServiceHandler());
		services.put("LoginService", new LoginServiceHandler());
		services.put("RegisterService", new RegisteServiceHandler());
		return new RpcIoHandler(services);
	}

	@Test
	public void testNullReceived() {
		IoHandler handler = createRpcHandler();
		Request message = null;
		IoSession session = null;
		try {
			handler.messageReceived(session, message);
			assertFail();
		} catch (RpcException e) {
			assertFail(e.getReason(), ErrorReason.BAD_REQUEST_DATA);
		} catch (Exception e) {
			assertFail(e);
		}
	}

	@Test
	public void testMethodNotFoundReceived() {
		IoHandler handler = createRpcHandler();
		com.glu.rpc.test.TestProto.User user = com.glu.rpc.test.TestProto.User.newBuilder().setUserName("yubingxing").build();
		Request request = Request.newBuilder().setServiceName("TestService")
				.setMethodName("testMethod").setRequestProto(
						user.toByteString()).build();
		IoSession session = null;
		try {
			handler.messageReceived(session, request);
			assertFail();
		} catch (RpcException e) {
			assertFail(e.getReason(), ErrorReason.METHOD_NOT_FOUND);
		} catch (Exception e) {
			assertFail(e);
		}
	}

	@Test
	public void testReceived() {
		Map<String, Service> services = new HashMap<String, Service>();
		services.put("TestService", new TestServiceHandler());
		services.put("LoginService", new LoginServiceHandler());
		services.put("RegisterService", new RegisteServiceHandler());
		IoHandler handler = new RpcIoHandler(services) {
			void outputResponse(IoSession session, Response response)
					throws IOException {
				Assert.assertTrue(response.getCallback());
				Assert.assertTrue(response.getError().length() == 0);
				Assert.assertNotNull(response.getResponseProto());
			}
		};
//		com.glu.rpc.test.TestProto.User user = com.glu.rpc.test.TestProto.User.newBuilder().setUserName("yubingxing").build();
//		Request request = Request.newBuilder().setServiceName("TestService")
//				.setMethodName("testMethod").setRequestProto(
//						user.toByteString()).build();
		com.glu.rpc.service.UserProto.User user = com.glu.rpc.service.UserProto.User.newBuilder().setUserName("yubingxing").setPassword("123456").build();
		Request request = Request.newBuilder().setServiceName("LoginService").setMethodName("login").setRequestProto(user.toByteString()).build();
		try {
			IoSession session = null;
			handler.messageReceived(session, request);
		} catch (RpcException e) {
			assertFail(e);
		} catch (Exception e) {
			assertFail(e);
		}
	}

	private void assertFail() {
		Assert.fail("could not reach here!");
	}

	private void assertFail(Throwable e) {
		Assert.fail("could not reach here!", e);
	}

	private void assertFail(Object m, Object e) {
		Assert.assertEquals(m, e);
	}
}
