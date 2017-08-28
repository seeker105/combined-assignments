package com.cooksys.ftd.assignments.socket;

import com.cooksys.ftd.assignments.socket.model.Config;
import com.cooksys.ftd.assignments.socket.model.LocalConfig;
import com.cooksys.ftd.assignments.socket.model.Student;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class Server extends Utils {

    /**
     * Reads a {@link Student} object from the given file path
     *
     * @param studentFilePath the file path from which to read the student config file
     * @param jaxb the JAXB context to use during unmarshalling
     * @return a {@link Student} object unmarshalled from the given file path
     */
    public static Student loadStudent(String studentFilePath, JAXBContext jaxb) {
        return null; // TODO
    }

    /**
     * The server should load a {@link com.cooksys.ftd.assignments.socket.model.Config} object from the
     * <project-root>/config/config.xml path, using the "port" property of the embedded
     * {@link com.cooksys.ftd.assignments.socket.model.LocalConfig} object to create a server socket that
     * listens for connections on the configured port.
     *
     * Upon receiving a connection, the server should unmarshal a {@link Student} object from a file location
     * specified by the config's "studentFilePath" property. It should then re-marshal the object to xml over the
     * socket's output stream, sending the object to the client.
     *
     * Following this transaction, the server may shut down or listen for more connections.
     */
    public static void main(String[] args) {
    	ServerSocket servSocket = null;
    	Socket socket = null;
    	Student student = null;
    	Unmarshaller unMarshaller = null;
    	Marshaller marshaller = null;
    	Config config = null;
    	LocalConfig localConfig=null;
    	JAXBContext context = createJAXBContext();
    	
    	try {
			unMarshaller = context.createUnmarshaller();
			marshaller = context.createMarshaller();
			config = loadConfig("C:/Users/ftd-17/code/combined-assignments/4-socket-io-serialization/config/config.xml", context);
			localConfig = config.getLocal();
		} catch (JAXBException e) {
			e.printStackTrace();
			System.out.println("Failed to create Marshaller or Unmarshaller");
		}
    	
    	
    	try {
			if (localConfig == null) throw new Exception();
			servSocket = new ServerSocket(localConfig.getPort());
			System.out.println("Waiting for connection...");
			socket = servSocket.accept();
			System.out.println("Connected!");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e){
			System.out.println("Failed to create LocalConfig");
		} finally {
			try {
				if (servSocket != null) servSocket.close();
			} catch (IOException e) {
				System.out.println("Failed to close ServerSocket");
				e.printStackTrace();
			}
		}
    	
		// Client connects...
		
    	// Read the student file
		File studentFile = new File(config.getStudentFilePath());
		
		try {
			student = (Student) unMarshaller.unmarshal(studentFile);
		} catch (JAXBException e) {
			System.out.println("Failed to unmarshall Student object");
			e.printStackTrace();
		}
		
		// Re-marshall the Student data and send it to the Client over the Socket
		try {
			marshaller.marshal(student, socket.getOutputStream());
		} catch (IOException e) {
			System.out.println("Failed to create Client's PrintWriter");
			e.printStackTrace();
		} catch (JAXBException j) {
			System.out.println("Failed to Marshal the Student object to the Client");
			j.printStackTrace();
		} finally {
			try {
				if (socket != null) socket.close();
			} catch (IOException e) {
				System.out.println("Failed to close outgoing Socket");
				e.printStackTrace();
			}
		}    	
    }
}
