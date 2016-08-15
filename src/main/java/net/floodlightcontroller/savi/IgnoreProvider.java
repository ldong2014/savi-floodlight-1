package net.floodlightcontroller.savi;

import java.util.ArrayList;
import java.util.List;

import org.projectfloodlight.openflow.protocol.OFFactories;
import org.projectfloodlight.openflow.protocol.OFVersion;
import org.projectfloodlight.openflow.protocol.action.OFAction;
import org.projectfloodlight.openflow.protocol.match.Match;
import org.projectfloodlight.openflow.protocol.match.MatchField;
import org.projectfloodlight.openflow.types.EthType;
import org.projectfloodlight.openflow.types.IPv4Address;
import org.projectfloodlight.openflow.types.IPv6Address;
import org.projectfloodlight.openflow.types.MacAddress;
import org.projectfloodlight.openflow.types.TableId;

import net.floodlightcontroller.devicemanager.SwitchPort;
import net.floodlightcontroller.forwarding.Forwarding;
import net.floodlightcontroller.packet.ARP;
import net.floodlightcontroller.packet.Ethernet;
import net.floodlightcontroller.packet.IPv4;
import net.floodlightcontroller.packet.IPv6;
import net.floodlightcontroller.routing.IRoutingDecision.RoutingAction;

public class IgnoreProvider extends ReactiveProvider {
	@Override
	protected RoutingAction process(SwitchPort switchPort, Ethernet eth) {
		// TODO Auto-generated method stub
		
		MacAddress macAddress = eth.getSourceMACAddress();
		
		if(securityPort.contains(switchPort) || !topologyService.isEdge(switchPort.getSwitchDPID(), switchPort.getPort())) {
			return RoutingAction.FORWARD_OR_FLOOD;
		}
		
		if(eth.getEtherType() == EthType.IPv4) {
			IPv4 ipv4 = (IPv4)eth.getPayload();
			IPv4Address address = ipv4.getSourceAddress();
			
			if(this.manager.check(switchPort, macAddress, address)) {
				return RoutingAction.FORWARD_OR_FLOOD;
			}
			else {
				return RoutingAction.NONE;
			}
			
			
		}
		else if(eth.getEtherType() == EthType.IPv6) {
			IPv6 ipv6 = (IPv6)eth.getPayload();
			IPv6Address address = ipv6.getSourceAddress();
			
			if(this.manager.check(switchPort, macAddress, address)) {
				return RoutingAction.FORWARD_OR_FLOOD;
			}
			else {
				return RoutingAction.NONE;
			}
			
		}
		else if(eth.getEtherType() == EthType.ARP) {
			ARP arp = (ARP)eth.getPayload();
			IPv4Address address = arp.getSenderProtocolAddress();
			
			if(this.manager.check(switchPort, address)) {
				return RoutingAction.FORWARD_OR_FLOOD;
			}
			else {
				return RoutingAction.NONE;
			}
			
		}
		
		return RoutingAction.NONE;
	}
}
