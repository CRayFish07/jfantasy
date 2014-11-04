package com.fantasy.framework.comet;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;

class Producer implements Runnable {
	private BlockingQueue<String> drop;
	List<String> messages = Arrays.asList(new String[] { "Mares eat oats", "Does eat oats", "Little lambs eat ivy", "Wouldn't you eat ivy too?" });

	public Producer(BlockingQueue<String> d) {
		this.drop = d;
	}

	public void run() {
		try {
			int i = 0;
			for (String s : this.messages) {
				this.drop.put(s);
				System.out.println(this.drop.size());
				Thread.sleep(1000L);
			}
			this.drop.put("DONE");
		} catch (InterruptedException intEx) {
			intEx.printStackTrace();
		}
	}
}