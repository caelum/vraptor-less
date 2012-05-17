package br.com.caelum.vraptor.less;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;

import org.apache.commons.io.FileUtils;
import org.mozilla.javascript.tools.ToolErrorReporter;

import com.yahoo.platform.yui.compressor.JavaScriptCompressor;

public class OurJsCompressor implements Compressor {

	@Override
	public void compress(File js) throws IOException {
		System.out.print("[yui] comprimindo " + js + "...");
		String conteudo = FileUtils.readFileToString(js, "UTF-8");
		FileWriter writer = new FileWriter(js);
		new JavaScriptCompressor(new StringReader(conteudo),
				new ToolErrorReporter(false, System.out)).compress(writer, -1,
				true, false, false, false);
		writer.close();
		System.out.println("[yui] ok");
	}

}
