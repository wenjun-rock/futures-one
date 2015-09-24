package fwj.futures.resource.entity.com;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name="com_comment")
public class Comment extends AbstractPersistable<Integer> {

	private static final long serialVersionUID = 4785485505130102439L;

	@Column(columnDefinition = "TEXT")
	private String content;

	@Column(columnDefinition = "TINYINT")
	private int type;

	@Column(length = 10)
	private String rltKey;
	
	@Column(length = 20)
	private String rltName;

	@Column(columnDefinition = "TIMESTAMP")
	private Date commitTime;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getRltKey() {
		return rltKey;
	}

	public void setRltKey(String rltKey) {
		this.rltKey = rltKey;
	}

	public String getRltName() {
		return rltName;
	}

	public void setRltName(String rltName) {
		this.rltName = rltName;
	}

	public Date getCommitTime() {
		return commitTime;
	}

	public void setCommitTime(Date commitTime) {
		this.commitTime = commitTime;
	}

}
