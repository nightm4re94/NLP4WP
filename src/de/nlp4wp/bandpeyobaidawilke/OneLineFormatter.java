package de.nlp4wp.bandpeyobaidawilke;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class OneLineFormatter extends Formatter {

	private static final String PATTERN = "yyyy-MM-dd'|'HH:mm:ss";

	@Override
	public String format(final LogRecord record) {
		return String.format("%1$s %2$-7s %3$s\n", new SimpleDateFormat(PATTERN).format(new Date(record.getMillis())),
				record.getLevel().getName(), this.formatMessage(record));
	}
}