package com.aity.orderbook.iface;

/**
 * Specifies the behaviour of the application environment. <br />
 * Typical usage:
 * 
 * <pre>
 * <code>
 * AppEnvironment env = ...
 * env.registerHandler(handler1);
 * env.registerHandler(handler2);
 * env.run();
 * </code>
 * </pre>
 * 
 * @author Mantas
 */
public interface AppEnvironment
{
    /**
     * Registers the given OrderConsumer, such that it will be notified of order based events.
     * 
     * @param orderConsumer
     */
    void registerOrderConsumer(OrderConsumer orderConsumer);

    /**
     * Performs a run of order processing
     */
    void run();

}
