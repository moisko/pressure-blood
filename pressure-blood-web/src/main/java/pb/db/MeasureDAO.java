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

	private final String username;

	public MeasureDAO(EntityManagerFactory emf, String username) {
		this.emf = emf;
		UserValidator.validateUsername(username);
		this.username = username;
	}

	public List<Measurement> getMeasures() {
		EntityManager em = emf.createEntityManager();
		try {
			List<Measurement> measures = findAllMeasuresFromDb(em);
			return measures;
		} finally {
			em.close();
		}
	}

	public void addMeasure(Measurement measure) {
		MeasurementValidator.validateMeasure(measure);
		EntityManager em = emf.createEntityManager();
		try {
			Users user = findUserByUsername(em);
			if (user != null) {
				measure.attachUser(user);
				addMeasureToDb(em, measure);
			}
		} finally {
			em.close();
		}
	}

	public void deleteMeasure(String measureId) {
		MeasurementValidator.validateMeasureId(measureId);
		EntityManager em = emf.createEntityManager();
		try {
			Measurement measure = findMeasureById(em, measureId);
			if (measure != null) {
				deleteMeasureFromDb(em, measure);
			}
		} finally {
			em.close();
		}
	}

	private Users findUserByUsername(EntityManager em) {
		Users user = em.find(Users.class, username);
		return user;
	}

	private Measurement findMeasureById(EntityManager em, String measureId) {
		Measurement measure = em.find(Measurement.class,
				Long.parseLong(measureId));
		return measure;

	}

	@SuppressWarnings("unchecked")
	private List<Measurement> findAllMeasuresFromDb(EntityManager em) {
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

	private void deleteMeasureFromDb(EntityManager em, Measurement measure) {
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
