package pb.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pb.model.Measurement;
import pb.model.Users;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

@WebServlet("/AddRecordServlet")
public class AddRecordServlet extends HttpServlet {

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
			EntityManager em = (EntityManager) getServletContext()
					.getAttribute("em");
			Users user = em.find(Users.class, request.getRemoteUser());
			measurement.setUser(user);
			em.getTransaction().begin();
			em.persist(measurement);
			em.getTransaction().commit();
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
			SimpleDateFormat formatter = new SimpleDateFormat(
					"MMM d yyyy H:mm aaa");
			Date datetime = null;
			try {
				datetime = formatter.parse(datetimeString);
			} catch (ParseException e) {
				throw new JsonParseException(e.getMessage(), e);
			}
			return datetime;
		}
	}

}
