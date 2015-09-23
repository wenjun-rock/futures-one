package fwj.futures.resource.entity.com;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity()
@Table(name = "com_holiday")
public class Holiday extends AbstractPersistable<Integer> {

	private static final long serialVersionUID = -2898454756527694681L;

	@Column(length = 10)
	private String desc2;

	@Column(columnDefinition = "TIMESTAMP")
	private Date startDateTime;

	@Column(columnDefinition = "TIMESTAMP")
	private Date endDateTime;

	public String getDesc2() {
		return desc2;
	}

	public void setDesc2(String desc2) {
		this.desc2 = desc2;
	}

	public Date getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(Date startDateTime) {
		this.startDateTime = startDateTime;
	}

	public Date getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(Date endDateTime) {
		this.endDateTime = endDateTime;
	}

}
