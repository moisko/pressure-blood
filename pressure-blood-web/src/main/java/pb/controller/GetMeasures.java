package pb.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pb.model.Measurement;
import pb.model.Users;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@WebServlet("/o.getMeasures")
public class GetMeasures extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("application/json");

		EntityManagerFactory emf = (EntityManagerFactory) getServletContext()
				.getAttribute("emf");

		List<Measurement> measures = findAllMeasuresByUsername(emf,
				request.getRemoteUser());

		String json = toJson(measures);

		sendJsonResponse(response, json);
	}

	@SuppressWarnings("unchecked")
	private List<Measurement> findAllMeasuresByUsername(
			EntityManagerFactory emf, String remoteUser) {
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

	private String toJson(List<Measurement> measures) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.excludeFieldsWithoutExposeAnnotation();
		Gson gson = gsonBuilder.create();
		String json = gson.toJson(measures);
		return json;
	}

	private void sendJsonResponse(HttpServletResponse response, String json)
			throws IOException {
		PrintWriter writer = response.getWriter();
		writer.write(json);
		writer.close();
	}

}
