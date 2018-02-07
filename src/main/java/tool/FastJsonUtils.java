package tool;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;

/**
 * alibaba fastjson utils
 * 
 * @see https://github.com/alibaba/fastjson/wiki
 * @author
 * @version 2015-04-07
 */
public final class FastJsonUtils {
	private static SerializeConfig mapping = new SerializeConfig();

	static {
		mapping.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
	}

	private FastJsonUtils() {
	}

	/**
	 * javaBean、list、map convert to json string
	 */
	public static String toJsonString(Object obj) {
		// return
		// JSON.toJSONString(obj,SerializerFeature.UseSingleQuotes);//使用单引号
		// return JSON.toJSONString(obj,true);//格式化数据，方便阅读
		return JSON.toJSONString(obj, mapping);
	}

	/**
	 * javaBean、list、map convert to json string
	 */
	public static String toJsonStringFilter(Object obj, SimplePropertyPreFilter filter) {
		// return
		// JSON.toJSONString(obj,SerializerFeature.UseSingleQuotes);//使用单引号
		// return JSON.toJSONString(obj,true);//格式化数据，方便阅读

		return JSON.toJSONString(obj, filter);
	}

	/**
	 * json string convert to javaBean、map
	 */
	public static <T> T jsonToObject(String jsonStr, Class<T> clazz) {
		return JSON.parseObject(jsonStr, clazz);
	}

	/**
	 * json array string convert to list with javaBean
	 */
	public static <T> List<T> jsonToList(String jsonArrayStr, Class<T> clazz) {
		return JSON.parseArray(jsonArrayStr, clazz);
	}

	/**
	 * json string convert to map
	 */
	@SuppressWarnings("unchecked")
	public static <T> Map<String, Object> jsonToMap(String jsonStr) {
		return jsonToObject(jsonStr, Map.class);
	}

	/**
	 * json string convert to map with javaBean
	 */
	public static <T> Map<String, T> json2map(String jsonStr, Class<T> clazz) {
		Map<String, T> map = JSON.parseObject(jsonStr, new TypeReference<Map<String, T>>() {
		});
		for (Entry<String, T> entry : map.entrySet()) {
			JSONObject obj = (JSONObject) entry.getValue();
			map.put(entry.getKey(), JSONObject.toJavaObject(obj, clazz));
		}
		return map;
	}
}