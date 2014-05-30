package pb.db;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import pb.model.Measurement;
import pb.model.Users;
import pb.validator.MeasurementValidator;

public class PressureBloodDAO {

	private final EntityManagerFactory emf;

	public PressureBloodDAO(EntityManagerFactory emf) {
		this.emf = emf;
	}

	@SuppressWarnings("unchecked")
	public List<Measurement> getMeasures(String remoteUser) {
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

	public boolean deleteMeasure(String measureId, String remoteUser) {
		MeasurementValidator.validateMeasureId(measureId);

		EntityManager em = emf.createEntityManager();
		Measurement measure = em.find(Measurement.class,
				Long.parseLong(measureId));
		try {
			if (measure != null
					&& measure.getUser().getUsername().equals(remoteUser)) {
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
				return true;
			}
		} finally {
			em.close();
		}
		return false;
	}
}
