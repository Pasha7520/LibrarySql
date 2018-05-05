package DAOImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;

import com.mysql.jdbc.PreparedStatement;

import serviceImpl.PositionServiceImpl;
import util.DataBaseUtil2;


import util.HibernateUtil;
import entity.Author;
import entity.Customer;
import entity.Person;
import DAO.BaseDAO;
import DAO.UserDAO;

public class DAOUserImpl implements BaseDAO<Person>,UserDAO{
	@Override
	public Person getById(int id) throws SQLException {
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Person> list = null;
		
		try{
			session.beginTransaction();
			SQLQuery query = session.createSQLQuery("SELECT * FROM person LEFT JOIN position "
				+ "ON person.position_id = position.id LEFT JOIN department ON person.department_id = department.id WHERE person.id =?");
			query.setParameter(0 , id);
			query.addEntity(Person.class);
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
	public boolean add(Person user) throws SQLException {
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		try{
			
			session.beginTransaction();
			SQLQuery query = session.createSQLQuery("INSERT INTO person (person_name,person_nickname,person_sername,person_age,sallary"
					+ ",department_id,position_id) VALUE (?,?,?,?,?,?,?);");
			query.setParameter(0,user.getName());
			query.setParameter(1,user.getNickname());
			query.setParameter(2,user.getSername());
			query.setParameter(3,user.getAge());
			query.setParameter(4,user.getSallary());
			query.setParameter(5,user.getDepartmentNumber());
			
			DAOPositionImpl position = new DAOPositionImpl();
			user.getPosition().setId(position.getNubmerOfPosition(user.getPosition().getName()));
			PositionServiceImpl positionService = new PositionServiceImpl();
			
			if(!(positionService.checkNumberOfPosition(user.getPosition().getId()))){
				System.out.println("Sach a position doesn't exist!!");
				return false;
			}

			query.setParameter(6,user.getPosition().getId());
			
			query.executeUpdate();
			
			
			
			session.getTransaction().commit();
			
			if(user.getPosition().getId()>1){
				writeInAuthorization(user);
			}
			
			
			}catch(Exception e){
				session.getTransaction().rollback();
				e.printStackTrace();
			}
			finally{
				session.close();
			}
			System.out.println("Emploee registered!!");
			return true;
		
		
		/*DataBaseUtil2.init();
		String INSERT = "INSERT INTO person (person_name,person_nickname,person_sername,person_age,sallary,department_id,position_id) VALUE (?,?,?,?,?,?,?);";
		PreparedStatement prepStat = (PreparedStatement) DataBaseUtil2.getConnection().prepareStatement(INSERT);
		prepStat.setString(1,user.getName());
		prepStat.setString(2,user.getNickname());
		prepStat.setString(3,user.getSername());
		prepStat.setInt(4,user.getAge());
		prepStat.setInt(5,user.getSallary());
		prepStat.setInt(6,user.getDepartmentNumber());
		DAOPositionImpl position = new DAOPositionImpl();
		
		prepStat.setInt(7,position.getNubmerOfPosition(user.getPosition()));
		if(position.getNubmerOfPosition(user.getPosition()) >0 && position.getNubmerOfPosition(user.getPosition()) < 5){
			if(position.getNubmerOfPosition(user.getPosition()) > 4 || position.getNubmerOfPosition(user.getPosition()) < 1){
				System.out.println("Sach a position doesn't exist!!");
				prepStat.close();
				return false;
			}
			prepStat.executeUpdate();
			
			if(position.getNubmerOfPosition(user.getPosition())>1){
				user.setPerson_id(getLastId());
			writeInAuthorization(user);
			}
			
			
			System.out.println("New worker added!!");
		}
		
		
		prepStat.close();
		return true;*/
	}

	@Override
	public boolean delete(Person user) throws SQLException {
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		try{
			
			session.beginTransaction();
			SQLQuery query = session.createSQLQuery("DELETE FROM person WHERE id =?");
			query.setParameter(0,user.getId());
			
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
		//if(!(getByIdPosition(user.getPerson_id()).equals("Cleaner"))){
			deleteFromAuthorization(user);
		//}
		String query = "DELETE FROM person WHERE id =?";
		PreparedStatement prepStat = (PreparedStatement) DataBaseUtil2.getConnection().prepareStatement(query);
		prepStat.setInt(1,user.getPerson_id());
		prepStat.executeUpdate();
		prepStat.close();
		return true;*/
	}

	@Override
	public List<Person> getAll() throws SQLException {
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Person> list = null;
		
		try{
			session.beginTransaction();
			SQLQuery query = session.createSQLQuery("SELECT * FROM person LEFT JOIN position "
				+ "ON person.position_id = position.id LEFT JOIN department ON person.department_id = department.id order by person.id;");
			query.addEntity(Person.class);
			list = query.list();
			
			session.getTransaction().commit();
		}catch(Exception e){
			session.getTransaction().rollback();
			e.printStackTrace();
		}finally{
			session.close();
		}
		return list;
		
	}


	@Override
	public boolean writeInAuthorization(Person user) throws SQLException {
			Session session = HibernateUtil.getSessionFactory().openSession();
			String login = user.getName()+"_"+user.getSername();
			int id = getLastId();
		try{
			
			session.beginTransaction();
			SQLQuery query = session.createSQLQuery("UPDATE person SET login =?,password=? WHERE id = ?;");
			query.setParameter(0,login );
			query.setParameter(1,"7520");
			query.setParameter(2,id);
			
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
	public boolean changePassword(Person user,String password) throws SQLException {
		Session session = HibernateUtil.getSessionFactory().openSession();
		
	try{
		
		session.beginTransaction();
		SQLQuery query = session.createSQLQuery("UPDATE person SET password = ? WHERE id = ?;");
		query.setParameter(0,password);
		query.setParameter(1,user.getId());
		
		query.executeUpdate();
		
		session.getTransaction().commit();
		}catch(Exception e){
			session.getTransaction().rollback();
			e.printStackTrace();
		}
		finally{
			session.close();
		}
		System.out.println("Changed!!");
		return true;
		
		
		/*DataBaseUtil2.init();
		String query = "UPDATE person SET password = ? WHERE id = ?;";
		PreparedStatement prepStat = (PreparedStatement) DataBaseUtil2.getConnection().prepareStatement(query);
		prepStat.setString(1, password);
		prepStat.setInt(2, user.getId());
		prepStat.executeUpdate();
		prepStat.close();
		return true;*/
	}



	@Override
	public int getLastId() throws SQLException {
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Integer> list = null;
		
		try{
			
			session.beginTransaction();
			SQLQuery query = session.createSQLQuery("SELECT MAX(id) FROM person ;");
			
			list = query.list();
			
			session.getTransaction().commit();
		}catch(Exception e){
			session.getTransaction().rollback();
			e.printStackTrace();
		}finally{
			session.close();
		}
		return (int)list.get(0);
		
	}



	@Override
	public Person getByLogin(String login) throws SQLException {
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Person> list = null;
		
		try{
			session.beginTransaction();
			SQLQuery query = session.createSQLQuery("SELECT * FROM person LEFT JOIN position "
				+ "ON person.position_id = position.id LEFT JOIN department ON person.department_id = department.id WHERE person.login =?");
			query.setParameter(0 , login);
			query.addEntity(Person.class);
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






	
	

	

}
