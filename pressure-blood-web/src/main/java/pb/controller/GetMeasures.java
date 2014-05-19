package pb.controller;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pb.db.PressureBloodDAO;
import pb.model.Measurement;

@WebServlet("/o.getMeasures")
public class GetMeasures extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");

		String remoteUSer = request.getRemoteUser();

		EntityManagerFactory emf = (EntityManagerFactory) getServletContext()
				.getAttribute("emf");
		PressureBloodDAO pbDao = new PressureBloodDAO(emf);
		List<Measurement> measures = pbDao.getMeasures(remoteUSer);
		request.setAttribute("measures", measures);
		RequestDispatcher view = request.getRequestDispatcher("index.jsp");
		view.forward(request, response);
	}

}
