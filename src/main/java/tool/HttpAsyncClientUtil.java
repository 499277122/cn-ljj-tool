package tool;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.log4j.Logger;

/**
 * 异步请求
 * 
 * @author yi.wang
 * @date 2016年11月25日
 */
public final class HttpAsyncClientUtil {

	private static Logger log = Logger.getLogger(HttpAsyncClientUtil.class);

	private HttpAsyncClientUtil() {
	}

	/**
	 * 获取请求
	 * 
	 * @return
	 */
	public static CloseableHttpAsyncClient getClient() {
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(60000).setConnectTimeout(60000).build();
		CloseableHttpAsyncClient httpclient = HttpAsyncClients.custom().setDefaultRequestConfig(requestConfig).build();
		httpclient.start();
		return httpclient;
	}

	/**
	 * 获取请求， 设置最大连接数100
	 * 
	 * @return
	 * @throws IOReactorException
	 */
	public static CloseableHttpAsyncClient getClientTwo() throws IOReactorException {
		ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor();
		PoolingNHttpClientConnectionManager cm = new PoolingNHttpClientConnectionManager(ioReactor);
		cm.setMaxTotal(100);
		CloseableHttpAsyncClient httpAsyncClient = HttpAsyncClients.custom().setConnectionManager(cm).build();
		httpAsyncClient.start();
		return httpAsyncClient;
	}

	/**
	 * 关闭
	 * 
	 * @param httpclient
	 */
	public static void close(CloseableHttpAsyncClient httpclient) {
		if (httpclient != null) {
			try {
				httpclient.close();
			} catch (IOException e) {
				log.error("关闭异步httpclient异常:" + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws Exception {
		CloseableHttpAsyncClient httpAsyncClient = HttpAsyncClientUtil.getClient();
		String[] urisToGet = { "http://www.baidu.com/", "http://www.qq.com/", };
		final CountDownLatch latch = new CountDownLatch(urisToGet.length);
		try {
			for (final String uri : urisToGet) {
				final HttpGet httpget = new HttpGet(uri);
				httpAsyncClient.execute(httpget, new FutureCallback<HttpResponse>() {

					public void completed(final HttpResponse response) {
						latch.countDown();
						System.out.println(httpget.getRequestLine() + "->" + response.getStatusLine());
					}

					public void failed(final Exception ex) {
						latch.countDown();
						System.out.println(httpget.getRequestLine() + "->" + ex);
					}

					public void cancelled() {
						latch.countDown();
						System.out.println(httpget.getRequestLine() + " cancelled");
					}

				});
			}
			latch.await();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			HttpAsyncClientUtil.close(httpAsyncClient);
		}
	}

}
