package com.aity.orderbook.model;

/**
 * Data structure that holds data based by price
 * @author Mantas
 *
 */
public class ByPrice implements Comparable<Object> {
	private int price;
	private int quantity;
	private int orders;

	public ByPrice(int p, int q, int o) {
		this.price 		= p;
		this.quantity 	= q;
		this.orders 	= o;
	}

	/**
	 * @return the price
	 */
	public int getPrice() {
		return price;
	}

	/**
	 * @param price
	 *            the price to set
	 */
	public void setPrice(int price) {
		this.price = price;
	}

	/**
	 * @return the quantity
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity
	 *            the quantity to set
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return the orders
	 */
	public int getOrders() {
		return orders;
	}

	/**
	 * @param orders
	 *            the orders to set
	 */
	public void setOrders(int orders) {
		this.orders = orders;
	}

	public boolean equals(Object o) {
		if ((o instanceof ByPrice)
				&& (((ByPrice) o).getPrice() == this.getPrice())) {
			return true;
		} else {
			return false;
		}
	}

	public int hashCode() {
		return this.price;
	}

	@Override
	public int compareTo(Object o) {
		ByPrice b = (ByPrice) o;
		return price - b.price;
	}
}