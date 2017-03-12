package de.nlp4wp.bandpeyobaidawilke;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import de.nlp4wp.bandpeyobaidawilke.xmltypes.Log;

public class IDFXReader {

	public Log getMarshalledLog(final String filePath) {
		final File file = new File(filePath);
		this.preprocessFile(file);
		try {
			final JAXBContext jaxbContext = JAXBContext.newInstance(Log.class);
			final Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			final Log log = (Log) jaxbUnmarshaller.unmarshal(file);
			log.setFilePath(filePath);
			return log;
		} catch (final Exception e) {
			Program.logException(e);
			return null;
		}
	}

	// delete characters known to be illegal in XML (the "&" character is
	// illegal and needs to be escaped)
	private void preprocessFile(final File file) {
		String fileContent = "";
		Path path = Paths.get(file.getPath());
		Charset charset = StandardCharsets.UTF_8;
		try {
			fileContent = new String(Files.readAllBytes(path), charset);
			fileContent = fileContent.replaceAll("&#", "&amp;#");
			PrintWriter writer = new PrintWriter(file, "UTF-8");
			writer.write(fileContent);
			writer.close();

		} catch (IOException e) {
			Program.logException(e);
		} finally {
		}

	}

}
