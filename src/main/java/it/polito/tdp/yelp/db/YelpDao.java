package it.polito.tdp.yelp.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


import it.polito.tdp.yelp.model.Adiacenza;
import it.polito.tdp.yelp.model.Business;
import it.polito.tdp.yelp.model.Review;
import it.polito.tdp.yelp.model.User;

public class YelpDao {

	public List<Business> getAllBusiness(){
		String sql = "SELECT * FROM Business";
		List<Business> result = new ArrayList<Business>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Business business = new Business(res.getString("business_id"), 
						res.getString("full_address"),
						res.getString("active"),
						res.getString("categories"),
						res.getString("city"),
						res.getInt("review_count"),
						res.getString("business_name"),
						res.getString("neighborhoods"),
						res.getDouble("latitude"),
						res.getDouble("longitude"),
						res.getString("state"),
						res.getDouble("stars"));
				result.add(business);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Review> getAllReviews(){
		String sql = "SELECT * FROM Reviews";
		List<Review> result = new ArrayList<Review>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Review review = new Review(res.getString("review_id"), 
						res.getString("business_id"),
						res.getString("user_id"),
						res.getDouble("stars"),
						res.getDate("review_date").toLocalDate(),
						res.getInt("votes_funny"),
						res.getInt("votes_useful"),
						res.getInt("votes_cool"),
						res.getString("review_text"));
				result.add(review);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<User> getAllUsers(Map<String, User> idMap){
		String sql = "SELECT * FROM Users";
		List<User> result = new ArrayList<User>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				User user = new User(res.getString("user_id"),
						res.getInt("votes_funny"),
						res.getInt("votes_useful"),
						res.getInt("votes_cool"),
						res.getString("name"),
						res.getDouble("average_stars"),
						res.getInt("review_count"));
				
				idMap.put(user.getUserId(), user);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<User> getVertici(int x, Map<String,User> idMap){
		String sql ="SELECT u.user_id as id "
				+ "FROM users u, reviews r "
				+ "WHERE u.user_id = r.user_id "
				+ "GROUP BY u.user_id "
				+ "HAVING COUNT(*) >=? ";
		
		List<User> result = new LinkedList<>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, x);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				result.add(idMap.get(rs.getString("id")));
			}
			
			rs.close();
			st.close();
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		
	}
	
	public List<Adiacenza> getAdiacenze(int x,int anno, Map<String,User> idMap){
		String sql ="WITH utente AS( "
				+ "SELECT u.user_id "
				+ "FROM users u, reviews r "
				+ "WHERE u.user_id = r.user_id "
				+ "GROUP BY u.user_id "
				+ "HAVING COUNT(*) >? "
				+ ")\n"
				+ "SELECT u1.user_id AS id1, u2.user_id as id2,COUNT(*) AS peso "
				+ "FROM utente as u1, utente as u2,reviews as r1, reviews as r2 "
				+ "WHERE u1.user_id = r1.user_id "
				+ "AND u2.user_id = r2.user_id "
				+ "AND u1.user_id<>u2.user_id "
				+ "AND r1.business_id = r2.business_id "
				+ "AND YEAR(r1.review_date) = ? "
				+ "AND YEAR(r1.review_date)=YEAR(r2.review_date) "
				+ "GROUP BY u1.user_id, u2.user_id "
				+ "ORDER BY peso DESC ";
				
		
		List<Adiacenza> result = new LinkedList<Adiacenza>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, x);
			st.setInt(2, anno);
			ResultSet rs = st.executeQuery();
			
			while (rs.next()) {
				User sorgente = idMap.get(rs.getString("id1"));
				User destinazione = idMap.get(rs.getString("id2"));
				if(sorgente != null && destinazione != null) {
					result.add(new Adiacenza(sorgente, 
							destinazione, rs.getInt("peso")));
				} else {
					System.out.println("Errore in getRotte");
				}
			}
			rs.close();
			st.close();
			conn.close();
			
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
				
	}
	
	
}
