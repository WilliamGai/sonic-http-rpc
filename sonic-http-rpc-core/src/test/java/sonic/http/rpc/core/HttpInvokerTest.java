//package sonic.http.rpc.core;
//
//import java.io.OutputStream;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.http.HttpHost;
//import org.apache.http.HttpResponse;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.conn.routing.HttpRoute;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
//import org.apache.http.message.BasicNameValuePair;
//import org.apache.http.util.EntityUtils;
//
//import com.sincetimes.website.core.common.support.HttpUtil;
//import com.sonic.http.rpc.exception.RpcException;
//import com.sonic.http.rpc.exception.RpcExceptionCodeEnum;
//import com.sonic.http.rpc.invoke.ConsumerConfig;
//import com.sonic.http.rpc.invoke.Invoker;
//
///**
// * 使用了阿帕奇的httpClient
// * 
// * 为consumer提供http服务,请求远程的服务提供者
// * 		<dependency>
//			<groupId>org.apache.httpcomponents</groupId>
//			<artifactId>httpclient</artifactId>
//		</dependency>
//
// * @author bao
// * @date 2017年8月2日 上午12:16:21
// */
//public class HttpInvokerTest implements Invoker {
//    private static final HttpClient httpClient = getHttpClient();
//
//    public static final Invoker invoker = new HttpInvokerTest();
//
//    public String request(String request, ConsumerConfig consumerConfig) throws RpcException {
//	 HttpPost post = new HttpPost(consumerConfig.getUrl());
//	// post.setHeader("Connection", "Keep-Alive");
//	 List<NameValuePair> params = new ArrayList<NameValuePair>();
//	 params.add(new BasicNameValuePair("data", request));
//	 try {
//	 post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
//	 HttpResponse response = httpClient.execute(post);
//	 if (response.getStatusLine().getStatusCode() == 200) {
//	 return EntityUtils.toString(response.getEntity(), "UTF-8");
//	 }
//	 throw new RpcException(RpcExceptionCodeEnum.INVOKE_REQUEST_ERROR.getCode(), request);
//	 } catch (Exception e) {
//	 throw new RpcException("http 调用异常", e, RpcExceptionCodeEnum.INVOKE_REQUEST_ERROR.getCode(), request);
//	 }
//
//    }
//
//    public void response(String response, OutputStream outputStream) throws RpcException {
//	try {
//	    outputStream.write(response.getBytes("UTF-8"));
//	    outputStream.flush();
//	} catch (Exception e) {
//	    e.printStackTrace();
//	}
//    }
//
//    public static HttpClient getHttpClient() {
//	PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
//	// 连接池最大生成连接数200
//	cm.setMaxTotal(200);
//	// 默认设置route最大连接数为20
//	cm.setDefaultMaxPerRoute(20);
//	// 指定专门的route，设置最大连接数为80
//	HttpHost localhost = new HttpHost("localhost", 8080);
//	cm.setMaxPerRoute(new HttpRoute(localhost), 50);
//	// 创建httpClient
//	return HttpClients.custom().setConnectionManager(cm).build();
//    }
//}
