package com.keycloaktesting.sampleeventlistenerprovider.provider.dto;

import java.net.URI;

import com.keycloaktesting.sampleeventlistenerprovider.provider.service.GlobalParams;

public class DeleteEntityRequest extends RequestToService{
	private String entity;
	
	public static DeleteEntityRequest deleteUser(String userId) {
		return new DeleteEntityRequest("user:"+userId);
	}
	public static DeleteEntityRequest deleteGroup(String groupId) {
		return new DeleteEntityRequest("group:"+groupId);
	}
	public static DeleteEntityRequest deleteRole(String roleId) {
		return new DeleteEntityRequest("role:"+roleId);
	}
	
	public DeleteEntityRequest(String entity) {
		this.entity = entity;
	}
	
	public String getEntity() {
		return entity;
	}

	public String toString() {
		return "deleting all: "+getEntity();
	}
	@Override
	public URI getUri() {
		return URI.create(GlobalParams.OpenFgaControlerURL()+"/remEntity");
	}
}
