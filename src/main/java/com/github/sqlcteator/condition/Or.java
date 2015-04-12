package com.github.sqlcteator.condition;

import java.util.List;

import com.github.sqlcteator.util.StringUtil;

public class Or extends Condition {

	List<String> propertyName;

	public Or(List<String> propertyName, Object value) {
		this.propertyName = propertyName;
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
		if (StringUtil.isEmpty(propertyName)) {
			return "";
		}
		StringBuilder sql = new StringBuilder(getPrefix());
		int size = propertyName.size();
		if (size > 1) {
			sql.append(" ( ");
		}
		for (int i = 0; i < size; i++) {
			sql.append(" ").append(propertyName.get(i)).append(" = '").append(getValue()).append("'");
			if (i < size - 1) {
				sql.append(" AND ");
			}
		}
		if (size > 1) {
			sql.append(" ) ");
		}
		return sql.toString();
	}
}