package com.atguigu.atcrowdfunding.manager.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.atguigu.atcrowdfunding.common.BaseController;
import com.atguigu.atcrowdfunding.common.bean.MemberCert;
import com.atguigu.atcrowdfunding.common.bean.Page;
import com.atguigu.atcrowdfunding.manager.service.MemberService;
import com.atguigu.atcrowdfunding.manager.service.ProcessService;

@Controller
@RequestMapping("/auth")
public class AuthController extends BaseController {
	
	@Autowired
	private ProcessService processService;
	
	@Autowired
	private MemberService memberService;
	
	@ResponseBody
	@RequestMapping("/ok")
	public Object ok(String taskid,Integer memberid) {
		start();
		
		try {
			//更新会员实名认证状态
			Map<String,Object> varMap = new HashMap<String,Object>();
			varMap.put("taskid", taskid);
			varMap.put("memberid", memberid);
			memberService.finishAuth(varMap);
			
			success();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		
		return end();
	}
	
	@RequestMapping("/detail")
	public String detail(String taskid ,Integer memberid,Model model) {
		
		List<MemberCert> mcs = memberService.queryMemberCertsByMemberid(memberid);
		model.addAttribute("mcs", mcs);	
		model.addAttribute("memberid", memberid);
		model.addAttribute("taskid", taskid);
		return "auth/detail";
	}
	
	@RequestMapping("/index")
	public String index() {
		return "auth/index";
	}
	
	@ResponseBody
	@RequestMapping("/pageQuery")
	public Object pageQuery(Integer pageno,Integer pagesize) {
		
		start();
		
		try {
			
			int count = processService.pageQueryTaskCount();
			
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("pageno", pageno);
			paramMap.put("pagesize", pagesize);
			
			List<Map<String, Object>> taskMaps = processService.pageQueryTaskData( paramMap );
			
			Page<Map<String, Object>> taskPage = new Page<Map<String, Object>>();
			taskPage.setTotalsize(count);
			taskPage.setPageno(pageno);
			taskPage.setPagesize(pagesize);
			taskPage.setDatas(taskMaps);
			data(taskPage);
			success();
		} catch ( Exception e ) {
			e.printStackTrace();
			fail();
		}
		
		return end();
		
	}
}
