package com.aity.orderbook.model;


public class Command {
    private final Action m_action;
    private final Order m_order;

    public Command(Action action,
                   long orderId,
                   String symbol,
                   boolean isBuy,
                   int price,
                   int quantity)
    {
        m_action = action;
        m_order = new Order(orderId, symbol, isBuy, price, quantity);
    }

    public Action getAction()
    {
        return m_action;
    }

    public Order getOrder()
    {
        return m_order;
    }
    
    public boolean equals(Object o) {
		if ((o instanceof Command)
				&& (((Command) o).m_action == this.m_action)
				&& (((Command) o).m_order.getPrice() == m_order.getPrice())
				&& (((Command) o).m_order.getOrderId() == m_order.getOrderId())
				&& (((Command) o).m_order.getSymbol() == m_order.getSymbol())
				&& (((Command) o).m_order.getQuantity() == m_order.getQuantity())) {
			return true;
		} else {
			return false;
		}
	}
}
