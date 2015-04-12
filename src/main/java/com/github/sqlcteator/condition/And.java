package com.github.sqlcteator.condition;

public class And extends Condition {

	public And(String propertyName, Object value) {
		this.column = propertyName;
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
		return " AND ";
	}

	@Override
	public String getCondition() {
		return new StringBuilder(getPrefix()).append(getColumn()).append(" = '").append(getValue()).append("' ")
				.toString();
	}

}
