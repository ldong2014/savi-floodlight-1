package net.floodlightcontroller.savi.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.projectfloodlight.openflow.types.DatapathId;
import org.projectfloodlight.openflow.types.OFPort;
import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import org.restlet.routing.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.MappingJsonFactory;

import net.floodlightcontroller.devicemanager.SwitchPort;
import net.floodlightcontroller.restserver.RestletRoutable;
import net.floodlightcontroller.savi.binding.Binding;
import net.floodlightcontroller.savi.service.SAVIProviderService;

public class SAVIRest extends ServerResource {
	protected static Logger log = LoggerFactory.getLogger(SAVIRest.class);
	
	public static final String ADD_SECURITY_PORT_TYPE = "add_security_port";
	public static final String DEL_SECURITY_PORT_TYPE = "del_security_port";
	public static final String GET_SECURITY_PORT_TYPE = "get_security_port";
	public static final String GET_BINDING_TYPE = "get_binding";
	public static final String TYPE = "type";
	public static final String SWITCH_DPID = "dpid";
	public static final String PORT_NUM = "port";
	public static final String MAC = "mac";
	public static final String IP = "ip";
	public static final String IPv6 = "ipv6";
	
	public class SAVIRoutable implements RestletRoutable{
		@Override
		public Restlet getRestlet(Context context) {
			// TODO Auto-generated method stub
			Router router = new Router(context);
			router.attach("/config", SAVIRest.class);
			return router;
		}

		@Override
		public String basePath() {
			// TODO Auto-generated method stub
			return "/savi";
		}	
	}
	
	@Post
	public String post(String json){

		Map<String, String> jsonMap = jsonToStringMap(json);
		if(jsonMap == null){
			return "{FAIL}";
		}
		switch(jsonMap.get(TYPE)){
		case ADD_SECURITY_PORT_TYPE:
			break;
		case DEL_SECURITY_PORT_TYPE:
			break;
		case GET_SECURITY_PORT_TYPE:
			break;
		case GET_BINDING_TYPE:
			break;
		}
		return null;
		
	}
	
	protected synchronized String doAddSecurityPort(Map<String, String> jsonMap){
		SAVIProviderService providerService = (SAVIProviderService)getContext().getAttributes().get(SAVIProviderService.class.getCanonicalName());
		if(jsonMap.containsKey(SWITCH_DPID)&&jsonMap.containsKey(PORT_NUM)){
			DatapathId dpid = DatapathId.of(jsonMap.get(SWITCH_DPID));
			OFPort port = OFPort.ofInt(Integer.valueOf(jsonMap.get(PORT_NUM)));
			providerService.addSecurityPort(new SwitchPort(dpid,port));
		}
		return "{SUCCESS}";
	}
	
	protected synchronized String doDelSecurityPort(Map<String, String> jsonMap){	
		SAVIProviderService providerService = (SAVIProviderService)getContext().getAttributes().get(SAVIProviderService.class.getCanonicalName());
		if(jsonMap.containsKey(SWITCH_DPID)&&jsonMap.containsKey(PORT_NUM)){
			DatapathId dpid = DatapathId.of(jsonMap.get(SWITCH_DPID));
			OFPort port = OFPort.ofInt(Integer.valueOf(jsonMap.get(PORT_NUM)));
			providerService.delSecurityPort(new SwitchPort(dpid,port));
		}
		return "{SUCCESS}";
	}
	protected synchronized String doGetBinding(){
		SAVIProviderService providerService = (SAVIProviderService)getContext().getAttributes().get(SAVIProviderService.class.getCanonicalName());
		List<Binding<?>> bindings = providerService.getBindings();
		int i = 0;
		String ret = "[";
		for(;i<bindings.size()-1;i++){
			ret += bindings.get(i).toString()+",";
		}
		if(i>0){
			ret += bindings.get(i).toString();
		}
		
		return ret;
	}
	
	protected synchronized String doGetSecurityPort(){
		SAVIProviderService providerService = (SAVIProviderService)getContext().getAttributes().get(SAVIProviderService.class.getCanonicalName());
		int i = 0;
		String ret = "[";
		SwitchPort[] set = (SwitchPort[]) providerService.getSecurityPorts().toArray();
		for(;i<set.length-1;i++){
			ret += set[i].toString() + ","; 
		}
		if(i>0){
			ret += set[i].toString();
		}
		ret += "]";
		return ret;
	}
	@SuppressWarnings("deprecation")
	public Map<String, String> jsonToStringMap(String json){
		Map<String, String> jsonMap = new HashMap<>();
		JsonParser jp;
		MappingJsonFactory f = new MappingJsonFactory();
		try {
			jp = f.createJsonParser(json);
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
		try{
			jp.nextToken();
			if(jp.getCurrentToken() != JsonToken.START_OBJECT){
				return null;
			}
			while(jp.nextToken()!=JsonToken.END_OBJECT){
				String name = jp.getCurrentName();
				jp.nextToken();
				jsonMap.put(name, jp.getCurrentName());
			}
			return jsonMap;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
}
