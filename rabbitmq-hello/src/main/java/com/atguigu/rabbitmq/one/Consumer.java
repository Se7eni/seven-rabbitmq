package com.atguigu.rabbitmq.one;

import com.atguigu.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.*;

/**
 * 消费者：接受消息
 */
public class Consumer {
    //队列名称
    public  static  final  String QUEUE_NAME ="hello";
    //接受消息
    public static void main(String[] args) throws Exception {
       /* //创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //工厂IP 连接rabbitmq的队列
        connectionFactory.setHost("127.0.0.1");
        //设置用户名
        connectionFactory.setUsername("guest");
        //设置密码
        connectionFactory.setPassword("guest");


        //创建连接
        Connection connection = connectionFactory.newConnection();*/
        //获取信道
        Channel channel = RabbitMQUtils.getChannel();

        //声明接收消息的回调
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println(new String(message.getBody()));
        };
        //取消消息时的回调
        CancelCallback cancelCallback = consumerTag ->{
            System.out.println("消息消费被中断");
        };

        /**
         * 消费者接受消息
         * 1：消费那个队列
         * 2：消费成功之后是否要自动应答，true代表自动应答，false代表手动应答
         * 3：消费者未成功消费的回调
         * 4：消费者取消消费的回调
         */
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
    }
}
