package DAOImpl;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Date;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.mysql.jdbc.PreparedStatement;

import util.DataBaseUtil2;
import util.HibernateUtil;
import entity.Author;
import entity.Order;
import entity.Rack;
import DAO.BaseDAO;
import DAO.OrderDAO;

public class DAOOrderImpl implements BaseDAO<Order>,OrderDAO {

	@Override
	public Order getById(int id) throws SQLException {
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Order> list = null;
		
		try{
			session.beginTransaction();
			SQLQuery query = session.createSQLQuery("SELECT * FROM orders WHERE id =?;");
			query.setParameter(0 , id);
			query.addEntity(Order.class);
			list = query.list();
			
			session.getTransaction().commit();
		}catch(Exception e){
			session.getTransaction().rollback();
			e.printStackTrace();
		}finally{
			session.close();

		}
		return list.get(0);
		
		
		
	/*	Order order = new Order();
		DataBaseUtil2.init();
		String query = "SELECT * FROM orders WHERE id =?;";
		PreparedStatement prepStat = (PreparedStatement) DataBaseUtil2.getConnection().prepareStatement(query);
		prepStat.setInt(1, id);
		ResultSet resultSet = prepStat.executeQuery();
		while(resultSet.next()){
			order.setId(resultSet.getInt("id"));
			order.setStartDate(resultSet.getDate("start_date"));
			order.setEndDate(resultSet.getDate("end_date"));
			order.setActualDate(resultSet.getDate("actual_date"));
			order.setBookId(resultSet.getInt("book_id"));
			order.setCustomersId(resultSet.getInt("customer_id"));
			
		}
		resultSet.close();
		prepStat.close();
		return order;*/
	}

	@Override
	public boolean add(Order t) throws SQLException {
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		try{
			
			session.beginTransaction();
			SQLQuery query = session.createSQLQuery("INSERT INTO orders (start_date,end_date,customer_id,book_id) VALUE (?,?,?,?);");
			query.setParameter(0,t.getStartDate());
			query.setParameter(1,t.getEndDate());
			query.setParameter(2,t.getCustomer().getId());
			query.setParameter(3,t.getBookB().getId());
			
			query.executeUpdate();
			
			session.getTransaction().commit();
			}catch(Exception e){
				session.getTransaction().rollback();
				e.printStackTrace();
			}
			finally{
				session.close();
			}
			return true;
		/*DataBaseUtil2.init();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		System.out.println("Please!! Date of Return to "+dateFormat.format(t.getEndDate())+"!!");
		String INSERT = "INSERT INTO orders (start_date,end_date,customer_id,book_id) VALUE (?,?,?,?);";
		PreparedStatement prepStat = (PreparedStatement) DataBaseUtil2.getConnection().prepareStatement(INSERT);
		prepStat.setString(1,dateFormat.format(t.getStartDate()));
		prepStat.setString(2,dateFormat.format(t.getEndDate()));
		prepStat.setInt(3,t.getCustomerId());
		prepStat.setInt(4,t.getBookId());
		
		
		prepStat.executeUpdate();
		DAOBookImpl bookDAO = new DAOBookImpl();
		bookDAO.changeAvailebleBook(t.getBookId(), false);
		
		prepStat.close();
		return true;*/
		
	}

	@Override
	public boolean delete(Order t) throws SQLException {
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		try{
			
			session.beginTransaction();
			SQLQuery query = session.createSQLQuery("DELETE FROM orders WHERE id = ?");
			query.setParameter(0,t.getId());
			
			query.executeUpdate();
			
			session.getTransaction().commit();
			}catch(Exception e){
				session.getTransaction().rollback();
				e.printStackTrace();
			}
			finally{
				session.close();
			}
			return true;
		
		////
		/*DataBaseUtil2.init();
		String query = "DELETE FROM orders WHERE id = ?";
		PreparedStatement prepStat = (PreparedStatement) DataBaseUtil2.getConnection().prepareStatement(query);
		prepStat.setInt(1,t.getId());
		prepStat.executeUpdate();
		
		prepStat.close();
		return true;*/
	}

	@Override
	public List<Order> getAll() throws SQLException {
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Order> list = null;
	
		try{
		
		session.beginTransaction();
		SQLQuery query = session.createSQLQuery("SELECT * FROM orders;");
		query.addEntity(Order.class);
		list = query.list();
		
		session.getTransaction().commit();
		}catch(Exception e){
			session.getTransaction().rollback();
			e.printStackTrace();
		}
		finally{
			session.close();
		}
		return list;

	/*	DataBaseUtil2.init();
		List<Order> listOrder = new ArrayList<Order>();
		String query = "SELECT * FROM orders ;";
		PreparedStatement prepStat = (PreparedStatement) DataBaseUtil2.getConnection().prepareStatement(query);
		ResultSet resultSet = prepStat.executeQuery();
		while(resultSet.next()){
			Order order = new Order();
			order.setId(resultSet.getInt("id"));
			order.setStartDate(resultSet.getDate("start_date"));
			order.setEndDate(resultSet.getDate("end_date"));
			order.setActualDate(resultSet.getDate("actual_date"));
			order.setBookId(resultSet.getInt("book_id"));
			order.setCustomersId(resultSet.getInt("customer_id"));
			listOrder.add(order);
			
		}
		resultSet.close();
		prepStat.close();
		return listOrder;*/
	}


	@Override
	public void writeActualDateInOrder(int orderId,Date date) throws SQLException {
		Session session = HibernateUtil.getSessionFactory().openSession();
	
		try{
		
		session.beginTransaction();
		SQLQuery query = session.createSQLQuery("UPDATE orders SET actual_date =? WHERE id =?");
		query.setParameter(0,date);
		query.setParameter(1,orderId);
		query.executeUpdate();
		
		session.getTransaction().commit();
		}catch(Exception e){
			session.getTransaction().rollback();
			e.printStackTrace();
		}
		finally{
			session.close();
		}
		
		////
		/*DataBaseUtil2.init();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String query = "UPDATE orders SET actual_date =? WHERE id =?";
		PreparedStatement prepStat = (PreparedStatement) DataBaseUtil2.getConnection().prepareStatement(query);
		prepStat.setString(1, dateFormat.format(date));
		prepStat.setInt(2, orderId);
		prepStat.executeUpdate();
		
		prepStat.close();*/
		
	}

	
	
}
