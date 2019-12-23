package tech.mhuang.ext.elasticsearch.server.annoation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * ES注解类
 *
 * @author mhuang
 * @since 1.0.0
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ESTable {

	/**
	 * 索引
 	 * @return String
	 */
	String index();

	/**
	 * 分片数
	 * @return short
	 */
	short shards() default 5;

	/**
	 * 副本
	 * @return short
	 */
	short replicas() default 1;
}
