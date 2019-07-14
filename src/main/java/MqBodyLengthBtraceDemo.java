import static com.sun.btrace.BTraceUtils.Reflective.get;
import static com.sun.btrace.BTraceUtils.classOf;
import static com.sun.btrace.BTraceUtils.field;
import static com.sun.btrace.BTraceUtils.println;

import com.helijia.framework.mq.MqMessage;
import com.sun.btrace.annotations.BTrace;
import com.sun.btrace.annotations.Export;
import com.sun.btrace.annotations.Kind;
import com.sun.btrace.annotations.Location;
import com.sun.btrace.annotations.OnMethod;

// nohup \
// btrace -cp ~/.m2/repository/com/helijia/framework/helijia.mq/1.1.1/helijia.mq-1.1.1.jar \
// `jps | grep RocketmqApp | awk '{print $1}'` \
// ~/github/moonrunwater/btrace-demo/src/main/java/MqBodyLengthBtraceDemo.java \
// >> mq_body_length.log \
// &
@BTrace
public class MqBodyLengthBtraceDemo {

    @Export
    private static long max = 0;

    @OnMethod(
            clazz = "com.helijia.framework.mq.rocketmq.RocketMqProducerService",
            method = "send",
            location = @Location(Kind.ENTRY)
    )
    public static void on_send_with_classpath(MqMessage message, long timeout) {
        int length = 0;
        if (message != null) {
            byte[] body = (byte[]) get(field(classOf(message), "body"), message);
            if (body != null) {
                length = body.length;
            }
        }
        println("length(unit:byte)=" + length);

        if (length > max) {
            max = length;
            println("max(unit:byte) changed to " + max);
        }
    }

}
