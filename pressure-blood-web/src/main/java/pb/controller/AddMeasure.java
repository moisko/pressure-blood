package pb.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pb.model.Measurement;
import pb.model.PressureBlood;
import pb.model.Users;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

@WebServlet("/AddMeasure")
public class AddMeasure extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPut(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");

		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(
					request.getInputStream()));
			GsonBuilder gsonBuilder = new GsonBuilder();
			gsonBuilder.registerTypeAdapter(Date.class,
					new DatetimeDeserializer());
			Gson gson = gsonBuilder.create();
			Measurement measurement = gson.fromJson(br, Measurement.class);
			validateMeasure(measurement);
			EntityManagerFactory emf = (EntityManagerFactory) getServletContext()
					.getAttribute("emf");
			EntityManager em = emf.createEntityManager();
			Users user = em.find(Users.class, request.getRemoteUser());
			measurement.setUser(user);
			try {
				EntityTransaction et = em.getTransaction();
				try {
					et.begin();
					em.persist(measurement);
					et.commit();
				} finally {
					if (et.isActive()) {
						et.rollback();
					}
				}
			} finally {
				em.close();
			}
		} finally {
			if (br != null) {
				br.close();
			}
		}
	}

	private class DatetimeDeserializer implements JsonDeserializer<Date> {
		@Override
		public Date deserialize(JsonElement json, Type typeOfT,
				JsonDeserializationContext context) throws JsonParseException {
			String datetimeString = json.getAsString();
			SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy H:mm");
			Date datetime = null;
			try {
				datetime = formatter.parse(datetimeString);
			} catch (ParseException e) {
				throw new JsonParseException(e.getMessage(), e);
			}
			return datetime;
		}
	}

	private void validateMeasure(Measurement measure) {
		PressureBlood pb = measure.getPressureBlood();
		Integer sbp = pb.getSbp();
		if (sbp == null) {
			throw new IllegalArgumentException("SBP value not set");
		} else if (sbp <= 0) {
			throw new IllegalArgumentException(
					"SBP value must be greater than or qeual to 0");
		}
		Integer dbp = pb.getDbp();
		if (dbp == null) {
			throw new IllegalArgumentException("DBP value not set");
		} else if (dbp < 0) {
			throw new IllegalArgumentException(
					"DBP value must be greater than or qeual to 0");
		}
		Integer pulse = measure.getPulse();
		if (pulse == null) {
			throw new IllegalArgumentException("Pulse value not set");
		} else if (pulse < 0) {
			throw new IllegalArgumentException(
					"Pulse value must be greater than or qeual to 0");
		}
		Date datetime = measure.getDatetime();
		if (datetime == null) {
			throw new IllegalArgumentException("Datetime value not set");
		}
	}

}
