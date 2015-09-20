package fwj.futures.resource.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class Comment extends AbstractPersistable<Integer> {

	private static final long serialVersionUID = 4785485505130102439L;

	@Column(columnDefinition = "TEXT")
	private String content;

	@Column(columnDefinition = "TINYINT")
	private int relativeType;

	@Column(length = 10)
	private String relativeKey;

	@Column(columnDefinition = "TIMESTAMP")
	private Date commitTime;

	public String getContent() {
		return content;
	}

	public int getRelativeType() {
		return relativeType;
	}

	public String getRelativeKey() {
		return relativeKey;
	}

	public Date getCommitTime() {
		return commitTime;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setRelativeType(int relativeType) {
		this.relativeType = relativeType;
	}

	public void setRelativeKey(String relativeKey) {
		this.relativeKey = relativeKey;
	}

	public void setCommitTime(Date commitTime) {
		this.commitTime = commitTime;
	}

}
