package pb.controller;

import java.io.IOException;

import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pb.db.MeasureDAO;
import pb.model.Measurement;

@WebServlet("/o.addMeasure")
public class AddMeasure extends PressureBloodBaseServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPut(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("application/json");

		Measurement measure = getMeasureFromHttpRequest(request);

		String username = getUsernameFromHttpRequest(request);

		EntityManagerFactory emf = (EntityManagerFactory) getServletContext()
				.getAttribute("emf");

		MeasureDAO measureDao = new MeasureDAO(emf);

		measureDao.addMeasureForUserWithUsername(measure, username);

		serializeMeasureToJson(response, measure);
	}

}
