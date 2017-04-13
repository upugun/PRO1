package net.ds.value.object;

import net.jhl.objects.common.TimeStamp;

public class DesOccupancy {
	
	private String uuid					= "";
	private String propertyId			= "";
	private String roomCategoryId		= "";
	private String roomCategoryName		= "";;
	private int totalRooms 				= 0;
	private int occupiedRooms 			= 0;
	private int availableRooms 			= 0;
	private int outOfOrderRooms 		= 0;
	private double occupancyPercentage 	= 0.00;
	private String occupancyDate		= "";
	
	private TimeStamp timeStamp;
	
	public DesOccupancy(){
		timeStamp = new TimeStamp();
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}

	public String getRoomCategoryId() {
		return roomCategoryId;
	}

	public void setRoomCategoryId(String roomCategoryId) {
		this.roomCategoryId = roomCategoryId;
	}

	public String getRoomCategoryName() {
		return roomCategoryName;
	}

	public void setRoomCategoryName(String roomCategoryName) {
		this.roomCategoryName = roomCategoryName;
	}

	public int getTotalRooms() {
		return totalRooms;
	}

	public void setTotalRooms(int totalRooms) {
		this.totalRooms = totalRooms;
	}

	public int getOccupiedRooms() {
		return occupiedRooms;
	}

	public void setOccupiedRooms(int occupiedRooms) {
		this.occupiedRooms = occupiedRooms;
	}

	public int getAvailableRooms() {
		return availableRooms;
	}

	public void setAvailableRooms(int availableRooms) {
		this.availableRooms = availableRooms;
	}

	public TimeStamp getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(TimeStamp timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getOccupancyDate() {
		return occupancyDate;
	}

	public void setOccupancyDate(String occupancyDate) {
		this.occupancyDate = occupancyDate;
	}

	public double getOccupancyPercentage() {
		return occupancyPercentage;
	}

	public void setOccupancyPercentage(double occupancyPercentage) {
		this.occupancyPercentage = occupancyPercentage;
	}

	public int getOutOfOrderRooms() {
		return outOfOrderRooms;
	}

	public void setOutOfOrderRooms(int outOfOrderRooms) {
		this.outOfOrderRooms = outOfOrderRooms;
	}
	
}
