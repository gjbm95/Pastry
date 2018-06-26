package pastry;

import Dominio.Sistema;
import com.Entidades.Nodo;
import com.Entidades.NodoRF;
import com.Utils.SistemaUtil;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.CharBuffer;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import rice.environment.Environment;

/**
 * Main GUI for P2P file replicator application
 */
public class P2PFileReplicatorApplication extends JFrame{
	private static final long serialVersionUID = 1L;
	private static Environment env;
	private javax.swing.JLabel bindPortLabel,bootAddressLabel,bootPortLabel,userNameLabel,fileNameLabel,searchfileNameLabel;
        private javax.swing.JButton startP2PNetworkButton,announceUpdateButton,searchButton;
        private javax.swing.JTextField bindPortText,bootAddressText,bootPortText,userNameText,fileNameText,searchfileText;
        private JScrollPane scrollableTextLog;
        private JTextArea  logText;
        private P2PReplicatorNode node;
        private String user;
        private Sistema sistema = Sistema.obtenerInstancia();

        public P2PFileReplicatorApplication(){
		initComponents();
                this.sistema.cargarRecursos();
        }
    
    private void initComponents(){
    	bindPortLabel = new JLabel("Puerto del nodo");
    	bootAddressLabel = new JLabel("Direccion del fantasma");
    	bootPortLabel     = new JLabel("Puerto del Fantasma");
    	userNameLabel = new JLabel("Enter user name");
        fileNameLabel = new JLabel("Archivo a compartir");
        searchfileNameLabel = new JLabel("Buscar archivo");
    	startP2PNetworkButton = new JButton("Iniciar Conexion");
    	announceUpdateButton = new JButton("Compartir Archivo");
        searchButton = new JButton("Descargar Archivo");
    	bindPortText = new JTextField("0",5);
    	bootAddressText = new JTextField("127.0.0.1",15);
    	bootPortText = new JTextField("0", 5);
    	userNameText = new JTextField("Nodo1",15);
        fileNameText = new JTextField("Archivo",15);
        searchfileText = new JTextField("Archivo",15);
    	logText = new JTextArea();
    	scrollableTextLog = new JScrollPane(logText);
    	scrollableTextLog.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    	scrollableTextLog.setPreferredSize(new Dimension(250,250));
    	setTitle("Replicador de Archivos, en P2P");
    	startP2PNetworkButton.addActionListener(new StartP2PNetworkListener());
    	announceUpdateButton.addActionListener(new AnnounceFileUpdateListener());
    	searchButton.addActionListener(new DownloadListener());
        setLayout(new BoxLayout(getContentPane(),BoxLayout.Y_AXIS));
    	add(bindPortLabel);
    	add(bindPortText);
    	add(bootAddressLabel);
    	add(bootAddressText);
    	add(bootPortLabel);
    	add(bootPortText);
    	add(userNameLabel);
    	add(userNameText);
        add(fileNameLabel);
    	add(fileNameText);
        add(searchfileNameLabel);
        add(searchfileText);
        add(searchButton);
    	add(startP2PNetworkButton);
    	add(announceUpdateButton);
    	add(scrollableTextLog);
    	addWindowListener(new ExitInitilizer());
        pack();
    }
    
 
    private void exit(){
    	dispose();
        if (env!=null)
    	env.destroy();
    	System.exit(0);
    }
    
    private class StartP2PNetworkListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			final int bindport = Integer.parseInt(bindPortText.getText());
			final String bootaddress = bootAddressText.getText();
			final int bootport = Integer.parseInt(bootPortText.getText());
			user = userNameText.getText();
                        Utils.nodename =  user;
                        Utils.nodeport = bootport;
                        SistemaUtil.servidorTiempo = bootaddress;
                        try {
                            SistemaUtil.reportarTiempo("addnode", "inicio", new NodoRF(Utils.nodename,Utils.nodeport));
                        } catch (NoSuchAlgorithmException ex) {
                            Logger.getLogger(P2PFileReplicatorApplication.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        Thread startNode = new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						env = new Environment();
						env.getParameters().setString("nat_search_policy", "never");
						final InetAddress addr = InetAddress.getByName(bootaddress);
						InetSocketAddress bootAddress = new InetSocketAddress(addr, bootport);
						node = new P2PReplicatorNode(bindport, bootAddress, env, user,logText);
					} catch (UnknownHostException ex) {
						ex.printStackTrace();
					} catch (Exception ex) {
						logText.append("Could not join the pestry ring\n");
                                                System.out.println("No fue posible unirse al anillo de Pastry.\n");
					}
				}
			});
			startNode.start();	
		}
    }
    
    
    private class AnnounceFileUpdateListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			Thread startPublish = new Thread(new Runnable() {
				
				@Override
				public void run() {
                                        Utils.filename = fileNameText.getText();
					File f = new File("storage\\"+fileNameText.getText());
					node.publishUpdate(new Date(f.lastModified()),fileNameText.getText());
					logText.append("Announcing last file update time.\n");
                                        System.out.println("Announcing last file update time.\n");
				}
			});
			startPublish.start();
						
		}
    	
    }
    
    private class DownloadListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
                Thread startSearch = new Thread(new Runnable() {	
                        @Override
                        public void run() {
                            try {
                                //Inicio de busqueda
                                SistemaUtil.reportarTiempo("search", "inicio", new NodoRF(Utils.nodename,Utils.nodeport));
                                node.searchFile(searchfileText.getText());
                            } catch (NoSuchAlgorithmException ex) {
                                Logger.getLogger(P2PFileReplicatorApplication.class.getName()).log(Level.SEVERE, null, ex);
                            }
                           
                        }
                });
                startSearch.start();

        }
    	
    }
    
    private class ExitInitilizer implements WindowListener{


		@Override
		public void windowActivated(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowClosed(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowClosing(WindowEvent e) {
			exit();
			
		}

		@Override
		public void windowDeactivated(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowDeiconified(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowIconified(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowOpened(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}
    	
    }
	
    
    public static void main(String[] args) throws Exception {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(P2PFileReplicatorApplication.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(P2PFileReplicatorApplication.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(P2PFileReplicatorApplication.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(P2PFileReplicatorApplication.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        if(args.length>0){
           String [] archivos = {"archivo1.jpg","archivo2.mp3","archivo3.txt"};
           Random r = new Random();
           Integer valor = r.nextInt(3);
           
           if(args[0].equals("node"))
           new P2PFileReplicatorApplication()
           .pilotoAutomatico(args[1],1000,Utils.seleccionarPuerto(),"autopilot", archivos[valor]);
           if(args[0].equals("central"))
           new P2PFileReplicatorApplication()
           .pilotoAutomatico(args[1],1000,1000,"autopilot", archivos[valor]);
           
        }else{
        System.out.println("------------------------------------------------");
        System.out.println("Bienvenido al simulador de Pastry");
        System.out.println("------------------------------------------------");
        System.out.println("Como desea ejecutar la aplicacion?");
        System.out.println("------------------------------------------------");
        System.out.println("1.- GUI (Interfaz gráfica)");
        System.out.println("2.- Linea de comandos");
        System.out.println("3.- Salir");
        System.out.println("------------------------------------------------");
        System.out.println("Escriba:");
        Scanner reader = new Scanner(System.in);
        int numero = 0;
                while (numero != 3){
                       try {
                           numero = reader.nextInt();
                                 if(numero == 1){
                                     java.awt.EventQueue.invokeLater(new Runnable() {

                                         public void run() {
                                             new P2PFileReplicatorApplication().setVisible(true);
                                         }
                                     });
                                 }
                                 if(numero == 2){
                                    new P2PFileReplicatorApplication().appTerminal();
                                 }
                       } catch (InputMismatchException ime){
                         System.out.println("¡Cuidado! Solo puedes insertar números. ");
                         reader.next();
                       }
                }
                
        }
        
	}
    
    public void appTerminal(){
        System.out.println("----------------------------------------------------");
        System.out.println("Que desea realizar");
        System.out.println("----------------------------------------------------");
        System.out.println("Asignar usuario -> user [nombre de usuario]");
        System.out.println("Asignar red -> network [ip fantasma] [puerto fantasma] [puerto nodo]");
        System.out.println("Buscar recurso -> search [nombre del archivo]");
        System.out.println("Salir -> exit");
        System.out.println("----------------------------------------------------");
        System.out.println("Escriba:");
        Scanner reader = new Scanner(System.in);
        String opcion = "";
                while (!opcion.equals("exit")){
                       try {
                           opcion = reader.nextLine();
                                 if(opcion.contains("user")){
                                     String[] opciones = opcion.split(" ");
                                     user = opciones[1];                        
                                 }
                                 if(opcion.contains("search")){
                                    String[] opciones = opcion.split(" ");
                                    node.searchFile(opciones[1]);
                                 }
                                 if(opcion.contains("network")){
                                     String[] opciones = opcion.split(" ");
                                   try {
						env = new Environment();
						env.getParameters().setString("nat_search_policy", "never");
						final InetAddress addr = InetAddress.getByName(opciones[1]);
						InetSocketAddress bootAddress = new InetSocketAddress(addr,Integer.parseInt(opciones[2]));
                                                if(user==null)
                                                user = "node";
                                                logText = new JTextArea();
						node = new P2PReplicatorNode(Integer.parseInt(opciones[3]), bootAddress, env, user,logText);
					} catch (UnknownHostException ex) {
						ex.printStackTrace();
					} catch (Exception ex) {
                                                System.out.println("No fue posible unirse al anillo de Pastry.\n");
					}
                                 }
                                 if(opcion.contains("exit")){
                                     System.exit(0);
                                 }
                       } catch (InputMismatchException ime){
                         System.out.println("¡Cuidado! Solo puedes insertar números. ");
                         reader.next();
                       }
                }
   
    }
    
    public synchronized void pilotoAutomatico(String central,int port,int portnode,String user,String file){
           try {
                    env = new Environment();
                    env.getParameters().setString("nat_search_policy", "never");
                    final InetAddress addr = InetAddress.getByName(central);
                    InetSocketAddress bootAddress = new InetSocketAddress(addr,port);
                    if(user==null)
                    user = "node";
                    logText = new JTextArea();
                    node = new P2PReplicatorNode(portnode, bootAddress, env, user,logText);
                    node.searchFile(file);
            } catch (UnknownHostException ex) {
                    ex.printStackTrace();
            } catch (Exception ex) {
                    System.out.println("No fue posible unirse al anillo de Pastry.\n");
            }
              
    }
}
