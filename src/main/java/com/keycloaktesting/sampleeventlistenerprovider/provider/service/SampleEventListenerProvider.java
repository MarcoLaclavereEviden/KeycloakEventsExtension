package com.keycloaktesting.sampleeventlistenerprovider.provider.service;

import java.util.ArrayList;
import java.util.Map.Entry;

import org.json.*;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.events.admin.OperationType;

import com.keycloaktesting.sampleeventlistenerprovider.provider.dto.DeleteEntityRequest;
import com.keycloaktesting.sampleeventlistenerprovider.provider.dto.DeleteTupleRequest;
import com.keycloaktesting.sampleeventlistenerprovider.provider.dto.WriteTupleRequest;

public class SampleEventListenerProvider implements EventListenerProvider {
 
	 public SampleEventListenerProvider() {
	  
	 }

	@Override
    public void onEvent(Event event) {
		System.out.println("----- USER -----");
		System.out.println("Event Occurred:" + printEventContent(event));
    }

    @Override
    public void onEvent(AdminEvent adminEvent, boolean b) {
    	System.out.println("\n\n----- NEW EVENT -----");
        
	 	OperationType type = adminEvent.getOperationType();
	 	
	 	String[] splitPath = adminEvent.getResourcePath().split("/");
	 	
        switch(type) {
	        case ACTION:{
	        	System.out.println("Action type");
	        	break;
	        	
	        } case CREATE:{
	        	System.out.println("Create type");
	        	switch(splitPath[0].toLowerCase()) {
		        	case "users": {
		        		handleCreateEventUser(adminEvent);
		        		break;
		        	}
		        	case "groups":{
		        		handleCreateEventGroups(adminEvent);
		        		break;
		        	}
		        	case "roles":{
		        		handleCreateEventRoles(adminEvent);
		        		break;
		        	}
		        	
		        	default:{
		        		System.out.println("didn't recognize: "+splitPath[0]);
		        	}
	        	}
	        	break;
	        	
	        } case DELETE:{
	        	switch(splitPath[0].toLowerCase()) {
	        		case "users":{
	        			handleDeleteEventUser(adminEvent);
	        			break;
	        		}
	        		case "groups":{
	        			handleDeleteEventGroup(adminEvent);
	        			break;
	        		}
	        		case "roles", "roles-by-id":{
	        			handleDeleteEventRole(adminEvent);
	        			break;
	        		}
	        		
	        		default:{
	        			System.out.println("didn't recognize: "+splitPath[0]);
	        		}
	        	}
	        	break;
	        	
	        } case UPDATE:{
	        	System.out.println("Update type");
	        	break;
	        	
	        } default:{
	        	System.out.println("Default type");
	        	break;
	        }
        }
    }

    @Override
    public void close() {

    }
    private String printEventContent(Event event) {

        StringBuilder sb = new StringBuilder();

        sb.append("type=");
        sb.append(event.getType());

        sb.append(", realmId=");
        sb.append(event.getRealmId());

        sb.append(", clientId=");
        sb.append(event.getClientId());

        sb.append(", userId=");
        sb.append(event.getUserId());

        sb.append(", ipAddress=");
        sb.append(event.getIpAddress());

//        if (event.getError() != null) {
//            sb.append(", error=");
//            sb.append(event.getError());
//        }

        if (event.getDetails() != null) {

            for (Entry<String, String> e : event.getDetails().entrySet()) {
                sb.append(", ");
                sb.append(e.getKey());

                if (e.getValue() == null || e.getValue().indexOf(' ') == -1) {
                    sb.append("=");
                    sb.append(e.getValue());

                } else {
                    sb.append("='");
                    sb.append(e.getValue());
                    sb.append("'");

                }
            }
        }

        return sb.toString();

    }
    private String printEventContent(AdminEvent adminEvent) {

	 	OperationType type = adminEvent.getOperationType();
	 	
        StringBuilder sb = new StringBuilder();

        sb.append("operationType=");
        sb.append(type);

        sb.append(", realmId=");
        sb.append(adminEvent.getAuthDetails().getRealmId());

        sb.append(", clientId=");
        sb.append(adminEvent.getAuthDetails().getClientId());

        sb.append(", userId=");
        sb.append(adminEvent.getAuthDetails().getUserId());

        sb.append(", ipAddress=");
        sb.append(adminEvent.getAuthDetails().getIpAddress());
        
        sb.append(", Representation=");
        sb.append(adminEvent.getRepresentation());

        sb.append(", resourcePath=");
        sb.append(adminEvent.getResourcePath());
        String[] splitPath = adminEvent.getResourcePath().split("/");
        {
	        sb.append(" -> ");
	        
        	int num=1;
	        for(String value : splitPath) {
	        	sb.append("\n"+ num +" | "+value);
	        	num++;
	        }
	        sb.append("\n");
        }
        String splitRepresentation = adminEvent.getRepresentation();
        sb.append("Representation: "+splitRepresentation+"\n");
        
        if (adminEvent.getError() != null) {
            sb.append(", error=");
            sb.append(adminEvent.getError());

        }

        return sb.toString();
	}
    
	private void handleCreateEventUser(AdminEvent adminEvent) {
 	 	String[] splitResource = adminEvent.getResourcePath().split("/");
		 
 	 	String realmHost = adminEvent.getRealmId();
		 
		 String userId = splitResource[1];
		 
		 switch(splitResource.length) {
		 	case 2:{
		 		WriteTupleRequest writeRequest = WriteTupleRequest.createUser(userId,realmHost);
				System.out.println(writeRequest.toString());
	 			writeRequest.send();
		 		break;
		 	}
		 	
		 	case 4:{
		 		String subtype = splitResource[2].toLowerCase();
		 		switch(subtype) {
			 		case "users":{
			 			WriteTupleRequest writeRequest = WriteTupleRequest.createUser(userId, realmHost);
						System.out.println(writeRequest.toString());
			 			writeRequest.send();
			 			break;
			 		}
			 		case "groups":{

			 			WriteTupleRequest writeRequest = WriteTupleRequest.userJoinsGroup(userId,splitResource[3]);
						System.out.println(writeRequest.toString());
			 			writeRequest.send();
			 			break;
			 		}
			 		case "role-mappings":{
			 			JSONArray representation = new JSONArray(adminEvent.getRepresentation());
			 			ArrayList<String> rolesId = new ArrayList<String>();
			 			for(int i=0; i< representation.length();i++) {
			 				rolesId.add(representation.getJSONObject(i).getString("id"));
			 			}
			 			for(String roleId : rolesId) {
				 			WriteTupleRequest writeRequest = WriteTupleRequest.addRoleToUser(userId,roleId);
				 			System.out.println(writeRequest.toString());
				 			writeRequest.send();
			 			}
			 			break;
			 		}
			 		
			 		default:{
			 			System.out.println("3rd parameter not recognised. Found:'"+splitResource[2]);
			 			break;
			 		}
		 		}
		 		break;
		 	}
		 	
		 	default:{
		 		System.err.println("No entry for: "+splitResource.length+" parameters");
		 		break;
		 	}
		 }
	}
	private void handleCreateEventGroups(AdminEvent adminEvent) {
 		 String[] splitResource = adminEvent.getResourcePath().split("/");
					 	
 	 	 String realmHost = adminEvent.getRealmId();
		 String groupId = splitResource[1];
		 
		 switch(splitResource.length) {
		 	case 2: {
	 			WriteTupleRequest writeRequest = WriteTupleRequest.createGroup(groupId,realmHost);
				System.out.println(writeRequest.toString());
	 			writeRequest.send();
				break;
	 		 
		 	}
			case 3:{
				 if(splitResource[2].toLowerCase().equals("children")) {
					 JSONObject representation = new JSONObject(adminEvent.getRepresentation());
				 	 String childId = representation.getString("id");
		 			 WriteTupleRequest writeRequest = WriteTupleRequest.createSubGroup(childId, groupId);
					 System.out.println(writeRequest.toString());
		 			 writeRequest.send();
				 }
				 break;
			}
		 	case 4: {
				 switch(splitResource[2].toLowerCase()) {
					 case "role-mappings":{
						 System.out.println("link role to group?");
			 			JSONArray representation = new JSONArray(adminEvent.getRepresentation());
			 			ArrayList<String> rolesId = new ArrayList<String>();
			 			for(int i=0; i< representation.length();i++) {
			 				rolesId.add(representation.getJSONObject(i).getString("id"));
			 			}
			 			for(String roleId : rolesId) {
				 			WriteTupleRequest writeRequest = WriteTupleRequest.addRoleToGroup(groupId,roleId);
				 			System.out.println(writeRequest.toString());
				 			writeRequest.send();
			 			}
						 break;
					 }
				 	
				 	 default:{
				 		 System.out.println("3rd parameter not recognised, got: '"+splitResource[2].toLowerCase()+"'");
				 		 break;
				 	 }
				 }
				 break;
		 	}
		 	
		 	default: {
				 System.err.println("Too few parameters to continue: "+splitResource.length);
				 break;
		 	}
		 }
	}
	private void handleCreateEventRoles(AdminEvent adminEvent) {
		String[] splitResource = adminEvent.getResourcePath().split("/");
	 	
		String realmHost = adminEvent.getRealmId();
		String roleName = splitResource[1];
		
		switch(splitResource.length) {
			case 2:{
				JSONObject representation = new JSONObject(adminEvent.getRepresentation());
				String roleId = representation.getString("id");
				WriteTupleRequest writeRequest = WriteTupleRequest.createRole(roleId,realmHost);
				System.out.println(writeRequest.toString());
				writeRequest.send();
				break; 
			}
			case 4: {
				switch(splitResource[2].toLowerCase()) {
					default:{
						System.out.println("3rd parameter not recognised, got: '"+splitResource[2].toLowerCase()+"'");
						break;
					}
				}
				break;
			}
			
			default:{
				System.err.println("Too few parameters to continue: " + splitResource.length);
				return;
			}
		}
	}
	 
	private void handleDeleteEventUser(AdminEvent adminEvent) {
		System.out.println("event delete from user");
		String[] splitResource = adminEvent.getResourcePath().split("/");
	 	
		String realmHost = adminEvent.getRealmId();
		String userId = splitResource[1];
		
		switch(splitResource.length) {
			case 2:{
				DeleteEntityRequest deleteRequest = DeleteEntityRequest.deleteUser(userId);
				System.out.println(deleteRequest.toString());
				deleteRequest.send();
				break;
			}
			case 4:{
				switch(splitResource[2]) {
					case "groups":{
						DeleteTupleRequest deleteRequest = DeleteTupleRequest.userLeaveGroup(userId,splitResource[3]);
						System.out.println(deleteRequest.toString());
						deleteRequest.send();
						break;
					}
					case "role-mapping":{
						JSONArray representation = new JSONArray(adminEvent.getRepresentation());
			 			ArrayList<String> rolesId = new ArrayList<String>();
			 			for(int i=0; i< representation.length();i++) {
			 				rolesId.add(representation.getJSONObject(i).getString("id"));
			 			}
			 			for(String roleId : rolesId) {
				 			DeleteTupleRequest deleteRequest = DeleteTupleRequest.deleteRoleFromUser(userId,roleId);
				 			System.out.println(deleteRequest.toString());
				 			deleteRequest.send();
			 			}
						break;
					}
					default:{
						System.err.println("3rd argument unknown, got: "+splitResource[2]);
						break;
					}
				}
				break;
			}
			default:{
				System.err.println("Number of parameters not recognised: " + splitResource.length);
				return;
			}
		}
	}
	private void handleDeleteEventGroup(AdminEvent adminEvent) {
		System.out.println("event delete from user");
		String[] splitResource = adminEvent.getResourcePath().split("/");
	 	
		String realmHost = adminEvent.getRealmId();
		String groupId = splitResource[1];
		
		switch(splitResource.length) {
			case 2: {
				DeleteEntityRequest deleteRequest = DeleteEntityRequest.deleteGroup(groupId);
				System.out.println(deleteRequest.toString());
				deleteRequest.send();
				break;
			}
			case 4: {
				switch(splitResource[2].toLowerCase()) {
					case "role-mappings": {
						JSONArray representation = new JSONArray(adminEvent.getRepresentation());
			 			ArrayList<String> rolesId = new ArrayList<String>();
			 			for(int i=0; i< representation.length();i++) {
			 				rolesId.add(representation.getJSONObject(i).getString("id"));
			 			}
			 			for(String roleId : rolesId) {
				 			DeleteTupleRequest deleteRequest = DeleteTupleRequest.deleteRoleFromGroup(groupId,roleId);
				 			System.out.println(deleteRequest.toString());
				 			deleteRequest.send();
			 			}
						break;
					}
					default:{
						System.err.println("3rd argument unknown, got: "+splitResource[2]);
					}
				}
				break;
			}
			default:{
				System.err.println("Number of parameters not recognised: " + splitResource.length);
				return;
			}
		}
	}
	private void handleDeleteEventRole(AdminEvent adminEvent) {
		System.out.println("event delete from user");
		String[] splitResource = adminEvent.getResourcePath().split("/");
	 	
		String realmHost = adminEvent.getRealmId();
		
		switch(splitResource.length) {
			case 2: {
				DeleteEntityRequest deleteRequest = DeleteEntityRequest.deleteRole(splitResource[1]);
				System.out.println(deleteRequest.toString());
				deleteRequest.send();
				break;
			}
			default:{
				System.err.println("Number of parameters not recognised: " + splitResource.length);
				return;
			}
		}
	}
}