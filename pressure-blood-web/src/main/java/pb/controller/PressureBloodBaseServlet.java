package pb.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pb.model.Measurement;
import pb.model.Users;
import pb.model.UsersDTO;
import pb.usermanagement.RegisterUserResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class PressureBloodBaseServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final String MEASURE_ID_PARAM = "id";

	private static final String DATETIME_PATTERN = "dd.MM.yyyy H:mm";

	protected String getUsernameFromHttpRequest(HttpServletRequest request) {
		String username = request.getRemoteUser();
		return username;
	}

	protected Users getUserFromHttpRequest(HttpServletRequest request)
			throws IOException {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(
					request.getInputStream()));
			Gson gson = new Gson();
			UsersDTO userDTO = gson.fromJson(br, UsersDTO.class);
			Users user = new Users(userDTO);
			return user;
		} finally {
			if (br != null) {
				br.close();
			}
		}
	}

	protected String getMeasureIdFromHttpRequest(HttpServletRequest request) {
		String measureId = request.getParameter(MEASURE_ID_PARAM);
		return measureId;
	}

	protected Measurement getMeasureFromHttpRequest(HttpServletRequest request)
			throws IOException {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(
					request.getInputStream()));

			Gson gson = createGsonInstanceWithTypeAdapter();

			Measurement measure = gson.fromJson(br, Measurement.class);
			return measure;
		} finally {
			if (br != null) {
				br.close();
			}
		}
	}

	protected void serializeUserRegistrationStatusToJson(
			HttpServletResponse response,
			RegisterUserResponse registerUserResponse)
			throws IOException {
		PrintWriter writer = response.getWriter();
		try {
			Gson gson = new Gson();
			String json = gson.toJson(registerUserResponse);
			writer.write(json);
		} finally {
			writer.close();
		}
	}

	protected void serializeMeasuresToJson(HttpServletResponse response,
			List<Measurement> measures) throws IOException {
		PrintWriter writer = response.getWriter();
		try {
			Gson gson = createGsonInstanceWithTypeAdapter();

			String json = gson.toJson(measures);

			writer.write(json);
		} finally {
			writer.close();
		}
	}

	protected void serializeMeasureToJson(HttpServletResponse response,
			Measurement measure) throws IOException {
		PrintWriter writer = response.getWriter();
		try {
			Gson gson = createGsonInstanceWithTypeAdapter();

			String json = gson.toJson(measure);

			writer.write(json);
		} finally {
			writer.close();
		}
	}

	private Gson createGsonInstanceWithTypeAdapter() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Date.class, new DatetimeDeserializer());
		gsonBuilder.excludeFieldsWithoutExposeAnnotation();
		Gson gson = gsonBuilder.create();
		return gson;
	}

	private class DatetimeDeserializer implements JsonDeserializer<Date> {
		@Override
		public Date deserialize(JsonElement json, Type typeOfT,
				JsonDeserializationContext context) throws JsonParseException {
			String datetimeString = json.getAsString();
			SimpleDateFormat formatter = new SimpleDateFormat(DATETIME_PATTERN);
			try {
				Date datetime = formatter.parse(datetimeString);
				return datetime;
			} catch (ParseException e) {
				throw new JsonParseException(e.getMessage(), e);
			}
		}
	}

}
