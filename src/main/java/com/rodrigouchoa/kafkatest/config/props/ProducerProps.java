package com.rodrigouchoa.kafkatest.config.props;

public class ProducerProps {
	private Boolean addTypeInfoHeaders;
	private Long bufferMemory;

	
	public Boolean getAddTypeInfoHeaders() {
		return addTypeInfoHeaders;
	}

	public void setAddTypeInfoHeaders(Boolean addTypeInfoHeaders) {
		this.addTypeInfoHeaders = addTypeInfoHeaders;
	}

	public Long getBufferMemory() {
		return bufferMemory;
	}

	public void setBufferMemory(Long bufferMemory) {
		this.bufferMemory = bufferMemory;
	}
}
