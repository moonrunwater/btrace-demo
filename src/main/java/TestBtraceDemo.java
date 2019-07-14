import static com.sun.btrace.BTraceUtils.classOf;
import static com.sun.btrace.BTraceUtils.field;
import static com.sun.btrace.BTraceUtils.get;
import static com.sun.btrace.BTraceUtils.jstackAll;
import static com.sun.btrace.BTraceUtils.printArray;
import static com.sun.btrace.BTraceUtils.println;
import static com.sun.btrace.BTraceUtils.sizeof;

import com.sun.btrace.AnyType;
import com.sun.btrace.annotations.BTrace;
import com.sun.btrace.annotations.Duration;
import com.sun.btrace.annotations.Export;
import com.sun.btrace.annotations.Kind;
import com.sun.btrace.annotations.Location;
import com.sun.btrace.annotations.OnMethod;
import com.sun.btrace.annotations.OnTimer;
import com.sun.btrace.annotations.ProbeClassName;
import com.sun.btrace.annotations.ProbeMethodName;
import com.sun.btrace.annotations.Return;
import com.sun.btrace.annotations.Self;

// import static com.sun.btrace.BTraceUtils.*;
// import com.sun.btrace.annotations.*;

@BTrace
public class TestBtraceDemo {

    // create a jstat counter using @Export
    @Export
    private static long count = 0;

    @OnMethod(clazz = "App",
            method = "print",
            location = @Location(Kind.ENTRY))
    public static void anyRead(@ProbeClassName String pcn, @ProbeMethodName String pmn, AnyType[] args) {
        println("ClassName=" + pcn + ", MethodName: " + pmn);
        printArray(args);
        println();
    }

    @OnMethod(clazz = "App",
            method = "print",
            location = @Location(Kind.RETURN))
    public static void on(@Self Object self,
                          String name, int age, @Return AnyType result,
                          @Duration long cost) {
        println("size: " + sizeof(self));
        println("arg1: " + name);
        println("arg2: " + age);

        Class resultClass = classOf(result);
        println("return: name=" + get(field(resultClass, "name"), result));
        println("return: age=" + get(field(resultClass, "age"), result));
        println("cost: " + cost / 1000000.0 + "ms");

        count++;

        // 打印栈信息
        jstackAll();
    }

    // @OnTimer annotation can be used to specify tracing actions
    // that have to run periodically once every N milliseconds.
    // Time period is specified as long "value" property of this annotation.
    @OnTimer(5000L)
    public static void count() {
        println("count=" + count);
    }

    // @OnError annotation can be used to specify actions that are run
    // whenever any exception is thrownby tracing actions of some other probe.
    // BTrace method annotated by this annotation is called when any exception is thrown
    // by any of the other BTrace action methods in the same BTrace class.

}