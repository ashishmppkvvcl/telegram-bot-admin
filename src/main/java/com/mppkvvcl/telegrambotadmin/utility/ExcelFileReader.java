package com.mppkvvcl.telegrambotadmin.utility;


import com.monitorjbl.xlsx.StreamingReader;
import com.mppkvvcl.telegrambotadmin.botcode.MyAmazingBot;
import com.mppkvvcl.telegrambotadmin.dto.ExcelDTO;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;

import java.io.*;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExcelFileReader {
    private Logger logger = LoggerUtil.getLogger(ExcelFileReader.class);

    public List excelFileReader(byte[] data) throws IOException, ParseException {
        long exceptionCount=0;
        InputStream is = new ByteArrayInputStream(data);
        Workbook workbook = StreamingReader.builder()
                .rowCacheSize(100)
                .bufferSize(4096)
                .open(is);
        logger.info("Excel File opened successfully!! @"+ Static.sdf.format(new Date(System.currentTimeMillis())));
        Sheet sheet = workbook.getSheetAt(0);
        BigInteger big = BigInteger.ZERO;
        long startTime = System.currentTimeMillis();
        List<ExcelDTO> excelDTOList = new ArrayList<>();

        SimpleDateFormat ngbDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        for(Row r: sheet)
        {
            ExcelDTO excelDTO = new ExcelDTO();
            if(r.getRowNum()==0)
                continue;
            for(Cell c: r)
            {
                String cellValue = (c.getStringCellValue()==null)?"":c.getStringCellValue().trim();
                if(cellValue.isEmpty())
                    cellValue = "0";
                switch(c.getColumnIndex())
                {
                    case 0: excelDTO.setConsumerNo(cellValue);
                        logger.info(excelDTO.getConsumerNo()+",");
                        break;
                    case 1: excelDTO.setBillMonth(cellValue);
                        logger.info(excelDTO.getBillMonth()+",");
                        break;
                }
                logger.info(c.getStringCellValue()+" ");
            }
            //Saving the created bean Object
            try
            {
                excelDTOList.add(excelDTO);
                big = big.add(BigInteger.ONE);
            }
            catch(Exception e)
            {
                ++exceptionCount;
                logger.error("***********EXCEPTION NUMBER " + exceptionCount + "***********" + "Occured on: " + new Date());
                logger.error("***********CONSUMER NUMBER: " + excelDTO.getConsumerNo() + "***********");
                logger.error("Root cause : ");
                e.printStackTrace();
                continue;
            }

        }
        long endTime = System.currentTimeMillis();
        long seconds = (endTime - startTime)/1000;
        long minutes = seconds/60;
        seconds -= minutes*60;
        logger.info(big.toString() + " ROWS SUCCESSFULLY READED!!!!");
        logger.info(exceptionCount + " EXCEPTIONS CAUGHT !! PLEASE REFER EXCEPTION LOG FOR MORE DETAILS!!");
        logger.info("Time Elapsed: " + minutes + " Minutes " + seconds + " Seconds");
        workbook.close();
        return excelDTOList;
    }

    private static boolean strToBool(String str) {
        if("TRUE".equals(str))
            return true;
        else
            return false;
    }
}
