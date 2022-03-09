package com.mppkvvcl.telegrambotadmin.service;

import com.mppkvvcl.telegrambotadmin.repository.TelegramRepository;
import com.mppkvvcl.telegrambotadmin.utility.LoggerUtil;
import com.mppkvvcl.telegrambotadmin.utility.Static;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class RestTemplateService {
	private Logger logger = LoggerUtil.getLogger(RestTemplateService.class);

	@Autowired
	TelegramRepository telegramRepository;

	public ResponseEntity<?> getNGBInfo(String consumerNo) throws HttpClientErrorException {
		logger.info("getNGBInfo in RestTemplateService");
		ResponseEntity response = null;
		RestTemplate restTemplate = new RestTemplate();
		System.out.println("Inside restemplate service");
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.set("Authorization", "Bearer " + Static.CMI_TOKEN);
		HttpEntity<Object> request = new HttpEntity<>(headers);
		System.out.println(consumerNo);
		try {
			response = restTemplate.exchange(Static.CMI_INFO_URL + consumerNo, HttpMethod.GET, request, List.class);
		} catch (HttpStatusCodeException e) {

			return ResponseEntity.status(e.getRawStatusCode())
					.body(e.getResponseBodyAsString());
		}

		if (response != null && response.getStatusCode() == HttpStatus.OK && response.hasBody()
				&& response.getBody() != null) {
			return new ResponseEntity<>(response.getBody(), response.getStatusCode());
		}

		return response;
	}


	public List<Map> getNGBPayment(String consumerNo, String sortBy, String sortOrder, int pageNumber, int pageSize) throws HttpClientErrorException {
		logger.info("getNGBPayment in RestTemplateService");
		ResponseEntity<List> response = null;
		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.set("Authorization", "Bearer " + Static.CMI_TOKEN);
		HttpEntity<Object> request = new HttpEntity<>(headers);
		String url = Static.CMI_PAYMENT_URl + consumerNo + "&sortBy=" + sortBy + "&sortOrder=" + sortOrder + "&pageNumber=" + pageNumber + "&pageSize=" + pageSize;
		response = restTemplate.exchange(url, HttpMethod.GET, request, List.class);

		if (response != null && response.getStatusCode() == HttpStatus.OK && response.hasBody()
				&& response.getBody() != null) {
			return response.getBody();

		}

		return (List<Map>) response;
	}

	public byte[] getNGBBillPDFbyNgbBillId(String ngbBillId) throws HttpClientErrorException {
		logger.info("getNGBBillPDFbyNgbBillId in RestTemplateService");
		ResponseEntity<byte[]> response = null;
		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.set("Authorization", "Bearer " + Static.CMI_TOKEN);
		HttpEntity<Object> request = new HttpEntity<>(headers);
		String url = Static.CMI_PDF_BILL+ ngbBillId ;
		response = restTemplate.exchange(url, HttpMethod.GET, request, byte[].class);
		if (response != null && response.getStatusCode() == HttpStatus.OK && response.hasBody()
				&& response.getBody() != null) {
			return response.getBody();

		}
		return response.getBody();
	}

	public byte[] getNGBPassbookByConsumerNo(String consumerNO) throws HttpClientErrorException {
		logger.info("getNGBBillPDFbyNgbBillId in RestTemplateService");
		ResponseEntity<byte[]> response = null;
		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.set("Authorization", "Bearer " + Static.CMI_TOKEN);
		HttpEntity<Object> request = new HttpEntity<>(headers);
		String url = Static.CMI_PASSBOOK+ consumerNO ;
		response = restTemplate.exchange(url, HttpMethod.GET, request, byte[].class);
		if (response != null && response.getStatusCode() == HttpStatus.OK && response.hasBody()
				&& response.getBody() != null) {
			return response.getBody();

		}
		return response.getBody();
	}
}




