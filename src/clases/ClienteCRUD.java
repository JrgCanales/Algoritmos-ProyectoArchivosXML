package clases;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ClienteCRUD{
    public int ultimoId;
    
    //Este metodo solo se uso al principio para crear el archivo
    public void crearXML() throws ParserConfigurationException, SAXException, IOException, TransformerConfigurationException, TransformerException{
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        DOMImplementation implementation = builder.getDOMImplementation();
        
        Document doc = implementation.createDocument(null, "Clientes", null);
            
        TransformerFactory transFactory = TransformerFactory.newInstance();
        Transformer transformer = transFactory.newTransformer();
        DOMSource source =  new DOMSource(doc);
        StreamResult stream = new StreamResult(new File("DatosClientes.xml"));
        transformer.transform(source, stream);
    }
    
    public void listadoClientes(JTable tblClientes) throws ParserConfigurationException{
        DefaultTableModel dtmClientes = new DefaultTableModel();
        
        dtmClientes.addColumn("Id");
        dtmClientes.addColumn("Nombre");
        dtmClientes.addColumn("Identidad");
        dtmClientes.addColumn("Telefono");
        dtmClientes.addColumn("Ciudad");
        dtmClientes.addColumn("Correo");
        dtmClientes.addColumn("Edad");
        
        tblClientes.setModel(dtmClientes);
        
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            
            Document doc = builder.parse(new File("DatosClientes.xml"));
            doc.getDocumentElement().normalize();
            
            NodeList listadoClientes = doc.getElementsByTagName("Cliente");
            Node cliente;
            
            for (int i = 0; i < listadoClientes.getLength(); i++) {
                cliente = listadoClientes.item(i);
                
                if(cliente.getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element) cliente;
                    
                    Object row[]={
                        element.getElementsByTagName("Id").item(0).getTextContent(),
                        element.getElementsByTagName("Nombre").item(0).getTextContent(),
                        element.getElementsByTagName("Identidad").item(0).getTextContent(),
                        element.getElementsByTagName("Telefono").item(0).getTextContent(),
                        element.getElementsByTagName("Ciudad").item(0).getTextContent(),
                        element.getElementsByTagName("Correo").item(0).getTextContent(),
                        element.getElementsByTagName("Edad").item(0).getTextContent()
                    };
                    
                    dtmClientes.addRow(row);
                }
            }
            
        } catch (SAXException | IOException ex) {
            Logger.getLogger(ClienteCRUD.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public void agregarCliente(Cliente client) throws ParserConfigurationException, SAXException, IOException, TransformerConfigurationException, TransformerException{
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        
        Document doc = builder.parse(new File("DatosClientes.xml"));
        doc.getDocumentElement().normalize();
        
        Node nodoRaiz = doc.getDocumentElement();
        Element nwCliente = doc.createElement("Cliente");
        
        //nw = new
        Element nwId = doc.createElement("Id");
        nwId.setTextContent(Integer.toString(client.getId()));
        Element nwNombre = doc.createElement("Nombre");
        nwNombre.setTextContent(client.getNombre());
        Element nwIdentidad = doc.createElement("Identidad");
        nwIdentidad.setTextContent(client.getIdentidad());
        Element nwTelefono = doc.createElement("Telefono");
        nwTelefono.setTextContent(client.getTelefono());
        Element nwCiudad = doc.createElement("Ciudad");
        nwCiudad.setTextContent(client.getCiudad());
        Element nwCorreo = doc.createElement("Correo");
        nwCorreo.setTextContent(client.getCorreo());
        Element nwEdad = doc.createElement("Edad");
        nwEdad.setTextContent(Integer.toString(client.getEdad()));
        
        nwCliente.appendChild(nwId);
        nwCliente.appendChild(nwNombre);
        nwCliente.appendChild(nwIdentidad);
        nwCliente.appendChild(nwTelefono);
        nwCliente.appendChild(nwCiudad);
        nwCliente.appendChild(nwCorreo);
        nwCliente.appendChild(nwEdad);
        
        nodoRaiz.appendChild(nwCliente);
        
        TransformerFactory transFactory = TransformerFactory.newInstance();
        Transformer transformer = transFactory.newTransformer();
        DOMSource source =  new DOMSource(doc);
        StreamResult stream = new StreamResult(new File("DatosClientes.xml"));
        transformer.transform(source, stream);
        
        JOptionPane.showMessageDialog(null, "El Cliente ha sido Guardado correctamente!");
    }
    
    public void actualizarCliente(Cliente cliente) throws SAXException, IOException, TransformerConfigurationException, TransformerException{
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            
            Document doc = builder.parse(new File("DatosClientes.xml"));
            doc.getDocumentElement().normalize();
            
            NodeList listadoClientes = doc.getElementsByTagName("Cliente");
            
            for (int i = 0; i < listadoClientes.getLength(); i++) {
                Node nodo = listadoClientes.item(i);
                
                NodeList listaPropiedades = nodo.getChildNodes();
                Node propiedadBuscada;
                
                for (int j = 0; j < listaPropiedades.getLength(); j++) {
                    propiedadBuscada = listaPropiedades.item(j);
                    
                    if(propiedadBuscada.getNodeName().equals("Id") && propiedadBuscada.getTextContent().equals(Integer.toString(cliente.getId()))){
                        Element element = (Element) nodo;
                        
                        element.getElementsByTagName("Nombre").item(0).setTextContent(cliente.getNombre());
                        element.getElementsByTagName("Telefono").item(0).setTextContent(cliente.getTelefono());
                        element.getElementsByTagName("Ciudad").item(0).setTextContent(cliente.getCiudad());
                        element.getElementsByTagName("Correo").item(0).setTextContent(cliente.getCorreo());
                        element.getElementsByTagName("Edad").item(0).setTextContent(Integer.toString(cliente.getEdad()));
                        
                        JOptionPane.showMessageDialog(null, "Los Datos del cliente se actualizaron correctamente!");
                    }
                }
            }
            
            TransformerFactory transFactory = TransformerFactory.newInstance();
            Transformer transformer = transFactory.newTransformer();
            DOMSource source =  new DOMSource(doc);
            StreamResult stream = new StreamResult(new File("DatosClientes.xml"));
            transformer.transform(source, stream);
            
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ClienteCRUD.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void asignarId() throws SAXException, IOException{
        try {
            ArrayList<Integer> listaIds = new ArrayList<>();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            
            Document doc = builder.parse(new File("DatosClientes.xml"));
            doc.getDocumentElement().normalize();

            NodeList listadoClientes = doc.getElementsByTagName("Cliente");
            
            for (int i = 0; i < listadoClientes.getLength(); i++) {
                Node nodo = listadoClientes.item(i);
                
                NodeList listaPropiedades = nodo.getChildNodes();
                Node propiedadBuscada;
                
                for (int j = 0; j < listaPropiedades.getLength(); j++) {
                    propiedadBuscada = listaPropiedades.item(j);

                    if(propiedadBuscada.getNodeName().equals("Id")){
                        Element element = (Element) nodo;
                        
                        if(nodo.getNodeType() == Node.ELEMENT_NODE){
                            listaIds.add(Integer.parseInt(element.getElementsByTagName("Id").item(0).getTextContent()));
                        }
                        
                    }
                }
            }
            
            int idMayor=0;
            
            for (int k = 0; k < listaIds.size(); k++) {
                idMayor = listaIds.get(k);
            }
            
            ultimoId = idMayor;
            
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ClienteCRUD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void eliminarCliente(Cliente client) throws ParserConfigurationException, TransformerConfigurationException, TransformerException{
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            
            Document doc = builder.parse(new File("DatosClientes.xml"));
            doc.getDocumentElement().normalize();
            
            NodeList listadoClientes = doc.getElementsByTagName("Cliente");
            
            for (int i = 0; i < listadoClientes.getLength(); i++) {
                Node nodo = listadoClientes.item(i);
                
                NodeList listaPropiedades = nodo.getChildNodes();
                Node propiedadBuscada;
                
                for (int j = 0; j < listaPropiedades.getLength(); j++) {
                    propiedadBuscada = listaPropiedades.item(j);
                    
                    if(propiedadBuscada.getNodeName().equals("Id") && propiedadBuscada.getTextContent().equals(Integer.toString(client.getId()))){
                        
                        nodo.getParentNode().removeChild(nodo);

                        JOptionPane.showMessageDialog(null, "El Cliente ha sido eliminado correctamente!");
                    }
                }
            }
            
            TransformerFactory transFactory = TransformerFactory.newInstance();
            Transformer transformer = transFactory.newTransformer();
            DOMSource source =  new DOMSource(doc);
            StreamResult stream = new StreamResult(new File("DatosClientes.xml"));
            transformer.transform(source, stream);
            
        } catch (SAXException | IOException ex) {
            Logger.getLogger(ClienteCRUD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void buscarCliente(Cliente client) throws SAXException, IOException{
        boolean encontrado=false;
        
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            
            Document doc = builder.parse(new File("DatosClientes.xml"));
            doc.getDocumentElement().normalize();
            
            NodeList listadoClientes = doc.getElementsByTagName("Cliente");
            
            for (int i = 0; i < listadoClientes.getLength(); i++) {
                Node nodo = listadoClientes.item(i);
                
                NodeList listaPropiedades = nodo.getChildNodes();
                Node propiedadBuscada;
                
                for (int j = 0; j < listaPropiedades.getLength(); j++) {
                    propiedadBuscada = listaPropiedades.item(j);
                    
                    if(propiedadBuscada.getNodeName().equals("Id") && propiedadBuscada.getTextContent().equals(Integer.toString(client.getId()))){
                        JOptionPane.showMessageDialog(null, "Cliente encontrado, los datos se cargaran en el formulario!");
                        
                        if(nodo.getNodeType() == Node.ELEMENT_NODE){
                            Element element = (Element) nodo;
                            
                            client.setId(Integer.parseInt(element.getElementsByTagName("Id").item(0).getTextContent()));
                            client.setNombre(element.getElementsByTagName("Nombre").item(0).getTextContent());
                            client.setIdentidad(element.getElementsByTagName("Identidad").item(0).getTextContent());
                            client.setTelefono(element.getElementsByTagName("Telefono").item(0).getTextContent());
                            client.setCiudad(element.getElementsByTagName("Ciudad").item(0).getTextContent());
                            client.setCorreo(element.getElementsByTagName("Correo").item(0).getTextContent());
                            client.setEdad(Integer.parseInt(element.getElementsByTagName("Edad").item(0).getTextContent()));
                            
                            encontrado=true;
                        }
                        
                    }
                }
            }

        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ClienteCRUD.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(encontrado==false){
            JOptionPane.showMessageDialog(null, "Cliente no encontrado!");
        }
    }

}
