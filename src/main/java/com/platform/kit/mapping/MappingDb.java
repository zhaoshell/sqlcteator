package com.platform.kit.mapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.platform.kit.mapping.exam.MapperObj;

public class MappingDb {

	private Class<?> clazz;
	private Object obj;
	private boolean mapUnderscoreToCamelCase = true;

	private MappingDb() {
	}

	public MappingDb(Class<?> clazz) {
		this();
		this.clazz = clazz;
	}

	public MappingDb(Object obj) {
		this();
		this.clazz = obj.getClass();
		this.obj = obj;
	}

	/**
	 * 对象获取字段时加下划线，反之，数据表映射对象时去掉下划线（驼峰）
	 * 
	 * @param mapUnderscoreToCamelCase
	 *            void
	 * @Author 杨健/YangJian
	 * @Date 2015年5月7日 上午11:10:25
	 * @Version 1.0.0
	 */
	public void setMapUnderscoreToCamelCase(boolean mapUnderscoreToCamelCase) {
		this.mapUnderscoreToCamelCase = mapUnderscoreToCamelCase;
	}

	/**
	 * 将对象属性转换对应的数据库字段
	 * 
	 * @param clazz
	 * @return List<String>
	 * @Author 杨健/YangJian
	 * @Date 2015年5月7日 上午11:48:07
	 * @Version 1.0.0
	 */
	public List<String> getColumns() {
		List<String> columns = new ArrayList<String>();
		Map<String, MappingField> map = Mapping.m.getFieldMap(clazz);
		Set<String> keySet = map.keySet();
		String keyName = null;
		MappingField objField = null;
		for (String key : keySet) {
			objField = map.get(map.get(key).getKeyName());
			if (!objField.isMapping()) {
				continue;
			}
			keyName = objField.getKeyName();
			if (mapUnderscoreToCamelCase) {
				keyName = camelToUnderscore(keyName);
				if ("".equals(keyName)) {
					continue;
				}
			}
			columns.add(keyName);
		}
		return columns;
	}

	public Object[] getValues() {
		// 被转换对象的field Map
		Map<String, MappingField> map = Mapping.m.getFieldMap(clazz);
		Set<String> keySet = map.keySet();
		Object fieldValue = null;
		MappingField objField = null;
		int length = 0 ;
		for (String key : keySet) {
			objField = map.get(map.get(key).getKeyName());
			if (!objField.isMapping()) {
				continue;
			}
			length++;
		}
		Object[] values = new Object[length];
		try {
			int i = 0;
			for (String key : keySet) {
				// 被转化对象的field信息。
				objField = map.get(map.get(key).getKeyName());
				if (!objField.isMapping()) {
					continue;
				}
				if (objField != null) {
					// 获得被转化对象的该字段的值。
					fieldValue = objField.getFieldValue(obj);
				}
				if (fieldValue != null) {
					values[i++] = fieldValue;
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return values;
	}

	/**
	 * 将驼峰字段转换为下划线字段
	 * 
	 * @param param
	 * @return String
	 * @Author 杨健/YangJian
	 * @Date 2015年5月7日 上午11:49:06
	 * @Version 1.0.0
	 */
	public static String camelToUnderscore(String param) {
		Pattern pattern = Pattern.compile("[A-Z]");
		if (param == null || param.equals("")) {
			return "";
		}
		StringBuilder builder = new StringBuilder(param);
		Matcher matcher = pattern.matcher(param);
		int i = 0;
		while (matcher.find()) {
			builder.replace(matcher.start() + i, matcher.end() + i, "_" + matcher.group().toLowerCase());
			i++;
		}
		if ('_' == builder.charAt(0)) {
			builder.deleteCharAt(0);
		}
		return builder.toString();
	}

	public static void main(String[] args) {
		MappingDb m = new MappingDb(MapperObj.class);
		List<String> columns = m.getColumns();
		for (String key : columns) {
			System.out.println(key);
		}
	}
}
