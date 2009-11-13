/**
 * Copyright 2009/8/31 com.glu Group.
 */
package com.glu.rpc.proto;

import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;

/**
 * implement RpcController
 * 
 * @author yubingxing
 * @version $Revision$
 */
public class RpcControllerHandler implements RpcController {

	private String reason;
	private boolean failed;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.protobuf.RpcController#errorText()
	 */
	@Override
	public String errorText() {
		return reason;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.protobuf.RpcController#failed()
	 */
	@Override
	public boolean failed() {
		return failed;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.protobuf.RpcController#isCanceled()
	 */
	@Override
	public boolean isCanceled() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.protobuf.RpcController#notifyOnCancel(com.google.protobuf.
	 * RpcCallback)
	 */
	@Override
	public void notifyOnCancel(RpcCallback<Object> callback) {
		// TODO Auto-generated method stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.protobuf.RpcController#reset()
	 */
	@Override
	public void reset() {
		// TODO Auto-generated method stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.protobuf.RpcController#setFailed(java.lang.String)
	 */
	@Override
	public void setFailed(String reason) {
		this.reason = reason;
		this.failed = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.protobuf.RpcController#startCancel()
	 */
	@Override
	public void startCancel() {
		// TODO Auto-generated method stub
	}

}
