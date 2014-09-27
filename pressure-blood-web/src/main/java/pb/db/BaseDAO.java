package pb.db;

import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import pb.model.Measurement;
import pb.model.Users;

public class BaseDAO {

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

	@SuppressWarnings("unchecked")
	protected List<Measurement> getAllMeasuresForDataVisualizationFromDb(
			EntityManager em, String username, int maxRecords) {
		Query q = em.createNamedQuery("findAllMeasuresForDataVisualization");
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
