package org.jahia.modules.minaChat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.DatagramSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.apache.vysper.mina.TCPEndpoint;
import org.apache.vysper.storage.StorageProviderRegistry;
import org.apache.vysper.storage.inmemory.MemoryStorageProviderRegistry;
import org.apache.vysper.storage.jcr.JcrStorageProviderRegistry;
import org.apache.vysper.xmpp.addressing.Entity;
import org.apache.vysper.xmpp.addressing.EntityImpl;
import org.apache.vysper.xmpp.authorization.AccountCreationException;
import org.apache.vysper.xmpp.authorization.AccountManagement;
import org.apache.vysper.xmpp.modules.extension.xep0049_privatedata.PrivateDataModule;
import org.apache.vysper.xmpp.modules.extension.xep0054_vcardtemp.VcardTempModule;
import org.apache.vysper.xmpp.modules.extension.xep0119_xmppping.XmppPingModule;
import org.apache.vysper.xmpp.modules.extension.xep0202_entity_time.EntityTimeModule;
import org.apache.vysper.xmpp.server.XMPPServer;
import org.jahia.services.JahiaAfterInitializationService;
import org.jahia.services.content.JCRStoreService;

import org.jahia.exceptions.JahiaException;
import org.jahia.exceptions.JahiaInitializationException;
import org.jahia.services.JahiaService;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MinaServerService extends JahiaService implements JahiaAfterInitializationService {
    
    static private JCRStoreService instance = null;

    private static Logger logger = LoggerFactory.getLogger(JCRStoreService.class);    
    
    
    private int PORT;
    private String XMPPServerName;
    private String TLSCertificatePath;
    private String TLSCertificatePassword;

    public void setPORT(int PORT) {
        this.PORT = PORT;
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
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jahia.services.JahiaService#start()
	 */
	public void start() throws JahiaInitializationException {
		// do nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jahia.services.JahiaService#stop()
	 */
	public void stop() throws JahiaException {
		// do nothing
	}
	
	public void initAfterAllServicesAreStarted() throws JahiaInitializationException {
		XMPPServer server = new XMPPServer(XMPPServerName);  
	      
		StorageProviderRegistry providerRegistry = new MemoryStorageProviderRegistry();  
		//StorageProviderRegistry providerRegistry = new JcrStorageProviderRegistry();  
	    //StorageProviderRegistry providerRegistry = new JahiaStorageProviderRegistry();  
	      
	    AccountManagement accountManagement = (AccountManagement) providerRegistry.retrieve(AccountManagement.class);  
	    //AccountManagement accountManagement = (AccountManagement) providerRegistry.retrieve(JahiaAccountManagement.class);  
	      
	      
	    Entity user = EntityImpl.parseUnchecked("user@"+XMPPServerName);  
	    
	    try {
			if(!accountManagement.verifyAccountExists(user)) {
		        accountManagement.addUser(user, "password");
		    }
	    }catch (AccountCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	      
	    server.setStorageProviderRegistry(providerRegistry);  
	      
        TCPEndpoint endpoint = new TCPEndpoint();
        endpoint.setPort(PORT);
        server.addEndpoint(endpoint);
        
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
        
        
	}
	/*
	protected XMPPConnection connectClient(int port, String username, String password) throws Exception {
        ConnectionConfiguration connectionConfiguration = new ConnectionConfiguration("localhost", port);
        connectionConfiguration.setCompressionEnabled(false);
        connectionConfiguration.setSecurityMode(ConnectionConfiguration.SecurityMode.required);
        connectionConfiguration.setSASLAuthenticationEnabled(true);
        connectionConfiguration.setDebuggerEnabled(false);
        connectionConfiguration.setKeystorePath("src/main/config/bogus_mina_tls.cert");
        connectionConfiguration.setTruststorePath("src/main/config/bogus_mina_tls.cert");
        connectionConfiguration.setTruststorePassword("boguspw");

        XMPPConnection.DEBUG_ENABLED = true;
        XMPPConnection client = new XMPPConnection(connectionConfiguration);

        client.connect();

        client.login(username, password);
        return client;
    }
	
	protected Packet sendSync(XMPPConnection client, Packet request) {
        // Create a packet collector to listen for a response.
        PacketCollector collector = client.createPacketCollector(new PacketIDFilter(request.getPacketID()));

        client.sendPacket(request);

        // Wait up to 5 seconds for a result.
        return collector.nextResult(5000);
    }*/
}