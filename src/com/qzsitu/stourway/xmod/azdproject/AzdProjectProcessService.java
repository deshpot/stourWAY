package com.qzsitu.stourway.xmod.azdproject;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.runtime.Execution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qzsitu.stourway.dao.GenericDao;

@Service
public class AzdProjectProcessService {

	@Autowired
	private GenericDao<AzdProject> azdProjectDao;
	
	@Transactional
	public List<String> getAssigneeList(Execution execution, String taskName) {
		List<String> assigneeList = new ArrayList<String>();
		
		AzdProject azdProject = azdProjectDao.queryOne("AzdProject.getAzdProjectByExecutionId", new Object[]{execution.getProcessInstanceId()});
		
		
		String conceptSolutionManager = azdProject.getConceptSolutionManager();
		String detailedSolutionManager = azdProject.getDetailedSolutionManager();
		String conceptDesignManager = azdProject.getConceptDesignManager();
		String detailedDesignManager = azdProject.getDetailedDesignManager();
		
		if(!assigneeList.contains(conceptSolutionManager))
			assigneeList.add(conceptSolutionManager);
		if(!assigneeList.contains(detailedSolutionManager))
			assigneeList.add(detailedSolutionManager);
		if(!assigneeList.contains(conceptDesignManager))
			assigneeList.add(conceptDesignManager);
		if(!assigneeList.contains(detailedDesignManager))
			assigneeList.add(detailedDesignManager);
		
		return assigneeList;
	}
}
