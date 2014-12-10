
/**
 * Copyright(c) 2000-2013 HC360.COM, All Rights Reserved.
 * Project: ework 
 * Author: Gao xingkun
 * Createdate: 下午3:48:40
 * Version: 1.0
 *
 */

package com.web.maven.common.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.UndeclaredThrowableException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.ConvertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * 反射工具
 * @project ework
 * @author Gao xingkun
 * @version 1.0
 * @date 2013-4-24 下午3:48:40   
 */
public class ReflectionUtils {
	static Logger logger = LoggerFactory.getLogger(ReflectionUtils.class);

	private static Object operate(Object obj, String fieldName,Object fieldVal, String type) {
		Object ret = null;
		try {
			// 获得对象类型
			Class<? extends Object> classType = obj.getClass();
			// 获得对象的所有属性
			Field fields[] = classType.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				if (field.getName().equals(fieldName)) {
					// 获得和属性对应的getXXX()方法的名字
					String firstLetter = fieldName.substring(0, 1).toUpperCase(); 
					if ("set".equals(type)) {
						// 获得和属性对应的getXXX()方法
						String setMethodName = "set" + firstLetter + fieldName.substring(1); 
						// 调用原对象的getXXX()方法
						Method setMethod = classType.getMethod(setMethodName,new Class[] { field.getType() }); 
						ret = setMethod.invoke(obj, new Object[] { fieldVal });
					}
					if ("get".equals(type)) {
						// 获得和属性对应的setXXX()方法的名字
						String getMethodName = "get" + firstLetter + fieldName.substring(1); 
						Method getMethod = classType.getMethod(getMethodName,new Class[] {});
						ret = getMethod.invoke(obj, new Object[] {});
					}
					return ret;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	public static Object getVal(Object obj, String fieldName) {
		return operate(obj, fieldName, null, "get");
	}

	public static void setVal(Object obj, String fieldName, Object fieldVal) {
		operate(obj, fieldName, fieldVal, "set");
	}

	/**
	 * 循环向上转型, 获取对象的 DeclaredMethod
	 * 
	 * @param object
	 * @param methodName
	 * @param parameterTypes
	 * @return
	 */
	private static Method getDeclaredMethod(Object object, String methodName,Class<?>[] parameterTypes) {
		for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				// superClass.getMethod(methodName, parameterTypes);
				return superClass.getDeclaredMethod(methodName, parameterTypes);
			} catch (NoSuchMethodException e) {
				// Method 不在当前类定义, 继续向上转型
			}
		}

		return null;
	}

	/**
	 * 使 field 变为可访问
	 * 
	 * @param field
	 */
	private static void makeAccessible(Field field) {
		if (!Modifier.isPublic(field.getModifiers())) {
			field.setAccessible(true);
		}
	}

	/**
	 * 循环向上转型, 获取对象的 DeclaredField
	 * 
	 * @param object
	 * @param filedName
	 * @return
	 */
	private static Field getDeclaredField(Object object, String filedName) {
		for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				return superClass.getDeclaredField(filedName);
			} catch (NoSuchFieldException e) {
				// Field 不在当前类定义, 继续向上转型
			}
		}
		return null;
	}

	/**
	 * 直接调用对象方法, 而忽略修饰符(private, protected)。
	 * 
	 * @param object
	 * @param methodName
	 * @param parameterTypes
	 * @param parameters
	 * @return
	 */
	public static Object invokeMethod(Object object, String methodName,Class<?>[] parameterTypes, Object[] parameters) {
		Method method = getDeclaredMethod(object, methodName, parameterTypes);

		if (method == null) {
			throw new IllegalArgumentException("Could not find method ["+ methodName + "] on target [" + object + "]");
		}

		method.setAccessible(true);

		try {
			return method.invoke(object, parameters);
		} catch (Exception ex) {
			handleReflectionException(ex);
		}
		throw new IllegalStateException("Should never get here");
	}

	/**
	 * 直接设置对象属性值, 忽略 private/protected 修饰符, 也不经过 setter
	 * 
	 * @param object
	 * @param fieldName
	 * @param value
	 */
	public static void setFieldValue(Object object, String fieldName,Object value) {
		Field field = getDeclaredField(object, fieldName);

		if (field == null)
			throw new IllegalArgumentException("Could not find field ["
					+ fieldName + "] on target [" + object + "]");

		makeAccessible(field);

		try {
			field.set(object, value);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 直接读取对象的属性值, 忽略 private/protected 修饰符, 也不经过 getter
	 * 
	 * @param object
	 * @param fieldName
	 * @return
	 */
	public static Object getFieldValue(Object object, String fieldName) {
		Field field = getDeclaredField(object, fieldName);
		if (field == null){
			return null;
		}
		

		makeAccessible(field);

		Object result = null;
		try {
			result = field.get(object);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return result;
	}
	
	/**
	 * Handle the given reflection exception. Should only be called if no
	 * checked exception is expected to be thrown by the target method.
	 * <p>Throws the underlying RuntimeException or Error in case of an
	 * InvocationTargetException with such a root cause. Throws an
	 * IllegalStateException with an appropriate message else.
	 * @param ex the reflection exception to handle
	 */
	public static void handleReflectionException(Exception ex) {
		if (ex instanceof NoSuchMethodException) {
			throw new IllegalStateException("Method not found: " + ex.getMessage());
		}
		if (ex instanceof IllegalAccessException) {
			throw new IllegalStateException("Could not access method: " + ex.getMessage());
		}
		if (ex instanceof InvocationTargetException) {
			handleInvocationTargetException((InvocationTargetException) ex);
		}
		if (ex instanceof RuntimeException) {
			throw (RuntimeException) ex;
		}
		throw new UndeclaredThrowableException(ex);
	}
	
	/**
	 * Handle the given invocation target exception. Should only be called if no
	 * checked exception is expected to be thrown by the target method.
	 * <p>Throws the underlying RuntimeException or Error in case of such a root
	 * cause. Throws an IllegalStateException else.
	 * @param ex the invocation target exception to handle
	 */
	public static void handleInvocationTargetException(InvocationTargetException ex) {
		rethrowRuntimeException(ex.getTargetException());
	}
	
	/**
	 * Rethrow the given {@link Throwable exception}, which is presumably the
	 * <em>target exception</em> of an {@link InvocationTargetException}. Should
	 * only be called if no checked exception is expected to be thrown by the
	 * target method.
	 * <p>Rethrows the underlying exception cast to an {@link RuntimeException} or
	 * {@link Error} if appropriate; otherwise, throws an
	 * {@link IllegalStateException}.
	 * @param ex the exception to rethrow
	 * @throws RuntimeException the rethrown exception
	 */
	public static void rethrowRuntimeException(Throwable ex) {
		if (ex instanceof RuntimeException) {
			throw (RuntimeException) ex;
		}
		if (ex instanceof Error) {
			throw (Error) ex;
		}
		throw new UndeclaredThrowableException(ex);
	}
	/**
	 * type=set,将Map中值赋值到对象属性中
	 * type=get,将对象属性值保存到Map中
	 * 
	 * @author chenxinwei
	 * @version 1.0
	 * @date 2013-3-9 下午03:31:05
	 * @param target
	 * @param source
	 * @param type
	 * @param caseSensitive
	 * @return Object
	 */
	public static Object operate(Object target, Map<String, Object> source, String type, boolean caseSensitive) {
		Object ret = null;
		String fieldName = null;
		Class fieldClass = null;
		try {
			
			// 获得对象类型
			Class<? extends Object> classType = target.getClass();
			// 获得对象的所有属性
			Field fields[] = classType.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				fieldName = field.getName();
				fieldClass = field.getType();
				// 获得和属性对应的setXXX()方法的名字
				String firstLetter = fieldName.substring(0, 1).toUpperCase(); 
				if ("set".equals(type)) {
					// 获得和属性对应的setXXX()方法
					String setMethodName = "set" + firstLetter + fieldName.substring(1); 
					// 调用原对象的setXXX()方法
					Method setMethod = classType.getMethod(setMethodName, new Class[] { fieldClass }); 
					Object object = source.get(fieldName);
					Object backObject = null;
					//大小写不敏感时对map中key分别进行大、小写取值
					if (!caseSensitive) {
						if (object == null) {
							object = source.get(fieldName.toUpperCase());
						}
						if (object == null) {
							object = source.get(fieldName.toLowerCase());
						}
					} 
//					BooleanConverter
					
					try{
						if (object != null) {
							
							if (!object.getClass().getName().equals(fieldClass.getName())) {
								backObject = ConvertUtils.convert(object.toString(), fieldClass);
							}
						}
					}catch (Exception e) {
						e.printStackTrace();
						logger.error("convert Error:" + fieldName + ",fieldClass:" + fieldClass!=null?fieldClass.getName():"" , e);
					}
					if (setMethod != null && backObject != null) {
						ret = setMethod.invoke(target, new Object[] { backObject });
					}else
					if (setMethod != null && object != null) {
						ret = setMethod.invoke(target, new Object[] { object });
					}
					
				}
				if ("get".equals(type)) { //需要测试get方法
					// 获得和属性对应的setXXX()方法的名字
					String getMethodName = "get" + firstLetter + fieldName.substring(1); 
					Method getMethod = classType.getMethod(getMethodName, new Class[] {});
					if (getMethod != null) {
						ret = getMethod.invoke(target, new Object[] {});
						if (ret != null) {
							source.put(fieldName, ret);
							//大小写不敏感时对map中key分别进行大、小写赋值
							if (!caseSensitive) {
								source.put(fieldName.toLowerCase(), ret);
								source.put(fieldName.toUpperCase(), ret);
							}
						}
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Reflect Error:" + fieldName , e);
		}
		return ret;
	}
	public static Object getVal(Object target, Map<String, Object> source, boolean caseSensitive) {
		if (target == null) {
			return null;
		}
		return operate(target, source, "get", caseSensitive);
	}

	/**
	 * 将Map中对应参数赋值到Object中
	 * 
	 * @author chenxinwei
	 * @version 1.0
	 * @date 2013-3-9 下午03:24:17
	 * @param target
	 * @param source 
	 * @param caseSensitive 是否为大小写敏感
	 */
	public static void setVal(Object target, Map<String, Object> source, boolean caseSensitive) {
		if (source == null) {
			return;
		}
		operate(target, source, "set", caseSensitive);
	}
	
}
