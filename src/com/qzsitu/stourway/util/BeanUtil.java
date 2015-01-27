package com.qzsitu.stourway.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.qzsitu.stourway.dao.GenericDao;

/****
 * 
 * @author Hongtu
 *
 */
public class BeanUtil<T> {
	public String dateFormat = "yyyy-MM-dd";
	
	/****
	 * 目前支持类型String Short Integer Double BigDecimal Boolean Date(yyyy-MM-dd)
	 * 待开发类型char short int long Long float Float double boolean
	 * @param map 分别对应bean的字段名和值,规则为beanName[xxxxxx(no.)][beanProperty],  eg: team[name]='第一组' team[studentList][xxxx][name]='宏图'
	 * @param bean 待赋值的bean
	 */	
	public List<T> map2Bean(Map<String, Object> map, T bean) {
		 return map2Bean(map, bean, null);
	}
	
	/****
	 * 目前支持类型String Short Integer Double BigDecimal Boolean Date(yyyy-MM-dd)
	 * 待开发类型char short int long Long float Float double boolean
	 * @param map 分别对应bean的字段名和值,规则为beanName[xxxxxx(no.)][beanProperty],  eg: team[name]='第一组' team[studentList][xxxx][name]='宏图'
	 * @param bean 待赋值的bean
	 * @param rule 转化规则，写法为 new Object(){public Object setId(Object id){ ......  return 处理过的id}
	 */
	public List<T> map2Bean(Map<String, Object> map, T bean, Object rule) {
		return map2Bean(map, bean, rule, null);
	}
	
	/****
	 * 目前支持类型String Short Integer Double BigDecimal Boolean Date(yyyy-MM-dd)
	 * 待开发类型char short int long Long float Float double boolean
	 * @param map 分别对应bean的字段名和值,规则为beanName[xxxxxx(no.)][beanProperty],  eg: team[name]='第一组' team[studentList][xxxx][name]='宏图'
	 * @param bean 待赋值的bean
	 * @param rule 转化规则，写法为 new Object(){public Object setId(Object id){ ......  return 处理过的id}
	 */
	public List<T> request2Bean(HttpServletRequest request, T bean, Object rule, GenericDao<T> dao) {
		Map<String, Object> map = new HashMap<String, Object>();
		Iterator<?> it = request.getParameterMap().keySet().iterator();
		while(it.hasNext()) {
			String key = (String)it.next();
			map.put(key, request.getParameter(key));
		}
		return map2Bean(map, bean, rule, dao);
	}
	
	/****
	 * 目前支持类型String Short Integer Double BigDecimal Boolean Date(yyyy-MM-dd)
	 * 待开发类型char short int long Long float Float double boolean
	 * @param map 分别对应bean的字段名和值,规则为beanName[xxxxxx(no.)][beanProperty],  eg: team[name]='第一组' team[studentList][xxxx][name]='宏图'
	 * @param bean 待赋值的bean
	 * @param rule 转化规则，写法为 new Object(){public Object setId(Object id){ ......  return 处理过的id}
	 */
	public List<T> map2Bean(Map<String, Object> map, T bean, Object rule, GenericDao<T> dao) {
		map = new HashMap<String,Object>(map);
		List<T> rsList = new ArrayList<T>();
		
		Pattern pat1 = Pattern.compile("(\\w+)\\[(\\d+)\\].*");
		Pattern pat2 = Pattern.compile("(\\w+)\\[(\\w+)\\].*");
		Map<String, Object> rsMap = new HashMap<String, Object>();

		Iterator<String> it = map.keySet().iterator();
		ArrayList<String> keyStringList = new ArrayList<String>();
		while(it.hasNext()) {
			String key = it.next();
			keyStringList.add(key);
		}
		for(String key : keyStringList) {
			if(map.get(key) == null) continue;
			Matcher mat = pat1.matcher(key);
			if(mat.find()) {
				String beanName = mat.group(1);
				String beanIndex = mat.group(2);
				if(rsMap.get(beanName) == null) {
					rsMap.put(beanName, new ArrayList<Map<String, Object>>());
				}
				Map<String, Object> subMap = new HashMap<String, Object>();
				for(String s : keyStringList) {
					if(s.startsWith(beanName+"[" + beanIndex + "]")) {
						subMap.put(s.substring((beanName+"[" + beanIndex + "]").length())
								.replaceFirst("\\[","").replaceFirst("\\]",""), map.get(s));
						map.remove(s);
					}
				}
				((List<Map<String, Object>>)(rsMap.get(beanName))).add(subMap);
			} else {
				mat = pat2.matcher(key);
				if(mat.find()) {
					String beanName = mat.group(1);
					if(rsMap.get(beanName) == null) {
						rsMap.put(beanName, new ArrayList<Map<String, Object>>());
					}
					Map<String, Object> subMap = new HashMap<String, Object>();
					for(String s : keyStringList) {
						if(s.startsWith(beanName)) {
							subMap.put(s.substring((beanName).length())
									.replaceFirst("\\[","").replaceFirst("\\]",""), map.get(s));
							map.remove(s);
						}
					}
					((List<Map<String, Object>>)(rsMap.get(beanName))).add(subMap);
				}
			}
		}
		Class<?> cls = bean.getClass();
		Method[] methods = cls.getDeclaredMethods();
		Field[] fields = cls.getDeclaredFields();
		Map<String, String> filedTypeMap = new HashMap<String, String>();
		for (Field field : fields) {
			try {
				String fieldType = field.getType().getSimpleName();
				String fieldName = field.getName();
				filedTypeMap.put(fieldName, fieldType);
			} catch (Exception e) {
				continue;
			}
		}
		String beanName = bean.getClass().getSimpleName().substring(0, 1).toLowerCase()  
                + bean.getClass().getSimpleName().substring(1);  
		List<Map<String, Object>> beanMapList = (List<Map<String, Object>>) rsMap.get(beanName);
		if(beanMapList == null)
			beanMapList = (List<Map<String, Object>>) rsMap.get(beanName + "List");
		if(beanMapList == null)
			return null;
		
		T obj = null;
		for(Map<String, Object> beanMap : beanMapList) {
			try {
				if((beanMap.get("id") != null) && (dao != null))
					obj = dao.read(bean.getClass(), (String) beanMap.get("id"));
				if(obj == null)
					obj = (T) bean.getClass().newInstance();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			Iterator<Entry<String, Object>> beanMapIt = beanMap.entrySet().iterator();
			while(beanMapIt.hasNext()) {
				Entry<String, Object> beanMapEntry = beanMapIt.next();
				String fieldName = (String) beanMapEntry.getKey();
				if(fieldName.indexOf('[') != -1) fieldName = fieldName.substring(0, fieldName.indexOf('['));
				Object fieldValue = beanMapEntry.getValue();
				if(!filedTypeMap.containsKey(fieldName)) continue;
				String fieldType = filedTypeMap.get(fieldName);
				String fieldSetName = parSetName(fieldName);
				if (!checkSetMet(methods, fieldSetName)) continue;
				
				try {
					Method ruleMethod = rule.getClass().getMethod(fieldSetName, new Class[]{Object.class});
					ruleMethod.setAccessible(true);
					Object result = ruleMethod.invoke(rule, new Object[] {beanMap});
					Class<?> clazz = result.getClass();
					if(result instanceof ArrayList) clazz = List.class;
					if(result instanceof HashMap) clazz = Map.class;
					Method fieldSetMet = cls.getMethod(fieldSetName, new Class[] {clazz});
					fieldSetMet.invoke(obj, new Object[]{result});
				} catch (Exception e) {
					try {
						if("String".equals(fieldType)) {
							Method fieldSetMet = cls.getMethod(fieldSetName, new Class[] {String.class});
							fieldSetMet.invoke(obj, new Object[]{fieldValue});
							continue;
						}
						if("Short".equals(fieldType)) {
							Method fieldSetMet = cls.getMethod(fieldSetName, new Class[] {Short.class});
							fieldSetMet.invoke(obj, new Object[]{Short.parseShort((String) fieldValue)});
							continue;
						}
						if("Integer".equals(fieldType)) {
							Method fieldSetMet = cls.getMethod(fieldSetName, new Class[] {Integer.class});
							fieldSetMet.invoke(obj, new Object[]{Integer.parseInt((String) fieldValue)});
							continue;
						}
						if("Double".equals(fieldType)) {
							Method fieldSetMet = cls.getMethod(fieldSetName, new Class[] {Double.class});
							fieldSetMet.invoke(obj, new Object[]{Double.parseDouble((String) fieldValue)});
							continue;
						}
						if("BigDecimal".equals(fieldType)) {
							Method fieldSetMet = cls.getMethod(fieldSetName, new Class[] {BigDecimal.class});
							fieldSetMet.invoke(obj, new Object[]{BigDecimal.valueOf(Double.parseDouble((String) fieldValue))});
							continue;
						}
		
						if("Boolean".equals(fieldType)) {
							Method fieldSetMet = cls.getMethod(fieldSetName, new Class[] {Boolean.class});
							fieldSetMet.invoke(obj, new Object[]{Boolean.valueOf((String) fieldValue)});
							continue;
						}
						if("Date".equals(fieldType)) {
							Method fieldSetMet = cls.getMethod(fieldSetName, new Class[] {Date.class});
							fieldSetMet.invoke(obj, new Object[]{fmtDate((String) fieldValue)});
							continue;
						}
					} catch(Exception ee) {
						
					}
				}
			}
			rsList.add(obj);
		}
		return rsList;
	}
	
	
	
	/*********
	 * @param beanList 要转化的beanList
	 * @return
	 */
	public List<Map<String, Object>> beanList2MapList(List<?> beanList) {
		List<Map<String, Object>> rs = new ArrayList<Map<String, Object>>();
		for(Object bean:beanList){
			rs.add(bean2Map(bean));
		}
		return rs;
	}	
	
	/*********
	 * @param beanList 要转化的beanList
	 * @param rule 转化规则，写法为 new Object(){public Object getId(Object id){ ......  return 处理过的id}
	 * @return
	 */
	public List<Map<String, Object>> beanList2MapList(List<?> beanList, Object rule) {
		List<Map<String, Object>> rs = new ArrayList<Map<String, Object>>();
		for(Object bean:beanList){
			rs.add(bean2Map(bean, rule));
		}
		return rs;
	}
	
	/*********
	 * @param beanList 要转化的beanList
	 * @param rule 转化规则，写法为 new Object(){public Object getId(Object id){ ......  return 处理过的id}
	 * @param level 转化层次，如 User -> GourpList( Group -> UserList ( User ->GroupList...
	 * @return
	 */
	public List<Map<String, Object>> beanList2MapList(List<?> beanList, Object rule, int level) {
		List<Map<String, Object>> rs = new ArrayList<Map<String, Object>>();
		for(Object bean:beanList){
			rs.add(bean2Map(bean, rule, level));
		}
		return rs;
	}
	
	
	/****
	 * @param bean 普通数据对象，按默认规则转化，date转化为yyyy-MM-dd，List和Map均转化第一层
	 * @return 返回BEAN对象转化成的Map类型
	 */
	public Map<String, Object> bean2Map(Object bean)  {
		return bean2Map(bean, null, 1);
	}
	
	/****
	 * @param bean 普通数据对象，按默认规则转化，date转化为yyyy-MM-dd，List和Map均转化第一层
	 * @param rule 转化规则，写法为 new Object(){public String getId(String id){ ......  return 处理过的id}
	 * @return 返回BEAN对象转化成的Map类型
	 */
	public Map<String, Object> bean2Map(Object bean, Object rule)  {
		return bean2Map(bean, rule, 1);
	}
	
	/***** 
	 * @param bean 普通数据对象
	 * @param rule 转化规则，写法为 new Object(){public String getId(String id){ ......  return 处理过的id}
	 * @param level 转化层次，如 User -> GourpList( Group -> UserList ( User ->GroupList...
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> bean2Map(Object bean, Object rule, int level) {
		//避免互相嵌套的bean无限循环
		level--; if(level < -1) return null;
		Map<String, Object> map = new HashMap<String, Object>();

		Class<?> cls = bean.getClass();

		Method[] methods = cls.getDeclaredMethods();
		Field[] fields = cls.getDeclaredFields();
		for (Field field : fields) {
			try {
				String fieldType = field.getType().getSimpleName();
				String fieldGetName = parGetName(field.getName());
				if (!checkGetMet(methods, fieldGetName)) {
					continue;
				}
				Method fieldGetMet = cls
						.getMethod(fieldGetName, new Class[] {});
				Object fieldVal = fieldGetMet.invoke(bean, new Object[] {});
				Object result = null;
				try {
					Method ruleMethod = rule.getClass().getMethod(fieldGetName, new Class[]{field.getType()});
					ruleMethod.setAccessible(true);
					result = ruleMethod.invoke(rule, new Object[] {fieldVal});
					if(!(result instanceof String || result instanceof Map || result instanceof List)) {
						result = bean2Map(result);
					}
				} catch (Exception ee) {
					if ("Date".equals(fieldType)) {
						result = fmtDate((Date) fieldVal);
					} else if ("Map".equals(fieldType)) {
						Map<String, Object> rs = new HashMap<String, Object>();
						if (null != fieldVal) {
							 Iterator<Entry<String, Object>> iter = ((Map<String, Object>) fieldVal).entrySet().iterator();
							 while (iter.hasNext()) {
								 Entry<String, Object> entry = (Entry<String, Object>) iter.next();
								 rs.put((String) entry.getKey(), bean2Map(entry.getValue(), rule, level));
							 }
							 result = rs;
						}
					} else if ("List".equals(fieldType)) {
						List<Object> rs = new ArrayList<Object>();
						if (null != fieldVal) {
							for(Object o:(List<Object>) fieldVal){
								rs.add(bean2Map(o, rule, level));
							}
						}
						result = rs;
					} else {
						if (null != fieldVal) {
							result = String.valueOf(fieldVal);
						}
					}
				}
				
				map.put(field.getName(), result);
			} catch (Exception e) {
				continue;
			}
		}

		return map;
	}
	
    /** 
     * 日期转化为String 
     *  
     * @param date 
     * @return date string 
     */  
    public String fmtDate(Date date) {  
        if (null == date) {  
            return null;  
        }  
        try {  
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat,  
                    Locale.CHINA);  
            return sdf.format(date);  
        } catch (Exception e) {  
            return null;  
        }  
    } 
    
    /** 
     * String 转化为日期
     *  
     * @param date 
     * @return date string 
     */  
    public Date fmtDate(String string) {   
        try {  
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat,  
                    Locale.CHINA);  
            return sdf.parse(string);
        } catch (Exception e) {  
            return null;  
        }  
    } 
    
    /** 
     * 拼接某属性的 get方法 
     *  
     * @param fieldName 
     * @return String 
     */  
    public String parGetName(String fieldName) {  
        if (null == fieldName || "".equals(fieldName)) {  
            return null;  
        }  
        int startIndex = 0;  
        if (fieldName.charAt(0) == '_')  
            startIndex = 1;  
        return "get"  
                + fieldName.substring(startIndex, startIndex + 1).toUpperCase()  
                + fieldName.substring(startIndex + 1);  
    }  
  
    
    
    /** 
     * 判断是否存在某属性的 set方法 
     *  
     * @param methods 
     * @param fieldSetMet 
     * @return boolean 
     */  
    public boolean checkSetMet(Method[] methods, String fieldSetMet) {  
        for (Method met : methods) {  
            if (fieldSetMet.equals(met.getName())) {  
                return true;  
            }  
        }  
        return false;  
    }  
  
    /** 
     * 判断是否存在某属性的 get方法 
     *  
     * @param methods 
     * @param fieldGetMet 
     * @return boolean 
     */  
    public boolean checkGetMet(Method[] methods, String fieldGetMet) {  
        for (Method met : methods) {  
            if (fieldGetMet.equals(met.getName())) {  
                return true;  
            }  
        }  
        return false;  
    } 
    
    /** 
     * 拼接在某属性的 set方法 
     *  
     * @param fieldName 
     * @return String 
     */  
    public String parSetName(String fieldName) {  
        if (null == fieldName || "".equals(fieldName)) {  
            return null;  
        }  
        int startIndex = 0;  
        if (fieldName.charAt(0) == '_')  
            startIndex = 1;  
        return "set"  
                + fieldName.substring(startIndex, startIndex + 1).toUpperCase()  
                + fieldName.substring(startIndex + 1);  
    }  
  
    /** 
     * 获取存储的键名称（调用parGetName） 
     *  
     * @param fieldName 
     * @return 去掉开头的get 
     */  
    public String parKeyName(String fieldName) {  
        String fieldGetName = parGetName(fieldName);  
        if (fieldGetName != null && fieldGetName.trim() != ""  
                && fieldGetName.length() > 3) {  
            return fieldGetName.substring(3);  
        }  
        return fieldGetName;  
    }  
    
}
