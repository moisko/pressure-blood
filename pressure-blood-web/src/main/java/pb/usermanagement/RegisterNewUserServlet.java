package pb.usermanagement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pb.controller.JsonResponse;
import pb.model.Users;
import pb.model.UsersDTO;
import pb.validator.UserValidator;

import com.google.gson.Gson;

@WebServlet("/RegisterNewUserServlet")
public class RegisterNewUserServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");

		PrintWriter writer = response.getWriter();
		BufferedReader br = null;
		Gson gson = new Gson();
		try {
			br = new BufferedReader(new InputStreamReader(
					request.getInputStream()));
			UsersDTO userDTO = gson.fromJson(br, UsersDTO.class);
			Users user = new Users(userDTO);
			UserValidator.validateUser(user);
			JsonResponse jsonResponse = registerUser(user);
			if (jsonResponse.getStatus().equals(JsonResponse.Status.SUCCESS)) {
				request.login(user.getUsername(), user.getPassword());
			}
			String json = gson.toJson(jsonResponse);
			writer.write(json);
		} catch (ServletException e) {
			JsonResponse jsonResponse = new JsonResponse(
					JsonResponse.Status.ERROR, e.getMessage(), e);
			String json = gson.toJson(jsonResponse);
			writer.write(json);
		} catch (IllegalArgumentException e) {
			JsonResponse jsonResponse = new JsonResponse(
					JsonResponse.Status.ERROR, e.getMessage(), e);
			String json = gson.toJson(jsonResponse);
			writer.write(json);
		} finally {
			if (br != null) {
				br.close();
			}
			writer.close();
		}
	}

	private JsonResponse registerUser(Users user) {
		JsonResponse jsonResponse = null;
		EntityManagerFactory emf = (EntityManagerFactory) getServletContext()
				.getAttribute("emf");
		EntityManager em = emf.createEntityManager();
		try {
			Users userFromDb = em.find(Users.class, user.getUsername());
			if (userFromDb == null) {
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
				jsonResponse = new JsonResponse(JsonResponse.Status.SUCCESS,
						"User " + user.getUsername()
								+ " successfully registered", null);
			} else {
				jsonResponse = new JsonResponse(JsonResponse.Status.EXISTS,
						"User " + user.getUsername() + " already exists", null);
			}
		} finally {
			em.close();
		}
		return jsonResponse;
	}
}
