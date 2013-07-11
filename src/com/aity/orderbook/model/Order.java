package com.aity.orderbook.model;

/**
 * Represents an immutable Order.
 * 
 * @author Mantas
 */
public class Order
{
    private final long m_orderId;
    private final String m_symbol;
    private final boolean m_isBuy;
    private final int m_price;
    private final int m_quantity;

    public Order(long orderId, String symbol, boolean isBuy, int price, int quantity)
    {
        m_orderId = orderId;
        m_symbol = symbol;
        m_isBuy = isBuy;
        m_price = price;
        m_quantity = quantity;
    }

    public long getOrderId()
    {
        return m_orderId;
    }

    public String getSymbol()
    {
        return m_symbol;
    }

    public boolean isBuy()
    {
        return m_isBuy;
    }

    public int getPrice()
    {
        return m_price;
    }

    public int getQuantity()
    {
        return m_quantity;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("Order [m_orderId=");
        builder.append(m_orderId);
        builder.append(", m_symbol=");
        builder.append(m_symbol);
        builder.append(", m_isBuy=");
        builder.append(m_isBuy);
        builder.append(", m_price=");
        builder.append(m_price);
        builder.append(", m_quantity=");
        builder.append(m_quantity);
        builder.append("]");
        return builder.toString();
    }
}
