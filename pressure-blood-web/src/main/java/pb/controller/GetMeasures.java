package pb.controller;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pb.db.MeasureDAO;
import pb.model.Measurement;

@WebServlet(urlPatterns = { "/o.getMeasures" })
public class GetMeasures extends PressureBloodBaseServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("application/json");

		EntityManagerFactory emf = (EntityManagerFactory) getServletContext()
				.getAttribute("emf");

		MeasureDAO measureDAO = new MeasureDAO(emf);

		String username = getUsernameFromHttpRequest(request);

		List<Measurement> measures = measureDAO.getAllMeasuresForUser(username);

		serializeMeasuresToJson(response, measures);
	}

}
