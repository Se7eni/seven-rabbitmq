package com.atguigu.rabbitmq.five;

import com.atguigu.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * RabbitMQ消息传递模型的核心思想是：生产者生产的消息从不会直接发送到队列，生产者只能将消息发送到交换机
 * Exchanges（交换机）的类型：直接（direct），主题（topic），标题（headers），扇出（fanout）
 * 		第一个参数就是交换机的名称，空字符串表示默认或者无名称交换机，消息能路由发送其实是由
 * 		routingKey（bindingkey）绑定key指定的
 *
 *
 * 消息的接受
 */
public class ReceiveLog1 {
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
        channel.queueBind(queue,EXCHANGE_NAME,"1");

        //接受消息回调函数
        DeliverCallback deliverCallback =(consumerTag, message)->{
            System.out.println("ReceiveLog1控制台打印接收到的消息："+new String(message.getBody()));
        };
        //取消消息回调函数
        CancelCallback cancelCallback =(consumerTag)->{

        };
        System.out.println("ReceiveLog1等待接受消息，把接收到的消息打印在屏幕上。。。");

        channel.basicConsume(queue,true,deliverCallback,cancelCallback);


    }
}
