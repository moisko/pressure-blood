package pb.validator;

import java.util.Date;

import pb.model.Measurement;
import pb.model.PressureBlood;

public class MeasurementValidator {

	public static void validateMeasure(Measurement measure) {
		PressureBlood pb = measure.getPressureBlood();
		Integer sbp = pb.getSbp();
		if (sbp == null) {
			throw new IllegalArgumentException("SBP value not set");
		} else if (sbp <= 0) {
			throw new IllegalArgumentException(
					"SBP value must be greater than or qeual to 0");
		} else if (sbp > 300) {
			throw new IllegalArgumentException(
					"SBP value must be less than or qeual to 300");
		}
		Integer dbp = pb.getDbp();
		if (dbp == null) {
			throw new IllegalArgumentException("DBP value not set");
		} else if (dbp < 0) {
			throw new IllegalArgumentException(
					"DBP value must be greater than or qeual to 0");
		} else if (dbp > 300) {
			throw new IllegalArgumentException(
					"DBP value must be less than or qeual to 300");
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

	public static void validateMeasureId(String measureId) {
		try {
			Long id = Long.parseLong(measureId);
			if (id < 1) {
				throw new IllegalArgumentException(
						"Measure ID value must be greater than or equal to 1");
			}
		} catch (NumberFormatException ex) {
			throw new IllegalArgumentException("Measure ID value not a number");
		}
	}
}
