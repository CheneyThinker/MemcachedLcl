package org.memcached.lcl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import net.spy.memcached.CASResponse;
import net.spy.memcached.CASValue;
import net.spy.memcached.MemcachedClient;

public class MemcachedLcl {
	
	public static void main(String[] args) {
		MemcachedClient memcachedClient = null;
		try {
			memcachedClient = new MemcachedClient(new InetSocketAddress("127.0.0.1", 11211));
			System.out.println("Connection to server sucessful.");
			
			Future<Boolean> future = memcachedClient.set("lcl", 1314, "Love");
			System.out.println(String.format("set status: %b", future.get()));
			System.out.println(String.format("lcl value in cache: %s", memcachedClient.get("lcl")));
			
			future = memcachedClient.add("lcl", 1314, "lcl");
			System.out.println(String.format("add status: %b", future.get()));
			System.out.println(String.format("lcl value in cache: %s", memcachedClient.get("lcl")));
			
			future = memcachedClient.replace("lcl", 1314, "lcl");
			System.out.println(String.format("replace status: %b", future.get()));
			System.out.println(String.format("lcl value in cache: %s", memcachedClient.get("lcl")));
			
			future = memcachedClient.add("cheney", 1314, "lcl");
			System.out.println(String.format("add status: %b", future.get()));
			System.out.println(String.format("cheney value in cache: %s", memcachedClient.get("cheney")));
			
			future = memcachedClient.append("cheney", " love thinker");
			System.out.println(String.format("append status: %b", future.get()));
			System.out.println(String.format("cheney value in cache: %s", memcachedClient.get("cheney")));
			
			future = memcachedClient.prepend("cheney", "thinker love ");
			System.out.println(String.format("prepend status: %b", future.get()));
			System.out.println(String.format("cheney value in cache: %s", memcachedClient.get("cheney")));
			
			CASValue<Object> casValue = memcachedClient.gets("cheney");
			System.err.println(String.format("casValue %s", casValue));
			CASResponse casResponse = memcachedClient.cas("cheney", casValue.getCas(), 1314, "lcl love thinker");
			System.err.println(String.format("casResponse %s", casResponse));
			System.out.println(String.format("cheney value in cache: %s", memcachedClient.get("cheney")));
			
			future = memcachedClient.delete("cheney");
			System.out.println(String.format("delete status: %b", future.get()));
			System.out.println(String.format("cheney value in cache: %s", memcachedClient.get("cheney")));
			
			future = memcachedClient.set("salary", 1314, "1000");
			System.out.println(String.format("set status: %b", future.get()));
			System.out.println(String.format("salary value in cache: %s", memcachedClient.get("salary")));
			System.out.println(String.format("salary in cache after increment: %d", memcachedClient.incr("salary", 1314)));
			System.out.println(String.format("salary in cache after decrement: %d", memcachedClient.decr("salary", 1000)));
			
			//statistics command
			Map<SocketAddress, Map<String, String>> map = memcachedClient.getStats();
			for (Map.Entry<SocketAddress, Map<String, String>> outerEntry : map.entrySet()) {
				System.err.println(outerEntry.getKey());
				for (Map.Entry<String, String> innerEntry : outerEntry.getValue().entrySet()) {
					System.err.println(innerEntry.getKey());
					System.err.println(innerEntry.getValue());
				}
			}
			
			future = memcachedClient.flush();
			System.out.println(String.format("flush status: %b", future.get()));
			System.out.println(String.format("salary value in cache: %s", memcachedClient.get("salary")));
		} catch (IOException | InterruptedException | ExecutionException e) {
		} finally {
			if (memcachedClient != null) {
				memcachedClient.shutdown();
			}
		}
	}

}
