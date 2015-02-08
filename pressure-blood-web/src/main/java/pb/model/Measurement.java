package pb.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.google.gson.annotations.Expose;

@Entity
@NamedQueries({
		@NamedQuery(name = "findAllMeasuresByUsername", query = "SELECT m FROM Measurement m WHERE m.user.username = :username"),
		@NamedQuery(name = "findAllMeasuresForDataVisualization", query = "SELECT m.datetime, m.pressureBlood.sbp, m.pressureBlood.dbp FROM Measurement m WHERE m.user.username = :username ORDER BY m.datetime"),
		@NamedQuery(name = "getMeasuresCount", query = "SELECT COUNT(m) FROM Measurement m WHERE m.user.username = :username") })
@Table(name = "MEASUREMENT")
public class Measurement implements Serializable {

	public static final String SBP = "sbp";
	public static final String DBP = "dbp";
	public static final String HAND = "hand";
	public static final String PULSE = "pulse";
	public static final String DATETIME = "datetime";

	private static final long serialVersionUID = 1L;

	@Expose
	private Long id;
	@Expose
	private Date datetime;
	@Expose
	private PressureBlood pressureBlood;
	private Users user;
	@Expose
	private Hand hand = Hand.LEFT_HAND;
	@Expose
	private Integer pulse;

	@Column(name = "TIME", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDatetime() {
		return this.datetime;
	}

	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}

	@OneToOne(optional = false, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "PRESSUREBLOOD_ID", nullable = false)
	public PressureBlood getPressureBlood() {
		return this.pressureBlood;
	}

	public void setPressureBlood(PressureBlood pressureBlood) {
		this.pressureBlood = pressureBlood;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MEASUREMENT_ID", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(optional = false, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "USERNAME")
	public Users getUser() {
		return user;
	}

	private void setUser(Users user) {
		this.user = user;
	}

	public String getUsername() {
		return this.user.getUsername();
	}

	@Enumerated(EnumType.STRING)
	public Hand getHand() {
		return hand;
	}

	public void setHand(Hand hand) {
		this.hand = hand;
	}

	@Column(name = "PULSE", nullable = true)
	public Integer getPulse() {
		return pulse;
	}

	public void setPulse(Integer pulse) {
		this.pulse = pulse;
	}

	public boolean belongsToUser(String username) {
		String actualUsername = this.user.getUsername();
		if (actualUsername != null) {
			return actualUsername.equals(username);
		}
		return false;
	}

	public void attachUser(Users user) {
		this.setUser(user);
	}

}
