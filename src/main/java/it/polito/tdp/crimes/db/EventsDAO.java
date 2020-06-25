package it.polito.tdp.crimes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.crimes.model.Arco;
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
	
	public List<String> getOffense(){
		String sql = "select distinct offense_category_id\n" + 
				"from events";
		List<String> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection() ;
		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			ResultSet res = st.executeQuery() ;
			
			while (res.next()) {
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
	
	public List<Integer> getDay(){
		String sql = "select distinct day(reported_date)\n" + 
				"from events order by day(reported_date) asc";
		List<Integer> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection() ;
		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			ResultSet res = st.executeQuery() ;
			
			while (res.next()) {
				result.add(res.getInt("day(reported_date)"));
			}
			conn.close();
			return result;
			
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
		
	}
	
	public List<String> getVertex(String categoria, Integer giorno){
		String sql = "select distinct offense_type_id " + 
				"from events " + 
				"where day(reported_date) = ? " + 
				"and offense_category_id = ?";
		List<String> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection() ;
		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, giorno);
			st.setString(2, categoria);
			ResultSet res = st.executeQuery() ;
			
			while (res.next()) {
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
	
	public List<Arco> getArchi(String categoria, Integer giorno){
		String sql = "SELECT e1.offense_type_id, e2.offense_type_id, COUNT(DISTINCT e1.precinct_id) AS peso\n" + 
				"FROM events e1, events e2\n" + 
				"WHERE e1.offense_category_id=?\n" + 
				"AND e2.offense_category_id=?\n" + 
				"AND DAY(e1.reported_date)=?\n" + 
				"AND DAY(e2.reported_date)=?\n" + 
				"AND e1.offense_type_id<e2.offense_type_id\n" + 
				"AND e1.precinct_id=e2.precinct_id\n" + 
				"GROUP BY e1.offense_type_id, e2.offense_type_id\n" + 
				"ORDER BY peso desc\n";
		List<Arco> result = new ArrayList<>();
		
		Connection conn = DBConnect.getConnection() ;
		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setString(1, categoria);
			st.setString(2, categoria);
			st.setInt(3, giorno);
			st.setInt(4, giorno);
			ResultSet res = st.executeQuery() ;
			
			while (res.next()) {
				Arco a = new Arco(res.getString("e1.offense_type_id"), res.getString("e2.offense_type_id"), res.getInt("peso"));
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


