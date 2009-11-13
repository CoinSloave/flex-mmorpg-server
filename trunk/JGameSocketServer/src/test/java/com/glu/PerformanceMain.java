/**
 * Copyright com.glu Group.
 */
package com.glu;

import java.io.IOException;
import java.util.LinkedList;

import org.testng.Assert;

import com.glu.rpc.proto.RpcChannelHandler;
import com.glu.rpc.test.TestProto.Result;
import com.glu.rpc.test.TestProto.TestService;
import com.glu.rpc.test.TestProto.User;
import com.glu.rpc.test.TestProto.TestService.Stub;
import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;

/**
 * @author yubingxing
 * 
 */
public class PerformanceMain {

	private static String host;
	private static int port;

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		if (args.length != 2) {
			System.out.println("测试线程池中的任务。");
			System.out.println("使用 ：java ThreadPoolTest numTasks numThreads");
			System.out.println("    numTasks - integer: number of task to run.");
			System.out.println("    numThreads - integer: number of threads in the thread pool.");
			return;
		}
		host = "localhost";
		port = 12345;

		int numTasks = Integer.parseInt(args[0]);
		int numThreads = Integer.parseInt(args[1]);

		// 创建线程池
		ThreadPool threadPool = new ThreadPool(numThreads);

		// 运行线程池中的任务
		for (int i = 0; i < numTasks; i++) {
			threadPool.runTask(createTask(i));
		}

		// 等待所有任务结束并关闭线程池
		threadPool.join();
		System.out.println("所有任务执行完毕！");
	}

	/**
	 * Creates a simple Runnable that prints an ID, waits 500 milliseconds, then
	 * 
	 * @param taskId
	 * @return
	 */
	private static Runnable createTask(final int taskId) {
		return new Runnable() {
			public void run() {
				System.out.println("任务 " + taskId + " : 启动");
				// 模拟客户端连接
				RpcChannelHandler channel = new RpcChannelHandler(host, port);
				RpcController controller = channel.newRpcController();
				Stub service = TestService.newStub(channel);
				// 模拟请求
				String reqdata = "yubingxing";
				User request = User.newBuilder().setUserName(reqdata).build();

				// 模拟回复
				RpcCallback<Result> done = new RpcCallback<Result>() {
					@Override
					public void run(Result result) {
						Assert.assertEquals(result.getResult(), "get user : yubingxing");
						Assert.assertTrue(result.getSuccess());
					}
				};
				// 模拟执行回调函数
				service.testMethod(controller, request, done);
				Assert.assertFalse(controller.failed());
				Assert.assertEquals(controller.errorText(), null);
				System.out.println("任务 " + taskId + " : 结束");
			}
		};
	}

	/**
	 * A thread pool is a group of a limited number of threads that are used to
	 * execute tasks.
	 * 
	 * @author yubingxing
	 * 
	 */
	static class ThreadPool extends ThreadGroup {
		private boolean isAlive;

		private LinkedList<Runnable> taskQueue;

		private int threadId;

		private static int threadPoolId = 0;

		public ThreadPool(int numThreads) {
			super("ThreadPool-" + (threadPoolId++));
			setDaemon(true);

			isAlive = true;

			taskQueue = new LinkedList<Runnable>();
			for (int i = 0; i < numThreads; i++) {
				new PooledThread().start();
			}
		}

		/**
		 * 向线程池中添加任务
		 * 
		 * @param task
		 */
		public synchronized void runTask(Runnable task) {
			if (!isAlive) {
				throw new IllegalStateException();
			}
			if (task != null) {
				taskQueue.add(task);
				notify();
			}
		}

		/**
		 * 取得当前执行的任务
		 * 
		 * @return
		 * @throws InterruptedException
		 */
		protected synchronized Runnable getTask() throws InterruptedException {
			while (taskQueue.size() == 0) {
				if (!isAlive) {
					return null;
				}
				wait();
			}
			return (Runnable) taskQueue.removeFirst();
		}

		/**
		 * 关闭线程池
		 */
		public synchronized void close() {
			if (isAlive) {
				isAlive = false;
				taskQueue.clear();
				interrupt();
			}
		}

		/**
		 * 等待所有任务结束并关闭线程池
		 */
		public void join() {
			// 同步线程池，唤醒所有等待线程
			synchronized (this) {
				isAlive = false;
				notifyAll();
			}

			// 创建线程组
			Thread[] threads = new Thread[activeCount()];
			// 复制线程到线程组
			int count = enumerate(threads);
			for (int i = 0; i < count; i++) {
				try {
					// 等待线程结束
					threads[i].join();
				} catch (InterruptedException ex) {
				}
			}
		}

		/**
		 * A PooledThread is a Thread in a ThreadPool group, designed to run
		 * tasks
		 * 
		 * @author yubingxing
		 * 
		 */
		private class PooledThread extends Thread {
			public PooledThread() {
				super(ThreadPool.this, "PooledThread-" + (threadId++));
			}

			public void run() {
				while (!isInterrupted()) {
					Runnable task = null;
					try {
						task = getTask();
					} catch (InterruptedException e) {
					}

					// 如果 getTask() 为 null 或者被打断，则关闭线程并返回
					if (task == null)
						return;
					// 启动线程，并消除所有异常
					try {
						task.run();
					} catch (Throwable t) {
						uncaughtException(this, t);
					}
				}
			}
		}
	}
}
