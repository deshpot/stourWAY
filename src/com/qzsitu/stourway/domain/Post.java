package com.qzsitu.stourway.domain;

import java.util.Date;import javax.persistence.Basic;
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
@NamedQuery(name = "Post.getPostByType", query ="Select new Post(id,title,author,datetime,type) from Post where type like ? order by datetime desc"),
@NamedQuery(name = "Post.getPostByTitle", query = "from Post where title like :title and type like :type"),
@NamedQuery(name = "Post.getPostByDatetime", query = "from Post where datetime like :datetime and type like :type and author like :author"),
@NamedQuery(name = "Post.getPostByTypeAndAuthor", query ="Select new Post(id,title,author,datetime,type) from Post where type like ? and author like ? order by datetime desc"),
@NamedQuery(name = "Post.getPostList", query ="Select new Post(id,title,author,datetime,type) from Post order by datetime desc"),
@NamedQuery(name = "Post.getPostCount", query ="Select count(*) from Post"),
@NamedQuery(name = "Post.getPostListByPostIdList", query ="Select new Post(id,title,author,datetime,type) from Post where (id in (:postIdList)) order by datetime desc"),
})
@Table(name="stsd_post")
public class Post {
	private String id;
	private String title;
	private String author;
	private String content;
	private Date datetime;
	private String type;
	private String Status;
	
	public static final String POST_STATUS_PUBLIC = "public";
	public static final String POST_STATUS_DRAFT = "draft";
	
	public Post(){}
	public Post(String id,String title,String author,Date datetime,String type){
		this.id = id;  
		this.title = title;
		this.author=author;
		this.datetime=datetime;
		this.type=type;
	}
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@Lob
	@Column(columnDefinition = "text")
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	
	@Basic
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	
	@Lob
	@Column(columnDefinition="longtext")
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDatetime() {
		return datetime;
	}
	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}
	@Basic
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Basic
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
}
