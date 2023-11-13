import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
 
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;


public class ControlServlet extends HttpServlet {
	    private static final long serialVersionUID = 1L;
	    private userDAO userdao = new userDAO();
	    private clientdao clientdao = new clientdao();
	    private requestsdao requestdao = new requestsdao();
	    private quotesdao quotedao = new quotesdao();
	    private orderofworkdao orderofworkdao = new orderofworkdao();
	    private billdao billdao = new billdao();
	    
	    
	    
	    private String currentUser;
	    private HttpSession session = null;
	    
	    public void init()
	    {
	    	userdao = new userDAO();
	    	clientdao = new clientdao();
	    	requestdao = new requestsdao();
	    	quotedao = new quotesdao();
	    	orderofworkdao = new orderofworkdao();
	    	billdao = new billdao();
	    	
	    	currentUser= "";
	    }
	    
	    public ControlServlet()
	    {
	    	
	    }
	    
	   
	    
	    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	        doGet(request, response);
	    }
	    
	    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	        String action = request.getServletPath();
	        System.out.println(action);
	    
	    try {
        	switch(action) {  
        	case "/login":
        		login(request,response);
        		break;
        	case "/register":
        		register(request, response);
        		break;
        	case "/initialize":
        		Initialize(request,response);
        		break;
        	case "registerClient":
        		registerClient(request, response);
        		
        	case "registerRequest":
        		registerRequest(request, response);
        		
        	case "registerQuote":
        		registerQuote(request,response);
        		
        	//////////////////////////////////////////////////////////
        	case "/quit":
        		quit(request, response);
        		break;
        	case "/response":
        		response(request, response);
        		break;
        		
        	case "/agree":
        		agree(request, response);
        		break;
        //////////////////////////////////////////////////////////		
        	case "/root":
        		rootPage(request,response, "");
        		break;
        	case "/logout":
        		logout(request,response);
        		break;
        	 case "/list": 
                 System.out.println("The action is: list");
                 listUser(request, response);           	
                 break;
	    	}
	    }
	    catch(Exception ex) {
        	System.out.println(ex.getMessage());
	    	}
	    }
        	
	    private void listUser(HttpServletRequest request, HttpServletResponse response)
	            throws SQLException, IOException, ServletException {
	        System.out.println("listUser started: 00000000000000000000000000000000000");

	     
	        List<user> listUser = userdao.listAllUsers();
	        request.setAttribute("listUser", listUser);       
	        RequestDispatcher dispatcher = request.getRequestDispatcher("UserList.jsp");       
	        dispatcher.forward(request, response);
	     
	        System.out.println("listPeople finished: 111111111111111111111111111111111111");
	    }
	    	        
	    private void rootPage(HttpServletRequest request, HttpServletResponse response, String view) throws ServletException, IOException, SQLException{
	    	System.out.println("root view");
			request.setAttribute("listUser", userdao.listAllUsers());
			request.setAttribute("listClients", clientdao.listAllClients());
			request.setAttribute("listQuotes", quotedao.listAllQuotes());
			request.setAttribute("listTreeRequests", requestdao.listAllRequests());
			request.setAttribute("listOrderOfWork", orderofworkdao.listAllOrders());
			request.setAttribute("listBills", billdao.listAllBills());
	    	request.getRequestDispatcher("rootView.jsp").forward(request, response);
	    }
	    
	    
	    protected void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
	    	 String username = request.getParameter("username");
	    	 String password = request.getParameter("password");
	    	 
	    	 if (username.equals("root") && password.equals("pass1234")) {
				 System.out.println("Login Successful! Redirecting to root");
				 session = request.getSession();
				 session.setAttribute("username", username);
				 rootPage(request, response, "");
				 request.getRequestDispatcher("rootView.jsp").forward(request, response);
	    	 }
	    	 else if(userdao.isValid(username, password)) 
	    	 {
			 	 
			 	 currentUser = username;
				 System.out.println("Login Successful! Redirecting");
				 request.getRequestDispatcher("activitypage.jsp").forward(request, response);
			 			 			 			 
	    	 }
	    	 else {
	    		 request.setAttribute("loginStr","Login Failed: Please check your credentials.");
	    		 request.getRequestDispatcher("login.jsp").forward(request, response);
	    	 }
	    }
	    
	    private void Initialize(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
	    	intialize.initializeDatabase();
	    	RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
	    	dispatcher.forward(request, response);

	    }

	    
	           
	    private void register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
	    	String username = request.getParameter("username");
	   	 	String password = request.getParameter("password");
	   	 	String role = request.getParameter("role"); 	   	 	
	   	 	String confirm = request.getParameter("confirmation");
	   	 	
	   	 	if (password.equals(confirm)) {
	   	 		if (!userdao.checkUsername(username)) {
		   	 		System.out.println("Registration Successful! Added to database");
		            user users = new user(username, password, role);
		   	 		userdao.insert(users);
		   	 		response.sendRedirect("login.jsp");
	   	 		}
		   	 	else {
		   	 		System.out.println("Username taken, please enter new username");
		    		 request.setAttribute("errorOne","Registration failed: Username taken, please enter a new username.");
		    		 request.getRequestDispatcher("register.jsp").forward(request, response);
		   	 	}
	   	 	}
	   	 	else {
	   	 		System.out.println("Password and Password Confirmation do not match");
	   		 request.setAttribute("errorTwo","Registration failed: Password and Password Confirmation do not match.");
	   		 request.getRequestDispatcher("register.jsp").forward(request, response);
	   	 	}
	    }    
	    
	    
	    private void registerClient(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
	        String firstName = request.getParameter("firstName");
	        String lastName = request.getParameter("lastName");
	        String address = request.getParameter("address");
	        String creditCard = request.getParameter("creditCard");
	        String phoneNumber = request.getParameter("phoneNumber");
	        String email = request.getParameter("email");
	        client Client = new client(firstName, lastName, address, creditCard, phoneNumber, email);
	        
	        clientdao.insert(Client);
	        System.out.println("Client Registration Successful!");
	        
	        response.sendRedirect("Clientportal.jsp");
	        
	    }
	    
	    private void registerQuote(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
	    	String initialPrice = request.getParameter("initialPrice");
	        String timeWindow = request.getParameter("timeWindow");
	        String note = request.getParameter("note");

	        quotes Quote = new quotes(initialPrice, note, timeWindow);
	        
	        quotedao.insert(Quote);
	        System.out.println("Quote Registration Successful!");
	        response.sendRedirect("request.jsp");
	    }
	    
	    private void registerRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
	    	String size = request.getParameter("size");
	        String height = request.getParameter("height");
	        String location = request.getParameter("location");
	        String nearHouse = request.getParameter("nearHourse");
	        String note = request.getParameter("note");
	        
	        requests request1 = new requests();
	        
	        requestdao.insert(request1);
	        System.out.println("Quote Registration Successful!");
	        response.sendRedirect("request.jsp");
	    }
	    
	    
	    private void agree(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
	    	quotes Quote = new quotes();
	    	quotedao.update(Quote);
	    }
	    
	    private void quit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
	    	quotes Quote = new quotes();
	    	quotedao.update(Quote);
	    }
	    
	    private void response(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
	    	quotes Quote = new quotes();
	    	quotedao.update(Quote);
	    }

	    
	    
	    private void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
	    	currentUser = "";
        		response.sendRedirect("login.jsp");
        	}
	    
	    
	    
}
	        
	        
	    




