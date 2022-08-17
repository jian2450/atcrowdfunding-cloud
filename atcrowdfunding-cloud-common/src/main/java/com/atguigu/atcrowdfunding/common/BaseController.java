package com.atguigu.atcrowdfunding.common;

import com.atguigu.atcrowdfunding.common.bean.AJAXResult;

public abstract class BaseController {

	private ThreadLocal<AJAXResult> resultLocal = new ThreadLocal<AJAXResult>();

	protected void start() {
		resultLocal.set(new AJAXResult());
	}

	protected Object end() {
		Object obj = resultLocal.get();
		resultLocal.remove();
		return obj;
	}
	
	protected void data(Object data) {
		AJAXResult result = resultLocal.get();
		result.setData(data);
	}
	
	protected void success(boolean flg) {
		AJAXResult result = resultLocal.get();
		result.setSuccess(flg);
	}

	protected void success() {
		success(true);
	}

	protected void fail() {
		success(false);
	}
}
