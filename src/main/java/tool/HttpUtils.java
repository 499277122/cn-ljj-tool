package tool;

import com.google.common.collect.Maps;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import javax.net.ssl.SSLContext;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用于http请求的工具类
 */
@SuppressWarnings("deprecation")
public final class HttpUtils {

	public static int CONNECT_SOKET_TIME_OUT_LONG = 14000;
	public static int CONNECT_TIME_OUT_LONG = 14000;
	public static int VERIFICATION_CODE_DEBUG = 1;

	// 禁止实例化和继承
	private HttpUtils() {
	}

	private static final Logger log = Logger.getLogger(HttpUtils.class);

	public static String httpPostRequest(String url, String params) {
		return httpRequest(false, "POST", params, url, 0, 0);
	}

	public static String httpPostRequest(String url) {
		return httpRequest(false, "POST", null, url, 0, 0);
	}

	public static String httpPostRequest(String url, String params, HashMap<String, String> headers) {
		return httpRequest(false, "POST", params, url, 0, 0, headers);
	}

	public static String httpPostRequest(String url, String params, int socketTimeout, int connectTimeout) {
		return httpRequest(false, "POST", params, url, socketTimeout, connectTimeout);
	}

	public static String httpPostOrderRequest(String url, String params, int socketTimeout, int connectTimeout) {
		return httpRequest(false, "httpPostOrderRequest", params, url, socketTimeout, connectTimeout);
	}

	public static String httpPostNotJsonRequest(String url, String params) {
		return httpRequest(false, "POSTNOTJSON", params, url, 0, 0);
	}

	public static String httpPostNoCharsetRequest(String url, String params, int socketTimeout, int connectTimeout) {
		return httpRequest(false, "POSTNOCHARSET", params, url, socketTimeout, connectTimeout);
	}

	public static String httpGetRequest(String url) {
		return httpRequest(false, "GET", null, url, 0, 0);
	}

	public static String httpGetRequest(String url, HashMap<String, String> headers) {
		return httpRequest(false, "GET", null, url, 0, 0, headers);
	}

	public static String httpGetRequestWithTime(String url, int socketTimeout, int connectTimeout) {
		return httpRequest(false, "GET", null, url, socketTimeout, connectTimeout);
	}

	public static String httpGetRequest(String url, Map<String, String> params) {
		String requestUrl = getUrl(url, params);
		return httpGetRequest(requestUrl);
	}

	public static String httpGetRequestWithTime(String url, Map<String, String> params, int socketTimeout, int connectTimeout) {
		String requestUrl = getUrl(url, params);
		return httpGetRequestWithTime(requestUrl, socketTimeout, connectTimeout);
	}

	public static String httpsPostRequest(String url, String params) {
		return httpRequest(true, "POST", params, url, 0, 0);
	}

	public static String httpsGetRequest(String url) {
		return httpRequest(true, "GET", null, url, 0, 0);
	}

	public static String httpsGetRequest(String url, Map<String, String> params) {
		String requestUrl = getUrl(url, params);
		return httpsGetRequest(requestUrl);
	}
	public static Map<String, Object> httpPostRequestMulResParameters(String url, String params, HashMap<String, String> headers) {
		return httpRequestMultiRepParams(false, "POST", params, url, 0, 0, headers);
	}

	private static String httpRequest(Boolean isHttps, String method, String params, String strUrl, int socketTimeout, int connectTimeout) {
		try {
			if (socketTimeout == 0) {
				socketTimeout = CONNECT_SOKET_TIME_OUT_LONG;
			}
			if (connectTimeout == 0) {
				connectTimeout = CONNECT_TIME_OUT_LONG;
			}
			CloseableHttpClient client = getClient(isHttps);
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout).build();
			CloseableHttpResponse response = null;
			if ("GET".equals(method)) {
				HttpGet get = null;
				get = new HttpGet(strUrl);

				get.setConfig(requestConfig);
				get.setHeader("Accept", "application/json");
				get.setHeader("Content-type", "application/json;charset=utf-8");
				// get.setHeader("Authorization", "k21313kasdfdssaassss");
				response = client.execute(get);
				if (VERIFICATION_CODE_DEBUG == 1) {
					log.error("提交getUrl:\n" + strUrl);
				}
			} else {
				HttpPost post = new HttpPost(strUrl);
				post.setConfig(requestConfig);
				HttpEntity postEntity = null;

				if ("POSTNOTJSON".equals(method)) {// 非Json格式的参数
					List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
					String[] paramArray = params.split("&");
					for (String param : paramArray) {
						String[] temp = param.split("=");
						nvps.add(new BasicNameValuePair(temp[0], temp[1]));
					}
					postEntity = new UrlEncodedFormEntity(nvps, "utf-8");
				} else if ("httpPostOrderRequest".equals(method)) {
					postEntity = new StringEntity(params, ContentType.APPLICATION_JSON);

				} else {
					post.setHeader("Accept", "application/json");
					if ("POSTNOCHARSET".equals(method)) {
						post.setHeader("Content-type", "application/json");
					} else {
						post.setHeader("Content-type", "application/json;charset=utf-8");
					}
					post.setHeader("Authorization", "k21313kasdfdssaassss");
					postEntity = new StringEntity(params, ContentType.APPLICATION_JSON);
				}
				// 测试环境下，打印url
				if (VERIFICATION_CODE_DEBUG == 1) {
					byte[] b = new byte[(int) postEntity.getContentLength()];
					postEntity.getContent().read(b);
					log.error("postUrl:\n" + strUrl + "\n" + new String(b, 0, (int) postEntity.getContentLength()));
				}
				post.setEntity(postEntity);
				response = client.execute(post);
			}
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String result = EntityUtils.toString(entity, "UTF-8");
					if (VERIFICATION_CODE_DEBUG == 1) {
						log.error("请求接口结果:\n" + result);
					}
					return result;
				} else {

				}
			} finally {
				response.close();
				client.close();
			}
		} catch (Exception e) {
			log.error("Method:" + method + " isHttps: " + isHttps + " params: " + params + " url: " + strUrl + " request failed", e);
		}
		return null;
	}

	/**
	 * @param isHttps
	 * @param method
	 * @param params
	 * @param strUrl
	 * @param socketTimeout
	 * @param connectTimeout
	 * @param headers
	 * @return 添加设置header 的设置
	 */
	@SuppressWarnings({ "rawtypes" })
	private static String httpRequest(Boolean isHttps, String method, String params, String strUrl, int socketTimeout, int connectTimeout,
			HashMap<String, String> headers) {
		try {
			if (socketTimeout == 0) {
				socketTimeout = CONNECT_SOKET_TIME_OUT_LONG;
			}
			if (connectTimeout == 0) {
				connectTimeout = CONNECT_TIME_OUT_LONG;
			}
			CloseableHttpClient client = getClient(isHttps);
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout).build();
			CloseableHttpResponse response = null;
			if ("GET".equals(method)) {
				HttpGet get = null;
				get = new HttpGet(strUrl);
				get.setConfig(requestConfig);
				get.setHeader("Accept", "application/json");
				get.setHeader("Content-type", "application/json;charset=utf-8");
				if (headers != null && headers.entrySet().size() > 0) {
					Iterator iter = headers.entrySet().iterator();
					while (iter.hasNext()) {
						Entry entry = (Entry) iter.next();
						get.setHeader((String) entry.getKey(), (String) entry.getValue());
					}
				}
				response = client.execute(get);
				// 测试环境下，打印url
				if (VERIFICATION_CODE_DEBUG == 1) {
					log.error("提交getUrl:\n" + strUrl);
				}
			} else {
				HttpPost post = new HttpPost(strUrl);
				post.setConfig(requestConfig);
				HttpEntity postEntity = null;

				if ("POSTNOTJSON".equals(method)) {// 非Json格式的参数
					List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
					String[] paramArray = params.split("&");
					for (String param : paramArray) {
						String[] temp = param.split("=");
						nvps.add(new BasicNameValuePair(temp[0], temp[1]));
					}
					postEntity = new UrlEncodedFormEntity(nvps, "utf-8");
				} else {
					post.setHeader("Accept", "application/json");
					if ("POSTNOCHARSET".equals(method)) {
						post.setHeader("Content-type", "application/json");
					} else {
						post.setHeader("Content-type", "application/json;charset=utf-8");
					}
					postEntity = new StringEntity(params, ContentType.APPLICATION_JSON);
				}
				if (headers != null && headers.entrySet().size() > 0) {
					Iterator iter = headers.entrySet().iterator();
					while (iter.hasNext()) {
						Entry entry = (Entry) iter.next();
						post.setHeader((String) entry.getKey(), (String) entry.getValue());
					}
				}
				// 测试环境下，打印url
				if (VERIFICATION_CODE_DEBUG == 1) {
					byte[] b = new byte[(int) postEntity.getContentLength()];
					postEntity.getContent().read(b);
					log.error("postUrl:\n" + strUrl + "\n" + new String(b, 0, (int) postEntity.getContentLength()));
				}
				post.setEntity(postEntity);
				response = client.execute(post);
			}
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String result = EntityUtils.toString(entity, "UTF-8");
					if (VERIFICATION_CODE_DEBUG == 1) {
						log.error("请求接口结果:\n" + result);
					}
					return result;
				} else {

				}
			} finally {
				response.close();
				client.close();
			}
		} catch (Exception e) {
			log.error("Method:" + method + " isHttps: " + isHttps + " params: " + params + " url: " + strUrl + " request failed", e);
		}
		return null;
	}

	/**
	 * @param isHttps
	 * @param method
	 * @param params
	 * @param strUrl
	 * @param socketTimeout
	 * @param connectTimeout
	 * @param headers
	 * @return 添加设置header 的设置
	 */
	@SuppressWarnings({ "rawtypes" })
	private static Map<String, Object> httpRequestMultiRepParams(Boolean isHttps, String method, String params, String strUrl, int socketTimeout, int connectTimeout,
									  HashMap<String, String> headers) {
		HashMap<String, Object> result = Maps.newHashMap();
		try {
			if (socketTimeout == 0) {
				socketTimeout = CONNECT_SOKET_TIME_OUT_LONG;
			}
			if (connectTimeout == 0) {
				connectTimeout = CONNECT_TIME_OUT_LONG;
			}
			CloseableHttpClient client = getClient(isHttps);
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout).build();
			CloseableHttpResponse response = null;
			if ("GET".equals(method)) {
				HttpGet get = null;
				get = new HttpGet(strUrl);
				get.setConfig(requestConfig);
				get.setHeader("Accept", "application/json");
				get.setHeader("Content-type", "application/json;charset=utf-8");
				if (headers != null && headers.entrySet().size() > 0) {
					Iterator iter = headers.entrySet().iterator();
					while (iter.hasNext()) {
						Entry entry = (Entry) iter.next();
						get.setHeader((String) entry.getKey(), (String) entry.getValue());
					}
				}
				response = client.execute(get);
				// 测试环境下，打印url
				if (VERIFICATION_CODE_DEBUG == 1) {
					log.error("提交getUrl:\n" + strUrl);
				}
			} else {
				HttpPost post = new HttpPost(strUrl);
				post.setConfig(requestConfig);
				HttpEntity postEntity = null;

				if ("POSTNOTJSON".equals(method)) {// 非Json格式的参数
					List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
					String[] paramArray = params.split("&");
					for (String param : paramArray) {
						String[] temp = param.split("=");
						nvps.add(new BasicNameValuePair(temp[0], temp[1]));
					}
					postEntity = new UrlEncodedFormEntity(nvps, "utf-8");
				} else {
					post.setHeader("Accept", "application/json");
					if ("POSTNOCHARSET".equals(method)) {
						post.setHeader("Content-type", "application/json");
					} else {
						post.setHeader("Content-type", "application/json;charset=utf-8");
					}
					postEntity = new StringEntity(params, ContentType.APPLICATION_JSON);
				}
				if (headers != null && headers.entrySet().size() > 0) {
					Iterator iter = headers.entrySet().iterator();
					while (iter.hasNext()) {
						Entry entry = (Entry) iter.next();
						post.setHeader((String) entry.getKey(), (String) entry.getValue());
					}
				}
				// 测试环境下，打印url
				if (VERIFICATION_CODE_DEBUG == 1) {
					byte[] b = new byte[(int) postEntity.getContentLength()];
					postEntity.getContent().read(b);
					log.error("postUrl:\n" + strUrl + "\n" + new String(b, 0, (int) postEntity.getContentLength()));
				}
				post.setEntity(postEntity);
				response = client.execute(post);
			}
			try {
				HttpEntity entity = response.getEntity();
				int statusCode = response.getStatusLine().getStatusCode();
				log.error("statusCode:\n" + statusCode);
				if (entity != null) {
					String data = EntityUtils.toString(entity, "UTF-8");
					if (VERIFICATION_CODE_DEBUG == 1) {
						log.error("请求接口结果:\n" + data);
						result.put("code",statusCode);
						result.put("data",data);
					}
					return result;
				} else {

				}
			} finally {
				response.close();
				client.close();
			}
		} catch (Exception e) {
			log.error("Method:" + method + " isHttps: " + isHttps + " params: " + params + " url: " + strUrl + " request failed", e);
		}
		return null;
	}

	private static CloseableHttpClient getClient(boolean isHttps) {
		if (isHttps) {
			return Objects.requireNonNull(createSSLInsecureClient());
		}
		return HttpClients.createDefault();
	}

	private static CloseableHttpClient createSSLInsecureClient() {
		try {
			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
				// 信任所有证书
				public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					return true;
				}
			}).build();
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
			return HttpClients.custom().setSSLSocketFactory(sslsf).build();
		} catch (Exception e) {
			log.error("get ssl client failed", e);
		}
		return null;
	}

	private static String getUrl(String url, Map<String, String> params) {
		StringBuilder requestUrl = new StringBuilder(url);
		int i = 0;
		for (Entry<String, String> entry : params.entrySet()) {
			if (entry.getValue() == null || "".equals(entry.getValue())) {
				continue;
			}
			if (i == 0) {
				if (url.matches(".*\\?.*")) {
					requestUrl.append("&");
				} else {
					requestUrl.append("?");
				}
				requestUrl.append(entry.getKey()).append("=").append(entry.getValue());
			} else {
				requestUrl.append("&").append(entry.getKey()).append("=").append(entry.getValue());
			}
			i++;
		}
		// return URLEncoder.encode(requestUrl.toString(),"UTF-8");
		return requestUrl.toString();
	}

	/**
	 * 验证网址Url
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean isUrl(String str) {
		String regex = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";
		return match(regex, str);
	}

	/**
	 * @param regex 正则表达式字符串
	 * @param str 要匹配的字符串
	 * @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
	 */
	private static boolean match(String regex, String str) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}

	public static String postUrlencoded(String url, String data) throws ClientProtocolException, IOException {
		CloseableHttpClient client = getClient(false);
		InputStreamEntity reqEntity = new InputStreamEntity(new ByteArrayInputStream(data.getBytes()));
		reqEntity.setContentType("application/x-www-form-urlencoded");
		reqEntity.setContentEncoding("utf-8");
		HttpPost post = new HttpPost(url);
		post.setEntity(reqEntity);
		HttpResponse response = client.execute(post);
		HttpEntity resEntity = response.getEntity();
		String res = EntityUtils.toString(resEntity);
		return res;
	}
	
	public static String postUrlencoded(String url, Map<String, String> paraMaps) throws ClientProtocolException, IOException {
        List<NameValuePair> params = new ArrayList<NameValuePair>();        
        for (Entry<String,String> entry: paraMaps.entrySet()) {
            params.add(new BasicNameValuePair(entry.getKey(),entry.getValue()));
        }
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
		CloseableHttpClient client = getClient(false);
		HttpPost post = new HttpPost(url);
		post.setHeader("Content-Type", "application/x-www-form-urlencoded");
		post.setEntity(entity);
		HttpResponse response = client.execute(post);
		HttpEntity resEntity = response.getEntity();
		String res = EntityUtils.toString(resEntity);
		return res;
	}
}
