package com.keycloaktesting.sampleeventlistenerprovider.provider.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.keycloak.Config.Scope;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventListenerProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

public class SampleEventListenerProviderFactory implements EventListenerProviderFactory {

 @Override
 public EventListenerProvider create(KeycloakSession session) {
  return new SampleEventListenerProvider();
 }

 @Override
 public void init(Scope config) {
	 
 }

 @Override
 public void postInit(KeycloakSessionFactory factory) {
	 System.out.println("pinging openfga controler");
	 HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(GlobalParams.OpenFgaControlerURL()+"/ping"))
				.header("Content-Type", "application/json")
				.build();
		HttpClient client = HttpClient.newBuilder().build();
		try {
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			System.out.println("Response status: " + response.statusCode());
		} catch (IOException | InterruptedException e) {
			System.out.println("Can't reach the openfga service");
			e.printStackTrace();
		}
	 
	 //factory.register(SampleEventListenerProvider.class);
 }

 @Override
 public void close() {
  
 }

 @Override
 public String getId() {
  return "sample_event_listener";
 }

}