package com.example.demo.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;

import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/")
public class UploadController {

	@ApiOperation(value="首页")
	@RequestMapping("/")
	public String index() {
		return "index";
	}

	@ApiOperation(value="上传图片")
	@ResponseBody
	@RequestMapping(value="upload",method=RequestMethod.POST)
	public JSONObject upload(@RequestParam("fileName") MultipartFile[] fileName, HttpServletRequest request) {

		JSONObject obj = new JSONObject();

		ArrayList<String> list=new ArrayList<String>();

		try {

			for (MultipartFile multipartFile : fileName) {
				String newFileName = multipartFile.getOriginalFilename();
				String filePath = "D:\\demo\\" + newFileName;
				multipartFile.transferTo(new File(filePath));
				
				String viewImagePath = "/image/"+newFileName;
				list.add(viewImagePath);
			}

			String[] str = new String[list.size()];
			String[] img = list.toArray(str);
			obj.put("errno", 0);
			obj.put("data", img);
			
		} catch (Exception e) {

			obj.put("errno", -1);
			obj.put("data", "上传失败!");

			e.printStackTrace();
		}

		return obj;
	}

	
	@ApiOperation(value="查看一个图片",notes="通过一个需要访问的图片名称")
	@RequestMapping(value="/image/{fileName}",method=RequestMethod.GET)
	public void visitImg(@PathVariable("fileName") String fileName, HttpServletResponse response) {
		FileInputStream fis = null;
		StringBuffer sb = new StringBuffer(20);
		try {
			sb.append("D:\\demo\\");

			String imgPath = sb.toString() + fileName;

			ServletOutputStream os = response.getOutputStream();
			fis = new FileInputStream(imgPath);
			byte[] bs = new byte[fis.available()];
			fis.read(bs);
			os.write(bs);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fis != null)
					fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
