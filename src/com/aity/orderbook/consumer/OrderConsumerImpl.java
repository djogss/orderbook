package com.aity.orderbook.consumer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.aity.orderbook.iface.Log;
import com.aity.orderbook.iface.LogLevel;
import com.aity.orderbook.iface.OrderConsumer;
import com.aity.orderbook.model.Action;
import com.aity.orderbook.model.BuyOrSell;
import com.aity.orderbook.model.Order;
import com.aity.orderbook.model.TradesInfo;
import com.aity.orderbook.processor.OrderProcessor;

/**
 * Development Test.
 * 
 * @author Mantas
 */
public class OrderConsumerImpl implements OrderConsumer {
	
	private Map<String, TradesInfo> byOrderBook;
	private Map<Long, String> ordersList;
	private  Log m_log;

	@Override
	public void startProcessing(Log log) {
		m_log = log;
		m_log.log(LogLevel.INFO, "OrderConsumerImp initialization");
		
		byOrderBook = new HashMap<String, TradesInfo>();
		ordersList = new HashMap<Long, String>();

		m_log.log(LogLevel.INFO, "Starting orders processor thread");
		OrderProcessor orderProcessor = new OrderProcessor(this);
		Thread t = new Thread(orderProcessor);
		t.setName("Orders processor");
		t.start();
	}

	@Override
	public synchronized void finishProcessing() 
	{
		Iterator<String> itOuter;
		TradesInfo ti;
		String key;
		itOuter = byOrderBook.keySet().iterator();
		while (itOuter.hasNext()) 
		{
			key = itOuter.next();
			m_log.log(LogLevel.INFO, "Symbol:" + key);
			m_log.log(LogLevel.INFO, "orders book ============================ BEGIN");
			ti = byOrderBook.get(key);
			ti.buildByPriceBook();
			ti.printOrersBook();
			m_log.log(LogLevel.INFO,"orders book ============================ END");
		}
		
	}

	@Override
	public synchronized void  handleEvent(Action action, Order order) {

		BuyOrSell bos = new BuyOrSell(action, order);
		TradesInfo ti = null;
		String symbol = order.getSymbol();

		// Checking if symbol was traded
		if ((ti = byOrderBook.get(symbol)) != null) {
			ti.handleRecord(bos);
			ordersList.put(order.getOrderId(), symbol); // Maintaining orders => symbols list
		} else {
			// Checking if order has to be modified or removed
			if (ordersList.containsKey(order.getOrderId())) {
				ti = byOrderBook.get(ordersList.get(order.getOrderId()));
				ti.handleRecord(bos);
			}
			// Adding new trading symbol to orderBook
			else {
				byOrderBook.put(order.getSymbol(), new TradesInfo(bos));
				ordersList.put(order.getOrderId(), symbol);
			}
		}
	}

	public Map<Long, String> getOrdersList() {
		return ordersList;
	}

	public void setOrdersList(Map<Long, String> ordersList) {
		this.ordersList = ordersList;
	}
	
	public Map<String, TradesInfo> getByOrderBook() {
		return byOrderBook;
	}

	public void setByOrderBook(Map<String, TradesInfo> byOrderBook) {
		this.byOrderBook = byOrderBook;
	}

}