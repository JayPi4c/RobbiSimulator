package com.JayPi4c.RobbiSimulator.controller.tutor;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface for RMI communication
 * 
 * @author Jonas Pohl
 *
 */
public interface ITutor extends Remote {

	/**
	 * Sends a request of code and territory to the tutor and returns the id of the
	 * request at the tutors instance.
	 * 
	 * @param code      the requests code
	 * @param territory the requests territory
	 * @return the requests id
	 * @throws RemoteException thrown on connection errors
	 */
	public int sendRequest(String code, String territory) throws RemoteException;

	/**
	 * Fetches the answer for the given id. The answer will be null, if no answer is
	 * set.
	 * 
	 * @param id the id for the fetched answer
	 * @return the answer for the id or null, if no answer is set
	 * @throws RemoteException thrown on connection errors
	 */
	public Answer getAnswer(int id) throws RemoteException;

}
