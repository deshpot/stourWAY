package com.qzsitu.stourway.xmod.system;

import java.util.HashMap;
import java.util.Map;

public class XModResponse {
	private Map<String, Object> attributtes;
	private String pageReturn;
	private Object ajaxReturn;
	
	public XModResponse(){
		attributtes = new HashMap<String, Object>();
	}
	
	public Map<String, Object> getAttributtes() {
		return attributtes;
	}
	public void setAttributtes(Map<String, Object> attributtes) {
		this.attributtes = attributtes;
	}
	public String getPageReturn() {
		return pageReturn;
	}
	public void setPageReturn(String pageReturn) {
		this.pageReturn = pageReturn;
	}
	public Object getAjaxReturn() {
		return ajaxReturn;
	}
	public void setAjaxReturn(Object ajaxReturn) {
		this.ajaxReturn = ajaxReturn;
	}
	public void put(String key, Object value) {
		this.attributtes.put(key, value);
	}
}
