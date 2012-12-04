package org.jahia.modules.minaChat;

import org.apache.vysper.mina.TCPEndpoint;
import org.apache.vysper.storage.StorageProviderRegistry;
import org.apache.vysper.storage.inmemory.MemoryStorageProviderRegistry;
import org.apache.vysper.xmpp.addressing.Entity;
import org.apache.vysper.xmpp.addressing.EntityImpl;
import org.apache.vysper.xmpp.authorization.AccountCreationException;
import org.apache.vysper.xmpp.authorization.AccountManagement;
import org.apache.vysper.xmpp.extension.xep0124.BoshEndpoint;
import org.apache.vysper.xmpp.modules.extension.xep0049_privatedata.PrivateDataModule;
import org.apache.vysper.xmpp.modules.extension.xep0054_vcardtemp.VcardTempModule;
import org.apache.vysper.xmpp.modules.extension.xep0119_xmppping.XmppPingModule;
import org.apache.vysper.xmpp.modules.extension.xep0202_entity_time.EntityTimeModule;
import org.apache.vysper.xmpp.server.XMPPServer;
import org.jahia.api.Constants;
import org.jahia.data.applications.ApplicationBean;
import org.jahia.exceptions.JahiaException;
import org.jahia.exceptions.JahiaInitializationException;
import org.jahia.services.JahiaAfterInitializationService;
import org.jahia.services.JahiaService;
import org.jahia.services.content.*;
import org.jahia.utils.LanguageCodeConverters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.query.Query;
import javax.jcr.query.QueryResult;
import java.io.File;
import java.util.Arrays;

public class MinaServerService extends JahiaService implements JahiaAfterInitializationService {
    
    private static Logger logger = LoggerFactory.getLogger(JCRStoreService.class);

    String password;
    Integer tcpport;
    Integer boshport;

    String XMPPServerName;
    String TLSCertificatePath;
    String TLSCertificatePassword;
    AccountManagement accountManagement;

    public void setPassword(String password) {
            this.password = password;
        }

    public void setXMPPServerName(String XMPPServerName) {
        this.XMPPServerName = XMPPServerName;
    }

    public void setTLSCertificatePath(String TLSCertificatePath) {
        this.TLSCertificatePath = TLSCertificatePath;
    }

    public void setTLSCertificatePassword(String TLSCertificatePassword) {
        this.TLSCertificatePassword = TLSCertificatePassword;
    }

    public Integer getTcpport() {
        return tcpport;
    }

    public void setTcpport(Integer tcpport) {
        this.tcpport = tcpport;
    }

    public Integer getBoshport() {
        return boshport;
    }

    public void setBoshport(Integer boshport) {
        this.boshport = boshport;
    }


    public String getPassword() {
        return password;
    }

    public String getXMPPServerName() {
        logger.info("getXMPPServerName: "+XMPPServerName);
        return XMPPServerName;
    }

    public String getTLSCertificatePath() {
        return TLSCertificatePath;
    }

    public String getTLSCertificatePassword() {
        return TLSCertificatePassword;
    }


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jahia.services.JahiaService#start()
	 */
	@Override
    public void start() throws JahiaInitializationException {
		// do nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jahia.services.JahiaService#stop()
	 */
	@Override
    public void stop() throws JahiaException {
		// do nothing
	}

    private void connectAllusers() {
        try {
            JCRTemplate.getInstance().doExecuteWithSystemSession(new JCRCallback<ApplicationBean>() {
                public ApplicationBean doInJCR(JCRSessionWrapper session) throws RepositoryException {
                    if (session.getWorkspace().getQueryManager() != null) {
                        JCRSessionWrapper defaultsession = JCRSessionFactory.getInstance().getCurrentUserSession(Constants.LIVE_WORKSPACE);
                        String query = "SELECT * FROM [jmix:chatUser]";
                        Query q = defaultsession.getWorkspace().getQueryManager().createQuery(query, Query.JCR_SQL2);
                        QueryResult qr = q.execute();
                        final NodeIterator nodes = qr.getNodes();
                        while (nodes.hasNext()) {
                            JCRNodeWrapper nodeWrapper = (JCRNodeWrapper) nodes.next();
                            addUser(nodeWrapper.getName());
                        }
                    }
                    return null;
                }
            });
        } catch (RepositoryException e) {
            logger.error("Error while retrieving application by context", e);
        }
    }

    public void addUser(String username){
        Entity user = EntityImpl.parseUnchecked(username+"@"+XMPPServerName);

	    try {
			if(!accountManagement.verifyAccountExists(user)) {
		        accountManagement.addUser(user, "password");
                logger.info("User: "+ user + " added with password: password");
		    }else{
                logger.info("User: "+ user + " already registrered");
            }
	    }catch (AccountCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	public void initAfterAllServicesAreStarted() throws JahiaInitializationException {
		XMPPServer server = new XMPPServer(XMPPServerName);  
	      
		StorageProviderRegistry providerRegistry = new MemoryStorageProviderRegistry();  

	    accountManagement = (AccountManagement) providerRegistry.retrieve(AccountManagement.class);


	      
	    server.setStorageProviderRegistry(providerRegistry);  
	    logger.info("boshport:" + boshport + " tcpport:" + tcpport + " password: " + password);
        TCPEndpoint endpoint = new TCPEndpoint();
        endpoint.setPort(tcpport);
        server.addEndpoint(endpoint);

        BoshEndpoint boshEndpoint = new BoshEndpoint();
        boshEndpoint.setPort(boshport);
        boshEndpoint.setAccessControlAllowOrigin(Arrays.asList("*"));
        //boshEndpoint.setSSLEnabled(true);
        //boshEndpoint.setSSLCertificateInfo("src/main/resources/keystore","password");
        boshEndpoint.setContextPath("/bosh");
        server.addEndpoint(boshEndpoint);
        try {
			server.setTLSCertificateInfo(new File(TLSCertificatePath), TLSCertificatePassword);
			System.out.println("setTLSCertificateInfo: "+TLSCertificatePath);  	        
			System.out.println("setTLSCertificatePwd: "+TLSCertificatePassword);  	        
		    server.start();  
			System.out.println("Vysper server is running...");
		      
		    server.addModule(new EntityTimeModule());  
		    server.addModule(new VcardTempModule());  
		    server.addModule(new XmppPingModule());  
		    server.addModule(new PrivateDataModule());  	
	        Thread.sleep(200);
	        
	    } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        connectAllusers();
	}
}