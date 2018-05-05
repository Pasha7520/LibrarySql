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
import entity.Customer;
import service.OrderService;
import serviceImpl.AuthorServiceImpl;
import serviceImpl.OrderServiceImpl;
import serviceImpl.RackServiceImpl;
import util.DataBaseUtil2;
import util.HibernateUtil;
import util.MathUtil;
import DAO.BaseDAO;
import DAO.BookDAO;

public class DAOBookImpl implements BaseDAO<Book>,BookDAO {

	@Override
	public Book getById(int Id) throws SQLException {
	Session session = HibernateUtil.getSessionFactory().openSession();
	List<Book> list = null;
	
	try{
		
		session.beginTransaction();
		SQLQuery query = session.createSQLQuery("SELECT b.* "+
				" FROM book_author left join book b ON b.id = "
				+ "book_author.book_id left join author a on book_author.author_id = a.id left join rack ON b.rack_id = rack.id left join department ON"
				+ " rack.department_id = department.id where b.id =?;");

		
		query.setParameter(0 , Id);
		query.addEntity("b", Book.class);
		
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
	public boolean add(Book b) throws SQLException {
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		try{
			
			session.beginTransaction();
			SQLQuery query = session.createSQLQuery("INSERT INTO book (book_name,book_page,rack_id,availeble,price) VALUE (?,?,?,?,?);");
			query.setParameter(0,b.getName());
			query.setParameter(1,b.getPages());
			RackServiceImpl rackService = new RackServiceImpl();
			query.setParameter(2,rackService.findfreeRac());
			query.setParameter(3,true);
			query.setParameter(4,b.getPrice());

			query.executeUpdate();
			
			session.getTransaction().commit();
			}catch(Exception e){
				session.getTransaction().rollback();
				e.printStackTrace();
			}
			finally{
				session.close();
			}
		
		AuthorServiceImpl authorServiceImpl = new AuthorServiceImpl();
		authorServiceImpl.checkingOrWritingNewAuthor(b.getListAuthor());
		bookAuthorWrite(b.getListAuthor());

			return true;
			
	
	}

	@Override
	public boolean delete(Book t) throws SQLException {
		
		if(deleteBookAuthor(t.getId())){
			Session session = HibernateUtil.getSessionFactory().openSession();
		
		try{
			
			session.beginTransaction();
			SQLQuery query = session.createSQLQuery("DELETE FROM book WHERE id =?");
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
		return false;
	}

	@Override
	public List<Book> getAll() throws SQLException {	
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Book> list = null;
	
		try{
		
		session.beginTransaction();
		SQLQuery query = session.createSQLQuery("SELECT DISTINCT b.* from book_author left join book b ON b.id = "
				+ "book_author.book_id left join author a ON book_author.author_id = a.id left join rack ON b.rack_id = "
				+ "rack.id left join department ON rack.department_id = department.id order by b.id;");
		query.addEntity("b",Book.class);
		
		
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
		List<Book> listbook  = new ArrayList<Book>();
		String query = "SELECT book.id,book.book_name,book.book_page,book.price,book.availeble,author.id author_id,author.author_name,rack.id rack_id from book_author left join book ON book.id = "
				+ "book_author.book_id left join author on book_author.author_id = author.id left join rack ON book.rack_id = rack.id order by book.id";
		PreparedStatement prepStat = (PreparedStatement) DataBaseUtil2.getConnection().prepareStatement(query);
		ResultSet resultSetBook = prepStat.executeQuery();
		int id = 0;
		while(resultSetBook.next()){
			
			if(id == resultSetBook.getInt("id")){
				Author author = new Author();
				author.setId(resultSetBook.getInt("author_id"));
				author.setAuthorName(resultSetBook.getString("author_name"));
				listbook.get(listbook.size()-1).addAuthor(author);
				
			}
			else{
				Book book = new Book();
				book.setId(resultSetBook.getInt("id"));	
				book.setName(resultSetBook.getString("book_name"));
				book.setPages(resultSetBook.getInt("book_page"));
					if(resultSetBook.getByte("availeble")==0){
						book.setAvaileble(false);
					}else{
						book.setAvaileble(true);
					}
					book.setNumberRack(resultSetBook.getInt("rack_id"));
					book.setPrice(resultSetBook.getDouble("price"));
			
					Author author = new Author();
					author.setId(resultSetBook.getInt("author_id"));
					author.setAuthorName(resultSetBook.getString("author_name"));
			
					book.addAuthor(author);
					listbook.add(book);
			}
			
				id = resultSetBook.getInt("id");
		}
		resultSetBook.close();
		prepStat.close();
		return listbook;
		*/
	}


	@Override
	public void bookAuthorWrite(List<Author> listAuthor) throws SQLException {
		int BookId = getMaxBookId();
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		AuthorServiceImpl authorService = new AuthorServiceImpl();
		listAuthor = authorService.findAuthorsId(listAuthor);
		
		try{
			
			session.beginTransaction();
			SQLQuery query = session.createSQLQuery("INSERT INTO book_author (book_id,author_id) VALUE (?,?);");
			
			for(Author a:listAuthor){
				query.setParameter(0,BookId);
				query.setParameter(1,a.getId());
				query.executeUpdate();
			}
			
			session.getTransaction().commit();
			}catch(Exception e){
				session.getTransaction().rollback();
				e.printStackTrace();
			}
			finally{
				session.close();
			}
		/////////
	/*	String query = "INSERT INTO book_author (book_id,author_id) VALUE (?,?);";
		PreparedStatement prepStat = (PreparedStatement)DataBaseUtil2.getConnection().prepareStatement(query);
		String maxId = "SELECT MAX(id) FROM book;";
		PreparedStatement prepStatMax = (PreparedStatement)DataBaseUtil2.getConnection().prepareStatement(maxId);
		ResultSet res = prepStatMax.executeQuery();
		res.next();
		int max = res.getInt(1);
		AuthorServiceImpl authorService = new AuthorServiceImpl();
		listAuthor = authorService.findAuthorsId(listAuthor);

		for(Author a : listAuthor){
		prepStat.setInt(1,max);
		prepStat.setInt(2, a.getId());
		prepStat.executeUpdate();
		}
		res.close();
		prepStatMax.close();
		prepStat.close();*/
	}
	public int getMaxBookId(){
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Integer> list = null;
		try{
			
			session.beginTransaction();
			SQLQuery query = session.createSQLQuery("SELECT MAX(id) FROM book;");
			
			list = query.list();
			
			session.getTransaction().commit();
			}catch(Exception e){
				session.getTransaction().rollback();
				e.printStackTrace();
			}
			finally{
				session.close();
			}
		return (int)list.get(0);
	}
	
	@Override
	public void changeAvailebleBook(int id,boolean b) throws SQLException {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			
			session.beginTransaction();
			SQLQuery query = session.createSQLQuery("UPDATE book SET availeble = ? WHERE id = ?;");
			if(b){
				byte br = 1;
				query.setParameter(0,br);
			}
			else {
				byte rr =0;
				query.setParameter(0, rr);
			}
			query.setParameter(1,id);
			
			query.executeUpdate();
			
			session.getTransaction().commit();
			}catch(Exception e){
				session.getTransaction().rollback();
				e.printStackTrace();
			}
			finally{
				session.close();
				
			}
			
		///////
		/*DataBaseUtil2.init();
		String queryBook = "UPDATE book SET availeble = ? WHERE id = ?;";
		PreparedStatement prepStatBook = (PreparedStatement) DataBaseUtil2.getConnection().prepareStatement(queryBook);
		if(b){
			byte br = 1;
		prepStatBook.setByte(1,br);
		}
		else {
			byte rr =0;
			prepStatBook.setByte(1, rr);
		}
		prepStatBook.setInt(2, id);
		prepStatBook.executeUpdate();
		prepStatBook.close();
		*/
	}

	@Override
	public int bookBelongDepartment(int bookId) throws SQLException {
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Integer> list = null;
		try{
			
			session.beginTransaction();
			SQLQuery query = session.createSQLQuery("SELECT department.id FROM book LEFT JOIN rack ON book.rack_id = rack.id LEFT JOIN "
					+ "department ON rack.department_id = department.id where book.id =?;");
			
			query.setParameter(0,bookId);
			
			list = query.list();
			
			session.getTransaction().commit();
			}catch(Exception e){
				session.getTransaction().rollback();
				e.printStackTrace();
			}
			finally{
				session.close();
			}
		
		
			/*DataBaseUtil2.init();
			String query = "SELECT department.id FROM book LEFT JOIN rack ON book.rack_id = rack.id LEFT JOIN "
					+ "department ON rack.department_id = department.id where book.id = "+bookId+";";
			PreparedStatement prepStatBook = (PreparedStatement) DataBaseUtil2.getConnection().prepareStatement(query);
			ResultSet resultSet = prepStatBook.executeQuery();
			resultSet.next();
			int number = resultSet.getInt(1);
			resultSet.close();
			prepStatBook.close();*/
			return (int)list.get(0);
		
	
	}


	@Override
	public List<Book> getByName(String name)throws SQLException {
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Book> list = null;
		
		try{
			
			session.beginTransaction();
			SQLQuery query = session.createSQLQuery("SELECT b.*"+
					" FROM book_author left join book b ON b.id = "
					+ "book_author.book_id left join author a on book_author.author_id = a.id left join rack ON b.rack_id = rack.id left join department ON"
					+ " rack.department_id = department.id where b.book_name =?;");

			
			query.setParameter(0 , name);
			query.addEntity("b",Book.class);
		
			
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

		////
		/*DataBaseUtil2.init();
		List<Book> listbook = new ArrayList<Book>();
		String queryBook = "SELECT book.id,book.book_name,book.book_page,book.price,book.availeble,author.id author_id,author.author_name,rack.id rack_id from book_author left join book ON book.id = "
		+ "book_author.book_id left join author on book_author.author_id = author.id left join rack ON book.rack_id = rack.id left join department ON"
		+ " rack.department_id = department.id where book.book_name =?";
		PreparedStatement prepStatBook = (PreparedStatement) DataBaseUtil2.getConnection().prepareStatement(queryBook);
		prepStatBook.setString(1, name);
		//DAOAuthorImpl daoAuthorImpl = new DAOAuthorImpl();
		ResultSet resultSetBook = prepStatBook.executeQuery();
		int id = 0;
		while(resultSetBook.next()){
			
			if(id == resultSetBook.getInt("id")){
				Author author = new Author();
				author.setId(resultSetBook.getInt("author_id"));
				author.setAuthorName(resultSetBook.getString("author_name"));
				listbook.get(listbook.size()-1).addAuthor(author);
				
			}
				else{
				Book book = new Book();
			book.setId(resultSetBook.getInt("id"));	
			book.setName(resultSetBook.getString("book_name"));
			book.setPages(resultSetBook.getInt("book_page"));
			if(resultSetBook.getByte("availeble")==0){
				book.setAvaileble(false);
			}else{
			book.setAvaileble(true);
				}
			book.setNumberRack(resultSetBook.getInt("rack_id"));
			book.setPrice(resultSetBook.getDouble("price"));
			
			Author author = new Author();
			author.setId(resultSetBook.getInt("author_id"));
			author.setAuthorName(resultSetBook.getString("author_name"));
			
			book.addAuthor(author);
			listbook.add(book);
			}
			
			id = resultSetBook.getInt("id");
		}
	
		
		
		prepStatBook.close();
		resultSetBook.close();
		return listbook;*/
	}

	@Override
	public boolean deleteBookAuthor(int id) throws SQLException {
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		try{
			
			session.beginTransaction();
			SQLQuery query = session.createSQLQuery("DELETE FROM book_author WHERE book_id =?");
			query.setParameter(0,id);
			
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

	

	

	
}
