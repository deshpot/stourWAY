package com.qzsitu.stourway.xmod.accountant;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "stsd_xmod_accountant_inventory")
public class Inventory {
	String id;
	String operation;
	String contactUnit;
	String materiel;
	int quantity;
	Double totalCost;
	Double totalPrice;
	String warehouse;
	Date date;
	
	public static final String INVENTORY_WAREHOUSE_0 = "0";
	public static final String INVENTORY_WAREHOUSE_1 = "1";
	public static final String INVENTORY_WAREHOUSE_2 = "2";
	public static final String INVENTORY_WAREHOUSE_3 = "3";
	public static final String INVENTORY_WAREHOUSE_4 = "4";
	
	/**
	 * 采购入库
	 */
	public static final String INVENTORY_OPERATION_0 = "0";
	/**
	 * 销售出库
	 */
	public static final String INVENTORY_OPERATION_1 = "1";
	public static final String INVENTORY_OPERATION_2 = "2";
	public static final String INVENTORY_OPERATION_3 = "3";
	public static final String INVENTORY_OPERATION_4 = "4";
	
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
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	@Basic
	public String getContactUnit() {
		return contactUnit;
	}
	public void setContactUnit(String contactUnit) {
		this.contactUnit = contactUnit;
	}
	@Basic
	public String getMateriel() {
		return materiel;
	}
	public void setMateriel(String materiel) {
		this.materiel = materiel;
	}
	@Basic
	public Double getTotalCost() {
		return totalCost;
	}
	public void setTotalCost(Double totalCost) {
		this.totalCost = totalCost;
	}
	@Basic
	public Double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}
	@Basic
	public String getWarehouse() {
		return warehouse;
	}
	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}
	@Basic
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
}
