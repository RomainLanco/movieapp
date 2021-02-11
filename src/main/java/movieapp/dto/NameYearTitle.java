package movieapp.dto;

public class NameYearTitle {

	private String name;
	private Integer year;
	private String title;
	
	
	public NameYearTitle(String name, Integer year, String title) {
		super();
		this.name = name;
		this.year = year;
		this.title = title;
	}
	public NameYearTitle() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@Override
	public String toString() {
		return "NameYearTitle [name=" + name + ", year=" + year + ", title=" + title + "]";
	}
	
	
}
