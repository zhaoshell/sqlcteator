package com.github.sqlcteator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.github.sqlcteator.condition.Condition;
import com.github.sqlcteator.condition.Or;
import com.github.sqlcteator.util.StrUtil;

/**
 * SQL构建器<br>
 * 首先拼接SQL语句，值使用 ? 占位<br>
 * 调用getParamValues()方法获得占位符对应的值
 * 
 * @author Looly
 *
 */
public class SqlCteator {

	// --------------------------------------------------------------- Static methods start
	/**
	 * 创建SQL构建器
	 * 
	 * @return SQL构建器
	 */
	public static SqlCteator create() {
		return new SqlCteator();
	}

	/**
	 * 创建SQL构建器
	 * 
	 * @param wrapper
	 *            包装器
	 * @return SQL构建器
	 */
	public static SqlCteator create(Query query) {
		return new SqlCteator(query);
	}

	// --------------------------------------------------------------- Static methods end

	// --------------------------------------------------------------- Enums start
	/**
	 * 逻辑运算符
	 * 
	 * @author Looly
	 *
	 */
	public static enum LogicalOperator {
		/** 且，两个条件都满足 */
		AND,
		/** 或，满足多个条件的一个即可 */
		OR
	}

	/**
	 * 排序方式（升序或者降序）
	 * 
	 * @author Looly
	 *
	 */
	public static enum Order {
		/** 升序 */
		ASC,
		/** 降序 */
		DESC
	}

	/**
	 * SQL中多表关联用的关键字
	 * 
	 * @author Looly
	 *
	 */
	public static enum Join {
		/** 如果表中有至少一个匹配，则返回行 */
		INNER,
		/** 即使右表中没有匹配，也从左表返回所有的行 */
		LEFT,
		/** 即使左表中没有匹配，也从右表返回所有的行 */
		RIGHT,
		/** 只要其中一个表中存在匹配，就返回行 */
		FULL
	}

	// --------------------------------------------------------------- Enums end

	final private StringBuilder sql = new StringBuilder();
	/** 占位符对应的值列表 */
	final private List<Object> paramValues = new ArrayList<Object>();
	/** 条件包装器 */
	private Query query;

	// --------------------------------------------------------------- Constructor start
	private SqlCteator() {
	}

	private SqlCteator(Query query) {
		this.query = query;
	}

	// --------------------------------------------------------------- Constructor end

	// --------------------------------------------------------------- Builder start
	/**
	 * 插入
	 * 
	 * @param entity
	 *            实体
	 * @return 自己
	 */
	public SqlCteator insert() {
		// 验证
		// DbUtil.validateEntity(query);

		sql.append("INSERT INTO ").append(query.getTableName()).append(" (");

		final StringBuilder placeHolder = new StringBuilder(") VALUES (");

		// for (Condition condition : query.getConditions()) {
		// // 非第一个参数，追加逗号
		// if (paramValues.size() > 0) {
		// sql.append(", ");
		// placeHolder.append(", ");
		// }
		//
		// sql.append(condition.getColumn());
		// placeHolder.append("?");
		// paramValues.add(condition.getValue());
		// }
		sql.append(placeHolder.toString()).append(")");

		return this;
	}

	/**
	 * 删除
	 * 
	 * @param tableName
	 *            表名
	 * @return 自己
	 */
	public SqlCteator delete(String tableName) {
		if (StrUtil.isEmpty(tableName)) {
			throw new RuntimeException("Table name is blank !");
		}

		sql.append("DELETE FROM ").append(tableName);

		return this;
	}

	/**
	 * 更新
	 * 
	 * @param entity
	 *            要更新的实体
	 * @return 自己
	 */
	public SqlCteator update() {
		// 验证
		// DbUtil.validateEntity(query);
		sql.append("UPDATE ").append(query.getTableName()).append(" SET ");
		// for (Condition condition : query.getWhereConditions()) {
		// if (paramValues.size() > 0) {
		// sql.append(", ");
		// }
		// sql.append(condition.getColumn()).append(" = ? ");
		// paramValues.add(condition.getValue());
		// }
		return this;
	}

	/**
	 * 查询
	 * 
	 * @param isDistinct
	 *            是否添加DISTINCT关键字（查询唯一结果）
	 * @param fields
	 *            查询的字段
	 * @return 自己
	 */
	public SqlCteator select(boolean isDistinct, String... columns) {
		return select(isDistinct, Arrays.asList(columns));
	}

	/**
	 * 查询
	 * 
	 * @param isDistinct
	 *            是否添加DISTINCT关键字（查询唯一结果）
	 * @param fields
	 *            查询的字段
	 * @return 自己
	 */
	public SqlCteator select(boolean isDistinct, Collection<String> columns) {
		sql.append("SELECT ");
		if (isDistinct) {
			sql.append("DISTINCT ");
		}
		if (StrUtil.isEmpty(columns)) {
			sql.append("*");
		} else {
			sql.append(StrUtil.join(columns, StrUtil.COMMA));
		}
		from(query.getTableName());
		where();
		return this;
	}

	/**
	 * 查询（非Distinct）
	 * 
	 * @param fields
	 *            查询的字段
	 * @return 自己
	 */
	public SqlCteator select(String... columns) {
		return select(false, columns);
	}

	/**
	 * 查询（非Distinct）
	 * 
	 * @param fields
	 *            查询的字段
	 * @return 自己
	 */
	public SqlCteator select(Collection<String> columns) {
		return select(false, columns);
	}

	/**
	 * 添加 from语句
	 * 
	 * @param tableNames
	 *            表名列表（多个表名用于多表查询）
	 * @return 自己
	 */
	public SqlCteator from(String... tableNames) {
		if (StrUtil.isEmpty(tableNames)) {
			throw new RuntimeException("Table name is blank in table names !");
		}
		sql.append(" FROM ").append(StrUtil.join(tableNames, StrUtil.COMMA));
		return this;
	}

	/**
	 * 添加Where语句<br>
	 * 只支持单一的逻辑运算符（例如多个条件之间）
	 * 
	 * @param logicalOperator
	 *            逻辑运算符
	 * @param conditions
	 *            条件
	 * @return 自己
	 */
	public SqlCteator where() {
		List<Condition> conditions = query.getWhereConditions();
		if (StrUtil.isNotEmpty(conditions)) {
			sql.append(" WHERE ").append(buildCondition(conditions));
		}
		return this;
	}

	/**
	 * 添加Where语句<br>
	 * 只支持单一的逻辑运算符（例如多个条件之间）
	 * 
	 * @param logicalOperator
	 *            逻辑运算符
	 * @param conditions
	 *            条件
	 * @return 自己
	 */
	public SqlCteator where(LogicalOperator logicalOperator, Condition... conditions) {
		if (StrUtil.isNotEmpty(conditions)) {
			sql.append(" WHERE ").append(buildCondition(logicalOperator, conditions));
		}
		return this;
	}

	/**
	 * 分组
	 * 
	 * @param fields
	 *            字段
	 * @return 自己
	 */
	public SqlCteator groupBy(String... columns) {
		if (StrUtil.isNotEmpty(columns)) {
			sql.append(" GROUP BY ").append(StrUtil.join(columns, StrUtil.COMMA));
		}
		return this;
	}

	/**
	 * 添加Having语句
	 * 
	 * @param logicalOperator
	 *            逻辑运算符
	 * @param conditions
	 *            条件
	 * @return 自己
	 */
	public SqlCteator having(LogicalOperator logicalOperator, Condition... conditions) {
		if (StrUtil.isNotEmpty(conditions)) {
			sql.append(" HAVING ").append(buildCondition(logicalOperator, conditions));
		}
		return this;
	}

	/**
	 * 排序
	 * 
	 * @param order
	 *            排序方式（升序还是降序）
	 * @param fields
	 *            按照哪个字段排序
	 * @return 自己
	 */
	public SqlCteator orderBy(Order order, String... columns) {
		if (StrUtil.isNotEmpty(columns)) {
			sql.append(" ORDER BY ").append(StrUtil.join(columns, StrUtil.COMMA)).append(StrUtil.SPACE)
					.append(null == order ? StrUtil.EMPTY : order);
		}
		return this;
	}

	/**
	 * 多表关联
	 * 
	 * @param tableName
	 *            被关联的表名
	 * @param join
	 *            内联方式
	 * @return 自己
	 */
	public SqlCteator join(String tableName, Join join) {
		if (StrUtil.isEmpty(tableName)) {
			throw new RuntimeException("Table name is blank !");
		}
		if (null != join) {
			sql.append(StrUtil.SPACE).append(join);
		}
		sql.append(" JOIN ").append(tableName);
		return this;
	}

	/**
	 * 配合JOIN的 ON语句，多表关联的条件语句<br>
	 * 只支持单一的逻辑运算符（例如多个条件之间）
	 * 
	 * @param logicalOperator
	 *            逻辑运算符
	 * @param conditions
	 *            条件
	 * @return 自己
	 */
	public SqlCteator on(LogicalOperator logicalOperator, Condition... conditions) {
		if (StrUtil.isNotEmpty(conditions)) {
			sql.append(" ON ").append(buildCondition(logicalOperator, conditions));
		}
		return this;
	}

	/**
	 * 追加SQL其它部分
	 * 
	 * @param sqlPart
	 *            SQL其它部分
	 * @return 自己
	 */
	public SqlCteator append(Object sqlPart) {
		if (null != sqlPart) {
			this.sql.append(sqlPart);
		}
		return this;
	}

	// --------------------------------------------------------------- Builder end

	/**
	 * 获得占位符对应的值列表<br>
	 * 
	 * @return 占位符对应的值列表
	 */
	public List<Object> getParamValues() {
		return paramValues;
	}

	/**
	 * 获得占位符对应的值列表<br>
	 * 
	 * @return 占位符对应的值列表
	 */
	public Object[] getParamValueArray() {
		return paramValues.toArray(new Object[paramValues.size()]);
	}

	/**
	 * 构建
	 * 
	 * @return 构建好的SQL语句
	 */
	public String build() {
		return this.sql.toString().trim();
	}

	@Override
	public String toString() {
		return this.build();
	}

	// --------------------------------------------------------------- private method start
	/**
	 * 构建组合条件
	 * 
	 * @param logicalOperator
	 *            逻辑运算符
	 * @param conditions
	 *            条件对象
	 * @return 构建后的SQL语句条件部分
	 */

	private String buildCondition(List<Condition> conditions) {
		final StringBuilder conditionStr = new StringBuilder("1=1 ");
		for (Condition condition : conditions) {
			conditionStr.append(StrUtil.SPACE).append(condition.getPrefix()).append(StrUtil.SPACE);
			if (condition instanceof Or) {
				Or or = (Or) condition;
				// 添加条件表达式
				//conditionStr.append(or.getCondition());
				paramValues.add(condition.getValue());
			} else {
				// 添加条件表达式
				conditionStr.append(condition.getColumn()).append(StrUtil.SPACE).append(condition.getOperator());
				if (condition.isPlaceHolder()) {
					// 使用条件表达式占位符
					conditionStr.append(" ?");
					paramValues.add(condition.getValue());
				} else {
					// 直接使用条件值
					conditionStr.append(condition.getValue());
				}
			}
		}
		return conditionStr.toString();
	}

	/**
	 * 构建组合条件
	 * 
	 * @param logicalOperator
	 *            逻辑运算符
	 * @param conditions
	 *            条件对象
	 * @return 构建后的SQL语句条件部分
	 */
	private String buildCondition(LogicalOperator logicalOperator, Condition... conditions) {
		if (StrUtil.isEmpty(conditions)) {
			return StrUtil.EMPTY;
		}
		if (null == logicalOperator) {
			logicalOperator = LogicalOperator.AND;
		}

		final StringBuilder conditionStr = new StringBuilder();
		boolean isFirst = true;
		for (Condition condition : conditions) {
			// 添加逻辑运算符
			if (isFirst) {
				isFirst = false;
			} else {
				conditionStr.append(StrUtil.SPACE).append(logicalOperator).append(StrUtil.SPACE);
			}

			// 添加条件表达式
			conditionStr.append(condition.getColumn()).append(StrUtil.SPACE).append(condition.getPrefix());
			if (condition.isPlaceHolder()) {
				// 使用条件表达式占位符
				conditionStr.append(" ?");
				paramValues.add(condition.getValue());
			} else {
				// 直接使用条件值
				conditionStr.append(condition.getValue());
			}
		}
		return conditionStr.toString();
	}
	// --------------------------------------------------------------- private method end
}
