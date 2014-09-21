package pb.controller;

import java.io.IOException;

import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletContext;
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

	@Override
	protected void doPut(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("application/json");

		Measurement measure = getMeasureFromHttpRequest(request);

		String username = getUsernameFromHttpRequest(request);

		ServletContext servletContext = getServletContext();

		EntityManagerFactory emf = (EntityManagerFactory) servletContext
				.getAttribute("emf");

		MeasureDAO measureDAO = new MeasureDAO(emf);
		long maxRecords = Long.parseLong(getInitParameter("maxRecords"));
		long userRecords = measureDAO.findUserRecordsCountFromDb(username);
		if (userRecords < maxRecords) {
			measureDAO.addMeasureForUser(measure, username);

			serializeMeasureToJson(response, measure);
		}
	}

}
