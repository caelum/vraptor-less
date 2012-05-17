package br.com.caelum.vraptor.less;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;

import org.apache.commons.io.FileUtils;

import com.yahoo.platform.yui.compressor.CssCompressor;

public class OurCssCompressor implements Compressor {

	public void compress(File css) throws IOException {
	    System.out.print("[yui] comprimindo " + css + "...");
	    String conteudo = FileUtils.readFileToString(css, "UTF-8").replace("\\n", "\n");
	    FileWriter writer  = new FileWriter(css);
	    new CssCompressor(new StringReader(conteudo)).compress(writer, -1);
	    writer.close();
	    System.out.println("[yui] ok");
	}

}
