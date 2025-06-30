package com.keycloaktesting.sampleeventlistenerprovider.provider.dto;

import java.net.URI;

import com.keycloaktesting.sampleeventlistenerprovider.provider.service.GlobalParams;

public class DeleteTupleRequest extends RequestToService{
	private String user;
	private String relation;
	private String object;
	
	public static DeleteTupleRequest userLeaveGroup(String userId, String groupId) {
		return new DeleteTupleRequest("user:"+userId,"member","group"+groupId);
	}

	public static DeleteTupleRequest deleteRoleFromGroup(String roleId, String groupId) {
		return new DeleteTupleRequest("role:"+roleId,"member","group:"+groupId);
	}

	public static DeleteTupleRequest deleteRoleFromUser(String userId, String groupId) {
		return new DeleteTupleRequest("user:"+userId,"member","group:"+groupId);
	}
	
	public DeleteTupleRequest(String user, String relation, String object) {
		this.user = user;
		this.relation = relation;
		this.object = object;
	}
	public String toString() {
		return "deleting: "+getUser()+"|"+getRelation()+"|"+getObject();
	}
	public String getUser() {
		return user;
	}
	public String getRelation() {
		return relation;
	}
	public String getObject() {
		return object;
	}

	@Override
	public URI getUri() {
		return URI.create(GlobalParams.OpenFgaControlerURL()+"/remTuple");
	}
}
