package com.atguigu.rabbitmq.two;

import com.atguigu.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * 这是一个工作线程【相当于原来的消费者】
 */
public class Worker01 {

    //队列的名称  大小写转换ctrl+shift+u
    public static  final  String QUEUE_NAME="hello";

    //接受消息
    public static void main(String[] args) throws Exception {
        //通过工具类直接获取信道
        Channel channel = RabbitMQUtils.getChannel();

        //声明接收消息的回调
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("接收到的消息："+new String(message.getBody()));
        };
        //取消消息时的回调
        CancelCallback cancelCallback = consumerTag ->{
            System.out.println(consumerTag+"消费者取消消费接口的回调逻辑");
        };

        /**
         * 消费者接受消息
         * 1：消费那个队列
         * 2：消费成功之后是否要自动应答，true代表自动应答，false代表手动应答
         * 3：消费者未成功消费的回调
         * 4：消费者取消消费的回调
         */
        System.out.println("C2等待接受消息。。。。。");
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);




    }
}
