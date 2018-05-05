package DAOImpl;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.mysql.jdbc.PreparedStatement;

import serviceImpl.RackServiceImpl;
import util.DataBaseUtil2;
import util.HibernateUtil;
import DAO.AuthorDAO;
import DAO.BaseDAO;
import entity.Author;
import entity.Customer;
import entity.Rack;

public class DAOAuthorImpl implements BaseDAO<Author>,AuthorDAO {

	@Override
	public Author getById(int Id) throws SQLException {
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Author> list = null;
		
		try{
			session.beginTransaction();
			SQLQuery query = session.createSQLQuery("SELECT * FROM author WHERE id = ?;");
			query.setParameter(0 , Id);
			query.addEntity(Author.class);
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
	public boolean add(Author a) throws SQLException {
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		try{
			
			session.beginTransaction();
			SQLQuery query = session.createSQLQuery("INSERT INTO author (author_name) VALUE (?);");
			query.setParameter(0,a.getAuthorName() );
			
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
	public boolean delete(Author t) throws SQLException {
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		try{
			
			session.beginTransaction();
			SQLQuery query = session.createSQLQuery("DELETE FROM author WHERE id =?");
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
	public List<Author> getAll() throws SQLException {
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Author> list = null;
	
		try{
		
		session.beginTransaction();
		/*SQLQuery query = session.createSQLQuery("SELECT DISTINCT a.* from book_author left join book b ON b.id = "
				+ "book_author.book_id left join author a ON book_author.author_id = a.id"
				+ " order by a.id;");*/
		SQLQuery query = session.createSQLQuery("SELECT DISTINCT a.* from author a"
				+ " order by a.id;");
		query.addEntity(Author.class);
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
