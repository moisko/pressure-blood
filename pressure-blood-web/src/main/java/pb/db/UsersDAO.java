package pb.db;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import pb.model.Users;
import pb.usermanagement.RegisterUserResponse;
import pb.usermanagement.RegisterUserResponse.Status;
import pb.validator.UserValidator;

public class UsersDAO {

	private final EntityManagerFactory emf;

	public UsersDAO(EntityManagerFactory emf) {
		this.emf = emf;
	}

	public RegisterUserResponse registerUser(Users user) {
		String username = user.getUsername();
		UserValidator.validateUsername(username);
		EntityManager em = emf.createEntityManager();
		try {
			if (em.find(Users.class, username) == null) {
				registerUserToDb(em, user);
				return new RegisterUserResponse(Status.SUCCESS, "User "
						+ username + " successfully registered");
			}
			return new RegisterUserResponse(Status.EXISTS, "User " + username
					+ " already exists");
		} finally {
			em.close();
		}
	}

	private void registerUserToDb(EntityManager em, Users user) {
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
}
