package com.keycloaktesting.sampleeventlistenerprovider.provider.dto;

import java.net.URI;

import com.keycloaktesting.sampleeventlistenerprovider.provider.service.GlobalParams;

public class WriteTupleRequest extends RequestToService{
	private static URI URL = GlobalParams.OpenFgaControlerURL();
	
	private String user;
	private String relation;
	private String object;
	
	// User related
	public static WriteTupleRequest createUser(String userId, String realmId) {
		return new WriteTupleRequest("user:"+userId,"member","realm:"+realmId);
	}
	public static WriteTupleRequest userJoinsGroup(String userId, String groupId) {
		return new WriteTupleRequest("user:"+userId,"member","group:"+groupId);
	}
	public static WriteTupleRequest addRoleToUser(String userId, String roleId) {
		return new WriteTupleRequest("user:"+userId,"member","role:"+roleId);
	}
	
	// Group related
	public static WriteTupleRequest createGroup(String groupId, String realmId) {
		return new WriteTupleRequest("group:"+groupId,"group","realm:"+realmId);
	}
	public static WriteTupleRequest createSubGroup(String childId, String parentId) {
		return new WriteTupleRequest("group:"+childId,"child","group:"+parentId);
	}
	
	// Role
	public static WriteTupleRequest createRole(String roleName, String realmId) {
		return new WriteTupleRequest("role:"+roleName,"role","realm:"+realmId);
	}
	public static WriteTupleRequest addRoleToGroup(String groupId, String roleId) {
		return new WriteTupleRequest("group:"+groupId,"group","role:"+roleId);
	}
	
	public WriteTupleRequest(String user, String relation, String object) {
		this.user = user;
		this.relation = relation;
		this.object = object;
	}
	public String toString() {
		return "creating: "+getUser()+"|"+getRelation()+"|"+getObject();
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
		return URI.create(GlobalParams.OpenFgaControlerURL()+"/addTuple");
	}

}
