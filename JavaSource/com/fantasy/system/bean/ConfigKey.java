package com.fantasy.system.bean;

import java.io.Serializable;

import javax.persistence.Column;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class ConfigKey implements Serializable {

	private static final long serialVersionUID = 6300479353590087147L;

	public static ConfigKey newInstance(String code, String type) {
		return new ConfigKey(code, type);
	}

	/**
	 * 代码
	 */
	@Column(name = "CODE", length = 50)
	private String code;

	/**
	 * 配置类别
	 */
	@Column(name = "TYPE", length = 20)
	private String type;

	public ConfigKey() {
	}

	public ConfigKey(String code, String type) {
		super();
		this.code = code;
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().appendSuper(super.hashCode()).append(this.getType()).append(this.getCode()).toHashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof ConfigKey) {
			ConfigKey key = (ConfigKey) o;
			return new EqualsBuilder().appendSuper(super.equals(o)).append(this.getType(), key.getType()).append(this.getCode(), key.getCode()).isEquals();
		}
		return false;
	}

	@Override
	public String toString() {
		return this.type + ":" + this.code;
	}

}