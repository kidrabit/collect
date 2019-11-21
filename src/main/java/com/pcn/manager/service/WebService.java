package com.pcn.manager.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

@Service
public class WebService {
	
    @Value("${logstash.dir.path}")
    private String logstash_dir_path;
    @Value("${logstash.conf.section}")
    private String logstash_conf_section;

    public String loadEdit(Model model, Map<String, String> paramMap) {
    	
		String file_path = this.getFilePathSptReplace(logstash_dir_path) + File.separator + paramMap.get("fileName");
		
		File file = new File(file_path);
		StringBuffer strBuf = new StringBuffer();
		
		if(file.exists()) {
            try (FileReader fileReader = new FileReader(file);
            	 BufferedReader bufReader = new BufferedReader(fileReader);){
				
				String line = "";
				String next = System.getProperty("line.separator");
				while((line = bufReader.readLine()) != null){
				    strBuf.append(line);
				    strBuf.append(next);
				}
				
				if(strBuf.lastIndexOf(next) > 0) {
					strBuf.delete(strBuf.lastIndexOf(next), strBuf.length());
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return strBuf.toString();
	}
	
	public void saveEdit(Model model, Map<String, String> paramMap) {
		
		String fileName = paramMap.get("fileName");
		
		if("add".equalsIgnoreCase(fileName)) {
			fileName = "logstash-"+this.getFileNm()+".conf";
		}
		
		String file_path = this.getFilePathSptReplace(logstash_dir_path) + File.separator + fileName;
		
		File file = new File(file_path);
	
    	try (FileWriter fileWriter = new FileWriter(file);
    		 BufferedWriter bufWriter =	new BufferedWriter(fileWriter);){
			
			bufWriter.write(paramMap.get("contents"));
			bufWriter.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
            
    	paramMap.put("fileName", fileName);
    	paramMap.put("msg", "success");
	}
	
	public String loadEditDt(Model model, Map<String, String> paramMap) {
		
		String file_path = this.getFilePathSptReplace(logstash_dir_path) + File.separator + paramMap.get("fileName");
		
		File file = new File(file_path);
		StringBuffer strBuf = new StringBuffer();
		
		String confSection = paramMap.get("confSection");
		String regEx = "^\\s*(?i)("+confSection+")\\s*\\{";
		
		String[] sectionArray = logstash_conf_section.split(",");
		List<String> sectionList = new ArrayList<String>();
		Collections.addAll(sectionList, sectionArray);
		sectionList.remove(confSection);
		
		if(file.exists()) {
            try (FileReader fileReader = new FileReader(file);
            	 BufferedReader bufReader = new BufferedReader(fileReader);){
            	
				String line = "";
				String next = System.getProperty("line.separator");
				
		        Pattern pattern = null, pattern1 = null, pattern2 = null;
		        Matcher matcher = null, matcher1 = null, matcher2 = null;

		        pattern1 = Pattern.compile("^\\s*(?i)("+sectionList.get(0)+")\\s*\\{");
		        pattern2 = Pattern.compile("^\\s*(?i)("+sectionList.get(1)+")\\s*\\{");
		        
				while((line = bufReader.readLine()) != null){
					
					pattern = Pattern.compile(regEx);
					matcher = pattern.matcher(line);
					
					if(matcher.find()){
						
						matcher1 = pattern1.matcher(line);
						matcher2 = pattern2.matcher(line);
						
						if(matcher1.find() || matcher2.find()) {
							break;
						}
						
						strBuf.append(line);
						strBuf.append(next);
						regEx = ".*";
					} 
				}
				
				if(strBuf.lastIndexOf(next) > 0) {
					strBuf.delete(strBuf.lastIndexOf(next), strBuf.length());
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		String contents = strBuf.toString();
		
		if(contents.length() > 0) {
			contents = contents.replaceAll("^\\s*(?i)("+confSection+")\\s*\\{", "");
			contents = contents.substring(0, contents.lastIndexOf("}")).trim();
		}
            
		return contents;
	}
	
	public void saveEditDt(Model model, Map<String, String> paramMap) {
		
		String file_path = this.getFilePathSptReplace(logstash_dir_path) + File.separator + paramMap.get("fileName");
		
		File file = new File(file_path);
		
		StringBuffer strBuf = new StringBuffer();
		StringBuffer strBufAll = new StringBuffer();
		
		String confSection = paramMap.get("confSection");
		String regEx = "^\\s*(?i)("+confSection+")\\s*\\{";
		
		String[] sectionArray = logstash_conf_section.split(",");
		List<String> sectionList = new ArrayList<String>();
		Collections.addAll(sectionList, sectionArray);
		sectionList.remove(confSection);
		
		if(file.exists()) {
			
			FileReader fileReader = null;
			BufferedReader bufReader = null;
			
            try {
				fileReader = new FileReader(file);
				bufReader = new BufferedReader(fileReader);
				
				String line = "";
				String next = System.getProperty("line.separator");
				
		        Pattern pattern = null, pattern1 = null, pattern2 = null;
		        Matcher matcher = null, matcher1 = null, matcher2 = null;
		        
		        pattern1 = Pattern.compile("^\\s*(?i)("+sectionList.get(0)+")\\s*\\{");
		        pattern2 = Pattern.compile("^\\s*(?i)("+sectionList.get(1)+")\\s*\\{");
		        
				while((line = bufReader.readLine()) != null){
					
					pattern = Pattern.compile(regEx);
					matcher = pattern.matcher(line);
					
					if(matcher.find()){
						
						matcher1 = pattern1.matcher(line);
						matcher2 = pattern2.matcher(line);
						
						if(matcher1.find() || matcher2.find()) {
							break;
						}
						
						strBuf.append(line);
						strBuf.append(next);
						regEx = ".*";
					} 
				}
				
				if(strBuf.lastIndexOf(next) > 0) {
					strBuf.delete(strBuf.lastIndexOf(next), strBuf.length());
				}
				
				bufReader.close();
				fileReader.close();
				
				fileReader = new FileReader(file);
				bufReader = new BufferedReader(fileReader);
				
				while((line = bufReader.readLine()) != null){
					strBufAll.append(line);
					strBufAll.append(next);
				}
				
				if(strBufAll.lastIndexOf(next) > 0) {
					strBufAll.delete(strBufAll.lastIndexOf(next), strBufAll.length());
				}
				
				String contents = confSection + " {" + next + paramMap.get("contents") + next + "}";
				
				if(strBuf.length() == 0) {
					if("input".equalsIgnoreCase(confSection)) {
						
						StringBuffer bufferTemp = new StringBuffer();
						bufferTemp.append(contents);
						bufferTemp.append(next);
						bufferTemp.append(strBufAll.toString().trim());
						paramMap.put("contentsAll", bufferTemp.toString());
					}else if("filter".equalsIgnoreCase(confSection)) {
						
						pattern = Pattern.compile("\\S*(?i)(output)\\s*\\{");
						matcher = pattern.matcher(strBufAll.toString());
						
						if(matcher.find()) {
							
							StringBuffer bufferTemp = new StringBuffer();
							bufferTemp.append(strBufAll.substring(0, matcher.start()).trim());
							bufferTemp.append(next);
							bufferTemp.append(contents);
							bufferTemp.append(next);
							bufferTemp.append(strBufAll.substring(matcher.start(), strBufAll.length()).trim());
							paramMap.put("contentsAll", bufferTemp.toString());
						}else {
							strBufAll.append(next);
							strBufAll.append(contents);
							paramMap.put("contentsAll", strBufAll.toString());
						}
					}else {
						strBufAll.append(next);
						strBufAll.append(contents);
						paramMap.put("contentsAll", strBufAll.toString());
					}
				}else {
					paramMap.put("contentsAll", strBufAll.toString().replace(strBuf.toString(), contents));
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					if(bufReader != null) bufReader.close();
					if(fileReader != null) fileReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
    	try (FileWriter fileWriter = new FileWriter(file);
    		 BufferedWriter bufWriter = new BufferedWriter(fileWriter);){
			
			bufWriter.write(paramMap.get("contentsAll"));
			bufWriter.flush();
			
			paramMap.remove("contentsAll");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	paramMap.put("msg", "success");
	}
	
	public void addConfFile(Model model, Map<String, String> paramMap) {
		
		String confFileName = paramMap.get("confFileName");
		String file_path = this.getFilePathSptReplace(logstash_dir_path) + File.separator + confFileName + ".conf";
		
		StringBuffer buf = new StringBuffer();
		String[] sectionArray = logstash_conf_section.split(",");
		String next = System.getProperty("line.separator");
		
		File file = new File(file_path);
		
		if(!file.exists()) {
			
	    	try (FileWriter fileWriter = new FileWriter(file);
	       		 BufferedWriter bufWriter =	new BufferedWriter(fileWriter);){
	    		
	    		for(String section : sectionArray) {
		    		buf.append(section);
		    		buf.append("{");
		    		buf.append(next);
		    		buf.append("}");
		    		buf.append(next);
	    		}
	   			
	   			bufWriter.write(buf.toString().trim());
	   			bufWriter.flush();
	   		} catch (IOException e) {
	   			// TODO Auto-generated catch block
	   			e.printStackTrace();
	   		}
	    	
	    	paramMap.put("msg", "success");
		} else {
			paramMap.put("msg", "duplicate");
		}
	}
	
	public JsonArray getConfJsonObject(Model model, Map<String, String> paramMap) {
		
		JsonArray jsonA = null;
		String contents = this.loadEditDt(model, paramMap);
		
		if(!"".equalsIgnoreCase(contents)) {
			
			String temp1 = "";
			String temp2 = "";
			String regEx0 = "\\}\\s*(?i)\\w+\\s*\\{";
			String regEx1 = "\\s*(?i)\\w+\\s*\\{";
			String regEx2 = "=>.*";
			String regEx3 = "=>\\s*\\{(\\s*|.*)+\\}\\s";
			String regEx4 = "\\s*\\{\\s*,";
			String regEx5 = ",\\s*\\}";
			
			Pattern pattern = null;
			Matcher matcher = null;
			
			//옵션이 2개 이상인 경우 첫번째 옵션 마지막 } 에 }, 추가
			pattern = Pattern.compile(regEx0);
			matcher = pattern.matcher(contents);
			
			while(matcher.find()) {
				
				temp1 = matcher.group();
				temp2 = temp1.replace("}", "}},");
				contents = contents.replace(temp1, temp2);
			}
			// end
			
			// 옵션에  { : 추가  (ex) beats {  --> { beats : {
			pattern = Pattern.compile(regEx1);
			matcher = pattern.matcher(contents);
			
			while(matcher.find()) {
				
				temp1 = matcher.group();
				temp2 = temp1.replace("{", ": {");
				contents = contents.replace(temp1, "{" + temp2);
			}
			// end 
			
			// , 추가
			List<Integer> idxList = new ArrayList<Integer>();
			
			pattern = Pattern.compile(regEx2);
			matcher = pattern.matcher(contents);
			
			while(matcher.find()) {
				idxList.add(matcher.end());
			}
			
			for(int i=(idxList.size()-1); i>-1; i--) {
				temp1 = contents.substring(0, idxList.get(i));
				temp2 = contents.substring(idxList.get(i), contents.length());
				contents = temp1 + ","+ temp2;
			}
			// end 
			
			// } 뒤에  , 추가  
			idxList = new ArrayList<Integer>();
			
			pattern = Pattern.compile(regEx3);
			matcher = pattern.matcher(contents);
			
			while(matcher.find()) {
				idxList.add(matcher.end());
			}
			
			for(int i=(idxList.size()-1); i>-1; i--) {
				
				temp1 = contents.substring(0, idxList.get(i));
				temp2 = contents.substring(idxList.get(i), contents.length());
				contents = temp1.trim() + ","+ temp2;
			}
			
			// => 를 : 로 변경
			contents = contents.replaceAll("=>", ":");
			
			// { 뒤에 있는 , 를 제거 (ex) { , --> {  			
			pattern = Pattern.compile(regEx4);
			matcher = pattern.matcher(contents);
			
			while(matcher.find()) {
				
				temp1 = matcher.group();
				temp2 = temp1.replace(",", "");
				contents = contents.replace(temp1, temp2);
			}
			// end
			
			// 마지막 ,  제거
			pattern = Pattern.compile(regEx5);
			matcher = pattern.matcher(contents);
			
			while(matcher.find()) {
				
				temp1 = matcher.group();
				temp2 = temp1.replace(",", "");
				contents = contents.replace(temp1, temp2);
			}
			
			//내용에 특수문자(\) 있을 경우 하나 더 추가(이유 : json 파싱 및 화면 오류)
			contents = contents.replaceAll("\\\\", "\\\\\\\\");
			// end

			contents = "[" + contents + "}]";
			
			//System.out.println(contents);
			JsonParser json = new JsonParser();
			jsonA = (JsonArray)json.parse(contents);
		}
		
		return jsonA;
	}
	
	public void getSelectList(Model model, Map<String, String> paramMap) {
		
		String confSection = paramMap.get("confSection");
		
		Set<String> set = null;
		JsonObject jsonO = this.getWriteFormJson(confSection);
		
		if(jsonO != null) set = jsonO.keySet();
		
		model.addAttribute("confSection", confSection);
		model.addAttribute("selectList", set);
		//model.addAttribute("formJson", jsonO);
	}
	
	public JsonObject getWriteFormJson(String confSection) {
		
		JsonParser parser = null;
		JsonElement jsonE = null;
		JsonObject jsonO = null;
		
		/*
		File file = new File("src\\main\\resources\\static\\manager\\writeFormJson\\" + confSection + ".json");
		
		if(file.exists()) {
			
			try (FileReader fr = new FileReader(file);) {
				parser = new JsonParser();
				jsonE = parser.parse(fr);
				jsonO = jsonE.getAsJsonObject();
			} catch (JsonIOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonSyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		*/
		ClassPathResource cpr = new ClassPathResource("static\\manager\\writeFormJson\\" + confSection + ".json");
		
		if(cpr.exists()) {
			try (InputStream  is = cpr.getInputStream();){
				byte[] data = FileCopyUtils.copyToByteArray(is);
				String strJson = new String(data);
				parser = new JsonParser();
				jsonE = parser.parse(strJson);
				jsonO = jsonE.getAsJsonObject();
			} catch (JsonSyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return jsonO;
	}
	
	public String writeUiSave(Model model, Map<String, String> paramMap, String[] tabItemNm, HttpServletRequest request) {
		
		String[] type = null;// 0 : 탭순서, 1 : 탭명 
		StringBuffer bf = new StringBuffer();
		String next = System.getProperty("line.separator");
		JsonObject jsonO = this.getWriteFormJson(paramMap.get("confSection"));
		JsonObject JsonItem = null;
		Set<Entry<String, JsonElement>> entrySet = null;
		String data = "";
		String dataType = "";
		String[] temp = null;
		String[] hashNm = null;
		String[] hashVal = null;
		String paramKey = "";
		
		if(tabItemNm != null) {
		
			for(int i=0; i<tabItemNm.length; i++) {
				
				type = tabItemNm[i].split(":");
				JsonItem = (JsonObject)jsonO.get(type[1]);
				
				entrySet = JsonItem.entrySet();
				
				bf.append("\t");
				bf.append(type[1]);
				bf.append(" {");
				bf.append(next);
				
				for(Entry<String, JsonElement> e : entrySet) {
					
					paramKey = type[0]+"_"+e.getKey();
					
					data = paramMap.get(paramKey);
					dataType = ((JsonObject)e.getValue()).get("data").getAsString();
					
					if(!"hash".equalsIgnoreCase(dataType)) {
						if(data == null || "".equalsIgnoreCase(data.trim())) {
							continue;
						} else {
							bf.append("\t\t");
							bf.append(e.getKey());
							bf.append(" => ");
						}
					}
					
					if("string".equalsIgnoreCase(dataType)) {
						
						bf.append("\"");
						bf.append(data);
						bf.append("\"");
						bf.append(next);
					} else if("string_single_quote".equalsIgnoreCase(dataType)) {
						
						bf.append("\'");
						bf.append(data);
						bf.append("\'");
						bf.append(next);
					} else if("hash".equalsIgnoreCase(dataType)) {
						
						hashNm = request.getParameterValues(paramKey+"_name");
						hashVal = request.getParameterValues(paramKey+"_value");
						
						if(hashNm != null) {
							
							bf.append("\t\t");
							bf.append(e.getKey());
							bf.append(" => ");
							
							bf.append("{");
							bf.append(next);
							for(int j=0; j<hashNm.length; j++) {
								bf.append("\t\t\t\t");
								bf.append("\"");
								bf.append(hashNm[j]);
								bf.append("\"");
								bf.append(" => ");
								bf.append("\"");
								bf.append(hashVal[j]);
								bf.append("\"");
								bf.append(next);
							}
							bf.append("\t\t\t\t}");
							bf.append(next);
						}
					} else if("array".equalsIgnoreCase(dataType)) {
						temp = data.trim().split(",");
						bf.append("[");
						for(int j=0; j<temp.length; j++) {
							bf.append("\"");
							bf.append(temp[j].trim());
							bf.append("\",");
						}
						bf.delete(bf.lastIndexOf(","), bf.length());
						bf.append("]");
						bf.append(next);
					} else {
						bf.append(data);
						bf.append(next);
					}
				}
				bf.append("\t}");
				bf.append(next);
			}
		}
		
		if(bf.lastIndexOf(next) > 0) {
			bf.delete(bf.lastIndexOf(next), bf.length());
		}
		
		paramMap.put("contents", bf.toString());
		this.saveEditDt(model, paramMap);
		
		return null;
	}
	
	public String sectionItemList(Model model, Map<String, String> paramMap){
		
		String itemList = "";
        JsonArray inputJson = this.getConfJsonObject(model, paramMap);
        
        if(inputJson != null) {
        	for(JsonElement jElement : inputJson) {
        		JsonObject jObject = jElement.getAsJsonObject();
        		Set<String> set = jObject.keySet();
        		
        		Iterator<String> itr = set.iterator();
        		
        		while(itr.hasNext()) {
        			itemList += itr.next() + ",";
        		}
        	}
        	
        	if(!"".equalsIgnoreCase(itemList)) {
        		itemList = itemList.substring(0, itemList.lastIndexOf(","));
        	}
        }
		
		return itemList;
	}
	
	public String getFileNm() {
		
		return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	}
	
	public String getFilePathSptReplace(String path) {
		
		return path.replaceAll("\\\\", Matcher.quoteReplacement(File.separator));
	}
	
	public List<String> getConfFileList() {
		
		File dir = new File(this.getFilePathSptReplace(logstash_dir_path));
		List<String> fileNmList = new ArrayList<String>();
		
		if(dir.exists()) {
			File[] fileArray = dir.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					// TODO Auto-generated method stub
					if(name.matches("(\\S)+\\.(conf)$")) {
						return true;
					}
					return false;
				}
			});
			
			for(File f : fileArray) {
				fileNmList.add(f.getName());
			}
		}

		return fileNmList;
	}
}
