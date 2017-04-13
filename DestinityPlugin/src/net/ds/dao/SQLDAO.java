package net.ds.dao;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import net.ds.value.object.DesDBProperties;
import net.jhl.constants.JHConstants;
import net.jwt.value.objects.User;


public class SQLDAO {
	
	public  User logedInUser;
	
	
	
	public User getLogedInUser() {
		return logedInUser;
	}

	public void setLogedInUser(User logedInUser) {
		this.logedInUser = logedInUser;
	}
	
	public DesDBProperties getPropValues(String propertyCode) throws IOException {
		DesDBProperties property = new DesDBProperties();
		InputStream inputStream = null;
		
		try {
			Properties prop 		= new Properties();
			String propFileName 	= JHConstants.DESTINITY_PATH+propertyCode+"/"+JHConstants.PROPERTY_FILE_Destinity;
 
			inputStream = new FileInputStream(propFileName);
 
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}
 
 
			// get the property value and print it out
			
			property.setDbURI(prop.getProperty("db_uri"));
			property.setDbUser(prop.getProperty("db_userid"));
			property.setDbPassword(prop.getProperty("db_password"));
			
			//System.out.println(property.getDbURI());
 
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} finally {
			inputStream.close();
		}
		return property;
	}

	public Connection getConnection(String propertyCode)
	{
	   Connection 
	   conn = null;  
	   
	   DesDBProperties 
	   dbProp = null;
	   
	   try
	   {
		   dbProp = getPropValues(propertyCode); 
	   }
	   catch (Exception e) {
		// TODO: handle exception
	   }
	   String db_connect_string = dbProp.getDbURI(); 
	   String db_userid 		= dbProp.getDbUser();
	   String db_password 		= dbProp.getDbPassword();
     try 
	   {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        conn = DriverManager.getConnection(db_connect_string,
                 db_userid, db_password);
        conn.setAutoCommit(false);
	   } 
	   catch (Exception e) 
	   {
        e.printStackTrace();
	   }
     
     return conn;
	}
	
	public void rollBack(Connection conn) throws SQLException
	{
		try{
			conn.rollback();
			System.out.println("SQLDAO.rollBack() : "+"["+ ""+"]transaction failed! Rollback the transaction...");
		}
		catch (SQLException e) {
			throw new SQLException(e.getMessage().toString());
		}
	}
	
	public int calculateNumberOfDaysBetween(Date startDate, Date endDate) 
	 {
	    if (startDate.after(endDate)) {
	        throw new IllegalArgumentException("End date should be grater or equals to start date");
	    }

	    long startDateTime = startDate.getTime();
	    long endDateTime = endDate.getTime();
	    long milPerDay = 1000*60*60*24; 

	    int numOfDays = (int) ((endDateTime - startDateTime) / milPerDay); // calculate vacation duration in days

	    return ( numOfDays + 1); // add one day to include start date in interval
	 }
	 
	 public String addDaysToDate(Date inDate, int gap, String dateFormat)
	{
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		Calendar c = Calendar.getInstance();
		c.setTime(inDate); // Now use today date.
		c.add(Calendar.DATE, gap); // Adding 5 days
		String output = sdf.format(c.getTime());
		return output;
	}
	
	public boolean checkRowUpdate(int i){
		switch(i){
		case 0:
			return false;
		case 1:
			return true;
		}
		return false;
	}
	
	public double roundDecimal(double value)
	 {
		 try
		 {
			 double _formatedValue = 0.0;
		 	
	 		 _formatedValue = Math.round(value * 100.0) / 100.0;
	 		 return _formatedValue;
		 }catch (Exception e) {
			 return 0;
		}
	 	
	 }
	
	 public double convertIntToDouble(int value)
	 {
		 try
		 {
			 return (double)value;
		 }catch (Exception e) {
			 return 0.0;
		}
	 	
		 
	 }
	 
	 public double convertStringToDouble(String str)
	 {
		 try
		 {
			 if(str.length()>0)
			 {
				 return Double.parseDouble(str);
			 }
			 else
				 return 0;
		 }catch (Exception e) {
			 return 0;
		}			 
	 }
	 
	 public String convertDoubleToString(double value)
	 {
		 return String.valueOf(value);
	 }
	 
	 public void closeCallableStatement(CallableStatement cstmt){
		try
		{
			if(cstmt != null)
			{
				if(!cstmt.isClosed())	
					cstmt.close();
			}
						
		}catch (Exception e) 
		{
			System.out.println("ER-09:Statement close exception-110 " + e.getMessage().toString());
		}
	}
	 
	 public void closeResultSet(ResultSet  rs){
		try
		{
			if(rs != null)
				rs.close();
						
		}catch (Exception e) 
		{
			System.out.println("ER-09:Statement close exception-110 " + e.getMessage().toString());
		}
	}
	 
	 public void closeDBConnection(Connection conn){
			try
			{
				if(null != conn)
				{
					if(!conn.isClosed())
						conn.close();
					
				}
			}catch (Exception e) {
				System.out.println("ER-10:Connection close exception-112" + e.getMessage().toString());
				e.printStackTrace();
			}
		}
	 
	 public void closeAllConnections(ResultSet  rs, CallableStatement cstmt, Connection conn)
	 {
		 closeResultSet(rs);
		 closeCallableStatement(cstmt);
		 closeDBConnection(conn);
	 }
		
	 public Date formateDate(String inputDate) throws Exception{
		
		 DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
		// DateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy");
		 Date date = originalFormat.parse(inputDate);
		//String formattedDate = targetFormat.format(date);  // 20120821
		 
		 return date;
	}
	 
	 public String formateDateString(String inputDate){
		 String formattedDate = "";	
		 try
		 {
		 DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
		 DateFormat targetFormat = new SimpleDateFormat("yyyy-dd-MM");
		 Date date = originalFormat.parse(inputDate);
		 formattedDate = targetFormat.format(date);  // 20120821
		 }
		 catch (Exception e) {
			// TODO: handle exception
		}
		 
		 return formattedDate;
	}
	
	 public String convertDesDateFormat(String inputDate){
		 String formattedDate = "";	
		 try
		 {
		 DateFormat originalFormat = new SimpleDateFormat("yyyy-dd-MM");
		 DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
		 Date date = originalFormat.parse(inputDate);
		 formattedDate = targetFormat.format(date);  // 20120821
		 }
		 catch (Exception e) {
			// TODO: handle exception
		}
		 
		 return formattedDate;
	}
	 
	public Date stringToDate(String inputDate)
	{
		Date date = null;
		
		try
		{
			date = new SimpleDateFormat("yyyy-dd-MM").parse(inputDate);
		}
		catch(Exception e)
		{
			
		}
		return date;
	}
}
