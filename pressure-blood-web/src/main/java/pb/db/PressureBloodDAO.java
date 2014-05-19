package pb.db;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.servlet.ServletContext;

import pb.model.Measurement;
import pb.model.Users;
import pb.validator.MeasurementValidator;

public class PressureBloodDAO {

	private final ServletContext servletContext;

	public PressureBloodDAO(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	@SuppressWarnings("unchecked")
	public List<Measurement> getMeasures(String remoteUser) {
		EntityManagerFactory emf = (EntityManagerFactory) servletContext
				.getAttribute("emf");
		EntityManager em = emf.createEntityManager();
		try {
			Users user = em.find(Users.class, remoteUser);
			Query q = em.createNamedQuery("findAllMeasuresByUsername");
			q.setParameter("username", user.getUsername());
			List<Measurement> measures = q.getResultList();
			return measures;
		} finally {
			em.close();
		}
	}

	public void addMeasure(Measurement measure, String remoteUser) {
		MeasurementValidator.validateMeasure(measure);

		EntityManagerFactory emf = (EntityManagerFactory) servletContext
				.getAttribute("emf");
		EntityManager em = emf.createEntityManager();
		Users user = em.find(Users.class, remoteUser);
		measure.setUser(user);
		try {
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
		} finally {
			em.close();
		}
	}

	public long deleteMeasure(String measureId, String remoteUser) {
		MeasurementValidator.validateMeasureId(measureId);

		EntityManagerFactory emf = (EntityManagerFactory) servletContext
				.getAttribute("emf");
		EntityManager em = emf.createEntityManager();
		Measurement measure = em.find(Measurement.class,
				Long.parseLong(measureId));
		try {
			// if the entity does not exist
			if (measure == null) {
				return 0L;
			} else {
				// if the entity exists but does not belong to the current user
				String measureUsername = measure.getUser().getUsername();
				if (measureUsername.equals(remoteUser)) {
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
					return measure.getId();
				}
			}
		} finally {
			em.close();
		}
		return 0L;
	}
}
