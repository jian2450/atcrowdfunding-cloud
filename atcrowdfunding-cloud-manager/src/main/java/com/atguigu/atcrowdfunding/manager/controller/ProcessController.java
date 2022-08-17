package com.atguigu.atcrowdfunding.manager.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.atguigu.atcrowdfunding.common.BaseController;
import com.atguigu.atcrowdfunding.common.bean.Page;
import com.atguigu.atcrowdfunding.manager.service.ProcessService;

@Controller
@RequestMapping("/process")
public class ProcessController extends BaseController {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private ProcessService processService;
	
	@RequestMapping("/index")
	public String index() {
		return "process/index";
	}
	
	@ResponseBody
	@RequestMapping("/delete{id}")
	public Object delete(@PathVariable("id") String id) {
		start();
		
		try {
			processService.delete(id);
			success();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		return end();
	}
	
	@RequestMapping("/loadImg/{id}")
	public void loadImg(@PathVariable("id") String id,HttpServletResponse resp) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_PNG);
		
		String url = "http://eureka-activiti-service/loadImgById/"+id;
		ResponseEntity<byte[]> response = restTemplate.exchange(
	        url,
	        HttpMethod.POST,
	        new HttpEntity<byte[]>(headers),
	        byte[].class); 
	    byte[] result = response.getBody();

	    InputStream in = new ByteArrayInputStream(result);
		OutputStream out = resp.getOutputStream();
		
		int i = -1;
		while ( (i = in.read()) != -1 ) {
			out.write(i);
		}

	}
	
	@RequestMapping("/showImg/{id}")
	public String showImg(@PathVariable("id") String id,Model model) {
		model.addAttribute("pdid", id);
		return "process/show";
	}
	
	@ResponseBody
	@RequestMapping("/upload")
	public Object upload(HttpServletRequest rq) {
		start();
		
		try {
			MultipartHttpServletRequest request = (MultipartHttpServletRequest) rq;
			
			MultipartFile file = request.getFile("procDefFile");
			
//			String name = file.getName();
//			String filename = file.getOriginalFilename();
//			System.out.println("name:"+name);
//			System.out.println("filename:"+filename);
			
			//在逻辑方法中，调用restTemplate对象，将文件上传给远程web服务
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			String uuid = UUID.randomUUID().toString();
			String fileName = file.getOriginalFilename();
			final File tempFile = File.createTempFile(uuid, fileName.substring(fileName.lastIndexOf(".")));
			//文件复制
			file.transferTo(tempFile);
			
		    FileSystemResource resource = new FileSystemResource(tempFile);  
		    MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();  
		    param.add("pdfile", resource);
		    restTemplate.postForObject("http://eureka-activiti-service/deployProcDef",param,String.class);

			
			success();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		return end();
	}
	
	@ResponseBody
	@RequestMapping("/pageQuery")
	public Object pageQuery(Integer pageno,Integer pagesize) {
		
		start();
		
		try {
			
			int count = processService.pageQueryProcDefCount();
			
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("pageno", pageno);
			paramMap.put("pagesize", pagesize);
			
			List<Map<String, Object>> pdMaps = processService.pageQueryProcDefData( paramMap );
			
			Page<Map<String, Object>> pdPage = new Page<Map<String, Object>>();
			pdPage.setTotalsize(count);
			pdPage.setPageno(pageno);
			pdPage.setPagesize(pagesize);
			pdPage.setDatas(pdMaps);
			data(pdPage);
			success();
		} catch ( Exception e ) {
			e.printStackTrace();
			fail();
		}
		
		return end();
		
	}
	
}
