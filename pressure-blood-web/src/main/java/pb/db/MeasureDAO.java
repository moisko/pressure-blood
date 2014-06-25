package pb.db;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import pb.model.Measurement;
import pb.model.Users;
import pb.validator.MeasurementValidator;
import pb.validator.UserValidator;

public class MeasureDAO {

	private final EntityManagerFactory emf;

	public MeasureDAO(EntityManagerFactory emf) {
		this.emf = emf;
	}

	public List<Measurement> getAllMeasuresForUserWithUsername(String username) {
		UserValidator.validateUsername(username);
		EntityManager em = emf.createEntityManager();
		try {
			List<Measurement> measures = getAllMeasuresForUserWithUsernameFromDb(
					em, username);
			return measures;
		} finally {
			em.close();
		}
	}

	public void addMeasureForUserWithUsername(Measurement measure,
			String username) {
		MeasurementValidator.validateMeasure(measure);
		UserValidator.validateUsername(username);
		EntityManager em = emf.createEntityManager();
		try {
			Users user = findUserByUsernameFromDb(em, username);
			if (user != null) {
				measure.attachUser(user);
				addMeasureToDb(em, measure);
			}
		} finally {
			em.close();
		}
	}

	public void deleteMeasureWithId(String measureId) {
		MeasurementValidator.validateMeasureId(measureId);
		EntityManager em = emf.createEntityManager();
		try {
			Measurement measure = findMeasureByIdFromDb(em, measureId);
			if (measure != null) {
				String username = measure.getUsername();
				deleteMeasureByUsernameFromDb(em, measure, username);
			}
		} finally {
			em.close();
		}
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
	private List<Measurement> getAllMeasuresForUserWithUsernameFromDb(
			EntityManager em, String username) {
		Query q = em.createNamedQuery("findAllMeasuresByUsername");
		q.setParameter("username", username);
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
