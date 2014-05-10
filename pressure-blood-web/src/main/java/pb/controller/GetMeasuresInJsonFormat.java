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

@WebServlet("/GetMeasuresInJsonFormat")
public class GetMeasuresInJsonFormat extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");

		PrintWriter writer = response.getWriter();

		EntityManagerFactory emf = (EntityManagerFactory) getServletContext()
				.getAttribute("emf");
		EntityManager em = emf.createEntityManager();
		try {
			Users user = em.find(Users.class, request.getRemoteUser());
			Query q = em.createNamedQuery("findAllMeasuresByUsername");
			q.setParameter("username", user.getUsername());
			List<Measurement> measures = q.getResultList();
			GsonBuilder gsonBuilder = new GsonBuilder();
			gsonBuilder.excludeFieldsWithoutExposeAnnotation();
			Gson gson = gsonBuilder.create();
			String json = gson.toJson(measures);
			writer.write(json);
		} finally {
			em.close();
			writer.close();
		}
	}

}
