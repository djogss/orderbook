package com.aity.orderbook.processor;

import java.util.Iterator;

import com.aity.orderbook.consumer.OrderConsumerImpl;
import com.aity.orderbook.model.TradesInfo;

public class OrderProcessor implements Runnable {

	OrderConsumerImpl oc;

	public OrderProcessor(OrderConsumerImpl oc) {
		this.oc = oc;
		
	}

	@Override
	public void run() 
	{
		int bookSize = oc.getOrdersList().size();
		int retry = 0;
		Iterator<String> itOuter;
		TradesInfo ti;
		String key;
		while (true) {
			synchronized(oc){
				if (!oc.getByOrderBook().isEmpty()) {
					itOuter = oc.getByOrderBook().keySet().iterator();
					while (itOuter.hasNext()) 
					{
						key = itOuter.next();
						ti = oc.getByOrderBook().get(key);
						ti.buildByPriceBook();
					}
				}
			}

			synchronized(oc){
				if(!oc.getOrdersList().isEmpty() && bookSize == oc.getOrdersList().size()){
					if(retry == 25) {						
						break;
					} else {
						retry++;
					}
					
				}else {
					bookSize = oc.getOrdersList().size();
					retry = 0;
				}
			}
		}

	}
}