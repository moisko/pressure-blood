package pb.db;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletContext;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import pb.controller.PressureBloodBaseServlet;
import pb.db.MeasureDAO;
import pb.model.Hand;
import pb.model.Measurement;
import pb.model.PressureBlood;

@RunWith(MockitoJUnitRunner.class)
public class MeasureDAOTest {

	@Rule
	public ExpectedException expectedExcetpion = ExpectedException.none();

	@Mock
	EntityManagerFactory emf;

	@Mock
	EntityManager em;

	@Mock
	PressureBloodBaseServlet pressureBloodServlet;

	@Mock
	Measurement measureMock;

	private static final int MAX_RECORDS = 10;

	private static final String EXISTING_USER = "test";
	// private static final String UNEXSTING_USER = "unexsting_user";

	private static final int VALID_SBP_VALUE = 120;
	private static final int INVALID_SBP_VALUE = 301;

	private static final int VALID_DBP_VALUE = 80;
	private static final int INVALID_DBP_VALUE = 301;

	private static final int INVALID_PULSE_VALUE = 301;

	private MeasureDAO measureDAO;

	@Before
	public void setUp() throws Exception {
		when(emf.createEntityManager()).thenReturn(em);
		measureDAO = new MeasureDAO(emf);
	}

	@Test
	public void testAddMeasureWithInvalidSbpValue() throws Exception {
		PressureBlood pb = createPressureBloodWithInvalidSbpValue();
		when(measureMock.getPressureBlood()).thenReturn(pb);

		expectedExcetpion.expect(IllegalArgumentException.class);
		measureDAO.addMeasureForUser(measureMock, "test", MAX_RECORDS);
	}

	@Test
	public void testAddMeasureWithInvalidDbpValue() throws Exception {
		PressureBlood pb = createPressureBloodWithInvalidDbpValue();
		when(measureMock.getPressureBlood()).thenReturn(pb);

		expectedExcetpion.expect(IllegalArgumentException.class);
		measureDAO.addMeasureForUser(measureMock, "test", MAX_RECORDS);
	}

	@Test
	public void testAddMeasureWithInvalidPulseValue() throws Exception {
		Measurement invalidMeasure = createMeasureWithInvalidPulseValue();
		expectedExcetpion.expect(IllegalArgumentException.class);
		measureDAO.addMeasureForUser(invalidMeasure, EXISTING_USER, MAX_RECORDS);
	}

	private Measurement createMeasureWithInvalidPulseValue() {
		PressureBlood pb = createValidPressureBlood();
		Hand leftHand = Hand.LEFT_HAND;
		Measurement measure = new Measurement();
		measure.setPressureBlood(pb);
		measure.setHand(leftHand);
		measure.setPulse(INVALID_PULSE_VALUE);
		return measure;
	}

	private PressureBlood createValidPressureBlood() {
		PressureBlood pb = new PressureBlood();
		pb.setSbp(VALID_SBP_VALUE);
		pb.setDbp(VALID_DBP_VALUE);
		return pb;
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
