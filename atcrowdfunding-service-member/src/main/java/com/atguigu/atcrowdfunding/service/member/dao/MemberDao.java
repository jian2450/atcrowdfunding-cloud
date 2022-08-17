package com.atguigu.atcrowdfunding.service.member.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.atguigu.atcrowdfunding.common.bean.Cert;
import com.atguigu.atcrowdfunding.common.bean.Member;
import com.atguigu.atcrowdfunding.common.bean.MemberCert;
import com.atguigu.atcrowdfunding.common.bean.Ticket;

public interface MemberDao {
	
	@Select("select * from t_member where loginacct = #{loginacct}")
	Member queryMemberByLoginacct(String loginacct);
	
	@Select("select * from t_ticket where memberid = #{memberid} and status ='0'") 
	Ticket queryTicketByMemberid(Integer id);

	void insertTicket(Ticket t);

	void updateAccountType(Member loginmember);

	void updateStep(Ticket t);

	void updateBasicInfo(Member loginmember);

	List<Cert> queryCertsByAccountType(String accttype);
	
	@Select("select * from t_member where id=#{id}")
	Member queryById(Integer memberid);
	
	//Dao中尽量不要传集合类型，需加@Param注解
	void insertMemberCerts(@Param("mcs")List<MemberCert> mcs);

	@Update("update t_member set email = #{email} where id =#{id}")
	void updateEmail(Member loginMember);

	void updateAuthcodeAndStep(Ticket t);

	void updateTicketAuthcode(Ticket t);
	
	@Update("update t_member set authstatus = #{authstatus} where id =#{id}")
	void updateAuthstatus(Member loginMember);

	Member queryMemberByPiid(String piid);

	List<MemberCert> queryMemberCertsByMemberid(Integer memberid);
	
	@Update("update t_ticket set status = #{status} where id =#{id}")
	void updateTicketStatus(Ticket t);

}
