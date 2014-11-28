package pb.validator;

import java.text.ParseException;
import java.util.Date;

import pb.db.BaseDAO;
import pb.model.Hand;
import pb.model.Measurement;
import pb.model.PressureBlood;

public class MeasurementValidator {

	// SBP
	private static final int MIN_SBP_VALUE = 0;
	private static final int MAX_SBP_VALDUE = 300;

	// DBP
	private static final int MIN_DBP_VALUE = 0;
	private static final int MAX_DBP_VALUE = 300;

	// PULSE
	private static final int MIN_PULSE_VALUE = 0;
	private static final int MAX_PULSE_VALUE = 300;

	public static void validateMeasure(Measurement measure) {
		ensureNotNull(measure, "Measure instance is null");

		PressureBlood pb = measure.getPressureBlood();
		Integer sbp = pb.getSbp();
		validateSbp(sbp);
		Integer dbp = pb.getDbp();
		validateDbp(dbp);

		validateHand(measure.getHand());

		Integer pulse = measure.getPulse();
		if (pulse != null) {
			validatePulse(pulse);
		}

		ensureNotNull(measure.getDatetime(), "Datetime value not set");
	}

	public static void validateMeasureId(String measureId) {
		ensureNotNull(measureId, "Measure id is null");

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

	public static void validateMeasureProperty(String measureProperty,
			String value) {
		switch (measureProperty) {
		case Measurement.SBP: {
			try {
				ensureNotNull(value, "SBP value not set");
				Integer sbp = Integer.parseInt(value);
				validateSbp(sbp);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("SBP value " + value
						+ " is not valid", e);
			}
		}
			break;
		case Measurement.DBP: {
			try {
				ensureNotNull(value, "DBP value not set");
				Integer dbp = Integer.parseInt(value);
				validateDbp(dbp);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("DBP value " + value
						+ " is not valid", e);
			}
		}
			break;
		case Measurement.HAND: {
			ensureNotNull(value, "Hand value not set");
			validateHand(Hand.valueOf(value));
		}
			break;
		case Measurement.PULSE: {
			if (value != null) {
				validatePulse(Integer.parseInt(value));
			}
		}
			break;
		case Measurement.DATETIME:
			validateDatetime(value);
			break;
		default:
			throw new IllegalArgumentException("Measure property ["
					+ measureProperty + "] is not a valid measure property");
		}
	}

	private static void validateSbp(Integer sbp) {
		ensureNotNull(sbp, "SBP value not set");
		if (sbp < MIN_SBP_VALUE) {
			throw new IllegalArgumentException(
					"SBP value must be greater than or qeual to "
							+ MIN_SBP_VALUE);
		} else if (sbp > MAX_SBP_VALDUE) {
			throw new IllegalArgumentException(
					"SBP value must be less than or qeual to " + MAX_SBP_VALDUE);
		}
	}

	private static void validateDbp(Integer dbp) {
		ensureNotNull(dbp, "DBP value not set");
		if (dbp < MIN_DBP_VALUE) {
			throw new IllegalArgumentException(
					"DBP value must be greater than or qeual to "
							+ MIN_DBP_VALUE);
		} else if (dbp > MAX_DBP_VALUE) {
			throw new IllegalArgumentException(
					"DBP value must be less than or qeual to " + MAX_DBP_VALUE);
		}
	}

	private static void validateHand(Hand hand) {
		ensureNotNull(hand, "Hand value not set");
		if (hand.equals(Hand.LEFT_HAND) || hand.equals(Hand.RIGHT_HAND)) {
			return;
		} else {
			throw new IllegalArgumentException("Hand value must be one of "
					+ Hand.RIGHT_HAND + " or " + Hand.LEFT_HAND);
		}
	}

	private static void validatePulse(Integer pulse) {
		ensureNotNull(pulse, "Pulse value not set");
		if (pulse < MIN_PULSE_VALUE) {
			throw new IllegalArgumentException(
					"PULSE value must be greater than or qeual to "
							+ MIN_PULSE_VALUE);
		} else if (pulse > MAX_PULSE_VALUE) {
			throw new IllegalArgumentException(
					"PULSE value must be less than or qeual to "
							+ MAX_PULSE_VALUE);
		}
	}

	private static void validateDatetime(String value) {
		ensureNotNull(value, "Datetime value not set");
		try {
			Date datetime = BaseDAO.FORMATTER.parse(value);
			String datetimeString = BaseDAO.FORMATTER.format(datetime);
			if (!datetimeString.equals(value)) {
				throw new IllegalArgumentException("Datetime value " + value
						+ " is not valid");
			}
		} catch (ParseException e) {
			throw new IllegalArgumentException("Datetime value " + value
					+ " is not valid", e);
		}
	}

	private static void ensureNotNull(Object object, String errorMessage) {
		if (object == null) {
			throw new IllegalArgumentException(errorMessage);
		}
	}

}
