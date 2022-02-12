package com.JayPi4c.RobbiSimulator.controller.tutor;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ITutor implementation to handle the RMI invocations.
 * 
 * @author Jonas Pohl
 *
 */
public class Tutor extends UnicastRemoteObject implements ITutor {

	private static final long serialVersionUID = 4722167139215525516L;

	private static final Logger logger = LoggerFactory.getLogger(Tutor.class);

	private transient Queue<Request> requests;
	private int currentID = 0;
	private Map<Integer, Answer> answers;

	/**
	 * Create a new tutor-instance
	 * 
	 * @throws RemoteException thrown on connection errors
	 */
	public Tutor() throws RemoteException {
		super();
		requests = new LinkedList<>();
		answers = new HashMap<>();
	}

	@Override
	public synchronized int sendRequest(String code, String territory) throws RemoteException {
		requests.add(new Request(currentID, code, territory));
		logger.debug("Added new request with id {}.", currentID);
		return currentID++;
	}

	@Override
	public synchronized Answer getAnswer(int id) throws RemoteException {
		if (answers.containsKey(id)) {
			logger.debug("Returning answer with id {}.", id);
			return answers.remove(id);
		}
		logger.debug("Could not find answer with id {}.", id);
		return null;
	}

	/**
	 * Returns an optional with a new request if one is available.
	 * 
	 * @return Optional of the oldest request or an empty Optional.
	 */
	public Optional<Request> getNewRequest() {
		if (requests.isEmpty())
			return Optional.empty();
		return Optional.ofNullable(requests.poll());
	}

	/**
	 * Adds an answer with the corresponding id in the list of done answers.
	 * 
	 * @param id     the id for the answer
	 * @param answer the actual answer
	 */
	public void setAnswer(int id, Answer answer) {
		answers.put(id, answer);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(answers, currentID, requests);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tutor other = (Tutor) obj;
		return Objects.equals(answers, other.answers) && currentID == other.currentID
				&& Objects.equals(requests, other.requests);
	}

}
