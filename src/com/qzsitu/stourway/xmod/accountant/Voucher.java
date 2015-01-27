package com.qzsitu.stourway.xmod.accountant;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@NamedQueries({
	@NamedQuery(name = "Voucher.getVoucherList", query ="from Voucher ORDER BY date, codeNumber"),
	@NamedQuery(name = "Voucher.getVoucherDetail", query ="from Voucher where date=? and codeName=? and codeNumber=? ORDER BY date, codeNumber"),
})
@Table(name = "stsd_xmod_accountant_voucher")
public class Voucher {
	String id;
	Date date;
	String codeName;
	String codeNumber;
	String comment;
	String subject;
	Double debit;
	Double credit;
	Integer invoices;
	String listerUserId;
	String reviewUserId;
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	@Basic
	public String getCodeName() {
		return codeName;
	}
	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}
	@Basic
	public String getCodeNumber() {
		return codeNumber;
	}
	public void setCodeNumber(String codeNumber) {
		this.codeNumber = codeNumber;
	}
	@Basic
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	@Basic
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	@Basic
	/**
	 * 借方
	 */
	public Double getDebit() {
		return debit;
	}
	public void setDebit(Double debit) {
		this.debit = debit;
	}
	@Basic
	/**
	 * 贷方
	 */
	public Double getCredit() {
		return credit;
	}
	public void setCredit(Double credit) {
		this.credit = credit;
	}
	@Basic
	public Integer getInvoices() {
		return invoices;
	}
	public void setInvoices(Integer invoices) {
		this.invoices = invoices;
	}
	@Basic
	public String getListerUserId() {
		return listerUserId;
	}
	public void setListerUserId(String listerUserId) {
		this.listerUserId = listerUserId;
	}
	@Basic
	public String getReviewUserId() {
		return reviewUserId;
	}
	public void setReviewUserId(String reviewUserId) {
		this.reviewUserId = reviewUserId;
	}
}
