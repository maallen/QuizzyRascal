package com.grimewad.quizzyrascal;

import java.net.UnknownHostException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

@Path("/")
public class QuizzyRascalServer {
	
	private static int DEVICE_ID = 0;
	
	private static final String QUIZZY_RASCAL = "quizzyrascal";
	
	private static final String MONGOLAB_DB_URI = "mongodb://root:shroot@ds031349.mongolab.com:31349/quizzyrascal";
	
	private MongoClient mongoClient;
	
	@POST
	@Path("/notifyServer")
	@Consumes("application/json")
	public void receiveNotificationFromClientDevice(Notification notification){
		System.out.println("Received notification for device "
				+ notification.getDeviceId() + ", running browser "
						+ notification.getOpenBrowser());	
	}
	
	@GET
	@Path("/getId")
	@Produces("application/json")
	public DeviceId assignDeviceId(){
		DeviceId deviceId = new DeviceId();
		deviceId.setDeviceId(generateDeviceId());
		DB db = getMongoDb();
		BasicDBObject dbObject = createDeviceDbObject(deviceId.getDeviceId());
		DBCollection devices = db.getCollection("Devices");
		devices.insert(dbObject);
		mongoClient.close();
		return deviceId;
	}
	
	@POST
	@Path("/createTeam")
	@Consumes("application/json")
	public void createTeam(Team team){
		BasicDBObject teamDbObject = new BasicDBObject().append(Team.TEAM_NAME_KEY, team.getTeamName())
				.append(Team.NUM_OF_MEMBERS_KEY, team.getNumOfMembers());
		DB db = getMongoDb();
		DBCollection teams = db.getCollection("Teams");
		teams.insert(teamDbObject);
		mongoClient.close();
	}
	
	private BasicDBObject createDeviceDbObject(int deviceId){
		return new BasicDBObject().append(DeviceId.DEVICE_ID_KEY, deviceId)
				.append("teamName", "testTeam");
	}
	
	private DB getMongoDb(){
		
		if (mongoClient != null){
			return mongoClient.getDB(QUIZZY_RASCAL);
		}
		
		MongoClientURI mongoClientURI = new MongoClientURI(MONGOLAB_DB_URI);
		try {
			mongoClient = new MongoClient(mongoClientURI);
		} catch (UnknownHostException e) {
			System.out.print("Error retrieving MongoDB Client from MongoLab");
			e.printStackTrace();
		}
		
		return mongoClient.getDB(QUIZZY_RASCAL);
	}
	
	private int generateDeviceId(){
		return ++DEVICE_ID;
	}

}
