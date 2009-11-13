/**
 * Copyright 2009/9/2 com.glu Group. 
 */
package com.glu.rpc.test;

import com.glu.rpc.test.TestProto.Result;
import com.glu.rpc.test.TestProto.TestService;
import com.glu.rpc.test.TestProto.User;
import com.glu.rpc.test.TestProto.Result.Builder;
import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;

/**
 * TestService implemention
 * 
 * @author yubingxing
 * 
 */
public class TestServiceHandler extends TestService {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.glu.rpc.test.TestProto.TestService#testMethod(com.google.protobuf
	 * .RpcController, com.glu.rpc.test.TestProto.User,
	 * com.google.protobuf.RpcCallback)
	 */
	@Override
	public void testMethod(RpcController controller, User request,
			RpcCallback<Result> done) {
		if (request.getUserName().equals("fail")) {
			throw new RuntimeException("try to throw exception!");
		}
		Builder builder = Result.newBuilder().setResult("get user : " + request.getUserName());
		builder.setSuccess(true);
		Result result = builder.build();
		done.run(result);
	}

}
