package com.atguigu.rabbitmq.four;

import com.atguigu.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.ConfirmListener;

import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * 发布确认模式
 * 使用的时间，比较那种确认方式是最好的
 * 1：单个确认发布 :缺点：发送速度特别慢
 * 2：批量确认发布 :缺点：发生故障导致发不出问题是，不知道是哪个消息出问题了
 * 3：异步确认发布 :
 * 优点：异步确认虽然编程逻辑上比上两个要复杂，但是性价比最高，无论是可靠性还是效率都没得说
 * 他是利用回调函数来达到消息可靠性传递的，这个中间件也是通过函数回调来保证是否投递成功
 */
public class ConfirmMessage {

    //批量发消息的个数
    public static final int MESSAGE_COUNT = 1000;

    public static void main(String[] args) throws Exception {
        //1：单个确认发布
        //发布条数：1000，单个确认消息耗时；620ms
        //publishMessageIndividually();
        //2：批量确认发布
        //发布条数：1000，批量确认消息耗时；66ms
        //publishMessageBatch();
        //3：异步确认发布
        //发布条数：1000，异步确认消息耗时；58ms
        publicshMessageAsync();
    }

    //单个确认
    public static void publishMessageIndividually() throws Exception {
        //获取信道
        Channel channel = RabbitMQUtils.getChannel();
        //队列的声明
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, false, false, false, null);
        //开启发布确认
        channel.confirmSelect();
        //开始时间
        long begin = System.currentTimeMillis();

        //批量发消息  单个发布确认
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes());
            //单个消息就马上进行发布确认
            boolean b = channel.waitForConfirms();
            if (b) {
                System.out.println("消息发送成功");
            }

        }
        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布条数：" + MESSAGE_COUNT + "，单个确认消息耗时；" + (end - begin) + "ms");

    }

    //批量发布确认
    public static void publishMessageBatch() throws Exception {
        //获取信道
        Channel channel = RabbitMQUtils.getChannel();
        //队列的声明
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, false, false, false, null);
        //开启发布确认
        channel.confirmSelect();
        //开始时间
        long begin = System.currentTimeMillis();

        //批量确认的长度
        int baechSize = 100;

        //批量发消息  批量发布确认
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes());

            //判断达到100条消息的时候，批量确认一次
            if (i % baechSize == 0) {
                //100条确认一次 发布确认
                boolean b = channel.waitForConfirms();
                if (b) {
                    System.out.println("消息发送成功");
                }
            }

        }


        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布条数：" + MESSAGE_COUNT + "，批量确认消息耗时；" + (end - begin) + "ms");
    }

    //异步发布确认
    public static void publicshMessageAsync() throws Exception {
        //获取信道
        Channel channel = RabbitMQUtils.getChannel();
        //队列的声明
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, false, false, false, null);
        //开启发布确认
        channel.confirmSelect();

        /**
         * 线程安全有序的一个哈希表，适用于高并发的情况下
         * 1：轻松的将序号与消息进行关联
         * 2：轻松的批量删除条目，只要给到序号
         * 3：支持高并发（多线程）
         */
        ConcurrentSkipListMap<Long, String> outstandingConfirms = new ConcurrentSkipListMap<>();

        //消息确认成功回调函数
        /**
         * 1:消息的标记
         * 2:是否为批量确认
         */
        ConfirmCallback ackCallback = (deliveryTag, multiple) -> {
            //2：删除掉已经确认的消息，剩下的就是未确认的消息
            if(multiple){
                ConcurrentNavigableMap<Long, String> confirmed = outstandingConfirms.headMap(deliveryTag);
                confirmed.clear();
            }else {
                outstandingConfirms.remove(deliveryTag);
            }

            System.out.println("确认的消息：" + deliveryTag);
        };
        //消息确认失败回调函数
        /**
         * 1:消息的标记
         * 2:是否为批量确认
         */
        ConfirmCallback nackCallback = (deliveryTag, multiple) -> {
            //3：打印一下未确认的消息有哪些
            System.out.println("未确认的消息标记：" + deliveryTag);
            //通过消息标记 获取消息
            String message = outstandingConfirms.get(deliveryTag);
            System.out.println("为确认的消息："+message);
        };
        //在发消息之前  准备消息的监听器，监听哪些消息成功，哪些消息失败
        /**
         * 1:监听哪些消息成功
         * 2:监听哪些消息失败
         */
        channel.addConfirmListener(ackCallback, nackCallback);//监听是异步通知

        //开始时间
        long begin = System.currentTimeMillis();
        //批量发送消息  异步确认
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = "消息" + i;
            channel.basicPublish("", queueName, null, message.getBytes());
            //1：此处记录下所有要发送的消息，消息的总和
            outstandingConfirms.put(channel.getNextPublishSeqNo(),message);
        }

        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布条数：" + MESSAGE_COUNT + "，异步确认消息耗时；" + (end - begin) + "ms");

    }
}
