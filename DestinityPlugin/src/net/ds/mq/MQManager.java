package net.ds.mq;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import javax.jms.Message;
import javax.jms.TextMessage;


import flex.messaging.util.UUIDUtils;

import jh.mq.jre.MQMessages;
import jh.mq.jre.MQMessagesDAO;
import jh.mq.jre.MQOriginalDAO;
import net.ds.dao.CreateAgentDAO;
import net.ds.dao.CreateReservationDAO;
import net.ds.dao.SQLDAO;
import net.ds.value.object.DestinityAgent;
import net.ds.value.object.DestinityReservation;
import net.jhl.constants.JHConstants;
import net.jhl.dao.AgentDetailsDAO;
import net.jhl.dao.AgentMarketsDAO;
import net.jhl.dao.AgentTypeDAO;
import net.jhl.dao.ConfirmationVoucherDetailsDAO;
import net.jhl.dao.GuestDAO;
import net.jhl.dao.GuestPreferenceDAO;
import net.jhl.dao.PropertyDAO;
import net.jhl.dao.RateCodeRoomRateDAO;
import net.jhl.dao.ReservationDAO;
import net.jhl.dao.ReservationDetailsDAO;
import net.jhl.dao.ReservationDiscountsDAO;
import net.jhl.dao.ReservationPaymentDetailsDAO;
import net.jhl.dao.RoomCategoryByHotelDAO;
import net.jhl.dao.RoomCategoryDAO;
import net.jhl.dao.RoomFacilitiesByPropertyDAO;
import net.jhl.dao.RoomFacilitiesMasterDAO;
import net.jhl.dao.TaxDAO;
import net.jhl.dao.ratecode.AppliedAgentMarketsDAO;
import net.jhl.dao.ratecode.BaseRateDAO;
import net.jhl.dao.ratecode.DiscountApprovalRequestDAO;
import net.jhl.dao.ratecode.DiscountConditionsDAO;
import net.jhl.dao.ratecode.DiscountsAndSupplementMappingDAO;
import net.jhl.dao.ratecode.DiscountsDAO;
import net.jhl.dao.ratecode.OtherCostsDAO;
import net.jhl.dao.ratecode.OtherCostsMasterDAO;
import net.jhl.dao.ratecode.RateCodeDAO;
import net.jhl.dao.ratecode.RoomCatagoryCostDAO;
import net.jhl.dao.ratecode.SeasonalRateDAO;
import net.jhl.dao.ratecode.SeasonsDAO;
import net.jhl.dao.ratecode.SpecialOffersDAO;
import net.jhl.objects.AgentDetails;
import net.jhl.objects.AgentType;
import net.jhl.objects.CheckInDetails;
import net.jhl.objects.CheckOutDetails;
import net.jhl.objects.ConfirmationVoucherDetails;
import net.jhl.objects.ContactDetails;
import net.jhl.objects.Guest;
import net.jhl.objects.GuestPreference;
import net.jhl.objects.GuideDetails;
import net.jhl.objects.MQOriginal;
import net.jhl.objects.MealDetails;
import net.jhl.objects.MessageObject;
import net.jhl.objects.PersonalDetails;
import net.jhl.objects.Property;
import net.jhl.objects.RateCodeRoomRate;
import net.jhl.objects.Reservation;
import net.jhl.objects.ReservationDetails;
import net.jhl.objects.ReservationDiscounts;
import net.jhl.objects.ReservationPaymentDetails;
import net.jhl.objects.RoomCategory;
import net.jhl.objects.RoomCategoryByHotel;
import net.jhl.objects.RoomFacilitiesByProperty;
import net.jhl.objects.RoomFacilitiesMaster;
import net.jhl.objects.common.JHMQMessage;
import net.jhl.objects.common.Tax;
import net.jhl.objects.common.TimeStamp;
import net.jhl.objects.ratecode.AgentMarkets;
import net.jhl.objects.ratecode.AppliedAgentMarkets;
import net.jhl.objects.ratecode.BaseRate;
import net.jhl.objects.ratecode.Commission;
import net.jhl.objects.ratecode.Condition;
import net.jhl.objects.ratecode.DiscountApprovalRequest;
import net.jhl.objects.ratecode.DiscountConditions;
import net.jhl.objects.ratecode.Discounts;
import net.jhl.objects.ratecode.DiscountsAndSupplementMapping;
import net.jhl.objects.ratecode.OtherCosts;
import net.jhl.objects.ratecode.OtherCostsMaster;
import net.jhl.objects.ratecode.RateCode;
import net.jhl.objects.ratecode.RoomCatagoryCost;
import net.jhl.objects.ratecode.SeasonalBaseRate;
import net.jhl.objects.ratecode.Seasons;
import net.jhl.objects.ratecode.SpecialOffers;
import net.jwt.dao.AccessDetailsOfGroupsDAO;
import net.jwt.dao.AccessGroupDetailsDAO;
import net.jwt.dao.AccessGroupMasterDAO;
import net.jwt.dao.UserDAO;
import net.jwt.system.PropertyFileLoader;
import net.jwt.value.objects.AccessDetailsOfGroups;
import net.jwt.value.objects.AccessGroupDetails;
import net.jwt.value.objects.AccessGroupMaster;
import net.jwt.value.objects.Country;
import net.jwt.value.objects.Department;
import net.jwt.value.objects.Manager;
import net.jwt.value.objects.RoleMaster;
import net.jwt.value.objects.User;

public class MQManager extends SQLDAO{
	
	private static String blockClose	= JHConstants.MQ_MAIN_BLOCK_CLOSE_CHAR;
	private static String spch 			= JHConstants.MQ_SEPERATOR_CHAR;
	
	private ArrayList<MQMessages> errorList = new ArrayList<MQMessages>();
	private User _loggedInUser;
	
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		String str  = "÷COLOMBO~KANDY~fbdadb8b-7caa-4a63-bdd1-bdf28a62b54c~12~MQ_UPDATE_ROOM_CATEGORIES~^~10~083001fe-1c88-442c-b50e-cb5229015de1^07^" +
				"TEST^ACTIVE^2016/07/28 09:46:32^198a076e-adb9-4d29-95cc-8ec38c40506f^udeshika^2016-07-28 09:46:32.0^198a076e-adb9-4d29-95cc-8ec38c40506f^udeshika~~083001fe-1c88-442c-b50e-cb5229015de1~";
		
		String str2 = "HQ~HQ~665cb10d-4b7a-4bda-97a1-f4df998365b4~8~MQ_UPDATE_RESERVATION_PAYMENT_DETAILS~^~11~d6face3a-18d3-4a4c-bf97-208293cf6051^^eefd4ae5-039f-4270-be11-9fcc100b102b^" +
				"7.43^ACTIVE^2016/03/28 16:02:17^1f240a92-c772-4b26-9446-37f926881a27^nisal^^1f240a92-c772-4b26-9446-37f926881a27^nisal~~d6face3a-18d3-4a4c-bf97-208293cf6051~";
		
		String str3 = "HQ~HQ~2a7c74d4-8fe3-4925-9b66-a60c7b317324~9~MQ_UPDATE_RESERVATION_PAYMENT_DETAILS~^~11~b2da8fd0-6509-4861-90dc-b91e5f72fe4a^^4f55d8ec-070a-4fe7-88e4-632e6ec2785c^" +
				"15.59^ACTIVE^2016/03/28 16:02:17^1f240a92-c772-4b26-9446-37f926881a27^nisal^^1f240a92-c772-4b26-9446-37f926881a27^nisal~~b2da8fd0-6509-4861-90dc-b91e5f72fe4a~";
		
		String str4 = "HQ~HQ~3a042ad8-b1f0-4c16-a6a9-a9a2cdc76b73~10~MQ_UPDATE_RESERVATION_PAYMENT_DETAILS~^~11~aa0fd125-8a54-4c5d-99c9-51d17da017bb^^TOTAL^171.52^ACTIVE^" +
				"2016/03/28 16:02:17^1f240a92-c772-4b26-9446-37f926881a27^nisal^^1f240a92-c772-4b26-9446-37f926881a27^nisal~~aa0fd125-8a54-4c5d-99c9-51d17da017bb~";
		
		String str5 = "HQ~HQ~d6f08840-2462-44e6-a32c-2b9579e196ca~11~MQ_UPDATE_RESERVATION~^~48~651482FD-79AF-AD9C-244B-BCC8013C9635^2016^4^^^^^^^^^6d257fb4-aeb3-4b8a-8b62-ca669f4b31d9^" +
				"2016-456-1^2016-789^^2017-02-14^^2017-02-15^^^^^^0^1^0^0^0^0^ACTIVE^2016/03/28 16:02:17^1f240a92-c772-4b26-9446-37f926881a27^nisal^^1f240a92-c772-4b26-9446-37f926881a27^" +
				"nisal^^CASH^148.50^1^Manuka^^CONFIRMED^FIT^Manuka Perera^manuka@jl.clk^07775452^1198^YALA^~~651482FD-79AF-AD9C-244B-BCC8013C9635~";
		
		JHMQMessage mq  =  new JHMQMessage();
		mq.setMqMessage(str);
		
		JHMQMessage mq2  =  new JHMQMessage();
		mq2.setMqMessage(str2);
		
		JHMQMessage mq3  =  new JHMQMessage();
		mq3.setMqMessage(str3);
		
		JHMQMessage mq4  =  new JHMQMessage();
		mq4.setMqMessage(str4);
		
		JHMQMessage mq5  =  new JHMQMessage();
		mq5.setMqMessage(str5);
		
		ArrayList<JHMQMessage> mqList = new ArrayList<JHMQMessage>();
		mqList.add(mq);
		//mqList.add(mq2);
		//mqList.add(mq3);
		//mqList.add(mq4);
		//mqList.add(mq5);
		
		MQManager mqm = new MQManager();
		mqm.recieveMessage(mqList);
		
	}
	
	public MQManager()
	{
		_loggedInUser = new User();
		_loggedInUser.setUuid("b-o-t-i-d");
		_loggedInUser.setUserName("bot");
	}
	
	
	public MessageObject translateMessages(Message message)
    {
    	MessageObject 
    		mo = new MessageObject();
    	
    	if(message != null)
    		{
	    	try
	    	{
	    		mo.setContentType("text/xml");
	    		mo.setTimeReceived(message.getJMSTimestamp());
	    		
	    		if (message instanceof TextMessage) 
	    		{
	                TextMessage textMessage = (TextMessage) message;
	                mo.setMessage(textMessage.getText());
	            } else {
	            	 mo.setMessage(message.toString());
	            }
	    		
	    		//System.out.println("\n\nA-"+message);
	    		
	    		String[] parts = null;
	    		if (mo.getMessage().contains("^")) {
	    			parts = mo.getMessage().split("\\^");
	    		} else {
	    		    throw new IllegalArgumentException("Received message splitting has errors: MQManager {160}");
	    		}
	    		
	    		mo.setSender(message.getJMSMessageID());
	    		
	    		if(parts != null)
	    		{
	    			for(int i =0; i<parts.length; i++)
		    		{
	    				switch(i){
					     case  0:
					    	 mo.setContentType(parts[i]);
					        break;
					     case  1:
					    	 mo.setMessageType(parts[1]);
					        break;
					     case  2:
					    	 mo.setAction(parts[i]);
					        break;
					     case  3:
					    	 mo.setImpactTo(parts[i]);
					        break;
					     case  4:
					    	 mo.setSenderName(parts[i]);
					        break;
	    				}
		    		}
		    		
	    		}
	    		
	    		mo.setUuid(UUIDUtils.createUUID());
	    		mo.setMechineId(message.getJMSMessageID());
	    		
	    		System.out.println(mo.getMessage());
	    		
	    	}catch (Exception e) {
	    		System.out.println("Incoming message translation failed !!! {MQAgent.java@121");
				e.printStackTrace();
			}
    	}
    	
    	return mo;	
    }
    
 	public ArrayList<MessageObject> translateAllMessages(ArrayList<Message> l)
    {
    	ArrayList<MessageObject> list = new ArrayList<MessageObject>();
    	
    	for(int c=0;c<l.size(); c++)
    		list.add(translateMessages(l.get(c)));
    	
    	return list;	
    }
 	
	public void recieveMQMessage(Message message) throws Exception
	{
		MessageObject mo = translateMessages(message);
		
		if(mo != null)
			recieveMessage(mo.getMessage());
	}
	
	public void recieveMQMessage(ArrayList<MessageObject> list) throws Exception
	{
		for(int i=0; i<list.size(); i++)
		{
			recieveMessage(list.get(i).getMessage());
			//recieveMessage(list.get(i));
		}
	}
	
	public void recieveMQMessage(MessageObject mo) throws Exception
	{
		recieveMessage(mo.getMessage());
	}
	
	public void recieveMessage(ArrayList<JHMQMessage> list) throws Exception
	{
		for(int i=0; i<list.size(); i++)
		{
			recieveMessage(list.get(i));
		}
	}
	
	public void recieveMessage(JHMQMessage jhmq)throws Exception
	{
		recieveMessage(jhmq.getMqMessage());
	}
	
	public void recieveMessage(String str) throws Exception
	{
		MQMessages mq  		= preapareAndCreateMQ(str);
		mq.setSequence(mq.getSequence());
		String reciever		= PropertyFileLoader.getOtiumPropValues().getLocation();
		System.out.println("MQManager.recieveMessage() reciever : " +  reciever);
		System.out.println("MQManager.recieveMessage() mq.getReceiver() : " +  mq.getReceiver());
		if(reciever.equals(mq.getReceiver()))// validate the receiver 
		{	
			creatMQOriginal(str); // write to mq original first
			MQMessages createdMq  	= creatMQ(mq);// create mq record
			
			if(createdMq !=null)
			{
				if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_RESERVATION))
				{
					createReservation(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_RESERVATION_DETAILS))	
				{
					createReservationDetails(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_RESERVATION_PAYMENT_DETAILS))	
				{
					createReservationPaymentDetails(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_GUEST_DETAILS))
				{
					createGuest(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_AGENT_DETAILS))	
				{
					createAgent(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_RATE_CODE_DETAILS))	
				{
					createRateCode(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_RATE_CODE_ROOM_DETAILS))	
				{
					createRateCodeRoomRate(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_ROOM_CATEGORIES))	
				{
					createRoomCategory(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_ROOM_CATEGORY_MAPPING))	
				{
					createRoomCategoryByHotel(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_AGENT_MARKETS))	
				{
					createAgentMarket(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_AGENT_TYPE))	
				{
					createAgenType(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_DISCOUNTS))	
				{
					createDiscount(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_SEASONS))	
				{
					createSeason(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_OFFERS))	
				{
					createOffers(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_DISCOUNT_CONDITIONS))	
				{
					createDiscountConditions(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_GUEST_PREFERENCE))	
				{
					createGuestPreference(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_DISCOUNTS_AND_SUPPLEMENT_MAPPING))	
				{
					createDiscountMapping(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_BASE_RATE_DETAILS))	
				{
					createBaseRate(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_SEASONAL_BASE_RATE_DETAILS))	
				{
					createSeasonalBaseRate(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_RATE_CODE_MAPPING_DETAILS))	
				{
					createRateCodeMapping(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_RATE_CODE_OTHER_COST))	
				{
					createOtherCost(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_RATE_CODE_ROOM_CATEGORY_COST))	
				{
					createRoomCategoryCost(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_DELETE_RESERVATION_DETAILS))	
				{
					deleteReservationDetails(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_DELETE_ROOM_CATEGORY_MAPPING))	
				{
					deleteRoomcategoryMapping(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_DELETE_RATE_CODE_OTHER_COST))	
				{
					deleteOtherCost(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_DELETE_RATE_CODE_ROOM_CATEGORY_COST))	
				{
					deleteRoomCategoryCost(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_DELETE_RATE_CODE_MAPPING_DETAILS))	
				{
					deleteRateCodeMapping(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_ROOM_FACILITIES))	
				{
					createRoomFacility(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_ROOM_FACILITIES_BY_PROPERTY))	
				{
					createRoomFacilityByProperty(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_DELETE_ROOM_FACILITIES_BY_PROPERTY))	
				{
					deleteRoomFacilityByProperty(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_TAX_MASTER))	
				{
					createTaxMaster(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_OTHER_COST_MASTER))	
				{
					createOtherCostMaster(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_RESERVATION_DISCOUNTS))	
				{
					createReservationDiscountsMaster(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_DELETE_RESERVATION_DISCOUNTS))	
				{
					deleteReservationDiscountsMaster(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_DELETE_DISCOUNTS_AND_SUPPLEMENT_MAPPING))	
				{
					deleteDiscountMapping(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_PROPERTY_MASTER))	
				{
					createProperty(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_DESTINITY_RESERVATION))	
				{
					createDestinityReservation(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_DESTINITY_AGENT))	
				{
					createDestinityAgent(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_RESERVATION_DESTINITY_NO))	
				{
					createReservation(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_RESERVATION_DESTINITY_STATUS))	
				{
					createReservation(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_USER_DETAILS))	
				{
					createUser(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_ACCESS_GROUP_MASTER))	
				{
					createAccessGroupMaster(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_ACCESS_GROUP_DETAILS))	
				{
					createAccessGroupDetails(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_DELETE_ACCESS_GROUP_DETAILS))	
				{
					deleteAccessGroupDetails(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_ACCESS_DETAILS_OF_GROUP))	
				{
					createAccessDetailsOfGroups(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_DISCOUNTS_USED_STATUS))	
				{
					changeDiscountStatus(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_DISCOUNT_APPROVE_REQUEST))	
				{
					createDiscountApprovalRequest(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_CONFIRMATION_VOUCHER_DETAILS))	
				{
					createConfirmationVoucherDetails(createdMq);
				}
				else
					errorList.add(createdMq);
			}
		}
	}
	
	public void recieveMessageList(ArrayList<MQMessages> list) throws Exception
	{
		for(int i=0; i<list.size(); i++)
		{
			recieveMessage(list.get(i));
		}
	}
	
	public void recieveMessage(MQMessages mq) throws Exception
	{
		String reciever		= PropertyFileLoader.getOtiumPropValues().getLocation();
		System.out.println("MQManager.recieveMessage() reciever : " +  reciever);
		System.out.println("MQManager.recieveMessage() mq.getReceiver() : " +  mq.getReceiver());
		if(reciever.equals(mq.getReceiver()))// validate the receiver 
		{	
			MQMessages createdMq  	= mq;// create mq record
			
			if(createdMq !=null)
			{
				if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_RESERVATION))
				{
					createReservation(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_RESERVATION_DETAILS))	
				{
					createReservationDetails(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_RESERVATION_PAYMENT_DETAILS))	
				{
					createReservationPaymentDetails(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_GUEST_DETAILS))
				{
					createGuest(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_AGENT_DETAILS))	
				{
					createAgent(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_RATE_CODE_DETAILS))	
				{
					createRateCode(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_RATE_CODE_ROOM_DETAILS))	
				{
					createRateCodeRoomRate(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_ROOM_CATEGORIES))	
				{
					createRoomCategory(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_ROOM_CATEGORY_MAPPING))	
				{
					createRoomCategoryByHotel(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_AGENT_MARKETS))	
				{
					createAgentMarket(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_AGENT_TYPE))	
				{
					createAgenType(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_DISCOUNTS))	
				{
					createDiscount(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_SEASONS))	
				{
					createSeason(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_OFFERS))	
				{
					createOffers(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_DISCOUNT_CONDITIONS))	
				{
					createDiscountConditions(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_GUEST_PREFERENCE))	
				{
					createGuestPreference(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_DISCOUNTS_AND_SUPPLEMENT_MAPPING))	
				{
					createDiscountMapping(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_BASE_RATE_DETAILS))	
				{
					createBaseRate(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_SEASONAL_BASE_RATE_DETAILS))	
				{
					createSeasonalBaseRate(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_RATE_CODE_MAPPING_DETAILS))	
				{
					createRateCodeMapping(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_RATE_CODE_OTHER_COST))	
				{
					createOtherCost(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_RATE_CODE_ROOM_CATEGORY_COST))	
				{
					createRoomCategoryCost(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_DELETE_RESERVATION_DETAILS))	
				{
					deleteReservationDetails(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_DELETE_ROOM_CATEGORY_MAPPING))	
				{
					deleteRoomcategoryMapping(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_DELETE_RATE_CODE_OTHER_COST))	
				{
					deleteOtherCost(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_DELETE_RATE_CODE_ROOM_CATEGORY_COST))	
				{
					deleteRoomCategoryCost(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_DELETE_RATE_CODE_MAPPING_DETAILS))	
				{
					deleteRateCodeMapping(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_ROOM_FACILITIES))	
				{
					createRoomFacility(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_ROOM_FACILITIES_BY_PROPERTY))	
				{
					createRoomFacilityByProperty(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_DELETE_ROOM_FACILITIES_BY_PROPERTY))	
				{
					deleteRoomFacilityByProperty(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_TAX_MASTER))	
				{
					createTaxMaster(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_OTHER_COST_MASTER))	
				{
					createOtherCostMaster(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_RESERVATION_DISCOUNTS))	
				{
					createReservationDiscountsMaster(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_DELETE_RESERVATION_DISCOUNTS))	
				{
					deleteReservationDiscountsMaster(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_DELETE_DISCOUNTS_AND_SUPPLEMENT_MAPPING))	
				{
					deleteDiscountMapping(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_PROPERTY_MASTER))	
				{
					createProperty(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_DESTINITY_RESERVATION))	
				{
					createDestinityReservation(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_DESTINITY_AGENT))	
				{
					createDestinityAgent(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_RESERVATION_DESTINITY_NO))	
				{
					createReservation(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_RESERVATION_DESTINITY_STATUS))	
				{
					createReservation(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_USER_DETAILS))	
				{
					createUser(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_ACCESS_GROUP_MASTER))	
				{
					createAccessGroupMaster(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_ACCESS_GROUP_DETAILS))	
				{
					createAccessGroupDetails(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_DELETE_ACCESS_GROUP_DETAILS))	
				{
					deleteAccessGroupDetails(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_ACCESS_DETAILS_OF_GROUP))	
				{
					createAccessDetailsOfGroups(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_DISCOUNTS_USED_STATUS))	
				{
					changeDiscountStatus(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_DISCOUNT_APPROVE_REQUEST))	
				{
					createDiscountApprovalRequest(createdMq);
				}
				else if(createdMq.getCommand().equals(JHConstants.MQ_UPDATE_CONFIRMATION_VOUCHER_DETAILS))	
				{
					createConfirmationVoucherDetails(createdMq);
				}
				else
					errorList.add(createdMq);
			}
		}
	}
	
	
	private void createReservation(MQMessages mq)
	{
		Reservation res = prepareReservation(mq.getMessage());
		res.setFollowedByMQ(false);
		if(res!=null)
		{
			ReservationDAO resDAO = new ReservationDAO();
			//resDAO.setLogedInUser(logedInUser);
			ArrayList<Reservation> list = new ArrayList<Reservation>();
			list.add(res);
			
			try
			{
				resDAO.setLogedInUser(_loggedInUser);
				
				if(mq.getCommand().equals(JHConstants.MQ_UPDATE_RESERVATION))
						resDAO.create(res, false);
				else if(mq.getCommand().equals(JHConstants.MQ_UPDATE_RESERVATION_DESTINITY_NO))
					resDAO.updateDesResNo(list);
				else if(mq.getCommand().equals(JHConstants.MQ_UPDATE_RESERVATION_DESTINITY_STATUS))
					resDAO.updateDesUploadStatus(list);
				
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_OK);
			}
			catch (Exception e) {
				e.printStackTrace();
				System.out.println("MQManager.createReservation() failed");
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_FAILED);
			}
		}
		else
			errorList.add(mq);
	}
	
	private void createReservationDetails(MQMessages mq)
	{
		ReservationDetails res = prepareReservationDetails(mq.getMessage());
		res.setFollowedByMQ(false);
		
		if(res!=null)
		{
			
			ReservationDetailsDAO resDAO = new ReservationDetailsDAO();
			//resDAO.setLogedInUser(logedInUser);
			try
			{
				resDAO.setLogedInUser(_loggedInUser);
				resDAO.setReservationId(res.getReservationId());
				resDAO.create(res);
				
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_OK);
			}
			catch (Exception e) {
				System.out.println("MQManager.createReservationDetails() failed");
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_FAILED);
			}
		}
		else
			errorList.add(mq);
	}
	
	private void createReservationPaymentDetails(MQMessages mq)
	{
		ReservationPaymentDetails res = prepareReservationPaymentDetails(mq.getMessage());
		res.setFollowedByMQ(false);
		
		if(res!=null)
		{
			ReservationPaymentDetailsDAO resDAO = new ReservationPaymentDetailsDAO();
			//resDAO.setLogedInUser(logedInUser);
			try
			{
				resDAO.setLogedInUser(_loggedInUser);
				resDAO.setReservationId(res.getReservationId());
				resDAO.create(res);
				
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_OK);
			}
			catch (Exception e) {
				System.out.println("MQManager.createReservationPaymentDetails() failed");
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_FAILED);
			}
		}
		else
			errorList.add(mq);
	}
	
	
	private void createGuest(MQMessages mq)
	{
		Guest _obj 		= prepareGuest(mq.getMessage());
		
		try
		{
			String location	= PropertyFileLoader.getOtiumPropValues().getLocationType();
			
			if(location.equals(JHConstants.APP_LOCATION_TYPE_HeadOffice))// if local server is HQ then need to send masterdata mq's to all properties
				_obj.setFollowedByMQ(true);
			else
				_obj.setFollowedByMQ(false);
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		
		if(_obj!=null)
		{
			GuestDAO dao = new GuestDAO();
			//resDAO.setLogedInUser(logedInUser);
			try
			{
				ArrayList<Guest> list = new ArrayList<Guest>();
				list.add(_obj);
				dao.setLogedInUser(_loggedInUser);
				dao.create(list);
				
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_OK);
			}
			catch (Exception e) {
				System.out.println("MQManager.createReservation() failed");
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_FAILED);
			}
		}
		else
			errorList.add(mq);
	}
	
	private void createAgent(MQMessages mq)
	{
		AgentDetails _obj = prepareAgent(mq.getMessage());
		try
		{
			String location	= PropertyFileLoader.getOtiumPropValues().getLocationType();
			
			if(location.equals(JHConstants.APP_LOCATION_TYPE_HeadOffice))// if local server is HQ then need to send masterdata mq's to all properties
				_obj.setFollowedByMQ(true);
			else
				_obj.setFollowedByMQ(false);
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		
		if(_obj!=null)
		{
			AgentDetailsDAO dao = new AgentDetailsDAO();
			//resDAO.setLogedInUser(logedInUser);
			try
			{
				ArrayList<AgentDetails> list = new ArrayList<AgentDetails>();
				list.add(_obj);
				dao.setLogedInUser(_loggedInUser);
				dao.create(list);
				
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_OK);
			}
			catch (Exception e) {
				System.out.println("MQManager.createReservation() failed");
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_FAILED);
			}
		}
		else
			errorList.add(mq);
	}
	
	private void createBaseRate(MQMessages mq)
	{
		BaseRate _obj = prepareBaseRate(mq.getMessage());
		_obj.setFollowedByMQ(false);
		
		if(_obj!=null)
		{
			BaseRateDAO dao = new BaseRateDAO();
			//resDAO.setLogedInUser(logedInUser);
			try
			{
				ArrayList<BaseRate> list = new ArrayList<BaseRate>();
				list.add(_obj);
				dao.setLogedInUser(_loggedInUser);
				dao.create(list);
				
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_OK);
			}
			catch (Exception e) {
				System.out.println("MQManager.createReservation() failed");
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_FAILED);
			}
		}
		else
			errorList.add(mq);
	}
	
	private void createSeasonalBaseRate(MQMessages mq)
	{
		SeasonalBaseRate _obj = prepareSeasonalBaseRate(mq.getMessage());
		_obj.setFollowedByMQ(false);
		
		if(_obj!=null)
		{
			SeasonalRateDAO dao = new SeasonalRateDAO();
			//resDAO.setLogedInUser(logedInUser);
			try
			{
				ArrayList<SeasonalBaseRate> list = new ArrayList<SeasonalBaseRate>();
				list.add(_obj);
				dao.setLogedInUser(_loggedInUser);
				dao.create(list);
				
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_OK);
			}
			catch (Exception e) {
				System.out.println("MQManager.createReservation() failed");
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_FAILED);
			}
		}
		else
			errorList.add(mq);
	}
	
	private void createRateCodeMapping(MQMessages mq)
	{
		AppliedAgentMarkets _obj = prepareRateCodeMapping(mq.getMessage());
		_obj.setFollowedByMQ(false);
		
		if(_obj!=null)
		{
			AppliedAgentMarketsDAO dao = new AppliedAgentMarketsDAO();
			//resDAO.setLogedInUser(logedInUser);
			try
			{
				ArrayList<AppliedAgentMarkets> list = new ArrayList<AppliedAgentMarkets>();
				list.add(_obj);
				dao.setLogedInUser(_loggedInUser);
				dao.create(list);
				
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_OK);
			}
			catch (Exception e) {
				System.out.println("MQManager.createReservation() failed");
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_FAILED);
			}
		}
		else
			errorList.add(mq);
	}
	
	private void createOtherCost(MQMessages mq)
	{
		OtherCosts _obj = prepareOtherCost(mq.getMessage());
		_obj.setFollowedByMQ(false);
		
		if(_obj!=null)
		{
			OtherCostsDAO dao = new OtherCostsDAO();
			//resDAO.setLogedInUser(logedInUser);
			try
			{
				ArrayList<OtherCosts> list = new ArrayList<OtherCosts>();
				list.add(_obj);
				dao.setParentId(_obj.getParentId());
				dao.setLogedInUser(_loggedInUser);
				dao.create(list);
				
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_OK);
			}
			catch (Exception e) {
				System.out.println("MQManager.createReservation() failed");
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_FAILED);
			}
		}
		else
			errorList.add(mq);
	}
	
	private void createRoomCategoryCost(MQMessages mq)
	{
		RoomCatagoryCost _obj = prepareRoomCategoryCost(mq.getMessage());
		_obj.setFollowedByMQ(false);
		
		if(_obj!=null)
		{
			RoomCatagoryCostDAO dao = new RoomCatagoryCostDAO();
			//resDAO.setLogedInUser(logedInUser);
			try
			{
				ArrayList<RoomCatagoryCost> list = new ArrayList<RoomCatagoryCost>();
				list.add(_obj);
				
				dao.setParentId(_obj.getParentId());
				dao.setLogedInUser(_loggedInUser);
				dao.create(list);
				
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_OK);
			}
			catch (Exception e) {
				System.out.println("MQManager.createReservation() failed");
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_FAILED);
			}
		}
		else
			errorList.add(mq);
	}
	
	private void createRateCode(MQMessages mq)
	{
		RateCode _obj = prepareRateCode(mq.getMessage());
		_obj.setFollowedByMQ(false);
		
		if(_obj!=null)
		{
			RateCodeDAO dao = new RateCodeDAO();
			//resDAO.setLogedInUser(logedInUser);
			try
			{
				ArrayList<RateCode> list = new ArrayList<RateCode>();
				list.add(_obj);
				dao.setLogedInUser(_loggedInUser);
				dao.create(list);
				
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_OK);
			}
			catch (Exception e) {
				System.out.println("MQManager.createReservation() failed");
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_FAILED);
			}
		}
		else
			errorList.add(mq);
	}
	
	private void createRateCodeRoomRate(MQMessages mq)
	{
		RateCodeRoomRate _obj = prepareRateCodeRoomRate(mq.getMessage());
		_obj.setFollowedByMQ(false);
		
		if(_obj!=null)
		{
			RateCodeRoomRateDAO dao = new RateCodeRoomRateDAO();
			//resDAO.setLogedInUser(logedInUser);
			try
			{
				ArrayList<RateCodeRoomRate> list = new ArrayList<RateCodeRoomRate>();
				list.add(_obj);
				dao.setLogedInUser(_loggedInUser);
				dao.setRateCodeId(_obj.getRateCodeId());
				dao.create(list);
				
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_OK);
			}
			catch (Exception e) {
				System.out.println("MQManager.createReservation() failed");
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_FAILED);
			}
		}
		else
			errorList.add(mq);
	}
	
	private void createRoomCategory(MQMessages mq)
	{
		RoomCategory _obj = prepareRoomCategory(mq.getMessage());
		try
		{
			String location	= PropertyFileLoader.getOtiumPropValues().getLocationType();
			
			if(location.equals(JHConstants.APP_LOCATION_TYPE_HeadOffice))// if local server is HQ then need to send masterdata mq's to all properties
				_obj.setFollowedByMQ(true);
			else
				_obj.setFollowedByMQ(false);
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		
		if(_obj!=null)
		{
			RoomCategoryDAO dao = new RoomCategoryDAO();
			//resDAO.setLogedInUser(logedInUser);
			try
			{
				ArrayList<RoomCategory> list = new ArrayList<RoomCategory>();
				list.add(_obj);
				dao.setLogedInUser(_loggedInUser);
				dao.create(list);
				
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_OK);
			}
			catch (Exception e) {
				System.out.println("MQManager.createReservation() failed");
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_FAILED);
			}
		}
		else
			errorList.add(mq);
	}
	
	private void createRoomCategoryByHotel(MQMessages mq)
	{
		RoomCategoryByHotel _obj = prepareRoomCategoryByHotel(mq.getMessage());
		_obj.setFollowedByMQ(false);
		
		if(_obj!=null)
		{
			RoomCategoryByHotelDAO dao = new RoomCategoryByHotelDAO();
			//resDAO.setLogedInUser(logedInUser);
			try
			{
				ArrayList<RoomCategoryByHotel> list = new ArrayList<RoomCategoryByHotel>();
				list.add(_obj);
				dao.setLogedInUser(_loggedInUser);
				dao.create(list);
				
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_OK);
			}
			catch (Exception e) {
				System.out.println("MQManager.createReservation() failed");
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_FAILED);
			}
		}
		else
			errorList.add(mq);
	}
	
	
	private void createAgentMarket(MQMessages mq)
	{
		AgentMarkets _obj = prepareAgentMarket(mq.getMessage());
		try
		{
			String location	= PropertyFileLoader.getOtiumPropValues().getLocationType();
			
			if(location.equals(JHConstants.APP_LOCATION_TYPE_HeadOffice))// if local server is HQ then need to send masterdata mq's to all properties
				_obj.setFollowedByMQ(true);
			else
				_obj.setFollowedByMQ(false);
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		
		if(_obj!=null)
		{
			AgentMarketsDAO dao = new AgentMarketsDAO();
			//resDAO.setLogedInUser(logedInUser);
			try
			{
				ArrayList<AgentMarkets> list = new ArrayList<AgentMarkets>();
				list.add(_obj);
				dao.setLogedInUser(_loggedInUser);
				dao.create(list);
				
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_OK);
			}
			catch (Exception e) {
				System.out.println("MQManager.createReservation() failed");
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_FAILED);
			}
		}
		else
			errorList.add(mq);
	}
	
	private void createProperty(MQMessages mq)
	{
		Property _obj = prepareProperty(mq.getMessage());
		try
		{
			String location	= PropertyFileLoader.getOtiumPropValues().getLocationType();
			
			if(location.equals(JHConstants.APP_LOCATION_TYPE_HeadOffice))// if local server is HQ then need to send masterdata mq's to all properties
				_obj.setFollowedByMQ(true);
			else
				_obj.setFollowedByMQ(false);
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		
		if(_obj!=null)
		{
			PropertyDAO dao = new PropertyDAO();
			//resDAO.setLogedInUser(logedInUser);
			try
			{
				ArrayList<Property> list = new ArrayList<Property>();
				list.add(_obj);
				dao.setLogedInUser(_loggedInUser);
				dao.create(list);
				
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_OK);
			}
			catch (Exception e) {
				System.out.println("MQManager.createReservation() failed");
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_FAILED);
			}
		}
		else
			errorList.add(mq);
	}
	
	private void createDestinityReservation(MQMessages mq)
	{
		DestinityReservation _obj = prepareDestinityReservation(mq.getMessage());
		try
		{
			String location		= PropertyFileLoader.getOtiumPropValues().getLocationType();
			String property		= PropertyFileLoader.getOtiumPropValues().getLocation();
			
			if(!location.equals(JHConstants.APP_LOCATION_TYPE_HeadOffice))// if local server is HQ then need to send masterdata mq's to all properties
			{
				if(_obj!=null)
				{
					CreateReservationDAO dao = new CreateReservationDAO();
					dao.setPropertyCode(property);
					try
					{
						ArrayList<DestinityReservation> list = new ArrayList<DestinityReservation>();
						list.add(_obj);
						dao.setLogedInUser(_loggedInUser);
						dao.create(list);
						
						updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_OK);
					}
					catch (Exception e) {
						System.out.println("MQManager.createReservation() failed");
						updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_FAILED);
					}
				}
				else
					errorList.add(mq);
			}
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	private void createDestinityAgent(MQMessages mq)
	{
		DestinityAgent _obj = prepareDestinityAgent(mq.getMessage());
		try
		{
			String location	= PropertyFileLoader.getOtiumPropValues().getLocationType();
			String property	= PropertyFileLoader.getOtiumPropValues().getLocation();
			
			if(!location.equals(JHConstants.APP_LOCATION_TYPE_HeadOffice))// if local server is HQ then need to send masterdata mq's to all properties
			{
				if(_obj!=null)
				{
					CreateAgentDAO dao = new CreateAgentDAO();
					dao.setPropertyCode(property);
					
					try
					{
						ArrayList<DestinityAgent> list = new ArrayList<DestinityAgent>();
						list.add(_obj);
						dao.setLogedInUser(_loggedInUser);
						dao.create(list);
						
						updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_OK);
					}
					catch (Exception e) {
						System.out.println("MQManager.createDestinityAgent() failed");
						updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_FAILED);
					}
				}
				else
					errorList.add(mq);
			}
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	private void createAgenType(MQMessages mq)
	{
		AgentType _obj = prepareAgentType(mq.getMessage());
		try
		{
			String location	= PropertyFileLoader.getOtiumPropValues().getLocationType();
			
			if(location.equals(JHConstants.APP_LOCATION_TYPE_HeadOffice))// if local server is HQ then need to send masterdata mq's to all properties
				_obj.setFollowedByMQ(true);
			else
				_obj.setFollowedByMQ(false);
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		
		if(_obj!=null)
		{
			AgentTypeDAO dao = new AgentTypeDAO();
			//resDAO.setLogedInUser(logedInUser);
			try
			{
				ArrayList<AgentType> list = new ArrayList<AgentType>();
				list.add(_obj);
				dao.setLogedInUser(_loggedInUser);
				dao.create(list);
				
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_OK);
			}
			catch (Exception e) {
				System.out.println("MQManager.createReservation() failed");
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_FAILED);
			}
		}
		else
			errorList.add(mq);
	}
	
	private void createSeason(MQMessages mq)
	{
		Seasons _obj = prepareSeason(mq.getMessage());
		try
		{
			String location	= PropertyFileLoader.getOtiumPropValues().getLocationType();
			
			if(location.equals(JHConstants.APP_LOCATION_TYPE_HeadOffice))// if local server is HQ then need to send masterdata mq's to all properties
				_obj.setFollowedByMQ(true);
			else
				_obj.setFollowedByMQ(false);
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		
		if(_obj!=null)
		{
			SeasonsDAO dao = new SeasonsDAO();
			//resDAO.setLogedInUser(logedInUser);
			try
			{
				ArrayList<Seasons> list = new ArrayList<Seasons>();
				list.add(_obj);
				dao.setLogedInUser(_loggedInUser);
				dao.create(list);
				
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_OK);
			}
			catch (Exception e) {
				System.out.println("MQManager.createReservation() failed");
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_FAILED);
			}
		}
		else
			errorList.add(mq);
	}
	
	private void createOffers(MQMessages mq)
	{
		SpecialOffers _obj = prepareOffers(mq.getMessage());
		_obj.setFollowedByMQ(false);
		
		if(_obj!=null)
		{
			SpecialOffersDAO dao = new SpecialOffersDAO();
			//resDAO.setLogedInUser(logedInUser);
			try
			{
				ArrayList<SpecialOffers> list = new ArrayList<SpecialOffers>();
				list.add(_obj);
				dao.setLogedInUser(_loggedInUser);
				dao.create(list);
				
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_OK);
			}
			catch (Exception e) {
				System.out.println("MQManager.createReservation() failed");
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_FAILED);
			}
		}
		else
			errorList.add(mq);
	}
	
	private void createDiscountConditions(MQMessages mq)
	{
		DiscountConditions _obj = prepareDiscountConditions(mq.getMessage());
		_obj.setFollowedByMQ(false);
		
		if(_obj!=null)
		{
			DiscountConditionsDAO dao = new DiscountConditionsDAO();
			//resDAO.setLogedInUser(logedInUser);
			try
			{
				ArrayList<DiscountConditions> list = new ArrayList<DiscountConditions>();
				list.add(_obj);
				dao.setLogedInUser(_loggedInUser);
				dao.create(list);
				
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_OK);
			}
			catch (Exception e) {
				System.out.println("MQManager.createReservation() failed");
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_FAILED);
			}
		}
		else
			errorList.add(mq);
	}
	
	private void createGuestPreference(MQMessages mq)
	{
		GuestPreference _obj = prepareGuestPreference(mq.getMessage());
		try
		{
			String location	= PropertyFileLoader.getOtiumPropValues().getLocationType();
			
			if(location.equals(JHConstants.APP_LOCATION_TYPE_HeadOffice))// if local server is HQ then need to send masterdata mq's to all properties
				_obj.setFollowedByMQ(true);
			else
				_obj.setFollowedByMQ(false);
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		
		if(_obj!=null)
		{
			GuestPreferenceDAO dao = new GuestPreferenceDAO();
			//resDAO.setLogedInUser(logedInUser);
			try
			{
				ArrayList<GuestPreference> list = new ArrayList<GuestPreference>();
				list.add(_obj);
				dao.setLogedInUser(_loggedInUser);
				dao.create(list);
				
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_OK);
			}
			catch (Exception e) {
				System.out.println("MQManager.createReservation() failed");
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_FAILED);
			}
		}
		else
			errorList.add(mq);
	}
	
	private void createDiscountMapping(MQMessages mq)
	{
		DiscountsAndSupplementMapping _obj = prepareDiscountMapping(mq.getMessage());
		_obj.setFollowedByMQ(false);
		
		if(_obj!=null)
		{
			DiscountsAndSupplementMappingDAO dao = new DiscountsAndSupplementMappingDAO();
			//resDAO.setLogedInUser(logedInUser);
			try
			{
				ArrayList<DiscountsAndSupplementMapping> list = new ArrayList<DiscountsAndSupplementMapping>();
				list.add(_obj);
				dao.setDiscountId(_obj.getDiscountId());
				dao.setLogedInUser(_loggedInUser);
				dao.create(list);
				
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_OK);
			}
			catch (Exception e) {
				System.out.println("MQManager.createReservation() failed");
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_FAILED);
			}
		}
		else
			errorList.add(mq);
	}
	
	private void changeDiscountStatus(MQMessages mq)
	{
		Discounts _obj = prepareDiscount(mq.getMessage());
		_obj.setFollowedByMQ(false);
		
		if(_obj!=null)
		{
			DiscountsDAO dao = new DiscountsDAO();
			//resDAO.setLogedInUser(logedInUser);
			try
			{
				ArrayList<Discounts> list = new ArrayList<Discounts>();
				list.add(_obj);
				dao.setLogedInUser(_loggedInUser);
				dao.markAsUsed(list);
				
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_OK);
			}
			catch (Exception e) {
				System.out.println("MQManager.createReservation() failed");
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_FAILED);
			}
		}
		else
			errorList.add(mq);
	}
	
	private void createDiscount(MQMessages mq)
	{
		Discounts _obj = prepareDiscount(mq.getMessage());
		_obj.setFollowedByMQ(false);
		
		if(_obj!=null)
		{
			DiscountsDAO dao = new DiscountsDAO();
			//resDAO.setLogedInUser(logedInUser);
			try
			{
				ArrayList<Discounts> list = new ArrayList<Discounts>();
				list.add(_obj);
				dao.setLogedInUser(_loggedInUser);
				dao.create(list);
				
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_OK);
			}
			catch (Exception e) {
				System.out.println("MQManager.createReservation() failed");
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_FAILED);
			}
		}
		else
			errorList.add(mq);
	}
	
	private void createRoomFacility(MQMessages mq)
	{
		RoomFacilitiesMaster _obj = prepareRoomFacility(mq.getMessage());
		try
		{
			String location	= PropertyFileLoader.getOtiumPropValues().getLocationType();
			
			if(location.equals(JHConstants.APP_LOCATION_TYPE_HeadOffice))// if local server is HQ then need to send masterdata mq's to all properties
				_obj.setFollowedByMQ(true);
			else
				_obj.setFollowedByMQ(false);
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		
		if(_obj!=null)
		{
			RoomFacilitiesMasterDAO dao = new RoomFacilitiesMasterDAO();
			//resDAO.setLogedInUser(logedInUser);
			try
			{
				ArrayList<RoomFacilitiesMaster> list = new ArrayList<RoomFacilitiesMaster>();
				list.add(_obj);
				dao.setLogedInUser(_loggedInUser);
				dao.create(list);
				
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_OK);
			}
			catch (Exception e) {
				System.out.println("MQManager.createReservation() failed");
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_FAILED);
			}
		}
		else
			errorList.add(mq);
	}
	
	private void createRoomFacilityByProperty(MQMessages mq)
	{
		RoomFacilitiesByProperty _obj = prepareRoomFacilityByProperty(mq.getMessage());
		_obj.setFollowedByMQ(false);
		
		if(_obj!=null)
		{
			RoomFacilitiesByPropertyDAO dao = new RoomFacilitiesByPropertyDAO();
			//resDAO.setLogedInUser(logedInUser);
			try
			{
				ArrayList<RoomFacilitiesByProperty> list = new ArrayList<RoomFacilitiesByProperty>();
				list.add(_obj);
				dao.setLogedInUser(_loggedInUser);
				dao.create(list);
				
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_OK);
			}
			catch (Exception e) {
				System.out.println("MQManager.createReservation() failed");
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_FAILED);
			}
		}
		else
			errorList.add(mq);
	}
	
	private void createOtherCostMaster(MQMessages mq)
	{
		OtherCostsMaster _obj = prepareOtherCostMaster(mq.getMessage());
		try
		{
			String location	= PropertyFileLoader.getOtiumPropValues().getLocationType();
			
			if(location.equals(JHConstants.APP_LOCATION_TYPE_HeadOffice))// if local server is HQ then need to send masterdata mq's to all properties
				_obj.setFollowedByMQ(true);
			else
				_obj.setFollowedByMQ(false);
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		
		if(_obj!=null)
		{
			OtherCostsMasterDAO dao = new OtherCostsMasterDAO();
			//resDAO.setLogedInUser(logedInUser);
			try
			{
				ArrayList<OtherCostsMaster> list = new ArrayList<OtherCostsMaster>();
				list.add(_obj);
				dao.setLogedInUser(_loggedInUser);
				dao.create(list);
				
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_OK);
			}
			catch (Exception e) {
				System.out.println("MQManager.createReservation() failed");
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_FAILED);
			}
		}
		else
			errorList.add(mq);
	}
	
	private void createReservationDiscountsMaster(MQMessages mq)
	{
		ReservationDiscounts _obj = prepareReservationDiscounts(mq.getMessage());
		_obj.setFollowedByMQ(false);
		
		if(_obj!=null)
		{
			ReservationDiscountsDAO dao = new ReservationDiscountsDAO();
			//resDAO.setLogedInUser(logedInUser);
			try
			{
				ArrayList<ReservationDiscounts> list = new ArrayList<ReservationDiscounts>();
				list.add(_obj);
				dao.setLogedInUser(_loggedInUser);
				dao.setVisualLineId(_obj.getVisualLineId());
				dao.setReservationId(_obj.getReservationId());
				dao.create(list);
				
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_OK);
			}
			catch (Exception e) {
				System.out.println("MQManager.createReservationiscountsMaster() failed");
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_FAILED);
			}
		}
		else
			errorList.add(mq);
	}
	
	private void createTaxMaster(MQMessages mq)
	{
		Tax _obj = prepareTaxMaster(mq.getMessage());
		try
		{
			String location	= PropertyFileLoader.getOtiumPropValues().getLocationType();
			
			if(location.equals(JHConstants.APP_LOCATION_TYPE_HeadOffice))// if local server is HQ then need to send masterdata mq's to all properties
				_obj.setFollowedByMQ(true);
			else
				_obj.setFollowedByMQ(false);
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		
		if(_obj!=null)
		{
			TaxDAO dao = new TaxDAO();
			//resDAO.setLogedInUser(logedInUser);
			try
			{
				ArrayList<Tax> list = new ArrayList<Tax>();
				list.add(_obj);
				dao.setLogedInUser(_loggedInUser);
				dao.create(list);
				
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_OK);
			}
			catch (Exception e) {
				System.out.println("MQManager.createReservation() failed");
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_FAILED);
			}
		}
		else
			errorList.add(mq);
	}
	
	private void createUser(MQMessages mq)
	{
		User _obj = prepareUser(mq.getMessage());
		_obj.setFollowedByMQ(false);
		
		if(_obj!=null)
		{
			UserDAO dao = new UserDAO();
			//resDAO.setLogedInUser(logedInUser);
			try
			{
				ArrayList<User> list = new ArrayList<User>();
				list.add(_obj);
				dao.setLogedInUser(_loggedInUser);
				dao.create(list);
				
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_OK);
			}
			catch (Exception e) {
				System.out.println("MQManager.createReservation() failed");
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_FAILED);
			}
		}
		else
			errorList.add(mq);
	}
	
	private void createAccessGroupMaster(MQMessages mq)
	{
		AccessGroupMaster _obj = prepareAccessGroupMaster(mq.getMessage());
		try
		{
			String location	= PropertyFileLoader.getOtiumPropValues().getLocationType();
			
			if(location.equals(JHConstants.APP_LOCATION_TYPE_HeadOffice))// if local server is HQ then need to send masterdata mq's to all properties
				_obj.setFollowedByMQ(true);
			else
				_obj.setFollowedByMQ(false);
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		
		if(_obj!=null)
		{
			AccessGroupMasterDAO dao = new AccessGroupMasterDAO();
			//resDAO.setLogedInUser(logedInUser);
			try
			{
				ArrayList<AccessGroupMaster> list = new ArrayList<AccessGroupMaster>();
				list.add(_obj);
				dao.setLogedInUser(_loggedInUser);
				dao.create(list);
				
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_OK);
			}
			catch (Exception e) {
				System.out.println("MQManager.createReservation() failed");
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_FAILED);
			}
		}
		else
			errorList.add(mq);
	}
	
	private void createAccessGroupDetails(MQMessages mq)
	{
		AccessGroupDetails _obj = prepareAccessGroupDetails(mq.getMessage());
		try
		{
			String location	= PropertyFileLoader.getOtiumPropValues().getLocationType();
			
			if(location.equals(JHConstants.APP_LOCATION_TYPE_HeadOffice))// if local server is HQ then need to send masterdata mq's to all properties
				_obj.setFollowedByMQ(true);
			else
				_obj.setFollowedByMQ(false);
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		
		if(_obj!=null)
		{
			AccessGroupDetailsDAO dao = new AccessGroupDetailsDAO();
			//resDAO.setLogedInUser(logedInUser);
			try
			{
				ArrayList<AccessGroupDetails> list = new ArrayList<AccessGroupDetails>();
				list.add(_obj);
				dao.setLogedInUser(_loggedInUser);
				dao.setAccessGroupMasterId(_obj.getAccessGroupMasterId());
				dao.createlevel02(list);
				
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_OK);
			}
			catch (Exception e) {
				System.out.println("MQManager.createReservation() failed");
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_FAILED);
			}
		}
		else
			errorList.add(mq);
	}
	
	private void createAccessDetailsOfGroups(MQMessages mq)
	{
		AccessDetailsOfGroups _obj = prepareAccessDetailsOfGroups(mq.getMessage());
		_obj.setFollowedByMQ(false);
		
		if(_obj!=null)
		{
			AccessDetailsOfGroupsDAO dao = new AccessDetailsOfGroupsDAO();
			//resDAO.setLogedInUser(logedInUser);
			try
			{
				ArrayList<AccessDetailsOfGroups> list = new ArrayList<AccessDetailsOfGroups>();
				list.add(_obj);
				dao.setLogedInUser(_loggedInUser);
				dao.create(list);
				
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_OK);
			}
			catch (Exception e) {
				System.out.println("MQManager.createReservation() failed");
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_FAILED);
			}
		}
		else
			errorList.add(mq);
	}
	
	private void createDiscountApprovalRequest(MQMessages mq)
	{
		DiscountApprovalRequest _obj = prepareDiscountApprovalRequest(mq.getMessage());
		_obj.setFollowedByMQ(false);
		
		if(_obj!=null)
		{
			DiscountApprovalRequestDAO dao = new DiscountApprovalRequestDAO();
			//resDAO.setLogedInUser(logedInUser);
			try
			{
				ArrayList<DiscountApprovalRequest> list = new ArrayList<DiscountApprovalRequest>();
				list.add(_obj);
				dao.setLogedInUser(_loggedInUser);
				dao.create(list);
				
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_OK);
			}
			catch (Exception e) {
				System.out.println("MQManager.createReservation() failed");
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_FAILED);
			}
		}
		else
			errorList.add(mq);
	}
	
	private void createConfirmationVoucherDetails(MQMessages mq)
	{
		ConfirmationVoucherDetails _obj = prepareConfirmationVoucherDetails(mq.getMessage());
		_obj.setFollowedByMQ(false);
		
		if(_obj!=null)
		{
			ConfirmationVoucherDetailsDAO dao = new ConfirmationVoucherDetailsDAO();
			//resDAO.setLogedInUser(logedInUser);
			try
			{
				ArrayList<ConfirmationVoucherDetails> list = new ArrayList<ConfirmationVoucherDetails>();
				list.add(_obj);
				dao.setLogedInUser(_loggedInUser);
				dao.create(list);
				
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_OK);
			}
			catch (Exception e) {
				System.out.println("MQManager.createReservation() failed");
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_FAILED);
			}
		}
		else
			errorList.add(mq);
	}
	
	private void deleteDiscountMapping(MQMessages mq)
	{
		DiscountsAndSupplementMapping _obj = prepareDiscountMapping(mq.getMessage());
		_obj.setFollowedByMQ(false);
		
		if(_obj!=null)
		{
			DiscountsAndSupplementMappingDAO dao = new DiscountsAndSupplementMappingDAO();
			//resDAO.setLogedInUser(logedInUser);
			try
			{
				ArrayList<DiscountsAndSupplementMapping> list = new ArrayList<DiscountsAndSupplementMapping>();
				list.add(_obj);
				dao.setLogedInUser(_loggedInUser);
				dao.delete(list);
				
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_OK);
			}
			catch (Exception e) {
				System.out.println("MQManager.deleteDiscountMapping() failed");
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_FAILED);
			}
		}
		else
			errorList.add(mq);
	}
	
	private void deleteReservationDetails(MQMessages mq)
	{
		ReservationDetails _obj = prepareReservationDetails(mq.getMessage());
		_obj.setFollowedByMQ(false);
		
		if(_obj!=null)
		{
			ReservationDetailsDAO dao = new ReservationDetailsDAO();
			//resDAO.setLogedInUser(logedInUser);
			try
			{
				ArrayList<ReservationDetails> list = new ArrayList<ReservationDetails>();
				list.add(_obj);
				dao.setLogedInUser(_loggedInUser);
				dao.delete(list);
				
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_OK);
			}
			catch (Exception e) {
				System.out.println("MQManager.createReservation() failed");
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_FAILED);
			}
		}
		else
			errorList.add(mq);
	}
	
	private void deleteReservationDiscountsMaster(MQMessages mq)
	{
		ReservationDiscounts _obj = prepareReservationDiscounts(mq.getMessage());
		_obj.setFollowedByMQ(false);
		
		if(_obj!=null)
		{
			ReservationDiscountsDAO dao = new ReservationDiscountsDAO();
			//resDAO.setLogedInUser(logedInUser);
			try
			{
				ArrayList<ReservationDiscounts> list = new ArrayList<ReservationDiscounts>();
				list.add(_obj);
				dao.setLogedInUser(_loggedInUser);
				dao.setVisualLineId(_obj.getReservationId());
				dao.delete(list);
				
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_OK);
			}
			catch (Exception e) {
				System.out.println("MQManager.createReservationiscountsMaster() failed");
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_FAILED);
			}
		}
		else
			errorList.add(mq);
	}
	
	private void deleteRoomcategoryMapping(MQMessages mq)
	{
		RoomCategoryByHotel _obj = prepareRoomCategoryByHotel(mq.getMessage());
		_obj.setFollowedByMQ(false);
		
		if(_obj!=null)
		{
			RoomCategoryByHotelDAO dao = new RoomCategoryByHotelDAO();
			//resDAO.setLogedInUser(logedInUser);
			try
			{
				ArrayList<RoomCategoryByHotel> list = new ArrayList<RoomCategoryByHotel>();
				list.add(_obj);
				dao.setLogedInUser(_loggedInUser);
				dao.delete(list);
				
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_OK);
			}
			catch (Exception e) {
				System.out.println("MQManager.createReservation() failed");
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_FAILED);
			}
		}
		else
			errorList.add(mq);
	}
	
	private void deleteOtherCost(MQMessages mq)
	{
		OtherCosts _obj = prepareOtherCost(mq.getMessage());
		_obj.setFollowedByMQ(false);
		
		if(_obj!=null)
		{
			OtherCostsDAO dao = new OtherCostsDAO();
			//resDAO.setLogedInUser(logedInUser);
			try
			{
				ArrayList<OtherCosts> list = new ArrayList<OtherCosts>();
				list.add(_obj);
				dao.setLogedInUser(_loggedInUser);
				dao.delete(list);
				
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_OK);
			}
			catch (Exception e) {
				System.out.println("MQManager.createReservation() failed");
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_FAILED);
			}
		}
		else
			errorList.add(mq);
	}
	
	private void deleteRoomCategoryCost(MQMessages mq)
	{
		RoomCatagoryCost _obj = prepareRoomCategoryCost(mq.getMessage());
		_obj.setFollowedByMQ(false);
		
		if(_obj!=null)
		{
			RoomCatagoryCostDAO dao = new RoomCatagoryCostDAO();
			//resDAO.setLogedInUser(logedInUser);
			try
			{
				ArrayList<RoomCatagoryCost> list = new ArrayList<RoomCatagoryCost>();
				list.add(_obj);
				dao.setLogedInUser(_loggedInUser);
				dao.delete(list);
				
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_OK);
			}
			catch (Exception e) {
				System.out.println("MQManager.createReservation() failed");
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_FAILED);
			}
		}
		else
			errorList.add(mq);
	}
	
	private void deleteRoomFacilityByProperty(MQMessages mq)
	{
		RoomFacilitiesByProperty _obj = prepareRoomFacilityByProperty(mq.getMessage());
		_obj.setFollowedByMQ(false);
		
		if(_obj!=null)
		{
			RoomFacilitiesByPropertyDAO dao = new RoomFacilitiesByPropertyDAO();
			//resDAO.setLogedInUser(logedInUser);
			try
			{
				ArrayList<RoomFacilitiesByProperty> list = new ArrayList<RoomFacilitiesByProperty>();
				list.add(_obj);
				dao.setLogedInUser(_loggedInUser);
				dao.delete(list);
				
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_OK);
			}
			catch (Exception e) {
				System.out.println("MQManager.createReservation() failed");
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_FAILED);
			}
		}
		else
			errorList.add(mq);
	}
	
	private void deleteRateCodeMapping(MQMessages mq)
	{
		AppliedAgentMarkets _obj = prepareRateCodeMapping(mq.getMessage());
		_obj.setFollowedByMQ(false);
		
		if(_obj!=null)
		{
			AppliedAgentMarketsDAO dao = new AppliedAgentMarketsDAO();
			//resDAO.setLogedInUser(logedInUser);
			try
			{
				ArrayList<AppliedAgentMarkets> list = new ArrayList<AppliedAgentMarkets>();
				list.add(_obj);
				dao.setLogedInUser(_loggedInUser);
				dao.delete(list);
				
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_OK);
			}
			catch (Exception e) {
				System.out.println("MQManager.createReservation() failed");
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_FAILED);
			}
		}
		else
			errorList.add(mq);
	}
	
	private void deleteAccessGroupDetails(MQMessages mq)
	{
		AccessGroupDetails _obj = prepareAccessGroupDetails(mq.getMessage());
		try
		{
			String location	= PropertyFileLoader.getOtiumPropValues().getLocationType();
			
			if(location.equals(JHConstants.APP_LOCATION_TYPE_HeadOffice))// if local server is HQ then need to send masterdata mq's to all properties
				_obj.setFollowedByMQ(true);
			else
				_obj.setFollowedByMQ(false);
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		
		if(_obj!=null)
		{
			AccessGroupDetailsDAO dao = new AccessGroupDetailsDAO();
			//resDAO.setLogedInUser(logedInUser);
			try
			{
				ArrayList<AccessGroupDetails> list = new ArrayList<AccessGroupDetails>();
				list.add(_obj);
				dao.setLogedInUser(_loggedInUser);
				dao.setAccessGroupMasterId(_obj.getAccessGroupMasterId());
				dao.delete(list);
				
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_OK);
			}
			catch (Exception e) {
				System.out.println("MQManager.deleteAccessGroupDetails() failed");
				updateMQStatus(mq, JHConstants.MQ_MSG_CONSUMED_FAILED);
			}
		}
		else
			errorList.add(mq);
	}
	
	private void updateMQStatus(MQMessages mq, String status)
	{
		try
		{
			MQMessagesDAO mqDAO = new MQMessagesDAO();
			mqDAO.setLogedInUser(_loggedInUser);
			
			ArrayList<MQMessages> mqList = new ArrayList<MQMessages>();
			mqList.add(mq);
			if(status.equals(JHConstants.MQ_MSG_SEND_OK))
				mqDAO.setSendOK(mqList);
			else if(status.equals(JHConstants.MQ_MSG_SEND_FAILED))
				mqDAO.setSendOK(mqList);
			else if(status.equals(JHConstants.MQ_MSG_CONSUMED_OK))
				mqDAO.setconsumeOk(mqList);
			else if(status.equals(JHConstants.MQ_MSG_CONSUMED_FAILED))
				mqDAO.setConsumeFailed(mqList);
		}
		catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	private MQMessages creatMQ(MQMessages mq)
	{
		MQMessages rMq = null;
		MQMessagesDAO mqDAO = new MQMessagesDAO();
		mqDAO.setLogedInUser(_loggedInUser);
		
		ArrayList<MQMessages> mqList = new ArrayList<MQMessages>();
		mqList.add(mq);
		
		try
		{
			int seq = mqDAO.create(mqList);// create mq record
			ArrayList<MQMessages> pmqList = mqDAO.dynamicSearch("searchBysequence", String.valueOf(seq));
			
			if(pmqList.size()>0)
				rMq = pmqList.get(0);
		}
		catch (Exception e) {
			e.printStackTrace();
			errorList.add(mq);
			System.out.println("recieveMessage, mq insert failed!");
		}
		
		return rMq;
	}
	
	private void creatMQOriginal(String str)
	{
		MQOriginal mq = new MQOriginal();
		mq.setMqMessage(str);
		
		MQOriginalDAO mqDAO = new MQOriginalDAO();
		mqDAO.setLogedInUser(_loggedInUser);
		
		ArrayList<MQOriginal> mqList = new ArrayList<MQOriginal>();
		mqList.add(mq);
		
		try
		{
			mqDAO.create(mqList);// create mq record
			
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("recieveMessage, mq insert failed!");
		}
		
	}
	
	private MQMessages preapareAndCreateMQ(String str)
	{
		MQMessages mq = null;
		int count	   = 0;
		if(str!=null)
		{
			mq  = new MQMessages();
			String[] token = str.split(blockClose);
			//StringTokenizer st = new StringTokenizer(str, blockClose);
			for (int i = 0; i < token.length; i++) {
			    //String token = st.nextToken();
			     
			     switch(count){
			     case  0:
			    	 mq.setSender(token[i]);
			        break; //optional
			     case 1 :
			    	 mq.setReceiver(token[i]);
			        break; //optional
			     case  2:
			    	 mq.setMessageId(token[i]);
			        break;
			     case  3:
			    	 mq.setSequence(Integer.parseInt(token[i]));
			        break;
			     case  4:
			    	 mq.setCommand(token[i]);
			        break; 
			     case  5:
			    	 mq.setSeparator(token[i]);
			        break;  
			     case  6:
			    	 mq.setMessageLength(Integer.parseInt(token[i]));
			        break; 
			     case  7:
			    	 mq.setMessage(token[i]);
			        break; 
			     case  8:
			    	 mq.setTransactionUuid(token[i]);
			        break;      
			     //You can have any number of case statements.
			     default : //Optional
			    	 System.out.println("Invalid grade " + count);
			 }
			     
			     ++count;
			}
		}
		
		return mq;
	}
	
	private Reservation prepareReservation(String str)
	{
		Reservation obj = null;
		
		int count	   = 0;
		if(str!=null)
		{
			obj  = new Reservation();
			TimeStamp timeStamp 			= new TimeStamp();
			CheckInDetails arrivalDetails 	= new CheckInDetails();
			CheckOutDetails departureDetails= new CheckOutDetails();
			MealDetails mealDetails			= new MealDetails();
			AgentDetails agent				= new AgentDetails();
			Guest guest						= new Guest();
			GuideDetails guide				= new GuideDetails();
			  
			String[] token = str.split("\\"+spch);
			
				for (int i = 0; i < token.length; i++)  {
				     
				     switch(count){
				     case  0:
				    	 obj.setUuid(token[i]);
				        break; //optional
				     case  1:
				    	 obj.setReservationYear(token[i]);
				        break; //optional   
				     case  2:
				    	 obj.setReservationNo(Integer.parseInt(token[i]));
				        break; //optional
				     case  3:
				    	 obj.setReservationType(token[i]);
				        break;
				     case  4:
				    	 obj.setGuestType(token[i]);
				        break;
				     case  5:
				    	 obj.setResPaxType(token[i]);
				        break; 
				     case  6:
				    	 agent.setAgentTypeId(token[i]);
				        break;  
				     case  7:
				    	 guest.setUuid(token[i]);
				        break; 
				     case  8:
				    	 agent.setUuid(token[i]);
				        break;  
				     case  9:
				    	 agent.setAgentMarketUuid(token[i]);
				        break;
				     case  10:
				    	 obj.setRateCodeId(token[i]);
				        break;
				     case  11:
				    	 obj.setPropertyId(token[i]);
				        break;
				     case  12:
				    	 obj.setVoucherNo(token[i]);
				        break; 
				     case  13:
				    	 obj.setTourNo(token[i]);
				        break; 
				     case  14:
				    	 obj.setBookingType(token[i]);
				        break; 
				     case  15:
				    	 arrivalDetails.setCheckInDate(token[i]);
				        break;
				     case  16:
				    	 arrivalDetails.setCheckInTime(token[i]);
				        break;
				     case  17:
				    	 departureDetails.setCheckOutDate(token[i]);
				        break;
				     case  18:
				    	 departureDetails.setCheckOutTime(token[i]);
				        break;
				     case  19:
				    	 mealDetails.setMealBasis(token[i]);
				        break;
				     case  20:
				    	 mealDetails.setArrivalMeal(token[i]);
				        break;   
				     case  21:
				    	 mealDetails.setLeaveMeal(token[i]);
				        break; 
				     case  22:
				    	 obj.setRemarks(token[i]);
				        break;
				     case  23:
				    	 obj.setNoOfSglRooms(Integer.parseInt(token[i]));
				        break;
				     case  24:
				    	 obj.setNoOfDblRooms(Integer.parseInt(token[i]));
				        break;
				     case  25:
				    	 obj.setNoOfTplRooms(Integer.parseInt(token[i]));
				        break;
				     case  26:
				    	 obj.setNoOfAdults(Integer.parseInt(token[i]));
				        break;
				     case  27:
				    	 obj.setNoOfChildren(Integer.parseInt(token[i]));
				        break;
				     case  28:
				    	 obj.setNoOfInfants(Integer.parseInt(token[i]));
				        break;
				     case  29:
				    	 obj.setStatus(token[i]);
				        break; 
				     case  30:
				    	 timeStamp.setCreatedByTimeStamp(token[i]);
				        break;
				     case  31:
				    	 timeStamp.setCreatedById(token[i]);
				        break;
				     case  32:
				    	 timeStamp.setCreatedByName(token[i]);
				        break;
				     case  33:
				    	 timeStamp.setUpdatedByTimeStamp(token[i]);
				        break;
				     case  34:
				    	 timeStamp.setUpdatedById(token[i]);
				        break;
				     case  35:
				    	 timeStamp.setUpdatedByName(token[i]);
				        break;
				     case  36:
				    	 obj.setCurrency(token[i]);
				        break;
				     case  37:
				    	 obj.setPaymentModeId(token[i]);
				        break;
				     case  38:
				    	 obj.setTotalAmount(token[i]);
				        break;
				     case  39:
				    	 obj.setNumberOfNights(Integer.parseInt(token[i]));
				        break;
				     case  40:
				    	 obj.setConfirmedBy(token[i]);
				        break;
				     case  41:
				    	 obj.setCommentsByAgent(token[i]);
				        break; 
				     case  42:
				    	 obj.setReservationsStatus(token[i]);
				        break; 
				     case  43:
				    	 obj.setReservedBy(token[i]);
				        break;
				     case  44:
				    	 obj.setGuestFirstName(token[i]);
				        break;
				     case  45:
				    	 obj.setGuestLastName(token[i]);
				        break;
				     case  46:
				    	 obj.setContactEmail(token[i]);
				        break;
				     case  47:
				    	 obj.setContactMobileNo(token[i]);
				        break;
				     case  48:
				    	 obj.setCountryId(token[i]);
				        break;
				     case  49:
				    	 obj.setOriginator(token[i]);
				        break; 
				     case  50:
				    	 obj.setDesResNo(token[i]);
				        break;
				     case  51:
				    	 obj.setDesUploadStatus(Boolean.parseBoolean(token[i]));
				        break;  
				     case  52:
				    	 obj.setDmcMarketId(token[i]);
				        break;
				     case  53:
				    	 obj.setBedPreferences(token[i]);
				        break;
				     case  54:
				    	 obj.setApplyPromotionCode(Boolean.parseBoolean(token[i]));
				        break;
				     case  55:
				    	 obj.setPromotionCode(token[i]);
				        break;
				     case  56:
				    	 guide.setNoOfGuides(Integer.parseInt(token[i]));
				        break;
				     case  57:
				    	 guide.setGuideRate(convertStringToDouble(token[i]));
				        break;
				     case  58:
				    	 guide.setGuideMeal(token[i]);
				        break;
				     case  59:
				    	 guide.setGuideOccupancyType(token[i]);
				        break;
				     case  60:
				    	 obj.setGuideRequire(Boolean.parseBoolean(token[i]));
				        break;
				     case  61:
				    	 guide.setGuideRoomCategory(token[i]);
				        break;
				     //You can have any number of case statements.
				     default : //Optional
				    	 System.out.println("Invalid grade " + count);
				 }
				     
				     ++count;
				}
			
			obj.setAgentDetails(agent);
			obj.setGuestDetails(guest);
			obj.setArrivalDetails(arrivalDetails);
		    obj.setDepartureDetails(departureDetails);
		    obj.setMealDetails(mealDetails);
		    obj.setTimeStamp(timeStamp);
		    obj.setGuide(guide);
		}
		
		
		return obj;
	}
	
	private ReservationDetails prepareReservationDetails(String str)
	{
		ReservationDetails obj = null;
		
		int count	   = 0;
		if(str!=null)
		{
			obj  = new ReservationDetails();
			TimeStamp timeStamp 			= new TimeStamp();
			  
			String[] token = str.split("\\"+spch);
			
				for (int i = 0; i < token.length; i++)  {
				     
				     switch(count){
				     case  0:
				    	 obj.setUuid(token[i]);
				        break; //optional
				     case  1:
				    	 obj.setReservationId(token[i]);
				        break; //optional   
				     case  2:
				    	 obj.setRoomCategoryId(token[i]);
				        break; //optional
				     case  3:
				    	 obj.setCurrency(token[i]);
				        break;
				     case  4:
				    	 obj.setRate(token[i]);
				        break;
				     case  5:
				    	 obj.setRateCodeId(token[i]);
				        break; 
				     case  6:
				    	 obj.setCheckInDate(token[i]);
				        break;  
				     case  7:
				    	 obj.setCheckOutDate(token[i]);
				        break; 
				     case  8:
				    	 obj.setNoOfRooms(Integer.parseInt(token[i]));
				        break;  
				     case  9:
				    	 obj.setOccupancyType(token[i]);
				        break;
				     case  10:
				    	 obj.setMealBasis(token[i]);
				        break;
				     case  11:
				    	 obj.setNoOfAdults(Integer.parseInt(token[i]));
				        break;
				     case  12:
				    	 obj.setNoOfChildren(Integer.parseInt(token[i]));
				        break; 
				     case  13:
				    	 obj.setGuide(Integer.parseInt(token[i]));
				        break; 
				     case  14:
				    	 obj.setStatus(token[i]);
				        break; 
				     case  15:
				    	 timeStamp.setCreatedByTimeStamp(token[i]);
				        break;
				     case  16:
				    	 timeStamp.setCreatedById(token[i]);
				        break;
				     case  17:
				    	 timeStamp.setCreatedByName(token[i]);
				        break;
				     case  18:
				    	 timeStamp.setUpdatedByTimeStamp(token[i]);
				        break;
				     case  19:
				    	 timeStamp.setUpdatedById(token[i]);
				        break;
				     case  20:
				    	 timeStamp.setUpdatedByName(token[i]);
				        break;   
				     case  21:
				    	 obj.setNoOfRoomNights(Integer.parseInt(token[i]));
				        break; 
				     case  22:
				    	 obj.setVisualLineId(token[i]);
				        break; 
				     case  23:
				    	 obj.setRoomPaxType(token[i]);
				        break;
				     
				     default : //Optional
				    	 System.out.println("Invalid grade " + count);
				 }
				     
				     ++count;
				}
			
		    obj.setTimeStamp(timeStamp);
		}
		
		
		return obj;
	}
	
	private ReservationPaymentDetails prepareReservationPaymentDetails(String str)
	{
		ReservationPaymentDetails obj = null;
		
		int count	   = 0;
		if(str!=null)
		{
			obj  = new ReservationPaymentDetails();
			TimeStamp timeStamp 			= new TimeStamp();
			  
			String[] token = str.split("\\"+spch);
			
//			if(token.length == 11)
//			{
				for (int i = 0; i < token.length; i++)  {
				     
				     switch(count){
				     case  0:
				    	 obj.setUuid(token[i]);
				        break; //optional
				     case  1:
				    	 obj.setReservationId(token[i]);
				        break; //optional   
				     case  2:
				    	 obj.setCostCode(token[i]);
				        break; //optional
				     case  3:
				    	 obj.setAmount(token[i]);
				        break;
				     case  4:
				    	 obj.setStatus(token[i]);
				        break;
				     case  5:
				    	 timeStamp.setCreatedByTimeStamp(token[i]);
				        break;
				     case  6:
				    	 timeStamp.setCreatedById(token[i]);
				        break;
				     case  7:
				    	 timeStamp.setCreatedByName(token[i]);
				        break;
				     case  8:
				    	 timeStamp.setUpdatedByTimeStamp(token[i]);
				        break;
				     case  9:
				    	 timeStamp.setUpdatedById(token[i]);
				        break;
				     case  10:
				    	 timeStamp.setUpdatedByName(token[i]);
				        break;   
				     
				     default : //Optional
				    	 System.out.println("Invalid grade " + count);
				 }
				     
				     ++count;
				}
			
			//}
		    obj.setTimeStamp(timeStamp);
		}
		
		
		return obj;
	}
	
	private Guest prepareGuest(String str)
	{
		Guest obj = null;
		
		int count	   = 0;
		if(str!=null)
		{
			obj  = new Guest();
			TimeStamp timeStamp 			= new TimeStamp();
			PersonalDetails personalDetails = new PersonalDetails();
			ContactDetails contactObj 		= new ContactDetails();
			  
			String[] token = str.split("\\"+spch);
			
//			if(token.length == 27)
//			{
				for (int i = 0; i < token.length; i++)  {
				     
				     switch(count){
				     case  0:
				    	 obj.setUuid(token[i]);
				        break; //optional
				     case  1:
				    	 personalDetails.setTitle(token[i]);
				        break; //optional   
				     case  2:
				    	 personalDetails.setFirstName(token[i]);
				        break; //optional
				     case  3:
				    	 personalDetails.setLastName(token[i]);
				        break;
				     case  4:
				    	 personalDetails.setPassportNo(token[i]);
				        break;
				     case  5:
				    	 personalDetails.setDateOfBirth(token[i]);
				        break; 
				     case  6:
				    	 personalDetails.setCountryId(token[i]);
				        break;  
				     case  7:
				    	 contactObj.setPostalAddress(token[i]);
				        break; 
				     case  8:
				    	 contactObj.setLandNo(token[i]);
				        break;  
				     case  9:
				    	 contactObj.setMobileNo(token[i]);
				        break;
				     case  10:
				    	 contactObj.setWeb(token[i]);
				        break;
				     case  11:
				    	 contactObj.setEmail(token[i]);
				        break;
				     case  12:
				    	 contactObj.setSkype(token[i]);
				        break; 
				     case  13:
				    	 timeStamp.setCreatedByTimeStamp(token[i]);
				        break; 
				     case  14:
				    	 timeStamp.setCreatedById(token[i]);
				        break; 
				     case  15:
				    	 timeStamp.setCreatedByName(token[i]);
				        break;
				     case  16:
				    	 timeStamp.setUpdatedByTimeStamp(token[i]);
				        break;
				     case  17:
				    	 timeStamp.setUpdatedById(token[i]);
				        break;
				     case  18:
				    	 timeStamp.setUpdatedByName(token[i]);
				        break;
				     case  19:
				    	 obj.setStatus(token[i]);
				        break;
				     case  20:
				    	 contactObj.setFaxNo(token[i]);
				        break;   
				     case  21:
				    	 obj.setRemarks(token[i]);
				        break; 
				     case  22:
				    	 personalDetails.setMiddleName(token[i]);
				        break;
				     case  23:
				    	 obj.setArandaMemberorNot(Boolean.parseBoolean(token[i]));
				        break;
				     case  24:
				    	 obj.setAradanaMemberId(token[i]);
				        break;
				     case  25:
				    	 obj.setGuestRelationship(token[i]);
				        break;
				     case  26:
				    	 obj.setTreatmentCategory(token[i]);
				        break;
				     //You can have any number of case statements.
				     default : //Optional
				    	 System.out.println("Invalid grade " + count);
				 }
				     
				     ++count;
				}
			
			//}
			 obj.setPersonalDetails(personalDetails);
			 obj.setContactDetails(contactObj);
			 obj.setTimeStamp(timeStamp);
		}
		
		
		return obj;
	}
	
	private AgentDetails prepareAgent(String str)
	{
		AgentDetails _obj = null;
		
		int count	   = 0;
		if(str!=null)
		{
			_obj  = new AgentDetails();
			TimeStamp timeStamp 			= new TimeStamp();
			ContactDetails contactObj 		= new ContactDetails();
			
			String[] token = str.split("\\"+spch);
			
//			if(token.length == 22)
//			{
				for (int i = 0; i < token.length; i++)  {
				     
				     switch(count){
				     case  0:
				    	 _obj.setUuid(token[i]);
				        break; //optional
				     case  1:
				    	 _obj.setAgentCode(token[i]);
				        break; //optional   
				     case  2:
				    	 _obj.setAgentCategory(token[i]);
				        break; //optional
				     case  3:
				    	 _obj.setAgentName(token[i]);
				        break;
				     case  4:
				    	 contactObj.setPostalAddress(token[i]);
				        break;
				     case  5:
				    	 contactObj.setLandNo(token[i]);
				        break; 
				     case  6:
				    	 contactObj.setFaxNo(token[i]);
				        break;  
				     case  7:
				    	 contactObj.setEmail(token[i]);
				        break; 
				     case  8:
				    	 _obj.setDescription(token[i]);
				        break;  
				     case  9:
				    	 _obj.setAgentTypeId(token[i]);
				        break;
				     case  10:
				    	 _obj.setCountryId(token[i]);
				        break;
				     case  11:
				    	 timeStamp.setCreatedByTimeStamp(token[i]);
				        break; 
				     case  12:
				    	 timeStamp.setCreatedById(token[i]);
				        break; 
				     case  13:
				    	 timeStamp.setCreatedByName(token[i]);
				        break;
				     case  14:
				    	 timeStamp.setUpdatedByTimeStamp(token[i]);
				        break;
				     case  15:
				    	 timeStamp.setUpdatedById(token[i]);
				        break;
				     case  16:
				    	 timeStamp.setUpdatedByName(token[i]);
				        break;
				     case  17:
				    	 _obj.setStatus(token[i]);
				        break;
				     case  18:
				    	 _obj.setAgentMarketUuid(token[i]);
				        break;   
				     case  19:
				    	 contactObj.setWeb(token[i]);
				        break; 
				     case  20:
				    	 contactObj.setSkype(token[i]);
				        break;
				     case  21:
				    	 contactObj.setMobileNo(token[i]);
				        break;
				     case  22:
				    	 _obj.setContactPerson(token[i]);
				        break;
				     case  23:
				    	 _obj.setCreditLimit(convertStringToDouble(token[i]));
				        break;
				     case  24:
				    	 _obj.setVATNo(token[i]);
				        break;
				     case  25:
				    	 _obj.setPropertyId(token[i]);
				        break;
				     //You can have any number of case statements.
				     default : //Optional
				    	 System.out.println("Invalid grade " + count);
				 }
				     
				     ++count;
				}
			
			//}
			 _obj.setContactDetails(contactObj);
			 _obj.setTimeStamp(timeStamp);
		}
		
		
		return _obj;
	}
	
	private BaseRate prepareBaseRate(String str)
	{
		BaseRate _obj = null;
		
		int count	   = 0;
		if(str!=null)
		{
			_obj  = new BaseRate();
			TimeStamp timeStamp 			= new TimeStamp();
			
			String[] token = str.split("\\"+spch);
			
//			if(token.length == 15)
//			{
				for (int i = 0; i < token.length; i++)  {
				     
				     switch(count){
				     case  0:
				    	 _obj.setUuid(token[i]);
				        break; //optional
				     case  1:
				    	 _obj.setDescription(token[i]);
				        break; //optional   
				     case  2:
				    	 _obj.setCode(token[i]);
				        break; //optional
				     case  3:
				    	 _obj.setPropertyId(token[i]);
				        break;
				     case  4:   
				    	 timeStamp.setCreatedByTimeStamp(token[i]);
				    	 break;
				     case  5:
				    	 timeStamp.setCreatedById(token[i]);
				        break; 
				     case  6:
				    	 timeStamp.setCreatedByName(token[i]);
				        break;
				     case  7:
				    	 timeStamp.setUpdatedByTimeStamp(token[i]);
				        break;
				     case  8:
				    	 timeStamp.setUpdatedById(token[i]);
				        break;
				     case  9:
				    	 timeStamp.setUpdatedByName(token[i]);
				        break;
				     case  10:
				    	 _obj.setStatus(token[i]);
				        break;
				     case  11:
				    	 _obj.setCurrencyId(token[i]);
				        break;   
				     case  12:
				    	 _obj.setCodeShortName(token[i]);
				        break; 
				     case  13:
				    	 _obj.setVisible(token[i]);
				        break;
				     case  14:
				    	 _obj.setConfirmToVisible(Boolean.parseBoolean(token[i]));
				        break;
				     //You can have any number of case statements.
				     default : //Optional
				    	 System.out.println("Invalid grade " + count);
				 }
				     
				     ++count;
				}
			
			//}
			 _obj.setTimeStamp(timeStamp);
		}
		
		
		return _obj;
	}
	
	private SeasonalBaseRate prepareSeasonalBaseRate(String str)
	{
		SeasonalBaseRate _obj = null;
		
		int count	   = 0;
		if(str!=null)
		{
			_obj  = new SeasonalBaseRate();
			TimeStamp timeStamp 			= new TimeStamp();
			
			String[] token = str.split("\\"+spch);
			
//			if(token.length == 17)
//			{
				for (int i = 0; i < token.length; i++)  {
				     
				     switch(count){
				     case  0:
				    	 _obj.setUuid(token[i]);
				        break; //optional
				     case  1:
				    	 _obj.setDescription(token[i]);
				        break; //optional  
				     case  2:
				    	 _obj.setCode(token[i]);
				        break;    
				     case  3:
				    	 _obj.setSeasonId(token[i]);
				        break; //optional
				     case  4:
				    	 _obj.setParentUuid(token[i]);
				        break;
				     case  5:   
				    	 timeStamp.setCreatedByTimeStamp(token[i]);
				    	 break;
				     case  6:
				    	 timeStamp.setCreatedById(token[i]);
				        break; 
				     case  7:
				    	 timeStamp.setCreatedByName(token[i]);
				        break;
				     case  8:
				    	 timeStamp.setUpdatedByTimeStamp(token[i]);
				        break;
				     case  9:
				    	 timeStamp.setUpdatedById(token[i]);
				        break;
				     case  10:
				    	 timeStamp.setUpdatedByName(token[i]);
				        break;
				     case  11:
				    	 _obj.setStatus(token[i]);
				        break;
				     case  12:
				    	 _obj.setCurrency(token[i]);
				        break; 
				     case  13:
				    	 _obj.setPropertyId(token[i]);
				        break;   
				     case  14:
				    	 _obj.setCodeShortName(token[i]);
				        break; 
				     case  15:
				    	 _obj.setVisible(token[i]);
				        break;
				     case  16:
				    	 _obj.setConfirmToVisible(Boolean.parseBoolean(token[i]));
				        break;
				     //You can have any number of case statements.
				     default : //Optional
				    	 System.out.println("Invalid grade " + count);
				 }
				     
				     ++count;
				}
			
			//}
			 _obj.setTimeStamp(timeStamp);
		}
		
		
		return _obj;
	}
	
	private AppliedAgentMarkets prepareRateCodeMapping(String str)
	{
		AppliedAgentMarkets _obj = null;
		
		int count	   = 0;
		if(str!=null)
		{
			_obj  = new AppliedAgentMarkets();
			TimeStamp timeStamp 			= new TimeStamp();
			
			String[] token = str.split("\\"+spch);
			
//			if(token.length == 12)
//			{
				for (int i = 0; i < token.length; i++)  {
				     
				     switch(count){
				     case  0:
				    	 _obj.setUuid(token[i]);
				        break; //optional
				     case  1:
				    	 _obj.setMarketcodeUuid(token[i]);
				        break; //optional  
				     case  2:
				    	 _obj.setRateCodeUuid(token[i]);
				        break;    
				     case  3:
				    	 _obj.setStatus(token[i]);
				        break;
				     case  4:   
				    	 timeStamp.setCreatedByTimeStamp(token[i]);
				    	 break;
				     case  5:
				    	 timeStamp.setCreatedById(token[i]);
				        break; 
				     case  6:
				    	 timeStamp.setCreatedByName(token[i]);
				        break;
				     case  7:
				    	 timeStamp.setUpdatedByTimeStamp(token[i]);
				        break;
				     case  8:
				    	 timeStamp.setUpdatedById(token[i]);
				        break;
				     case  9:
				    	 timeStamp.setUpdatedByName(token[i]);
				        break;
				     case  10:
				    	 _obj.setSeasonId(token[i]);
				        break;
				     case  11:
				    	 _obj.setHotelId(token[i]);
				        break; 
				     case  12:
				    	 _obj.setCurrencyId(token[i]);
				        break;
				     //You can have any number of case statements.
				     default : //Optional
				    	 System.out.println("Invalid grade " + count);
				 }
				     
				     ++count;
				}
			
			//}
			 _obj.setTimeStamp(timeStamp);
		}
		
		
		return _obj;
	}
	
	private OtherCosts prepareOtherCost(String str)
	{
		OtherCosts _obj = null;
		
		int count	   = 0;
		if(str!=null)
		{
			_obj  = new OtherCosts();
			TimeStamp timeStamp 			= new TimeStamp();
			
			String[] token = str.split("\\"+spch);
			
//			if(token.length == 15)
//			{
				for (int i = 0; i < token.length; i++)  {
				     
				     switch(count){
				     case  0:
				    	 _obj.setUuid(token[i]);
				        break; //optional
				     case  1:
				    	 _obj.setChangeItemId(token[i]);
				        break; //optional  
				     case  2:
				    	 _obj.setChangeAction(token[i]);
				        break;    
				     case  3:
				    	 _obj.setChangeType(token[i]);
				        break;
				     case  4:
				    	 _obj.setChangeValue(convertStringToDouble(token[i]));
				        break;
				     case  5:
				    	 _obj.setParentId(token[i]);
				        break;
				     case  6:
				    	 _obj.setParentType(token[i]);
				        break;
				     case  7:   
				    	 timeStamp.setCreatedByTimeStamp(token[i]);
				    	 break;
				     case  8:
				    	 timeStamp.setCreatedById(token[i]);
				        break; 
				     case  9:
				    	 timeStamp.setCreatedByName(token[i]);
				        break;
				     case  10:
				    	 timeStamp.setUpdatedByTimeStamp(token[i]);
				        break;
				     case  11:
				    	 timeStamp.setUpdatedById(token[i]);
				        break;
				     case  12:
				    	 timeStamp.setUpdatedByName(token[i]);
				        break;
				     case  13:
				    	 _obj.setStatus(token[i]);
				        break;
				     case  14:
				    	 _obj.setItemOrder(Integer.parseInt(token[i]));
				        break; 
				     //You can have any number of case statements.
				     default : //Optional
				    	 System.out.println("Invalid grade " + count);
				 }
				     
				     ++count;
				}
			
			//}
			 _obj.setTimeStamp(timeStamp);
		}
		
		
		return _obj;
	}
	
	private RoomFacilitiesMaster prepareRoomFacility(String str)
	{
		RoomFacilitiesMaster _obj = null;
		
		int count	   = 0;
		if(str!=null)
		{
			_obj  = new RoomFacilitiesMaster();
			TimeStamp timeStamp 			= new TimeStamp();
			
			String[] token = str.split("\\"+spch);
			
//			if(token.length == 12)
//			{
				for (int i = 0; i < token.length; i++)  {
				     
				     switch(count){
				     case  0:
				    	 _obj.setUuid(token[i]);
				        break; //optional
				     case  1:
				    	 _obj.setFacilityCode(token[i]);
				        break; //optional  
				     case  2:
				    	 _obj.setFacilityType(token[i]);
				        break;    
				     case  3:
				    	 _obj.setDescription(token[i]);
				        break;
				     case  4:
				    	 _obj.setNote(token[i]);
				        break;
				     case  5:   
				    	 timeStamp.setCreatedByTimeStamp(token[i]);
				    	 break;
				     case  6:
				    	 timeStamp.setCreatedById(token[i]);
				        break; 
				     case  7:
				    	 timeStamp.setCreatedByName(token[i]);
				        break;
				     case  8:
				    	 timeStamp.setUpdatedByTimeStamp(token[i]);
				        break;
				     case  9:
				    	 timeStamp.setUpdatedById(token[i]);
				        break;
				     case  10:
				    	 timeStamp.setUpdatedByName(token[i]);
				        break;
				     case  11:
				    	 _obj.setStatus(token[i]);
				        break;
				     //You can have any number of case statements.
				     default : //Optional
				    	 System.out.println("Invalid grade " + count);
				 }
				     
				     ++count;
				}
			
			//}
			 _obj.setTimeStamp(timeStamp);
		}
		
		
		return _obj;
	}
	
	private OtherCostsMaster prepareOtherCostMaster(String str)
	{
		OtherCostsMaster _obj = null;
		
		int count	   = 0;
		if(str!=null)
		{
			_obj  = new OtherCostsMaster();
			TimeStamp timeStamp 			= new TimeStamp();
			
			String[] token = str.split("\\"+spch);
			
//			if(token.length == 12)
//			{
				for (int i = 0; i < token.length; i++)  {
				     
				     switch(count){
				     case  0:
				    	 _obj.setUuid(token[i]);
				        break; //optional
				     case  1:
				    	 _obj.setChangeItemCode(token[i]);
				        break; //optional  
				     case  2:
				    	 _obj.setDescription(token[i]);
				        break;    
				     case  3:
				    	 _obj.setChangeType(token[i]);
				        break;
				     case  4:
				    	 _obj.setStatus(token[i]);
				        break;
				     case  5:   
				    	 timeStamp.setCreatedByTimeStamp(token[i]);
				    	 break;
				     case  6:
				    	 timeStamp.setCreatedById(token[i]);
				        break; 
				     case  7:
				    	 timeStamp.setCreatedByName(token[i]);
				        break;
				     case  8:
				    	 timeStamp.setUpdatedByTimeStamp(token[i]);
				        break;
				     case  9:
				    	 timeStamp.setUpdatedById(token[i]);
				        break;
				     case  10:
				    	 timeStamp.setUpdatedByName(token[i]);
				        break;
				     case  11:
				    	 _obj.setItemOrder(Integer.parseInt(token[i]));
				        break;
				     //You can have any number of case statements.
				     default : //Optional
				    	 System.out.println("Invalid grade " + count);
				 }
				     
				     ++count;
				}
			
			//}
			 _obj.setTimeStamp(timeStamp);
		}
		
		
		return _obj;
	}
	
	private ReservationDiscounts prepareReservationDiscounts(String str)
	{
		ReservationDiscounts _obj = null;
		
		int count	   = 0;
		if(str!=null)
		{
			_obj  = new ReservationDiscounts();
			TimeStamp timeStamp 			= new TimeStamp();
			
			String[] token = str.split("\\"+spch);
			
//			if(token.length == 13)
//			{
				for (int i = 0; i < token.length; i++)  {
				     
				     switch(count){
				     case  0:
				    	 _obj.setUuid(token[i]);
				        break; //optional
				     case  1:
				    	 _obj.setReservationId(token[i]);
				        break;   
				     case  2:
				    	 _obj.setDiscountId(token[i]);
				        break; //optional  
				     case  3:
				    	 _obj.setDiscountAmount(convertStringToDouble(token[i]));
				        break;    
				     case  4:
				    	 _obj.setAmountType(token[i]);
				        break;
				     case  5:
				    	 _obj.setDsType(token[i]);
				        break;
				     case  6:
				    	 timeStamp.setCreatedById(token[i]);
				        break; 
				     case  7:
				    	 timeStamp.setCreatedByName(token[i]);
				        break;
				     case  8:   
				    	 timeStamp.setCreatedByTimeStamp(token[i]);
				    	 break;  
				     case  9:
				    	 timeStamp.setUpdatedById(token[i]);
				        break;
				     case  10:
				    	 timeStamp.setUpdatedByName(token[i]);
				        break;
				     case  11:
				    	 timeStamp.setUpdatedByTimeStamp(token[i]);
				        break;   
				     case  12:
				    	 _obj.setStatus(token[i]);
				        break;
				     case  13:
				    	 _obj.setVisualLineId(token[i]);
				        break;
				     case  14:
				    	 _obj.setOccupancyType(token[i]);
				        break;
				     case  15:
				    	 _obj.setNoOfUnits(convertStringToDouble(token[i]));
				        break;
				     case  16:
				    	 _obj.setApplicableStatus(Boolean.parseBoolean(token[i]));
				        break;
				     //You can have any number of case statements.
				     default : //Optional
				    	 System.out.println("Invalid grade " + count);
				 }
				     
				     ++count;
				}
			
			//}
			 _obj.setTimeStamp(timeStamp);
		}
		
		
		return _obj;
	}
	
	private Tax prepareTaxMaster(String str)
	{
		Tax _obj = null;
		
		int count	   = 0;
		if(str!=null)
		{
			_obj  = new Tax();
			TimeStamp timeStamp 			= new TimeStamp();
			
			String[] token = str.split("\\"+spch);
			
//			if(token.length == 15)
//			{
				for (int i = 0; i < token.length; i++)  {
				     
				     switch(count){
				     case  0:
				    	 _obj.setUuid(token[i]);
				        break; //optional
				     case  1:
				    	 _obj.setTaxCode(token[i]);
				        break; //optional  
				     case  2:
				    	 _obj.setTaxDescription(token[i]);
				        break;    
				     case  3:
				    	 _obj.setDateEffectiveOn(token[i]);
				        break;
				     case  4:
				    	 _obj.setDateInactiveOn(token[i]);
				        break;
				     case  5:
				    	 _obj.setTaxType(token[i]);
				        break;
				     case  6:
				    	 _obj.setTaxAmount(convertStringToDouble(token[i]));
				        break;
				     case  7:
				    	 _obj.setTaxCalculationSequence(Integer.parseInt(token[i]));
				        break;
				     case  8:
				    	 _obj.setStatus(token[i]);
				        break;
				     case  9:   
				    	 timeStamp.setCreatedByTimeStamp(token[i]);
				    	 break;
				     case  10:
				    	 timeStamp.setCreatedById(token[i]);
				        break; 
				     case  11:
				    	 timeStamp.setCreatedByName(token[i]);
				        break;
				     case  12:
				    	 timeStamp.setUpdatedByTimeStamp(token[i]);
				        break;
				     case  13:
				    	 timeStamp.setUpdatedById(token[i]);
				        break;
				     case  14:
				    	 timeStamp.setUpdatedByName(token[i]);
				        break;
				     //You can have any number of case statements.
				     default : //Optional
				    	 System.out.println("Invalid grade " + count);
				 }
				     
				     ++count;
				}
			
			//}
			 _obj.setTimeStamp(timeStamp);
		}
		
		
		return _obj;
	}
	
	private RoomFacilitiesByProperty prepareRoomFacilityByProperty(String str)
	{
		RoomFacilitiesByProperty _obj = null;
		
		int count	   = 0;
		if(str!=null)
		{
			_obj  = new RoomFacilitiesByProperty();
			TimeStamp timeStamp 			= new TimeStamp();
			
			String[] token = str.split("\\"+spch);
			
//			if(token.length == 11)
//			{
				for (int i = 0; i < token.length; i++)  {
				     
				     switch(count){
				     case  0:
				    	 _obj.setUuid(token[i]);
				        break; //optional
				     case  1:
				    	 _obj.setFacilityId(token[i]);
				        break; //optional  
				     case  2:
				    	 _obj.setPropertyId(token[i]);
				        break;    
				     case  3:
				    	 _obj.setRoomCategoryId(token[i]);
				        break;
				     case  4:
				    	 _obj.setStatus(token[i]);
				        break;
				     case  5:   
				    	 timeStamp.setCreatedByTimeStamp(token[i]);
				    	 break;
				     case  6:
				    	 timeStamp.setCreatedById(token[i]);
				        break; 
				     case  7:
				    	 timeStamp.setCreatedByName(token[i]);
				        break;
				     case  8:
				    	 timeStamp.setUpdatedByTimeStamp(token[i]);
				        break;
				     case  9:
				    	 timeStamp.setUpdatedById(token[i]);
				        break;
				     case  10:
				    	 timeStamp.setUpdatedByName(token[i]);
				        break;
				     //You can have any number of case statements.
				     default : //Optional
				    	 System.out.println("Invalid grade " + count);
				 }
				     
				     ++count;
				}
			
			//}
			 _obj.setTimeStamp(timeStamp);
		}
		
		
		return _obj;
	}
	
	private RoomCatagoryCost prepareRoomCategoryCost(String str)
	{
		RoomCatagoryCost _obj = null;
		
		int count	   = 0;
		if(str!=null)
		{
			_obj  = new RoomCatagoryCost();
			TimeStamp timeStamp 			= new TimeStamp();
			
			String[] token = str.split("\\"+spch);
			
//			if(token.length == 16)
//			{
				for (int i = 0; i < token.length; i++)  {
				     
				     switch(count){
				     case  0:
				    	 _obj.setUuid(token[i]);
				        break; //optional
				     case  1:
				    	 _obj.setParentId(token[i]);
				        break; //optional  
				     case  2:
				    	 _obj.setParentType(token[i]);
				        break;    
				     case  3:
				    	 _obj.setPropertyUuid(token[i]);
				        break;
				     case  4:
				    	 _obj.setAppartmentUuid(token[i]);
				        break;
				     case  5:
				    	 _obj.setPrice(convertStringToDouble(token[i]));
				        break;
				     case  6:
				    	 _obj.setCurrencyId(token[i]);
				        break;
				     case  7:   
				    	 timeStamp.setCreatedByTimeStamp(token[i]);
				    	 break;
				     case  8:
				    	 timeStamp.setCreatedById(token[i]);
				        break; 
				     case  9:
				    	 timeStamp.setCreatedByName(token[i]);
				        break;
				     case  10:
				    	 timeStamp.setUpdatedByTimeStamp(token[i]);
				        break;
				     case  11:
				    	 timeStamp.setUpdatedById(token[i]);
				        break;
				     case  12:
				    	 timeStamp.setUpdatedByName(token[i]);
				        break;
				     case  13:
				    	 _obj.setStatus(token[i]);
				        break;
				     case  14:
				    	 _obj.setChangeAction(token[i]);
				        break; 
				     case  15:
				    	 _obj.setChangeType(token[i]);
				        break;   
				     //You can have any number of case statements.
				     default : //Optional
				    	 System.out.println("Invalid grade " + count);
				 }
				     
				     ++count;
				}
			
			//}
			 _obj.setTimeStamp(timeStamp);
		}
		
		
		return _obj;
	}
	
	private RateCode prepareRateCode(String str)
	{
		RateCode _obj = null;
		
		int count	   = 0;
		if(str!=null)
		{
			_obj  = new RateCode();
			TimeStamp timeStamp 			= new TimeStamp();
			Commission commision			= new Commission();
			
			String[] token = str.split("\\"+spch);
			
//			if(token.length == 21)
//			{
				for (int i = 0; i < token.length; i++)  {
				     
				     switch(count){
				     case  0:
				    	 _obj.setUuid(token[i]);
				        break; //optional
				     case  1:
				    	 _obj.setDescription(token[i]);
				        break; //optional  
				     case  2:
				    	 _obj.setCode(token[i]);
				        break;    
				     case  3:
				    	 _obj.setParentUuid(token[i]);
				        break; //optional
				     case  4:
				    	 commision.setCommType(token[i]);
				        break;
				     case  5:
				    	 commision.setCommValue(convertStringToDouble(token[i]));
				        break;   
				     case  6:   
				    	 timeStamp.setCreatedByTimeStamp(token[i]);
				    	 break;
				     case  7:
				    	 timeStamp.setCreatedById(token[i]);
				        break; 
				     case  8:
				    	 timeStamp.setCreatedByName(token[i]);
				        break;
				     case  9:
				    	 timeStamp.setUpdatedByTimeStamp(token[i]);
				        break;
				     case  10:
				    	 timeStamp.setUpdatedById(token[i]);
				        break;
				     case  11:
				    	 timeStamp.setUpdatedByName(token[i]);
				        break;
				     case  12:
				    	 _obj.setStatus(token[i]);
				        break;
				     case  13:
				    	 _obj.setCurrency(token[i]);
				        break; 
				     case  14:
				    	 _obj.setPropertyId(token[i]);
				        break;   
				     case  15:
				    	 _obj.setCodeShortName(token[i]);
				        break; 
				     case  16:
				    	 _obj.setVisible(token[i]);
				        break;
				     case  17:
				    	 _obj.setConfirmToVisible(Boolean.parseBoolean(token[i]));
				        break;
				     case  18:
				    	 _obj.setSeasonId(token[i]);
				        break;
				     case  19:
				    	 _obj.setApplyTaxes(Boolean.parseBoolean(token[i]));
				        break;   
				     case  20:
				    	 _obj.setRoundOffValue(Boolean.parseBoolean(token[i]));
				        break;   
				     //You can have any number of case statements.
				     default : //Optional
				    	 System.out.println("Invalid grade " + count);
				 }
				     
				     ++count;
				}
			
			//}
			 _obj.setCommission(commision);
			 _obj.setTimeStamp(timeStamp);
		}
		
		
		return _obj;
	}
	
	private RateCodeRoomRate prepareRateCodeRoomRate(String str)
	{
		RateCodeRoomRate _obj = null;
		
		int count	   = 0;
		if(str!=null)
		{
			_obj  = new RateCodeRoomRate();
			TimeStamp timeStamp 	= new TimeStamp();
			
			String[] token = str.split("\\"+spch);
			
//			if(token.length == 20)
//			{
				for (int i = 0; i < token.length; i++)  {
				     
				     switch(count){
				     case  0:
				    	 _obj.setUuid(token[i]);
				        break; //optional
				     case  1:
				    	 _obj.setRateCodeId(token[i]);
				        break; //optional   
				     case  2:
				    	 _obj.setPropertyId(token[i]);
				        break; //optional
				     case  3:
				    	 _obj.setSeasonId(token[i]);
				        break;
				     case  4:
				    	 _obj.setRoomCategoryId(token[i]);
				        break;
				     case  5:
				    	 _obj.setOccupancyType(token[i]);
				        break; 
				     case  6:
				    	 _obj.setMealBasis(token[i]);
				        break;  
				     case  7:
				    	 _obj.setStatus(token[i]);
				        break; 
				     case  8:
				    	 _obj.setActualRoomRateWithoutTax(convertStringToDouble((token[i])));
				        break;  
				     case  9:
				    	 _obj.setActualRoomRateWithTax(convertStringToDouble(token[i]));
				        break;
				     case  10:
				    	 _obj.setRoundedRoomRateWithTax(convertStringToDouble(token[i]));
				        break;
				     case  11:
				    	 _obj.setActualTaxAmount(convertStringToDouble(token[i]));
				        break; 
				     case  12:
				    	 _obj.setRoundOffValue(Boolean.parseBoolean(token[i]));
				        break; 
				     case  13:
				    	 _obj.setTaxApplied(Boolean.parseBoolean(token[i]));
				        break;
				     case  14:
				    	 timeStamp.setCreatedByTimeStamp(token[i]);
				        break;
				     case  15:
				    	 timeStamp.setCreatedById(token[i]);
				        break;
				     case  16:
				    	 timeStamp.setCreatedByName(token[i]);
				        break;
				     case  17:
				    	 timeStamp.setUpdatedByTimeStamp(token[i]);
				        break;
				     case  18:
				    	 timeStamp.setUpdatedById(token[i]);
				        break;   
				     case  19:
				    	 timeStamp.setUpdatedByName(token[i]);
				        break; 
				     case  20:
				    	 _obj.setCurrencyId(token[i]);
				        break;
				     //You can have any number of case statements.
				     default : //Optional
				    	 System.out.println("Invalid grade " + count);
				 }
				     
				     ++count;
				}
			
			//}
			 _obj.setTimeStamp(timeStamp);
		}
		
		
		return _obj;
	}

	
	private RoomCategory prepareRoomCategory(String str)
	{
		RoomCategory _obj = null;
		
		int count	   = 0;
		if(str!=null)
		{
			_obj  = new RoomCategory();
			TimeStamp timeStamp 	= new TimeStamp();
			
			String[] token = str.split("\\"+spch);
			
//			if(token.length == 10)
//			{
				for (int i = 0; i < token.length; i++)  {
				     
				     switch(count){
				     case  0:
				    	 _obj.setUuid(token[i]);
				        break; //optional
				     case  1:
				    	 _obj.setRoomCategoryCode(token[i]);
				        break; //optional   
				     case  2:
				    	 _obj.setRoomCategoryName(token[i]);
				        break; //optional
				     case  3:
				    	 _obj.setStatus(token[i]);
				        break;
				     case  4:
				    	 timeStamp.setCreatedByTimeStamp(token[i]);
				        break;
				     case  5:
				    	 timeStamp.setCreatedById(token[i]);
				        break;
				     case  6:
				    	 timeStamp.setCreatedByName(token[i]);
				        break;
				     case  7:
				    	 timeStamp.setUpdatedByTimeStamp(token[i]);
				        break;
				     case  8:
				    	 timeStamp.setUpdatedById(token[i]);
				        break;   
				     case  9:
				    	 timeStamp.setUpdatedByName(token[i]);
				        break; 
				     case  10:
				    	 _obj.setRoomUseableType(token[i]);
				        break;
				     //You can have any number of case statements.
				     default : //Optional
				    	 System.out.println("Invalid grade " + count);
				 }
				     
				     ++count;
				}
			
			//}
			 _obj.setTimeStamp(timeStamp);
		}
		
		
		return _obj;
	}
	
	private RoomCategoryByHotel prepareRoomCategoryByHotel(String str)
	{
		RoomCategoryByHotel _obj = null;
		
		int count	   = 0;
		if(str!=null)
		{
			_obj  = new RoomCategoryByHotel();
			TimeStamp timeStamp 	= new TimeStamp();
			
			String[] token = str.split("\\"+spch);
			
//			if(token.length == 12)
//			{
				for (int i = 0; i < token.length; i++)  {
				     
				     switch(count){
				     case  0:
				    	 _obj.setUuid(token[i]);
				        break; //optional
				     case  1:
				    	 _obj.setPropertyId(token[i]);
				        break; //optional   
				     case  2:
				    	 _obj.setRoomCategoryId(token[i]);
				        break; //optional
				     case  3:
				    	 _obj.setStatus(token[i]);
				        break;
				     case  4:
				    	 timeStamp.setCreatedByTimeStamp(token[i]);
				        break;
				     case  5:
				    	 timeStamp.setCreatedById(token[i]);
				        break;
				     case  6:
				    	 timeStamp.setCreatedByName(token[i]);
				        break;
				     case  7:
				    	 timeStamp.setUpdatedByTimeStamp(token[i]);
				        break;
				     case  8:
				    	 timeStamp.setUpdatedById(token[i]);
				        break;   
				     case  9:
				    	 timeStamp.setUpdatedByName(token[i]);
				        break; 
				     case  10:
				    	 _obj.setVisible(token[i]);
				        break;
				     case  11:
				    	 _obj.setTotalRooms(Integer.parseInt(token[i]));
				        break;    
				     //You can have any number of case statements.
				     default : //Optional
				    	 System.out.println("Invalid grade " + count);
				 }
				     
				     ++count;
				}
			
			//}
			 _obj.setTimeStamp(timeStamp);
		}
		
		
		return _obj;
	}
	
	private AgentMarkets prepareAgentMarket(String str)
	{
		AgentMarkets _obj = null;
		
		int count	   = 0;
		if(str!=null)
		{
			_obj  = new AgentMarkets();
			TimeStamp timeStamp 	= new TimeStamp();
			
			String[] token = str.split("\\"+spch);
			
//			if(token.length == 10)
//			{
				for (int i = 0; i < token.length; i++)  {
				     
				     switch(count){
				     case  0:
				    	 _obj.setUuid(token[i]);
				        break; //optional
				     case  1:
				    	 _obj.setAgentMarketCode(token[i]);
				        break; //optional   
				     case  2:
				    	 _obj.setDescription(token[i]);
				        break; //optional
				     case  3:
				    	 _obj.setStatus(token[i]);
				        break;
				     case  4:
				    	 timeStamp.setCreatedByTimeStamp(token[i]);
				        break;
				     case  5:
				    	 timeStamp.setCreatedById(token[i]);
				        break;
				     case  6:
				    	 timeStamp.setCreatedByName(token[i]);
				        break;
				     case  7:
				    	 timeStamp.setUpdatedByTimeStamp(token[i]);
				        break;
				     case  8:
				    	 timeStamp.setUpdatedById(token[i]);
				        break;   
				     case  9:
				    	 timeStamp.setUpdatedByName(token[i]);
				        break; 
				     //You can have any number of case statements.
				     default : //Optional
				    	 System.out.println("Invalid grade " + count);
				 }
				     
				     ++count;
				}
			
			//}
			 _obj.setTimeStamp(timeStamp);
		}
		
		
		return _obj;
	}
	
	private Property prepareProperty(String str)
	{
		Property _obj = null;
		
		int count	   = 0;
		if(str!=null)
		{
			_obj  = new Property();
			TimeStamp timeStamp 	= new TimeStamp();
			
			String[] token = str.split("\\"+spch);
			
//			if(token.length == 10)
//			{
				for (int i = 0; i < token.length; i++)  {
				     
				     switch(count){
				     case  0:
				    	 _obj.setUuid(token[i]);
				        break; //optional
				     case  1:
				    	 _obj.setPropertyCode(token[i]);
				        break; //optional   
				     case  2:
				    	 _obj.setPropertyName(token[i]);
				        break; //optional
				     case  3:
				    	 _obj.setLocationId(token[i]);
				        break;
				     case  4:
				    	 _obj.setContactId(token[i]);
				        break;   
				     case  5:
				    	 _obj.setStatus(token[i]);
				        break;   
				     case  6:
				    	 timeStamp.setCreatedByTimeStamp(token[i]);
				        break;
				     case  7:
				    	 timeStamp.setCreatedById(token[i]);
				        break;
				     case  8:
				    	 timeStamp.setCreatedByName(token[i]);
				        break;
				     case  9:
				    	 timeStamp.setUpdatedByTimeStamp(token[i]);
				        break;
				     case  10:
				    	 timeStamp.setUpdatedById(token[i]);
				        break;   
				     case  11:
				    	 timeStamp.setUpdatedByName(token[i]);
				        break; 
				     case  12:
				    	 _obj.setOutQPoint(token[i]);
				        break;    
				     //You can have any number of case statements.
				     default : //Optional
				    	 System.out.println("Invalid grade " + count);
				 }
				     
				     ++count;
				}
			
			//}
			 _obj.setTimeStamp(timeStamp);
		}
		
		
		return _obj;
	}
	
	private DestinityReservation prepareDestinityReservation(String str)
	{
		DestinityReservation _obj = null;
		
		int count	   = 0;
		if(str!=null)
		{
			_obj  = new DestinityReservation();
			
			String[] token = str.split("\\"+spch);
			
			
//			if(token.length == 10)
//			{
				for (int i = 0; i < token.length; i++)  {
				     
				     switch(count){
				     case  0:
				    	 _obj.setDesResNo(token[i]);
				        break; //optional
				     case  1:
				    	 _obj.setTitle(token[i]);
				        break; //optional   
				     case  2:
				    	 _obj.setFirstName(token[i]);
				        break; //optional
				     case  3:
				    	 _obj.setLastName(token[i]);
				        break;
				     case  4:
				    	 _obj.setPassportNo(token[i]);
				        break;   
				     case  5:
				    	 _obj.setEmail(token[i]);
				        break;   
				     case  6:
				    	 _obj.setLandNo(token[i]);
				        break;
				     case  7:
				    	 _obj.setArrivalMeal(token[i]);
				        break;
				     case  8:
				    	 _obj.setLeaveMeal(token[i]);
				        break;
				     case  9:
				    	 _obj.setAddress(token[i]);
				        break;
				     case  10:
				    	 _obj.setCountry(token[i]);
				        break;
				     case  11:
				    	 _obj.setRemarks(token[i]);
				        break;
				     case  12:
				    	 _obj.setPackageCode(token[i]);
				        break;
				     case  13:
				    	 _obj.setAgentId(token[i]);
				        break;
				     case  14:
				    	 _obj.setMarketCode(token[i]);
				        break;
				     case  15:
				    	 _obj.setRateCodeId(token[i]);
				        break;
				     case  16:
				    	 _obj.setHotelCode(token[i]);
				        break;
				     case  17:
				    	 _obj.setChildSupplement(token[i]);
				        break;
				     case  18:
				    	 _obj.setMealBasis(token[i]);
				        break;
				     case  19:
				    	 _obj.setCreateStatus(token[i]);
				        break;
				     case  20:
				    	 _obj.setResStatus(token[i]);
				        break;
				     case  21:
				    	 _obj.setUserId(token[i]);
				        break;
				     case  22:
				    	 _obj.setGuestId(token[i]);
				        break;
				     case  23:
				    	 _obj.setVoucherNo(token[i]);
				        break;
				     case  24:
				    	 _obj.setTourNo(token[i]);
				        break;
				     case  25:
				    	 _obj.setRoomXml(token[i]);
				        break;
				     case  26:
				    	 _obj.setArrivalDate(convertDateToSqlDate(token[i]));
				        break;
				     case  27:
				    	 _obj.setDepartureDate(convertDateToSqlDate(token[i]));
				        break; 
				     case  28:
				    	 _obj.setNoOfAdults(Integer.parseInt(token[i]));
				        break; 
				     case  29:
				    	 _obj.setNoOfChildren(Integer.parseInt(token[i]));
				        break;
				     case  30:
				    	 _obj.setSegment(token[i]);
				        break;
				     case  31:
				    	 _obj.setNatianaliity(token[i]);
				        break;
				     case  32:
				    	 _obj.setBookingType(token[i]);
				        break;
				     case  33:
				    	 _obj.setComplementaryType(Boolean.parseBoolean(token[i]));
				        break;
				     case  34:
				    	 _obj.setGuestType(token[i]);
				        break;
				     case  35:
				    	 _obj.setOtiumResId(token[i]);
				        break; 
				     //You can have any number of case statements.
				     default : //Optional
				    	 System.out.println("Invalid grade " + count);
				 }
				     
				     ++count;
				}
			
			//}
		}
		
		
		return _obj;
	}
	
	
	private DestinityAgent prepareDestinityAgent(String str)
	{
		DestinityAgent _obj = null;
		
		int count	   = 0;
		if(str!=null)
		{
			_obj  = new DestinityAgent();
			
			String[] token = str.split("\\"+spch);
			
			
//			if(token.length == 10)
//			{
				for (int i = 0; i < token.length; i++)  {
				     
				     switch(count){
				     case  0:
				    	 _obj.setOtiumAgentId(token[i]);
				        break; //optional
				     case  1:
				    	 _obj.setAgentCode(token[i]);
				        break; //optional   
				     case  2:
				    	 _obj.setAgentName(token[i]);
				        break; //optional
				     case  3:
				    	 _obj.setAddress(token[i]);
				        break;
				     case  4:
				    	 _obj.setLandNo(token[i]);
				        break;   
				     case  5:
				    	 _obj.setFaxNo(token[i]);
				        break;   
				     case  6:
				    	 _obj.setEmail(token[i]);
				        break;
				     case  7:
				    	 _obj.setCreditLimit(convertStringToBugDecimal(token[i]));
				        break;
				     case  8:
				    	 _obj.setVATNo(token[i]);
				        break;
				     case  9:
				    	 _obj.setContactperson(token[i]);
				        break;
				     case  10:
				    	 _obj.setFreeDays(token[i]);
				        break;
				     case  11:
				    	 _obj.setAgentType(token[i]);
				        break;
				     case  12:
				    	 _obj.setUpload(token[i]);
				        break;
				     case  13:
				    	 _obj.setDescription(token[i]);
				        break;
				     case  14:
				    	 _obj.setOperatorType(Boolean.parseBoolean(token[i]));
				        break;
				     case  15:
				    	 _obj.setCountryId(token[i]);
				        break;
				     case  16:
				    	 _obj.setCreateStatus(token[i]);
				        break;
				     case  17:
				    	 _obj.setBackColor(Integer.parseInt(token[i]));
				        break;
				     case  18:
				    	 _obj.setCrbal(Integer.parseInt(token[i]));
				        break;
				     case  19:
				    	 _obj.setCrPeriod(Integer.parseInt(token[i]));
				        break;
				     //You can have any number of case statements.
				     default : //Optional
				    	 System.out.println("Invalid grade " + count);
				 }
				     
				     ++count;
				}
			
			//}
		}
		
		
		return _obj;
	}
	
	private BigDecimal convertStringToBugDecimal(String str)
	{
		BigDecimal big = new BigDecimal(str);
		
		return big;
	}
	
	private java.sql.Date convertDateToSqlDate(String date) 
	{
		Date adate = new Date();
		try
		{
			adate = formateDate(date);
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		
		java.sql.Date sqlaDate = new java.sql.Date(adate.getTime());
		
		return sqlaDate;
	}
	
	private AgentType prepareAgentType(String str)
	{
		AgentType _obj = null;
		
		int count	   = 0;
		if(str!=null)
		{
			_obj  = new AgentType();
			TimeStamp timeStamp 	= new TimeStamp();
			
			String[] token = str.split("\\"+spch);
			
//			if(token.length == 11)
//			{
				for (int i = 0; i < token.length; i++)  {
				     
				     switch(count){
				     case  0:
				    	 _obj.setUuid(token[i]);
				        break; //optional
				     case  1:
				    	 _obj.setAgentTypeCode(token[i]);
				        break; //optional   
				     case  2:
				    	 _obj.setAgentTypeName(token[i]);
				        break; //optional
				     case  3:
				    	 _obj.setDescription(token[i]);
				        break;
				     case  4:
				    	 _obj.setStatus(token[i]);
				        break;
				     case  5:
				    	 timeStamp.setCreatedByTimeStamp(token[i]);
				        break;
				     case  6:
				    	 timeStamp.setCreatedById(token[i]);
				        break;
				     case  7:
				    	 timeStamp.setCreatedByName(token[i]);
				        break;
				     case  8:
				    	 timeStamp.setUpdatedByTimeStamp(token[i]);
				        break;
				     case  9:
				    	 timeStamp.setUpdatedById(token[i]);
				        break;   
				     case  10:
				    	 timeStamp.setUpdatedByName(token[i]);
				        break; 
				     //You can have any number of case statements.
				     default : //Optional
				    	 System.out.println("Invalid grade " + count);
				 }
				     
				     ++count;
				}
			
			//}
			 _obj.setTimeStamp(timeStamp);
		}
		
		
		return _obj;
	}
	
	private Seasons prepareSeason(String str)
	{
		Seasons _obj = null;
		
		int count	   = 0;
		if(str!=null)
		{
			_obj  = new Seasons();
			TimeStamp timeStamp 	= new TimeStamp();
			
			String[] token = str.split("\\"+spch);
			
//			if(token.length == 14)
//			{
				for (int i = 0; i < token.length; i++)  {
				     
				     switch(count){
				     case  0:
				    	 _obj.setUuid(token[i]);
				        break; //optional
				     case  1:
				    	 _obj.setSeasonCode(token[i]);
				        break; //optional   
				     case  2:
				    	 _obj.setDescription(token[i]);
				        break; //optional
				     case  3:
				    	 _obj.setStartTime(token[i]);
				        break;
				     case  4:
				    	 _obj.setEndTime(token[i]);
				        break;   
				     case  5:
				    	 timeStamp.setCreatedByTimeStamp(token[i]);
				        break;
				     case  6:
				    	 timeStamp.setCreatedById(token[i]);
				        break;
				     case  7:
				    	 timeStamp.setCreatedByName(token[i]);
				        break;
				     case  8:
				    	 timeStamp.setUpdatedByTimeStamp(token[i]);
				        break;
				     case  9:
				    	 timeStamp.setUpdatedById(token[i]);
				        break;   
				     case  10:
				    	 timeStamp.setUpdatedByName(token[i]);
				        break; 
				     case  11:
				    	 _obj.setStatus(token[i]);
				        break; 
				     case  12:
				    	 _obj.setSeasonShortName(token[i]);
				        break; 
				     case  13:
				    	 _obj.setVisible(token[i]);
				        break;    
				     //You can have any number of case statements.
				     default : //Optional
				    	 System.out.println("Invalid grade " + count);
				 }
				     
				     ++count;
				}
			
			//}
			 _obj.setTimeStamp(timeStamp);
		}
		
		
		return _obj;
	}
	
	private SpecialOffers prepareOffers(String str)
	{
		SpecialOffers _obj = null;
		
		int count	   = 0;
		if(str!=null)
		{
			_obj  = new SpecialOffers();
			TimeStamp timeStamp 	= new TimeStamp();
			Condition condition		= new Condition();
			String[] token = str.split("\\"+spch);
			
//			if(token.length == 16)
//			{
				for (int i = 0; i < token.length; i++)  {
				     
				     switch(count){
				     case  0:
				    	 _obj.setUuid(token[i]);
				        break; //optional
				     case  1:
				    	 _obj.setOfferCode(token[i]);
				        break; //optional   
				     case  2:
				    	 _obj.setDescription(token[i]);
				        break; //optional
				     case  3:
				    	 _obj.setRateCodeId(token[i]);
				        break;
				     case  4:
				    	 _obj.setOfferType(token[i]);
				        break; 
				     case  5:
				    	 _obj.setOfferValue(convertStringToDouble(token[i]));
				        break;
				     case  6:
				    	 condition.setConditionType(token[i]);
				        break;
				     case  7:
				    	 condition.setConditionValue(token[i]);
				        break;
				     case  8:
				    	 _obj.setStatus(token[i]);
				        break;    
				     case  9:
				    	 timeStamp.setCreatedByTimeStamp(token[i]);
				        break;
				     case  10:
				    	 timeStamp.setCreatedById(token[i]);
				        break;
				     case  11:
				    	 timeStamp.setCreatedByName(token[i]);
				        break;
				     case  12:
				    	 timeStamp.setUpdatedByTimeStamp(token[i]);
				        break;
				     case  13:
				    	 timeStamp.setUpdatedById(token[i]);
				        break;   
				     case  14:
				    	 timeStamp.setUpdatedByName(token[i]);
				        break; 
				     case  15:
				    	 _obj.setVisible(token[i]);
				        break;    
				     //You can have any number of case statements.
				     default : //Optional
				    	 System.out.println("Invalid grade " + count);
				 }
				     
				     ++count;
				}
			
			//}
			 _obj.setTimeStamp(timeStamp);
		}
		
		
		return _obj;
	}
	
	private DiscountConditions prepareDiscountConditions(String str)
	{
		DiscountConditions _obj = null;
		
		int count	   = 0;
		if(str!=null)
		{
			_obj  = new DiscountConditions();
			TimeStamp timeStamp 	= new TimeStamp();
			Condition condition1	= new Condition();
			Condition condition2	= new Condition();
			Condition condition3	= new Condition();
			Condition condition4	= new Condition();
			Condition condition5	= new Condition();
			
			String[] token = str.split("\\"+spch);
			
//			if(token.length == 20)
//			{
				for (int i = 0; i < token.length; i++)  {
				     
				     switch(count){
				     case  0:
				    	 _obj.setUuid(token[i]);
				        break; //optional
				     case  1:
				    	 _obj.setDiscountId(token[i]);
				        break; //optional   
				     case  2:
				    	 condition1.setConditionValue(token[i]);
				        break; //optional
				     case  3:
				    	 condition2.setConditionValue(token[i]);
				        break;
				     case  4:
				    	 condition3.setConditionValue(token[i]);
				        break; 
				     case  5:
				    	 condition4.setConditionValue(token[i]);
				        break;
				     case  6:
				    	 condition5.setConditionValue(token[i]);
				        break;
				     case  7:
				    	 condition1.setCondition(token[i]);
				        break;
				     case  8:
				    	 condition2.setCondition(token[i]);
				        break;    
				     case  9:
				    	 condition3.setCondition(token[i]);
				        break;
				     case  10:
				    	 condition4.setCondition(token[i]);
				        break;
				     case  11:
				    	 condition5.setCondition(token[i]);
				        break;
				     case  12:
				    	 timeStamp.setCreatedByName(token[i]);
				        break;
				     case  13:
				    	 timeStamp.setUpdatedByTimeStamp(token[i]);
				        break;   
				     case  14:
				    	 timeStamp.setUpdatedById(token[i]);
				        break; 
				     case  15:
				    	 timeStamp.setUpdatedByName(token[i]);
				        break;
				     case  16:
				    	 _obj.setVisible(token[i]);
				        break;   
				     case  17:
				    	 _obj.setStatus(token[i]);
				        break;
				     case  18:
				    	 timeStamp.setCreatedById(token[i]);
				        break;
				     case  19:
				    	 timeStamp.setCreatedByTimeStamp(token[i]);
				        break; 
				     case  20:
				    	 _obj.setMealBasis(token[i]);
				        break;
				     case  21:
				    	 _obj.setRoomOccupancyType(token[i]);
				        break;
				     //You can have any number of case statements.
				     default : //Optional
				    	 System.out.println("Invalid grade " + count);
				 }
				     
				     ++count;
				}
			
			//}
			 _obj.setTimeStamp(timeStamp);
			 _obj.setCondition1(condition1);
			 _obj.setCondition2(condition2);
			 _obj.setCondition3(condition3);
			 _obj.setCondition4(condition4);
			 _obj.setCondition5(condition5);
		}
		
		
		return _obj;
	}
	
	private GuestPreference prepareGuestPreference(String str)
	{
		GuestPreference _obj = null;
		
		int count	   = 0;
		if(str!=null)
		{
			_obj  = new GuestPreference();
			TimeStamp timeStamp 	= new TimeStamp();
			
			String[] token = str.split("\\"+spch);
			
//			if(token.length == 13)
//			{
				for (int i = 0; i < token.length; i++)  {
				     
				     switch(count){
				     case  0:
				    	 _obj.setUuid(token[i]);
				        break; //optional
				     case  1:
				    	 _obj.setGuestId(token[i]);
				        break; //optional   
				     case  2:
				    	 _obj.setPreferenceType(token[i]);
				        break; //optional
				     case  3:
				    	 _obj.setPreference(token[i]);
				        break;
				     case  4:
				    	 _obj.setDescription(token[i]);
				        break; 
				     case  5:
				    	 _obj.setStatus(token[i]);
				        break;    
				     case  6:
				    	 timeStamp.setCreatedByTimeStamp(token[i]);
				        break;
				     case  7:
				    	 timeStamp.setCreatedById(token[i]);
				        break;
				     case  8:
				    	 timeStamp.setCreatedByName(token[i]);
				        break;
				     case  9:
				    	 timeStamp.setUpdatedByTimeStamp(token[i]);
				        break;
				     case  10:
				    	 timeStamp.setUpdatedById(token[i]);
				        break;   
				     case  11:
				    	 timeStamp.setUpdatedByName(token[i]);
				        break; 
				     case  12:
				    	 _obj.setPropertyId(token[i]);
				        break;    
				     //You can have any number of case statements.
				     default : //Optional
				    	 System.out.println("Invalid grade " + count);
				 }
				     
				     ++count;
				}
			
			//}
			 _obj.setTimeStamp(timeStamp);
		}
		
		
		return _obj;
	}
	
	private DiscountsAndSupplementMapping prepareDiscountMapping(String str)
	{
		DiscountsAndSupplementMapping _obj = null;
		
		int count	   = 0;
		if(str!=null)
		{
			_obj  = new DiscountsAndSupplementMapping();
			TimeStamp timeStamp 	= new TimeStamp();
			
			String[] token = str.split("\\"+spch);
			
//			if(token.length == 18)
//			{
				for (int i = 0; i < token.length; i++)  {
				     
				     switch(count){
				     case  0:
				    	 _obj.setUuid(token[i]);
				        break; //optional
				     case  1:
				    	 _obj.setDiscountId(token[i]);
				        break; //optional   
				     case  2:
				    	 _obj.setSeasonUuid(token[i]);
				        break; //optional
				     case  3:
				    	 _obj.setFromTime(token[i]);
				        break;
				     case  4:
				    	 _obj.setToTime(token[i]);
				        break; 
				     case  5:
				    	 _obj.setTimeType(token[i]);
				        break; 
				     case  6:
				    	 _obj.setConcernedParty(token[i]);
				        break;
				     case  7:
				    	 _obj.setConcernedPartyType(token[i]);
				        break;
				     case  8:
				    	 _obj.setConcern(token[i]);
				        break; 
				     case  9:
				    	 _obj.setReadyToUse(token[i]);
				        break; 
				     case  10:
				    	 _obj.setStatus(token[i]);
				        break;   
				     case  11:
				    	 timeStamp.setCreatedByTimeStamp(token[i]);
				        break;
				     case  12:
				    	 timeStamp.setCreatedById(token[i]);
				        break;
				     case  13:
				    	 timeStamp.setCreatedByName(token[i]);
				        break;
				     case  14:
				    	 timeStamp.setUpdatedByTimeStamp(token[i]);
				        break;
				     case  15:
				    	 timeStamp.setUpdatedById(token[i]);
				        break;   
				     case  16:
				    	 timeStamp.setUpdatedByName(token[i]);
				        break; 
				     case  17:
				    	 _obj.setConcernedPartyName(token[i]);
				        break;    
				     //You can have any number of case statements.
				     default : //Optional
				    	 System.out.println("Invalid grade " + count);
				 }
				     
				     ++count;
				}
			
			//}
			 _obj.setTimeStamp(timeStamp);
		}
		
		
		return _obj;
	}

	
	private Discounts prepareDiscount(String str)
	{
		Discounts _obj = null;
		
		int count	   = 0;
		if(str!=null)
		{
			_obj  = new Discounts();
			TimeStamp timeStamp 	= new TimeStamp();
			
			String[] token = str.split("\\"+spch);
			
//			if(token.length == 23)
//			{
				for (int i = 0; i < token.length; i++)  {
				     
				     switch(count){
				     case  0:
				    	 _obj.setUuid(token[i]);
				        break; //optional
				     case  1:
				    	 _obj.setDiscountCode(token[i]);
				        break; //optional   
				     case  2:
				    	 _obj.setDescription(token[i]);
				        break; //optional
				     case  3:
				    	 _obj.setAmountType(token[i]);
				        break;  
				     case  4:
				    	 _obj.setAmount(convertStringToDouble(token[i]));
				        break;
				     case  5:
				    	 _obj.setPropertyId(token[i]);
				        break; 
				     case  6:
				    	 _obj.setConditionType(token[i]);
				        break; 
				     case  7:
				    	 _obj.setStatus(token[i]);
				        break;
				     case  8:
				    	 timeStamp.setCreatedByTimeStamp(token[i]);
				        break;
				     case  9:
				    	 timeStamp.setCreatedById(token[i]);
				        break;
				     case  10:
				    	 timeStamp.setCreatedByName(token[i]);
				        break;
				     case  11:
				    	 timeStamp.setUpdatedByTimeStamp(token[i]);
				        break;
				     case  12:
				    	 timeStamp.setUpdatedById(token[i]);
				        break;   
				     case  13:
				    	 timeStamp.setUpdatedByName(token[i]);
				        break; 
				     case  14:
				    	 _obj.setVisible(token[i]);
				        break;
				     case  15:
				    	 _obj.setDsType(token[i]);
				        break;
				     case  16:
				    	 _obj.setRateCodeId(token[i]);
				        break;
				     case  17:
				    	 _obj.setDiscountApplyOrder(Integer.parseInt(token[i]));
				        break;
				     case  18:
				    	 _obj.setMappingCombinationTag(token[i]);
				        break;
				     case  19:
				    	 _obj.setDaysApplyType(token[i]);
				        break;
				     case  20:
				    	 _obj.setChoosable(Boolean.parseBoolean(token[i]));
				        break;
				     case  21:
				    	 _obj.setApplicableStatus(Boolean.parseBoolean(token[i]));
				        break;
				     case  22:
				    	 _obj.setToBeAppliedOn(token[i]);
				        break;
				     case  23:
				    	 _obj.setCurrencyId(token[i]);
				        break;
				     case  24:
				    	 _obj.setToBeApplyAfterCreatedDate(Boolean.parseBoolean(token[i]));
				        break;
				     case  25:
				    	 _obj.setConfirmToVisible(Boolean.parseBoolean(token[i]));
				        break;
				     //You can have any number of case statements.
				     default : //Optional
				    	 System.out.println("Invalid grade " + count);
				 }
				     
				     ++count;
//				}
			
			}
			 _obj.setTimeStamp(timeStamp);
			 _obj.setDiscountConditions(null);
			 ArrayList<DiscountsAndSupplementMapping> list = new  ArrayList<DiscountsAndSupplementMapping>();
			 _obj.setDiscountSupplementMappingList(list);
		}
		
		
		return _obj;
	}
	
	private DiscountApprovalRequest prepareDiscountApprovalRequest(String str)
	{
		DiscountApprovalRequest _obj = null;
		
		int count	   = 0;
		if(str!=null)
		{
			_obj  = new DiscountApprovalRequest();
			TimeStamp timeStamp 	= new TimeStamp();
			
			String[] token = str.split("\\"+spch);
			
//			if(token.length == 23)
//			{
				for (int i = 0; i < token.length; i++)  {
				     
				     switch(count){
				     case  0:
				    	 _obj.setUuid(token[i]);
				        break; //optional
				     case  1:
				    	 _obj.setReservationId(token[i]);
				        break; //optional   
				     case  2:
				    	 _obj.setDiscountCode(token[i]);
				        break; //optional
				     case  3:
				    	 _obj.setRemarks(token[i]);
				        break;  
				     case  4:
				    	 _obj.setPropertyId(token[i]);
				        break;
				     case  5:
				    	 _obj.setConfirmedById(token[i]);
				        break; 
				     case  6:
				    	 _obj.setSeasonId(token[i]);
				        break; 
				     case  7:
				    	 _obj.setAgentId(token[i]);
				        break;
				     case  8:
				    	 _obj.setAgentMarketId(token[i]);
				        break;
				     case  9:
				    	 _obj.setCheckInDate(token[i]);
				        break;
				     case  10:
				    	 _obj.setCheckOutDate(token[i]);
				        break;
				     case  11:
				    	 _obj.setPromotionCode(token[i]);
				        break;
				     case  12:
				    	 _obj.setDsType(token[i]);
				        break;
				     case  13:
				    	 _obj.setOccupancyType(token[i]);
				        break;
				     case  14:
				    	 _obj.setSystemRate(token[i]);
				        break;
				     case  15:
				    	 _obj.setSpecialRate(token[i]);
				        break;
				     case  16:
				    	 _obj.setChangeAmount(token[i]);
				        break;
				     case  17:
				    	 timeStamp.setCreatedByTimeStamp(token[i]);
				        break;
				     case  18:
				    	 timeStamp.setCreatedById(token[i]);
				        break;
				     case  19:
				    	 timeStamp.setCreatedByName(token[i]);
				        break;
				     case  20:
				    	 timeStamp.setUpdatedByTimeStamp(token[i]);
				        break;
				     case  21:
				    	 timeStamp.setUpdatedById(token[i]);
				        break;   
				     case  22:
				    	 timeStamp.setUpdatedByName(token[i]);
				        break; 
				     case  23:
				    	 _obj.setStatus(token[i]);
				        break;
				     case  24:
				    	 _obj.setApprovalStatus(token[i]);
				        break;
				     case  25:
				    	 _obj.setCurrencyId(token[i]);
				        break;
				     case  26:
				    	 _obj.setUserEmail(token[i]);
				        break;
				     //You can have any number of case statements.
				     default : //Optional
				    	 System.out.println("Invalid grade " + count);
				 }
				     
				     ++count;
//				}
			
			}
			 _obj.setTimeStamp(timeStamp);
		}
		
		
		return _obj;
	}
	
	private ConfirmationVoucherDetails prepareConfirmationVoucherDetails(String str)
	{
		ConfirmationVoucherDetails _obj = null;
		
		int count	   = 0;
		if(str!=null)
		{
			_obj  = new ConfirmationVoucherDetails();
			TimeStamp timeStamp 	= new TimeStamp();
			
			String[] token = str.split("\\"+spch);
			
//			if(token.length == 23)
//			{
				for (int i = 0; i < token.length; i++)  {
				     
				     switch(count){
				     case  0:
				    	 _obj.setUuid(token[i]);
				        break; //optional
				     case  1:
				    	 _obj.setPropertyId(token[i]);
				        break; //optional   
				     case  2:
				    	 _obj.setDescription(token[i]);
				        break; //optional
				     case  3:
				    	 _obj.setItemType(token[i]);
				        break;  
				     case  4:
				    	 _obj.setDefaultEnabled(Boolean.parseBoolean(token[i]));
				        break;
				     case  5:
				    	 timeStamp.setCreatedByTimeStamp(token[i]);
				        break;
				     case  6:
				    	 timeStamp.setCreatedById(token[i]);
				        break;
				     case  7:
				    	 timeStamp.setCreatedByName(token[i]);
				        break;
				     case  8:
				    	 timeStamp.setUpdatedByTimeStamp(token[i]);
				        break;
				     case  9:
				    	 timeStamp.setUpdatedById(token[i]);
				        break;   
				     case  10:
				    	 timeStamp.setUpdatedByName(token[i]);
				        break; 
				     case  11:
				    	 _obj.setStatus(token[i]);
				        break;
				     case  12:
				    	 _obj.setItemOrder(Integer.parseInt(token[i]));
				        break;
				     //You can have any number of case statements.
				     default : //Optional
				    	 System.out.println("Invalid grade " + count);
				 }
				     
				     ++count;
//				}
			
			}
			 _obj.setTimeStamp(timeStamp);
		}
		
		
		return _obj;
	}
	
	private User prepareUser(String str)
	{
		User _obj = null;
		
		int count	   = 0;
		if(str!=null)
		{
			_obj  = new User();
			
			String[] token = str.split("\\"+spch);
			Country 	country 	= new Country();;
			Department 	department 	= new Department();
			Property	market 		= new Property();
			Manager		supervisor 	= new Manager();
			
//			if(token.length == 23)
//			{
				for (int i = 0; i < token.length; i++)  {
				     
				     switch(count){
				     case  0:
				    	 _obj.setUuid(token[i]);
				        break; //optional
				     case  1:
				    	 _obj.setFirstName(token[i]);
				        break; //optional   
				     case  2:
				    	 _obj.setLastName(token[i]);
				        break; //optional
				     case  3:
				    	 _obj.setAge(Integer.parseInt(token[i]));
				        break;  
				     case  4:
				    	 _obj.setSex(token[i]);
				        break;
				     case  5:
				    	 department.setUuid(token[i]);
				        break; 
				     case  6:
				    	 country.setUuid(token[i]);
				        break; 
				     case  7:
				    	 _obj.setDesignation(token[i]);
				        break;
				     case  8:
				    	 _obj.setDOB(token[i]);
				        break;
				     case  9:
				    	 _obj.setUserId(token[i]);
				        break;
				     case  10:
				    	 market.setUuid(token[i]);
				        break;
				     case  11:
				    	 supervisor.setUuid(token[i]);
				        break;
				     case  12:
				    	 _obj.setStatus(Integer.parseInt(token[i]));
				        break;   
				     case  13:
				    	 _obj.setPassword(token[i]);
				        break; 
				     case  14:
				    	 _obj.setUserName(token[i]);
				        break;
				     case  15:
				    	 _obj.setSuperUser(Integer.parseInt(token[i]));
				        break;
				     case  16:
				    	 _obj.setActingManager(Integer.parseInt(token[i]));
				        break;
				     case  17:
				    	 _obj.setEmail(token[i]);
				        break;
				     case  18:
				    	 _obj.setMobile(token[i]);
				        break;
				     case  19:
				    	 _obj.setPhoneExtension(token[i]);
				        break;
				     case  20:
				    	 _obj.setResolutionId(token[i]);
				        break;
				     //You can have any number of case statements.
				     default : //Optional
				    	 System.out.println("Invalid grade " + count);
				 }
				     
				     ++count;
//				}
			
			}
				
			_obj.setSupervisor(supervisor);	
			_obj.setCountry(country);
			_obj.setDepartment(department);
			_obj.setMarket(market);
		}
		
		return _obj;
	}
	
	private AccessGroupMaster prepareAccessGroupMaster(String str)
	{
		AccessGroupMaster _obj = null;
		
		int count	   = 0;
		if(str!=null)
		{
			_obj  = new AccessGroupMaster();
			
			String[] token = str.split("\\"+spch);
			
//			if(token.length == 11)
//			{
				for (int i = 0; i < token.length; i++)  {
				     
				     switch(count){
				     case  0:
				    	 _obj.setUuid(token[i]);
				        break; //optional
				     case  1:
				    	 _obj.setGroupName(token[i]);
				        break; //optional   
				     case  2:
				    	 _obj.setStatus(Integer.parseInt(token[i]));
				        break; //optional
				     //You can have any number of case statements.
				     default : //Optional
				    	 System.out.println("Invalid grade " + count);
				 }
				     
				     ++count;
				}
			
			//}
		}
		
		
		return _obj;
	}
	
	private AccessGroupDetails prepareAccessGroupDetails(String str)
	{
		AccessGroupDetails _obj = null;
		
		int count	   = 0;
		if(str!=null)
		{
			_obj  = new AccessGroupDetails();
			
			String[] token = str.split("\\"+spch);
			RoleMaster roleMaster = new RoleMaster();
//			if(token.length == 11)
//			{
				for (int i = 0; i < token.length; i++)  {
				     
				     switch(count){
				     case  0:
				    	 _obj.setUuid(token[i]);
				        break; //optional
				     case  1:
				    	 _obj.setAccessGroupMasterId(token[i]);
				        break; //optional   
				     case  2:
				    	 roleMaster.setUuid(token[i]);
				        break; //optional
				     case  3:
				    	 _obj.setStatus(Integer.parseInt(token[i]));
				        break; //optional   
				     //You can have any number of case statements.
				     default : //Optional
				    	 System.out.println("Invalid grade " + count);
				 }
				     
				     ++count;
				}
			
			//}
			_obj.setRoleMasterObj(roleMaster);
		}
		
		
		return _obj;
	}
	
	private AccessDetailsOfGroups prepareAccessDetailsOfGroups(String str)
	{
		AccessDetailsOfGroups _obj = null;
		
		int count	   = 0;
		if(str!=null)
		{
			_obj  = new AccessDetailsOfGroups();
			
			String[] token = str.split("\\"+spch);
			AccessGroupMaster access 	= new AccessGroupMaster();
			User user					= new User();
//			if(token.length == 11)
//			{
				for (int i = 0; i < token.length; i++)  {
				     
				     switch(count){
				     case  0:
				    	 _obj.setUuid(token[i]);
				        break; //optional
				     case  1:
				    	 access.setUuid(token[i]);
				        break; //optional   
				     case  2:
				    	 user.setUuid(token[i]);
				        break; //optional
				     case  3:
				    	 _obj.setStatus(Integer.parseInt(token[i]));
				        break; //optional   
				     //You can have any number of case statements.
				     default : //Optional
				    	 System.out.println("Invalid grade " + count);
				 }
				     
				     ++count;
				}
			
			//}
			_obj.setAccessGroupMasterObj(access);
			_obj.setUserObj(user);
		}
		
		
		return _obj;
	}

}
