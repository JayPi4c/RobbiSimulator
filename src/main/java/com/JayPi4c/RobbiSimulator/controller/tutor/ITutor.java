package com.JayPi4c.RobbiSimulator.controller.tutor;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ITutor extends Remote {
	public int sendRequest(String code, String territory) throws RemoteException;

	public Answer getAnswer(int id) throws RemoteException;

}
