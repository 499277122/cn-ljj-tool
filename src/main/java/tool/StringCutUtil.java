package tool;

public class StringCutUtil {
	/**
	 * 截取指定长度字符串
	 * @param s
	 * @param length
	 * @return
	 */
	public static String cutString(String s,int maxLength,int minLength){
		if(s.length()>maxLength){
			s = s.substring(0, maxLength-minLength);
			s = s + " cut end";
		}
		return s;
	}
}
