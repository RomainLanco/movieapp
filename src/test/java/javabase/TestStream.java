package javabase;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

class TestStream {

	@Test
	void testMapForEach() {
		List<String> cities = List.of("Toulouse","Pau","Nantes","Paris","Nîmes");
		
		cities.stream()
		//.map(c -> c.toUpperCase()) 
			.map(String::toUpperCase)
			//.forEach(c->System.out.println(c))
			.forEach(System.out::println)
			;
	}
	
	
	@Test
	void testMapFilterCollect() {
		List<String> cities = List.of(" Toulouse","Pau "," Nantes","Paris"," Nîmes ");
		var res = cities.stream()
		.map(String::trim)
		.map(String::toLowerCase)
		.filter(c->c.startsWith("n"))	//String -> boolean
		//.forEach(System.out::println)
		.collect(Collectors.toList())
		//collecte necessite : 
		//-> 1 accumulateur initial
		//-> 1 opération d'accumulation pour chaque donnée du stream 
		//-> 1 opération de finalisation (accumulateur -> ?)
		//-> 1 opération de combinaison d'accumulateur (travail en //)
		
		;
	
		System.out.println(res);
	}
	
	@Test
	void testMapFilterToStats() {
		List<String> cities = List.of(" Toulouse","Pau "," Nantes","Paris"," Nîmes ");
		var res = cities.stream()
		.map(String::trim)
		.map(String::toLowerCase)
		//.filter(c->c.startsWith("n"))	//String -> boolean
		.mapToInt(String::length)
			//.sum();
			//.average()
			//.count()
			//.min()
			//.max()
		.summaryStatistics()
		//.forEach(System.out::println)
//		.collect(Collectors.toList())
		//collecte necessite : 
		//-> 1 accumulateur initial
		//-> 1 opération d'accumulation pour chaque donnée du stream 
		//-> 1 opération de finalisation (accumulateur -> ?)
		//-> 1 opération de combinaison d'accumulateur (travail en //)
		
		;
	
		System.out.println(res);
	}

}
