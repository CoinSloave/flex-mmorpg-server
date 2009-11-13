/**
 * 
 */
package com.glu.rpc.service;

import java.util.List;

import com.glu.db.dao.UserDAO;
import com.glu.rpc.service.LoginServiceProto.LoginResult;
import com.glu.rpc.service.LoginServiceProto.LoginService;
import com.glu.rpc.service.LoginServiceProto.LoginResult.Builder;
import com.glu.rpc.service.UserProto.User;
import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;

/**
 * @author yubingxing
 *
 */
public class LoginServiceHandler extends LoginService {

	/* (non-Javadoc)
	 * @see com.glu.rpc.service.LoginServiceProto.LoginService#login(com.google.protobuf.RpcController, com.glu.rpc.proto.UserProto.User, com.google.protobuf.RpcCallback)
	 */
	@Override
	public void login(RpcController controller, User request,
			RpcCallback<LoginResult> done) {
		UserDAO user = new UserDAO();
		List<com.glu.db.entity.User> result = user.findByName(request.getUserName(), 0);
		Builder builder = LoginResult.newBuilder();
		if(result != null && result.size() > 0){
			if(result.get(0).getPassword().equals(request.getPassword())){
				builder.setSuccess(true);
				LoginResult result1 = builder.build();
				done.run(result1);
				return;
			}
		}
		builder.setSuccess(false);
		done.run(builder.build());
	}

}
