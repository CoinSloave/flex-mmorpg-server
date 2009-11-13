/**
 * 
 */
package com.glu.rpc.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glu.rpc.codec.ProtobufMessageEncode;
import com.glu.rpc.codec.ProtobufRequestDecode;
import com.glu.rpc.define.RpcSymbols;

/**
 * RPC Server
 * 
 * @author yubingxing
 * @version $Revision$
 */
public class RpcServerHandler implements IRPCServer {
	private final static Logger logger = LoggerFactory
			.getLogger(RpcServerHandler.class);
	private SocketAcceptor acceptor;
	private String host;
	private int port;
	private IoHandler ioHandler;

	public RpcServerHandler(@Inject @Symbol(RpcSymbols.HOST) String host,
			@Inject @Symbol(RpcSymbols.PORT) int port,
			@Inject IoHandler rpcIoHandler) {
		this.host = host;
		this.port = port;
		this.ioHandler = rpcIoHandler;
	}

	/*
	 * start rpc server
	 * 
	 * @see com.glu.server.IRPCServer#start()
	 */
	@Override
	public void start() throws IOException {
		logger.info("starting rpc server!");
		// ExecutorService executor = Executors.newCachedThreadPool();
		int processorCount = Runtime.getRuntime().availableProcessors();
		acceptor = new NioSocketAcceptor(processorCount);
		acceptor.setReuseAddress(true);
		acceptor.getSessionConfig().setReuseAddress(true);
		acceptor.getSessionConfig().setReceiveBufferSize(1024);
		acceptor.getSessionConfig().setSendBufferSize(1024);
		acceptor.getSessionConfig().setTcpNoDelay(true);
		acceptor.getSessionConfig().setSoLinger(-1);
		acceptor.setBacklog(10240);

		acceptor.setDefaultLocalAddress(new InetSocketAddress(port));
		DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();
		// chain.addLast("executor", new ExecutorFilter(executor));
		chain.addLast("codec", new ProtocolCodecFilter(
				ProtobufMessageEncode.class, ProtobufRequestDecode.class));
		acceptor.setHandler(ioHandler);
		acceptor.bind(new InetSocketAddress(host, port));
	}

	/*
	 * stop rpc server
	 * 
	 * @see com.glu.server.IRPCServer#stop()
	 */
	@Override
	public void stop() {
		logger.info("stopping rpc server!");
		acceptor.unbind();
	}

}
