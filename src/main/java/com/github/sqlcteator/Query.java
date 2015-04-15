package com.github.sqlcteator;

import static com.github.sqlcteator.util.StrUtil.isEmpty;
import static com.github.sqlcteator.util.StrUtil.isNotEmpty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.github.sqlcteator.condition.And;
import com.github.sqlcteator.condition.Condition;
import com.github.sqlcteator.condition.Or;

public class Query {

	private String tableName;

	private List<Condition> conditions;

	private Query() {
	}

	private Query(String tableName) {
		this.tableName = tableName;
		this.conditions = new ArrayList<Condition>();
	}

	public static Query table(String table) {
		return new Query(table);
	}

	public String getTableName() {
		return tableName;
	}

	/** 相等 */
	public Query eq(String propertyName, Object value) {
		if (isEmpty(value))
			return this;
		conditions.add(new And(propertyName, "=", value));
		return this;
	}

	/** 不相等 */
	public void notEq(String propertyName, Object value) {
		if (isEmpty(value)) {
			return;
		}
		conditions.add(new And(propertyName, "!=", value));
	}

	/** 或 */
	public void or(List<String> propertyName, Object value) {
		if (isEmpty(value))
			return;
		if ((propertyName == null) || (propertyName.size() == 0))
			return;
		conditions.add(new Or(propertyName, "or", value));
	}

	/** 空 */
	public void isNull(String propertyName) {
		if (isEmpty(propertyName)) {
			return;
		}
	}

	/** 非空 */
	public void isNotNull(String propertyName) {
		if (isEmpty(propertyName)) {
			return;
		}
	}

	/**
	 * not in
	 * 
	 * @param propertyName
	 *            属性名称
	 * @param value
	 *            值集合
	 */
	public void notIn(String propertyName, Collection<?> value) {
		if ((value == null) || (value.size() == 0)) {
			return;
		}
	}

	/**
	 * 模糊匹配
	 * 
	 * @param propertyName
	 *            属性名称
	 * @param value
	 *            属性值
	 */
	public void like(String propertyName, String value) {
		if (isEmpty(value))
			return;
		if (value.indexOf("%") < 0)
			value = "%" + value + "%";
	}

	/**
	 * 时间区间查询
	 * 
	 * @param propertyName
	 *            属性名称
	 * @param lo
	 *            属性起始值
	 * @param go
	 *            属性结束值
	 */
	public void between(String propertyName, Date lo, Date go) {
		if (isNotEmpty(lo) && isNotEmpty(go)) {
			return;
		}

		if (isNotEmpty(lo) && isEmpty(go)) {
			return;
		}

		if (isNotEmpty(go)) {
		}

	}

	public void between(String propertyName, Number lo, Number go) {
		if (!(isEmpty(lo)))
			ge(propertyName, lo);

		if (!(isEmpty(go)))
			le(propertyName, go);
	}

	/**
	 * 小于等于
	 * 
	 * @param propertyName
	 *            属性名称
	 * @param value
	 *            属性值
	 */
	public void le(String propertyName, Number value) {
		if (isEmpty(value)) {
			return;
		}
	}

	/**
	 * 小于
	 * 
	 * @param propertyName
	 *            属性名称
	 * @param value
	 *            属性值
	 */
	public void lt(String propertyName, Number value) {
		if (isEmpty(value)) {
			return;
		}
	}

	/**
	 * 大于等于
	 * 
	 * @param propertyName
	 *            属性名称
	 * @param value
	 *            属性值
	 */
	public void ge(String propertyName, Number value) {
		if (isEmpty(value)) {
			return;
		}
	}

	/**
	 * 大于
	 * 
	 * @param propertyName
	 *            属性名称
	 * @param value
	 *            属性值
	 */
	public void gt(String propertyName, Number value) {
		if (isEmpty(value)) {
			return;
		}
	}

	/**
	 * in
	 * 
	 * @param propertyName
	 *            属性名称
	 * @param value
	 *            值集合
	 */
	public void in(String propertyName, Collection<?> value) {
		if ((value == null) || (value.size() == 0)) {
			return;
		}
	}

	public List<Condition> getConditions() {
		return conditions;
	}

	public String select() {
		return SqlCteator.create(this).select().toString();
	}

	public String insert() {
		return SqlCteator.create(this).insert().toString();
	}

}
