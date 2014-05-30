package pb.controller;

import java.io.IOException;

import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pb.db.PressureBloodDAO;

@WebServlet("/o.deleteMeasure")
public class DeleteMeasure extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final String MEASURE_ID = "id";

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		EntityManagerFactory emf = (EntityManagerFactory) getServletContext()
				.getAttribute("emf");

		PressureBloodDAO pbDao = new PressureBloodDAO(emf);

		boolean measureDeleted = pbDao.deleteMeasure(
				request.getParameter(MEASURE_ID), request.getRemoteUser());

		setStatusCode(response, measureDeleted);
	}

	private void setStatusCode(HttpServletResponse response,
			boolean measureDeleted) {
		if (measureDeleted) {
			response.setStatus(HttpServletResponse.SC_OK);
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}

}
