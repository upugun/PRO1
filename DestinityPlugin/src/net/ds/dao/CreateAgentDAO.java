package net.ds.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import net.ds.value.object.DestinityAgent;


public class CreateAgentDAO extends SQLDAO{

	public CreateAgentDAO() {
		
	}
	
	private String propertyCode;
	
	

	public String getPropertyCode() {
		return propertyCode;
	}

	public void setPropertyCode(String propertyCode) {
		this.propertyCode = propertyCode;
	}
	
	public static void main(String[] args) throws Exception{
		
//		CreateAgentDAO dao = new CreateAgentDAO();
//		
//		AgentDetailsDAO guestDAo = new AgentDetailsDAO();
//		
//		ArrayList<AgentDetails> agentList = guestDAo.dynamicSearch("uuid", "679");
//		
//		boolean b = dao.create(agentList);
//		
//		System.out.print(b);
	}
	
	public boolean create(ArrayList<DestinityAgent> list) throws Exception
	{
	      Connection conn = getConnection(getPropertyCode());
	      CallableStatement cstmt = null;
	      
	      int _count = 0;
	      int squeryExecuted 	= 0;
	      try
	      {
	    	  while((list.size() - _count) > 0)
	    	  {
	    		  DestinityAgent _obj = list.get(_count);
	    		  int x = 1;
		    	  cstmt = conn.prepareCall("{call dbo.Jet_InsertAgents(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
		    	  cstmt.setString(x, _obj.getAgentCode()); //Null not allowed
		    	  cstmt.setString(++x, _obj.getAgentName());
		    	  cstmt.setString(++x, _obj.getAddress());
		    	  cstmt.setString(++x, _obj.getLandNo());
		    	  cstmt.setString(++x, _obj.getFaxNo());
		    	  cstmt.setString(++x, _obj.getEmail());
		    	  cstmt.setBigDecimal(++x, _obj.getCreditLimit()); //@CreditLimit --No match (money type)
		    	  cstmt.setInt(++x, _obj.getBackColor()); //@BackColor --No match-- Null not allowed (money type)
		    	  cstmt.setInt(++x, _obj.getCrbal()); //@Crbal --No match--//Null not allowed
		    	  cstmt.setString(++x, _obj.getContactperson()); //@ContactPerson --No match //Null not allowed
		    	  cstmt.setString(++x, _obj.getVATNo()); //@VATNo --No match
		    	  cstmt.setString(++x, ""); //@FreeDays--Null not allowed (bit type)
		    	  cstmt.setInt(++x, _obj.getCrPeriod()); //@crperiod -- Null not allowed
		    	  cstmt.setString(++x,_obj.getAgentType()); //@AgentType
		    	  cstmt.setString(++x, _obj.getUpload()); //@Upload (0)
		    	  cstmt.setString(++x, _obj.getDescription()); //@remarks
		    	  cstmt.setBoolean(++x, _obj.isOperatorType()); //@FIT (fit or agent) (bit type)
		    	  cstmt.setString(++x, _obj.getCountryId()); //@Country
		    	  cstmt.setString(++x, ""); //@AllCombine
		    	  cstmt.setString(++x, ""); //@Policy (cancellation policy code)
		    	  cstmt.setString(++x, ""); //@Promotion
		    	  cstmt.setString(++x, _obj.getCreateStatus()); //@id --Should be 'I'
		    	  
		    	  //System.out.print("Test " + x);
		    	  squeryExecuted = cstmt.executeUpdate();
		    	  _count++;
	    	  }
	    	  
	    	  conn.commit();
	    	  
	      }
	      catch (SQLException se) 
	      {
	    	se.printStackTrace();
	    	  throw new SQLException(se.getMessage().toString());
	      }catch (Exception e) 
	      {
	    	  rollBack(conn);
	    	  throw new Exception(e.getMessage().toString());
	      }
	      finally{
	    	  closeDBConnection(conn);
	      }
	      return checkRowUpdate(squeryExecuted);
	      
	}

}
