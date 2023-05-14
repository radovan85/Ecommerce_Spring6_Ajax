package com.radovan.spring.dto;

import java.io.Serializable;
import java.sql.Timestamp;

public class AdminMessageDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer adminMessageId;

	private String text;

	private Timestamp createdAt;

	private String createdAtStr;

	private Integer customerId;

	public Integer getAdminMessageId() {
		return adminMessageId;
	}

	public void setAdminMessageId(Integer adminMessageId) {
		this.adminMessageId = adminMessageId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public String getCreatedAtStr() {
		return createdAtStr;
	}

	public void setCreatedAtStr(String createdAtStr) {
		this.createdAtStr = createdAtStr;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

}
