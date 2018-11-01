package com.test.web.Utils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;



public class HttpUtil {

	
	public  static String httpInvoke1(String url, String methodm, String data) {
		StringBuilder response = new StringBuilder();
		try {
			URL httpurl = new URL(url);
			HttpURLConnection hc = (HttpURLConnection) httpurl.openConnection();
			String method = methodm.toUpperCase();
			hc.setRequestMethod(method);
			hc.setDoInput(true);
			hc.setConnectTimeout(5000);
			hc.setReadTimeout(5000);

			if ("POST".equals(method)) {
				hc.setDoOutput(true);
				if (data != null) {
					hc.setRequestProperty("Content-Length",
							String.valueOf(data.length()));
				}
			}
			hc.setRequestProperty("Content-Type", "application/json");
			hc.setRequestProperty("Charset", "UTF-8");

			hc.connect();
			if ("POST".equals(method)) {
				OutputStream ops = hc.getOutputStream();
				byte[] buff;
				if (data != null) {
					buff = data.getBytes("UTF-8");
					ops.write(buff);
				}
				ops.flush();
				ops.close();
			}
			int code = hc.getResponseCode();
			if (code == 200) {
				InputStream ins;
				ins = hc.getInputStream();
				InputStreamReader isr = new InputStreamReader(ins, "UTF-8");
				char[] cbuf = new char[1024];
				int i = isr.read(cbuf);
				while (i > 0) {
					response.append(new String(cbuf, 0, i));
					i = isr.read(cbuf);
				}
				ins.close();
			} else {
				InputStream ins;
				ins = hc.getErrorStream();
				InputStreamReader isr = new InputStreamReader(ins, "UTF-8");
				char[] cbuf = new char[1024];
				int i = isr.read(cbuf);
				while (i > 0) {
					response.append(new String(cbuf, 0, i));
					i = isr.read(cbuf);
				}
				ins.close();
			}
			hc.disconnect();
		} catch (Exception e) {
			//log.info("网络不通或连接超时，请稍后再试！");
			System.out.println("网络不通或连接超时，请稍后再试！");
			e.printStackTrace();
		}
		return response.toString();
	}



}
