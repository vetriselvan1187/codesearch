
package com.codesearch;

import java.util.Map;
import java.util.HashMap;
import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import java.util.stream.Collectors;
import org.json.JSONObject;
import org.json.JSONException;

public class RepositoryCache {

	private Map<String, String> repositoryCache = new HashMap<String, String>();
	private List<RepositoryObject> repositoryList = new ArrayList<RepositoryObject>();
	private List<String> filePaths;

	public RepositoryCache(List<String> filePaths) {
		this.filePaths = filePaths;	
	}

	public void load() throws IOException {
		for(String path : filePaths) {
			String content = loadFile(path);
			repositoryCache.put(path, content);
			repositoryList.add(new RepositoryObject(path, content));
		}
	}

	public Map<String, List<String>> search(String word, String type) throws IOException {
		Map<String, List<String>> matches = new ConcurrentHashMap<>();
		List<RepositoryObject> pathList = repositoryList
				.parallelStream()
				.filter(r -> r.has(word))
				.collect(Collectors.toList());


		Pattern pattern = null;
		if(type.equals("function")) {
				String functionRegex = "[\\w\\*[^\\S\\r\\n]]*?"+word+"[\\w]*?\\([\\w\\s,\\*]*?\\)[\\s]*?\\{";
				pattern = Pattern.compile(functionRegex, Pattern.MULTILINE);	
		}
		if(type.equals("parameter")) {
				String paramRegex = "\\([.[^\\(\\)]]*?"+word+"[.[^\\(]]*?\\)";
				pattern  = Pattern.compile(paramRegex, Pattern.MULTILINE);
		}
		if(pattern != null) {
			for(RepositoryObject object : pathList) {
				object.setPattern(pattern);
			}
		}

		pathList.parallelStream().forEach(o -> {
					try {
						List<String> matchlist = o.getMatches(word, type);
						if(!matchlist.isEmpty()) {
							matches.put(o.getPath(), matchlist);
						}
					} catch(IOException ex) {

					}
		});
		return matches;
	}

	private String loadFile(String filePath) throws IOException {
		BufferedReader in = null;
		StringBuilder fileContent = new StringBuilder();
		try {	
			in = new BufferedReader(new FileReader(filePath));
			String line = null;
			while((line = in.readLine()) != null) {
				fileContent.append(line);
				fileContent.append("\r");
				fileContent.append("\n");
			}
		} finally {
			in.close();
		}
		return fileContent.toString();
	}

	public long getSize() {
		long cacheSize = 0;
		for(String filePath : repositoryCache.keySet()) {
			String fileContent = repositoryCache.get(filePath);
			cacheSize += fileContent.length();
		}
		return cacheSize;
	}
}

final class RepositoryObject {

	private String path;
	private String content;
	private Pattern pattern;


	public RepositoryObject(String path, String content) {
			this.path = path;
			this.content = content;
	}

	public String getPath() {
			return this.path;
	}

	public String getContent() {
			return this.content;
	}


	public void setPattern(Pattern pattern) {
			this.pattern = pattern;
	}

	public Pattern getPattern() {
			return this.pattern;
	}

	List<String> getMatches(String word, String type) throws IOException {
			List<String> result;
			switch(type) {
				case "function":
					result = getFunctionMatches(word);
					break;
				case "parameter":
					result = getParamMatches(word);
					break;
				default:
					result = getMatches(word);
			}
			return result;
	}

	List<String> getParamMatches(String word) {		
			List<String> list = new ArrayList<String>();
			Matcher matcher = pattern.matcher(this.content);
			while(matcher.find()) {
					list.add(matcher.group());
			}
			return list;
	}

	List<String> getFunctionMatches(String word)  {
			List<String> list = new ArrayList<String>();
			Matcher matcher = pattern.matcher(this.content);
			while(matcher.find()) {
					list.add(matcher.group());
			}
			return list;
	}

	public List<String> getMatches(String word) throws IOException {
			List<String> matches = new ArrayList<>();
 			BufferedReader br = new BufferedReader(new StringReader(this.content));
			String line = null;
			while((line = br.readLine()) != null) {
					if(line.contains(word)) {
							matches.add(line);
					}
			}
			return matches;
	}

	public boolean has(String word) {
			return content.contains(word);
	}
}

