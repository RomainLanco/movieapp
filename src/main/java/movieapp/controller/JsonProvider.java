package movieapp.controller;

import java.time.LocalDate;

public class JsonProvider {
	
	
	
	public static String artistJson(String name, LocalDate birthdate) {
		return "{\"name\":\""+ name +"\", \"birthdate\":\""+birthdate+"\"}";
	}

	
}
