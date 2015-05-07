package com.platform.kit.mapping;

import java.lang.reflect.Field;

import com.platform.kit.mapping.annotations.Fields;

/**
 * @comment mapping字段类。不对外提供调用。
 * @author wangshuo
 * @date 2013-7-11 下午1:21:06
 * @version 1.0.0
 */
public class MappingField {

	/**
	 * 字段的Field属性。
	 */
	private Field field;

	/**
	 * 对外转换使用的key。默认是成员变量的名称。即Field的getName()。
	 */
	private String keyName;

	/**
	 * 是否映射填充,默认是。
	 */
	private boolean isMapping = true;

	public MappingField(Field field, Fields ann) {
		super();
		// 如果标注不为null，使用标注定义的内容来设置后两个字段。
		if (ann != null) {
			this.isMapping = ann.isMapping();
			if (ann.name() != null && ann.name().trim().length() != 0) {
				this.keyName = ann.name();
			}
		}
		// 如果ann没有设置keyName值，那么默认使用field名作为该值。
		if (this.keyName == null) {
			this.keyName = field.getName();
		}
		this.field = field;
		this.field.setAccessible(true);
	}

	public Field getField() {
		return field;
	}

	public String getKeyName() {
		return keyName;
	}

	public boolean isMapping() {
		return isMapping;
	}

	public Object getFieldValue(Object entity) throws IllegalArgumentException,
			IllegalAccessException {
		return this.field.get(entity);
	}

	public void setFieldValue(Object obj, Object value) throws IllegalArgumentException,
			IllegalAccessException {
		this.field.set(obj, value);
	}

}
