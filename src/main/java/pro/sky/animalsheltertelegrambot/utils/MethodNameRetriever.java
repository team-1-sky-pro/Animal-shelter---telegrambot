package pro.sky.animalsheltertelegrambot.utils;

public class MethodNameRetriever {
    public static String getMethodName() {
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        return elements[2].getMethodName();
    }
}
