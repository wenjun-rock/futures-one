package fwj.futures.resource.entity.com;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity()
@Table(name = "com_holiday")
public class Holiday extends AbstractPersistable<Integer> {

	private static final long serialVersionUID = 5095227873434116887L;

	@Column(length = 10)
	private String description;

	@Column(length = 10)
	private String startDate;

	@Column(length = 10)
	private String endDate;

	@Column(columnDefinition = "TIMESTAMP")
	private Date actualStartTime;

	@Column(columnDefinition = "TIMESTAMP")
	private Date actualEndTime;

	public String getDescription() {
		return description;
	}

	public String getStartDate() {
		return startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public Date getActualStartTime() {
		return actualStartTime;
	}

	public Date getActualEndTime() {
		return actualEndTime;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public void setActualStartTime(Date actualStartTime) {
		this.actualStartTime = actualStartTime;
	}

	public void setActualEndTime(Date actualEndTime) {
		this.actualEndTime = actualEndTime;
	}

}
