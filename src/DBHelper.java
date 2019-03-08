

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBHelper {
	
	private Connection connection = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	private String driver;
	private String url;
	private String username;
	private String password;
	
	public DBHelper() {

		driver = "org.postgresql.Driver";
		url = "jdbc:postgresql://localhost:5432/ChatRoom";
		username = "postgres";
		password = "9369";

		try {

			Class.forName(driver);
			connection = DriverManager.getConnection(url,username,password);
		} catch (ClassNotFoundException e) {
			System.out.println("JDBC driver not found");
			e.printStackTrace();
		} catch (SQLException e){
			System.out.println("Connection failure");
			e.printStackTrace();
		}
	}
	

		public void close()
		{
			try {
				if(resultSet !=null) resultSet.close();
				if(preparedStatement !=null) preparedStatement.close();
				if(connection !=null) connection.close();
			} catch (Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
		

		public boolean exeUpdate(String sql,String []paras)
		{
			boolean b = true;
			try {
				preparedStatement = connection.prepareStatement(sql);
				for(int i = 0;i<paras.length;i++)
				{
					preparedStatement.setString(i+1, paras[i]);
				}
				preparedStatement.executeUpdate();
			} catch (Exception e) {
				System.out.println(e.getMessage());
				b = false;
				e.printStackTrace();
			}
			
			return b;
		}


		public ResultSet query(String sql,String []paras)
		{
			try {
				preparedStatement = connection.prepareStatement(sql);
				for(int i = 0;i<paras.length;i++)
				{
					preparedStatement.setString(i+1, paras[i]);
				}
				resultSet = preparedStatement.executeQuery();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return resultSet;
		}
		
		public boolean checkUser(String username,String password)
		{
			boolean b = false;
			PreparedStatement psmt = null;
			try{

				String sql = "select username,password from public.\"accountInfo\" where username=? and password=?";
				psmt = connection.prepareStatement(sql);
				psmt.setString(1, username);
				psmt.setString(2, password);
				ResultSet rs = psmt.executeQuery();
				if(rs.next())
				{
					b=true;
				}
				rs.close();
	            psmt.close();
			}
			catch(SQLException se){

	            se.printStackTrace();
	        }catch(Exception e){

	            e.printStackTrace();
	        }finally{

	            try{
	                if(psmt!=null) psmt.close();
	            }catch(SQLException se2){
	            }
			
		}
			return b;
		}
		
		public void insertUser(String username,String password) {
			
			
			PreparedStatement psmt = null;
			try{
				

				String sql = "insert into public.\"accountInfo\" (username,password) values(?, ?)";
				psmt = connection.prepareStatement(sql);
				psmt.setString(1, username);
				psmt.setString(2, password);
				psmt.execute();
				
			}
			catch(SQLException se){

	            se.printStackTrace();
	        }catch(Exception e){

	            e.printStackTrace();
	        }finally{

	            try{
	                if(psmt!=null) psmt.close();
	            }catch(SQLException se2){
	            }
			
		}
		}
	
	

}
