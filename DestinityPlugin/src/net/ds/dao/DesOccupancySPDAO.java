package net.ds.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import net.ds.value.object.DesOccupancy;


public class DesOccupancySPDAO extends SQLDAO{

	public DesOccupancySPDAO() {
		
	}
	
	private String propertyCode;
	
	

	public String getPropertyCode() {
		return propertyCode;
	}

	public void setPropertyCode(String propertyCode) {
		this.propertyCode = propertyCode;
	}
	
	public static void main(String[] args) throws SQLException {
		
		DesOccupancySPDAO dao = new DesOccupancySPDAO();
		
		DesOccupancyDAO DesOccupancyDAo = new DesOccupancyDAO();
		
		ArrayList<DesOccupancy> DesOccupancyList = DesOccupancyDAo.dynamicSearch("uuid", "00001");
		
		boolean b = dao.create(DesOccupancyList);
		
		System.out.print(b);
	}
	
	public boolean create(ArrayList<DesOccupancy> list) throws SQLException
	{
	      Connection conn = getConnection(getPropertyCode());
	      CallableStatement cstmt = null;
	      
	      int _count 			= 0;
	      int squeryExecuted 	= 0;
	      
	      try
	      {
	    	  while((list.size() - _count) > 0)
	    	  {
	    		  DesOccupancy _obj = list.get(_count);
	    		  int x = 1;
		    	  cstmt = conn.prepareCall("{call dbo.JetX_RoomOccupancy(?)}");
		    	  cstmt.setString(x, _obj.getOccupancyDate());
		    	  
		    	  
		    	  squeryExecuted = cstmt.executeUpdate();
		    	  _count++;
	    	  }
	    	  
	    	  conn.commit();
	    	  
	      }
	      catch (SQLException e) 
	      {
	    	  rollBack(conn);
	      }
	      return checkRowUpdate(squeryExecuted);
	}
}
