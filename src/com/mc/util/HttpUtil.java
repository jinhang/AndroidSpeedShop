package com.mc.util;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
/**
 * 
 * @author Administrator
 * @description �ǵ��޸Ĵ��룬���ͷ�������Ӧ��ʱ����Ҫ fall back ����
 */
public class HttpUtil {
	// ����URL
	//public static final String BASE_URL="http://36.47.36.172:8080/ShopServer/";
	//public static final String BASE_URL="http://10.0.2.2:8080/ShopServer/";
	public static final String BASE_URL = "http://222.24.63.111:88/EmailCUP/";
	//public static String SERVER_ADDRESS="192.168.1.103";
	public static String SERVER_ADDRESS="222.24.63.111";
	public static int SERVER_PORT = 88;
	
	public static String CONNECT_EXCEPTION="�����쳣��";
	
	// ���Get�������request
	public static HttpGet getHttpGet(String url){
		HttpGet request = new HttpGet(url);
		 return request;
	}
	// ���Post�������request
	public static HttpPost getHttpPost(String url){
		 HttpPost request = new HttpPost(url);
		 return request;
	}
	// ������������Ӧ����response
	public static HttpResponse getHttpResponse(HttpGet request) throws ClientProtocolException, IOException{
		HttpResponse response = new DefaultHttpClient().execute(request);
		return response;
	}
	// ������������Ӧ����response
	public static HttpResponse getHttpResponse(HttpPost request) throws ClientProtocolException, IOException{
		HttpResponse response = new DefaultHttpClient().execute(request);
		return response;
	}
	
	// ����Post���󣬻����Ӧ��ѯ���
	public static String queryStringForPost(String url){
		// ����url���HttpPost����
		//for test remove , if run server ,need fall back
		HttpPost request = HttpUtil.getHttpPost(url);
		System.out.println("request=========="+request);
		String result = null;
		//for test remove , if run server ,need fall back
		try {
			// �����Ӧ����
			HttpResponse response = HttpUtil.getHttpResponse(request);
			System.out.println("response=========="+response);
			System.out.println("response.getStatusLine().getStatusCode()=========="+response.getStatusLine().getStatusCode());
			// �ж��Ƿ�����ɹ�
			if(response.getStatusLine().getStatusCode()==200){
				System.out.println("��Ӧ�ɹ�");
				System.out.println("response.getEntity()=========="+response.getEntity());
				
				// �����Ӧ
				result = EntityUtils.toString(response.getEntity(),"utf-8");//��ֹ��������
				//result=new String(result.getBytes("ISO-8859-1"),"utf-8"); //
				System.out.println("result=========="+result);
				return result;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			result = HttpUtil.CONNECT_EXCEPTION;
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			result = HttpUtil.CONNECT_EXCEPTION;
			return result;
		}
        return null;
        
		
		//for test add , if run server ,need remove
		//return String.valueOf(1);
    }
	// �����Ӧ��ѯ���
	public static String queryStringForPost(HttpPost request){
		String result = null;
		try {
			// �����Ӧ����
			HttpResponse response = HttpUtil.getHttpResponse(request);
			// �ж��Ƿ�����ɹ�
			if(response.getStatusLine().getStatusCode()==200){
				// �����Ӧ
				result = EntityUtils.toString(response.getEntity());
				return result;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			result = HttpUtil.CONNECT_EXCEPTION;
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			result = HttpUtil.CONNECT_EXCEPTION;
			return result;
		}
        return null;
    }
	// ����Get���󣬻����Ӧ��ѯ���
	public static  String queryStringForGet(String url){
		// ���HttpGet����
		HttpGet request = HttpUtil.getHttpGet(url);
		String result = null;
		try {
			// �����Ӧ����
			HttpResponse response = HttpUtil.getHttpResponse(request);
			// �ж��Ƿ�����ɹ�
			if(response.getStatusLine().getStatusCode()==200){
				// �����Ӧ
				result = EntityUtils.toString(response.getEntity());
				return result;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			result = HttpUtil.CONNECT_EXCEPTION;
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			result = HttpUtil.CONNECT_EXCEPTION;
			return result;
		}
        return null;
    }
	public static void main(String[] args) {
		String  url = HttpUtil.BASE_URL+
				"TodaySalesPromotionServlet?shop_id=" +"1";;
		HttpUtil.queryStringForGet(url);
	}
}
