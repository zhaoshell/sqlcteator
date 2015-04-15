package com.github.sqlcteator;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TestSqlCteator {

	@Test
	public void main() {
		List<String> or = new ArrayList<String>();
		or.add("name");
		// or.add("sex");
		Query query = Query.table("nihao");
		query.eq("name", "yangjian");
		query.eq("password", "yj");
		query.eq("id", "1");
		String sql = query.select();
		System.out.println(sql);
		sql = query.insert();
		System.out.println(sql);
		// query.or(or, "lilei");
		// SqlCteator sqlCteator = SqlCteator.create(query);
		// System.out.println(sqlCteator.insert().toString());
	}
}
