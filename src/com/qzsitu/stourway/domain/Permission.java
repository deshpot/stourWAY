package com.qzsitu.stourway.domain;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


@Entity
@NamedQueries({
	@NamedQuery(name = "Permission.checkPermission",
			query = "from Permission where roleId = ? and permission = ?"),
	@NamedQuery(name = "Permission.getPermissionList",
			query = "from Permission where type = ?"),
	@NamedQuery(name = "Permission.getRolePermissionList",
			query = "from Permission where roleId = ? and type = ?"),
	@NamedQuery(name = "Permission.deletePermissionList",
			query = "delete from Permission where type = :type")
})
@Table(name="stsd_permission")
public class Permission {
	public final static String SQL_DELETE_ALL_PERMISSION = "delete from STSD_PERMISSION";
	
	private String id;
	private String roleId;
	private String permission;
	private String type;
	
	public static final String PERMISSION_TYPE_SYSTEM = "system";
	public static final String PERMISSION_TYPE_KNOWLEDGE = "knowledge";
	public static final String PERMISSION_TYPE_XMOD = "xmod";
	public static final String PERMISSION_ROLE_EVERYONE = "everyone";
	
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
	public String getPermission() {
		return permission;
	}
	public void setPermission(String permission) {
		this.permission = permission;
	}
	@Basic
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	@Basic
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
