package com.krish.retrofit.cache;

import java.sql.SQLException;

import com.hazelcast.config.Config;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.core.IQueue;

/**
 * Hello world!
 *
 */
public class CacheProvider 
{	
	public static String memberName;
	public final static String cacheGroupName = "AdminGating";
	public final static String cachePassword = "password";

	private static HazelcastInstance cacheInstance;
	private static CacheProvider cache;

	private CacheProvider(){
	}
	static{
		try{
			System.out.println("test");
			Config memberConfig = new Config();
	        memberConfig.getGroupConfig().setName(cacheGroupName).setPassword(cachePassword);
	        NetworkConfig network = memberConfig.getNetworkConfig();
	        network.setPort(5701).setPortCount(20);
	        network.setPortAutoIncrement(true);
	        JoinConfig join = network.getJoin();
	        join.getMulticastConfig().setEnabled(false);
	        join.getTcpIpConfig().addMember("192.168.1.11").addMember("192.168.1.9").setEnabled(true);
	        cacheInstance = Hazelcast.newHazelcastInstance(memberConfig);
		}catch(Exception e){
			
		}
	}
	public static CacheProvider getCacheProvider(){
		if(null == cache){
			cache = new CacheProvider();
		}
		return cache;
	}
	public int getCacheSize() throws SQLException{
		  IQueue<String> cacheQ = cacheInstance.getQueue("AdminData");
		  IMap<Integer, GatingTableData> gatingDataCache = cacheInstance.getMap("GatingData");
		  for(Integer key :H2DataBaseProvider.getAllTableData("PERSON").keySet()) {
			  System.out.println("__"+key+"----"+H2DataBaseProvider.getAllTableData("PERSON").get(key));
		  }
		  System.out.println("Size ::: "+cacheQ.size());
		return cacheQ.size() ;
	}
	public void updateCache(){
		IQueue<String> cacheQ = cacheInstance.getQueue("AdminData");
		cacheQ.add("ddddd");
	}
}

/*
 * Clients use 
 * CacheProvider.getCacheProvider().getCacheSize();
   CacheProvider.getCacheProvider().updateCache();
   CacheProvider.getCacheProvider().getCacheSize();
 */