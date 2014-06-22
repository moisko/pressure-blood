package pb.controller;

import java.io.IOException;

import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pb.db.MeasureDAO;

@WebServlet("/o.deleteMeasure")
public class DeleteMeasure extends PressureBloodBaseServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		EntityManagerFactory emf = (EntityManagerFactory) getServletContext()
				.getAttribute("emf");

		MeasureDAO pbDao = new MeasureDAO(emf);

		String measureId = getMeasureIdFromHttpRequest(request);

		pbDao.deleteMeasureWithId(measureId);
	}

}
