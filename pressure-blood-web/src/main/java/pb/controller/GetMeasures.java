package pb.controller;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pb.model.Measurement;
import pb.model.Users;

@WebServlet("/GetMeasures")
public class GetMeasures extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");

		EntityManagerFactory emf = (EntityManagerFactory) getServletContext()
				.getAttribute("emf");
		EntityManager em = emf.createEntityManager();
		try {
			Users user = em.find(Users.class, request.getRemoteUser());
			Query q = em.createNamedQuery("findAllMeasuresByUsername");
			q.setParameter("username", user.getUsername());
			List<Measurement> measures = q.getResultList();
			request.setAttribute("measures", measures);
			RequestDispatcher view = request.getRequestDispatcher("index.jsp");
			view.forward(request, response);
		} finally {
			em.close();
		}
	}

}
