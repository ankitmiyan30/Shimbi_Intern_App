package com.shimbi.library;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.shimbi.billing.Login;


public class MyBilling_user_function {
	byte[] data;
	HttpPost httppost;
	StringBuffer buffer, buffer1;
	HttpResponse response;
	HttpClient httpclient;
	InputStream inputStream;

	private static String appURL ="https://www.mybilling.biz/andriod/";
	//private static String appURL ="http://10.0.2.2/MyBilling/android_test/";

	Login login=new Login();

	public static Integer url_error = 0;
	String stresult;
	
	
	public String validateLogin(String uname, String pwd)
	{
		String domain_name=MyBilling_sessionManager.get_Admin_link();
		String url = appURL+"user_validate.php?domain_name="+domain_name+"&email="+uname+"&pwd="+pwd;
		/*String encrpy_username=md5(uname);
		String encrpy_pwd=md5(pwd);
		List<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("email", encrpy_username));
		nameValuePairs.add(new BasicNameValuePair("pwd", encrpy_pwd));*/
		handleUrl(url);
		return stresult;
	}
	
    private static final String md5(final String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(password.getBytes());
            byte messageDigest[] = digest.digest();
     
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();
     
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
    
    public String getRecordsList(String userID, int report_type)
	{
    	String domain_name=MyBilling_sessionManager.get_Admin_link();
		String url = appURL;
		//String encode_url = null;
	
			switch(report_type)
			{
				case 1 : //invoice list
						url = url+"invoice_list.php?id="+userID+"&domain_name="+domain_name;
					
						break;
				case 2 : //Receipt List
						url = url+"receipt_list.php?id="+userID+"&domain_name="+domain_name;
					
						break;	
				case 3 : //estimate List
						url = url+"estimate_list.php?id="+userID+"&domain_name="+domain_name;
			;
						break;
				case 4: //overdue list
					url = url+"OverDues_list.php?id="+userID+"&domain_name="+domain_name;
					break;
			}
			
			//System.out.println("URL "+ url);
		
			handleUrl(url);
		
		return stresult;
	}
    public String getInvoiceDetails(String invoice_id, int type)
	{
		String url="";
		//String encode_url = null;
		String domain_name=MyBilling_sessionManager.get_Admin_link();;
		switch(type)
		{
			case 1 : 
				url = appURL+"invoice_detail.php?id="+invoice_id+"&domain_name="+domain_name;
				
				break;
			case 2 :
				url = appURL+"receipt_detail.php?id="+invoice_id+"&domain_name="+domain_name;
				
				break;
			case 3:
				url = appURL+"estimate_details.php?id="+invoice_id+"&domain_name="+domain_name;
				
				break;
			case 4 : 
				url = appURL+"invoice_detail.php?id="+invoice_id+"&domain_name="+domain_name;
				
				break;
		}
		//System.out.println("Respective Url: "+url);
		handleUrl(url);
		
	
		return stresult;
	}
    public void handleUrl(String url)
	{
		stresult="";

		//Making HTTP request
		try
		{
			HttpParams httpParameters = new BasicHttpParams();
	    	HttpConnectionParams.setConnectionTimeout(httpParameters, 30000);
	    	HttpConnectionParams.setSoTimeout(httpParameters, 30000);

			//defaultHttpClient
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			//httpPost.setEntity(new UrlEncodedFormEntity(params));

			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			inputStream = httpEntity.getContent();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			inputStream.close();
			stresult = sb.toString();

		}
		catch (Exception e)
		{
			url_error = 1;
		//	System.out.println(e.toString());
		}
	}
	public String getHomeDetails(String clientID) {
		// TODO Auto-generated method stub
		String domain_name = MyBilling_sessionManager.get_Admin_link();
		String url=appURL+"home.php?id="+clientID+"&domain_name="+domain_name;
		handleUrl(url);
		return stresult;
		
	}
	
	public Bitmap DownloadImageFromPath(String path){
		Bitmap bmp = null;
		//Making HTTP request
				try
				{
					HttpParams httpParameters = new BasicHttpParams();
			    	HttpConnectionParams.setConnectionTimeout(httpParameters, 30000);
			    	HttpConnectionParams.setSoTimeout(httpParameters, 30000);

					//defaultHttpClient
					HttpClient httpClient = new DefaultHttpClient();
					HttpPost httpPost = new HttpPost(path);
					//httpPost.setEntity(new UrlEncodedFormEntity(params));

					HttpResponse httpResponse = httpClient.execute(httpPost);
					HttpEntity httpEntity = httpResponse.getEntity();
					inputStream = httpEntity.getContent();

				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				try
				{
					
					 BitmapFactory.Options bmOptions = new BitmapFactory.Options();
			            bmOptions.inSampleSize = 1;
					
					bmp=BitmapFactory.decodeStream(inputStream, null, bmOptions);
					inputStream.close();
					return bmp;
				}
				catch (Exception e)
				{
					url_error = 1;
					//System.out.println(e.toString());
				}
		return bmp;
    }
	
	
	
	public void handleUrl(String url, List<NameValuePair> params)
	{
		stresult="";

		//Making HTTP request
		try
		{
			HttpParams httpParameters = new BasicHttpParams();
	    	HttpConnectionParams.setConnectionTimeout(httpParameters, 30000);
	    	HttpConnectionParams.setSoTimeout(httpParameters, 30000);

			//defaultHttpClient
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			//httpPost.setEntity(new UrlEncodedFormEntity(params));

			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			inputStream = httpEntity.getContent();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			inputStream.close();
			stresult = sb.toString();
			//System.out.println("Result "+stresult);

		}
		catch (Exception e)
		{
			url_error = 1;
		//	System.out.println(e.toString());
		}
	}
	
	
	public String LoadMore(String userID, int report_type, int page_no)
	{
		//String domain_name=MyBilling_sessionManager.get_Admin_link();
		//String url = appURL;
		//String encode_url = null;
	
		//			url = url+"invoice_list.php?id="+"8"+"&domain_name="+domain_name+"&page_no="+page_no;
		//			System.out.println("URL "+ url);
		
		//		handleUrl(url);
		
		//return stresult;

		String domain_name=MyBilling_sessionManager.get_Admin_link();
		String url = appURL;
		//String encode_url = null;
	
			switch(report_type)
			{
				case 1 : //invoice list
						url = url+"invoice_list.php?id="+userID+"&domain_name="+domain_name+"&page_no="+page_no;
					
						break;
				case 2 : //Receipt List
						url = url+"receipt_list.php?id="+userID+"&domain_name="+domain_name+"&page_no="+page_no;
					
						break;	
				case 3 : //estimate List
						url = url+"estimate_list.php?id="+userID+"&domain_name="+domain_name+"&page_no="+page_no;
			
						break;
				case 4: //overdue list
					url = url+"OverDues_list.php?id="+userID+"&domain_name="+domain_name+"&page_no="+page_no;
					break;
			}
			
			//System.out.println("URL "+ url);
		
			handleUrl(url);
		
		return stresult;
	}
	
}

