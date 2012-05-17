package br.com.caelum.vraptor.less;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.asual.lesscss.LessEngine;
import com.asual.lesscss.LessException;

public class OurLessCompressor implements Compressor {

	private final File cssDir;

	public OurLessCompressor(File cssDir) {
		this.cssDir = cssDir;
	}

	@Override
	public void compress(File less) throws IOException, LessException {
		System.out.print("[less] compilando " + less + "...");
		String compiled = new LessEngine().compile(less).replace("\\n", "");
		String cssName = less.getName().replaceFirst("less$", "css");
		FileUtils.write(new File(cssDir, cssName), compiled);
		System.out.println("ok");
	}

}
