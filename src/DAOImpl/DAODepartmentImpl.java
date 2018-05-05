package DAOImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.mysql.jdbc.PreparedStatement;

import util.DataBaseUtil2;


import util.HibernateUtil;
import entity.Author;
import entity.Department;
import entity.Rack;
import DAO.BaseDAO;
import DAO.DepartmentDAO;

public class DAODepartmentImpl implements BaseDAO<Department>,DepartmentDAO {

	@Override
	public Department getById(int Id) throws SQLException {
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Department> list = null;
		
		try{
			session.beginTransaction();
			SQLQuery query = session.createSQLQuery("SELECT * FROM department WHERE id = ?;");
			query.setParameter(0 , Id);
			query.addEntity(Department.class);
			list = query.list();
			
			session.getTransaction().commit();
		}catch(Exception e){
			session.getTransaction().rollback();
			e.printStackTrace();
		}finally{
			session.close();
		}
		return list.get(0);
		
		///
		/*Department department = new Department();
		DataBaseUtil2.init();
		String query = "SELECT * FROM department WHERE id =?;";
		PreparedStatement prepStat = (PreparedStatement) DataBaseUtil2.getConnection().prepareStatement(query);
		prepStat.setInt(1, Id);
		ResultSet resultSet = prepStat.executeQuery();
		while(resultSet.next()){
			department.setId(resultSet.getInt("id"));
			department.setLibrary_id(resultSet.getInt("library_id"));
			
		}
		resultSet.close();
		prepStat.close();
		return department;
		*/
	}

	@Override
	public boolean add(Department t) throws SQLException {
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		try{
			
			session.beginTransaction();
			SQLQuery query = session.createSQLQuery("INSERT INTO department (id,library_id) VALUE (?,?);");
			query.setParameter(0,t.getId());
			query.setParameter(1,t.getLibrary_id());
			
			
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
		//
	/*	DataBaseUtil2.init();
		String INSERT = "INSERT INTO department (id,library_id) VALUE (?,?);";
		PreparedStatement prepStat = (PreparedStatement) DataBaseUtil2.getConnection().prepareStatement(INSERT);
		prepStat.setInt(1,t.getId());
		prepStat.setInt(2,t.getLibrary_id());
		prepStat.executeUpdate();
		
		prepStat.close();
		return true;*/
	}

	@Override
	public boolean delete(Department t) throws SQLException {
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		try{
			
			session.beginTransaction();
			SQLQuery query = session.createSQLQuery("DELETE FROM department WHERE id =?");
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
		
		
		/*DataBaseUtil2.init();
		String query = "DELETE FROM department WHERE id =?";
		PreparedStatement prepStat = (PreparedStatement) DataBaseUtil2.getConnection().prepareStatement(query);
		prepStat.setInt(1,t.getId());
		prepStat.executeUpdate();
		
		prepStat.close();
		return true;*/
	}

	@Override
	public List<Department> getAll() throws SQLException {
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Department> list = null;
	
		try{
		
		session.beginTransaction();
		SQLQuery query = session.createSQLQuery("SELECT * FROM department d ;");
		query.addEntity("d",Department.class);
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

		
		/*DataBaseUtil2.init();
		List<Department> listDepartment = new ArrayList<Department>();
		String query = "SELECT * FROM department";
		PreparedStatement prepStat = (PreparedStatement) DataBaseUtil2.getConnection().prepareStatement(query);
		ResultSet resultSet = prepStat.executeQuery();
		while(resultSet.next()){
			Department department = new Department();
			department.setId(resultSet.getInt("id"));
			department.setLibrary_id(resultSet.getInt("library_id"));
			
			listDepartment.add(department);
				
				
				
		}
		resultSet.close();
		prepStat.close();
		return listDepartment;*/
	}

}
