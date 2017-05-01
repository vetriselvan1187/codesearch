
package com.codesearch;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;


public class Directory {

	private String dirPath;
	private List<String> filePathList = new ArrayList<String>();

	public Directory(String dirPath) {
		this.dirPath = dirPath;
	}

	public void load() throws IOException {
		File file = new File(this.dirPath);
		if(!file.isDirectory()) {
			throw new IOException("Invalid Directory");

		}
		loadFilePaths(this.dirPath);

	}

	public void loadFilePaths(String path) {
		File root = new File(path);
		File[] list = root.listFiles();

		for(File file : list) {
			if(file.isDirectory()) {
				loadFilePaths(file.getAbsolutePath());
				System.out.println("Dir: "+file.getAbsoluteFile());
			} else {
				filePathList.add(file.getAbsolutePath());
			}
		}
	}

	public List<String> getFilepaths() {
		return filePathList;
	}
}
