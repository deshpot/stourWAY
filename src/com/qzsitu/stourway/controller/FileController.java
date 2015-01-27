package com.qzsitu.stourway.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.qzsitu.stourway.domain.FileItem;
import com.qzsitu.stourway.domain.User;
import com.qzsitu.stourway.exception.StourWAYException;
import com.qzsitu.stourway.service.KnowledgeService;
import com.qzsitu.stourway.util.StourWAYConfiguration;


@Controller
@RequestMapping(value="/file")
public class FileController {
	@Autowired
	private CommonsMultipartResolver multipartResolver;
	@Autowired
	private KnowledgeService knowledgeService;
	
	//文件上传
	@RequestMapping(value = "/upload")
	public void upload(HttpServletRequest request, HttpServletResponse response) throws StourWAYException{
		try {
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html; charset=UTF-8");

			User user = (User) (request.getSession().getAttribute("user"));
			String uploadFiles = "";
			if (multipartResolver.isMultipart(request)) {
				MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;

				Iterator<String> iter = multiRequest.getFileNames();
				while (iter.hasNext()) {
					MultipartFile file = multiRequest.getFile(iter.next());
					if (file != null) {
						String fileName = file.getOriginalFilename();

						FileItem fileItem = new FileItem();
						fileItem.setFileName(fileName);
						fileItem.setFileType(fileName.substring(fileName
								.lastIndexOf('.') + 1));
						fileItem.setCreator(user.getId());
						fileItem.setCreatedTime(new Date());
						String fileItemId = knowledgeService
								.createFile(fileItem).getId();
						
						uploadFiles += fileItemId + ":" + fileName + "|";

						String path = StourWAYConfiguration.getProperties("uploadPath") + fileItemId;
						File localFile = new File(path);
						try {
							file.transferTo(localFile);
						} catch (IOException e) {
							knowledgeService.deleteFile(fileItemId);
							e.printStackTrace();
							response.getWriter()
									.write("<script>parent.uploadCallback(-1, '上传文件出现异常，请重新尝试或者联系管理员')</script>");
						}
					}
				}
				response.getWriter().write(
						"<script>parent.uploadCallback(0, '" + uploadFiles
								+ "')</script>");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//文件下载
	@RequestMapping(value = "/download/{fileItemId}")
	public void upload( HttpServletRequest request, @PathVariable String fileItemId,
			HttpServletResponse response) throws StourWAYException{
		try {
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html; charset=UTF-8");
			
			FileItem fileItem = knowledgeService.getFile(fileItemId);
			
			File downloadFile=new File(StourWAYConfiguration.getProperties("uploadPath") + fileItemId);
			
			if (downloadFile.exists()) {
			    Long length=downloadFile.length();
			    response.setContentLength(length.intValue());
			    String agent =request.getHeader("USER-AGENT");
			    String fileName;
			    if (null != agent && -1 != agent.indexOf("Firefox")) {
			    	fileName = new String( fileItem.getFileName().getBytes("gb2312"), "ISO8859-1" );
			    } else {
			    	fileName = URLEncoder.encode(fileItem.getFileName(), "UTF-8");
			    }
			    response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
			    
			    ServletOutputStream servletOutputStream=response.getOutputStream();
			    FileInputStream fileInputStream=new FileInputStream(downloadFile);
			    BufferedInputStream bufferedInputStream=new BufferedInputStream(fileInputStream);
			    int size=0;
			    byte[] b=new byte[4096];
			    while ((size=bufferedInputStream.read(b))!=-1) {
			        servletOutputStream.write(b, 0, size);
			    }
			    servletOutputStream.flush();
			    servletOutputStream.close();
			    bufferedInputStream.close();
			}else {
			    
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
