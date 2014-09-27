package pb.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.google.gson.annotations.Expose;

@Entity
@Table(name = "PRESSUREBLOOD")
public class PressureBlood implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	@Expose
	private Integer sbp;
	@Expose
	private Integer dbp;
	private Measurement measurement;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PRESSUREBLOOD_ID", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "SBP", nullable = false)
	public Integer getSbp() {
		return sbp;
	}

	public void setSbp(Integer sbp) {
		this.sbp = sbp;
	}

	@Column(name = "DBP", nullable = false)
	public Integer getDbp() {
		return dbp;
	}

	public void setDbp(Integer dbp) {
		this.dbp = dbp;
	}

	@OneToOne(optional = false, mappedBy = "pressureBlood")
	public Measurement getMeasurement() {
		return measurement;
	}

	public void setMeasurement(Measurement measurement) {
		this.measurement = measurement;
	}

}
