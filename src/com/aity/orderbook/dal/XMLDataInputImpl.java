package com.aity.orderbook.dal;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.aity.orderbook.model.Action;
import com.aity.orderbook.model.Command;

public class XMLDataInputImpl implements InputDataManager{

	private final List<Command> commandList;
	
	public XMLDataInputImpl()
	{
		commandList = new ArrayList<Command>();
	}
	
	public static void main(String args[]){
		
		XMLDataInputImpl imp = new XMLDataInputImpl();
		imp.loadData();
	}
	
	@Override
	public synchronized List<Command> loadData() {

		final String fileName = "resources/orders2.xml";

        readXML(fileName);
        return commandList;
		
	}
	
	/**
	 * Reading XML file and creating list of Commands
	 * @param fileName
	 */
	private synchronized void readXML(String fileName)
    {
        Document document;
        DocumentBuilder documentBuilder;
        DocumentBuilderFactory documentBuilderFactory;
        NodeList nodeList;
        File xmlInputFile;
        
        Command command;
        Map<String, Command>newOrders = new HashMap<String, Command>();
        

        try
        {
            xmlInputFile = new File(fileName);
            documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            document = documentBuilder.parse(xmlInputFile);
            nodeList = document.getElementsByTagName("*");

            document.getDocumentElement().normalize();

            for (int index = 0; index < nodeList.getLength(); index++)
            {
                Node node = nodeList.item(index);
                if (node.getNodeType() == Node.ELEMENT_NODE)
                {
                    Element element = (Element) node;

                    command = getTagOrder(element, newOrders);                    

                    if(command != null){
                    	commandList.add(command);
                    	newOrders.put((command.getAction() + String.valueOf(command.getOrder().getOrderId())), command);
                    }
                }
            }
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }
	
	/**
	 * Reading every record information and creating Command object
	 * @param element
	 * @param orders
	 * @return
	 */
	private synchronized Command getTagOrder(Element element, Map<String, Command> orders) {
		
		Action a = getAction(element.getTagName());
		Command oldCommand;
		Long orderID;
		String symbol;
		Boolean type;
		Integer price, quantity;
		
		if (a == Action.ADD | a == Action.EDIT || a == Action.REMOVE )
		{
//			System.out.println( element.getTagName() + " " +element.getAttribute("order-id") + " " + element.getAttribute("symbol")
//     		+ " " + element.getAttribute("type")
//     		+ " " + element.getAttribute("price")
//     		+ " " + element.getAttribute("quantity"));
	            
			// If order was modified we taking data from last modified order
			oldCommand = orders.get(Action.EDIT + element.getAttribute("order-id"));
			// If order wasn't modified yet, the data is taken from original order
			if(oldCommand == null)
				oldCommand = orders.get(Action.ADD + element.getAttribute("order-id"));
			orderID = element.getAttribute("order-id").equals("") 	? null 	: Long.valueOf(element.getAttribute("order-id"));
			symbol 	= element.getAttribute("symbol").equals("") 	? oldCommand.getOrder().getSymbol()  : element.getAttribute("symbol");
			
			type = null;
			if(element.getAttribute("type").equals("buy"))
				type = true;
			else if(element.getAttribute("type").equals("sell"))
					type = false;
				else
					type = oldCommand.getOrder().isBuy();
					
			
			price 	= element.getAttribute("price").equals("") 		? oldCommand.getOrder().getPrice() 		: Integer.valueOf(element.getAttribute("price"));
			quantity= element.getAttribute("quantity").equals("") 	? oldCommand.getOrder().getQuantity() 	: Integer.valueOf(element.getAttribute("quantity"));
			
			orders.remove(a + element.getAttribute("order-id"));
	        return  new Command(a, orderID, symbol, type, price, quantity); 
	        }
	        else
	        {
	            return null;
	        }
	}

	
	private synchronized Action getAction(String tagName) {

		if(tagName.equals("add"))
        	return  Action.ADD;
        
		if(tagName.equals("edit"))
			return Action.EDIT;
        
		if(tagName.equals("remove"))
        	return Action.REMOVE;
	
		return null;
	}	
}