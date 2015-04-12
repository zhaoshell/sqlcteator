package com.github.sqlcteator;

import static com.github.sqlcteator.util.StringUtil.isEmpty;
import static com.github.sqlcteator.util.StringUtil.isNotEmpty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.github.sqlcteator.condition.And;
import com.github.sqlcteator.condition.Condition;
import com.github.sqlcteator.condition.Or;

public class Query {
	// private static final String ADDLINE = System.getProperty("line.separator");
	private static final String ADDSPACE = " ";
	private String table;

	private List<Condition> conditions;

	private final StringBuilder sql;

	public Query() {
		this.sql = new StringBuilder();
	}

	private Query(String table) {
		this.table = table;
		this.sql = new StringBuilder();
		this.conditions = new ArrayList<Condition>();
	}

	public static Query cteator(String table) {
		return new Query(table);
	}

	/** 相等 */
	public void eq(String propertyName, Object value) {
		if (isEmpty(value))
			return;
		conditions.add(new And(propertyName, value));
	}

	/** 不相等 */
	public void notEq(String propertyName, Object value) {
		if (isEmpty(value)) {
			return;
		}

	}

	/** 或 */
	public void or(List<String> propertyName, Object value) {
		if (isEmpty(value))
			return;
		if ((propertyName == null) || (propertyName.size() == 0))
			return;
		conditions.add(new Or(propertyName, value));
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

	private StringBuilder append(String value) {
		sql.append(value).append(ADDSPACE);
		return sql;
	}

	public Query select() {
		append("SELECT * FROM");
		append(table);
		append("WHERE 1=1");
		for (Condition condition : conditions) {
			append(condition.getCondition());
		}
		return this;
	}

	public String sql() {
		return sql.toString();
	}

	@Test
	public void main() {
		List<String> or = new ArrayList<String>();
		or.add("name");
		// or.add("sex");
		Query query = Query.cteator("nihao");
		query.eq("name", "yangjian");
		query.or(or, "lilei");
		System.out.println(query.select().sql());
	}
}
