/**
 * 
 */
package com.glu.rpc.server;

import java.io.IOException;

/**
 * RPC server interface
 * 
 * @author yubingxing
 * @version $Revision$
 */
public interface IRPCServer {
	/**
	 * start rpc server
	 * 
	 * @throws IOException
	 */
	public abstract void start() throws IOException;

	/**
	 * stop rpc server
	 */
	public abstract void stop();
}
