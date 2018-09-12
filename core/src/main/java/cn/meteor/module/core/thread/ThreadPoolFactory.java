package cn.meteor.module.core.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

public class ThreadPoolFactory {

//	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private ExecutorService pool;
	private int nThreads;
	
	public ExecutorService getFixedThreadPool() {
		if(pool==null) {
			pool = Executors.newFixedThreadPool(nThreads);
		}
		return pool;
	}

	public void setNThreads(int nThreads) {
		this.nThreads = nThreads;
	}
}
