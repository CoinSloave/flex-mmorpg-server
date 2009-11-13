package com.glu.rpc.service;

import java.util.List;

import com.glu.db.bean.UserBean;
import com.glu.db.dao.UserDAO;
import com.glu.rpc.service.RegisteServiceProto.FailureCause;
import com.glu.rpc.service.RegisteServiceProto.RegisteResult;
import com.glu.rpc.service.RegisteServiceProto.RegisteService;
import com.glu.rpc.service.RegisteServiceProto.RegisteResult.Builder;
import com.glu.rpc.service.RegisteServiceProto.RegisteService.Interface;
import com.glu.rpc.service.UserProto.User;
import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;

public class RegisteServiceHandler extends RegisteService implements
		Interface {

	@Override
	public void checkDuplicateUserName(RpcController controller, User request,
			RpcCallback<RegisteResult> done) {
		RegisteResult result = _checkDuplicateUserName(request).build();
		done.run(result);
	}

	@Override
	public void callRegister(RpcController controller, User request,
			RpcCallback<RegisteResult> done) {
		Builder builder = _checkDuplicateUserName(request);
		if(builder.getSuccess()){
			UserBean user = new UserBean();
			try{
				user.setUserInfo(request.getUserName(), request.getPassword(), request.getEmail());
			}catch(Exception ex){
				builder.setFailureCause(FailureCause.REGISTER_RPC_FAILED);
				builder.setSuccess(false);
			}
		}
		done.run(builder.build());
	}

	private Builder _checkDuplicateUserName(User request) {
		UserDAO user = new UserDAO();
		List<com.glu.db.entity.User> result = user.findByName(request.getUserName(), 0);
		Builder builder = RegisteResult.newBuilder();
		if(result != null && result.size() > 0){
			builder.setSuccess(false);
			builder.setFailureCause(FailureCause.DUPLICATE_USERNAME);
		}else{
			builder.setSuccess(true);
		}
		return builder;
	}
}
