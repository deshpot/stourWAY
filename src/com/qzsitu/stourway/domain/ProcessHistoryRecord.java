package com.qzsitu.stourway.domain;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
@Entity
@NamedQueries({
	@NamedQuery(name = "ProcessHistoryRecord.queryHistoryRecordListByProcessId", query ="from ProcessHistoryRecord where processId=?"),
})
@Table(name = "stsd_processhistoryrecord")
public class ProcessHistoryRecord {
	String id;
	String processId;
	
	String user;
	String comment;
	String decision;
	String attachmentIds;
	Date datetime;
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Basic
	public String getProcessId() {
		return processId;
	}
	public void setProcessId(String processId) {
		this.processId = processId;
	}
	@Basic
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	@Lob
	@Column(columnDefinition="longtext")
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	@Basic
	public String getDecision() {
		return decision;
	}
	public void setDecision(String decision) {
		this.decision = decision;
	}
	@Basic
	public String getAttachmentIds() {
		return attachmentIds;
	}
	public void setAttachmentIds(String attachmentIds) {
		this.attachmentIds = attachmentIds;
	}
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDatetime() {
		return datetime;
	}
	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}
}
