package pastry;

import javax.swing.JTextArea;

import rice.environment.Environment;
import rice.p2p.commonapi.Application;
import rice.p2p.commonapi.Endpoint;
import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.Message;
import rice.p2p.commonapi.Node;
import rice.p2p.commonapi.NodeHandle;
import rice.p2p.commonapi.RouteMessage;
import rice.pastry.NodeIdFactory;
import rice.pastry.PastryNode;
import rice.pastry.PastryNodeFactory;

public class P2PFileReplicationMessegeApplicationImpl implements Application {
	
	private Endpoint endpoint;
	private Node node;
	private String user;
	private JTextArea log;
	private P2PFileTransferApplicationImpl file_app;
	public P2PFileReplicationMessegeApplicationImpl(Node node,String user,JTextArea log, P2PFileTransferApplicationImpl file_app){
		this.user = user;
		this.node = node;
		this.log = log;
		this.file_app = file_app;
		endpoint = node.buildEndpoint(this, "File transfer messege");
		endpoint.register();
	}
	
	  /**
	   * Called to directly send a message to the nh
	   */
	  public void routeFileTransferRequestDirect(NodeHandle nh,String message) {
		log.append("Sending request direct to "+nh+" Tiempo: "+Utils.obtenerHora()+"\n"); 
                System.out.println("Sending request direct to "+nh+" Tiempo: "+Utils.obtenerHora()+"\n");
	    Message msg = new P2PFileReplicationMessegeImpl(nh,node.getLocalNodeHandle(), user, message);
	    endpoint.route(null, msg, nh);
	  }

	@Override
	public boolean forward(RouteMessage message) {
		return true;
	}

	@Override
	public void deliver(Id id, Message message) {
		P2PFileReplicationMessegeImpl msg = (P2PFileReplicationMessegeImpl) message;
                 final NodeHandle update_reciever = (NodeHandle) msg.getFrom();
		if (msg.getMessege().equals("trans_ok")){
                    log.append("Enviando archivo a :"+update_reciever+" Tiempo: "+Utils.obtenerHora()+"\n");
                    System.out.println("Enviando archivo a :"+update_reciever+" Tiempo: "+Utils.obtenerHora()+"\n");
                    Thread start_transfer = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                    file_app.sendMessegeDirect(update_reciever);
                            }
                    });
                    start_transfer.start();
                }else if (msg.getMessege().equals("lotengoyo")){
                   log.append("Solicitando archivo a :"+update_reciever+" Tiempo: "+Utils.obtenerHora()+"\n");
                   System.out.println("Solicitando archivo a :"+update_reciever+" Tiempo: "+Utils.obtenerHora()+"\n");
                }
	}

	@Override
	public void update(NodeHandle handle, boolean joined) {
		log.append("Otro nodo se unio: "+handle+" "+ joined+" Tiempo: "+Utils.obtenerHora()+"\n");
                System.out.println("Otro nodo se unio: "+handle+" "+ joined+" Tiempo: "+Utils.obtenerHora()+"\n");
	}

	public Endpoint getEndpoint() {
		return endpoint;
	}

	public Node getNode() {
		return node;
	}

	public void setEndpoint(Endpoint endpoint) {
		this.endpoint = endpoint;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

}
