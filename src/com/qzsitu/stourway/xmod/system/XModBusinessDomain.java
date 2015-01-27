package com.qzsitu.stourway.xmod.system;

import java.util.Date;

public interface XModBusinessDomain {
	public String getId();
	public String getProcessId();
	public String getDescription();
	public String getInitiator();
	public Date getStartTime();
	public Date getEndTime();
	public String getStatus();
}
