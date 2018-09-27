package controller;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoCursor;

import database.EventDBO;
import gui.EventGO;

public class Controller {

	private EventDBO edb;


	private static final String EVENT_NAME_KEY = "title";


	public Controller() {
		this.edb = new EventDBO();
	}

	/**
	 * 	helper function to convert from string stored in database to usable boolean array
	 * @param string
	 * @return
	 */
	private static Boolean[] fromString(String string) {
		String[] strings = string.replace("[", "").replace("]", "").split(", ");
		Boolean result[] = new Boolean[strings.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = Boolean.parseBoolean(strings[i]);
		}
		return result;
	}

	/**
	 * 	helper function to convert from the Document object returned from database query
	 * to EventGO object used by GUI
	 * @param Document doc
	 * @return EventGO
	 */
	public static EventGO convertDocToEventGO(Document doc){
		ObjectId oid = doc.getObjectId("_id");
		int i = doc.getInteger("eventType");
		EventType type = EventType.valueOf(i);
		String title = doc.getString(EVENT_NAME_KEY);
		LocalDate date = LocalDate.parse(doc.getString("date"));
		LocalTime time = LocalTime.parse(doc.getString("time"));
		Duration duration = Duration.parse(doc.getString("duration"));
		int priority = doc.getInteger("priority");
		Boolean[] repeatDays = fromString(doc.getString("repeatDays"));
		LocalDate endRepeat = LocalDate.parse(doc.getString("endRepeat"));
		Duration notificationOffset = Duration.parse(doc.getString("notificationOffset"));
		Boolean completed = doc.getBoolean("completed");
		String user = doc.getString("userName");
		String id = oid.toString();
		return new EventGO(type, id, title, date, time, duration, priority, repeatDays, endRepeat, notificationOffset, completed, user);
	}

	/**
	 * Gets all events in database that are associated with the userName you pass in
	 * and returns an ArrayList of EventGO objects
	 * @param userName
	 * @return
	 */
	public ArrayList<EventGO> getAllEvents(String userName){
		MongoCursor<Document> c = edb.getAllEvents(userName);
		ArrayList<EventGO> al = new ArrayList<EventGO>();
		Document doc;
		if(c == null) {
			return al;
		}
		while(c.hasNext()) {
			doc  = c.next();
			al.add(convertDocToEventGO(doc));
		}
		return al;
	}

	/**
	 * Returns the name of the event
	 * @param ego
	 * @return returns name of event as string
	 */
	public String getEventNameById(EventGO ego) {
		if(ego == null) {
			return "";
		}
		Document d = edb.getEvent(ego.getID());
		if(d == null) {
			return "";
		}
		String name = d.getString(EVENT_NAME_KEY);
		return name;
	}

	/**
	 * Adds event to database and sets its ID to the id that mongoDB assigns it.
	 * Returns the string id that it was set to
	 * @param e
	 * @return the string ID that it was set to in MongoDB
	 */
	public String addEventToDatabase(EventGO e) {
		if(e == null) {
			return "";
		}
		int type = e.getType().ordinal();
		String title = e.getTitle();
		LocalDate date = e.getDate();
		LocalTime time = e.getTime();
		Duration duration = e.getDuration();
		int priority = e.getPriority();
		Boolean[] repeatDays = e.getRepeatDays();
		LocalDate endRepeat = e.getEndRepeat();
		Duration notificationOffset = e.getNotificationOffset();
		Boolean completed = e.getCompleted();
		String userName = e.getUserName();
		String idResult = edb.insertEvent(type, title, date, time, duration, priority, repeatDays, endRepeat, notificationOffset, completed, userName);
		e.setID(idResult);
		return idResult;
	}

	/**
	 * Deletes event from database
	 * @param Object id
	 */
	public void deleteEventFromDatabase(String id) {
		if(id != null) {
			edb.deleteEvent(id);
		}
	}

	/**
	 * Updates the event with the corresponding ID in the database
	 * @param updates the corresponding event in the 
	 */
	public void updateEventInDatabase(EventGO e) {
		if(e == null) {
			return;
		}
		ObjectId oid = new ObjectId(e.getID());
		int type = e.getType().ordinal();
		String title = e.getTitle();
		LocalDate date = e.getDate();
		LocalTime time = e.getTime();
		Duration duration = e.getDuration();
		int priority = e.getPriority();
		Boolean[] repeatDays = e.getRepeatDays();
		LocalDate endRepeat = e.getEndRepeat();
		Duration notificationOffset = e.getNotificationOffset();
		Boolean completed = e.getCompleted();
		String userName = e.getUserName();
		edb.updateEvent(oid, type, title, date, time, duration, priority, repeatDays, endRepeat, notificationOffset, completed, userName);
	}

	/**
	 * Returns the corresponding EventGO from the database given the ID of the object
	 * @param id
	 * @return new eventGO of event in database
	 */
	public EventGO getEventInDatabase(String id) {
		Document doc = edb.getEvent(id);
		if(doc == null) {
			return null;
		}
		return convertDocToEventGO(doc);
	}
}