package pb.controller;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pb.db.MeasureDAO;
import pb.model.Measurement;

@WebServlet(urlPatterns = { "/o.getMeasures" }, initParams = { @WebInitParam(name = "maxRecords", value = "50") })
public class GetMeasures extends PressureBloodBaseServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setContentType(MEDIA_TYPE_APPLICATION_JSON);

		EntityManagerFactory emf = (EntityManagerFactory) getServletContext()
				.getAttribute(EMF);

		int maxRecords = Integer.parseInt(getInitParameter(PARAM_MAX_RECORDS));

		MeasureDAO measureDAO = new MeasureDAO(emf, maxRecords);

		String username = getUsernameFromHttpRequest(request);

		List<Measurement> measures = measureDAO.getAllMeasuresForUser(username);

		serializeMeasuresToJson(response, measures);
	}

}
