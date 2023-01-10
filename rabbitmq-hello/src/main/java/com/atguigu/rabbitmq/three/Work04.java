package com.atguigu.rabbitmq.three;

import com.atguigu.rabbitmq.utils.RabbitMQUtils;
import com.atguigu.rabbitmq.utils.SleepUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * 消息在手动应答时是不丢失，放回队列中重新消费
 */
public class Work04 {
    //队列名称
    public static  final  String TASK_QUEUE_NAME="ack_queue";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMQUtils.getChannel();
        System.out.println("C2等待接受消息处理————时间较长");

        //声明接收消息的回调
        DeliverCallback deliverCallback = ( consumerTag,  message)->{
            //沉睡1s
            SleepUtils.sleep(30);
            System.out.println("接收到的消息："+new String(message.getBody()));
            //需要手动应答
            /**
             * 1.消息的标记 tag
             * 2.是否批量应答 false:不批量应答信道中的消息，true:批量应答信道中的消息
             */
            channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
        };

        //取消消息的回调
        CancelCallback CancelCallback =(consumerTag)->{
            System.out.println("消费者取消消费接口回调");
        };
        //消费者在接受消费之前 设置不公平分发，默认设置为0，公平分发，你一个我一个，设置为1不公平分发，能者多劳
        int prefetchCount = 1;
        channel.basicQos(prefetchCount);
        //采用手动应答
        boolean autoAck = false;
        //接受消息
        channel.basicConsume(TASK_QUEUE_NAME,autoAck,deliverCallback,CancelCallback);

    }
}
