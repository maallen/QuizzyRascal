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
	
	public static int DEVICE_ID = 0;
	
	@POST
	@Path("notifyServer")
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
		BasicDBObject dbObject = createDbObject(deviceId.getDeviceId());
		DBCollection devices = db.getCollection("Devices");
		devices.insert(dbObject);
		return deviceId;
	}
	
	private BasicDBObject createDbObject(int deviceId){
		return new BasicDBObject().append("deviceId", deviceId)
				.append("teamName", "testTeam");
	}
	
	private DB getMongoDb(){
		String mongoDB = "mongodb://root:shroot@ds031349.mongolab.com:31349/quizzyrascal";
		MongoClientURI mongoClientURI = new MongoClientURI(mongoDB);
		MongoClient mongoClient = null;
		try {
			mongoClient = new MongoClient(mongoClientURI);
			System.out.println("Connected!!");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return mongoClient.getDB("quizzyrascal");
	}
	
	@GET
	@Path("/test")
	@Produces("application/json")
	public Notification testGet(){
		Notification notification = new Notification();
		notification.setDeviceId(1);
		notification.setOpenBrowser("Chrome");
		return notification;
	}
	
	private int generateDeviceId(){
		return ++DEVICE_ID;
	}

}
