package com.github.sqlcteator.condition;

public abstract class Condition {

	String column;
	Object value;

	public abstract String getCondition();

	public abstract String getPrefix();

	public abstract String getColumn();

	public abstract Object getValue();
}
