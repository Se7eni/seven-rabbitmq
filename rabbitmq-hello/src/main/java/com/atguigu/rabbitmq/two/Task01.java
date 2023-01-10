package com.atguigu.rabbitmq.two;

import com.atguigu.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.Channel;

import java.util.Scanner;

/**
 * 生产者，可以发送大量消息
 */
public class Task01 {
    //队列名称
    public  static  final String QUEUE_NAME="hello";

    public static void main(String[] args) throws Exception {
        //获取信道
        Channel channel = RabbitMQUtils.getChannel();
        /**
         * 生成一个队列
         * 1：队列名称
         * 2：队列里面的消息是否持久化（磁盘），默认情况消息存储在内存中；true表示持久化存在磁盘，false表示不持久化存在内存
         * 3：该队列是否只供一个消费者进行消费，是否进行消息共享，true可以多个消费者消费，false只能一个消费者消费
         * 4：是否自动删除，最后一个消费者端开连接以后，该队列是否自动删除，true表示自动删除 false不自动删除
         * 5：其他参数
         */
        channel.queueDeclare(QUEUE_NAME,true,false,false,null);

        //从控制台接受消息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();
            /**
             * 发送一个消息
             * 1；发送到那个交换机
             * 2；路由的key值是哪个，本次是队列名称
             * 3；其他参数信息
             * 4；发送消息的消息体
             */
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
            System.out.println("消息发送完成:"+message);

        }


    }

}
