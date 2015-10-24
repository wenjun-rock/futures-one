package fwj.futures.resource.prod.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name="prod_label_futures")
public class LabelFutures extends AbstractPersistable<Integer> {

	private static final long serialVersionUID = -8256570882473387891L;

	private Integer labelId;

	private String labelName;

	private Integer labelRank;

	private String futuresCode;

	private String futuresName;

	public Integer getLabelId() {
		return labelId;
	}

	public String getLabelName() {
		return labelName;
	}

	public Integer getLabelRank() {
		return labelRank;
	}

	public String getFuturesCode() {
		return futuresCode;
	}

	public String getFuturesName() {
		return futuresName;
	}

	public void setLabelId(Integer labelId) {
		this.labelId = labelId;
	}

	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}

	public void setLabelRank(Integer labelRank) {
		this.labelRank = labelRank;
	}

	public void setFuturesCode(String futuresCode) {
		this.futuresCode = futuresCode;
	}

	public void setFuturesName(String futuresName) {
		this.futuresName = futuresName;
	}

}
