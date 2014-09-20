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
		UserValidator.validateUser(user);
		EntityManager em = emf.createEntityManager();
		try {
			String username = user.getUsername();
			if (!userExistsInDb(em, username)) {
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

	private boolean userExistsInDb(EntityManager em, String username) {
		boolean userExists = em.find(Users.class, username) != null;
		return userExists;
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
