/*******************************************************************************
 * Copyright (c) 2014-2015 University of Luxembourg.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Alfredo Capozucca - initial API and implementation
 *     Christophe Kamphaus - Remote implementation of Actors
 *     Thomas Mortimer - Updated client to MVC and added new design patterns
 ******************************************************************************/

package lu.uni.lassy.excalibur.examples.icrash.dev.java.environment.actors;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.crypto.NoSuchPaddingException;

import lu.uni.lassy.excalibur.examples.icrash.dev.java.system.IcrashSystem;
import lu.uni.lassy.excalibur.examples.icrash.dev.java.system.types.primary.DtAnswer;
import lu.uni.lassy.excalibur.examples.icrash.dev.java.system.types.primary.DtKey;
import lu.uni.lassy.excalibur.examples.icrash.dev.java.system.types.primary.DtLogin;
import lu.uni.lassy.excalibur.examples.icrash.dev.java.system.types.primary.DtPassword;
import lu.uni.lassy.excalibur.examples.icrash.dev.java.types.stdlib.PtBoolean;
import lu.uni.lassy.excalibur.examples.icrash.dev.java.types.stdlib.PtString;
import lu.uni.lassy.excalibur.examples.icrash.dev.java.utils.Log4JUtils;
import lu.uni.lassy.excalibur.examples.icrash.dev.java.utils.RmiUtils;

import org.apache.log4j.Logger;

/**
 * The Class ActAuthenticatedImpl, which creates a server side actor of type
 * authenticated. It is an abstract class, so must be created by another actor,
 * like the coordinator
 */
public abstract class ActAuthenticatedImpl extends UnicastRemoteObject implements ActAuthenticated {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 227L;

	/** The login of the class type associated with this actor. */
	private DtLogin login;

	/**
	 * Instantiates a new server side actor of type authenticated.
	 *
	 * @param n
	 *            The username that is associated with this authenticated actor.
	 *            This helps tracking actors and their associated class type
	 *            (Ct...)
	 * @throws RemoteException
	 *             Thrown if the server is offline
	 */
	public ActAuthenticatedImpl(DtLogin n) throws RemoteException {
		super(RmiUtils.getInstance().getPort());
		this.login = n;

	}
	
	
	/** getter for the login field*/
	public DtLogin getLogin() {
		return login;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see lu.uni.lassy.excalibur.examples.icrash.dev.java.environment.actors.
	 * ActAuthenticated#oeLogin(lu.uni.lassy.excalibur.examples.icrash.dev.java.
	 * system.types.primary.DtLogin,
	 * lu.uni.lassy.excalibur.examples.icrash.dev.java.system.types.primary.
	 * DtPassword)
	 */
	synchronized public PtString oeLogin(DtLogin aDtLogin, DtPassword aDtPassword)
			throws RemoteException, NotBoundException, NoSuchPaddingException, NoSuchAlgorithmException, IOException {

		Logger log = Log4JUtils.getInstance().getLogger();

		Registry registry = LocateRegistry.getRegistry(RmiUtils.getInstance().getHost(),
				RmiUtils.getInstance().getPort());

		// Gathering the remote object as it was published into the registry
		IcrashSystem iCrashSys_Server = (IcrashSystem) registry.lookup("iCrashServer");

		// set up ActAuthenticated instance that performs the request
		iCrashSys_Server.setCurrentRequestingAuthenticatedActor(this);

		log.info("message ActAuthenticated.oeLogin sent to system");
		
		DtKey key = getNewKey();
		
		log.info("Secret key obtained from a \"secret channel\"");
		aDtLogin.encrypt(key);
		aDtPassword.encrypt(key);
        log.info("Your data was encrypted on the client");
        
		PtString res = iCrashSys_Server.oeLogin(aDtLogin, aDtPassword);
		
		if (!res.getValue().equals("false"))
			log.info("operation oeLogin successfully executed by the system");
		else 
			log.error("operation oeLogin was not a success by the system");
		return res;
	}

	/** this method generated new key for encryption/decryption */
	public DtKey getNewKey() throws FileNotFoundException{
		String dictionary = "abcdefgihjklmnopqrstuvwxyz0123456789_*][)('?!=-/.|&@#$%^";
        String generatedKey = "";
        Random rand = new Random();
        for (int i = 0; i < 16; ++i) {
            char a = dictionary.charAt(rand.nextInt(dictionary.length()));
            generatedKey = generatedKey.concat(String.valueOf(a));
        }
        PrintWriter pw = new PrintWriter(new File("key.txt"));
        pw.print(generatedKey);
        pw.flush();
        pw.close();
		return new DtKey(new PtString(generatedKey));
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see lu.uni.lassy.excalibur.examples.icrash.dev.java.environment.actors.
	 * ActAuthenticated#oeLogin(lu.uni.lassy.excalibur.examples.icrash.dev.java.
	 * system.types.primary.DtQuestion,
	 * lu.uni.lassy.excalibur.examples.icrash.dev.java.system.types.primary.
	 * DtAnswer)
	 */
	synchronized public PtBoolean oeEnterAnswer(DtLogin aDtLogin, DtAnswer aDtAnswer)
			throws RemoteException, NotBoundException, NoSuchPaddingException, NoSuchAlgorithmException, IOException {

		Logger log = Log4JUtils.getInstance().getLogger();

		Registry registry = LocateRegistry.getRegistry(RmiUtils.getInstance().getHost(),
				RmiUtils.getInstance().getPort());

		// Gathering the remote object as it was published into the registry
		IcrashSystem iCrashSys_Server = (IcrashSystem) registry.lookup("iCrashServer");

		// set up ActAuthenticated instance that performs the request
		iCrashSys_Server.setCurrentRequestingAuthenticatedActor(this);                                                                                     
		
		DtKey key = getNewKey();
		
		log.info("Secret key obtained from a \"secret channel\"");
		aDtLogin.encrypt(key);
		aDtAnswer.encrypt(key);
        log.info("Your data was encrypted on the client");
		
		log.info("message ActAuthenticated.oeEnterAnswer sent to system");
		PtBoolean res = iCrashSys_Server.oeEnterAnswer(aDtLogin, aDtAnswer);

		if (res.getValue() == true)
			log.info("operation oeEnterAnswer successfully executed by the system");
		else log.error("operation oeEnterAnswer was not a success by the system");

		return res;
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see lu.uni.lassy.excalibur.examples.icrash.dev.java.environment.actors.
	 * ActAuthenticated#oeLogout()
	 */
	synchronized public PtBoolean oeLogout() throws RemoteException, NotBoundException {

		Logger log = Log4JUtils.getInstance().getLogger();

		Registry registry = LocateRegistry.getRegistry(RmiUtils.getInstance().getHost(),
				RmiUtils.getInstance().getPort());

		// Gathering the remote object as it was published into the registry
		IcrashSystem iCrashSys_Server = (IcrashSystem) registry.lookup("iCrashServer");

		// set up ActAuthenticated instance that performs the request
		iCrashSys_Server.setCurrentRequestingAuthenticatedActor(this);

		log.info("message ActAuthenticated.oeLogout sent to system");
		PtBoolean res = iCrashSys_Server.oeLogout();

		if (res.getValue() == true)
			log.info("operation oeLogout successfully executed by the system");

		return res;
	}

	/** A list of listeners associated with this actor. */
	protected List<ActProxyAuthenticated> listeners = new ArrayList<ActProxyAuthenticated>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lu.uni.lassy.excalibur.examples.icrash.dev.java.environment.actors.
	 * ActAuthenticated#addListener(lu.uni.lassy.excalibur.examples.icrash.dev.
	 * java.environment.actors.ActProxyAuthenticated)
	 */
	synchronized public void addListener(ActProxyAuthenticated aActProxyAuthenticated)
			throws RemoteException, NotBoundException {
		listeners.add(aActProxyAuthenticated);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see lu.uni.lassy.excalibur.examples.icrash.dev.java.environment.actors.
	 * ActAuthenticated#removeListener(lu.uni.lassy.excalibur.examples.icrash.
	 * dev.java.environment.actors.ActProxyAuthenticated)
	 */
	synchronized public void removeListener(ActProxyAuthenticated aActProxyAuthenticated)
			throws RemoteException, NotBoundException {
		listeners.remove(aActProxyAuthenticated);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see lu.uni.lassy.excalibur.examples.icrash.dev.java.environment.actors.
	 * ActAuthenticated#ieMessage(lu.uni.lassy.excalibur.examples.icrash.dev.
	 * java.types.stdlib.PtString)
	 */
	public PtBoolean ieMessage(PtString aMessage) {
		Logger log = Log4JUtils.getInstance().getLogger();
		log.info("message ActAuthenticated.ieMessage received from system");
		log.info("ieMessage is: " + aMessage.getValue());
		log.info("Message being sent to " + this.login.value.getValue());
		for (Iterator<ActProxyAuthenticated> iterator = listeners.iterator(); iterator.hasNext();) {
			ActProxyAuthenticated aProxy = iterator.next();
			try {
				aProxy.ieMessage(aMessage);
			} catch (RemoteException e) {
				Log4JUtils.getInstance().getLogger().error(e);
				iterator.remove();
			}
		}
		return new PtBoolean(true);
	}

}