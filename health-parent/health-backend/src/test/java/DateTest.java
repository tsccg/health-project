//import com.tsccg.utils.DateUtils;
//import org.junit.Test;
//
//import java.util.Calendar;
//import java.util.Date;
//
///**
// * @Author: TSCCG
// * @Date: 2021/11/29 21:16
// */
//public class DateTest {
//    @Test
//    public void dateTest() throws Exception {
//        //获取日历对象，默认为当前时间
//        Calendar calendar = Calendar.getInstance();
//        //获取日历对象对应的日期对象，默认为西方时间格式 Mon Nov 29 21:01:55 CST 2021
//        Date time = calendar.getTime();
//        //转换日期格式 2021-11-29
//        System.out.println("当前日期：" + DateUtils.parseDate2String(time));
//        //当前日期：2021-11-29
//
//        //获取12个月前的时间
//        calendar.add(Calendar.MONTH,-12);
//        System.out.println("12个月前日期：" + DateUtils.parseDate2String(calendar.getTime()));
//        //12个月前日期：2020-11-29
//
//        for (int i = 0; i < 12; i++) {
//            calendar.add(Calendar.MONTH,1);//每循环一次，前进一个月
//            System.out.println(DateUtils.parseDate2String(calendar.getTime()));
//        }
//    }
//}
