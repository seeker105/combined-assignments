package com.cooksys.ftd.assignments.socket;

import java.io.IOException;
import java.net.Socket;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.cooksys.ftd.assignments.socket.model.Config;
import com.cooksys.ftd.assignments.socket.model.RemoteConfig;
import com.cooksys.ftd.assignments.socket.model.Student;

public class Client extends Utils {

    /**
     * The client should load a {@link com.cooksys.ftd.assignments.socket.model.Config} object from the
     * <project-root>/config/config.xml path, using the "port" and "host" properties of the embedded
     * {@link com.cooksys.ftd.assignments.socket.model.RemoteConfig} object to create a socket that connects to
     * a {@link Server} listening on the given host and port.
     *
     * The client should expect the server to send a {@link com.cooksys.ftd.assignments.socket.model.Student} object
     * over the socket as xml, and should unmarshal that object before printing its details to the console.
     */
    public static void main(String[] args) {
       	Socket socket = null;
    	Unmarshaller unMarshaller = null;
    	Config config = null;
    	RemoteConfig remoteConfig=null;
    	Student student = null;
    	JAXBContext context = createJAXBContext();
    	
    	try {
			unMarshaller = context.createUnmarshaller();
			config = loadConfig("C:/Users/ftd-17/code/combined-assignments/4-socket-io-serialization/config/config.xml", context);
			remoteConfig = config.getRemote();

		} catch (JAXBException e) {
			e.printStackTrace();
			System.out.println("Failed to create Unmarshaller");
		}

    	
		try {
			if (remoteConfig == null){
				throw new Exception();
			}
			socket = new Socket(remoteConfig.getHost(), remoteConfig.getPort());
		} catch (IOException e) {
			System.out.println("Failed to create Client to Server socket");
			e.printStackTrace();
		} catch (Exception e){
			System.out.println("Failed to create RemoteConfig");
		}
    	
		try {
			student = (Student) unMarshaller.unmarshal(socket.getInputStream());
		} catch (IOException e1) {
			System.out.println("IO Error. Connection failed from Server");
			e1.printStackTrace();
		} catch (JAXBException e) {
			System.out.println("JAXB Error. Failed to unmarshal the student data");
			e.printStackTrace();		
		} finally {
			try {
				if (socket != null) socket.close();
			} catch (IOException e) {
				System.out.println("Failed to close the Server/Client Socket");
				e.printStackTrace();
			}
		}
		System.out.println(student);
    	}
}
