package com.atguigu.rabbitmq.seven;

import com.atguigu.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * 声明主体交换机和相关队列
 */
public class ReceiveLogsTopic01 {

    public static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws Exception {
        //获取信道
        Channel channel = RabbitMQUtils.getChannel();
        //声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        //声明队列
        String queueName = "Q1";
        /**
         * 生成一个队列
         * 1：队列名称
         * 2：队列里面的消息是否持久化（磁盘），默认情况消息存储在内存中；true表示持久化存在磁盘，false表示不持久化存在内存
         * 3：该队列是否只供一个消费者进行消费，是否进行消息共享，true可以多个消费者消费，false只能一个消费者消费
         * 4：是否自动删除，最后一个消费者端开连接以后，该队列是否自动删除，true表示自动删除 false不自动删除
         * 5：其他参数
         */
        channel.queueDeclare(queueName,false,false,false,null);
        //绑定
        channel.queueBind(queueName,EXCHANGE_NAME,"*.orange.*");
        System.out.println("等待接受消息。。。。");


        //接受消息回调函数
        DeliverCallback deliverCallback =(consumerTag, message)->{
            System.out.println("ReceiveLogsTopic01控制台打印接收到的消息："+new String(message.getBody()));
            System.out.println("接受队列："+queueName+"  绑定键："+message.getEnvelope().getRoutingKey());
        };
        //取消消息回调函数
        CancelCallback cancelCallback =(consumerTag)->{

        };
        System.out.println("ReceiveLogsTopic01等待接受消息，把接收到的消息打印在屏幕上。。。");

        channel.basicConsume(queueName,deliverCallback,cancelCallback);
    }
}
