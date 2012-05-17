package br.com.caelum.vraptor.less;

import java.io.File;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.caelum.vraptor.core.DefaultStaticContentHandler;
import br.com.caelum.vraptor.core.StaticContentHandler;

import com.asual.lesscss.LessEngine;
import com.asual.lesscss.LessException;

/**
 * Receives a css request and redirects to a packaged .less file if the css file
 * does not exist.
 * 
 * @author unknown
 * @author guilherme silveira
 */
@WebFilter(urlPatterns="*.css", displayName="less2css")
public class LesscssFilter implements Filter {

	private static final Logger logger = LoggerFactory
			.getLogger(LesscssFilter.class);

	private ServletContext context;
	private StaticContentHandler handler;

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		if (handler.requestingStaticFile(request)) {
			// logger.info("Ignoring {}", request.getRequestURI());
			chain.doFilter(request, response);
			return;
		}

		String path = context.getRealPath(path(request));
		File file = new File(path);
		if (!file.exists()) {
			logger.info("CSS does not exist {}", request.getRequestURI());
			chain.doFilter(request, response);
			return;
		}

		if (request.getDateHeader("If-Modified-Since") == file.lastModified()) {
			response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
			return;
		}

		logger.info("Processing {}", file);
		LessEngine engine = new LessEngine();
		try {
			String compiled = engine.compile(file).replace("\\n", "\n");
			response.setDateHeader("Last-Modified", file.lastModified());
			response.setContentType("text/css");
			response.getWriter().write(compiled);
		} catch (LessException e) {
			throw new ServletException(e);
		}
	}

	@Override
	public void destroy() {
	}

	private String path(HttpServletRequest request) {
		String uri = request.getRequestURI();
		String noContext = uri.replaceFirst(request.getContextPath(), "");
		return noContext.replaceFirst("css$", "less");
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		context = config.getServletContext();
		handler = new DefaultStaticContentHandler(context);
	}
}
