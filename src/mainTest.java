
import java.sql.SQLException;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import printImpl.PrintBookImpl;
import cons.Constants;
import serviceImpl.AuthorServiceImpl;
import serviceImpl.OrderServiceImpl;
import serviceImpl.RackServiceImpl;
import util.AuthorizationUtil;
import util.HibernateUtil;
import util.StatisticDAOUtil;
import util.StatisticUtil;
import entity.Author;
import entity.Book;
import entity.Customer;
import entity.Department;
import entity.Order;
import entity.Person;
import entity.Rack;
import DAOImpl.DAOAuthorImpl;
import DAOImpl.DAOBookImpl;
import DAOImpl.DAOCustomerImpl;
import DAOImpl.DAODepartmentImpl;
import DAOImpl.DAOOrderImpl;
import DAOImpl.DAORackImpl;
import DAOImpl.DAOUserImpl;


public class mainTest {

	public static void main(String[] args) throws SQLException {
		
	//register new customer will bee trouble
	
		StatisticDAOUtil s = new StatisticDAOUtil();
		s.printAVGCustomersInfo();
	}
	
}
