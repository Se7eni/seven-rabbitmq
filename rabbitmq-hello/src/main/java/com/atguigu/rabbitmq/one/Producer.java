package com.atguigu.rabbitmq.one;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 生产者：发送消息
 */
public class Producer {
    //队列名称
    public  static  final  String QUEUE_NAME ="hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        //创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //工厂IP 连接rabbitmq的队列
        connectionFactory.setHost("127.0.0.1");
        //设置用户名
        connectionFactory.setUsername("guest");
        //设置密码
        connectionFactory.setPassword("guest");

        
        //创建连接
        Connection connection = connectionFactory.newConnection();
        //获取信道
        Channel channel = connection.createChannel();
        /**
         * 生成一个队列
         * 1：队列名称
         * 2：队列里面的消息是否持久化（磁盘），默认情况消息存储在内存中；true表示持久化存在磁盘，false表示不持久化存在内存
         * 3：该队列是否只供一个消费者进行消费，是否进行消息共享，true可以多个消费者消费，false只能一个消费者消费
         * 4：是否自动删除，最后一个消费者端开连接以后，该队列是否自动删除，true表示自动删除 false不自动删除
         * 5：其他参数
         */
        channel.queueDeclare(QUEUE_NAME,true,false,false,null);

        //准备发消息
        String message="hello world";
        /**
         * 发送一个消息
         * 1；发送到那个交换机
         * 2；路由的key值是哪个，本次是队列名称
         * 3；其他参数信息
         * 4；发送消息的消息体
         */
        channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
        System.out.println("消息发送完成！！！");

    }
}
/**
 * MQ的功能：流量消峰，应用解耦，异步处理
 * RibbitMQ的四大核心概念：生产者，交换机，队列，消费者
 * RibbitMQ的六大模式：简单模式（Hello World），工作模式（Work queues），
 * 					发布订阅模式（Publish/Subscribe），路由模式(Routing)，
 * 					主题模式(Topics)，发布确认模式(Publisher Confirms)
 * Producer-->Connection(多个Channel)-->Broker（RabbitMQ（Exchange-->多个Queue））-->Connection(多个Channel)-->Consumer				
 */