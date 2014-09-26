package pb.controller;

import java.io.IOException;
import java.util.logging.Logger;

import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pb.db.MeasureDAO;
import pb.model.Measurement;

@WebServlet(urlPatterns = { "/o.addMeasure" }, initParams = { @WebInitParam(name = "maxRecords", value = "10") })
public class AddMeasure extends PressureBloodBaseServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = Logger.getLogger(AddMeasure.class
			.getName());

	@Override
	protected void doPut(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setContentType(MEDIA_TYPE_APPLICATION_JSON);

		Measurement measure = getMeasureFromHttpRequest(request);

		String username = getUsernameFromHttpRequest(request);

		EntityManagerFactory emf = (EntityManagerFactory) getServletContext()
				.getAttribute(EMF);

		int maxRecords = Integer.parseInt(getInitParameter(MAX_RECORDS));

		MeasureDAO measureDAO = new MeasureDAO(emf, maxRecords);

		if (measureDAO.addMeasureForUser(measure, username)) {
			serializeMeasureToJson(response, measure);
		} else {
			error(LOGGER, "User '" + username
					+ "' has reached the maximum allowed number of "
					+ maxRecords + " measures");

			response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);

			String message = "You have reached the maximum allowed number of "
					+ maxRecords + " measures";

			sendResponseMessage(response, message);
		}
	}

}
