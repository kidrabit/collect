package com.pcn.manager.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

//설정파일 외부로 뺄때 사용 주석 제거 후 사용
//ex) java -jar -Dconf.home=파일위치 test.war
//@Configuration
public class PropertyConfiguration {
	
	private static String confHome = System.getProperty("conf.home");
	
    @Bean
    @SuppressWarnings("resource")
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() throws IOException {
    	
    	PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
    	
    	Path path = Paths.get(confHome);
		Stream<Path> files = Files.list(path);
    	
    	Object[] file_temp_array = files.toArray();
    	List<File> file_properties = new ArrayList<File>();
    	List<File> file_yml = new ArrayList<File>();
    	
    	String fileNm = "";
    	File file = null;
    	
    	for(Object obj : file_temp_array) {
    		file = new File(obj.toString());
    		fileNm = file.getName();
    		if(fileNm.matches("(?i)(\\S)+\\.(properties)$")) {
    			file_properties.add(file);
    		} else if (fileNm.matches("(?i)(\\S)+\\.(yml)$")) {
    			file_yml.add(file);
    		}
    	}
    	
    	Resource[] resource = null;
    	
    	if(file_properties.size() > 0) {
    		resource = getResource(file_properties);
    		propertySourcesPlaceholderConfigurer.setLocations(resource);
    	}
    	
    	if(file_yml.size() > 0) {
    		resource = getResource(file_yml);
            YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
            yaml.setResources(resource);
            propertySourcesPlaceholderConfigurer.setProperties(yaml.getObject());
    	}

        return propertySourcesPlaceholderConfigurer;
    }
    
    public static Resource[] getResource(List<File> list) {
    	Resource[] resource = new Resource[list.size()];
    	
    	for(int i=0; i<list.size(); i++) {
    		resource[i] = new FileSystemResource(list.get(i).getPath());
    	}
    	return resource;
    }
}
