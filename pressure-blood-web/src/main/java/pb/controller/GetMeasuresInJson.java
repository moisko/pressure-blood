package pb.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pb.model.Measurement;
import pb.model.Users;

import com.google.gson.Gson;

@WebServlet("/GetMeasuresInJson")
public class GetMeasuresInJson extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");

		EntityManager em = (EntityManager) getServletContext().getAttribute(
				"em");
		Users user = em.find(Users.class, request.getRemoteUser());
		Query q = em.createNamedQuery("findAllMeasuresByUsername");
		q.setParameter("username", user.getUsername());
		List<Measurement> measurements = q.getResultList();
		Gson gson = new Gson();
		PrintWriter writer = response.getWriter(); 
		writer.write(gson.toJson(measurements));
		writer.close();
	}

}
