package util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;

import com.mysql.jdbc.PreparedStatement;

import serviceImpl.OrderServiceImpl;
import DAOImpl.DAOAuthorImpl;
import DAOImpl.DAOBookImpl;
import DAOImpl.DAOCustomerImpl;


import entity.Author;
import entity.Book;
import entity.Customer;
import entity.Order;

public class StatisticDAOUtil {
	public List<Book> getCustomerAllBook(Customer customer) throws SQLException {
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Book> list = null;
	
		try{
		
		session.beginTransaction();
		SQLQuery query = session.createSQLQuery("SELECT b.* FROM book b LEFT JOIN orders ON b.id = orders.book_id LEFT JOIN customers "
				+ "ON orders.customer_id = customers.id WHERE customers.id = ? AND orders.actual_date IS NOT NULL");
		query.setParameter(0, customer.getId());
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

	}
	/*public List<Book> getCustomerAllBook(Customer customer) throws SQLException {
		List<Book> list = new ArrayList<Book>();
		DataBaseUtil2.init();
		String query = "SELECT book.id FROM book LEFT JOIN orders ON book.id = orders.book_id LEFT JOIN customers "
				+ "ON orders.customer_id = customers.id WHERE customers.id = ? AND orders.actual_date IS NOT NULL";
		PreparedStatement prepStat = (PreparedStatement) DataBaseUtil2.getConnection().prepareStatement(query);
		prepStat.setInt(1, customer.getId());
		ResultSet res  = prepStat.executeQuery();
		DAOBookImpl daoBookImpl = new DAOBookImpl();
		while(res.next()){
			list.add(daoBookImpl.getById(res.getInt("id")));
		}
		res.close();
		prepStat.close();
		return list;
	}*/
	public Customer getByInitials(String name, String sername, String nickname) throws SQLException {
		Customer customer = new Customer();
		DAOCustomerImpl daoCustomer = new DAOCustomerImpl();
		for(Customer c :daoCustomer.getAll()){
			if(c.getName().equals(name) && c.getNickname().equals(nickname) && c.getSername().equals(sername)){
				customer = c;
			}
		}
		
		return customer;
	}
	public List<Book> getCustomerBooksOnHands(Customer customer) throws SQLException {
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Book> list = null;
	
		try{
		
		session.beginTransaction();
		SQLQuery query = session.createSQLQuery("SELECT b.* FROM book b LEFT JOIN orders ON b.id = orders.book_id LEFT JOIN customers "
				+ "ON orders.customer_id = customers.id WHERE customers.id = ? AND orders.actual_date IS NULL");
		query.setParameter(0, customer.getId());
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

	}
/*	public List<Book> getCustomerBooksOnHands(Customer customer) throws SQLException {
		List<Book> list = new ArrayList<Book>();
		DataBaseUtil2.init();
		String query = "SELECT book.id FROM book LEFT JOIN orders ON book.id = orders.book_id LEFT JOIN customers "
				+ "ON orders.customer_id = customers.id WHERE customers.id = ? AND orders.actual_date IS NULL";
		PreparedStatement prepStat = (PreparedStatement) DataBaseUtil2.getConnection().prepareStatement(query);
		prepStat.setInt(1, customer.getId());
		ResultSet res  = prepStat.executeQuery();
		DAOBookImpl daoBookImpl = new DAOBookImpl();
		while(res.next()){
			list.add(daoBookImpl.getById(res.getInt("id")));
		}
		res.close();
		prepStat.close();
		return list;
	}*/
	public Date getStartTimeUseLibrary(Customer customer) throws SQLException {
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Date> list = null;
	
		try{
		
		session.beginTransaction();
		SQLQuery query = session.createSQLQuery("SELECT MIN(start_date) FROM orders LEFT JOIN customers"
				+ " ON orders.customer_id = customers.id WHERE customers.id =?");
		query.setParameter(0, customer.getId());
		
		
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
	/*public Date getStartTimeUseLibrary(Customer customer) throws SQLException {
		DataBaseUtil2.init();
		String query = "SELECT MIN(start_date) FROM orders LEFT JOIN customers ON orders.customer_id = customers.id WHERE customers.id =?";
		PreparedStatement prepStat = (PreparedStatement) DataBaseUtil2.getConnection().prepareStatement(query);
		prepStat.setInt(1, customer.getId());
		ResultSet res = prepStat.executeQuery();
		res.next();
		Date date = res.getDate(1);
		res.close();
		prepStat.close();
		return date;
	}*/
	public int getDuringCustomerUseLibrary(Customer customer) throws SQLException {
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<BigInteger> list = null;
	
		try{
		
		session.beginTransaction();
		SQLQuery query = session.createSQLQuery("SELECT DATEDIFF(CURDATE(),MIN(start_date)) FROM orders "
				+ "LEFT JOIN customers ON orders.customer_id = customers.id WHERE customers.id =?;");
		query.setParameter(0, customer.getId());
		
		
		list = query.list();
		
		session.getTransaction().commit();
		}catch(Exception e){
			session.getTransaction().rollback();
			e.printStackTrace();
		}
		finally{
			session.close();
			
		}
		BigInteger b = list.get(0);
		
		return (int)b.intValue();
	}
/*	public int getDuringCustomerUseLibrary(Customer customer) throws SQLException {
		DataBaseUtil2.init();
		String query = "SELECT DATEDIFF(CURDATE(),MIN(start_date)) FROM orders LEFT JOIN customers ON orders.customer_id = customers.id WHERE customers.id =?";
		PreparedStatement prepStat = (PreparedStatement) DataBaseUtil2.getConnection().prepareStatement(query);
		prepStat.setInt(1, customer.getId());
		ResultSet res = prepStat.executeQuery();
		res.next();
		int days = res.getInt(1);
		res.close();
		prepStat.close();
		return days;
	}*/
	public List<Book> getBookCustomerEverOrdered(Customer customer) throws SQLException {
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Book> list = null;
	
		try{
		
		session.beginTransaction();
		SQLQuery query = session.createSQLQuery("SELECT b.* FROM book b LEFT JOIN orders ON b.id = orders.book_id LEFT JOIN customers"
				+ " ON orders.customer_id = customers.id WHERE customers.id = ? AND orders.actual_date IS NOT NULL;");
		query.setParameter(0, customer.getId());
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
	}
	/*public List<Integer> getBookCustomerEverOrdered(Customer customer) throws SQLException {
		DataBaseUtil2.init();
		List<Integer> listbookId  = new ArrayList<Integer>();
		String query = "SELECT orders.book_id id FROM book LEFT JOIN orders ON book.id = orders.book_id LEFT JOIN customers"
				+ " ON orders.customer_id = customers.id WHERE customers.id = ? AND orders.actual_date IS NOT NULL";
		PreparedStatement prepStatBook = (PreparedStatement) DataBaseUtil2.getConnection().prepareStatement(query);
		prepStatBook.setInt(1,customer.getId());
		ResultSet resultSet = prepStatBook.executeQuery();
		while(resultSet.next()){
			listbookId.add(resultSet.getInt("id"));
		}
		resultSet.close();
		prepStatBook.close();
		return listbookId;
	}*/
	public int avarageTimeReading(Book book) throws SQLException {
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<BigDecimal> list = null;
	
		try{
		
		session.beginTransaction();
		SQLQuery query = session.createSQLQuery("SELECT AVG(DATEDIFF(orders.actual_date,orders.start_date)) DAY FROM book "
				+ "JOIN orders ON book.id = orders.book_id WHERE book.id = ?");
		query.setParameter(0, book.getId());
		
		
		list = query.list();
		
		session.getTransaction().commit();
		}catch(Exception e){
			session.getTransaction().rollback();
			e.printStackTrace();
		}
		finally{
			session.close();
			
		}
		
		BigDecimal b = list.get(0);
		
		return (int)b.intValue();
	}
/*	public int avarageTimeReading(Book book) throws SQLException {
		DataBaseUtil2.init();
		String query = "SELECT AVG(DATEDIFF(orders.actual_date,orders.start_date)) DAY FROM book JOIN orders ON book.id = orders.book_id WHERE book.id = ?";
		PreparedStatement prepStatBook = (PreparedStatement) DataBaseUtil2.getConnection().prepareStatement(query);
		prepStatBook.setInt(1, book.getId());
		StatisticUtil orderService = new  StatisticUtil();
		ResultSet res = prepStatBook.executeQuery();
		
		double sumDays = 0;
		while(res.next()){
			sumDays = res.getDouble("DAY");
		}
		
		res.close();
		prepStatBook.close();
		return (int)sumDays;
	}*/
	public int getBooksByIdHowMenyTimesTake(Book book)throws SQLException {
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<BigInteger> list = null;
	
		try{
		
		session.beginTransaction();
		SQLQuery query = session.createSQLQuery("SELECT COUNT(book.id) FROM book JOIN orders ON "
				+ "book.id = orders.book_id WHERE book.id = ?");
		query.setParameter(0, book.getId());
		
		
		list = query.list();
		
		session.getTransaction().commit();
		}catch(Exception e){
			session.getTransaction().rollback();
			e.printStackTrace();
		}
		finally{
			session.close();
			
		}
		
		
		
		return list.get(0).intValue();
	}
	/*public int getBooksByIdHowMenyTimesTake(Book book)throws SQLException {
		DataBaseUtil2.init();
		String query = "SELECT COUNT(book.id) FROM book JOIN orders ON book.id = orders.book_id WHERE book.id = ?";
		PreparedStatement prepStatBook = (PreparedStatement) DataBaseUtil2.getConnection().prepareStatement(query);
		prepStatBook.setInt(1, book.getId());
		ResultSet res = prepStatBook.executeQuery();
		res.next();
		int how = res.getInt(1);
		
		res.close();
		prepStatBook.close();
		return how;
	}*/
	public List<Book> getAllBookEverOrdered() throws SQLException {
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Book> list = null;
	
		try{
		
		session.beginTransaction();
		SQLQuery query = session.createSQLQuery("SELECT DISTINCT b.* FROM book b "
				+ "JOIN orders ON b.id = orders.book_id ORDER BY orders.book_id");
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

	}
	/*public List<Integer> getAllBookEverOrdered() throws SQLException {
		DataBaseUtil2.init();
		List<Integer> listbookId  = new ArrayList<Integer>();
		String query = "SELECT DISTINCT orders.book_id id FROM book JOIN orders ON book.id = orders.book_id ORDER BY orders.book_id";
		PreparedStatement prepStatBook = (PreparedStatement) DataBaseUtil2.getConnection().prepareStatement(query);
		ResultSet resultSet = prepStatBook.executeQuery();
		while(resultSet.next()){
			listbookId.add(resultSet.getInt("id"));
		}
		resultSet.close();
		prepStatBook.close();
		return listbookId;
	}*/
	public void printMostPopularBook() throws SQLException{
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Integer> list = null;
		List<Integer> list1 = null;
	
		try{
		
		session.beginTransaction();
		SQLQuery query = session.createSQLQuery("SELECT book.id FROM orders INNER JOIN book ON"
				+ " book.id = orders.book_id GROUP BY orders.book_id "
				+ "HAVING count(*) >=  ALL (SELECT count(*) FROM book INNER JOIN orders ON book.id = orders.book_id GROUP BY orders.book_id)");
		SQLQuery query1 = session.createSQLQuery("SELECT count(*) AS HOW FROM book INNER JOIN orders ON"
				+ " book.id = orders.book_id GROUP BY orders.book_id "
				+ "HAVING count(*) >=  ALL (SELECT count(*) FROM book INNER JOIN orders ON book.id = orders.book_id GROUP BY orders.book_id)");
		
		list = query.list();
		list1 = query1.list();
		
		session.getTransaction().commit();
		}catch(Exception e){
			session.getTransaction().rollback();
			e.printStackTrace();
		}
		finally{
			session.close();
			
		}
		DAOBookImpl daoBookImpl = new DAOBookImpl();
		for(Integer i:list){
			System.out.println(daoBookImpl.getById(list.get(0)));
		}

		System.out.println("The book is taken is "+ list1.get(0)+" times!!");
	}
	/*public void printMostPopularBook() throws SQLException{
		DataBaseUtil2.init();
		String query = "SELECT book.id AS ID,count(*) AS HOW FROM book LEFT JOIN orders ON book.id = orders.book_id GROUP BY orders.book_id HAVING count(*) >=  ALL (SELECT count(*) FROM book LEFT JOIN orders ON book.id = orders.book_id GROUP BY orders.book_id)";
		PreparedStatement prepStatBook = (PreparedStatement) DataBaseUtil2.getConnection().prepareStatement(query);
		ResultSet resultSet = prepStatBook.executeQuery();
		DAOBookImpl daoBookImpl = new DAOBookImpl();
		while(resultSet.next()){
			System.out.println(daoBookImpl.getById(resultSet.getInt("ID")));
			System.out.println("The book is taken is "+resultSet.getInt("HOW")+" times!!");
		}
		resultSet.close();
		prepStatBook.close();
	
		
	}*/
	public boolean printMostUnpopularBookNeverTaken() throws SQLException{
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Book> list = null;
	
		try{
		
		session.beginTransaction();
		SQLQuery query = session.createSQLQuery("SELECT b.* FROM book b LEFT JOIN orders ON "
				+ "b.id = orders.book_id WHERE orders.book_id IS NULL");
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
		
		if(list.isEmpty()){
			return false;
		}
		else{
		System.out.print("This book never taken!!! - ");
		for(Book b:list){
			System.out.println(b);
		}
		
		return true;
		}
	}
	/*public boolean printMostUnpopularBookNeverTaken() throws SQLException{
		DataBaseUtil2.init();
		String query = "SELECT book.id AS ID FROM book LEFT JOIN orders ON book.id = orders.book_id WHERE orders.book_id IS NULL";
		PreparedStatement prepStatBook = (PreparedStatement) DataBaseUtil2.getConnection().prepareStatement(query);
		ResultSet resultSet = prepStatBook.executeQuery();
		DAOBookImpl daoBookImpl = new DAOBookImpl();
		boolean entry = false;
		while(resultSet.next()){
			entry = true;
			System.out.print("This book never taken!!! - ");
			System.out.println(daoBookImpl.getById(resultSet.getInt("ID")));
			
		}
		resultSet.close();
		prepStatBook.close();
		return entry;
	}*/
	public boolean printMostUnpopularBook() throws SQLException{
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Book> list = null;
		List<Integer> list1 = null;
	
		try{
		
		session.beginTransaction();
		SQLQuery query = session.createSQLQuery("SELECT b.* FROM book b LEFT JOIN orders ON b.id = orders.book_id GROUP BY orders.book_id "
				+ "HAVING count(*) <=  ALL (SELECT count(*) FROM book LEFT JOIN orders ON book.id = orders.book_id GROUP BY orders.book_id)");
		query.addEntity("b",Book.class);
		SQLQuery query1 = session.createSQLQuery("SELECT count(*) AS HOW FROM book LEFT JOIN orders ON book.id = orders.book_id GROUP BY orders.book_id "
				+ "HAVING count(*) <=  ALL (SELECT count(*) FROM book LEFT JOIN orders ON book.id = orders.book_id GROUP BY orders.book_id)");
		list = query.list();
		list1 = query1.list();
		
		session.getTransaction().commit();
		}catch(Exception e){
			session.getTransaction().rollback();
			e.printStackTrace();
		}
		finally{
			session.close();
			
		}
		boolean bool = false;
		int count = 0;
		for(Book b:list){
				System.out.println(b);
			bool = true;
			count++;
		}
		if(count>1)System.out.println("The books have been taken "+list1.get(0)+" times!!");
		else if(count==1) System.out.println("The book has been taken "+list1.get(0)+" time!!");
		return bool;
		
	}
	/*public boolean printMostUnpopularBook() throws SQLException{
		DataBaseUtil2.init();
		String query = "SELECT book.id AS ID,count(*) AS HOW FROM book LEFT JOIN orders ON book.id = orders.book_id GROUP BY orders.book_id HAVING count(*) <=  ALL (SELECT count(*) FROM book LEFT JOIN orders ON book.id = orders.book_id GROUP BY orders.book_id)";
		PreparedStatement prepStatBook = (PreparedStatement) DataBaseUtil2.getConnection().prepareStatement(query);
		ResultSet resultSet = prepStatBook.executeQuery();
		DAOBookImpl daoBookImpl = new DAOBookImpl();
		boolean entry = false;
		while(resultSet.next()){
			entry = true;

			System.out.println(daoBookImpl.getById(resultSet.getInt("ID")));
			System.out.println("The book is taken is "+resultSet.getInt("HOW")+" times!!");
			
		}
		resultSet.close();
		prepStatBook.close();
		return entry;
	}*/
	public void printDebtor() throws SQLException{
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Order> list = null;
	
		try{
		
		session.beginTransaction();
		SQLQuery query = session.createSQLQuery("SELECT o.* FROM customers c JOIN "
				+ "orders o ON c.id = o.customer_id WHERE o.actual_date IS NULL ORDER BY c.id;");
		query.addEntity("c",Order.class);
		
		list = query.list();
		
		session.getTransaction().commit();
		}catch(Exception e){
			session.getTransaction().rollback();
			e.printStackTrace();
		}
		finally{
			session.close();
			
		}
		Order order  = new Order();
		order.getCustomer().setId(0);
		for(Order c:list){
			if(order.getCustomer().getId() == c.getCustomer().getId()){
				System.out.println("Borow-------"+c.getBookB());
			}
			else{
				System.out.println(c.getCustomer());
				System.out.println("Borow-------"+c.getBookB());
			}
			order = c;
		}
		
		
	}
	/*public void printDebtor() throws SQLException{
		DataBaseUtil2.init();
		String query = "SELECT customers.id AS id,customers.name AS name,customers.sername AS sername,customers.nickname AS nickname,customers.age AS age,"
				+ "orders.book_id AS book_id,orders.end_date AS date FROM customers JOIN "
				+ "orders ON customers.id = orders.customer_id WHERE orders.actual_date IS NULL ORDER BY customers.id;";
		PreparedStatement prepStatBook = (PreparedStatement) DataBaseUtil2.getConnection().prepareStatement(query);
		ResultSet resultSet = prepStatBook.executeQuery();
		int id = 0;
		while(resultSet.next()){
			if(id == resultSet.getInt("Id")){
				System.out.println("---Book id "+resultSet.getInt("book_id")+", Must return to "+resultSet.getDate("date")+"!!");
			}
			else{
			System.out.println("Id -"+resultSet.getInt("Id")+","+resultSet.getString("name")+","+resultSet.getString("sername")+
					","+resultSet.getString("nickname")+" age -"+resultSet.getInt("age")+",\n"+"---Book id "+resultSet.getInt("book_id")+", Must return to "+resultSet.getDate("date")+"!!");
			}
			id = resultSet.getInt("Id");
		}
		resultSet.close();
		prepStatBook.close();
	}*/
	public void printAVGCustomersInfo() throws SQLException{
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<BigDecimal> list = null;
		List<BigDecimal> list1 = null;
	
		try{
		
		session.beginTransaction();
		SQLQuery query = session.createSQLQuery("SELECT AVG(customers.age) AS AVG_AGE"
				+ " FROM orders JOIN customers ON customers.id = orders.customer_id ;");
		SQLQuery query1 = session.createSQLQuery("SELECT AVG(DATEDIFF(o.actual_date,o.start_date)) AS AVG_DATE FROM "
				+ "orders o JOIN customers ON customers.id = o.customer_id WHERE o.actual_date IS NOT NULL;");
		list = query.list();
		list1 = query1.list();
		session.getTransaction().commit();
		}catch(Exception e){
			session.getTransaction().rollback();
			e.printStackTrace();
		}
		finally{
			session.close();
			
		}
		System.out.println("Middle age customers is :"
				+list.get(0).intValue()+", and average time of "
						+ "reading is :"+list1.get(0).intValue()+" days");
		
		
	}
	/*public void printAVGCustomersInfo() throws SQLException{
		DataBaseUtil2.init();
		String query = "SELECT AVG(customers.age) AS AVG_AGE,DATEDIFF(CURDATE(),AVG(orders.start_date)) AS AVG_DATE FROM orders JOIN customers ON "
				+ "customers.id = orders.customer_id ;";
		PreparedStatement prepStatBook = (PreparedStatement) DataBaseUtil2.getConnection().prepareStatement(query);
		ResultSet resultSet = prepStatBook.executeQuery();
		while(resultSet.next()){
			System.out.println("Middle age customers is :"
		+resultSet.getInt("AVG_AGE")+", and average date of reading is :"+resultSet.getInt("AVG_DATE")+" days");
		}
		resultSet.close();
		prepStatBook.close();
	}*/
	public void printAVGCustomersByBook(int bookId) throws SQLException{
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<BigDecimal> list = null;

		try{
		
		session.beginTransaction();
		SQLQuery query = session.createSQLQuery("SELECT AVG(customers.age) AS AVG_AGE FROM orders JOIN customers ON "
				+ "customers.id = orders.customer_id JOIN book ON book.id = orders.book_id WHERE book.id = ? ;");
		query.setParameter(0, bookId);
		list = query.list();
	
		session.getTransaction().commit();
		}catch(Exception e){
			session.getTransaction().rollback();
			e.printStackTrace();
		}
		finally{
			session.close();
			
		}
		DAOBookImpl daoBook = new DAOBookImpl();
		System.out.println("By the book :"+ daoBook.getById(bookId));
		System.out.println("Middle age customers is :" + list.get(0).intValue()+"!!");
		
	}
/*	public void printAVGCustomersByBook(int bookId) throws SQLException{
		DataBaseUtil2.init();
		String query = "SELECT AVG(customers.age) AS AVG_AGE FROM orders JOIN customers ON "
				+ "customers.id = orders.customer_id JOIN book ON book.id = orders.book_id WHERE book.id = ? ;";
		PreparedStatement prepStat = (PreparedStatement) DataBaseUtil2.getConnection().prepareStatement(query);
		prepStat.setInt(1, bookId);
		DAOBookImpl daoBook = new DAOBookImpl();
		Book book = daoBook.getById(bookId);
		ResultSet resultSet = prepStat.executeQuery();
		while(resultSet.next()){
			System.out.println("By the book :"+ book);
			System.out.println("Middle age customers is :" + resultSet.getInt("AVG_AGE")+"!!");
		}
		resultSet.close();
		prepStat.close();
	}*/
	public void printAVGCustomersByAuthor(int authorId) throws SQLException{
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<BigDecimal> list = null;

		try{
		
		session.beginTransaction();
		SQLQuery query = session.createSQLQuery("SELECT AVG(customers.age) AS AVG_AGE FROM orders JOIN customers ON "
				+ "customers.id = orders.customer_id JOIN book ON book.id = orders.book_id JOIN book_author ON book.id = book_author.book_id"
				+ " JOIN author ON author.id = book_author.author_id WHERE author.id = ? ");
		query.setParameter(0, authorId);
		list = query.list();
	
		session.getTransaction().commit();
		}catch(Exception e){
			session.getTransaction().rollback();
			e.printStackTrace();
		}
		finally{
			session.close();
			
		}
		DAOAuthorImpl daoAuthor = new DAOAuthorImpl();
		System.out.println("By the author :"+ daoAuthor.getById(authorId));
		System.out.println("Middle age customers is :" + list.get(0).intValue()+"!!");
		
	}
	/*public void printAVGCustomersByAuthor(int authorId) throws SQLException{
		DataBaseUtil2.init();
		String query = "SELECT AVG(customers.age) AS AVG_AGE FROM orders JOIN customers ON "
				+ "customers.id = orders.customer_id JOIN book ON book.id = orders.book_id JOIN book_author ON book.id = book_author.book_id"
				+ " JOIN author ON author.id = book_author.author_id WHERE author.id = ? ;";
		PreparedStatement prepStat = (PreparedStatement) DataBaseUtil2.getConnection().prepareStatement(query);
		prepStat.setInt(1, authorId);
		DAOAuthorImpl daoAuthor = new DAOAuthorImpl();
		Author author = daoAuthor.getById(authorId);
		ResultSet resultSet = prepStat.executeQuery();
		while(resultSet.next()){
			System.out.println("By the author :"+ author);
			System.out.println("Middle age customers is :" + resultSet.getInt("AVG_AGE")+"!!");
		}
		resultSet.close();
		prepStat.close();
	}*/
	
}
