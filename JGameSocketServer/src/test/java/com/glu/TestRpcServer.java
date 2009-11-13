/**
 * 
 */
package com.glu;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.mina.core.service.IoHandler;

import com.glu.rpc.server.IRPCServer;
import com.glu.rpc.server.RpcServerHandler;
import com.glu.rpc.server.io.RpcIoHandler;
import com.glu.rpc.test.TestServiceHandler;
import com.glu.rpc.test.TestProto.TestService;
import com.google.protobuf.Service;

/**
 * @author yubingxing
 * 
 */
public class TestRpcServer {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		String host = "localhost";
		int port = 12345;
		Map<String, Service> services = new HashMap<String, Service>();
		services.put(TestService.getDescriptor().getFullName(),
				new TestServiceHandler());
		IoHandler handler = new RpcIoHandler(services);

		// start rpc server
		IRPCServer server = new RpcServerHandler(host, port, handler);
		server.start();
	}

}
