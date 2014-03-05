package quizzyrascal;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/")
public class QuizzyRascalServer {
	
	@POST
	@Path("/test")
	@Consumes({ "application/json" })
	public void testPost(){
		System.out.println("We're here!!!");
	}
	
	@GET
	@Path("/test")
	@Produces("application/json")
	public String testGet(){
		return "It worked!!!!!";
	}

}
