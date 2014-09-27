package pb.db;

import java.util.logging.Logger;

public class BaseDAO {

	protected void info(Logger logger, String message) {
		logger.info(message);
	}

	protected void warning(Logger logger, String message) {
		logger.warning(message);
	}

	protected void error(Logger logger, String message) {
		logger.severe(message);
	}
}
