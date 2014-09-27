package pb.db;

import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

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
			List<Measurement> measures = getAllMeasuresFromDb(em, username);

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
					em, username);

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

	private long getUserRecordsCountFromDb(EntityManager em, String username) {
		Query q = em.createNamedQuery("getMeasuresCount");
		q.setParameter("username", username);
		long userRecords = (Long) q.getSingleResult();
		return userRecords;
	}

	private Users findUserByUsernameFromDb(EntityManager em, String username) {
		Users user = em.find(Users.class, username);
		return user;
	}

	private Measurement findMeasureByIdFromDb(EntityManager em, String measureId) {
		Measurement measure = em.find(Measurement.class,
				Long.parseLong(measureId));
		return measure;
	}

	@SuppressWarnings("unchecked")
	private List<Measurement> getAllMeasuresFromDb(EntityManager em,
			String username) {
		Query q = em.createNamedQuery("findAllMeasuresByUsername");
		q.setParameter("username", username);
		q.setMaxResults(maxRecords);
		List<Measurement> measures = q.getResultList();
		return measures;
	}

	@SuppressWarnings("unchecked")
	private List<Measurement> getAllMeasuresForDataVisualizationFromDb(
			EntityManager em, String username) {
		Query q = em.createNamedQuery("findAllMeasuresForDataVisualization");
		q.setParameter("username", username);
		q.setMaxResults(maxRecords);
		List<Measurement> measures = q.getResultList();
		return measures;
	}

	private void addMeasureToDb(EntityManager em, Measurement measure) {
		EntityTransaction et = em.getTransaction();
		try {
			et.begin();
			em.persist(measure);
			et.commit();
		} finally {
			if (et.isActive()) {
				et.rollback();
			}
		}
	}

	private void deleteMeasureByUsernameFromDb(EntityManager em,
			Measurement measure, String username) {
		if (measure.belongsToUser(username)) {
			EntityTransaction et = em.getTransaction();
			try {
				et.begin();
				em.remove(measure);
				et.commit();
			} finally {
				if (et.isActive()) {
					et.rollback();
				}
			}
		}
	}

}
