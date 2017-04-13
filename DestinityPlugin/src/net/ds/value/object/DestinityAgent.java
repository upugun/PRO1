package net.ds.value.object;

import java.math.BigDecimal;


public class DestinityAgent {
	
	private String otiumAgentId			= "";
	private String agentCode			= "";
	private String agentName			= "";
	private String address				= "";;
	private String landNo				= "";
	private String faxNo				= "";
	private String email				= "";
	private String VATNo				= null;
	private String contactperson		= "";
	private String freeDays				= "";
	private String agentType			= "";
	private String upload				= "";
	private String description			= "";
	private boolean operatorType		= false;
	private String countryId			= "";
	private String createStatus			= "";
	
	private BigDecimal creditLimit;
	
	
	private int backColor				= 0;
	private int crbal					= 0;
	private int crPeriod				= 0;
	
	public String getOtiumAgentId() {
		return otiumAgentId;
	}
	public void setOtiumAgentId(String otiumAgentId) {
		this.otiumAgentId = otiumAgentId;
	}
	public String getAgentCode() {
		return agentCode;
	}
	public void setAgentCode(String agentCode) {
		this.agentCode = agentCode;
	}
	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getLandNo() {
		return landNo;
	}
	public void setLandNo(String landNo) {
		this.landNo = landNo;
	}
	public String getFaxNo() {
		return faxNo;
	}
	public void setFaxNo(String faxNo) {
		this.faxNo = faxNo;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getVATNo() {
		return VATNo;
	}
	public void setVATNo(String no) {
		VATNo = no;
	}
	public String getContactperson() {
		return contactperson;
	}
	public void setContactperson(String contactperson) {
		this.contactperson = contactperson;
	}
	public String getFreeDays() {
		return freeDays;
	}
	public void setFreeDays(String freeDays) {
		this.freeDays = freeDays;
	}
	public String getAgentType() {
		return agentType;
	}
	public void setAgentType(String agentType) {
		this.agentType = agentType;
	}
	public String getUpload() {
		return upload;
	}
	public void setUpload(String upload) {
		this.upload = upload;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isOperatorType() {
		return operatorType;
	}
	public void setOperatorType(boolean operatorType) {
		this.operatorType = operatorType;
	}
	public String getCountryId() {
		return countryId;
	}
	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}
	public String getCreateStatus() {
		return createStatus;
	}
	public void setCreateStatus(String createStatus) {
		this.createStatus = createStatus;
	}
	public BigDecimal getCreditLimit() {
		return creditLimit;
	}
	public void setCreditLimit(BigDecimal creditLimit) {
		this.creditLimit = creditLimit;
	}
	public int getBackColor() {
		return backColor;
	}
	public void setBackColor(int backColor) {
		this.backColor = backColor;
	}
	public int getCrbal() {
		return crbal;
	}
	public void setCrbal(int crbal) {
		this.crbal = crbal;
	}
	public int getCrPeriod() {
		return crPeriod;
	}
	public void setCrPeriod(int crPeriod) {
		this.crPeriod = crPeriod;
	}
	
	
	
}
