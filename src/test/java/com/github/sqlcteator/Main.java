package com.github.sqlcteator;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.io.Files;

public class Main {

	public static void main(String[] args) {
		try {
			List<String> readLines = Files.readLines(new File("D:/so.txt"), Charset.defaultCharset());
			List<List<String>> newReadLines = Lists.newArrayList();
			int i = 1;
			List<String> newArrayList = Lists.newArrayList();
			for (String s : readLines) {
				newArrayList.add(s);
				if (i % 100 == 0) {
					newReadLines.add(newArrayList);
					newArrayList = Lists.newArrayList();
				}
				i++;
			}
			if (newArrayList != null && newArrayList.size() > 0) {
				newReadLines.add(newArrayList);
			}
			String join = null;
			String strAppend = null;
			for (List<String> strList : newReadLines) {
				join = Joiner.on(",").join(strList);
				strAppend = strAppend(join, ",", "'");
				System.out.println(strAppend);
				System.out.println("");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 判断字符串是否为null或空字符串,在模糊查询的时候很有意义
	 */
	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0 || str.trim().length() == 0;
	}

	/**
	 * 字符串前后拼接给定字符，如：SO1506116511,SO1506117038 以字符"'"拼接结果：'SO1506116511','SO1506117038'
	 * 
	 * @param str
	 *            原字符串
	 * @param splitStr
	 *            原字符串分割字符
	 * @param appendStr
	 *            前后拼接给定字符
	 * @return String
	 * @Author 杨健/YangJian
	 * @Date 2015年6月18日 下午12:00:04
	 * @Version 1.0.0
	 */
	public static String strAppend(String str, String splitStr, String appendStr) {
		if (isEmpty(str)) {
			return null;
		}
		List<String> strList = Splitter.on(splitStr).splitToList(StringUtils.replaceChars(str, appendStr, ""));
		List<String> newStrList = Lists.newArrayList();
		for (String s : strList) {
			if (isEmpty(s)) {
				continue;
			}
			newStrList.add(StringUtils.join(appendStr, s, appendStr));
		}
		return Joiner.on(splitStr).skipNulls().join(newStrList);
	}

}
