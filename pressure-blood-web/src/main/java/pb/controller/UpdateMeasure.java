package pb.controller;

import java.io.IOException;

import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pb.db.MeasureDAO;

@WebServlet(urlPatterns = { "/o.updateMeasure" })
public class UpdateMeasure extends PressureBloodBaseServlet {

	private static final long serialVersionUID = 1L;

	private static final int MEASURE_PROPERTY_INDEX = 0;

	private static final int MEASURE_ID_INDEX = 1;

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		EntityManagerFactory emf = (EntityManagerFactory) getServletContext()
				.getAttribute(EMF);

		String measureProperty = getMeasureProperty(request);

		String measureId = getMeasureId(request);

		String value = getValue(request);

		MeasureDAO measureDAO = new MeasureDAO(emf);

		measureDAO.updateMeasure(measureId, measureProperty, value);

		sendResponseMessage(response, value);
	}

	private String getMeasureProperty(HttpServletRequest request) {
		String elementId = request.getParameter("id");
		String measureProperty = elementId.split("_")[MEASURE_PROPERTY_INDEX];
		return measureProperty;
	}

	private String getMeasureId(HttpServletRequest request) {
		String elementId = request.getParameter("id");
		String measureId = elementId.split("_")[MEASURE_ID_INDEX];
		return measureId;
	}

	private String getValue(HttpServletRequest request) {
		String elementValue = request.getParameter("value");
		return elementValue;
	}

}
