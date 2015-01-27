package com.qzsitu.stourway.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.qzsitu.stourway.domain.KnowledgeMenu;
import com.qzsitu.stourway.service.KnowledgeService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContextTest.xml")
public class KnowledgeServiceTest {
	@Autowired
	private KnowledgeService knowledgeService;
	
	@Before
	public void setUp(){
		knowledgeService.createMenu("默认目录");
	}
	@After
	public void tearDown() {
		for(KnowledgeMenu menu : knowledgeService.getKnowledgeMenuList())
			knowledgeService.removeMenu(menu.getId());
	}
	
	
	@Test
	public void getKnowledgeMenuList(){
		assertEquals(1, knowledgeService.getKnowledgeMenuList().size());
	}
}
