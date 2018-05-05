package DAOImpl;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import util.DataBaseUtil2;
import util.HibernateUtil;



import java.util.Date;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.mysql.jdbc.PreparedStatement;

import DAO.BaseDAO;
import DAO.CustomerDAO;
import entity.Author;
import entity.Book;
import entity.Customer;

public class DAOCustomerImpl implements BaseDAO<Customer>,CustomerDAO{

	@Override
	public Customer getById(int Id) throws SQLException {
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Customer> list = null;
	
		try{
		
		session.beginTransaction();
		SQLQuery query = session.createSQLQuery("SELECT * FROM customers WHERE id = ?;");
		query.setParameter(0, Id);
		query.addEntity(Customer.class);
		list = query.list();
		
		session.getTransaction().commit();
		}catch(Exception e){
			session.getTransaction().rollback();
			e.printStackTrace();
		}
		finally{
			session.close();
		}
		return list.get(0);
		
	}

	@Override
	public boolean add(Customer t) throws SQLException {
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		try{
			
			session.beginTransaction();
			SQLQuery query = session.createSQLQuery("INSERT INTO customers (name,nickname,sername,age) VALUE (?,?,?,?);");
			query.setParameter(0,t.getName() );
			query.setParameter(1,t.getNickname() );
			query.setParameter(2,t.getSername() );
			query.setParameter(3,t.getAge() );
			
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
			
	
	}

	@Override
	public boolean delete(Customer t) throws SQLException {
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		try{
			
			session.beginTransaction();
			SQLQuery query = session.createSQLQuery("DELETE FROM customers WHERE id =?");
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
	}

	@Override
	public List<Customer> getAll() throws SQLException {
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Customer> list = null;
	
		try{
		
		session.beginTransaction();
		SQLQuery query = session.createSQLQuery("SELECT * FROM customers");
		query.addEntity(Customer.class);
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
	
	}



}
