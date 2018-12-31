package com.formDemo.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.MultipartFile;

@Controller
//要注意的是--dispatcher servlet的設定mapping路徑會被加在符合的handler mapping之前
@RequestMapping("/handler/DemoHandler")
@MultipartConfig
public class DemoHandler implements ServletContextAware {
	private Log logger = LogFactory.getLog(DemoHandler.class);
	private static final String DEFAULT_SUB_FOLDER_FORMAT_AUTO = "yyyyMMdd";
	/** ~~~ 这里扩充一下格式，防止手动建立的不统一 */
	private static final String DEFAULT_SUB_FOLDER_FORMAT_NO_AUTO = "yyyy-MM-dd";
	@Autowired
	ServletContext context;

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.context = servletContext;
	}

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = { "/getFormData.do" })
	public String getFormData(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			@RequestParam("name") String name, @RequestParam("job") String job)
			throws IOException, ServletException {
		System.out.println("hi hi hi getFormData");
		Map<String, String> errorMap = new HashMap<String, String>();
		if (job.trim().equals("") || name.trim().equals("")) {
			errorMap.put("formError", "fill the form please");
		}
		if (!errorMap.isEmpty()) {
			model.put("errorMap", errorMap);
			return "/demoForm";
		}
//		processUpload(model, request, response, file);
		model.put("myName", name);
		model.put("myJob", job);
		model.put("presentStuff", "OK MAN OK");
//		RequestDispatcher dispatcher = request.getRequestDispatcher("/demoView/demoPresent");
//		dispatcher.forward(request, response);
		return "/demoPresent";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/getImage.do")
	public void getImage(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws IOException {
		OutputStream out = response.getOutputStream();
		try {
			String name = request.getParameter("name");
			String directoryName = "C:/demoReportImg/" + name+"/";
			File nameFolder = new File("C:/demoReportImg/" + name);
			String[] contentName = nameFolder.list();
			String imgName = contentName[0];
			File file = new File(directoryName + imgName);
			byte[] buf = new byte[8192];
			InputStream is = new FileInputStream(file);
			int i = 0;
			while ((i = is.read(buf, 0, buf.length)) > 0) {
				out.write(buf, 0, i);
				out.flush();
			} // end while
		} catch (Exception e) {
			e.printStackTrace();
		} // end catch
	}// end method

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/processUpload.do")
	public void processUpload(ModelMap modelMap, HttpServletRequest request, HttpServletResponse response,
			@RequestParam("file") MultipartFile file) {
		String name = request.getParameter("name");
		processUploadPost(modelMap, request, response, name, file);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/processUploadPost.do")
	public void processUploadPost(ModelMap modelMap, HttpServletRequest request, HttpServletResponse response,
			@RequestParam("name") String name, @RequestParam("file") MultipartFile file) {

		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (!isMultipart) {
			return;
		}
		// 获取目录
		File folder = buildFolder(request, name);
		if (null == folder) {
			return;
		}
		if (folder.list().length > 0) {
			try {
				FileUtils.cleanDirectory(folder);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
//			Part bImagePart = request.getPart("file");
//			getFileName(bImagePart);
			if (null == file || file.getSize() == 0) {
				logger.error("getPart獨不到圖片");
				return;
			}
			String fileClientName = file.getOriginalFilename();
			String fileFix = StringUtils.substring(fileClientName, fileClientName.lastIndexOf(".") + 1);
			if (!StringUtils.equalsIgnoreCase(fileFix, "jpg") && !StringUtils.equalsIgnoreCase(fileFix, "jpeg")
					&& !StringUtils.equalsIgnoreCase(fileFix, "bmp") && !StringUtils.equalsIgnoreCase(fileFix, "gif")
					&& !StringUtils.equalsIgnoreCase(fileFix, "png")) {
				logger.error("上传文件的格式错误=" + fileFix);
				return;
			}
			if (logger.isInfoEnabled()) {
				logger.info("开始上传文件:" + fileClientName);
			}
			// 为了客户端已经设置好了图片名称在服务器继续能够明确识别，这里不改名称
			File newfile = new File(folder, fileClientName);
			InputStream fileInputStream = file.getInputStream();
			FileOutputStream fileOutputStream = new FileOutputStream(newfile);
			try {
				// "/image/order/wormenSniper.jpg"
				byte[] buf = new byte[8192];
				int c = 0;
				while ((c = fileInputStream.read(buf, 0, buf.length)) > 0) {
					fileOutputStream.write(buf, 0, c);
					fileOutputStream.flush();
				} // end while
			} catch (Exception e) {
				e.printStackTrace();
			} // end catch

			if (logger.isInfoEnabled()) {
				logger.info("上传文件结束，新名称:" + fileClientName + ".floder:" + newfile.getPath());
			}
			response.setContentType("text/html; charset=UTF-8");
			response.setHeader("Cache-Control", "no-cache");
			PrintWriter out = response.getWriter();
			// 上传文件的返回地址
			String fileUrl = "";
			// 组装返回url，以便于ckeditor定位图片，要注意瀏覽器與server對路徑的識別
			// fileURL是給瀏覽器看的路徑
			fileUrl = "C:/demoReportImg/" + "/" + folder.getName() + "/" + newfile.getName();
			fileUrl = StringUtils.replace(fileUrl, "//", "/");
			out.flush();
			out.close();
		} catch (IOException e) {
			logger.error("上传文件发生异常！", e);
		} catch (Exception e) {
			logger.error("上传文件发生异常！", e);
		}
	}

	private String getFileName(final Part part) {
		for (String content : part.getHeader("content-disposition").split(";")) {
			if (content.trim().startsWith("filename")) {
				return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
			}
		}
		return null;
	}

	private File buildFolder(HttpServletRequest request, String name) {
		// 在控件中無法正常操作
		// 要注意瀏覽器與server對路徑的識別
		// realPath是給server看的路徑
		String realPath = "c:\\demoReportImg\\";
		// FOR_CUS_LOAD_DIR+

		logger.info(realPath);
		File firstFolder = new File(realPath);
		if (!firstFolder.exists()) {
			// i think : 要有"s"
			if (!firstFolder.mkdirs()) {
				logger.error("fail to create" + firstFolder);
				return null;
			}
		}
		String folderdir = realPath + name;
		if (StringUtils.isBlank(folderdir)) {
			logger.error("路径错误:" + folderdir);
			return null;
		}
		File floder = new File(folderdir);
		if (!floder.exists()) {
			// i think : 要有"s"
			if (!floder.mkdirs()) {
				logger.error("创建文件夹出错！path=" + folderdir);
				return null;
			}
		}
		return floder;
	}
}
