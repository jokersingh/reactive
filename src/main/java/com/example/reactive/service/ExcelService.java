package com.example.reactive.service;

import com.example.reactive.domain.Data;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ExcelService {

    public Flux<Data> getData() {
        return Flux.generate(
                this::getWorkbookSheet,
                (sheet, sink) ->{
                    sink.next(getNextData(sheet));
                    sink.complete();
                    return sheet;
                }
                ,
                this::closeWorkbook);
    }

    private void closeWorkbook(Iterator<Row> rowIterator) {

    }

    private <S> void closeWorkbook(Sheet sheet) {

    }

    private Data getNextData(Iterator<Row> rowIterator) {
        /*Iterator<Row> rowIterator = sheet.iterator();*/
        while (rowIterator.hasNext())
        {
            String name = "";
            String shortCode = "";

            //Get the row object
            Row row = rowIterator.next();

            //Every row has columns, get the column iterator and iterate over them
            Iterator<Cell> cellIterator = row.cellIterator();

            while (cellIterator.hasNext())
            {
                //Get the Cell object
                Cell cell = cellIterator.next();

                //check the cell type and process accordingly
                switch(cell.getCellType()){
                    case STRING:
                        if(shortCode.equalsIgnoreCase("")){
                            shortCode = cell.getStringCellValue().trim();
                        }else if(name.equalsIgnoreCase("")){
                            //2nd column
                            name = cell.getStringCellValue().trim();
                        }else{
                            //random data, leave it
                            System.out.println("Random data::"+cell.getStringCellValue());
                        }
                        break;
                    case NUMERIC:
                        System.out.println("Random data::"+cell.getNumericCellValue());
                }
            } //end of cell iterator
            return new Data(name, shortCode);
        }
        return null;
    }

    private Iterator<Row> getWorkbookSheet() throws IOException {
        String fileName = "C:\\workspace\\reactive\\country.xlsx";
        FileInputStream fis = new FileInputStream(fileName);
        Workbook workbook = null;
        if(fileName.toLowerCase().endsWith("xlsx")){
            workbook = new XSSFWorkbook(fis);
        }else if(fileName.toLowerCase().endsWith("xls")){
            workbook = new HSSFWorkbook(fis);
        }
        Sheet sheet = workbook.getSheetAt(0);
        return sheet.iterator();
    }

    private List<Data> readExcelData(String fileName) {
        List<Data> countriesList = new ArrayList<>();

        try {
            //Create the input stream from the xlsx/xls file
            FileInputStream fis = new FileInputStream(fileName);

            //Create Workbook instance for xlsx/xls file input stream
            Workbook workbook = null;
            if(fileName.toLowerCase().endsWith("xlsx")){
                workbook = new XSSFWorkbook(fis);
            }else if(fileName.toLowerCase().endsWith("xls")){
                workbook = new HSSFWorkbook(fis);
            }

            //Get the number of sheets in the xlsx file
            int numberOfSheets = workbook.getNumberOfSheets();

            //loop through each of the sheets
            for(int i=0; i < numberOfSheets; i++){

                //Get the nth sheet from the workbook
                Sheet sheet = workbook.getSheetAt(i);

                //every sheet has rows, iterate over them
                Iterator<Row> rowIterator = sheet.iterator();
                while (rowIterator.hasNext())
                {
                    String name = "";
                    String shortCode = "";

                    //Get the row object
                    Row row = rowIterator.next();

                    //Every row has columns, get the column iterator and iterate over them
                    Iterator<Cell> cellIterator = row.cellIterator();

                    while (cellIterator.hasNext())
                    {
                        //Get the Cell object
                        Cell cell = cellIterator.next();

                        //check the cell type and process accordingly
                        switch(cell.getCellType()){
                            case STRING:
                                if(shortCode.equalsIgnoreCase("")){
                                    shortCode = cell.getStringCellValue().trim();
                                }else if(name.equalsIgnoreCase("")){
                                    //2nd column
                                    name = cell.getStringCellValue().trim();
                                }else{
                                    //random data, leave it
                                    System.out.println("Random data::"+cell.getStringCellValue());
                                }
                                break;
                            case NUMERIC:
                                System.out.println("Random data::"+cell.getNumericCellValue());
                        }
                    } //end of cell iterator
                    Data c = new Data(name, shortCode);
                    countriesList.add(c);
                } //end of rows iterator


            } //end of sheets for loop

            //close file input stream
            fis.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return countriesList;
    }
}
