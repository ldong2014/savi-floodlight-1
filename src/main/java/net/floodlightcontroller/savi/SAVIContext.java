package net.floodlightcontroller.savi;

import org.projectfloodlight.openflow.protocol.OFFactories;
import org.projectfloodlight.openflow.protocol.OFFactory;
import org.projectfloodlight.openflow.protocol.OFMessage;
import org.projectfloodlight.openflow.protocol.OFPacketIn;
import org.projectfloodlight.openflow.protocol.OFType;
import org.projectfloodlight.openflow.protocol.OFVersion;
import org.projectfloodlight.openflow.protocol.match.MatchField;
import org.projectfloodlight.openflow.types.DatapathId;
import org.projectfloodlight.openflow.types.OFPort;

import net.floodlightcontroller.core.IOFSwitch;
import net.floodlightcontroller.devicemanager.SwitchPort;
import net.floodlightcontroller.savi.service.SAVIProviderService;

public class SAVIContext {
	protected static OFFactory defaultFactory = OFFactories.getFactory(OFVersion.OF_13);
	
	protected SAVIProviderService provider;
	protected OFMessage msg;
	protected IOFSwitch sw;
	
	public SAVIContext(IOFSwitch sw, OFMessage msg, SAVIProviderService provider) {
		// TODO Auto-generated constructor stub
		this.provider = provider;
		this.sw = sw;
		this.msg = msg;
	}
	
	public OFFactory getOFFactory() {
		try {
			return provider.getOFFactory();
		}
		catch(NullPointerException e){
			return defaultFactory;
		}
	}
	
	public OFType getContextType() {
		try {
			return msg.getType();
		}
		catch(NullPointerException e) {
			return null;
		}
	}
	
	public DatapathId getDatapathId() {
		try {
			return sw.getId();
		}
		catch(NullPointerException e) {
			return null;
		}
	}
	
	public SwitchPort getInPort() {
		/*
		 * Only PACKET_IN message has 'InPort' attribute.
		 */
		try {
			if(msg.getType() == OFType.PACKET_IN) {
				OFPacketIn pi = (OFPacketIn) msg;
				OFPort port = (pi.getVersion().compareTo(OFVersion.OF_12) < 0 ? pi.getInPort()
						: pi.getMatch().get(MatchField.IN_PORT));
				return new SwitchPort(sw.getId(), port);
			}
		}
		catch (NullPointerException e) {
			// TODO: handle exception
			return null;
		}
		return null;
	}
 	
}
