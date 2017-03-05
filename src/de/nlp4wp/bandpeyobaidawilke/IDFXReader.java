package de.nlp4wp.bandpeyobaidawilke;

import java.io.File;
import java.util.logging.Level;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import de.nlp4wp.bandpeyobaidawilke.xmltypes.Log;

public class IDFXReader {

  public Log getMarshalledLog(final String filePath) {
    try {
      final File file = new File(filePath);
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
}
