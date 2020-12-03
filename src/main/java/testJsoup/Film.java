package testJsoup;

import java.util.ArrayList;
import java.util.Arrays;

public class Film {
	
	private int imdbId;
	private String title;
	private String sinopsis;
	private int year;
	//private double score;
    private String[] genre;
    private ArrayList<String> actors;
	
    public Film() {}
    
    public Film(int imdbId, String sinopsis, String title, int year, String[] genre,
				ArrayList<String> actors) {
		super();
		this.imdbId = imdbId;
		this.sinopsis = sinopsis;
		this.title = title;
		this.year = year;
		this.genre = genre;
		this.actors = actors;
	}
	/**
	 * @return the imdbId
	 */
	public int getImdbId() {
		return imdbId;
	}
	/**
	 * @param imdbId the imdbId to set
	 */
	public void setImdbId(int imdbId) {
		this.imdbId = imdbId;
	}
	/**
	 * @return the link
	 */
	public String getLink() {
		return sinopsis;
	}
	/**
	 * @param link the link to set
	 */
	public void setLink(String description) {
		this.sinopsis = description;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the year
	 */
	public int getYear() {
		return year;
	}
	/**
	 * @param year the year to set
	 */
	public void setYear(int year) {
		this.year = year;
	}
	/**
	 * @return the genreList
	 */
	public String[] getGenre() {
		return genre;
	}
	/**
	 * @param genre the genreList to set
	 */
	public void setGenre(String[] genre) {
		this.genre = genre;
	}

	/**
	 * @return the description
	 */
	public String getSinopsis() {
		return sinopsis;
	}

	/**
	 * @param sinopsis the description to set
	 */
	public void setSinopsis(String sinopsis) {
		this.sinopsis = sinopsis;
	}

	/**
	 * @return the cast
	 */
	public ArrayList<String> getActors() {
		return actors;
	}

	/**
	 * @param actors the cast to set
	 */
	public void setActors(ArrayList<String> actors) {
		this.actors = actors;
	}

	@Override
	public String toString() {
		return "Film [imdbId=" + imdbId + ", description=" + sinopsis + ", title=" + title + ", year=" + year
				+ ", genreList=" + Arrays.toString(genre) + ", cast=" + actors + "]";
	}

}
