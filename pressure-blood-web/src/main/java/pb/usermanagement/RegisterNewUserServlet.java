package pb.usermanagement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pb.controller.JsonResponse;
import pb.model.Users;
import pb.model.UsersDTO;

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
			JsonResponse jsonResponse = registerUser(user);
			if (jsonResponse.getStatus().equals(JsonResponse.Status.SUCCESS)) {
				request.login(user.getUsername(), user.getPassword());
			}
			String json = gson.toJson(jsonResponse);
			writer.write(json);
		} catch (ServletException e) {
			JsonResponse jsonResponse = new JsonResponse(
					JsonResponse.Status.ERROR, e.getMessage());
			String json = gson.toJson(jsonResponse);
			writer.write(json);
		} catch (IllegalArgumentException e) {
			JsonResponse jsonResponse = new JsonResponse(
					JsonResponse.Status.ERROR, e.getMessage());
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
		EntityManager em = (EntityManager) getServletContext().getAttribute(
				"em");
		Users userFromDb = em.find(Users.class, user.getUsername());
		if (userFromDb == null) {
			em.getTransaction().begin();
			em.persist(user);
			em.getTransaction().commit();
			jsonResponse = new JsonResponse(JsonResponse.Status.SUCCESS,
					"User " + user.getUsername() + " successfully registered");
		} else {
			jsonResponse = new JsonResponse(JsonResponse.Status.EXISTS, "User "
					+ user.getUsername() + " already exists");
		}
		return jsonResponse;
	}
}
