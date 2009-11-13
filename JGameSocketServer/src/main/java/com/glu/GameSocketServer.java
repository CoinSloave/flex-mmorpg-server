/**
 * Copyright 2009/9/1 com.glu Group.
 */
package com.glu;

import java.io.IOException;
import java.util.Formatter;
import java.util.List;

import org.apache.tapestry5.ioc.IOCUtilities;
import org.apache.tapestry5.ioc.Registry;
import org.apache.tapestry5.ioc.RegistryBuilder;
import org.apache.tapestry5.ioc.services.ServiceActivity;
import org.apache.tapestry5.ioc.services.ServiceActivityScoreboard;
import org.apache.tapestry5.ioc.services.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glu.rpc.define.RpcCoreModule;
import com.glu.rpc.server.IRPCServer;

/**
 * Main entry
 * 
 * @author yubingxing
 * @version $Revision$
 */
public class GameSocketServer {

	private static final Logger logger = LoggerFactory
			.getLogger(GameSocketServer.class);
	// 服务器启动时间
	private long startTime;
	// 服务器结束时间
	private long endTime;
	// IOC 注册器
	private final RegistryBuilder builder = new RegistryBuilder();
	private Registry registry;
	// Rpc server
	private IRPCServer server;

	public GameSocketServer() {
		this(new Class<?>[0]);
	}

	public GameSocketServer(Class<?>... classes) {
		buildIOCRegistry(classes);
	}

	public void start() throws IOException {
		server.start();
		this.announceStartup();
	}

	public void stop() {
		server.stop();
	}
	
	/**
	 * Build tapestry IoC registry
	 * 
	 * @param classes
	 */
	private void buildIOCRegistry(Class<?>... classes) {
		startTime = System.currentTimeMillis();
		IOCUtilities.addDefaultModules(builder);
		builder.add(RpcCoreModule.class);
		builder.add(classes);

		registry = builder.build();
		registry.performRegistryStartup();

		server = registry.getService(IRPCServer.class);
		endTime = System.currentTimeMillis();
	}

	/**
	 * Announce Startup status
	 */
	private void announceStartup() {
		long toFinish = System.currentTimeMillis();

		StringBuilder buffer = new StringBuilder("Startup status:\n\n");
		Formatter f = new Formatter(buffer);

		String appName = "Java NIO Socket Game Server";
		f.format("服务器： '%s' (Tapestry IOC version %s). \n\n"
				+ "服务器启动：%,d ms 注册 IOC 服务, %,d ms 启动 rpc server.\n\n"
				+ "服务启动状态：\n", appName, "5.1.0.14", endTime - startTime,
				toFinish - startTime);

		int unrealized = 0;
		// 取得 Tapestry IoC 中的注册服务
		ServiceActivityScoreboard scoreboard = registry
				.getService(ServiceActivityScoreboard.class);
		// 取得IOC 服务状态
		List<ServiceActivity> serviceActivityList = scoreboard
				.getServiceActivity();
		int longest = 0;

		// 统计没有实现的服务数目
		for (ServiceActivity activity : serviceActivityList) {
			Status status = activity.getStatus();
			longest = Math.max(longest, activity.getServiceId().length());
			if (status == Status.DEFINED || status == Status.VIRTUAL)
				unrealized++;
		}

		String formatStr = "%" + longest + "s: %s\n";

		// 输出所有服务的信息
		for (ServiceActivity activity : serviceActivityList) {
			f.format(formatStr, activity.getServiceId(), activity.getStatus()
					.name());
		}

		f.format("\n%4.2f%% unrealized services (%d/%d)\n", 100. * unrealized
				/ serviceActivityList.size(), unrealized, serviceActivityList
				.size());

		logger.info(buffer.toString());
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		GameSocketServer server = new GameSocketServer();
		server.start();
	}

}
