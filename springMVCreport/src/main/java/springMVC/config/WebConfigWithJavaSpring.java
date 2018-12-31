package springMVC.config;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;

import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class WebConfigWithJavaSpring implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext container) {
		// Create the 'root' Spring application context
		// i think: with .xml config, use XmlWebApplicationContext. with java config,
		// use AnnotationConfigWebApplicationContext
		 AnnotationConfigWebApplicationContext rootContext = new
		 AnnotationConfigWebApplicationContext();
		// Manage the lifecycle of the root application context
		 rootContext.setServletContext(container);	
		rootContext.register(BeanConfig.class);
		container.addListener(new ContextLoaderListener(rootContext));
		container.addListener(new RequestContextListener());

		/**
		 * Create the dispatcher servlet's Spring application context official:Like
		 * generic application contexts, web application contexts are hierarchical.
		 * There is a single root context per application, while each servlet in the
		 * application (including a dispatcher servlet in the MVC framework) has its own
		 * child vcontext.
		 **/
		 AnnotationConfigWebApplicationContext dispatcherServlet = new
		 AnnotationConfigWebApplicationContext();
		 dispatcherServlet.register(DispatcherConfigWithJavaSpring.class);
		// Register and map the dispatcher servlet
		ServletRegistration.Dynamic dispatcher = container.addServlet("dispatcher",
				new DispatcherServlet(dispatcherServlet));
		dispatcher.setMultipartConfig(getMultipartConfigElement());
		dispatcher.setLoadOnStartup(1);
		String[] mappingArray= {"/com/formDemo/*"};
		dispatcher.addMapping(mappingArray);

	}

	private MultipartConfigElement getMultipartConfigElement() {
		MultipartConfigElement multipartConfigElement = new MultipartConfigElement("");
//		LOCATION, MAX_FILE_SIZE,
//		MAX_REQUEST_SIZE, FILE_SIZE_THRESHOLD
		return multipartConfigElement;
	}

	private static final String LOCATION = "C:/temp/"; // Temporary location where files will be stored

	private static final long MAX_FILE_SIZE = 5242880; // 5MB : Max file size.
														// Beyond that size spring will throw exception.
	private static final long MAX_REQUEST_SIZE = 20971520; // 20MB : Total request size containing Multi part.

	private static final int FILE_SIZE_THRESHOLD = 0; // Size threshold after which files will be written to disk

}