package jedis.test;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * spring data redis 整合 jedis
 *
 * @author 闫惠甜娇
 * @version 1.0，2017年11月3日
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class RedisTemplateTest {
	
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	@Test
	public void test01() {
		redisTemplate.opsForValue().set("username", "aaa");
		String username = redisTemplate.opsForValue().get("username");
		System.out.println(username);
		
		redisTemplate.opsForValue().set("password", "123", 10, TimeUnit.SECONDS);
		System.out.println(redisTemplate.opsForValue().get("password"));
	}
	
}
