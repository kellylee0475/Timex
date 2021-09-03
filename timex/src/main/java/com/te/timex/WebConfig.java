package com.te.timex;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Value("${profilePhoto_path}")
	private String download_path;

	 
//    @Value("${spring.webservice.intro}")
//    private String introPage;
//    
//    
//	@Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        // 루트 (/) 로 접근 시 introPage로 이동하는 매핑 추가(application.properties에서 intro페이지 설정)
//        registry.addRedirectViewController("/", introPage);
//    }
//	
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		//C:\\Users\\pc1\\Desktop\\Timex Spring Boot\\profile_photos
	//	exposeDirectory("profilePhoto", registry);
//		 String myExternalFilePath = "file:///C:/Users/pc1/Desktop/Timex Spring Boot/profile_photos/";
	String myExternalFilePath = "file:///"+download_path+"/";
		    registry.addResourceHandler("/profile_photos/**").addResourceLocations(myExternalFilePath);
		    
	}

//	private void exposeDirectory(String dirName, ResourceHandlerRegistry registry) {
//	//	Path uploadDir = Paths.get(dirName);
//
//	//	String uploadPath = uploadDir.toFile().getAbsolutePath();	
//	
//	    String myExternalFilePath = "file:///C:/Users/pc1/Desktop/Timex Spring Boot/profile_photos/";
//	    registry.addResourceHandler("/profile_photos/**").addResourceLocations(myExternalFilePath);
//	    
//
//	}
}