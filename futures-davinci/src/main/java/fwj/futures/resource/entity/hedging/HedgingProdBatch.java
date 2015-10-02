package fwj.futures.resource.entity.hedging;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity()
public class HedgingProdBatch extends AbstractPersistable<Integer> {

	private static final long serialVersionUID = 518216574212298838L;

	@Column(columnDefinition = "DATE")
	private Date StartDt;
	
	@Column(columnDefinition = "DATE")
	private Date endDt;
	
	@Column(columnDefinition = "TIMESTAMP")
	private Date runDt;

	public Date getStartDt() {
		return StartDt;
	}

	public Date getEndDt() {
		return endDt;
	}

	public Date getRunDt() {
		return runDt;
	}

	public void setStartDt(Date startDt) {
		StartDt = startDt;
	}

	public void setEndDt(Date endDt) {
		this.endDt = endDt;
	}

	public void setRunDt(Date runDt) {
		this.runDt = runDt;
	}
}
