package fwj.futures.resource.entity.price;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "price_realtime", uniqueConstraints = {
		@UniqueConstraint(name = "price_realtime_uni", columnNames = { "priceTime", "code" }) })
public class RealtimeStore extends AbstractPersistable<Integer>implements Comparable<RealtimeStore> {

	private static final long serialVersionUID = -708505925599299652L;

	@Column(columnDefinition = "TIMESTAMP")
	private Date priceTime;

	@Column(length = 4)
	private String code;

	private String data;

	public Date getPriceTime() {
		return priceTime;
	}

	public String getCode() {
		return code;
	}

	public String getData() {
		return data;
	}

	public void setPriceTime(Date priceTime) {
		this.priceTime = priceTime;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setData(String data) {
		this.data = data;
	}

	/*
	 * 按照priceTime升序
	 */
	@Override
	public int compareTo(RealtimeStore that) {
		return this.priceTime.compareTo(that.priceTime);
	}
}
