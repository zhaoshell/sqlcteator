package com.github.sqlcteator.condition;

public class Null extends Condition {

	public Null(String propertyName, Boolean isNull) {
		this.column = propertyName;
		this.value = isNull;
	}

	@Override
	public String getCondition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPrefix() {
		return null;
	}

	@Override
	public String getColumn() {
		return column;
	}

	@Override
	public Object getValue() {
		return value;
	}

}
