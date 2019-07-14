import java.util.Random;

import com.helijia.framework.mq.rocketmq.RocketMqProducerService;

public class RocketmqApp {

    private static final String ADDRESS = "mq-stg.helijia.com:9876";
    private static final Random RANDOM = new Random();
    private static final String ALPHA_NUM = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_~!@#$%^&*()_-";
    private static final char[] CHARS = ALPHA_NUM.toCharArray();

    public static void main(String[] args) throws Exception {
        RocketMqProducerService producer = new RocketMqProducerService();
        producer.setAddress(ADDRESS);
        producer.setGroup("GROUP_huohu_btrace_producer");
        producer.start();

        Thread.sleep(20 * 1000);
        for (int i = 0; i < 1000; i++) {
            producer.send("TOPIC_huohu_btrace_test", ("MSG_" + randomMsg(RANDOM.nextInt(500) + 1) + "_" + i).getBytes());
        }

        producer.shutdown();
    }


    private static String randomMsg(int size) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(CHARS[RANDOM.nextInt(CHARS.length)]);
        }

        return sb.toString();
    }
}
