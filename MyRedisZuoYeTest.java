package com.xiexin.redistest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)  //使用spring的Junit测试
@ContextConfiguration({"classpath:applicationContext.xml"})  //模拟ssm框架运行后加载xml容器
public class MyRedisZuoYeTest {
    @Autowired
    private JedisPool jedisPool;

    //用java代码写，把咱们班33个人的名字 形成 一个集合， 
    //运行后随机点一个人的名字，就把这个人的名字移除。 再次
    //点名是 点 32个人的随机中的一个。
    @Test
    public void test01() {
        Boolean clazz = jedisPool.getResource().exists("clazz");
        if (clazz) {
            String strings1 = jedisPool.getResource().srandmember("clazz");
            System.out.println("点到的人是 = " + strings1);
            jedisPool.getResource().srem("clazz", strings1);
            Long clazz1 = jedisPool.getResource().scard("clazz");
            System.out.println("还剩下" + clazz1 + "个人");

        } else {
            System.out.println("没有这个表，请重新运行创建表");
            jedisPool.getResource().sadd("clazz", "白世纪", "陈红利", "陈世纪", "陈洋洋", "杜晓梦", "付春辉", "高芳芳", "郭旭", "胡艺果", "贾礼博", "李雪莹", "李祎豪", "林梦娇", "刘顺顺", "卢光辉", "吕亚伟", "宁静静", "牛志阳", "史倩影", "宋健行", "孙超阳", "孙乾力", "田君垚", "汪高洋", "王学斌", "杨天枫", "杨原辉", "袁仕奇", "张浩宇", "张晓宇", "张志鹏", "赵博苛", "邹开源");

        }
    }


    //使用 java 代码编写，
    //有一个双端队列集合， 里面有 10 条数据，
    //查询出  第5个人是什么数据，
    //左边弹出1个 ， 右边弹出1个，打印还剩多少条数据，
    //然后，再 第3个数据前面，插入一个数据，
    //然后，进行查询全部数据进行查看。
    @Test
    public void test02() {
        jedisPool.getResource().lpush("queue", "大娃", "二娃", "三娃", "四娃", "五娃");
        jedisPool.getResource().rpush("queue", "六娃", "七娃", "八娃", "九娃", "十娃");
        String queue = jedisPool.getResource().lindex("queue", 4);
        System.out.println("第五个人是 = " + queue);
        String queue1 = jedisPool.getResource().lpop("queue");
        System.out.println("左边弹出 = " + queue1);
        String queue2 = jedisPool.getResource().rpop("queue");
        System.out.println("右边弹出 = " + queue2);
        Long queue3 = jedisPool.getResource().llen("queue");
        System.out.println("还剩 = " + queue3);
        Long linsert = jedisPool.getResource().linsert("queue", BinaryClient.LIST_POSITION.BEFORE, "三娃", "我是插入的");
        List<String> queue4 = jedisPool.getResource().lrange("queue", 0, -1);
        for (String s : queue4) {
            System.out.println("s = " + s);
        }
    }

    @Test
    public void test03() {
        // 查看key是否存在
        Boolean xnams = jedisPool.getResource().exists("xnames");
        if (xnams) {
            System.out.println("存在");
        } else {
            System.out.println("不存在");
        }
    }

    @Test
    public void test04() {
        //查找满足 x开头的   的keys（需要在数据库中添加若干个 x开头的 几个单词）
        jedisPool.getResource().set("xname", "小乔");
        jedisPool.getResource().set("xclazz", "大乔");
        jedisPool.getResource().set("xstudent", "周瑜");
        Set<String> keys = jedisPool.getResource().keys("x*");
        for (String key : keys) {
            System.out.println("key = " + key);
        }

    }

    @Test
    public void test05() {
        //查看key的超时时间
        Long xnames = jedisPool.getResource().ttl("xnames");
        System.out.println("xnames = " + xnames);
    }

    @Test
    public void test06() {
        //遍历key
        Set<String> keys = jedisPool.getResource().keys("*");
        for (String key : keys) {
            System.out.println("key = " + key);
        }
    }

    @Test
    public void test07() {
        //返回key的值的序列化
        byte[] xnames = jedisPool.getResource().dump("xnames");
        System.out.println("xnames = " + xnames);
    }

    //string类型数据的命令操作
    @Test
    public void test08() {
        //设置键值
        String score = jedisPool.getResource().set("score", "95");
        System.out.println("score = " + score);
        //读取键值
        String score1 = jedisPool.getResource().get("score");
        System.out.println("score1 = " + score1);
        //数值类型自增1
        Long score2 = jedisPool.getResource().incr("score");
        System.out.println("score2 = " + score2);
        //数值类型自减1
        Long score3 = jedisPool.getResource().decr("score");
        System.out.println("score3 = " + score3);
        //查看值的长度
        Long score4 = jedisPool.getResource().strlen("score");
        System.out.println("score4 = " + score4);
    }

    //list类型数据的命令操作
    @Test
    public void test09() {
        //对列表city插入元素：nanjing Suzhou Hangzhou wuxi
        // jedisPool.getResource().lpush("city","nanjing","Suzhou","Hangzhou","wuxi");
        jedisPool.getResource().lpush("name", "xiaoqiao", "daqiao", "zhouyu", "luban");
        jedisPool.getResource().lpush("number", "1", "2", "3", "4");
        //将列表city里的头部的元素移除
//        String city1 = jedisPool.getResource().lpop("city");
//        System.out.println("city1 = " + city1);
        //将name列表的尾部元素移除到number列表的头部
        jedisPool.getResource().rpoplpush("name", "number");
        //对一个已存在的列表插入新元素
        Long rpushx = jedisPool.getResource().rpushx("city", "shanghai");
        System.out.println("rpushx = " + rpushx);
        //查看list的值长度
        Long city = jedisPool.getResource().llen("city");
        System.out.println("city = " + city);
    }

    //hash类型数据的命令操作
    @Test
    public void test10() {
        //设置一个hash表，order表里包括的键值信息有：id：1,customer_name：张三
        jedisPool.getResource().hset("order", "id", "1");
        jedisPool.getResource().hset("order", "customer_name", "张三");
        // 创建一个hash表，表里的键值批量插入
        // 获取order对应的map的所有key
        jedisPool.getResource().hkeys("order");
        //获取order对应的map的键值数量
        Long order = jedisPool.getResource().hlen("order");
        System.out.println("order = " + order);
        //获取order表里的id值
        String hget = jedisPool.getResource().hget("order", "id");
        System.out.println("hget = " + hget);
    }


}
