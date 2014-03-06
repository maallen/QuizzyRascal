package com.grimewad.quizzyrascal;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/")
public class QuizzyRascalServer {
	
	public static int DEVICE_ID = 0;
	
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
		return deviceId;
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
