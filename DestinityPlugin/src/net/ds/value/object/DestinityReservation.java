package net.ds.value.object;

import java.sql.Date;


public class DestinityReservation {
	
	private String otiumResId			= "";
	private String desResNo				= "";
	private String title				= "";
	private String firstName			= "";
	private String lastName				= "";;
	private String passportNo			= "";
	private String email				= "";
	private String landNo				= "";
	private String arrivalMeal			= "";
	private String leaveMeal			= "";
	private String address				= "";
	private String country				= "";
	private String remarks				= "";
	private String packageCode			= "";
	private String agentId				= "";
	private String marketCode			= "";
	private String rateCodeId			= "";
	private String hotelCode			= "";
	private String childSupplement		= null;
	private String mealBasis			= "";
	private String createStatus			= "";
	private String resStatus			= "";
	private String userId				= "";
	private String guestId				= "";
	private String voucherNo			= "";
	private String tourNo				= "";
	
	private String segment				= "";
	private String natianaliity			= "";
	private String bookingType			= "";
	private String guestType			= "";
	private boolean complementaryType	= false;
	
	private String roomXml;
	
	private Date arrivalDate;
	private Date departureDate;
	
	private int noOfAdults				= 0;
	private int noOfChildren			= 0;
	public String getDesResNo() {
		return desResNo;
	}
	public void setDesResNo(String desResNo) {
		this.desResNo = desResNo;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getPassportNo() {
		return passportNo;
	}
	public void setPassportNo(String passportNo) {
		this.passportNo = passportNo;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getLandNo() {
		return landNo;
	}
	public void setLandNo(String landNo) {
		this.landNo = landNo;
	}
	public String getArrivalMeal() {
		return arrivalMeal;
	}
	public void setArrivalMeal(String arrivalMeal) {
		this.arrivalMeal = arrivalMeal;
	}
	public String getLeaveMeal() {
		return leaveMeal;
	}
	public void setLeaveMeal(String leaveMeal) {
		this.leaveMeal = leaveMeal;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getPackageCode() {
		return packageCode;
	}
	public void setPackageCode(String packageCode) {
		this.packageCode = packageCode;
	}
	public String getAgentId() {
		return agentId;
	}
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	public String getMarketCode() {
		return marketCode;
	}
	public void setMarketCode(String marketCode) {
		this.marketCode = marketCode;
	}
	public String getRateCodeId() {
		return rateCodeId;
	}
	public void setRateCodeId(String rateCodeId) {
		this.rateCodeId = rateCodeId;
	}
	public String getHotelCode() {
		return hotelCode;
	}
	public void setHotelCode(String hotelCode) {
		this.hotelCode = hotelCode;
	}
	public String getChildSupplement() {
		return childSupplement;
	}
	public void setChildSupplement(String childSupplement) {
		this.childSupplement = childSupplement;
	}
	public String getMealBasis() {
		return mealBasis;
	}
	public void setMealBasis(String mealBasis) {
		this.mealBasis = mealBasis;
	}
	public String getCreateStatus() {
		return createStatus;
	}
	public void setCreateStatus(String createStatus) {
		this.createStatus = createStatus;
	}
	public String getResStatus() {
		return resStatus;
	}
	public void setResStatus(String resStatus) {
		this.resStatus = resStatus;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getGuestId() {
		return guestId;
	}
	public void setGuestId(String guestId) {
		this.guestId = guestId;
	}
	public String getVoucherNo() {
		return voucherNo;
	}
	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}
	public String getTourNo() {
		return tourNo;
	}
	public void setTourNo(String tourNo) {
		this.tourNo = tourNo;
	}
	public String getRoomXml() {
		return roomXml;
	}
	public void setRoomXml(String roomXml) {
		this.roomXml = roomXml;
	}
	public Date getArrivalDate() {
		return arrivalDate;
	}
	public void setArrivalDate(Date arrivalDate) {
		this.arrivalDate = arrivalDate;
	}
	public Date getDepartureDate() {
		return departureDate;
	}
	public void setDepartureDate(Date departureDate) {
		this.departureDate = departureDate;
	}
	public int getNoOfAdults() {
		return noOfAdults;
	}
	public void setNoOfAdults(int noOfAdults) {
		this.noOfAdults = noOfAdults;
	}
	public int getNoOfChildren() {
		return noOfChildren;
	}
	public void setNoOfChildren(int noOfChildren) {
		this.noOfChildren = noOfChildren;
	}
	public String getOtiumResId() {
		return otiumResId;
	}
	public void setOtiumResId(String otiumResId) {
		this.otiumResId = otiumResId;
	}
	public String getSegment() {
		return segment;
	}
	public void setSegment(String segment) {
		this.segment = segment;
	}
	public String getNatianaliity() {
		return natianaliity;
	}
	public void setNatianaliity(String natianaliity) {
		this.natianaliity = natianaliity;
	}
	public String getBookingType() {
		return bookingType;
	}
	public void setBookingType(String bookingType) {
		this.bookingType = bookingType;
	}
	public boolean isComplementaryType() {
		return complementaryType;
	}
	public void setComplementaryType(boolean complementaryType) {
		this.complementaryType = complementaryType;
	}
	public String getGuestType() {
		return guestType;
	}
	public void setGuestType(String guestType) {
		this.guestType = guestType;
	}
	
	 
	
	
	
}
