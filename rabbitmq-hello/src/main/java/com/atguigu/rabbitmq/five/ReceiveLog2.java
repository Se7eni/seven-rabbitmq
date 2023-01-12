package com.atguigu.rabbitmq.five;

import com.atguigu.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * 消息的接受
 */
public class ReceiveLog2 {

        //声明交换机的名称
        public static final String EXCHANGE_NAME="logs";

        public static void main(String[] args) throws Exception{
        Channel channel = RabbitMQUtils.getChannel();
        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
        //声明一个队列  临时队列
        /**
         * 生成一个临时队列，队列的名称是随机的
         * 当消费者断开与队列的连接的时候，队列就自动删除了
         */
        String queue = channel.queueDeclare().getQueue();
        /**
         * 绑定交换机与队列
         * 1：队列名称
         * 2：交换机名称
         * 3：routingKey 绑定的key  可以写""
         */
        channel.queueBind(queue,EXCHANGE_NAME,"2");

        //接受消息回调函数
        DeliverCallback deliverCallback =(consumerTag, message)->{
            System.out.println("ReceiveLog2控制台打印接收到的消息："+new String(message.getBody()));
        };
        //取消消息回调函数
        CancelCallback cancelCallback =(consumerTag)->{

        };
        System.out.println("ReceiveLog2等待接受消息，把接收到的消息打印在屏幕上。。。");

        channel.basicConsume(queue,true,deliverCallback,cancelCallback);


    }

}
