package com.aity.orderbook.model;


public class BuyOrSell implements Comparable<Object> {

	private final Action m_action;
	private final Order m_order;
	private boolean isActive;


	public BuyOrSell(Action action, Order order) {
		this.m_action = action;
		this.m_order = order;
		this.isActive = true;
	}

	public String toString() {

		StringBuilder sb = new StringBuilder();
		sb.append(this.m_action).append(" ")
		.append(this.m_order.getOrderId()).append(" ")
		.append(this.m_order.isBuy()).append(" ")
		.append(this.m_order.getPrice()).append(" ")
		.append(this.m_order.getQuantity());

		return sb.toString();

	}

	@Override
	public int compareTo(Object o) {
		BuyOrSell bos = (BuyOrSell) o;
		return this.m_order.getPrice() - bos.m_order.getPrice();
	}

	public boolean equals(Object o) {
		if ((o instanceof BuyOrSell)
				&& (((BuyOrSell) o).m_action == this.m_action)
				&& (((BuyOrSell) o).m_order.getPrice() == m_order
				.getPrice())
				&& (((BuyOrSell) o).m_order.getQuantity() == m_order
				.getQuantity())) {
			return true;
		} else {
			return false;
		}
	}
	
	public int hashCode() {
		return (m_action + " " + m_order.getOrderId()).hashCode();
	}
	
	public boolean isActive() {
		return isActive;
	}

	public synchronized void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	
	public Action getAction() {
		return m_action;
	}

	public Order getOrder() {
		return m_order;
	}
}