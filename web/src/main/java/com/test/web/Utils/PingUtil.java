package com.test.web.Utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class PingUtil {

	
	 public static Integer doPingCmd(String destIp, int maxCount) { 
	        LineNumberReader input = null; 
	        try { 
	            String osName = System.getProperties().getProperty("os.name"); 
	            String pingCmd = null; 
	            if (osName.startsWith("Windows")) { 
	                pingCmd = "cmd /c ping -n {0} {1}"; 
	                pingCmd = MessageFormat.format(pingCmd, maxCount, destIp); 
	            } else if (osName.startsWith("Linux")) { 
	                pingCmd = "ping -c {0} {1}"; 
	                pingCmd = MessageFormat.format(pingCmd, maxCount, destIp); 
	            } else { 
	                System.out.println("not support OS"); 
	                return null; 
	            } 
	            Process process = Runtime.getRuntime().exec(pingCmd); 
	            InputStreamReader ir = new InputStreamReader(process 
	                    .getInputStream(),"gbk"); 
	            input = new LineNumberReader(ir); 
	            String line; 
	            List<String> reponse = new ArrayList<String>(); 
	 
	            while ((line = input.readLine()) != null) { 
	                if (!"".equals(line)) { 
	                    reponse.add(line); 
	                    // System.out.println("====:" + line); 
	                } 
	            } 
	            if (osName.startsWith("Windows")) { 
	                return parseWindowsMsg(reponse, maxCount); 
	            } else if (osName.startsWith("Linux")) { 
	                return parseLinuxMsg(reponse, maxCount); 
	            } 
	 
	        } catch (IOException e) { 
	            System.out.println("IOException   " + e.getMessage()); 
	 
	        } finally { 
	            if (null != input) { 
	                try { 
	                    input.close(); 
	                } catch (IOException ex) { 
	                    System.out.println("close error:" + ex.getMessage()); 
	 
	                } 
	            } 
	        } 
	        return null; 
	    } 
	  private static  int parseWindowsMsg(List<String> reponse, int total) { 
	        int countTrue = 0; 
	        int countFalse = 0; 
	        for (String str : reponse) { 
	            if (str.startsWith("来自") || str.startsWith("Reply from")) { 
	                countTrue++; 
	            } 
	            if (str.startsWith("请求超时") || str.startsWith("Request timed out")) { 
	                countFalse++; 
	            } 
	        } 
	        return countTrue; 
	    } 
	 
	    private  static int parseLinuxMsg(List<String> reponse, int total) { 
	        int countTrue = 0; 
	        for (String str : reponse) { 
	            if (str.contains("bytes from") && str.contains("icmp_seq=")) { 
	                countTrue++; 
	            } 
	        } 
	        return countTrue; 
	    } 
	  
	    

}
