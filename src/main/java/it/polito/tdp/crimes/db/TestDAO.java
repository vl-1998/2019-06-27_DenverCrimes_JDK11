package it.polito.tdp.crimes.db;

import it.polito.tdp.crimes.model.Event;

public class TestDAO {

	public static void main(String[] args) {
		EventsDAO dao = new EventsDAO();
		for(Event e : dao.listAllEvents())
			System.out.println(e);
	}

}
