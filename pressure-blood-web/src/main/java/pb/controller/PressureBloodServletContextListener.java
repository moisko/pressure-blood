package pb.controller;

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

	@Override
	public void contextInitialized(ServletContextEvent event) {
		emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		ServletContext sc = event.getServletContext();
		sc.setAttribute("emf", emf);
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		emf.close();
	}

}
