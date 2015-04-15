package com.github.sqlcteator.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

public class StrUtil {

	public static final String COMMA = ",";
	public static final String SPACE = " ";
	public static final String EMPTY = "";

	/**
	 * 判断一个对象是否为空。它支持如下对象类型：
	 * <ul>
	 * <li>null : 一定为空
	 * <li>字符串 : ""为空,多个空格也为空
	 * <li>数组
	 * <li>集合
	 * <li>Map
	 * <li>其他对象 : 一定不为空
	 * </ul>
	 * 
	 * @param obj
	 *            任意对象
	 * @return 是否为空
	 */
	public final static boolean isEmpty(final Object obj) {
		if (obj == null) {
			return true;
		}
		if (obj instanceof String) {
			return "".equals(String.valueOf(obj).trim());
		}
		if (obj.getClass().isArray()) {
			return Array.getLength(obj) == 0;
		}
		if (obj instanceof Collection<?>) {
			return ((Collection<?>) obj).isEmpty();
		}
		if (obj instanceof Map<?, ?>) {
			return ((Map<?, ?>) obj).isEmpty();
		}
		return false;
	}

	public final static boolean isNotEmpty(final Object obj) {
		return !isEmpty(obj);
	}

	/**
	 * 以 delimiter 为分隔符将集合转换为字符串
	 * 
	 * @param <T>
	 *            被处理的集合
	 * @param collection
	 *            集合
	 * @param delimiter
	 *            分隔符
	 * @return 连接后的字符串
	 */
	public static <T> String join(Iterable<T> collection, String delimiter) {
		StringBuilder sb = new StringBuilder();
		boolean isFirst = true;
		for (T item : collection) {
			if (isFirst) {
				isFirst = false;
			} else {
				sb.append(delimiter);
			}
			sb.append(item);
		}
		return sb.toString();
	}

	/**
	 * 以 delimiter 为分隔符将数组转换为字符串
	 * 
	 * @param <T>
	 *            被处理的集合
	 * @param array
	 *            数组
	 * @param delimiter
	 *            分隔符
	 * @return 连接后的字符串
	 */
	public static <T> String join(T[] array, String delimiter) {
		StringBuilder sb = new StringBuilder();
		boolean isFirst = true;
		for (T item : array) {
			if (isFirst) {
				isFirst = false;
			} else {
				sb.append(delimiter);
			}
			sb.append(item);
		}
		return sb.toString();
	}
}
