package pb.db;

import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import pb.model.Measurement;
import pb.model.Users;
import pb.validator.MeasurementValidator;
import pb.validator.UserValidator;

public class MeasureDAO extends BaseDAO {

	private static final Logger LOGGER = Logger.getLogger(MeasureDAO.class
			.getName());

	private final EntityManagerFactory emf;

	private final int maxRecords;

	public MeasureDAO(EntityManagerFactory emf, int maxRecords) {
		this.emf = emf;
		this.maxRecords = maxRecords;
	}

	public List<Measurement> getAllMeasuresForUser(String username) {
		UserValidator.validateUsername(username);
		EntityManager em = emf.createEntityManager();
		try {
			List<Measurement> measures = getAllMeasuresFromDb(em, username,
					maxRecords);

			info(LOGGER, "[" + username + "] " + measures.size()
					+ " retrieved from db");

			return measures;
		} finally {
			em.close();
		}
	}

	public List<Measurement> getAllMeasuresForDataVisualization(String username) {
		UserValidator.validateUsername(username);
		EntityManager em = emf.createEntityManager();
		try {
			List<Measurement> measures = getAllMeasuresForDataVisualizationFromDb(
					em, username, maxRecords);

			info(LOGGER, "[" + username + "] " + measures.size()
					+ " retireved from db");

			return measures;
		} finally {
			em.close();
		}
	}

	public boolean addMeasureForUser(Measurement measure, String username) {
		MeasurementValidator.validateMeasure(measure);
		UserValidator.validateUsername(username);
		EntityManager em = emf.createEntityManager();
		try {
			long userRecords = getUserRecordsCountFromDb(em, username);

			info(LOGGER, "[" + username + "] " + "has " + userRecords
					+ " records in db");

			if (userRecords <= maxRecords) {
				Users user = findUserByUsernameFromDb(em, username);
				if (user != null) {
					measure.attachUser(user);

					addMeasureToDb(em, measure);

					info(LOGGER, "[" + username + "] " + "Measure " + measure
							+ " successfully added to db");

					return true;
				}
			} else {
				info(LOGGER, "[" + username + "] "
						+ "has reached the maximum allowed number of "
						+ maxRecords + " measures");
			}
		} finally {
			em.close();
		}
		return false;
	}

	public boolean deleteMeasure(String measureId) {
		MeasurementValidator.validateMeasureId(measureId);
		EntityManager em = emf.createEntityManager();
		try {
			Measurement measure = findMeasureByIdFromDb(em, measureId);
			if (measure != null) {
				String username = measure.getUsername();

				deleteMeasureByUsernameFromDb(em, measure, username);

				info(LOGGER, "[" + username + "] " + "Measure " + measure
						+ " successfully deleted from db");

				return true;
			}
		} finally {
			em.close();
		}
		return false;
	}
}
