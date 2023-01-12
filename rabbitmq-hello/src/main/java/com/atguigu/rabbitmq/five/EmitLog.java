package com.atguigu.rabbitmq.five;

import com.atguigu.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.util.Scanner;

/**
 * 发消息  交换机
 * 绑定相同的交换机时，绑定类型为BuiltinExchangeType.FANOUT，无论routingKey填写的什么，都会接受到消息，与routingKey无关
 */
public class EmitLog {
    //声明交换机的名称
    public static final String EXCHANGE_NAME="logs";

    public static void main(String[] args) throws Exception {
        //获取信道
        Channel channel = RabbitMQUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();
            channel.basicPublish(EXCHANGE_NAME,"",null,message.getBytes("UTF-8"));
            System.out.println("生产者成功发送消息："+message);
        }




    }


}
