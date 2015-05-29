package pb.db;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import pb.model.Hand;
import pb.model.Measurement;
import pb.model.Users;

public class BaseDAO {

	private static final Logger LOGGER = Logger.getLogger(BaseDAO.class
			.getName());

	private static final String DATE_PATTERN = "dd/MM/yyyy HH:mm";

	private static final SimpleDateFormat FORMATTER = new SimpleDateFormat(
			DATE_PATTERN);

	// Measures

	protected long getUserRecordsCountFromDb(EntityManager em, String username) {
		Query q = em.createNamedQuery("getMeasuresCount");
		q.setParameter("username", username);
		long userRecords = (Long) q.getSingleResult();
		return userRecords;
	}

	protected Users findUserByUsernameFromDb(EntityManager em, String username) {
		Users user = em.find(Users.class, username);
		return user;
	}

	protected Measurement findMeasureByIdFromDb(EntityManager em,
			String measureId) {
		Measurement measure = em.find(Measurement.class,
				Long.parseLong(measureId));
		return measure;
	}

	@SuppressWarnings("unchecked")
	protected List<Measurement> getAllMeasuresFromDb(EntityManager em,
			String username, int maxRecords) {
		Query q = em.createNamedQuery("findAllMeasuresByUsername");
		q.setParameter("username", username);
		q.setMaxResults(maxRecords);
		List<Measurement> measures = q.getResultList();
		return measures;
	}

	protected void addMeasureToDb(EntityManager em, Measurement measure) {
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

	protected void updateMeasurePropertyInDb(EntityManager em,
			Measurement measure, String measureProperty, String value) {
		EntityTransaction et = em.getTransaction();
		try {
			et.begin();
			switch (measureProperty) {
			case Measurement.SBP:
				measure.getPressureBlood().setSbp(Integer.parseInt(value));
				break;
			case Measurement.DBP:
				measure.getPressureBlood().setDbp(Integer.parseInt(value));
				break;
			case Measurement.HAND:
				measure.setHand(Hand.valueOf(value));
				break;
			case Measurement.PULSE:
				measure.setPulse(Integer.parseInt(value));
				break;
			case Measurement.DATETIME: {
				try {
					Date datetime = FORMATTER.parse(value);
					measure.setDatetime(datetime);
				} catch (ParseException e) {
					error(LOGGER, e.getMessage());
				}
			}
				break;
			default:
				error(LOGGER, "Measure property " + measureProperty
						+ " must be one of [" + Measurement.SBP + ", "
						+ Measurement.DBP + ", " + Measurement.HAND + ", "
						+ Measurement.PULSE + ", " + Measurement.DATETIME
						+ "] property values");
				break;
			}
			et.commit();
		} finally {
			if (et.isActive()) {
				et.rollback();
			}
		}
	}

	protected void deleteMeasureByUsernameFromDb(EntityManager em,
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

	// User registration

	protected boolean userExistsInDb(EntityManager em, String username) {
		boolean userExists = em.find(Users.class, username) != null;
		return userExists;
	}

	protected void registerUserToDb(EntityManager em, Users user) {
		EntityTransaction et = em.getTransaction();
		try {
			et.begin();
			em.persist(user);
			et.commit();
		} finally {
			if (et.isActive()) {
				et.rollback();
			}
		}
	}

	// Logging

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
