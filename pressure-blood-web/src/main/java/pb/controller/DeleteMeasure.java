package pb.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pb.controller.JsonResponse.Status;
import pb.db.PressureBloodDAO;

import com.google.gson.Gson;

@WebServlet("/o.deleteMeasure")
public class DeleteMeasure extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("application/json");

		String id = request.getParameter("id");
		String remoteUser = request.getRemoteUser();

		PressureBloodDAO pbDao = new PressureBloodDAO(getServletContext());
		long measureId = pbDao.deleteMeasure(id, remoteUser);
		JsonResponse jsonResponse = null;
		if (measureId == 0L) {
			jsonResponse = new JsonResponse(Status.MEASURE_NOT_FOUND,
					"Measure with id " + id + " not found", null);
		} else {
			jsonResponse = new JsonResponse(Status.MEASURE_FOUND,
					"Measure with id " + id + " successfully deleted from db",
					null);
		}

		PrintWriter writer = response.getWriter();
		try {
			Gson gson = new Gson();
			writer.write(gson.toJson(jsonResponse));
		} finally {
			writer.close();
		}
	}

}
