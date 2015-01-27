package com.qzsitu.stourway.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * 读取存储目录配置，有待替换
 * @author Hongtu
 *
 */
public class StourWAYConfiguration {
	private Properties properties;
	private static StourWAYConfiguration stourWAYConfiguration;
	
	public StourWAYConfiguration(){
		try {
			properties = new Properties();
			InputStream in = this.getClass().getResourceAsStream("/stourWAY.properties");
			InputStreamReader ir = new InputStreamReader(in, "UTF-8");
			properties.load(ir);
			ir.close();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getProperties(String key) {
		if(stourWAYConfiguration == null)
			stourWAYConfiguration = new StourWAYConfiguration();
		return stourWAYConfiguration.properties.getProperty(key);
	}
	
	public static void main(String[] args){
		System.out.println(StourWAYConfiguration.getProperties("uploadPath"));
	}
}
