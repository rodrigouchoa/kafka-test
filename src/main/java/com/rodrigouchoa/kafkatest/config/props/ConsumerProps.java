package com.rodrigouchoa.kafkatest.config.props;

public class ConsumerProps {
	private String groupId;
	private String autoOffsetReset;
	
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getAutoOffsetReset() {
		return autoOffsetReset;
	}
	public void setAutoOffsetReset(String autoOffsetReset) {
		this.autoOffsetReset = autoOffsetReset;
	}

}
