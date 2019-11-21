package com.pcn.manager.command;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

import org.springframework.stereotype.Component;

/**
 * @class Shutter
 * 	com.pcn.manager.command
 * @section 클래스작성정보
 *    |    항  목        	|       	내  용       			|
 *    | :--------: 	| -----------------------------	|
 *    | Company 	| PCN
 *    | Author 		| rnd
 *    | Date 		| 2019. 8. 22.
 *    | 작업자 		| rnd, Others...
 * @section 상세설명
 * - 클래스의 업무내용에 대해 기술...
*/
@Component
public class Shutter {
	
	private Map<String, String> msgMap; 
	
	public Shutter() {
		msgMap = new HashMap<String, String>();
	}

    public Map<String, String> engineStart(String osType, String path, String port) {
        
    	String strMsg = "";
        String pid = "";
        String[] command = null;
        String fSepar = File.separator;
        path = path.replaceAll("\\\\", Matcher.quoteReplacement(File.separator));
        
        if(osType.equalsIgnoreCase("win")) {
        	pid = getWinPID(port);
        	command = new String[]{"cmd", "/C", path + fSepar + "logstash.bat -f " + path + fSepar + "logstash.conf"};
        } else {
        	pid = getLxPID(port);
        	command = new String[]{"sh", "-c", path + fSepar + "logstash -f " + path + fSepar + "logstash.conf &"};
        }
        
        if("".equalsIgnoreCase(pid)) {
        	
        	Process process = null;
        	InputStreamReader isr = null;
        	BufferedReader br = null;

	        Runtime run = Runtime.getRuntime();
	
	        try {
	            process = run.exec(command);
	            
				isr = new InputStreamReader(process.getInputStream());
				br = new BufferedReader(isr);

				String line = "";
				
				line = br.readLine();
				
				while ((line = br.readLine()) != null) {
				    System.out.println(line);
				    
				    if(osType.equalsIgnoreCase("win")) {
				    	if(line.toUpperCase().contains("SUCCESSFULLY STARTED LOGSTASH")) break;
				    }
				}
				
				br.close();
				isr.close();
				process.destroy();
				
				if(!osType.equalsIgnoreCase("win")) Thread.sleep(25000);
				
				if(osType.equalsIgnoreCase("win")) {
					pid = getWinPID(port);
				} else {
					pid = getLxPID(port);
				}
				
				if(!"".equalsIgnoreCase(pid)) {
					strMsg = "엔진 시작";
				} else {
					strMsg = "엔진 시작 오류";
				}
	
				br.close();
				isr.close();
				process.destroy();
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
				try {
					if(br != null) br.close();
					if(isr != null) isr.close();
					if(process != null) process.destroy();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
        } else {
        	strMsg = "시작중인 엔진 있음";
        }
        
        System.out.println(strMsg);
        msgMap.put("engineMsg", strMsg);
        
        return msgMap;
    }

    public Map<String, String> engineDown(String osType, String port) {
    	
    	String strMsg = "";
        String pid = "";
        String[] command = null;
        
        if(osType.equalsIgnoreCase("win")) {
        	pid = getWinPID(port);
        	command = new String[]{"cmd", "/C", "taskkill /f /pid " + pid};
        } else {
        	pid = getLxPID(port);
        	command = new String[]{"sh", "-c", "kill -9 " + pid};
        }
        
        if(!"".equalsIgnoreCase(pid)) {
        	
        	Process process = null;
        	InputStreamReader isr = null;
        	BufferedReader br = null;
        
	        Runtime run = Runtime.getRuntime();
	
			try {
				process = run.exec(command);
				
				isr = new InputStreamReader(process.getInputStream());
				br = new BufferedReader(isr);

				String line = "";
				
				while ((line = br.readLine()) != null) {
				    System.out.println(line);
				}
				
				strMsg = "엔진 다운";
				
				br.close();
				isr.close();
				process.destroy();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					if(br != null) br.close();
					if(isr != null) isr.close();
					if(process != null) process.destroy();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
        } else {
        	strMsg = "시작중인 엔진이 없음";
        }
        
        System.out.println(strMsg);
        msgMap.put("engineMsg", strMsg);
        
        return msgMap;
    }

    public String getWinPID(String port) {
    	
        String[] command = {"cmd", "/C", "netstat -ano | findstr " + port};
        
		Process process = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
        
		String pid = "";
        Runtime run = Runtime.getRuntime();

        try {
			process = run.exec(command);
			isr = new InputStreamReader(process.getInputStream());
			br = new BufferedReader(isr);
			
			String line = "";
			
            while ((line = br.readLine()) != null) {
            	line = line.replaceAll("\\s+", " ");
            	pid = line.split(" ")[5].trim();
            }
            
			br.close();
			isr.close();
			process.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
			try {
				if(br != null) br.close();
				if(isr != null) isr.close();
				if(process != null) process.destroy();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
        return pid;
    }
    
    public String getLxPID(String port) {
    	
    	//String[] command = {"sh", "-c", "ps -ef|grep logstash|grep -v grep|awk '{print $2}'"};
    	String[] command = {"sh", "-c", "netstat -lntp|grep " + port + "|awk '{print $7}'"};
    	
		Process process = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		
        Runtime run = Runtime.getRuntime();
        String pid = "";
        	
		try {
			process = run.exec(command);
			isr = new InputStreamReader(process.getInputStream());
			br = new BufferedReader(isr);
			
			String line = "";
			
			while((line = br.readLine()) != null) {
			   pid = line.trim().split("/")[0];
			}
			
			br.close();
			isr.close();
			process.destroy();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(br != null) br.close();
				if(isr != null) isr.close();
				if(process != null) process.destroy();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
        
        return pid;
    }
}
