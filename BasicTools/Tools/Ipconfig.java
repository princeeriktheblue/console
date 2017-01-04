/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tools;


import console.Command;
import console.commands.exceptions.UnknownErrorException;
import console.display.Console;
import console.exceptions.EException;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Enumeration;

/**
 *
 * @author Erik Belluomini
 */
public class Ipconfig extends Command
{
	private final static String[] help = 
	{
		"Needs improvement"
	};
	
    public Ipconfig(Console parent)
    {
        super("ipconfig", parent, null, help);
    }

    @Override
    public synchronized void execute(String[] args) throws EException 
    {
        if(args.length == 0)
        {
            try
            {
                InetAddress address = InetAddress.getLocalHost();
                String conicalHost = address.getCanonicalHostName();
                conicalHost = conicalHost.substring(conicalHost.indexOf('.')+1);
                console.output("Connection-specific DNS Suffix: " + conicalHost);
                console.output("Network Name: " + address.getHostName());
                console.output("Local IPv4 Address: " + address.getHostAddress());
                console.output("Default Gateway: Working on it");
            }
            catch(UnknownHostException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            Enumeration<NetworkInterface> interfaces;
	        try
	        {
	            interfaces = NetworkInterface.getNetworkInterfaces();
	            console.output("");
	            showData(interfaces);
	        }
	        catch(Exception e)
	        {
	            throw new UnknownErrorException("There was a problem receiving data from network interfaces");
	        }
        }
    }
    
    private synchronized void showData(Enumeration<NetworkInterface> interfaces) throws SocketException
    {
    	for(NetworkInterface net: Collections.list(interfaces))
        {
            if(!net.isLoopback() && !net.isVirtual())
            {
            	console.output(net.getDisplayName() + " : " + net.getName());
                Enumeration<InetAddress> addresses = net.getInetAddresses();
                for(InetAddress e: Collections.list(addresses))
                {
                    if(!e.isLoopbackAddress())
                    {
                        if(e.isMCNodeLocal())
                            console.output("Node: "+e.getHostAddress());
                        else if(e.isMulticastAddress())
                        	console.output("Multicast: "+e.getHostAddress());
                        else
                        	console.output(e.getHostAddress());
                    }
                }
                console.output("");
            }
        }
    }
}
