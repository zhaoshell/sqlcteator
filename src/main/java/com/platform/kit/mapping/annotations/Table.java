package com.platform.kit.mapping.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 *@comment 针对对象的类进行标注。 <br/>
 *该注解标注在转换对象的类名上。<br/>
 *name参数，表示该成员变量对外对应的名称。不填，默认就是成员变量名；填写了，不论是作为转换对象还是被转换对象，都使用name值来进行转换匹配。<br/>
 * @Author 杨健/YangJian
 * @Date 2015年5月27日 下午5:09:56
 * @Version 1.0.0
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface Table {

	String name() default "";
	
}
