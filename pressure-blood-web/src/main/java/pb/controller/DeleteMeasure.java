package pb.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pb.controller.JsonResponse.Status;
import pb.model.Measurement;
import pb.model.Users;

import com.google.gson.Gson;

@WebServlet("/DeleteMeasure")
public class DeleteMeasure extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");

		JsonResponse jsonResponse = null;

		EntityManager em = (EntityManager) getServletContext().getAttribute(
				"em");
		Users user = em.find(Users.class, request.getRemoteUser());
		String measureId = request.getParameter("id");
		Measurement measurement = em.find(Measurement.class,
				Long.parseLong(measureId));
		if (measurement == null) {
			jsonResponse = new JsonResponse(Status.MEASURE_NOT_FOUND,
					"Measure with id " + measureId + " not found");
		} else {
			String username = user.getUsername();
			if (measurement.getUser().getUsername().equals(username)) {
				em.getTransaction().begin();
				em.remove(measurement);
				em.getTransaction().commit();
				jsonResponse = new JsonResponse(Status.MEASURE_FOUND,
						"Measure with id " + measureId
								+ " successfully deleted from db");
			} else {
				jsonResponse = new JsonResponse(Status.MEASURE_NOT_FOUND,
						"Measure with id " + measureId + " not found");
			}
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

}
