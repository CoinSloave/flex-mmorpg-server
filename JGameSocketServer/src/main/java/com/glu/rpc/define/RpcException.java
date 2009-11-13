/**
 * Copyright 2009/3/31 com.glu Group.
 */
package com.glu.rpc.define;

import com.glu.rpc.service.RpcProtobuf.ErrorReason;

/**
 * Rpc call exception
 * 
 * @author yubingxing
 * @version $Revision$
 */
@SuppressWarnings("serial")
public class RpcException extends RuntimeException {
	private ErrorReason reason;

	/**
	 * Initialize RpcException
	 * 
	 * @param errorReason
	 * @param message
	 */
	public RpcException(ErrorReason errorReason, String message) {
		super(message);
		this.reason = errorReason;
	}

	public RpcException(ErrorReason errorReason, Exception e) {
		super(e);
		this.reason = errorReason;
	}

	/**
	 * @return the reason
	 */
	public ErrorReason getReason() {
		return reason;
	}
}
