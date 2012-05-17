package br.com.caelum.vraptor.less;

import static org.apache.commons.io.filefilter.FileFilterUtils.and;
import static org.apache.commons.io.filefilter.FileFilterUtils.notFileFilter;
import static org.apache.commons.io.filefilter.FileFilterUtils.suffixFileFilter;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import org.apache.commons.io.filefilter.IOFileFilter;

import com.asual.lesscss.LessException;

public class LessProcessor {
	public static void main(String[] args) throws LessException, IOException {
		String base = args[0];
		
		File cssDir = new File(base + "/css");
		File jsDir = new File(base + "/js");

		compress(cssDir, lessFilter(), new OurLessCompressor(cssDir));
		new File(cssDir, "defs.css").delete();

		boolean shouldCompress = (args.length > 1 && args[1] == "--compress");
		if (shouldCompress) {
			compress(cssDir, cssFilter(), new OurCssCompressor());
			compress(jsDir, jsFilter(), new OurJsCompressor());
		}
	}

	private static void compress(File dir, FilenameFilter filterCssFiles,
			Compressor compressor) throws IOException, LessException {
		FilenameFilter cssFilter = cssFilter();
		for (File file : dir.listFiles(cssFilter)) {
			compressor.compress(file);
		}
	}

	private static IOFileFilter jsFilter() {
		return and(suffixFileFilter(".js"),
				notFileFilter(suffixFileFilter("min.js")));
	}

	private static FilenameFilter cssFilter() {
		return (FilenameFilter) suffixFileFilter(".css");
	}

	private static FilenameFilter lessFilter() {
		return (FilenameFilter) suffixFileFilter(".less");
	}
}