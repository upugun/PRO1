package net.ds.bo;

import java.util.ArrayList;

import net.ds.dao.CreateAgentDAO;
import net.ds.dao.CreateGuestDAO;
import net.ds.dao.CreateReservationDAO;
import net.ds.dao.DestinityFetchDAO;
import net.ds.value.object.DesOccupancy;
import net.ds.value.object.DestinityAgent;
import net.ds.value.object.DestinityReservation;
import net.jhl.objects.Guest;
import net.jwt.value.objects.QuerryObject;

public class DestinityBO {

	public DestinityBO() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public boolean createAgent(ArrayList<DestinityAgent> list) throws Exception
	{
		CreateAgentDAO agentDAO = new CreateAgentDAO();
		boolean result = agentDAO.create(list);
		
		return result;
	}
	
	public boolean createGuest(ArrayList<Guest> list) throws Exception
	{
		CreateGuestDAO guestDAO = new CreateGuestDAO();
		boolean result = guestDAO.create(list);
		
		return result;
	}
	
	public boolean createReservation(ArrayList<DestinityReservation> list) throws Exception
	{
		CreateReservationDAO resDAO = new CreateReservationDAO();
		boolean result = resDAO.create(list);
		
		return result;
	}
	
	public ArrayList<DesOccupancy> getOccupancyDetails(QuerryObject q) throws Exception
	{
		DestinityFetchDAO fetchDAO = new DestinityFetchDAO();
		ArrayList<DesOccupancy> returnList = fetchDAO.getOccupancyDetails(q);
		
		return returnList;
	}


}
