package testJsoup;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Scraper {
	public static final String URL_WIKIE = "https://www.imdb.com/title/tt0111161/";
	
	public static ArrayList<Film> filmsList = new ArrayList<Film>();

	public static void main(String[] args) throws Exception {
		//getTitle(URL_WIKIE);
		//getHRef(URL_WIKIE);
		//getImgs(URL_WIKIE);
		//getH4(URL_WIKIE);
		//getSummary(URL_WIKIE);
		//getSummaryItem(URL_WIKIE);
		//getCast(URL_WIKIE);
		readExcel();

		Gson gson = new Gson();
		String filmsJson = gson.toJson(filmsList);

		System.out.println(filmsJson);

		try {
			FileWriter myWriter = new FileWriter("resultGson.json");
			myWriter.write(filmsJson);
			myWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Este método recupera el título de cualquier página web
	 * @param url
	 * @throws IOException
	 */
	public static void getTitle(String url) throws IOException{
		Document doc = Jsoup.connect(url).get();
		System.out.println(url + ":\t" + doc.title());
		System.out.println();
	}
	
	/**
	 * Este método recupera todos los href de una página web y los muestra por pantalla
	 * @param url
	 * @throws IOException
	 */
	public static void getHRef(String url) throws IOException{
		Document doc = Jsoup.connect(url).get();
		System.out.println("a href from:\t" + doc.title());
		Elements lst = doc.select("a[href]");
		for (Element elem:lst) {
			System.out.println("\t:" + elem.text());
		}
		System.out.println();
	}
	
	/**
	 * Este método recupera todas las imagenes de una página web y las muestra por pantalla
	 * @param url
	 * @throws IOException
	 */
	public static void getImgs(String url) throws IOException{
		Document doc = Jsoup.connect(url).get();
		System.out.println("Imgs from:\t" + doc.title());
		Elements lst = doc.select("img[src~=[\\w\\d\\W]logo[\\w\\d\\W]]");
		for (Element elem:lst) {
			System.out.println("\t:" + elem.attr("src"));
		}
		System.out.println();
	}
	
	/**
	 * Este método recupera todas las etiquetas h4 de una página web y las muestra por pantalla
	 * @param url
	 * @throws IOException
	 */
	public static void getH4(String url) throws IOException{
		Document doc = Jsoup.connect(url).get();
		System.out.println("H4 from:\t" + doc.title());
		//obtenemos todas las etiquetas h4
		Elements lst = doc.select("h4");
		for (Element elem:lst) {
			System.out.println("\t:" + elem.childNode(0).toString());
		}
		System.out.println();
	}
	
	/**
	 * Este método recupera todas las etiquetas div summary_tex de una página web y las muestra por pantalla
	 * @param url
	 * @throws IOException
	 */
	public static String getSummary(String url) throws IOException{
		Document doc = Jsoup.connect(url).get();
		//System.out.println("Summary from:\t" + doc.title());
		//obtenemos el div summary_text
		String description = "";
		Elements lst = doc.select("div.summary_text");
		for (Element elem:lst) {
			description = description + elem.text();
		}
		return description;
	}
	
	
	/**
	 * Este método recupera todas las etiquetas div credit_summary_item de una página web y las muestra por pantalla
	 * @param url
	 * @throws IOException
	 */
	public static void getSummaryItem(String url) throws IOException{
		Document doc = Jsoup.connect(url).get();
		System.out.println("Items from:\t" + doc.title());
		//obtenemos el div credit_summary_item
		Elements lst = doc.select("div.credit_summary_item");
		for (Element elem:lst) {
			System.out.println("\t:" + elem.text());
		}
		System.out.println();
	}
	
	/**
	 * Metodo para recuperar los actores que actuaron en la pelicula
	 * @throws IOException 
	 */
	public static ArrayList<String> getCast(String url) throws IOException {
		Document doc = Jsoup.connect(url).get();
		ArrayList<String> cast = new ArrayList<String>();
		Element table = doc.select("table").get(0); //select the first table.
		Elements rows = table.select("tr");

		for (int i = 1; i < rows.size(); i++) { //first row is the col names so skip it.
		    Element row = rows.get(i);
		    Elements cols = row.select("td");
		    try{
				cast.add(cols.get(1).select("a").text());
			} catch (IndexOutOfBoundsException e){

			}
		}
			
		return cast;
		
	}
	
	
	
	/**
	 * Este metodo va a leer las cosas del excel
	 */
	 private static void readExcel() throws Exception {
		    XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream("MovieGenreIGC_v3.xlsx"));
		    XSSFSheet sheet = wb.getSheetAt(0);
	        
	        Film film;

	        int rows = sheet.getLastRowNum();
	        for (int i = 1; i < 10; ++i) {
	        	XSSFRow row = sheet.getRow(i);

	        	XSSFCell imdbIdCell = row.getCell(0);
	        	XSSFCell linkCell = row.getCell(1);
	        	XSSFCell titleCell = row.getCell(2);
	        	//XSSFCell scoreCell = row.getCell(3);
	        	XSSFCell genereCell = row.getCell(4);
	        	//XSSFCell posterCell = row.getCell(5);

	            int imdbId = (int) imdbIdCell.getNumericCellValue();
	            String link = linkCell.getStringCellValue();
	            String[] titleAux = titleCell.getStringCellValue().split("[\\(\\)]");
	            String title = titleAux[0];
	            int year = 0;
	            for(int j = 0; j<titleAux.length; j++) {
	            	if(isNumeric(titleAux[j])) {
						year = Integer.parseInt(titleAux[j]);
					}
				}
	            //double score = scoreCell.getNumericCellValue();
	            String[] genreList = genereCell.getStringCellValue().split("\\|");
	            //String poster = posterCell.getStringCellValue();
	            
	            String description = getSummary(link);
	            
	            ArrayList<String> cast = getCast(link);
	            
	            film = new Film(imdbId, description, title, year, genreList, cast);

		 		filmsList.add(film);


	            System.out.println(film.toString());

				/**
	            ObjectMapper mapper = new ObjectMapper();
	            
	            Map<String, Object> filmMap = new HashMap<String, Object>();

	            filmMap.put("imdbid", imdbId);
		 		filmMap.put("title", title);
		 		filmMap.put("year", year);
		 		filmMap.put("genre", genreList);
		 		filmMap.put("actors", cast);
		 		filmMap.put("sinopsis", description);

		 		Gson gson = new Gson();
		 		String filmsJson = gson.toJson(film);

	            try {
	                mapper.writeValue(new File("resultGson.json"), filmsJson);
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	            **/

	        }
	        
	        wb.close();
	        
	    }

	public static boolean isNumeric(String strNum) {
		if (strNum == null) {
			return false;
		}
		try {
			int d = Integer.parseInt(strNum);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}
	
	
}
