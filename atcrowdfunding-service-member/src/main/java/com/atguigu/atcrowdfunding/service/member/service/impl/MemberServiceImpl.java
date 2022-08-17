package com.atguigu.atcrowdfunding.service.member.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.atguigu.atcrowdfunding.common.bean.Cert;
import com.atguigu.atcrowdfunding.common.bean.Member;
import com.atguigu.atcrowdfunding.common.bean.MemberCert;
import com.atguigu.atcrowdfunding.common.bean.Ticket;
import com.atguigu.atcrowdfunding.service.member.dao.MemberDao;
import com.atguigu.atcrowdfunding.service.member.service.MemberService;

@Transactional(readOnly = true)
@Service
public class MemberServiceImpl implements MemberService {
	
	@Autowired
	private MemberDao memberDao;

	@Override
	public Member queryMemberByLoginacct(String loginacct) {
		return memberDao.queryMemberByLoginacct(loginacct);
	}

	@Override
	public Ticket queryTicketByMemberid(Integer id) {
		return memberDao.queryTicketByMemberid(id);
	}
	
	@Transactional
	public void insertTicket(Ticket t) {
		memberDao.insertTicket(t);
	}

	@Transactional
	public void updateAccountType(Member loginmember) {
		memberDao.updateAccountType(loginmember);
	}

	@Transactional
	public void updateStep(Ticket t) {
		memberDao.updateStep(t);
	}

	@Transactional
	public void updateBasicInfo(Member loginmember) {
		memberDao.updateBasicInfo(loginmember);
	}

	@Override
	public List<Cert> queryCertsByAccountType(String accttype) {
		return memberDao.queryCertsByAccountType(accttype);
	}

	@Override
	public Member queryById(Integer memberid) {
		return memberDao.queryById(memberid);
	}

	@Transactional
	public void insertMemberCerts(List<MemberCert> mcs) {
		memberDao.insertMemberCerts(mcs);
	}

	@Transactional
	public void updateEmail(Member loginMember) {
		memberDao.updateEmail(loginMember);
	}

	@Transactional
	public void updateAuthcodeAndStep(Ticket t) {
		memberDao.updateAuthcodeAndStep(t);
	}

	@Transactional
	public void updateTicketAuthcode(Ticket t) {
		memberDao.updateTicketAuthcode(t);
	}

	@Transactional
	public void updateAuthstatus(Member loginMember) {
		memberDao.updateAuthstatus(loginMember);
	}

	@Override
	public Member queryMemberByPiid(String piid) {
		return memberDao.queryMemberByPiid(piid);
	}

	@Override
	public List<MemberCert> queryMemberCertsByMemberid(Integer memberid) {
		return memberDao.queryMemberCertsByMemberid(memberid);
	}

	@Transactional
	public void updateTicketStatus(Ticket t) {
		memberDao.updateTicketStatus(t);
	}
	
}
