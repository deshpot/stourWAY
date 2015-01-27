package com.qzsitu.stourway.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qzsitu.stourway.dao.GenericDao;
import com.qzsitu.stourway.domain.CustomizationView;
import com.qzsitu.stourway.domain.CustomizationXMod;

@Service
public class CustomizationService {
	@Autowired
	private GenericDao<CustomizationXMod> xmodDao;
	@Autowired
	private GenericDao<CustomizationView> viewDao;
	
	@Transactional
	public void addCustomizationXMod(CustomizationXMod xmod) {
		xmodDao.create(xmod);
	}
	
	@Transactional
	public List<CustomizationXMod> getAllCustomizationXMod() {
		return xmodDao.queryAll(CustomizationXMod.class);
	}
	
	@Transactional
	public List<CustomizationView> getAllCustomizationView() {
		return viewDao.queryAll(CustomizationView.class);
	}
}
