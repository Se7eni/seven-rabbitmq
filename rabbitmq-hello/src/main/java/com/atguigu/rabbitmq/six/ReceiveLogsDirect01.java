package com.atguigu.rabbitmq.six;

import com.atguigu.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

public class ReceiveLogsDirect01 {

    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        //声明一个队列
        channel.queueDeclare("console",false,false,false,null);

        //绑定信道
        channel.queueBind("console",EXCHANGE_NAME,"info");
        channel.queueBind("console",EXCHANGE_NAME,"warning");
        //接受消息回调函数
        DeliverCallback deliverCallback =(consumerTag, message)->{
            System.out.println("ReceiveLogsDirect01控制台打印接收到的消息："+new String(message.getBody()));
        };
        //取消消息回调函数
        CancelCallback cancelCallback =(consumerTag)->{

        };
        System.out.println("ReceiveLogsDirect01等待接受消息，把接收到的消息打印在屏幕上。。。");

        channel.basicConsume("console",true,deliverCallback,cancelCallback);

    }
}
