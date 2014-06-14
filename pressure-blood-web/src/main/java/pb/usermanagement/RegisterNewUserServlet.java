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

@WebServlet("/o.registerUser")
public class RegisterNewUserServlet extends PressureBloodBaseServlet {

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("application/json");

		EntityManagerFactory emf = (EntityManagerFactory) getServletContext()
				.getAttribute("emf");

		UsersDAO usersDAO = new UsersDAO(emf);

		Users user = getUserFromHttpRequest(request);

		RegisterUserResponse registerUserResponse = usersDAO.registerUser(user);

		serializeObject(response, registerUserResponse);
	}

}
