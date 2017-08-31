package com.EY.DB;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Servlet implementation class testing
 */
public class testing extends HttpServlet {
	private static final Logger log = Logger.getLogger(testing.class.getName());

	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public testing() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

//			new DbOperation().deleteFromDb("sf", "sfs");

		log.info("Method start");
		try {

			String path = readFromExcel.class.getResource("/sample_data.xlsx").getPath();
			FileInputStream excelFile = new FileInputStream(new File(path));
			Workbook workbook = new XSSFWorkbook(excelFile);
			Sheet datatypeSheet = workbook.getSheetAt(0);
			Iterator<Row> iterator = datatypeSheet.iterator();

			String[] headers = new String[55];
			String[] cRow = new String[55];
			boolean firstRow = true ;
			int index = 0;
			while (iterator.hasNext()) {
				index = 0;
				Row currentRow = iterator.next();
				Iterator<Cell> cellIterator = currentRow.iterator();

				while (cellIterator.hasNext()) {
					Cell currentCell = cellIterator.next();
					//getCellTypeEnum shown as deprecated for version 3.15
					//getCellTypeEnum ill be renamed to getCellType starting from version 4.0
					
					if (currentCell.getCellTypeEnum() ==  CellType.STRING) {
						if(firstRow){
							headers[index] = currentCell.getStringCellValue();
							index++;
						}
						else{
							cRow[index] = currentCell.getStringCellValue();
							index++;
						}


					} else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {

						//System.out.print(currentCell.getNumericCellValue() + "--");
						if(firstRow){
							headers[index] = currentCell.getStringCellValue();
							index++;
						}
						else{
							cRow[index] = currentCell.getStringCellValue();
							index++;
						}
					}
				}
				log.info("excel read : proceed to functions ");
				if(!firstRow){
					log.info(" insert topic ");

					//insertTopic(conn,cRow[0]);
					readFromExcel.insertTopic(cRow[0]);
					log.info("insert state");
					readFromExcel.insertState(headers, "US");
					//insertSubTopic(conn, cRow[1], cRow[0], out);
					//insertState(conn, headers, "US", out);
					//insertLawDesc(headers, cRow);
					//insertQuestion(conn, cRow[2], cRow[1], cRow[2], out);
				}
				else
				{
					//insertState(headers, "US");

				}
				firstRow = false;
				//System.out.println(cRow[0]);

			}
			workbook.close();
		} catch (Exception e) {
			log.info("exception reading excel : " + e);
			e.printStackTrace();
		}
		
			response.getWriter().write("end---------");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
