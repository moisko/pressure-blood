package pb.db;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import pb.controller.PressureBloodBaseServlet;
import pb.model.Hand;
import pb.model.Measurement;
import pb.model.PressureBlood;
import pb.model.Users;

@RunWith(MockitoJUnitRunner.class)
public class MeasureDAOTest {

	@Rule
	public ExpectedException expectedExcetpion = ExpectedException.none();

	@Mock
	EntityManagerFactory emf;

	@Mock
	EntityManager em;

	@Mock
	Query query;

	@Mock
	PressureBloodBaseServlet pressureBloodServlet;

	@Mock
	Measurement measure;

	private static final int MAX_RECORDS = 10;

	private static final String EXISTING_USER = "test";

	private static final int VALID_SBP_VALUE = 120;
	private static final int INVALID_SBP_VALUE = 301;

	private static final int VALID_DBP_VALUE = 80;
	private static final int INVALID_DBP_VALUE = 301;

	private static final int INVALID_PULSE_VALUE = 301;

	private MeasureDAO measureDAO;

	@Before
	public void setUp() throws Exception {
		when(emf.createEntityManager()).thenReturn(em);
		measureDAO = new MeasureDAO(emf, MAX_RECORDS);
	}

	@Test
	public void testAddMeasureWithInvalidSbpValue() throws Exception {
		PressureBlood pb = createPressureBloodWithInvalidSbpValue();
		when(measure.getPressureBlood()).thenReturn(pb);

		expectedExcetpion.expect(IllegalArgumentException.class);
		measureDAO.addMeasureForUser(measure, "test");
	}

	@Test
	public void testAddMeasureWithInvalidDbpValue() throws Exception {
		PressureBlood pb = createPressureBloodWithInvalidDbpValue();
		when(measure.getPressureBlood()).thenReturn(pb);

		expectedExcetpion.expect(IllegalArgumentException.class);
		measureDAO.addMeasureForUser(measure, "test");
	}

	@Test
	public void testAddMeasureWithInvalidPulseValue() throws Exception {
		Measurement measureWithInvalidPulse = createMeasureWithInvalidPulseValue();
		expectedExcetpion.expect(IllegalArgumentException.class);
		measureDAO.addMeasureForUser(measureWithInvalidPulse, EXISTING_USER);
	}

	@Test
	public void testAddMeasureWithUserMaxRecordsReached() throws Exception {
		Measurement validMeasure = createMeasure(120, 80, Hand.LEFT_HAND, 75,
				new Date(), null);
		when(em.createNamedQuery("getMeasuresCount")).thenReturn(query);
		when(query.getSingleResult()).thenReturn(Long.valueOf(11));
		boolean measureAdded = measureDAO.addMeasureForUser(validMeasure,
				"test");
		assertFalse(
				"User has reached the max number of 10 records. Measure must not be added to db.",
				measureAdded);
	}

	private Measurement createMeasureWithInvalidPulseValue() {
		return createMeasure(VALID_SBP_VALUE, VALID_DBP_VALUE, Hand.LEFT_HAND,
				INVALID_PULSE_VALUE, new Date(), null);
	}

	private Measurement createMeasure(int sbp, int dbp, Hand hand, int pulse,
			Date datetime, Users user) {
		Measurement measure = new Measurement();
		PressureBlood pb = new PressureBlood();
		pb.setSbp(sbp);
		pb.setDbp(dbp);
		measure.setPressureBlood(pb);
		measure.setHand(hand);
		measure.setPulse(pulse);
		measure.setDatetime(datetime);
		measure.attachUser(user);
		return measure;
	}

	private PressureBlood createPressureBloodWithInvalidSbpValue() {
		PressureBlood pb = new PressureBlood();
		pb.setSbp(INVALID_SBP_VALUE);
		pb.setDbp(VALID_DBP_VALUE);
		return pb;
	}

	private PressureBlood createPressureBloodWithInvalidDbpValue() {
		PressureBlood pb = new PressureBlood();
		pb.setSbp(VALID_SBP_VALUE);
		pb.setDbp(INVALID_DBP_VALUE);
		return pb;
	}
}
