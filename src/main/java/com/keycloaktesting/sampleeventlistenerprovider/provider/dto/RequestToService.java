package com.keycloaktesting.sampleeventlistenerprovider.provider.dto;



import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public abstract class RequestToService {
	
	protected BodyPublisher toBodyRequest() throws JsonProcessingException{
		ObjectMapper mapper = new ObjectMapper();
		return BodyPublishers.ofString(mapper.writeValueAsString(this));
	}
	public String send() {
		System.out.println("prepare sending to "+this.getUri());
    	try {
    		HttpRequest request = HttpRequest.newBuilder()
    				.uri(this.getUri())
    				.header("Content-Type", "application/json")
    				.POST(this.toBodyRequest())
    				.build();
    		System.out.println("Sending:\n" + request.toString());
    		HttpClient client = HttpClient.newBuilder().build();
    		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    		System.out.println("Response status: " + response.statusCode());
    		return response.body();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch(InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return "";
	}
	public abstract URI getUri();
}
