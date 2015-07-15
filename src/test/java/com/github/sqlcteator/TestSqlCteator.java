package com.github.sqlcteator;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TestSqlCteator {

	@Test
	public void main() {
		List<String> or = new ArrayList<String>();
		or.add("name");
		or.add("nickname");
		Query query = Query.table("nihao");
		query.eq("name", "yangjian");
		query.eq("password", "yj");
		query.notEq("id", "1");
		// query.or("nickname", "yang");
		query.or(or, "yang");
//		String sql = query.select();
//		System.out.println(sql);
//		sql = query.insert();
//		System.out.println(sql);
	}
}
