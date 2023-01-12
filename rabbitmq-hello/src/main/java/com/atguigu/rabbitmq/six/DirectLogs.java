package com.atguigu.rabbitmq.six;

import com.atguigu.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.util.Scanner;

/**
 * 绑定相同的交换机时，绑定类型为BuiltinExchangeType.DIRECT，通过routingKey确定你要发送给谁，与routingKey有关
 */
public class DirectLogs {

    //声明交换机的名称
    public static final String EXCHANGE_NAME="direct_logs";

    public static void main(String[] args) throws Exception {
        //获取信道
        Channel channel = RabbitMQUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();
            //通过routingKey确定你要发送给谁
            channel.basicPublish(EXCHANGE_NAME,"error",null,message.getBytes("UTF-8"));
            System.out.println("生产者成功发送消息："+message);
        }




    }
}
