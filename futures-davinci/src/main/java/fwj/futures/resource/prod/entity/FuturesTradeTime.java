package fwj.futures.resource.prod.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity()
@Table(name="prod_futures_trade_time")
public class FuturesTradeTime extends AbstractPersistable<Integer> {

	private static final long serialVersionUID = -3276061828476429271L;

	@Column(length = 4)
	private String code;

	@Column(length = 4)
	private String startTime;

	@Column(length = 4)
	private String endTime;
	
	/**
	 * 十倍小时数
	 */
	@Column(columnDefinition = "TINYINT")
	private Integer decHours;

	public String getCode() {
		return code;
	}

	public String getStartTime() {
		return startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Integer getDecHours() {
		return decHours;
	}

	public void setDecHours(Integer decHours) {
		this.decHours = decHours;
	}
}
