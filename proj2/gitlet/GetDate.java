package gitlet;


import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

public class GetDate {
    public static String getDate() {
        // 创建一个Date对象表示当前时间
        Date currentDate = new Date();

        // 使用Formatter来格式化日期
        StringBuilder sb = new StringBuilder();
        Formatter formatter = new Formatter(sb, Locale.ENGLISH);
        formatter.format("%1$tH:%1$tM:%1$tS UTC, %1$tA, %1$te %1$tB %1$tY", currentDate);

        // 输出结果
        return sb.toString();
    }

    public static String getDate0() {
        Date currentDate = new Date(0);

        // 使用Formatter来格式化日期
        StringBuilder sb = new StringBuilder();
        Formatter formatter = new Formatter(sb, Locale.ENGLISH);
        formatter.format("%1$tH:%1$tM:%1$tS UTC, %1$tA, %1$te %1$tB %1$tY", currentDate);

        // 输出结果
        return sb.toString();
    }

}
