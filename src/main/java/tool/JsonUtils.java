package tool;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.type.JavaType;

/**
 * json处理工具
 */
public final class JsonUtils {
	private static final Logger log = Logger.getLogger(JsonUtils.class);

	private JsonUtils() {
	}

	/**
	 * ObjectMapper 提供单例供全局使用
	 */
	private static class SingletonHolder {
		private static ObjectMapper mapper;
		static {
			mapper = new ObjectMapper();
			//设置日期对象的输出格式
			mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE));
			//设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
			mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			//禁止使用int代表Enum的order()来反序列化Enum
			mapper.configure(DeserializationConfig.Feature.FAIL_ON_NUMBERS_FOR_ENUMS, true);
			//为空不参加序列号
			//mapper.setSerializationInclusion(Inclusion.NON_NULL);  
			//设置  null 自动转换成 ""
			/*mapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
				@Override
				public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
					jgen.writeString("");
				}
			});*/
		}
	}

	private static ObjectMapper getMapper() {
		return SingletonHolder.mapper;
	}

	/**
	 * 将对象转换为json字符串
	 * @param pojo
	 * @return
	 * @throws IOException
	 */
	public static String toJsonString(Object pojo) {
		if (pojo == null) {
			return null;
		}
		try {
			return getMapper().writeValueAsString(pojo);
		} catch (IOException e) {
			log.error("pojo parse  json string error", e);
		}
		return null;
	}

	/**
	 * 将字符串转换为json对象
	 * @param input
	 * @return
	 * @throws IOException
	 */
	public static JsonNode parseJson(String input) {
		if (input == null) {
			return null;
		}
		try {
			return getMapper().readTree(input);
		} catch (IOException e) {
			log.error("json processing error,input: " + input, e);
		}
		return null;
	}

	/**
	 * 将inputStream 转换为json对象
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static JsonNode getJsonNodefromStream(InputStream in) {
		try {
			return getMapper().readTree(in);
		} catch (JsonProcessingException e) {
			log.error("json processing error", e);
		} catch (IOException e) {
			log.error("read file error", e);
		}
		return null;
	}

	/**
	 * 将json字符串转换为java对象，只支持返回简单对象（非集合类型）
	 * @param jsonString
	 * @param valueType
	 * @return
	 * @throws IOException
	 */
	public static <T> T jsonToObject(String jsonString, Class<T> valueType) {
		if (StringUtils.isNotBlank(jsonString)) {
			try {
				return getMapper().readValue(jsonString, valueType);
			} catch (IOException e) {
				log.error("read readValue error", e);
				return null;
			}
		}
		return null;
	}

	/**
	 * 将json字符串转换为java对象，只支持返回简单对象（非集合类型）
	 * @param jsonString
	 * @param valueType
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public static HashMap<String, String> jsonToObjectAsHashMap(String jsonString) {
		HashMap<String, String> paramMap = null;
		if (StringUtils.isNotBlank(jsonString)) {
			try {
				paramMap = getMapper().readValue(jsonString, HashMap.class);
			} catch (Exception e) {
				log.error("read jsonToObjectAsHashMap error", e);
			}
			//去除通用的
			paramMap.remove("clientInfo");
			paramMap.remove("authority");
			return paramMap;
		}
		return paramMap;
	}

	/**
	 * 将json字符串转为集合类型 List、Map等
	 * @param jsonStr json字符串
	 * @param collectionClass 集合类型
	 * @param elementClasses 泛型类型
	 */
	public static <T> T jsonToObject(String jsonStr, Class<?> collectionClass, Class<?>... elementClasses) {
		if (!StringUtils.isNotBlank(jsonStr)) {
			return null;
		}
		JavaType javaType = getMapper().getTypeFactory().constructParametricType(collectionClass, elementClasses);
		try {
			return getMapper().readValue(jsonStr, javaType);
		} catch (IOException e) {
			log.error("read jsonToObject error", e);
		}
		return null;
	}

	public static <T> T jsonToObject(JsonNode node, Class<?> collectionClass, Class<?>... elementClasses) {
		if (node == null) {
			return null;
		}
		JavaType javaType = getMapper().getTypeFactory().constructParametricType(collectionClass, elementClasses);
		try {
			return getMapper().readValue(node, javaType);
		} catch (IOException e) {
			log.error("read jsonToObject error", e);
		}
		return null;
	}
	

	public static <T> T jacksonToCollection(String src, Class<?> collectionClass, Class<?>... valueType) throws Exception {
		JavaType javaType = getMapper().getTypeFactory().constructParametricType(collectionClass, valueType);
		return getMapper().readValue(src, javaType);
	}

	/**
	 * 创建一个空的json对象
	 * @return
	 */
	public static ObjectNode createObjectNode() {
		return getMapper().createObjectNode();
	}

	/**
	 * 创建一个空的json数组对象
	 * @return
	 */
	public static ArrayNode createArrayNode() {
		return getMapper().createArrayNode();
	}


}
