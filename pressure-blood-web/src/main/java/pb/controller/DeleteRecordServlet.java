package pb.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pb.model.Measurement;
import pb.model.Users;

import com.google.gson.Gson;

@WebServlet("/DeleteRecordServlet")
public class DeleteRecordServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");

		JsonResponse jsonResponse = new JsonResponse();

		EntityManager em = (EntityManager) getServletContext().getAttribute(
				"em");
		Users user = em.find(Users.class, request.getRemoteUser());
		String recordId = request.getParameter("id");
		Measurement measurement = em.find(Measurement.class,
				Long.parseLong(recordId));
		if (measurement == null) {
			jsonResponse.setStatus(JsonResponse.RECORD_NOT_FOUND);
			jsonResponse
					.setMessage("Record with id " + recordId + " not found");
		} else {
			String username = user.getUsername();
			if (measurement.getUser().getUsername().equals(username)) {
				em.getTransaction().begin();
				em.remove(measurement);
				em.getTransaction().commit();
				jsonResponse.setStatus(JsonResponse.RECORD_FOUND);
				jsonResponse.setMessage("Record with id " + recordId
						+ " successfully deleted from db");
			} else {
				jsonResponse.setStatus(JsonResponse.RECORD_NOT_FOUND);
				jsonResponse.setMessage("Record with id " + recordId
						+ " not found");
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
