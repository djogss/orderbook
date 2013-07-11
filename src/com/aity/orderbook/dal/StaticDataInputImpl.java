package com.aity.orderbook.dal;


import java.util.ArrayList;
import java.util.List;

import com.aity.orderbook.model.Action;
import com.aity.orderbook.model.Command;

public class StaticDataInputImpl implements InputDataManager{

	
	
	@Override
	public synchronized List<Command> loadData() {

		List<Command> commands 	= new ArrayList<Command>();
		commands.add(new Command(Action.ADD, 1L, "MSFT.L", true, 5, 200));
		commands.add(new Command(Action.ADD, 2L, "VOD.L", true, 15, 100));
		commands.add(new Command(Action.ADD, 3L, "MSFT.L", false, 5, 300));
		commands.add(new Command(Action.ADD, 4L, "MSFT.L", true, 7, 150));
		commands.add(new Command(Action.REMOVE, 1L, null, true, -1, -1));
		commands.add(new Command(Action.ADD, 5L, "VOD.L", false, 17, 300));
		commands.add(new Command(Action.ADD, 6L, "VOD.L", true, 12, 150));
		commands.add(new Command(Action.EDIT, 3L, null, true, 7, 200));
		commands.add(new Command(Action.ADD, 7L, "VOD.L", false, 16, 100));
		commands.add(new Command(Action.ADD, 8L, "VOD.L", false, 19, 100));
		commands.add(new Command(Action.ADD, 9L, "VOD.L", false, 21, 112));
		commands.add(new Command(Action.REMOVE, 5L, null, false, -1, -1));
		
		return commands;
		
	}
	
	
}