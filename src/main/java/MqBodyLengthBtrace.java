import static com.sun.btrace.BTraceUtils.Reflective.get;
import static com.sun.btrace.BTraceUtils.classOf;
import static com.sun.btrace.BTraceUtils.field;
import static com.sun.btrace.BTraceUtils.println;

import com.sun.btrace.AnyType;
import com.sun.btrace.annotations.BTrace;
import com.sun.btrace.annotations.Export;
import com.sun.btrace.annotations.Kind;
import com.sun.btrace.annotations.Location;
import com.sun.btrace.annotations.OnMethod;

// nohup \
// btrace \
// `jps | grep RocketmqApp | awk '{print $1}'` \
// ~/github/moonrunwater/btrace-demo/src/main/java/MqBodyLengthBtrace.java \
// >> mq_body_length.log \
// &
@BTrace
public class MqBodyLengthBtrace {

    @Export
    private static long max = 0;
    @Export
    private static long count = 0;

    @OnMethod(
            clazz = "com.helijia.framework.mq.rocketmq.RocketMqProducerService",
            method = "send",
            type = "<boolean>(com.helijia.framework.mq.MqMessage, long)",
            location = @Location(Kind.ENTRY)
    )
    public static void onSend(AnyType[] args) {

        // println("args.length=" + args.length +
        //         ", classOf(args[0])=" + classOf(args[0]) +
        //         ", classOf(args[1])=" + classOf(args[1]));

        count++;

        int length = 0;
        if (args[0] != null) {
            byte[] body = (byte[]) get(field(classOf(args[0]), "body"), args[0]);
            if (body != null) {
                length = body.length;
            }
        }
        println("length=" + length + ", count=" + count);

        if (length > max) {
            max = length;
            println("max= " + max);
        }
    }

}
