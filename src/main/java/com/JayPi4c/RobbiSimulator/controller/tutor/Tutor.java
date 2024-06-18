package com.JayPi4c.RobbiSimulator.controller.tutor;

import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.io.Serial;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

/**
 * ITutor implementation to handle the RMI invocations.
 *
 * @author Jonas Pohl
 */
@Slf4j
@EqualsAndHashCode(callSuper = true)
public class Tutor extends UnicastRemoteObject implements ITutor {

    @Serial
    private static final long serialVersionUID = 4722167139215525516L;

    private transient final Queue<Request> requests;
    /**
     * ID of the latest request. Will increment after a request arrived.
     */
    private int currentID = 0;
    /**
     * Map to hold answers accessible by their id
     */
    private final Map<Integer, Answer> answers;

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
    public synchronized Optional<Request> getNewRequest() {
        return Optional.ofNullable(requests.poll());
    }

    /**
     * Adds an answer with the corresponding id in the list of done answers.
     *
     * @param id     the id for the answer
     * @param answer the actual answer
     */
    public synchronized void setAnswer(int id, Answer answer) {
        answers.put(id, answer);
    }

}
