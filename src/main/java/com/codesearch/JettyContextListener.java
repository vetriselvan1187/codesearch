

package com.codesearch;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;


import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;


@WebListener
public class JettyContextListener implements ServletContextListener {

	private static final String REPOSITORY_PATH = "repository_path";

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent){
		System.out.println("Starting UP");
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		String configPath = servletContextEvent.getServletContext().getRealPath("/WEB-INF/codesearch.properties");
		System.out.println("codesearch configpath = "+configPath);
		CodeSearchInitializer.getInstance(configPath);
	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		System.out.println("Shutting down!");
	}

}
