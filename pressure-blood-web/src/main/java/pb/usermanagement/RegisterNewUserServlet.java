package pb.usermanagement;

import java.io.IOException;

import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pb.controller.PressureBloodBaseServlet;
import pb.db.UsersDAO;
import pb.model.Users;
import pb.usermanagement.RegisterUserResponse.Status;

@WebServlet("/o.registerUser")
public class RegisterNewUserServlet extends PressureBloodBaseServlet {

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setContentType(MEDIA_TYPE_APPLICATION_JSON);

		EntityManagerFactory emf = (EntityManagerFactory) getServletContext()
				.getAttribute(EMF);

		UsersDAO usersDAO = new UsersDAO(emf);

		Users user = getUserFromHttpRequest(request);

		RegisterUserResponse registerUserResponse = usersDAO.registerUser(user);
		if (Status.SUCCESS.equals(registerUserResponse.getStatus())) {
			request.login(user.getUsername(), user.getPassword());
		}

		serializeUserRegistrationStatusToJson(response, registerUserResponse);
	}

}
