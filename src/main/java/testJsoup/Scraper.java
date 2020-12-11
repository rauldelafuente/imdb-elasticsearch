package testJsoup;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import com.google.gson.Gson;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Scraper {

	public static StringBuilder finalContent = new StringBuilder();

	public static void main(String[] args) throws Exception {

		readExcel();

		try {
			FileWriter myWriter = new FileWriter("resultBulk.json");
			myWriter.write(finalContent.toString());
			myWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Este método recupera todas las etiquetas div summary_tex de una página web y las muestra por pantalla
	 * @param url
	 * @throws IOException
	 * @return String
	 */
	public static String getSummary(String url) throws IOException{
		Document doc = Jsoup.connect(url).get();
		StringBuilder description = new StringBuilder();
		Elements lst = doc.select("div.summary_text"); //<div clas="summary_text"> </div>
		String fullSum = lst.select("a").attr("abs:href");
		if(fullSum.equals("") || !fullSum.contains("plotsummary")) {
			for (Element elem : lst) {
				description.append(elem.text());
			}
		} else {
			Document docSum = Jsoup.connect(fullSum).get();
			Elements sum = docSum.select("li.ipl-zebra-list__item");
			description.append(sum.select("p").get(0).text());
		}
		return description.toString();
	}
	
	/**
	 * Metodo para recuperar los actores que actuaron en la pelicula
	 * @param url
	 * @throws IOException
	 * @return ArrayList<String>
	 */
	public static ArrayList<String> getCast(String url) throws IOException {
		Document doc = Jsoup.connect(url).get();
		ArrayList<String> cast = new ArrayList<String>();
		Element table;
		try {
			table = doc.select("table").get(0); //select the first table.
		} catch(IndexOutOfBoundsException e){
			return cast;
		}
		Elements rows = table.select("tr");

		for (int i = 1; i < rows.size(); i++) { //first row is the col names so skip it.
		    Element row = rows.get(i);
		    Elements cols = row.select("td");
		    try{
				cast.add(cols.get(1).select("a").text());
			} catch (IndexOutOfBoundsException e){
				//Skip this line
			}
		}
		return cast;
	}

	/**
	 * Este metodo va a leer las cosas del excel
	 * @throws IOException
	 * @return
	 */
	 private static void readExcel() throws IOException {
		    XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream("MovieGenreIGC_v3.xlsx"));
		    XSSFSheet sheet = wb.getSheetAt(0);
	        
	        Film film;

	        int rows = sheet.getLastRowNum();
	        for (int i = 1; i < rows; ++i) {
	        	System.out.println(i);
	        	XSSFRow row = sheet.getRow(i);

	        	XSSFCell imdbIdCell = row.getCell(0);
	        	XSSFCell linkCell = row.getCell(1);
	        	XSSFCell titleCell = row.getCell(2);
	        	XSSFCell genreCell = row.getCell(4);

	            int imdbId = (int) imdbIdCell.getNumericCellValue();
	            String link = linkCell.getStringCellValue();
	            String[] titleAux = titleCell.getStringCellValue().split("[()]");
	            String title = titleAux[0];
	            int year = 0;
				for (String titleAu : titleAux) {
					if (isNumeric(titleAu)) {
						year = Integer.parseInt(titleAu);
					}
				}
				String[] genreList = new String[0];
	            try{
	            	genreList = genreCell.getStringCellValue().split("\\|");
				} catch (NullPointerException e) {

				}
	            
	            String description = getSummary(link);
	            ArrayList<String> cast = getCast(link);
	            
	            film = new Film(imdbId, description, title, year, genreList, cast);

				Gson gson = new Gson();
				String filmOutput = gson.toJson(film);

				finalContent.append("{\"index\":{\"_id\":\"").append(imdbId).append("\"}}\n").
						append(filmOutput).append("\n");

			}
	        wb.close();
	    }

	public static boolean isNumeric(String strNum) {
		if (strNum == null) {
			return false;
		}
		try {
			Integer.parseInt(strNum);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}
}
