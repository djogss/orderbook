package com.aity.orderbook.app;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.aity.orderbook.dal.InputDataManager;
import com.aity.orderbook.dal.XMLDataInputImpl;
import com.aity.orderbook.iface.AppEnvironment;
import com.aity.orderbook.iface.Log;
import com.aity.orderbook.iface.LogLevel;
import com.aity.orderbook.iface.OrderConsumer;
import com.aity.orderbook.model.Action;
import com.aity.orderbook.model.Command;
import com.aity.orderbook.model.Order;

/**
 * Concrete implementation of the AppEnvironment interface.
 * 
 * @author Mantas
 */
public class AppEnvironmentImpl implements AppEnvironment
{
    private final Set<OrderConsumer> m_orderConsumers = new LinkedHashSet<OrderConsumer>();
    private final LogLevel m_logLevel;

    protected final Log m_log = new Log()
    {
        @Override
        public void log(LogLevel logLevel, String msg)
        {
            if(isEnabled(logLevel))
            {
                System.out.println(logLevel + ": " + msg);
            }

        }

        private boolean isEnabled(LogLevel logLevel)
        {
            return logLevel.compareTo(AppEnvironmentImpl.this.m_logLevel) >= 0;
        }
    };

    /**
     * Default constructor that takes the LogLevel you want applied.
     * 
     * @param logLevel
     */
    public AppEnvironmentImpl(LogLevel logLevel)
    {
        m_logLevel = logLevel;
    }


    @Override
    public void registerOrderConsumer(OrderConsumer orderConsumer)
    {
        m_orderConsumers.add(orderConsumer);
    }


    @Override
    public final void run()
    {
        notifyStart();
        try
        {
            feedOrders();
        }
        catch(Exception e)
        {
            m_log.log(LogLevel.ERROR, e.getMessage());
        }
        finally
        {
            notifyFinish();
        }
    }

    /**
     * Sends a stream of orders to the {@link OrderConsumer}s.
     * 
     * @see #notifyOrder(Action, Order)
     * @throws Exception if there is an error.
     */
    protected void feedOrders() throws Exception
    {
      
        List<Command> commands 	= new ArrayList<Command>();
        InputDataManager idm 	= new XMLDataInputImpl();
        commands 				= idm.loadData();
       
        for(Command command : commands)
        {
            notifyOrder(command.getAction(), command.getOrder());
        }

    }

    /**
     * Invokes {@link OrderConsumer#handleEvent(Action, Order)} for every registered consumer with
     * specified <code>action</code> and <code>order</code>.
     * 
     */
    
    protected final void notifyOrder(Action action, Order order)
    {
    	
        for(OrderConsumer consumer : m_orderConsumers)
        {
        	synchronized(m_orderConsumers){
        		consumer.handleEvent(action, order);
        	}
         
    	}
    }

    private final void notifyStart()
    {
        for(OrderConsumer consumer : m_orderConsumers)
        {
            consumer.startProcessing(m_log);
        }
    }

    private final void notifyFinish()
    {
        for(OrderConsumer consumer : m_orderConsumers)
        {
            consumer.finishProcessing();
        }
    }
}