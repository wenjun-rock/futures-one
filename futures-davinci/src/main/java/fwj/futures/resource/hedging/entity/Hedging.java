package fwj.futures.resource.hedging.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity()
public class Hedging extends AbstractPersistable<Integer> {

	private static final long serialVersionUID = 7666569451056780578L;

	@Column(length = 10)
	private String name;

	@Column(length = 50)
	private String description;

	@Column(length = 400)
	private String expression;
	
	@Column(columnDefinition = "DATE")
	private Date StartDt;

	@Column(columnDefinition = "DATE")
	private Date endDt;

	@Column(precision = 8)
	private int q1;
	
	@Column(precision = 8)
	private int q3;
	
	@Column(precision = 8)
	private int mid;
	
	@Column(precision = 8)
	private int down;
	
	@Column(precision = 8)
	private int up;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public Date getStartDt() {
		return StartDt;
	}

	public Date getEndDt() {
		return endDt;
	}

	public void setStartDt(Date startDt) {
		StartDt = startDt;
	}

	public void setEndDt(Date endDt) {
		this.endDt = endDt;
	}

	public int getQ1() {
		return q1;
	}

	public void setQ1(int q1) {
		this.q1 = q1;
	}

	public int getQ3() {
		return q3;
	}

	public void setQ3(int q3) {
		this.q3 = q3;
	}

	public int getMid() {
		return mid;
	}

	public void setMid(int mid) {
		this.mid = mid;
	}

	public int getDown() {
		return down;
	}

	public void setDown(int down) {
		this.down = down;
	}

	public int getUp() {
		return up;
	}

	public void setUp(int up) {
		this.up = up;
	}
	
}
