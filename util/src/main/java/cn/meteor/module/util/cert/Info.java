package cn.meteor.module.util.cert;

import java.math.BigInteger;
import java.util.Date;

public class Info {

	private Date startDate;
	
	private Date endDate;
	
	private BigInteger serialNumber;

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public BigInteger getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(BigInteger serialNumber) {
		this.serialNumber = serialNumber;
	}
	
	
	
}
