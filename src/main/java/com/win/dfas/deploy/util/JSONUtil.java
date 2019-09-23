package com.win.dfas.deploy.util;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

/**
 * JSON转换通用类
 *
 */
public class JSONUtil {

	private static ObjectMapper mapper;
	private static List<String> clazzList = new ArrayList<>();

	/**
	 * 初始化
	 */
	private static void before() {
		if (mapper == null) {
			mapper = new ObjectMapper();
			setMapper();
			//scan();
		}
	}

	/**
	 * 设置相关属性
	 */
	public static void setMapper() {
		// 有属性不能映射的时候不报错
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		// 设置全局的时间转化
		SimpleDateFormat smt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		mapper.setDateFormat(smt);
		// 解决时区差8小时问题
		mapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		// 启用包含unquotes控制字符（ASCII代码32以下，包括选项卡，换行）;
		mapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
		// 为null的属性值不映射
		mapper.setSerializationInclusion(Include.NON_NULL);
	}

	/**
	 * 获取mapper
	 * @return
	 */
	public static ObjectMapper getMapper() {
		before();
		return mapper;
	}

	/**
	 * 转换为List对象
	 * @param jsonString
	 * @param beanClass
	 * @return
	 */
	public static List<?> toList(String jsonString, Class<?> beanClass) {
		before();
		JavaType jt = mapper.getTypeFactory().constructParametricType(ArrayList.class, beanClass);
		try {
			return (List<?>) mapper.readValue(jsonString, jt);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 转换为字符串
	 * @param obj
	 * @return
	 */
	public static String toJson(Object obj) {
		try {
			before();
			return mapper.writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 字符串转换为JAVA对象
	 * @param jsonString
	 * @param beanClass
	 * @return
	 */
	public static <T> T toObject(String jsonString, Class<?> beanClass) {
		try {
			before();
			Object obj = mapper.readValue(jsonString, beanClass);
			return (T) obj;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String toString(Object o, boolean indent) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
		mapper.setSerializationInclusion(Include.NON_NULL);
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		return mapper.writeValueAsString(o);
	}
}
