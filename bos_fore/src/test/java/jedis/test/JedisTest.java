package jedis.test;

import org.junit.Test;

import redis.clients.jedis.Jedis;

/**
 * jedis的测试类
 *
 * @author 闫惠甜娇
 * @version 1.0，2017年11月3日
 */
public class JedisTest {
	@Test
	public void test01() {
		Jedis jedis = new Jedis("localhost",5379);
		jedis.set("a", "1");
		System.out.println(jedis.get("a"));
		
		jedis.setex("b", 30, "10");
		Long ttl = jedis.ttl("b");
		System.out.println(ttl);
		
	}
}
