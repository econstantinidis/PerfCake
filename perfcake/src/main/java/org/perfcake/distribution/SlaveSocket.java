package org.perfcake.distribution;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.perfcake.PerfCakeConst;
import org.perfcake.model.Scenario;

public class SlaveSocket {

	private static final Logger log = LogManager.getLogger(SlaveSocket.class);
	
	// instance
	private static SlaveSocket instance;
	
	private Socket sock;
	private InputStream iStream;
	private OutputStream oStream;
	
	public static SlaveSocket getInstance() {
		return instance;
	}
	
	public static void setupSlaveSocket(InetAddress masterAddress, int port) {
		log.info("Connecting to master " + masterAddress.getHostAddress() + ", port " + port);
		
		// FIXME locking
		
		try {
			instance = new SlaveSocket(masterAddress, port);
		} catch (IOException e) {
			log.fatal("Cannot connect to master", e);
			System.exit(PerfCakeConst.ERR_SLAVE_SOCKET);
		}
	}
	
	private SlaveSocket(InetAddress masterAddress, int port) throws IOException {
		this.sock = new Socket(masterAddress, port);
		
		this.iStream = sock.getInputStream();
		this.oStream = sock.getOutputStream();
	}
	
	public Scenario getScenarioModel() {
		ObjectInputStream in;
		try {
			in = new ObjectInputStream(iStream);
			return (Scenario) in.readObject();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public OutputStream getOutputStream()
	{
		return oStream;
	}
	
}
