package pb.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

		String remoteUSer = request.getRemoteUser();

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
			PressureBloodDAO pbDao = new PressureBloodDAO(getServletContext());
			pbDao.addMeasure(measure, remoteUSer);

			PrintWriter writer = response.getWriter();
			try {
				writer.write(gson.toJson(measure));
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
