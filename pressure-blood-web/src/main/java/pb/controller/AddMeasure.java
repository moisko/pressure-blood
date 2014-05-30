package pb.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pb.db.PressureBloodDAO;
import pb.model.Measurement;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

@WebServlet("/o.addMeasure")
public class AddMeasure extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPut(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("application/json");

		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(
					request.getInputStream()));
			GsonBuilder gsonBuilder = new GsonBuilder();
			gsonBuilder.registerTypeAdapter(Date.class,
					new DatetimeDeserializer());
			gsonBuilder.excludeFieldsWithoutExposeAnnotation();
			Gson gson = gsonBuilder.create();

			Measurement measure = gson.fromJson(br, Measurement.class);
			EntityManagerFactory emf = (EntityManagerFactory) getServletContext()
					.getAttribute("emf");
			PressureBloodDAO pbDao = new PressureBloodDAO(emf);
			pbDao.addMeasure(measure, request.getRemoteUser());

			PrintWriter writer = response.getWriter();
			try {
				String json = gson.toJson(measure);
				writer.write(json);
			} finally {
				writer.close();
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
			try {
				Date datetime = formatter.parse(datetimeString);
				return datetime;
			} catch (ParseException e) {
				throw new JsonParseException(e.getMessage(), e);
			}
		}
	}

}
