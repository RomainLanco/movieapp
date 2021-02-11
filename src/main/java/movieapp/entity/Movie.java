package movieapp.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "movies")
public class Movie {

	
	private Integer id;
	private String title;
	private Integer year,duration;
	private Artist director;
	
	private List<Artist> actors;
	
	public Movie() {
		super();
		actors = new ArrayList<>();
	}
	
	public Movie(String title, Integer year) {
		this();
		this.title = title;
		this.year = year;

	}
	public Movie(String title, Integer year,Integer duration) {
		this(title,year);
		this.duration = duration;
	}
	public Movie(String title, Integer year,Integer duration,Artist director) {
		this(title,year,duration);
		this.director = director;
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	@Column(nullable = false)
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	
	@Column(nullable = true)
	public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	
	@ManyToOne//(cascade = CascadeType.PERSIST)
	@JoinColumn(name="id_director",nullable = true)
	public Artist getDirector() {
		return director;
	}

	public void setDirector(Artist director) {
		this.director = director;
	}
	
	@ManyToMany
	@JoinTable(
			name = "play",
			joinColumns = @JoinColumn(name="id_movie"), //FK to this entity
			inverseJoinColumns = @JoinColumn(name="id_actor") //FK to the other entity
			)
	public List<Artist> getActors() {
		return actors;
	}

	public void setActors(List<Artist> actors) {
		this.actors = actors;
	}

	@Override
	public String toString() {
//		return  title + "(" + year + "," + duration + ")";
		
		StringBuilder builder = new StringBuilder();
		return builder.append(title)
				.append("(")
				.append(year)
				.append(",")
				.append(duration)
				.append(" mn)")
				.append("#")
				.append(id)
				.toString();
	}
	
	
	
}
