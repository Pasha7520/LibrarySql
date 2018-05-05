package printImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSetMetaData;

import print.Print;
import print.PrintBook;
import util.DataBaseUtil2;


import util.HibernateUtil;
import entity.Author;
import entity.Book;

public class PrintBookImpl implements Print<Book>,PrintBook {
	/*public void printBookAllBook() throws SQLException{
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Book> list = null;
		ArrayList<String> ar = new ArrayList<String>();
		
		try{
			
			session.beginTransaction();
			SQLQuery query = session.createSQLQuery("SELECT DISTINCT b.* from book_author left join book b ON b.id = "
				+ "book_author.book_id left join author a ON book_author.author_id = a.id left join rack ON b.rack_id = "
				+ "rack.id left join department ON rack.department_id = department.id order by b.id;");

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
		String s = "";
		for(Book b:list){
			System.out.println(b.getId()+"|"+b.getName()+"|"+printAuthors(b.getAuthorList())+"Department - "+b.getRack().getDepartment().getId());

		}
		
	}*/
	public void printBookAllBook() throws SQLException{
		DataBaseUtil2.init();
		ArrayList<String> ar = new ArrayList<String>();
		String query = "SELECT book.id,book.book_name,author.author_name,department.id from book_author left join book ON book.id = "
		+ "book_author.book_id left join author on book_author.author_id = author.id left join rack ON book.rack_id = rack.id left join department ON"
		+ " rack.department_id = department.id order by book.book_name;";
		PreparedStatement prepStat = (PreparedStatement) DataBaseUtil2.getConnection().prepareStatement(query);
		ResultSet resultSet = prepStat.executeQuery();
		ResultSetMetaData rsmd = (ResultSetMetaData) resultSet.getMetaData();
		int columnsNumber = rsmd.getColumnCount();
		while(resultSet.next()){
			String record = "";
			boolean entry = true;
			for(int i = 1; i <= columnsNumber;i++){
		record =record+ (resultSet.getString(i)+"| ");
			}
			record = record.substring(0,record.lastIndexOf("|"))+";";
		
			for(int i=0;i < ar.size();i++){
				if(record.substring(0,record.indexOf("|")).equals(ar.get(i).substring(0,ar.get(i).indexOf("|")))){
					String editTheAuthor = record.substring(record.indexOf("|")+1);
					String editTheAuthor1 = editTheAuthor.substring(editTheAuthor.indexOf("|")+2);
					
					String newS = ar.get(i).substring(0, ar.get(i).lastIndexOf("|")+1);
					String half1 = editTheAuthor1.substring(0, editTheAuthor1.lastIndexOf("|")+1);
					String half2 = editTheAuthor1.substring(editTheAuthor1.lastIndexOf("|")+1);
					String stringDepartment = newS+half1 + "Department -"+half2;
					
					ar.set(i,stringDepartment);
					entry = false;
				}
			}
			
			
			
			if(entry){
				String half1 = record.substring(0, record.lastIndexOf("|")+1);
				String half2 = record.substring(record.lastIndexOf("|")+1);
				String stringDepartment = half1 + "Department -"+half2;
				ar.add(stringDepartment);
				}
		}
		PrintList.printList(ar);
		resultSet.close();
		prepStat.close();
		
	}
	/*public void printDepartmentBoo(int DepNumber) throws SQLException{
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Book> list = null;
		ArrayList<String> ar = new ArrayList<String>();
		
		try{
			
			session.beginTransaction();
			SQLQuery query = session.createSQLQuery("SELECT DISTINCT b.* from book_author left join book b ON b.id = "
				+ "book_author.book_id left join author a ON book_author.author_id = a.id left join rack ON b.rack_id = "
				+ "rack.id left join department d ON rack.department_id = d.id WHERE d.id=? order by b.id;");
			query.setParameter(0, DepNumber);
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
		String s = "";
		for(Book b:list){
			System.out.println(b.getId()+"|"+b.getName()+"|"+printAuthors(b.getAuthorList())+"Department - "+b.getRack().getDepartment().getId());

		}
		
	}*/
	public void printDepartmentBook(int DepNumber) throws SQLException{
		ArrayList<String> ar = new ArrayList<String>();
		DataBaseUtil2.init();
		String query = "SELECT book.id,book.book_name,author.author_name,department.id from book_author left join book ON book.id = "
				+ "book_author.book_id left join author on book_author.author_id = author.id left join rack ON book.rack_id = rack.id left join department ON"
				+ " rack.department_id = department.id where department.id ="+DepNumber+" order by book.book_name;";
		PreparedStatement prepStat = (PreparedStatement) DataBaseUtil2.getConnection().prepareStatement(query);
		ResultSet resultSet = prepStat.executeQuery();
		
		ResultSetMetaData rsmd = (ResultSetMetaData) resultSet.getMetaData();
		int columnsNumber = rsmd.getColumnCount();
		while(resultSet.next()){
			String record = "";
			boolean entry = true;
			for(int i = 1; i <= columnsNumber;i++){
		record =record+ (resultSet.getString(i)+"| ");
			}
			record = record.substring(0,record.lastIndexOf("|"))+";";
		
			for(int i=0;i < ar.size();i++){
				if(record.substring(0,record.indexOf("|")).equals(ar.get(i).substring(0,ar.get(i).indexOf("|")))){
					String editTheAuthor = record.substring(record.indexOf("|")+1);
					String editTheAuthor1 = editTheAuthor.substring(editTheAuthor.indexOf("|")+2);
					
					String newS = ar.get(i).substring(0, ar.get(i).lastIndexOf("|")+1);
					String half1 = editTheAuthor1.substring(0, editTheAuthor1.lastIndexOf("|")+1);
					String half2 = editTheAuthor1.substring(editTheAuthor1.lastIndexOf("|")+1);
					String stringDepartment = newS+half1 + "Department -"+half2;
					
					ar.set(i,stringDepartment);
					entry = false;
				}
			}
			
			
			
			if(entry){
				String half1 = record.substring(0, record.lastIndexOf("|")+1);
				String half2 = record.substring(record.lastIndexOf("|")+1);
				String stringDepartment = half1 + "Department -"+half2;
				ar.add(stringDepartment);
				}
		}
		PrintList.printList(ar);
		prepStat.close();
		resultSet.close();
	}
	
	@Override
	public void print(Book book) {
		System.out.println(book);
		
	}
	@Override
	public void printList(List<Book> list) {
		for(Book book : list){
			System.out.println(book);
		}
		
	}
	public String printAuthors(List<Author>list){
		String s = "";
		for(Author author : list){
			s = s+author.getAuthorName()+"|";
		}
		return s;
	}
	
	
}
