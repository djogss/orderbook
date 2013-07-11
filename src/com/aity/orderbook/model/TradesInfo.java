package com.aity.orderbook.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;


/**
 * Order book 
 * @author Mantas
 *
 */
public class TradesInfo {
	
	/**
	 * Map<true/false, Set<ByPrice>>
	 */
	Map<Boolean, Set<ByPrice>> trades 	= new HashMap<Boolean, Set<ByPrice>>();

	// Stores orders book by unique orders
	Map<String, BuyOrSell> askOrBid 	= new HashMap<String, BuyOrSell>();
	
	// Stores all modification of trades
	List<BuyOrSell> ordersMod 			= new ArrayList<BuyOrSell>();

	public TradesInfo(BuyOrSell bos) {
		this.handleRecord(bos);
	}

	/**
	 * Handling unique orders and orders that modifies unique orders
	 * @param bos
	 */
	public synchronized void handleRecord(BuyOrSell bos) {
		if(bos.getAction() == Action.ADD)
		{
			askOrBid.put(bos.getAction() + String.valueOf(bos.getOrder().getOrderId()), bos);
		}
		else
			if(bos.getAction() == Action.EDIT || bos.getAction() == Action.REMOVE)
				ordersMod.add(bos);
			else
				System.out.println("Wrong action");

	}

	/**
	 * Building orders book by price
	 */
	public void buildByPriceBook() 
	{
		BuyOrSell bos 					= null;
		Set<ByPrice> dataSet 			= null;
		List<BuyOrSell> omTmp 			= new ArrayList<BuyOrSell>(ordersMod);
		Iterator<BuyOrSell> omTmpIt 	= omTmp.iterator();
		
		Map<String, BuyOrSell> abTmp 	= new HashMap<String, BuyOrSell>(askOrBid);
		Iterator<String> abIt 			= abTmp.keySet().iterator();
		
		
		while(omTmpIt.hasNext())
		{
			bos = omTmpIt.next();
			switch(bos.getAction()){
				case EDIT:
					this.handleOrdersEdit(bos);
					ordersMod.remove(bos);
					break;
				case REMOVE:
					this.handleOrdersRemoval(bos);
					ordersMod.remove(bos);
					break;
				default:
					break;
			}
		}
		
		abTmp	= new HashMap<String, BuyOrSell>(askOrBid);
		abIt	= abTmp.keySet().iterator();
		while (abIt.hasNext()) 
		{
			bos 	= askOrBid.get(abIt.next());
			dataSet = trades.get(bos.getOrder().isBuy());
			if(bos.getAction() == Action.ADD)
				this.handleOrdersRegistration(bos, dataSet);
		}

	}

	/**
	 * Printing order book by price
	 */
	
	public synchronized void printOrersBook()
	{
		TreeSet<ByPrice> b;
		ByPrice tmpB;
		Boolean action;
		Iterator<Boolean> itx;
		Iterator<ByPrice> itSet;
		
		itx = trades.keySet().iterator();		
		while (itx.hasNext()) 
		{
			action = itx.next();
			b = (TreeSet<ByPrice>)trades.get(action);
			// If BID than show price ascending else who price deciding
			if(action == true)
				itSet = b.iterator();
			else
				itSet = b.descendingIterator();
			System.out.println("Action | Price | Quantity | Orders ");
			while(itSet.hasNext())
			{
				tmpB = itSet.next();
				String a = action == true ? "BUY" : "SELL";
				System.out.println(a + " | " +tmpB.getPrice()+ " | " + tmpB.getQuantity() + " | " + tmpB.getOrders());
			}
		}			
	}
	
	/**
	 * Handling complete and canceled records
	 * @param bos
	 */
	private synchronized void handleOrdersRemoval(BuyOrSell bos)
	{
		String ckKeyOld, ckKeyUpd;
		BuyOrSell bosOld, bosTmp, bosNew;
		Order order;
		
		Map<String, BuyOrSell> abTmp 	= new HashMap<String, BuyOrSell>(askOrBid);
		Iterator<String> abIt 			= abTmp.keySet().iterator();			
		ckKeyOld 						= Action.ADD + String.valueOf(bos.getOrder().getOrderId());
		bosOld							= askOrBid.get(ckKeyOld);			
		int quantity 					= bosOld.getOrder().getQuantity();
		
		while(abIt.hasNext()){
			bosTmp = askOrBid.get(abIt.next());
			
			// Checking if trade was completed SELL!=BUY and price=price				
			if(bosOld.getOrder().isBuy() != bosTmp.getOrder().isBuy() &&
					bosOld.getOrder().getPrice() == bosTmp.getOrder().getPrice())
			{
				
				ckKeyUpd 	= Action.ADD + String.valueOf(bosTmp.getOrder().getOrderId());
				bosNew 		= askOrBid.get(ckKeyUpd);
				order	 	=  new Order(bosNew.getOrder().getOrderId(), bosNew.getOrder().getSymbol(), bosNew.getOrder().isBuy(), bosNew.getOrder().getPrice(), bosNew.getOrder().getQuantity() - quantity);
				askOrBid.put(ckKeyUpd, new BuyOrSell(Action.ADD, order));
			}
		}
		
		if(askOrBid.containsKey(ckKeyOld)){
			askOrBid.remove(ckKeyOld);
			askOrBid.remove(bos.getAction() + String.valueOf(bos.getOrder().getOrderId()));
		}	
		
		trades.clear();

	}
	
	/**
	 * If trade is changed the 'by Price' book will be rebuild
	 * 
	 * @param bos
	 * @param dataSet
	 * @param idx
	 */
	private synchronized void handleOrdersEdit(BuyOrSell bos){

		String compositeKey, symbol;
		Order newOrd;
		Order order = bos.getOrder();
		
		BuyOrSell bosOld	= askOrBid.get((Action.ADD + String.valueOf(order.getOrderId())));
		Integer newPrice 	= order.getPrice();
		Integer newQnty 	= order.getQuantity();
		Boolean newType 	= order.isBuy();
		
		if(newPrice == null || newPrice == 0)
			newPrice = bosOld.getOrder().getPrice();
		
		if(newQnty == null || newQnty == 0)
			newQnty =  bosOld.getOrder().getQuantity();
		
		if(newType == null)
			newType =  bosOld.getOrder().isBuy();
		
		compositeKey 	= bos.getAction() + String.valueOf(bos.getOrder().getOrderId()); 
//		symbol 			= ordersList.get(bos.getOrder().getOrderId());
		symbol 			= bosOld.getOrder().getSymbol();
		
		newOrd 			= new Order(order.getOrderId(), symbol, newType, newPrice, newQnty);

		// Removing element from orders modification data collection
		ordersMod.remove(compositeKey);								

		// Removing element from unique orders data collection
		askOrBid.remove(Action.ADD + String.valueOf(order.getOrderId()));
		// Adding updated unique order to data collection
		askOrBid.put(Action.ADD + String.valueOf(order.getOrderId()), new BuyOrSell(Action.ADD, newOrd));
		
		bos.setActive(false);		// action completed! 
		trades.clear();				// preparing for rebuild of order book
	}

	
	/**
	 * Registering new orders to orders book
	 * @param bos
	 * @param dataSet
	 */
	private synchronized void handleOrdersRegistration(BuyOrSell bos, Set<ByPrice> dataSet) {
		
		Set<ByPrice> copyDataSet = null;
		Iterator<ByPrice> dataSetIt;
		ByPrice oldBp, newBp;
		int newOrders, newQuantity;
		Order o = bos.getOrder();
		
		if (dataSet == null) {
			copyDataSet = new TreeSet<ByPrice>();
			bos.setActive(false);
			copyDataSet.add(new ByPrice(o.getPrice(), o.getQuantity(), 1));
		} else {
			newBp = new ByPrice(o.getPrice(), o.getQuantity(), 1);
			copyDataSet = new TreeSet<ByPrice>(dataSet);
			dataSetIt = dataSet.iterator();

			while (dataSetIt.hasNext()) {
				oldBp = dataSetIt.next();
				if (bos.isActive() == true && oldBp.getPrice() == o.getPrice()) {
					newOrders = oldBp.getOrders() + 1;
					newQuantity = oldBp.getQuantity() + o.getQuantity();
					copyDataSet.remove(oldBp);
					copyDataSet.add(new ByPrice(o.getPrice(), newQuantity,
							newOrders));
					bos.setActive(false);
				} else {

					copyDataSet.add(newBp);
					bos.setActive(false);
				}
			}
		}
		trades.put(o.isBuy(), copyDataSet);

	}

	public Map<String, BuyOrSell> getActions() {
		return askOrBid;
	}

	/**
	 * @return the trades
	 */
	public Map<Boolean, Set<ByPrice>> getTrades() {
		return trades;
	}

	/**
	 * @param trades
	 *            the trades to set
	 */
	public void setTrades(Map<Boolean, Set<ByPrice>> trades) {
		this.trades = trades;
	}

}
