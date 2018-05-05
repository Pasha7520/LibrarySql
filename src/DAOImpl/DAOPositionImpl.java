package DAOImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;

import org.hibernate.SQLQuery;
import org.hibernate.Session;

import com.mysql.jdbc.PreparedStatement;

import entity.Author;
import entity.Customer;
import entity.Position;
import util.DataBaseUtil2;
import util.HibernateUtil;
import DAO.BaseDAO;
import DAO.PositionDAO;

public class DAOPositionImpl implements BaseDAO<Position>,PositionDAO{

	@Override
	public int getNubmerOfPosition(String namePosition) throws SQLException {

		for(Position p : getAll()){
			if(p.getName().equals(namePosition)){
				return p.getId();
			}
		}
		
		return 0;
	}

	@Override
	public Position getById(int Id) throws SQLException {
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Position> list = null;
		
		try{
			session.beginTransaction();
			SQLQuery query = session.createSQLQuery("SELECT * FROM position WHERE id = ?;");
			query.setParameter(0 , Id);
			query.addEntity(Position.class);
			list = query.list();
			
			session.getTransaction().commit();
		}catch(Exception e){
			session.getTransaction().rollback();
			e.printStackTrace();
		}finally{
			session.close();
		}
		return list.get(0);
		
	}

	@Override
	public boolean add(Position t) throws SQLException {
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		try{
			
			session.beginTransaction();
			SQLQuery query = session.createSQLQuery("INSERT INTO position (name) VALUE (?);");
			query.setParameter(0,t.getName());
			
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
	public boolean delete(Position t) throws SQLException {
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		try{
			
			session.beginTransaction();
			SQLQuery query = session.createSQLQuery("DELETE FROM position WHERE id =?");
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
	public List<Position> getAll() throws SQLException {
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Position> list = null;
	
		try{
		
		session.beginTransaction();
		SQLQuery query = session.createSQLQuery("SELECT * FROM position");
		query.addEntity(Position.class);
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
		List<Position> listCustomer  = new ArrayList<Position>();
		String query = "SELECT * FROM position";
		PreparedStatement prepStat = (PreparedStatement)DataBaseUtil2.getConnection().prepareStatement(query);
		ResultSet resultSet = prepStat.executeQuery();
		while(resultSet.next()){
			Position position = new Position();
			position.setId(resultSet.getInt("id"));
			position.setName(resultSet.getString("name"));
			listCustomer.add(position);		
		}
		resultSet.close();
		prepStat.close();
		
		return listCustomer;*/
	}
	
}
