package com.dsw.filetrans.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.dsw.filetrans.Constants;
import com.dsw.filetrans.model.TaskModel;
import com.dsw.filetrans.util.JsonUtil;

/**
 * 文件Controller，用于windows和Linux之间传输数据（主要调用上传接口)
 * @author dsw
 *
 */
@Controller
@RequestMapping("/FileController")
public class FileController {
	private Logger logger = LogManager.getLogger(FileController.class);
	
	@RequestMapping("/springUpload")
	public String springUpload(HttpServletRequest request) throws Exception {
		long startTime = System.currentTimeMillis();
		String taskJson = request.getHeader(Constants.HTTP_HEAD_TASKMODEL);
		TaskModel taskModel = JsonUtil.json2Object(taskJson, TaskModel.class);
		String targetPlace = taskModel.getToPath();
		if (targetPlace.lastIndexOf(File.separator) == -1) {
			targetPlace = targetPlace + File.separator;
		}
		
		// 将当前上下文初始化给 CommonsMutipartResolver （多部分解析器）
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		// 检查form中是否有enctype="multipart/form-data"
		if (multipartResolver.isMultipart(request)) {
			// 将request变成多部分request
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			// 获取multiRequest 中所有的文件名
			Iterator<String> iter = multiRequest.getFileNames();

			while (iter.hasNext()) {
				// 一次遍历所有文件
				MultipartFile file = multiRequest.getFile(iter.next().toString());
				if (file != null) {
					String path = targetPlace + file.getOriginalFilename();
					// 上传
					file.transferTo(new File(path));
				}
			}
		}
		long endTime = System.currentTimeMillis();
		logger.info("方法三的运行时间：" + String.valueOf(endTime - startTime) + "ms");
		return "/success";
	}

	/**
	 * 文件下载
	 * 
	 * @Description:
	 * @param fileName
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/download")
	@ResponseBody
	public void downloadFile(@RequestParam("fileName") String fileName, HttpServletRequest request,
			HttpServletResponse response) {
		if (fileName != null) {
			String realPath = "C:/";//request.getServletContext().getRealPath("WEB-INF/File/");
			File file = new File(realPath, fileName);
			if (file.exists()) {
				response.setContentType("application/force-download");// 设置强制下载不打开
				//文件大小有限制，所以采用header中，采用字符串的方式传递文件大小
				//response.setContentLength((int)file.length());
				response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);// 设置文件名
				response.addHeader("filesize", file.length()+ "");
				byte[] buffer = new byte[1024];
				FileInputStream fis = null;
				BufferedInputStream bis = null;
				try {
					fis = new FileInputStream(file);
					bis = new BufferedInputStream(fis);
					OutputStream os = response.getOutputStream();
					int i = bis.read(buffer);
					while (i != -1) {
						os.write(buffer, 0, i);
						i = bis.read(buffer);
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (bis != null) {
						try {
							bis.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if (fis != null) {
						try {
							fis.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
}
