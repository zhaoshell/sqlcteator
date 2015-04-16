package com.github.sqlcteator.condition;

import java.util.List;

import com.github.sqlcteator.util.StrUtil;

public class Or extends Condition {

	List<String> propertyName;

	public Or(List<String> propertyName, String operator, Object value) {
		this.propertyName = propertyName;
		this.operator = operator;
		this.value = value;
	}

	public Or(String propertyName, String operator, Object value) {
		this.column = propertyName;
		this.operator = operator;
		this.value = value;
	}

	@Override
	public String getColumn() {
		return column;
	}

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public String getPrefix() {
		return " OR ";
	}

	@Override
	public String getCondition() {
		if (StrUtil.isEmpty(propertyName)) {
			return "";
		}
		StringBuilder sql = new StringBuilder();
		int size = propertyName.size();
		if (size > 1) {
			sql.append(" ( ");
		}
		for (int i = 0; i < size; i++) {
			sql.append(" ").append(propertyName.get(i)).append(" = ?");
			if (i < size - 1) {
				sql.append(" AND ");
			}
		}
		if (size > 1) {
			sql.append(" ) ");
		}
		return sql.toString();
	}

	@Override
	public String getOperator() {
		return operator;
	}
}