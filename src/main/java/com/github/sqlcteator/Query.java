package com.github.sqlcteator;

import static com.github.sqlcteator.util.StrUtil.isEmpty;
import static com.github.sqlcteator.util.StrUtil.isNotEmpty;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.github.sqlcteator.condition.And;
import com.github.sqlcteator.condition.Condition;
import com.github.sqlcteator.condition.Or;
import com.google.common.collect.Lists;

public class Query {

	public static void main(String[] args) {
		
		List<String>  columns= Lists.newArrayList("status","wall_code");
		Query query = new Query("wh_picking_wall");
		query.columns("wall_no", "id");
		query.eq("status", "PickingWallStatus_2");
		query.like("wall_code", "nihao");
		query.or(columns, "PickingWallStatus_2");
		query.isNotNull("status");
		String sql = query.toString();
		System.out.println(sql);
	}

	@Override
	public String toString() {
		if (isEmpty(columns)) {
			sql.append("*");
		} else {
			sql.append(StringUtils.join(columns, ", "));
		}
		sql.append(" from ").append(tableName);
		sql.append(" where 1=1 ");
		for (Condition condition : whereConditions) {
			sql.append(condition.getPrefix());
			sql.append(condition.getColumn());
			sql.append(condition.getOperator());
			sql.append(condition.getValue());
		}
		return sql.toString();
	}

	private String tableName;

	private final List<String> columns;

	private final StringBuilder sql;

	private final List<Object> parameters;

	private final List<Condition> whereConditions;

	private Query() {
		this.sql = new StringBuilder("select ");
		this.columns = new ArrayList<String>();
		this.whereConditions = new ArrayList<Condition>();
		this.parameters = new LinkedList<Object>();
	}

	private Query(String tableName) {
		this();
		this.tableName = tableName;
	}

	public static Query table(String table) {
		return new Query(table);
	}

	public String getTableName() {
		return tableName;
	}

	public Query columns(String... column) {
		for (String col : column) {
			columns.add(col);
		}
		return this;
	}

	/** 相等 */
	public Query eq(String propertyName, Object value) {
		if (isEmpty(value))
			return this;
		whereConditions.add(new And(propertyName, "=", value, parameters));
		return this;
	}

	/** 不相等 */
	public Query notEq(String propertyName, Object value) {
		if (isEmpty(value)) {
			return this;
		}
		whereConditions.add(new And(propertyName, "!=", value, parameters));
		return this;
	}

	/** 或 */
	public void or(List<String> propertyName, Object value) {
		if (isEmpty(value))
			return;
		if ((propertyName == null) || (propertyName.size() == 0))
			return;
		whereConditions.add(new Or(propertyName, "=", value));
	}

	public void or(String propertyName, Object value) {
		if (isEmpty(propertyName))
			return;
		if (isEmpty(value))
			return;
		whereConditions.add(new Or(propertyName, "=", value));
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
			value = StringUtils.join("'%" , value , "%'");
		whereConditions.add(new And(propertyName, "like", value, parameters));
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

	public List<Condition> getWhereConditions() {
		return whereConditions;
	}

}
