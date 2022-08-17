package com.atguigu.atcrowdfunding.service.act.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.atguigu.atcrowdfunding.common.bean.Member;
import com.atguigu.atcrowdfunding.service.act.service.MemberService;
import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler.Builder;

@RestController
public class ActController {
	
	@Autowired
	private RepositoryService repositoryService;
	
	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	private TaskService  taskService;
	
	@Autowired
	private MemberService memberService;
	
	@RequestMapping("/pageQueryTaskCount")
	public int pageQueryTaskCount() {
		TaskQuery query = taskService.createTaskQuery();
		
		int count = (int) query
				.processDefinitionKey("authflow")
				.taskCandidateGroup("manager")
				.count();
				
		return count;
	};
	
	@RequestMapping("/pageQueryTaskData")
	public List<Map<String,Object>> pageQueryTaskData(@RequestBody Map<String,Object> paramMap){
		
		TaskQuery query = taskService.createTaskQuery();
		
		int pageno = (Integer)paramMap.get("pageno");
		int pagesize = (Integer)paramMap.get("pagesize");
		
		List<Task> tasks = query
				.processDefinitionKey("authflow")
				.taskCandidateGroup("manager")
				.listPage((pageno-1)*pagesize, pagesize);
		 
		List<Map<String,Object>> taskMapList = new ArrayList<Map<String,Object>>();
		
		for(Task task : tasks) {
			Map<String,Object> taskMap = new HashMap<String,Object>();
			taskMap.put("id", task.getId());
			taskMap.put("name", task.getName());
			
			//task ==> pd
			String pdid =task.getProcessDefinitionId();
			
			ProcessDefinitionQuery pdQuery = repositoryService.createProcessDefinitionQuery();
			ProcessDefinition pd = pdQuery.processDefinitionId(pdid).singleResult();
			
			taskMap.put("pdversion", pd.getVersion());
			taskMap.put("pdname", pd.getName());
			
			//task ==> pi ==> member
			String piid = task.getProcessInstanceId();
			Member member = memberService.queryMemberByPiid(piid);
			
			taskMap.put("memberid", member.getId());
			taskMap.put("membername", member.getMembername());
			taskMapList.add(taskMap);
		}
		 
		return taskMapList;
	}
	
	@RequestMapping("/sendMail")
	public String sendMail(@RequestBody Member loginMember) {
		//发送邮件
		TaskQuery query = taskService.createTaskQuery();
		List<Task> tasks = query
			.processDefinitionKey("authflow")
			.taskAssignee(loginMember.getLoginacct()).list();
		
		//完成任务
		Map<String,Object> varMap = new HashMap<String,Object>();
		varMap.put("userEmail",loginMember.getEmail());
		
		StringBuilder builder = new StringBuilder();
		for(int i = 0; i < 4; i++) {
			builder.append(new Random().nextInt(10));
		}
		String authcode = builder.toString();
		varMap.put("authcode", authcode);
		
		for(Task t : tasks) {
			taskService.complete(t.getId(),varMap);
		}
		
		//更新验证码
		return authcode;
	};
	
	@RequestMapping("/nextProcess/{loginacct}")
	public void nextProcess(@PathVariable("loginacct") String loginacct) {
		//查询任务
		TaskQuery query = taskService.createTaskQuery();
		List<Task> tasks = query
			.processDefinitionKey("authflow")
			.taskAssignee(loginacct).list();
		//流程变量
		Map<String,Object> varMap = new HashMap<String,Object>();
		varMap.put("status", "next");
		//完成任务
		for(Task t : tasks) {
			taskService.complete(t.getId(),varMap);
		}
	};
	
	@RequestMapping("/endProcess/{taskid}")
	public void endProcess(@PathVariable("taskid") String taskid) {
		//流程变量
		Map<String,Object> varMap = new HashMap<String,Object>();
		varMap.put("status", "pass");
		//完成任务
		taskService.complete(taskid,varMap);
	};
	
	@RequestMapping("/process/{loginacct}")
	public void process(@PathVariable("loginacct") String loginacct) {
		//查询任务
		TaskQuery query = taskService.createTaskQuery();
		List<Task> tasks = query
			.processDefinitionKey("authflow")
			.taskAssignee(loginacct).list();
		//完成任务
		for(Task t : tasks) {
			taskService.complete(t.getId());
		}
	}
	
	@RequestMapping("/startProcessInstance/{loginacct}")
	public String startProcessInstance(@PathVariable("loginacct") String loginacct) {
		
		ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery();
		
		//查询实名认证的流程定义
		ProcessDefinition pd = query
			.processDefinitionKey("authflow")
			.latestVersion()
			.singleResult();
			
		String pdid = pd.getId();
		
		Map<String,Object> varMap = new HashMap<String,Object>();
		varMap.put("loginacct", loginacct);
		
		ProcessInstance pi = runtimeService.startProcessInstanceById(pdid,varMap);
		
		return pi.getId();
	}
	
	@RequestMapping("/delete/{id}")
	public void delete(@PathVariable("id")String id) {
		//查询流程定义对象
		ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery();
		ProcessDefinition pd = query.processDefinitionId(id).singleResult();
		
		String deployId = pd.getDeploymentId();
		//删除流程定义
		repositoryService.deleteDeployment(deployId, true);
	}
	
	@RequestMapping("/loadImgById/{id}")
	public byte[] loadImgById(@PathVariable("id")String id) {
		
		//查询流程定义对象
		ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery();
		ProcessDefinition pd = query.processDefinitionId(id).singleResult();
		
		String imgName = pd.getDiagramResourceName();
		String deployId = pd.getDeploymentId();
		
		//读取流程定义的图形
		InputStream in = repositoryService.getResourceAsStream(deployId, imgName);
		
		ByteArrayOutputStream swapStream = new ByteArrayOutputStream(); 
		byte[] buff = new byte[100]; //buff用于存放循环读取的临时数据 
		int rc = 0; 
		try {
			while ((rc = in.read(buff, 0, 100)) > 0) { 
			    swapStream.write(buff, 0, rc); 
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
		byte[] in_b = swapStream.toByteArray(); //in_b为转换之后的结果
		
		return in_b;
	}

	
	@RequestMapping("/deployProcDef")
	public String depolyProcDef( @RequestParam("pdfile") MultipartFile file ) {
		//System.out.println("file name:" + file.getOriginalFilename());
		//部署流程定义
		try {
			repositoryService.createDeployment()
			//.addClasspathResource(file.getOriginalFilename())
			.addInputStream(file.getOriginalFilename(), file.getInputStream())
			.deploy();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "success";
	}
	
	@RequestMapping("/pageQueryProcDefData")
	public List<Map<String,Object>> pageQueryProcDefData(@RequestBody Map<String,Object> paramMap){
		
		ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery();
		
		int pageno = (Integer)paramMap.get("pageno");
		int pagesize = (Integer)paramMap.get("pagesize");
		
		
		List<ProcessDefinition> pds = query.listPage((pageno-1)*pagesize, pagesize);
		 
		List<Map<String,Object>> pdMapList = new ArrayList<Map<String,Object>>();
		
		for(ProcessDefinition pd :pds) {
			Map<String,Object> pdMap = new HashMap<String,Object>();
			pdMap.put("id", pd.getId());
			pdMap.put("name", pd.getName());
			pdMap.put("key", pd.getKey());
			pdMap.put("version", pd.getVersion());
			pdMapList.add(pdMap);
		}
		 
		return pdMapList;
	}
	
	@RequestMapping("/pageQueryProcDefCount")
	public int pageQueryProcDefCount() {
		ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery();
		return (int)query.count();
	}
	
}
