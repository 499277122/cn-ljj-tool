package tool;

import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;

/**
 * 读取propertie工具类
 */
public class PropertiesUtil {

	private static final Logger log = Logger.getLogger(PropertiesUtil.class);

	/** 读取 properties 类型的对象 */
	private static Properties properties = null;

	/** 初始化 properties 成员 */
	static {
		properties = new Properties();
	}

	private PropertiesUtil() {
	}

	/**
	 * 读取各类 properties 类型配置文件信息
	 * 
	 * @param file
	 *            配置文件名称
	 * @param info
	 *            读取的配置信息条件
	 * @return 读取的配置信息
	 * @throws Exception
	 * @date : 2012-7-22
	 * @author : HHB
	 */
	public static String readPropertiesTools(String file, String info) {
		if (file == null) {
			log.error("读取配置文件时，配置文件名称为空");
		}
		if (info == null) {
			log.error("读取配置文件时，被读取信息ID为空");
		}

		/* 如果对象为空构造对象 */
		if (properties == null) {
			properties = new Properties();
		}

		try {
			/* 读取配置文件 */
			properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(file));
		} catch (IOException e) {
			log.error("读取配置文件键值时捕获IO异常，配置文件为" + file + ",异常为" + ExceptionUtils.getStackTrace(e));
		}

		/* 获得数据条件 */
		return properties.getProperty(info.trim());
	}

	/**
	 * 读取各类 properties 类型配置文件的所有键值
	 * 
	 * @param file
	 *            配置文件名称
	 * @return 键值列表
	 * @throws Exception
	 * @date : 2012-7-21
	 * @author : jy
	 */
	public static Set<Object> readPropertiesKeys(String file) {
		/* 判定入参的合法性 */
		if (file == null) {
			log.error("读取配置文件时，配置文件名称为空!");
		}

		/* 如果对象为空构造对象 */
		if (properties == null) {
			properties = new Properties();
		}

		/* 读取配置文件 */
		try {
			properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(file));
		} catch (IOException e) {
			log.error("读取配置文件键值时捕获IO异常，配置文件为" + file + ",异常为" + ExceptionUtils.getStackTrace(e));
		}

		/* 获得键值列表 */
		return properties.keySet();
	}
}
