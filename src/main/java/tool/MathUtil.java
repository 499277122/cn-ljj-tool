package tool;

import java.math.BigDecimal;

/**
 * 计算工具类
 * 
 * @author yi.wang
 * @date 2017年3月14日
 */
public class MathUtil {

	private static final int DEF_DIV_SCALE = 10;

	private MathUtil() {
	}

	/**
	 * 提供精确的加法运算
	 * 
	 * @author yi.wang
	 * @param v1 被加数
	 * @param v2 加数
	 * @return double 两个参数的和
	 * @date 2017年3月14日
	 */
	public static double add(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.add(b2).doubleValue();
	}
	
	/**
	 * 提供精确的加法运算
	 * 
	 * @author yi.wang
	 * @return double 多个参数的和
	 * @date 2017年3月14日
	 */
	public static double adds(double ...v) {
		BigDecimal ret = new BigDecimal(0);
		for (double o : v) {
			ret = ret.add(new BigDecimal(Double.toString(o)));
		}
		return ret.doubleValue();
	}
	
	/**
	 * 提供精确的减法运算
	 * 
	 * @author yi.wang
	 * @param v1 被减数
	 * @param v2 减数
	 * @return double
	 * @date 2017年3月14日
	 */
	public static double sub(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.subtract(b2).doubleValue();
	}

	/**
	 * 提供精确的乘法运算
	 * 
	 * @author yi.wang
	 * @param v1 被乘数
	 * @param v2 乘数
	 * @return double
	 * @date 2017年3月14日
	 */
	public static double mul(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.multiply(b2).doubleValue();
	}

	/**
	 * 指定精度的乘法运算
	 * 
	 * @author GJ
	 * @param v1 被乘数
	 * @param v2 乘数
	 * @param s 精度
	 * @return double
	 * @date 2017年4月10日
	 */
	public static double mul(double v1, double v2, int s) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.multiply(b2).setScale(s, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入
	 * 
	 * @author yi.wang
	 * @param v1 被除数
	 * @param v2 除数
	 * @return double
	 * @date 2017年3月14日
	 */
	public static double div(double v1, double v2) {
		return div(v1, v2, DEF_DIV_SCALE);
	}

	/**
	 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入
	 * 
	 * @author yi.wang
	 * @param v1 被除数
	 * @param v2 除数
	 * @param scale 表示表示需要精确到小数点以后几位
	 * @return double 两个参数的商
	 * @date 2017年3月14日
	 */
	public static double div(double v1, double v2, int scale) {
		if (v2 == 0) {
			return 0;
		}
		if (scale < 0) {
			throw new IllegalArgumentException("精确刻度必须是正整数或零");
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 提供精确的小数位四舍五入处理
	 * 
	 * @author yi.wang
	 * @param v 需要四舍五入的数字
	 * @param scale 小数点后保留几位
	 * @return double 四舍五入后的结果
	 * @date 2017年3月14日
	 */
	public static double round(double v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("精确刻度必须是正整数或零");
		}
		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	public static void main(String[] args) {
		/*
		 * System.out.println(MathUtil.add(0.05, 0.01)); System.out.println(MathUtil.sub(1.0, 0.42));
		 * System.out.println(MathUtil.mul(4.015, 100));
		 */
		System.out.println(MathUtil.div(0.9, 3, 20));
		/*
		 * System.out.println(MathUtil.div(123.3, 100)); // System.out.println(MathUtil.round(div(1, 3), 3));
		 * System.out.println("-------------"); System.out.println(0.05 + 0.01); System.out.println(1.0 - 0.42);
		 * System.out.println(4.015 * 100); System.out.println(1.0 / 3.0); System.out.println(123.3 / 100);
		 */
	}
}