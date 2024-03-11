package com.tds.helper;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class Helper {



    public static Map<Integer, String[]> convertExcelToMap(InputStream inputFile) {
        Map<Integer, String[]> excelData = new HashMap<>();

        try {
            XSSFWorkbook workbook = new XSSFWorkbook(inputFile);
            Sheet sheet = workbook.getSheet("data");

            Iterator<Row> rowIterator = sheet.iterator();

            int rowNumber = 0;
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();

                // Create an array to store cell values for the current row
                String[] rowData = new String[row.getLastCellNum()];

                int cellIdx = 0;
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();

                    // Extract data from the cell based on its type
                    switch (cell.getCellType()) {
                        case STRING:
                            rowData[cellIdx] = cell.getStringCellValue();
                            break;
                        case NUMERIC:
                            rowData[cellIdx] = String.valueOf(cell.getNumericCellValue());
                            break;
                        // Handle other cell types as needed
                        default:
                            rowData[cellIdx] = ""; // Empty string for other types
                            break;
                    }
                    cellIdx++;
                }

                // Store rowData in the map with the corresponding row number
                excelData.put(rowNumber, rowData);
                rowNumber++;
            }

            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return excelData;
    }

/*
    public static void printData(InputStream inputFile){
        Map<Integer, String[]> dataMap = convertExcelToMap(inputFile);

        UserDetails userDetails = new UserDetails();
        ArrayList<String> list = new ArrayList<>();

        int count=0;
        for (Map.Entry<Integer, String[]> entry : dataMap.entrySet()) {
            System.out.println("..........................................................");
            Integer rowNumber = entry.getKey();
            String[] rowData = entry.getValue();
            String payableTds="";
            String givenTDS="";

            if (count != 0) {
                payableTds = tdsCalculator(rowData[rowData.length - 2]);

                givenTDS = differenceBetweenTDS(payableTds, rowData[rowData.length-1] );
            }

            System.out.print("Row " + rowNumber + ":   ");
            for (String cellValue : rowData) {
                list.add(cellValue);
                System.out.print(cellValue + "   :   ");
            }


            System.out.print("   :   "+payableTds);
            System.out.print("   :   "+givenTDS);

            System.out.println();
            count++;
        }
    }

    */

//    /*

    public static int getTotalCellNumber(InputStream inputStream) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet data = workbook.getSheet("data");    //sheet name
//        XSSFSheet data = workbook.getSheetAt(1);

        return data.getRow(1).getLastCellNum();
    }
public static void printData(InputStream inputFile,int totalCellNumber) throws IOException {

//    XSSFWorkbook workbook1 = new XSSFWorkbook(inputFile);
//    XSSFSheet sheet1 = workbook1.getSheet("data");

//    int lastRowNum = sheet.getLastRowNum();
//    int lastCellNum = sheet.getRow(1).getLastCellNum();
//
//    InputStream copiedInputFile = copyInputStream(inputFile);

    Map<Integer, String[]> dataMap = convertExcelToMap(inputFile);

    XSSFWorkbook workbook = new XSSFWorkbook();
    XSSFSheet sheet = workbook.createSheet("data");   //sheet name


    int requiredCellNumber=totalCellNumber+2;

    int rowNo = 0;
    int count=0;
    for (Map.Entry<Integer, String[]> entry : dataMap.entrySet()) {   // loop for row
        XSSFRow row = sheet.createRow(rowNo++);             //increasing row number
        String[] rowData = entry.getValue();
        for (int i = 0; i < 6; i++) {

            String payableTds = "0.0";  //  "Actual TDS"  or Actual tds which user have to pay
            String givenTDS = "0.0";   //  "TDS" or Wrong tds which user has to givein

            if (count !=0) {

                if (i == rowData.length - 2) {
                    payableTds = tdsCalculator(rowData[rowData.length - 4]);
                    row.createCell(i).setCellValue(payableTds);

                } else if (i == rowData.length - 1) {
                    System.out.println(rowData[rowData.length-2]+"..."+rowData[rowData.length - 3]+"..."+rowData[rowData.length - 4]+"..."+rowData[rowData.length - 5]);
                    givenTDS = differenceBetweenTDS(rowData[rowData.length-2], rowData[rowData.length - 3]);
                    row.createCell(i).setCellValue(givenTDS);
                }else {
                    row.createCell(i).setCellValue(rowData[i]);
                }
            }else {
                row.createCell(i).setCellValue(rowData[i]);
            }
//            row.createCell(i).setCellValue(rowData[i]);

        }
        count++;
    }

    FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\Dell\\OneDrive\\Desktop\\data\\TDSResult.xlsx");

    workbook.write(fileOutputStream);
    fileOutputStream.close();
    System.out.println("TDS Excel file has been created successfully");
}


//*/


    /*
    private static InputStream copyInputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inputStream.read(buffer)) > -1) {
            baos.write(buffer, 0, len);
        }
        baos.flush();
        return new ByteArrayInputStream(baos.toByteArray());
    }

*/

    public static String tdsCalculator(String withdrwalamount){

        double amount= Double.parseDouble(withdrwalamount);

        double tds=amount*0.1*12;

        return String.valueOf(tds);
    }


    public static String differenceBetweenTDS(String payableTds, String userGivenTds){

        double userHasToPayTDS = Double.parseDouble(payableTds);
        double userHasGivenTDS = Double.parseDouble(userGivenTds);

        double differenceInTDS=userHasToPayTDS-userHasGivenTDS;

        return String.valueOf(differenceInTDS);

    }
}
