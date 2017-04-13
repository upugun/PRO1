package net.ds.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import net.ds.value.object.DesOccupancy;
import net.jwt.constants.JWTConstants;
import net.jwt.dao.DAO;
import net.jwt.interfaces.IDAO;
import net.jwt.value.objects.QuerryObject;
import net.jwt.value.objects.User;

public class DesOccupancyDAO extends  DAO implements IDAO{
	
	private static String TABLE_NAME 			= JWTConstants.DB_NAME + ".des_occupancy";
	
	public DesOccupancyDAO(){
	} 
	
	public ArrayList<DesOccupancy> fetch(String uuid){
		ArrayList<DesOccupancy> _list= new ArrayList<DesOccupancy>();
  		DesOccupancy _obj = new DesOccupancy();
  		_obj.setUuid(uuid);
  		_list.add(_obj);
  		
  		return fetch(_list);
	}
	
	public void setLogedInUser(User logedInUser) {
		super.logedInUser = logedInUser;
	}
	

/**
 * 
 * @return
 */
	public ArrayList<DesOccupancy> fetch(ArrayList<DesOccupancy> list){
		ArrayList<DesOccupancy> _returnList = new ArrayList<DesOccupancy>();
		String sql;
		
		java.sql.Statement stmt = null;
		ResultSet rs = null;
		
		if(list.size() > 0)
			sql = prepareSQLQuerry("uuid", list.get(0).getUuid().toString());
		else
			sql = prepareSQLQuerry("deafult", "");
		
		try{
			  stmt	=	getNewStatment();
			  rs 	= 	stmt.executeQuery(sql);
		      while(rs.next())
		      {
		    	 _returnList.add(objectMapping(rs));
		      }
		}catch(SQLException se){
			logError(se.toString(), this.getClass().toString());
		}	
		finally{ closeAllConnections(stmt, rs); }		
		return _returnList;
	}

	public ArrayList<DesOccupancy> dynamicSearch(String search, String condition){
		consoleLogMessage("Dynamic Search ",TABLE_NAME , logedInUser);
		return startDynamicSearch( search, condition);
	}
	
	public ArrayList<DesOccupancy> startDynamicSearch(String search, String condition){
		ArrayList<DesOccupancy> _returnList = new ArrayList<DesOccupancy>();
		String sql;
		
		sql = prepareSQLQuerry(search, condition);
		java.sql.Statement stmt = null;
		ResultSet rs = null;
		try{
			      stmt	=	getNewStatment();
				  rs = stmt.executeQuery(sql);
			      while(rs.next())
			      {
			    	 _returnList.add(objectMapping(rs));
			      }
		}catch(SQLException se){
			logError(se.toString(), this.getClass().toString());
		}		
		
		finally{ closeAllConnections(stmt, rs); }
		return _returnList;
	}
	
	private String prepareSQLQuerry(String qstr, String condition){
		String sqlStr;

		if(qstr.equals("uuid"))
			sqlStr = "SELECT * FROM " + TABLE_NAME + " WHERE `uuid` = '"+ condition + "'";
		else if(qstr.equals("dynamicCountry"))
			sqlStr = "SELECT * FROM " + TABLE_NAME + " WHERE `status` = '"+JWTConstants.STATUS_LABLE_ACTIVE+"' AND `RoomCategory` LIKE '"+ condition + "%'";
		else
			sqlStr = "SELECT * FROM " + TABLE_NAME + " WHERE `status` = '"+JWTConstants.STATUS_LABLE_ACTIVE+"'";
		
		//sqlStr = sqlStr + " order by createdDateTime desc";
		//System.out.println("MarketingPropertyDAO.prepareSQLQuerry() " + sqlStr);
		return sqlStr;
	}
	
	public ArrayList<DesOccupancy> dynamicSearch(QuerryObject q){
		consoleLogMessage("Dynamic Search ",TABLE_NAME , logedInUser);
		return startDynamicSearch(q);
	}
	
	public ArrayList<DesOccupancy> startDynamicSearch(QuerryObject q){
		ArrayList<DesOccupancy> _returnList = new ArrayList<DesOccupancy>();
		String sql;
		
		sql = prepareSQLQuerry(q);
		java.sql.Statement stmt = null;
		ResultSet rs = null;
		try{
			      stmt	=	getNewStatment();
				  rs = stmt.executeQuery(sql);
			      while(rs.next())
			      {
			    	 _returnList.add(objectMapping(rs));
			      }
		}catch(SQLException se){
			logError(se.toString(), this.getClass().toString());
		}		
		
		finally{ closeAllConnections(stmt, rs); }
		return _returnList;
	}
	
	private String prepareSQLQuerry(QuerryObject q){
		
		String qstr			=	q.getSearchCode(); 
		String condition	=	q.getCondition1();
//		String condition2	=	q.getCondition2();
//		String condition3	=	q.getCondition3();
		String sqlStr;
		if(qstr.equals("uuid"))
			sqlStr = "SELECT * FROM " + TABLE_NAME + " WHERE `uuid` = '"+ condition + "'";
		else if(qstr.equals("dynamicCountry"))
			sqlStr = "SELECT * FROM " + TABLE_NAME + " WHERE `status` = '"+JWTConstants.STATUS_LABLE_ACTIVE+"' AND `RoomCategory` LIKE '"+ condition + "%'";
		else
			sqlStr = "SELECT * FROM " + TABLE_NAME + " WHERE `status` = '"+JWTConstants.STATUS_LABLE_ACTIVE+"'";
		
		//System.out.println("MarketingPropertyDAO.prepareSQLQuerry() " + sqlStr);
		return sqlStr;
	}
	/**
	 * 
	 * @param rs
	 * @return
	 */
	private DesOccupancy objectMapping(ResultSet rs){
		  DesOccupancy _obj = new DesOccupancy();
		  try{
			  _obj.setUuid(rs.getString(1));
			  _obj.setPropertyId(rs.getString(2));
			  _obj.setRoomCategoryId(rs.getString(3));
			  _obj.setRoomCategoryName(rs.getString(4));
			  _obj.setTotalRooms(rs.getInt(5));
			  _obj.setOccupiedRooms(rs.getInt(6));
			  _obj.setAvailableRooms(rs.getInt(7));
			  _obj.getTimeStamp().setCreatedById(rs.getString(8));
			  _obj.getTimeStamp().setCreatedByName(rs.getString(9));
			  _obj.getTimeStamp().setUpdatedByTimeStamp(rs.getString(10));
			  _obj.setOccupancyPercentage(rs.getDouble(11));
			  _obj.setOccupancyDate(rs.getString(12));
			  
		  	}catch (Exception e) {
			logError(e.toString(), this.getClass().toString());
		  }
		return _obj;
	}
	
	/**
	 * 
	 * @param list
	 */
	public void create(ArrayList<DesOccupancy> list) throws Exception
	{
		insert(list);
	}
	
	private boolean insert(ArrayList<DesOccupancy> list)throws Exception{
		
		int _count = 0;
		String _sql;
		
		PreparedStatement stmt = null;
		Connection conn = getDbConnection();
		
		try{
			while((list.size() - _count) > 0){
				
				DesOccupancy _obj = list.get(_count);
				String uid = java.util.UUID.randomUUID().toString();
				_obj.setUuid(uid);
				_obj.getTimeStamp().setCreatedByTimeStamp(getCurrentDate());
				_obj.getTimeStamp().setCreatedById(getLogedInUser().getUuid());
				_obj.getTimeStamp().setCreatedByName(getLogedInUser().getUserName());
				
				_sql =  "INSERT INTO " + TABLE_NAME + " (`uuid`,  `propertyId`,  `roomCategoryId`,  `roomCategoryName`,  `totalRooms`,  `occupiedRooms`,  `availableRooms`,  `createdById`,  `createdByUser`,  `occupancyPercentage`,  `occupancyDate`)"+
								"VALUES (?,?,?,?,?,?,?,?,?,?,?)";
				
				
				stmt = conn.prepareStatement(_sql);
				stmt.setString(1, _obj.getUuid());
				stmt.setString(2, _obj.getPropertyId());
				stmt.setString(3, _obj.getRoomCategoryId());
				stmt.setString(4, _obj.getRoomCategoryName());
				stmt.setInt(5, _obj.getTotalRooms());
				stmt.setInt(6, _obj.getOccupiedRooms());
				stmt.setInt(7, _obj.getAvailableRooms());
				stmt.setString(8, _obj.getTimeStamp().getCreatedById());
				stmt.setString(9, _obj.getTimeStamp().getCreatedByName());
				stmt.setDouble(10, _obj.getOccupancyPercentage());
				stmt.setString(11, _obj.getOccupancyDate());
				
				stmt.executeUpdate();
				
				_count ++;
			}
			
			conn.commit();
			
		}catch (Exception e) {
			logError(e.toString(), this.getClass().toString());
			throw new SQLException(e.getMessage().toString());
		}
		finally{ closeAllConnections(stmt); }		
		return setReturn(_count);
	}
	
	
	
	
	/**
	 * 
	 * @param list
	 * @return
	 */
	
	
	/**
	 * 
	 * @param list
	 * @param status
	 * @return
	 */
	
	
	
	/**
	 * 
	 * @param list
	 * @return
	 */
	
	
	private boolean setReturn(int c){
		if(c > 0)
			return true;
		else 
			return false;
	}
}
