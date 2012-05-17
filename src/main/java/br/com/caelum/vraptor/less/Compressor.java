package br.com.caelum.vraptor.less;

import java.io.File;
import java.io.IOException;

import com.asual.lesscss.LessException;

public interface Compressor {

	public void compress(File file) throws IOException, LessException;
}
