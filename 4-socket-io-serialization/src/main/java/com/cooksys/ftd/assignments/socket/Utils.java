package com.cooksys.ftd.assignments.socket;

import com.cooksys.ftd.assignments.socket.model.Config;
import com.cooksys.ftd.assignments.socket.model.Student;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 * Shared static methods to be used by both the {@link Client} and {@link Server} classes.
 */
public class Utils {
    /**
     * @return a {@link JAXBContext} initialized with the classes in the
     * com.cooksys.socket.assignment.model package
     */
    public static JAXBContext createJAXBContext() {
    	try {
			return JAXBContext.newInstance(Config.class, Student.class, Client.class);
		} catch (JAXBException e) {
			e.printStackTrace();
			System.out.println("Failed to Create JAXBContext in class Utils.java");
		}
    	return null;
    }

    /**
     * Reads a {@link Config} object from the given file path.
     *
     * @param configFilePath the file path to the config.xml file
     * @param jaxb the JAXBContext to use
     * @return a {@link Config} object that was read from the config.xml file
     * @throws  
     */
    public static Config loadConfig(String configFilePath, JAXBContext jaxb) {
    	File configFile = new File(configFilePath);
    	Config c = null;
    	Unmarshaller unMarshaller = null;
		try {
			unMarshaller = jaxb.createUnmarshaller();
		} catch (JAXBException e) {
			System.out.println("Failed to create Unmarshaller in Util class");
			e.printStackTrace();
		}
    	try {
    		if (unMarshaller == null) {
    			System.out.println("In Utils class Unmarshaller was set to null");
    		}
			c = (Config) unMarshaller.unmarshal(configFile);
		} catch (JAXBException e) {
			System.out.println("Failed to unmarshall the configFile in Utils class");
			e.printStackTrace();
		}
    	return c;
    }
}
