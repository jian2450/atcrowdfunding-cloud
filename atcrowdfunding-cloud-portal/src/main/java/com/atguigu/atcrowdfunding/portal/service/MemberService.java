package com.atguigu.atcrowdfunding.portal.service;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.atguigu.atcrowdfunding.common.bean.Cert;
import com.atguigu.atcrowdfunding.common.bean.Member;
import com.atguigu.atcrowdfunding.common.bean.MemberCert;
import com.atguigu.atcrowdfunding.common.bean.Ticket;

@FeignClient("eureka-member-service")
public interface MemberService {
	
	@RequestMapping("/login/{loginacct}")
	public Member login(@PathVariable("loginacct") String loginacct);
	
	@RequestMapping("/queryTicketByMemberid/{id}")
	public Ticket queryTicketByMemberid(@PathVariable("id")Integer id);

	@RequestMapping("/insertTicket")
	public void insertTicket(@RequestBody Ticket t);
	
	@RequestMapping("/updateAccountType")
	public void updateAccountType(@RequestBody Member loginmember);
	
	@RequestMapping("/updateBasicInfo")
	public void updateBasicInfo(@RequestBody Member loginmember);

	@RequestMapping("/queryCertsByAccountType/{accttype}")
	public List<Cert> queryCertsByAccountType(@PathVariable("accttype")String accttype);
	
	@RequestMapping("/insertMemberCerts")
	public void insertMemberCerts(@RequestBody List<MemberCert> mcs);
	
	@RequestMapping("/updateEmail")
	public void updateEmail(@RequestBody Member loginMember);

	@RequestMapping("/updateTicketAuthcode")
	public void updateTicketAuthcode(@RequestBody Ticket t);
	
	@RequestMapping("/updateAuthstatus")
	public void updateAuthstatus(@RequestBody Member loginMember);
}
