
package com.codesearch;


import java.io.PrintWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class CodeSearchServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {
		String word = request.getParameter("word");
		String type = request.getParameter("type");
		type = (type == null) ? "default" : type;
		long starttime = System.currentTimeMillis();
		RepositoryCache cache = CodeSearchInitializer.getInstance().getRepositoryCache();
		Map<String, List<String>> matches = cache.search(word, type);
		System.out.println(System.currentTimeMillis()-starttime);
		response.setContentType("application/json");
		response.setStatus(HttpServletResponse.SC_OK);
		PrintWriter writer = response.getWriter();
		writer.print(matches);
		writer.flush();
	}

}
