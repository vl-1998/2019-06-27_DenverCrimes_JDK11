package it.polito.tdp.crimes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.crimes.model.Adiacenza;
import it.polito.tdp.crimes.model.Event;


public class EventsDAO {
	
	public List<Event> listAllEvents(){
		String sql = "SELECT * FROM events" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Event> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<String> getCategory(){
		String sql = "select distinct offense_category_id \n" + 
				"from events\n" + 
				"order by offense_category_id ASC";
		List<String> result = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
						
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				result.add(res.getString("offense_category_id"));
			}
			conn.close();
			return result;
			
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Integer> getYear(){
		String sql = "select distinct YEAR(reported_date)\n" + 
				"from events\n" + 
				"order by YEAR(reported_date) ASC";
		List<Integer> result = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
						
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				result.add(res.getInt("YEAR(reported_date)"));
			}
			conn.close();
			return result;
			
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<String> getVertex (String categoria, Integer anno){
		String sql = "select distinct offense_type_id\n" + 
				"from events\n" + 
				"where offense_category_id = ?\n" + 
				"and YEAR(reported_date) = ?\n" + 
				"order by offense_type_id ASC";
		List<String> result = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setString(1, categoria);
			st.setInt(2, anno);
						
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				result.add(res.getString("offense_type_id"));
			}
			conn.close();
			return result;
			
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Adiacenza> getEdge(String categoria, Integer anno){
		String sql = "select e1.offense_type_id, e2.offense_type_id, count(distinct e1.district_id) as peso\n" + 
				"from events e1, events e2\n" + 
				"where e1.offense_type_id<>e2.offense_type_id\n" + 
				"and e1.offense_type_id>e2.offense_type_id\n" + 
				"and e1.district_id = e2.district_id\n" + 
				"and e1.offense_category_id = e2.offense_category_id\n" + 
				"and e1.offense_category_id = ?\n" + 
				"and YEAR(e1.reported_date) = YEAR(e2.reported_date)\n" + 
				"and YEAR(e2.reported_date) = ?\n" + 
				"group by e1.offense_type_id, e2.offense_type_id";
		
		List<Adiacenza> result = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setString(1, categoria);
			st.setInt(2, anno);
						
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				Adiacenza a = new Adiacenza (res.getString("e1.offense_type_id"), 
						res.getString("e2.offense_type_id"), res.getInt("peso"));
				result.add(a);
			}
			conn.close();
			return result;
			
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
		
	}

}
