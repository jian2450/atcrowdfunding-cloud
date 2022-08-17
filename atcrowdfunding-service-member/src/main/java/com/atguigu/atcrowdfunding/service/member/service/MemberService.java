package com.atguigu.atcrowdfunding.service.member.service;

import java.util.List;

import com.atguigu.atcrowdfunding.common.bean.Cert;
import com.atguigu.atcrowdfunding.common.bean.Member;
import com.atguigu.atcrowdfunding.common.bean.MemberCert;
import com.atguigu.atcrowdfunding.common.bean.Ticket;

public interface MemberService {

	Member queryMemberByLoginacct(String loginacct);

	Ticket queryTicketByMemberid(Integer id);

	void insertTicket(Ticket t);

	void updateAccountType(Member loginmember);

	void updateStep(Ticket t);

	void updateBasicInfo(Member loginmember);

	List<Cert> queryCertsByAccountType(String accttype);

	Member queryById(Integer memberid);

	void insertMemberCerts(List<MemberCert> mcs);

	void updateEmail(Member loginMember);

	void updateAuthcodeAndStep(Ticket t);

	void updateTicketAuthcode(Ticket t);

	void updateAuthstatus(Member loginMember);

	Member queryMemberByPiid(String piid);

	List<MemberCert> queryMemberCertsByMemberid(Integer memberid);

	void updateTicketStatus(Ticket t);

}
