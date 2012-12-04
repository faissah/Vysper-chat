package org.jahia.modules.minaChat.client;
import org.jahia.bin.Action;
import org.jahia.bin.ActionResult;
import org.jahia.services.content.JCRSessionWrapper;
import org.jahia.services.render.RenderContext;
import org.jahia.services.render.Resource;
import org.jahia.services.render.URLResolver;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public class Connect extends Action {
    private transient static Logger logger = org.slf4j.LoggerFactory.getLogger(Connect.class);
    protected XMPPConnection client;

    private int port;
    private String XMPPServerName;
    private String TLSCertificatePath;
    private String TLSCertificatePassword;

    public void setPORT(int PORT) {
        this.port = port;
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

    @Override
	public ActionResult doExecute(HttpServletRequest req, RenderContext renderContext, Resource resource, JCRSessionWrapper session, Map<String, List<String>> parameters, URLResolver urlResolver) throws Exception {

    	client = connectClient(port, "user@"+XMPPServerName, "password");
        Presence presence = new Presence(Presence.Type.unavailable);
        presence.setStatus("Gone fishing");
        sendSync(client,presence);

        return ActionResult.OK_JSON;
    }


	protected XMPPConnection connectClient(int port, String username, String password) throws Exception {
        ConnectionConfiguration connectionConfiguration = new ConnectionConfiguration(XMPPServerName, port);
        connectionConfiguration.setCompressionEnabled(false);
        connectionConfiguration.setSecurityMode(ConnectionConfiguration.SecurityMode.required);
        connectionConfiguration.setSASLAuthenticationEnabled(true);
        connectionConfiguration.setDebuggerEnabled(false);
        connectionConfiguration.setKeystorePath(TLSCertificatePath);
        connectionConfiguration.setTruststorePath(TLSCertificatePath);
        connectionConfiguration.setTruststorePassword(TLSCertificatePassword);

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
    }
}
