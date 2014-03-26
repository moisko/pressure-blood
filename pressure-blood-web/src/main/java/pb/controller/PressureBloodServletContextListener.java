package pb.controller;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class PressureBloodServletContextListener implements
		ServletContextListener {

	private static final String PERSISTENCE_UNIT_NAME = "PressureBlood";

	private EntityManagerFactory emf;
	private EntityManager em;

	@Override
	public void contextInitialized(ServletContextEvent event) {
		emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		em = emf.createEntityManager();
		ServletContext sc = event.getServletContext();
		sc.setAttribute("em", em);
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		emf.close();
		em.close();
	}

}
