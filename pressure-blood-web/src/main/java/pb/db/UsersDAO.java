package pb.db;

import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import pb.model.Users;
import pb.usermanagement.RegisterUserResponse;
import pb.usermanagement.RegisterUserResponse.Status;
import pb.validator.UserValidator;

public class UsersDAO extends BaseDAO {

	private static final Logger LOGGER = Logger.getLogger(UsersDAO.class
			.getName());

	private final EntityManagerFactory emf;

	public UsersDAO(EntityManagerFactory emf) {
		this.emf = emf;
	}

	public RegisterUserResponse registerUser(Users user) {
		UserValidator.validateUser(user);
		EntityManager em = emf.createEntityManager();
		try {
			String username = user.getUsername();

			info(LOGGER, "Checking if user '" + username
					+ "' already exists in db");

			if (!userExistsInDb(em, username)) {
				registerUserToDb(em, user);

				info(LOGGER, "[" + username + "] " + " successfully registered");

				return new RegisterUserResponse(Status.SUCCESS, "User "
						+ username + " successfully registered");
			} else {
				info(LOGGER, "[" + username + "] " + " already registered");

				return new RegisterUserResponse(Status.EXISTS, "User "
						+ username + " already exists");
			}
		} finally {
			em.close();
		}
	}

}
