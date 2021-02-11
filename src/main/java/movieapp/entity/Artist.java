package movieapp.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;



@Entity
@Table(name = "stars")
public class Artist {

	private Integer id;
	private String name; //required
	private LocalDate birthdate;
	private LocalDate deathdate;
	
	
	public Artist() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Artist(String name, LocalDate birthdate, LocalDate deathdate) {
		super();
		this.name = name;
		this.birthdate = birthdate;
		this.deathdate = deathdate;
	}

	
	public Artist(String name) {
		super();
		this.name = name;
	}
	

	public Artist(String name, LocalDate birthdate) {
		super();
		this.name = name;
		this.birthdate = birthdate;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(nullable = false, length= 300)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(nullable = true)
	public LocalDate getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(LocalDate birthdate) {
		this.birthdate = birthdate;
	}
	@Column(nullable = true)
	public LocalDate getDeathdate() {
		return deathdate;
	}
	public void setDeathdate(LocalDate deathdate) {
		this.deathdate = deathdate;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		return builder.append(name)
		.append("(")
		.append(birthdate)
		.append(", ")
		.append(deathdate)
		.append(")#")
		.append(id)
		.toString();
		
	}
	
	
}
