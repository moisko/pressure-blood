package pb.validator;

import java.util.Date;

import pb.model.Measurement;
import pb.model.PressureBlood;

public class MeasurementValidator {

	public static void validateMeasure(Measurement measure) {
		ensureNotNull(measure, "Measure instance is null");

		PressureBlood pb = measure.getPressureBlood();
		Integer sbp = pb.getSbp();
		validateSbp(sbp);
		Integer dbp = pb.getDbp();
		validateDbp(dbp);

		Date datetime = measure.getDatetime();
		ensureNotNull(datetime, "Datetime value not set");
	}

	public static void validateMeasureId(String measureId) {
		if (measureId == null) {
			throw new IllegalArgumentException("Measure id is null");
		}
		try {
			Long id = Long.parseLong(measureId);
			if (id < 0) {
				throw new IllegalArgumentException(
						"Measure ID value must be greater than or equal to 1");
			}
		} catch (NumberFormatException ex) {
			throw new IllegalArgumentException(
					"Measure ID value is not a number", ex);
		}
	}

	private static void validateSbp(Integer sbp) {
		if (sbp == null) {
			throw new IllegalArgumentException("SBP value not set");
		} else if (sbp < 0) {
			throw new IllegalArgumentException(
					"SBP value must be greater than or qeual to 0");
		} else if (sbp > 300) {
			throw new IllegalArgumentException(
					"SBP value must be less than or qeual to 300");
		}
	}

	private static void validateDbp(Integer dbp) {
		if (dbp == null) {
			throw new IllegalArgumentException("DBP value not set");
		} else if (dbp < 0) {
			throw new IllegalArgumentException(
					"DBP value must be greater than or qeual to 0");
		} else if (dbp > 300) {
			throw new IllegalArgumentException(
					"DBP value must be less than or qeual to 300");
		}
	}

	private static void ensureNotNull(Object object, String errorMessage) {
		if (object == null) {
			throw new IllegalArgumentException(errorMessage);
		}
	}

}
