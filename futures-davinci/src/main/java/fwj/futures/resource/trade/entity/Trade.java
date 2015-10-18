package fwj.futures.resource.trade.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class Trade extends AbstractPersistable<Integer> {

	private static final long serialVersionUID = -7230382409707617510L;

	@Column(length = 50)
	private String name;

	@Column(columnDefinition = "TINYINT")
	private int type;

	@Column(columnDefinition = "DATE")
	private Date startDt;

	@Column(columnDefinition = "DATE")
	private Date endDt;

	private Integer maxMargin;
	
	private Integer vol;
	
	private Integer margin;

	private Integer completeProfit;

	public String getName() {
		return name;
	}

	public int getType() {
		return type;
	}

	public Integer getMaxMargin() {
		return maxMargin;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setMaxMargin(Integer maxMargin) {
		this.maxMargin = maxMargin;
	}

	public Integer getCompleteProfit() {
		return completeProfit;
	}

	public void setCompleteProfit(Integer completeProfit) {
		this.completeProfit = completeProfit;
	}

	public Date getStartDt() {
		return startDt;
	}

	public Date getEndDt() {
		return endDt;
	}

	public void setStartDt(Date startDt) {
		this.startDt = startDt;
	}

	public void setEndDt(Date endDt) {
		this.endDt = endDt;
	}

	public Integer getVol() {
		return vol;
	}

	public Integer getMargin() {
		return margin;
	}

	public void setVol(Integer vol) {
		this.vol = vol;
	}

	public void setMargin(Integer margin) {
		this.margin = margin;
	}

}
