package pb.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pb.controller.JsonResponse.Status;
import pb.model.Measurement;

import com.google.gson.Gson;

@WebServlet("/DeleteMeasure")
public class DeleteMeasure extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");

		JsonResponse jsonResponse = null;

		String username = request.getRemoteUser();
		String measureId = request.getParameter("id");
		validateMeasureId(measureId);
		EntityManagerFactory emf = (EntityManagerFactory) getServletContext()
				.getAttribute("emf");
		EntityManager em = emf.createEntityManager();
		try {
			Measurement measure = em.find(Measurement.class,
					Long.parseLong(measureId));
			if (measure == null) {
				jsonResponse = new JsonResponse(Status.MEASURE_NOT_FOUND,
						"Measure with id " + measureId + " not found", null);
			} else {
				if (measure.getUser().getUsername().equals(username)) {
					EntityTransaction et = em.getTransaction();
					try {
						et.begin();
						em.remove(measure);
						et.commit();
					} finally {
						if (et.isActive()) {
							et.rollback();
						}
					}
					jsonResponse = new JsonResponse(Status.MEASURE_FOUND,
							"Measure with id " + measureId
									+ " successfully deleted from db", null);
				} else {
					jsonResponse = new JsonResponse(Status.MEASURE_NOT_FOUND,
							"Measure with id " + measureId + " not found", null);
				}
			}
		} finally {
			em.close();
		}

		PrintWriter writer = response.getWriter();
		try {
			Gson gson = new Gson();
			String json = gson.toJson(jsonResponse);
			writer.write(json);
		} finally {
			writer.close();
		}
	}

	private void validateMeasureId(String measureId) {
		try {
			Integer id = Integer.parseInt(measureId);
			if (id < 1) {
				throw new IllegalArgumentException(
						"Measure ID value must be greater than or equal to 1");
			}
		} catch (NumberFormatException ex) {
			throw new IllegalArgumentException("Measure ID value not a number");
		}
	}

}
