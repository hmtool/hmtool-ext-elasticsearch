package tech.mhuang.ext.elasticsearch.server.annoation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @ClassName:  EsTable   
 * @Description:Table注解   
 * @author: mhuang
 * @date:   2017年8月4日 上午11:01:13
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ESTable {

	/**
	 * 
	 * @Title: index   
	 * @Description: 索引 
	 * @return
	 * @return String
	 */
	String index();

	/**
	 * 
	 * @Title: type   
	 * @Description: 类型 
	 * @return
	 * @return String
	 */
	String type() default "";
	
	/**
	 * 
	 * @Title: shards   
	 * @Description: 索引分片数
	 * @return
	 * @return short
	 */
	short shards() default 5;

	/**
	 * 
	 * @Title: replicas   
	 * @Description: 索引副本
	 * @return
	 * @return short
	 */
	short replicas() default 1;
}
