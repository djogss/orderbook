package com.aity.orderbook;

import com.aity.orderbook.app.AppEnvironmentImpl;
import com.aity.orderbook.consumer.OrderConsumerImpl;
import com.aity.orderbook.iface.AppEnvironment;
import com.aity.orderbook.iface.LogLevel;

/**
 * This class acts as the entry point to the  Development Test.
 * 
 * @author Mantas
 */
public class AppRunner
{
    /**
     * Pretty self-explanatory..
     * 
     * @param args
     */
    public static void main(String[] args)
    {
        AppEnvironment environment = new AppEnvironmentImpl(LogLevel.INFO);
        environment.registerOrderConsumer(new OrderConsumerImpl());
        environment.run();
    }
}
