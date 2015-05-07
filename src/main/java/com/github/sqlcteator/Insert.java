package com.github.sqlcteator;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;

import com.platform.kit.mapping.MappingDb;

public class Insert {

	private String table;
	private Class<?> clazz;
	private MappingDb mappingDb;
	private final List<String> columns;
	private final List<Object[]> values;
	private boolean terminated = false;
	private final StringBuilder sql;
	private static final String NEW_LINE = System.getProperty("line.separator");

	private transient final Logger log;

	{
		this.log = Logger.getLogger(getClass().getName());
	}

	public Insert() {
		this.sql = new StringBuilder("INSERT INTO ");
		this.columns = new LinkedList<>();
		this.values = new LinkedList<>();
	}

	public Insert(String table) {
		this();
		this.table = table;
	}

	public Insert(Class<?> clazz) {
		this();
		this.clazz = clazz;
		this.mappingDb = new MappingDb(this.clazz);
		this.table = MappingDb.camelToUnderscore(this.clazz.getSimpleName());
		this.columns.addAll(this.mappingDb.getColumns());
	}
	
	public Insert(Object obj) {
		this();
		this.clazz = obj.getClass();
		this.mappingDb = new MappingDb(obj);
		this.table = MappingDb.camelToUnderscore(this.clazz.getSimpleName());
		this.columns.addAll(this.mappingDb.getColumns());
		this.values.add(this.mappingDb.getValues());
	}

	public Insert insert(String table) {
		this.table = table;
		return this;
	}

	public Insert append(String expression) {
		sql.append(expression);
		return this;
	}

	public Insert appendLine(String expression) {
		sql.append(expression);
		sql.append(NEW_LINE);
		return this;
	}

	public Insert newLine() {
		sql.append(NEW_LINE);
		return this;
	}

	public Insert table(String table) {
		this.table = table;
		return this;
	}

	public Insert columns(String... columns) {
		Collections.addAll(this.columns, columns);
		return this;
	}

	public Insert values(Object... values) {
		this.values.add(values);
		return this;
	}

	public String toString() {
		terminate();
		log.log(Level.INFO, "\n" + sql.toString());
		return sql.toString();
	}

	public String toPrepareStatementString() {
		terminatePrepareStatement();
		log.log(Level.INFO, "\n" + sql.toString());
		return sql.toString();
	}

	private void terminatePrepareStatement() {
		if (columns.isEmpty())
			throw new RuntimeException("No columns informed!");
		if (values.isEmpty())
			throw new RuntimeException("No values informed!");

		for (Object[] valueSet : values) {
			if (valueSet.length != columns.size()) {
				throw new RuntimeException("Value size different from column size!");
			}
		}

		if (!terminated) {
			this.appendLine(table).append(" ( ").append(StringUtils.join(columns, ", ")).appendLine(" )")
					.append("VALUES (");
			for (Object[] valueSet : values) {
				for (int i = 0; i < valueSet.length; i++) {
					if (i == valueSet.length - 1) {
						this.append("?");
					} else {
						this.append(StringUtils.join("?", ", "));
					}
				}
			}
			sql.substring(0, sql.length() - 1);
			sql.append(")");
		}
	}

	private void terminate() {
		if (columns.isEmpty())
			throw new RuntimeException("No columns informed!");
		if (values.isEmpty())
			throw new RuntimeException("No values informed!");

		for (Object[] valueSet : values) {
			if (valueSet.length != columns.size()) {
				throw new RuntimeException("Value size different from column size!");
			}
		}

		if (!terminated) {
			this.appendLine(table).append(" ( ").append(StringUtils.join(columns, ", ")).appendLine(" )")
					.append("VALUES ").append(StringUtils.join(getSqlValues(), ", "));
		}
	}

	public Object[] getColumns() {
		Object[] result = new Object[values.size()];
		for (int i = 0; i < result.length; i++) {
			result[i] = values.get(i);
		}
		return columns.toArray();
	}
	
	public Object[] getValues() {
		int length = 0;
		for (Object[] v : values) {
			length += v.length;
		}
		Object[] v = new Object[length];
		int i = 0;
		for (Object[] v2 : values) {
			for (Object object : v2) {
				v[i++] = object;
			}
		}
		return v;
	}

	private String[] getSqlValues() {
		String[] result = new String[values.size()];
		for (int i = 0; i < result.length; i++) {
			Object[] objs = values.get(i);
			result[i] = toValue(objs);
		}
		return result;
	}

	private String toValue(Object[] objs) {
		String[] result = new String[objs.length];

		for (int i = 0; i < result.length; i++) {
			if (objs[i] instanceof String) {
				result[i] = "'" + objs[i].toString() + "'";
			} else {
				result[i] = objs[i].toString();
			}
		}
		return "(" + StringUtils.join(result, ", ") + ")";
	}
}