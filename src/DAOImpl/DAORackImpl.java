package DAOImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.mysql.jdbc.PreparedStatement;

import entity.Author;
import entity.Book;
import entity.Rack;
import util.DataBaseUtil2;
import util.HibernateUtil;
import DAO.BaseDAO;
import DAO.RackDAO;

public class DAORackImpl implements BaseDAO<Rack>,RackDAO {

	@Override
	public Rack getById(int Id) throws SQLException {
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Rack> list = null;
		
		try{
			session.beginTransaction();
			SQLQuery query = session.createSQLQuery("SELECT * FROM rack WHERE id = ?;");
			query.setParameter(0 , Id);
			query.addEntity(Rack.class);
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
	public boolean add(Rack t) throws SQLException {
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		try{
			
			session.beginTransaction();
			SQLQuery query = session.createSQLQuery("INSERT INTO rack (id,capacity,department_id) VALUE (?,?,?);");
			query.setParameter(0,t.getId() );
			query.setParameter(1,t.getCapacity());
			query.setParameter(2,t.getDepartment().getId());

			
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
	public boolean delete(Rack t) throws SQLException {
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		try{
			
			session.beginTransaction();
			SQLQuery query = session.createSQLQuery("DELETE FROM rack WHERE id =?");
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
	public List<Rack> getAll() throws SQLException {
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Rack> list = null;
	
		try{
		
		session.beginTransaction();
		SQLQuery query = session.createSQLQuery("SELECT * FROM rack");
		query.addEntity(Rack.class);
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


	@Override
	public int bookBelongRack(int rackId) throws SQLException {
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Book> list = null;
	
		try{
		
		session.beginTransaction();
		SQLQuery query = session.createSQLQuery("SELECT * FROM book WHERE book.rack_id = ?;");
		query.setParameter(0,rackId);
		query.addEntity(Book.class);
		list = query.list();
		
		session.getTransaction().commit();
		}catch(Exception e){
			session.getTransaction().rollback();
			e.printStackTrace();
		}
		finally{
			session.close();
			
		}
		return list.size();
		/*DataBaseUtil2.init();
		String query = "SELECT * FROM book WHERE book.rack_id = ?;";
		PreparedStatement prepStat = (PreparedStatement) DataBaseUtil2.getConnection().prepareStatement(query);
		prepStat.setInt(1, rackId);
		ResultSet resultSet = prepStat.executeQuery();
		resultSet.last();
	
		return resultSet.getRow();*/
	}

	

}
