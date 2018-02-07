package tool;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 容器之间互相转换
 * 
 * @author yi.wang
 * @date 2017年1月4日
 */
public final class ConvertorCollection {

	private ConvertorCollection() {
	}

	/**
	 * Map 的键转化为list
	 * 
	 * @param map
	 * @return
	 */
	public static List<?> mapKey2List(Map<?, ?> map) {
		return new ArrayList<>(map.keySet());
	}

	/**
	 * Map 的值转化为list
	 * 
	 * @param map
	 * @return
	 */
	public static List<?> mapValue2List(Map<?, ?> map) {
		return new ArrayList<>(map.values());
	}

	/**
	 * Map 的键转化为Set
	 * 
	 * @param map
	 * @return
	 */
	public static Set<?> mapKey2Set(Map<?, ?> map) {
		return map.keySet();
	}

	/**
	 * Map 的值转化为Set
	 * 
	 * @param map
	 * @return
	 */
	public static Set<?> mapValue2Set(Map<?, ?> map) {
		return new HashSet<>(map.values());
	}

	/**
	 * List 转换Set
	 * 
	 * @param list
	 * @return
	 */
	public static Set<?> list2Set(List<?> list) {
		return new HashSet<>(list);
	}

	/**
	 * Set 转换list
	 * 
	 * @param set
	 * @return
	 */
	public static List<?> set2List(Set<?> set) {
		return new ArrayList<>(set);
	}
}
