package com.JayPi4c.RobbiSimulator.controller.tutor;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Tutor extends UnicastRemoteObject implements ITutor {

	private static final long serialVersionUID = 4722167139215525516L;

	private static final Logger logger = LogManager.getLogger(Tutor.class);

	List<Request> requests;
	int currentID = 0;

	Map<Integer, Answer> answers;

	public Tutor() throws RemoteException {
		super();
		requests = new ArrayList<>();
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

	public Optional<Request> getNewRequest() {
		if (requests.isEmpty())
			return Optional.empty();
		return Optional.ofNullable(requests.remove(0));
	}

	public void setAnswer(int id, Answer answer) {
		answers.put(id, answer);
	}

}
