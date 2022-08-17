package com.atguigu.atcrowdfunding.manager.service;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("eureka-activiti-service")
public interface ProcessService {
	
	@RequestMapping("/pageQueryProcDefData")
	public List<Map<String,Object>> pageQueryProcDefData(@RequestBody Map<String,Object> paramMap);

	@RequestMapping("/pageQueryProcDefCount")
	public int pageQueryProcDefCount();
	
	@RequestMapping("/delete/{id}")
	public void delete(@PathVariable("id")String id);

	@RequestMapping("/pageQueryTaskCount")
	public int pageQueryTaskCount();

	@RequestMapping("/pageQueryTaskData")
	public List<Map<String, Object>> pageQueryTaskData(@RequestBody Map<String, Object> paramMap);
}
