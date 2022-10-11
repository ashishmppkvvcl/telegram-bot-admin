package com.mppkvvcl.telegrambotadmin.service;



import com.mppkvvcl.telegrambotadmin.dto.ExcelDTO;
import com.mppkvvcl.telegrambotadmin.dto.RecordsUpdated;
import com.mppkvvcl.telegrambotadmin.utility.LoggerUtil;
import com.mppkvvcl.telegrambotadmin.utility.Static;
import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.slf4j.Logger;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.zeroturnaround.zip.ZipUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service("PdfFromExcelService")
public class PdfFromExcelService {

    private Logger logger = LoggerUtil.getLogger(PdfFromExcelService.class);
    FileOutputStream fos;

    public ResponseEntity pdfFromExcel(List<ExcelDTO>excelDTOList, RecordsUpdated recordsUpdated, String folderName, String fileBaseName) {
        logger.info("Inside pdfFromExcel Method of class : " + PdfFromExcelService.class.getName());
        ResponseEntity<byte[]> response = null;
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "Bearer " + Static.NGB_TOKEN);
        HttpEntity<Object> request = new HttpEntity<>(headers);
        recordsUpdated.setSuccessRecords(0);
        PDFMergerUtility obj = new PDFMergerUtility();

     /*   try {
            FileUtils.deleteDirectory(new File(Static.FolderPath+"\\"+chatId+"\\"));
        } catch (IOException e) {
            e.printStackTrace();
        } */
        for (ExcelDTO excelDTO : excelDTOList) {
            if(excelDTO.getConsumerNo().substring(0).equals("N"))
                excelDTO.setConsumerNo(excelDTO.getConsumerNo().substring(1));
            String url = Static.NGB_bill_download_by_ngb_bill_month +excelDTO.getConsumerNo()+"/bill-month/"+excelDTO.getBillMonth()+"/file-format/PDF/language/ENGLISH/is-revised/true";
            headers.set("Authorization", "Bearer " + Static.NGB_TOKEN);
            try {
                response = restTemplate.exchange(url, HttpMethod.GET, request, byte[].class);
                if(new File(Static.FolderPath+"\\"+folderName).mkdirs()) {
                    fos = new FileOutputStream(Static.FolderPath + folderName + "\\"+ excelDTO.getConsumerNo()+"_"+excelDTO.getBillMonth()+ ".pdf");
                    fos.write((byte[]) response.getBody());
                    File file = new File(Static.FolderPath + folderName + "\\"+ excelDTO.getConsumerNo()+"_"+excelDTO.getBillMonth()+".pdf");
                    obj.addSource(file);
                    fos.close();
                    fos.flush();
                }
                else
                {
                    logger.error(Static.FolderPath+"\\"+folderName+" can't be created or already exist");
                    fos = new FileOutputStream(Static.FolderPath + folderName + "\\" + excelDTO.getConsumerNo()+"_"+excelDTO.getBillMonth()+ ".pdf");
                    fos.write((byte[]) response.getBody());
                    File file = new File(Static.FolderPath + folderName + "\\"+ excelDTO.getConsumerNo() +"_"+excelDTO.getBillMonth()+".pdf");
                    obj.addSource(file);
                    fos.close();
                    fos.flush();
                }
            }
            catch (Exception e) {
                logger.error(excelDTO.getConsumerNo() +": "+ e.getMessage()+Static.sdf.format(new Date(System.currentTimeMillis())));
                recordsUpdated.setFailureRecords(recordsUpdated.getFailureRecords()+1);
                continue;
            }
            if (response != null && response.getStatusCode()==HttpStatus.OK && response.hasBody() && response.getBody() != null) {
                recordsUpdated.setSuccessRecords(recordsUpdated.getSuccessRecords()+1);
                logger.info(excelDTO.getConsumerNo() +":" +response.getStatusCode() + response.getBody()+Static.sdf.format(new Date(System.currentTimeMillis())));
            }
        }

        new File(Static.FolderPath+"\\"+"MergedPDF"+"\\"+folderName+"\\").mkdirs();
        obj.setDestinationFileName(Static.FolderPath+"\\"+"MergedPDF\\"+folderName+"\\"+fileBaseName+".pdf");
        try {
            obj.mergeDocuments();
        } catch (IOException e) {
            e.printStackTrace();
        }

        new File(Static.ZipFolderPath+"\\"+folderName).mkdirs();
        File mergedFile=new File(Static.FolderPath+"\\"+"MergedPDF\\"+folderName+"\\"+fileBaseName+".pdf");
        ZipUtil.packEntry(mergedFile, new File(Static.ZipFolderPath+"\\"+folderName+"\\"+fileBaseName+".zip"));
    /*    try {
            FileUtils.deleteDirectory(new File(Static.FolderPath));
        } catch (IOException e) {
            e.printStackTrace();
        } */
        return new ResponseEntity(recordsUpdated,HttpStatus.OK);
    }
}
