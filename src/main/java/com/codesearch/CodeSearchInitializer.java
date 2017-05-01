

package com.codesearch;

import java.util.List;
import java.util.Properties;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class CodeSearchInitializer {
	
	private static final String REPOSITORY_PATH = "repository_path";
	private List<String> filePaths;
	private static CodeSearchInitializer initializer = null;
	private RepositoryCache cache;

	private CodeSearchInitializer() {

	}

	public RepositoryCache getRepositoryCache() {
		return this.cache;
	}

	public static CodeSearchInitializer getInstance() {
		return initializer;
	}

	public static synchronized CodeSearchInitializer getInstance(String configPath) {
		if(initializer == null) {
			initializer  = new CodeSearchInitializer();
			initializer.load(configPath);
		}
		return initializer;
	}

	private void load(String configPath) {
		InputStream in = null;
		try {
			in = new FileInputStream(configPath);
			Properties properties = new Properties();
			properties.load(in);
			Directory dir = new Directory(properties.getProperty(REPOSITORY_PATH));
			dir.load();
			cache = new RepositoryCache(dir.getFilepaths());
			cache.load();
		} catch(IOException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if(in != null) {
					in.close();
				}
			} catch(IOException ex) {

			}
		}
	}

	public List<String> getFilePaths() {
		return filePaths;

	}
}
