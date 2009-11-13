/**
 * Copyright 2009/9/1 com.glu Group.
 */
package com.glu.rpc.define;

import org.apache.mina.core.service.IoHandler;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;

import com.glu.rpc.server.IRPCServer;
import com.glu.rpc.server.RpcServerHandler;
import com.glu.rpc.server.io.RpcIoHandler;

/**
 * RPC Core Module
 * 
 * @author yubingxing
 * @version $Revision$
 */
public class RpcCoreModule {
	public static void bind(ServiceBinder binder) {
		binder.bind(IRPCServer.class, RpcServerHandler.class);
		binder.bind(IoHandler.class, RpcIoHandler.class);
	}

	/**
	 * Configure default host and port
	 * 
	 * @param configuration
	 */
	public static void contributeFactoryDefaults(
			MappedConfiguration<String, String> configuration) {
		configuration.add(RpcSymbols.HOST, RpcDefine.DEFAULT_HOST);
		configuration.add(RpcSymbols.PORT, String.valueOf(RpcDefine.DEFAULT_PORT));
	}
}
