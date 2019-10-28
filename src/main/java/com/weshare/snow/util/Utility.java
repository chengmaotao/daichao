package com.weshare.snow.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.weshare.sdk.whale.config.ds.BizAndDsHolder;
import com.weshare.sdk.whale.model.LightningResponse;
import com.weshare.sdk.whale.util.SdkUtil;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.text.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Utility {

    private static final Logger logger = LoggerFactory.getLogger(Utility.class);

    private final static String[] hexDigits = {
            "0", "1", "2", "3", "4", "5", "6", "7",
            "8", "9", "a", "b", "c", "d", "e", "f"};
    static Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isNotBlank(String str) {
        return (!isBlank(str));
    }

    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

    /**
     * format time to "yyyy-MM-dd"
     *
     * @param time
     * @return
     */
    public static String formatTime(int time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(time);
    }

    public static String formatTimeToMMDDYYYY(int time) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
        return formatter.format(time * 1000l);
    }

    public static String formatTimeToYYYYMM(int time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");
        return formatter.format(time * 1000L);
    }

    /**
     * format time to "yyyy-MM-dd"
     *
     * @return
     */
    public static String formatCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        return formatter.format(System.currentTimeMillis());
    }


    /**
     * get the day end time by given the time point
     * e.g. given timePoint is "2015.5.13 13:45:32", the returned day end time is "2015.5.13 23:59:59"
     *
     * @param timePoint
     * @return
     */
    public static int getDayEndTime(int timePoint) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        long theTime = ((long) timePoint) * 1000;
        calendar.setTimeInMillis(theTime);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        calendar.clear();
        calendar.set(year, month, day, 23, 59, 59);
        int ret = (int) (calendar.getTimeInMillis() / 1000);
        return ret;
    }

    /**
     * get the day start time by given the time point
     * e.g. given timePoint is "2015.5.13 13:45:32", the returned day start time is "2015.5.13 00:00:00"
     *
     * @param timePoint
     * @return
     */
    public static int getDayStartTime(int timePoint) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        String curday = sf.format(System.currentTimeMillis());
        int ret = timePoint;
        try {
            ret = (int) ((sf.parse(curday).getTime()) / 1000);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return ret;
    }

    public static int getStartTime(int timePoint) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        long theTime = ((long) timePoint) * 1000;
        calendar.setTimeInMillis(theTime);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.clear();
        calendar.set(year, month, day, 0, 0, 0);
        return (int) (calendar.getTimeInMillis() / 1000);
    }

    public static long getTomorrowStartTime() {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        long time = System.currentTimeMillis() + 24 * 60 * 60 * 1000;
        String curday = sf.format(time);
        long ret = time;
        try {
            ret = (sf.parse(curday).getTime());
        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        return ret;
    }

    /**
     * HH:mm:ss
     *
     * @param timePoint
     * @return
     */
    public static String getHMS(int timePoint) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        long theTime = ((long) timePoint) * 1000;
        calendar.setTimeInMillis(theTime);
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        return formatter.format(calendar.getTime());
    }

    public static String maskUserName(String userName) {
        if (isBlank(userName)) {
            return userName;
        }

        switch (userName.length()) {
            case 0:
                return userName;
            case 1:
                return "*";
            default:
                return userName.replaceAll(".", "*").replaceFirst(".$", userName.substring(userName.length() - 1));
        }
    }

    public static String maskPhoneNum(String phoneNum) {
        if (isBlank(phoneNum) || phoneNum.length() < 4) {
            return phoneNum;
        }
        return phoneNum.substring(0, 3) + "****" + phoneNum.substring(phoneNum.length() - 4, phoneNum.length());
    }

    /**
     * mask idCard
     *
     * @param idCard
     * @return
     * @author xuyi
     */
    public static String maskIdCard(String idCard) {
        if (isBlank(idCard) || idCard.length() < 18) {
            return idCard;
        }

        return idCard.substring(0, 6) + "********" + idCard.substring(idCard.length() - 4, idCard.length());
    }

    /**
     * mask CVN2
     *
     * @param str
     * @return
     */
    public static String maskCvn2(String str) {
        if (str == null || str.length() != 3) {
            return str;
        }
        return "**" + str.substring(str.length() - 1, str.length());
    }

    /**
     * Convert current time into int type. used for create_time/update time
     *
     * @param
     * @return timestamp
     */
    public static int getCurrentTimeStamp() {
        if (debugCurrentTimeStamp == 0) {
            return (int) (System.currentTimeMillis() / 1000);
        } else {
            return debugCurrentTimeStamp;
        }
    }

    public static int getTodayTime(int hour, int min, int second) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, min);
        cal.set(Calendar.SECOND, second);
        cal.set(Calendar.MILLISECOND, 0);
        return (int) (cal.getTimeInMillis() / 1000);
    }

    private static int debugCurrentTimeStamp = 0;

    /**
     * For test purpose, getCurrentTimeStamp will return the timestamp if non-zero.
     *
     * @param
     * @return timestamp
     */
    public static void setDebugCurrentTimeStamp(int timeStamp) {
        debugCurrentTimeStamp = timeStamp;
    }


    public static String getSuccessMsg(ReloadableResourceBundleMessageSource messageSource) {
        return SdkUtil.getMessage(messageSource, "response.success");
    }


    public static String generateInvitationCode(Set<String> existingCode) {
        int invitationCode = (int) ((Math.random() * 9 + 1) * 100000);
        if (existingCode != null) {
            while (existingCode.contains(String.valueOf(invitationCode))) {
                invitationCode++;
                if (invitationCode >= 999999) {
                    invitationCode = (int) ((Math.random() * 9 + 1) * 100000);
                }
            }
        }
        return String.valueOf(invitationCode);
    }

    /**
     * generate random UUID, eg:d17e0ec1-3dda-47cc-8643-e516afa08a36
     *
     * @return new uuid
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }


    public static int getStringFormatTimestamp(String time) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = sf.parse(time);
            return (int) (date.getTime() / 1000);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getDataFormatString(int currentSec) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sf.format(((long) currentSec) * 1000);
    }

    public static String getDateFormatString2(int currentSec) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        return sf.format(((long) currentSec) * 1000);
    }

    public static String getDateFormatZHString2(int currentSec) {
        SimpleDateFormat sf = new SimpleDateFormat("MM月dd日");
        return sf.format(((long) currentSec) * 1000);
    }

    public static int addDays(int timestamp, int days) {
        return timestamp + 3600 * 24 * days;
    }

    public static int addMonths(int timestamp, int month) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        long theTime = ((long) timestamp) * 1000;
        calendar.setTimeInMillis(theTime);
        calendar.add(Calendar.MONTH, month);
        return (int) (calendar.getTimeInMillis() / 1000);
    }

    public static String maskLastBankCard(String bankCardNo) {
        if (bankCardNo == null || bankCardNo.length() <= 4) {
            return bankCardNo;
        }
        return bankCardNo.substring(bankCardNo.length() - 4, bankCardNo.length());
    }

    /**
     * format time to "yyyy-MM-dd HH:mm"
     *
     * @param time
     * @return
     */
    public static String formatHourMinuteTime(int time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return formatter.format(((long) time) * 1000);
    }

    public static String formatMinuteTime(int time) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        return formatter.format(((long) time) * 1000);
    }

    public static String formatMonthDayTime(int time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(((long) time) * 1000);
    }

    public static int getSimpleTimeStamp(int timStamp) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        return (int) (Timestamp.valueOf(sf.format(((long) timStamp) * 1000)).getTime() / 1000);
    }

    public static String fmtDatetime(String pattern) {
        return FastDateFormat.getInstance(pattern).format(new Date());
    }

    public static String fmtYmd() {
        return FastDateFormat.getInstance("yyyy-MM-dd").format(new Date());
    }

    public static String fmtYmdHms() {
        return FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public static String fmtYmdHms(int timestamp) {
        long ts = ((long) timestamp) * 1000;
        return FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").format(ts);
    }

    public static String fmtChinaYmd(int timestamp) {
        long ts = ((long) timestamp) * 1000;
        return FastDateFormat.getInstance("yyyy年MM月dd日").format(ts);
    }

    public static String getFmtChinaYmd() {
        return FastDateFormat.getInstance("yyyy年MM月dd日").format(System.currentTimeMillis());
    }

    public static String getFmtChinaYmdms() {
        return FastDateFormat.getInstance("yyyy年MM月dd日 HH:mm").format(System.currentTimeMillis());
    }

    public static String fmtChinaMd(int timestamp) {
        long ts = ((long) timestamp) * 1000;
        return FastDateFormat.getInstance("MM月dd日").format(ts);
    }

    public static String fmtYmd(int timestamp) {
        long ts = ((long) timestamp) * 1000;
        return FastDateFormat.getInstance("yyyy-MM-dd").format(ts);
    }

    public static long formatDate(String dateStr, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date result = null;
        try {
            result = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result == null ? null : (result.getTime() / 1000);
    }

    public static int getCurrentMonthEndTime(int timePoint) {
        int ret = 0;
        try {
            Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
            long theTime = ((long) timePoint) * 1000;
            c.setTimeInMillis(theTime);
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int lastDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
            c.clear();
            c.set(year, month, lastDay, 23, 59, 59);
            ret = (int) (c.getTimeInMillis() / 1000);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ret;
    }

    public static int getCurrentMonthStartTime(int timePoint) {
        int ret = 0;
        try {
            Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
            long theTime = ((long) timePoint) * 1000;
            c.setTimeInMillis(theTime);
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int lastDay = c.getActualMinimum(Calendar.DAY_OF_MONTH);
            c.clear();
            c.set(year, month, lastDay, 0, 0, 0);
            ret = (int) (c.getTimeInMillis() / 1000);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ret;
    }

    public static String convertDecimal2PercentRate(BigDecimal rate) {
        NumberFormat percent = NumberFormat.getPercentInstance();
        percent.setMaximumFractionDigits(1);
        return percent.format(rate.setScale(3, BigDecimal.ROUND_DOWN));
    }

    /**
     * 校验银行卡卡号
     *
     * @param cardId
     * @return
     */
    public static boolean checkBankCard(String cardId) {
        char bit = getBankCardCheckCode(cardId.substring(0, cardId.length() - 1));
        if (bit == 'N') {
            return false;
        }
        return cardId.charAt(cardId.length() - 1) == bit;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     *
     * @param nonCheckCodeCardId
     * @return
     */
    public static char getBankCardCheckCode(String nonCheckCodeCardId) {
        if (nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0
                || !nonCheckCodeCardId.matches("\\d+")) {
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }

    /**
     * 获取min到max之间的随机数
     *
     * @param min
     * @param max
     * @return
     */
    public static int randomInt(int min, int max) {
        Random rand = new Random();
        int randNum = rand.nextInt(max - min + 1) + min;
        return randNum;
    }

    /**
     * get a rabbit message with the specified string
     */
    public static Message makeRabbitMessage(String message) {
        Assert.notNull(message, "message is null");
        return MessageBuilder.withBody(message.getBytes()).build();
    }

    /**
     * Get paging offset
     *
     * @param currPage
     * @param pageSize
     * @return
     */
    public static final int getPagingOffset(int currPage, int pageSize) {
        if (currPage < 1) {
            currPage = 1;
        }
        if (pageSize < 1) {
            pageSize = 1;
        }
        return (currPage - 1) * pageSize;
    }

    /**
     * Format to 2 decimal
     *
     * @param bigDecimal
     * @return
     */
    public static final String fmt2Decimal(BigDecimal bigDecimal) {
        if (bigDecimal == null) {
            return null;
        }
        return String.valueOf(bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP));
    }

    /**
     * Get today 23 hour timestamp
     *
     * @return
     */
    public static int getToday23Hour() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 0);
        //
        return (int) (cal.getTimeInMillis() / 1000);
    }

    /**
     * Get today zero hour timestamp
     *
     * @return
     */
    public static int getTodayZeroHour() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        //
        return (int) (cal.getTimeInMillis() / 1000);
    }

    public static String encodeUtf8(String url) {
        try {
            return URLEncoder.encode(url, "utf-8");
        } catch (UnsupportedEncodingException e) {
            logger.warn("encodeUtf8: is fail.{}", e.getMessage());
        }
        return url;
    }

    public static String decodeUtf8(String encodeUrl) {
        try {
            return URLDecoder.decode(encodeUrl, "utf-8");
        } catch (UnsupportedEncodingException e) {
            logger.warn("decodeUtf8: is fail.{}", e.getMessage());
        }
        return encodeUrl;
    }

    public static String getMessage(String msgId, ReloadableResourceBundleMessageSource messageSource) {
        return Utility.getMessage(msgId, null, messageSource);
    }

    public static String getMessage(String msgId, String[] args, ReloadableResourceBundleMessageSource messageSource) {
        if (messageSource == null || Utility.isBlank(msgId)) {
            logger.warn("getMessage: messageSource/msgId is null,return blank.");
            return "";
        }
        return SdkUtil.getMessage(messageSource, msgId, args);
    }

    public static String getCacheKey(String suffix) {
        if (Utility.isBlank(suffix)) {
            logger.warn("getCacheKey: suffix is null.");
            return null;
        }
        return "harbor:cache:" + suffix;
    }

    public static String getBizCacheKey(String suffix) {
        if (Utility.isBlank(suffix)) {
            logger.warn("getCacheKey: suffix is null.");
            return null;
        }
        return "harbor:cache:" + suffix + "_" + BizAndDsHolder.getBizt();
    }


    public static String getLockerKey(String suffix) {
        if (Utility.isBlank(suffix)) {
            logger.warn("getLockerKey: suffix is null.");
            return null;
        }
        return "harbor:locker:" + suffix;
    }

    public static String getBizLockerKey(String suffix) {
        if (Utility.isBlank(suffix)) {
            logger.warn("getLockerKey: suffix is null.");
            return null;
        }
        return "harbor:locker:" + suffix + "_" + BizAndDsHolder.getBizt();
    }

    public static final String getDragonLockerKey(String suffix) {
        if (Utility.isBlank(suffix)) {
            logger.warn("getDragonLockerKey: suffix is null.");
            return null;
        }
        return "dragon:locker:" + suffix + BizAndDsHolder.getBizt();
    }


    //return redis key name which shared in different apps
    public static final String getPublicCacheKey(String suffix) {
        if (Utility.isBlank(suffix)) {
            logger.warn("getCacheKey: suffix is null.");
            return null;
        }
        return "jiekuan:cache:" + suffix;
    }

    public static String getUnion32Key(String key) {
        return getMd5(key);
    }

    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;
    private static final int EOF = -1;

    public static long copyLarge(InputStream input, OutputStream output)
            throws IOException {

        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        long count = 0;
        int n = 0;
        while (EOF != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    public static String getMd5(String key) {
        if (Utility.isBlank(key)) {
            return key;
        }
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.reset();
            md5.update(key.getBytes("UTF-8"));
            return Hex.encodeHexString(md5.digest());
        } catch (Exception e) {
            logger.warn("getMd5: is fail.{}", e.getMessage());
        }
        return key;
    }

    public static boolean isNotTimestamp(Integer timestamp) {
        return !(isTimestamp(timestamp));
    }

    public static boolean isTimestamp(Integer timestamp) {
        //
        return (timestamp != null && timestamp.intValue() > 0);
    }

    public static int fmtRmbFen(BigDecimal money) {
        if (money == null) {
            return 0;
        }
        return money.setScale(2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).intValue();
    }

    /**
     * Get n day timestamp
     *
     * @param dayAmount
     * @return second
     */
    public static int getNdaysLaterTs(int dayAmount) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        cal.add(Calendar.DAY_OF_MONTH, dayAmount);
        //
        return (int) (cal.getTimeInMillis() / 1000);
    }

    /**
     * timeStamp is n day later timestamp
     *
     * @param dayAmount
     * @param timeStamp
     * @return
     */
    public static boolean isNdaysLater(int dayAmount, int timeStamp) {
        return (getSimpleTimeStamp(timeStamp) > getNdaysLaterTs(dayAmount));
    }

    public static boolean isApiOuterChannel(Integer channel) {
        if (channel == null) {
            return false;
        }
        if (channel <= 100_0000) {
            return false;
        }
        return true;
    }

    public static Map<String, Object> getFmtWechatResp(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return map;
        }
        // expires_in
        doFmtWechatResp("expires_in", map);
        // errcode
        doFmtWechatResp("errcode", map);
        // msgid
        doFmtWechatResp("msgid", map);
        //
        return map;
    }

    private static void doFmtWechatResp(String key, Map<String, Object> map) {
        if (map.containsKey(key)) {
            Object val = map.get(key);
            if (val != null) {
                if (val instanceof Double) {
                    map.put(key, ((Double) val).intValue());
                } else if (val instanceof String) {
                    map.put(key, Integer.parseInt(String.valueOf(val)));
                }
            }
        }
    }

    /**
     * created by liuxiaokang
     *
     * @return
     */
    public static Date getToday23HourDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 0);
        //
        return cal.getTime();
    }


    /**
     * getSomeday23HourDate
     *
     * @param days
     * @return
     */
    public static Date getSomeday23HourDate(int days) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.DAY_OF_YEAR, days);
        return cal.getTime();
    }

    /**
     * getSomeday23Hour
     *
     * @param timestamp
     * @return
     */
    public static int getSomeday23Hour(int timestamp) {
        Calendar cal = Calendar.getInstance();
        long theTime = ((long) timestamp) * 1000;
        cal.setTimeInMillis(theTime);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return (int) (cal.getTimeInMillis() / 1000);
    }

    /**
     * Get n day zero h/m/s timestamp
     *
     * @param dayAmount
     * @return second
     */
    public static int getNdaysZeroTs(int dayAmount) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.add(Calendar.DAY_OF_MONTH, dayAmount);
        //
        return (int) (cal.getTimeInMillis() / 1000);
    }

    /**
     * Get n month zero h/m/s timestamp
     *
     * @param monthAmount
     * @return second
     */
    public static int getNmonthsZeroTs(int monthAmount) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.MONTH, monthAmount);
        //
        return (int) (cal.getTimeInMillis() / 1000);
    }

    /**
     * Get n year zero h/m/s timestamp
     *
     * @param yearAmount
     * @return second
     */
    public static int getNyearsZeroTs(int yearAmount) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.MONTH, 0);
        cal.add(Calendar.YEAR, yearAmount);
        //
        return (int) (cal.getTimeInMillis() / 1000);
    }

    /**
     * timestamp to date
     *
     * @param timestamp
     * @return
     */
    public static Date toDate(int timestamp) {
        return new Date(1000L * timestamp + 999);
    }


    public static String fmtHm(int timestamp) {
        long ts = ((long) timestamp) * 1000;
        return FastDateFormat.getInstance("dd日 HH:mm").format(ts);
    }

    public static boolean isNeedAddGray(Integer channel) {

        if (channel == null) {
            return false;
        }

        if (channel == 1400 || channel == 1401 || channel == 1402 || channel == 8800 || channel == 14000
                || channel == 15300 || channel == 15301 || channel == 15302 || channel == 15303) {
            return false;
        }

        return true;
    }


    private static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n += 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    /**
     * @param origin String
     * @return String
     * @throws Exception
     */
    public static String getMD5Encode(String origin) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        if (origin == null) {
            return null;
        }
        byte[] temp = null;
        synchronized (md) {
            temp = md.digest(origin.getBytes());
        }

        return byteArrayToHexString(temp);

    }

    private static boolean isNotEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) ||
                (codePoint == 0x9) ||
                (codePoint == 0xA) ||
                (codePoint == 0xD) ||
                ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
                ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }

    /**
     * fiter emoji
     *
     * @param source
     * @return
     */
    public static String filterEmoji(String source) {
        int len = source.length();
        StringBuilder buf = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (isNotEmojiCharacter(codePoint)) {
                buf.append(codePoint);
            } else {

                buf.append("*");

            }
        }
        return buf.toString();
    }

    public static String matchResult(Pattern p, String str) {
        StringBuilder sb = new StringBuilder();
        Matcher m = p.matcher(str);
        while (m.find())
            for (int i = 0; i <= m.groupCount(); i++) {
                sb.append(m.group());
            }
        return sb.toString();
    }

    /**
     * Format to  decimal
     *
     * @param bigDecimal
     * @return
     */
    public static final String fmtDecimal(BigDecimal bigDecimal, int point) {
        if (bigDecimal == null) {
            return null;
        }
        return String.valueOf(bigDecimal.setScale(point, BigDecimal.ROUND_HALF_UP));
    }

    public static int fmtToInt(BigDecimal amount) {
        if (amount != null) {
            amount = amount.setScale(2, BigDecimal.ROUND_HALF_UP);
            return amount.multiply(new BigDecimal(100)).intValue();
        }
        return 0;
    }

    public static int fmtFeeToInt(BigDecimal amount) {
        if (amount != null) {
            return amount.multiply(new BigDecimal(10000)).intValue();
        }
        return 0;
    }

    public static BigDecimal fmtToBigDecimal(int amount) {
        return new BigDecimal(amount).divide(new BigDecimal(100), 4, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal fmtToBigDecimalScaled(int amount) {
        return new BigDecimal(amount).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
    }

    public static int fmtBigDecimalToInt(BigDecimal amount) {
        if (amount != null) {
            return amount.multiply(new BigDecimal(100)).intValue();
        }
        return 0;
    }

    public static BigDecimal dealLoanRateByPeriod(BigDecimal loanRate, int period, boolean isSub) {
        if (isSub) {
            return loanRate;
        }

        if (loanRate == null) {
            return BigDecimal.ZERO;
        }

        if (period == 10) {
            return loanRate.multiply(new BigDecimal(7)).divide(new BigDecimal(10), 4, BigDecimal.ROUND_UP);
        }

        return loanRate;
    }

    public static int convertReduceStatus(Integer reduceStatus) {
        if (reduceStatus == null) {
            return 3;
        }
        return reduceStatus;
    }

    public static String formatYearMonthDay() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyMMdd");
        return formatter.format(System.currentTimeMillis());
    }

    public static String getNumberContactCode(Integer id) {
        StringBuffer numberContactCode = new StringBuffer(id.toString());
        int length = id.toString().length();
        if (id != null) {
            for (int i = 0; i < 10 - length; i++) {
                numberContactCode = numberContactCode.insert(0, "0");
            }
            return formatYearMonthDay() + numberContactCode.toString();
        }
        return null;
    }


    public static BigDecimal splitCapital(BigDecimal amount, int periodNumber, int i) {
        BigDecimal result = BigDecimal.ZERO;
        if (i <= periodNumber) {
            result = amount.divideToIntegralValue(new BigDecimal(periodNumber));
            BigDecimal count = amount.subtract((result.multiply(new BigDecimal(periodNumber))));
            if (i == 1) {
                result = result.add(count);
            }
        }
        return result;
    }

    /**
     * exchange String to timeStamp
     *
     * @author liusai
     */
    public static int exString2TimeStamp(String effectTime) {
        String parttenYMD = "^\\d{4}-\\d{2}-\\d{2}$";
        String parttenYMDHms = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$";
        try {
            if (Pattern.matches(parttenYMD, effectTime)) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date date = format.parse(effectTime);
                return (int) (date.getTime() / 1000);
            } else if (Pattern.matches(parttenYMDHms, effectTime)) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = format.parse(effectTime);
                return (int) (date.getTime() / 1000);
            }
        } catch (ParseException e) {
        }
        return -1;
    }

    /**
     * @param multiple 是否为整数倍
     * @param amount   金额
     * @return true: 不校验 或校验通过; false: 校验未通过
     */
    public static boolean deterIsMultiple(Integer multiple, BigDecimal amount) {
        if (multiple == null || multiple <= 0 || amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return true;
        }

        if (amount.divide(new BigDecimal(multiple), 0, BigDecimal.ROUND_HALF_EVEN).multiply(new BigDecimal(multiple)).compareTo(amount) != 0) {
            return false;
        }
        return true;
    }

    public static Integer getLowestLoanAmount(BigDecimal productLowestAmount, BigDecimal canBorrowAmount) {
        if (productLowestAmount == null || canBorrowAmount == null || productLowestAmount.compareTo(BigDecimal.ZERO) < 0
                || canBorrowAmount.compareTo(BigDecimal.ZERO) < 0) {
            return 0;
        }

        if (canBorrowAmount.compareTo(productLowestAmount) > 0) {
            return productLowestAmount.intValue();
        }

        Integer lowestAmount = canBorrowAmount.divide(new BigDecimal(100), 0, BigDecimal.ROUND_FLOOR).multiply(new BigDecimal(100)).intValue();
        return lowestAmount < 100 ? 100 : lowestAmount;
    }

    public static int fmtMoney(BigDecimal money, int currency) {
        return money == null ? 0 : money.setScale(2, 4).multiply(new BigDecimal(currency)).intValue();
    }

    public static int getBigDecimalMode(int item) {
        switch (item) {
            case 0:
                return BigDecimal.ROUND_UP;
            case 1:
                return BigDecimal.ROUND_DOWN;
            case 2:
                return BigDecimal.ROUND_CEILING;
            case 3:
                return BigDecimal.ROUND_FLOOR;
            case 4:
                return BigDecimal.ROUND_HALF_UP;
            case 5:
                return BigDecimal.ROUND_HALF_DOWN;
            case 6:
                return BigDecimal.ROUND_HALF_EVEN;
            case 7:
                return BigDecimal.ROUND_UNNECESSARY;
            default:
                logger.warn("getBigDecimalMode: bigdecimal mode invalid.");
                return BigDecimal.ROUND_HALF_UP;
        }
    }

    public static String toJson(Object src) {
        return gson.toJson(src);
    }

    public static String mapToJson(Map<String, Object> map) {
        return gson.toJson(map);
    }

    public static Map<String, Object> jsonToMap(String json) {
        Type type = new TypeToken<Map<String, Object>>() {
        }.getType();
        Map<String, Object> map = gson.fromJson(json, type);
        return map;
    }

    public static <T> T fromJsonToObj(String json, Class<T> to) {
        return gson.fromJson(json, to);
    }

    public static String formatDateyyyyMMdd(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }

    public static boolean isIqiyiOrileChannel(Integer channel) {
        if (channel != null && (channel == 1010110048 || channel == 1010102362)) {
            return true;
        }
        return false;
    }

    public static <T> T mapToBean(Map<String, Object> map, Class<T> clazz) {
        Gson gson = new Gson();

        if (map == null || map.isEmpty())
            return null;

        return gson.fromJson(gson.toJson(map), clazz);
    }

    /**
     * return  offset list
     *
     * @param total
     * @param limit
     * @return
     */
    public static List<Integer> getOffsets(int total, int limit) {
        int range = total / limit;
        List<Integer> offsets = new ArrayList<>();
        for (int i = 0; i <= range; i++) {
            offsets.add(i * limit);
        }
        return offsets;
    }

    public static String getMessage(String content, final Map<String, Object> replaceArgs) {
        if (Utility.isBlank(content)) {
            return content;
        }
        if (replaceArgs != null && replaceArgs.size() > 0) {
            for (Map.Entry<String, Object> entry : replaceArgs.entrySet()) {
                content = content.replace("{" + entry.getKey() + "}", String.valueOf(entry.getValue()));
            }
        }
        return content;
    }

    /**
     * 根据身份证号获取性别
     * 0：女 1：男
     *
     * @param idCard
     * @return
     */
    public static Integer getSexByIdCard(String idCard) {
        if (idCard.length() == 18) {
            if (Integer.valueOf(idCard.substring(16, 17)) % 2 != 0) {
                return 1;
            } else {
                return 0;
            }
        } else if (idCard.length() == 15) {
            if (Integer.valueOf(idCard.substring(14, 15)) % 2 != 0) {
                return 1;
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }


    public final static String SORT_DESC = "desc";
    public final static String SORT_ASC = "asc";

    /**
     * <p>Discription:[List集合排序类(可按中文排序)]</p>
     * Created on 2016-3-14
     *
     * @param list     目标集合
     * @param property 排序字段名
     * @param sortType 正序 (CollectionUtils.SORT_ASC)、倒序 (CollectionUtils.SORT_DESC)
     * @param isCN     是否按中文排序
     * @author:武超强
     */
    public static <T> void sortList(List<T> list, final String property, final String sortType, final boolean isCN) {
        Collections.sort(list, new Comparator<T>() {
            private Collator collator = null;

            public int compare(T a, T b) {
                int ret = 0;
                Field field = ReflectionUtils.findField(a.getClass(), property);
                String getterMethodName = "get" + StringUtils.capitalize(property);
                Method method = ReflectionUtils.findMethod(a.getClass(), getterMethodName);
                Object value_a = ReflectionUtils.invokeMethod(method, a);
                Object value_b = ReflectionUtils.invokeMethod(method, b);
                if (field.getType() == String.class) {
                    if (isCN) {
                        collator = Collator.getInstance();
                        CollationKey key1 = collator.getCollationKey(value_a.toString());
                        CollationKey key2 = collator.getCollationKey(value_b.toString());
                        if (sortType != null && sortType.equals(SORT_DESC))
                            ret = key2.compareTo(key1);
                        else
                            ret = key1.compareTo(key2);
                    } else {
                        if (sortType != null && sortType.equals(SORT_DESC))
                            ret = value_b.toString().compareTo(value_a.toString());
                        else
                            ret = value_a.toString().compareTo(value_b.toString());
                    }
                } else if (field.getType() == Integer.class || field.getType() == Long.class || field.getType() == BigDecimal.class) {
                    BigDecimal decA = new BigDecimal(value_a.toString());
                    BigDecimal decB = new BigDecimal(value_b.toString());
                    if (sortType != null && sortType.equals(SORT_DESC))
                        ret = decB.compareTo(decA);
                    else
                        ret = decA.compareTo(decB);
                } else if (field.getType() == Date.class) {
                    if (sortType != null && sortType.equals(SORT_DESC))
                        ret = ((Date) value_b).compareTo((Date) value_a);
                    else
                        ret = ((Date) value_a).compareTo((Date) value_b);
                }
                return ret;
            }
        });
    }

    public static <T> void sortList(List<T> list, final String property, final String sortType) {
        sortList(list, property, sortType, false);
    }

    public static LightningResponse getErrorResponse(int code, String msg) {
        LightningResponse res = new LightningResponse();
        res.setStatus(code);
        res.setMessage(msg);
        return res;
    }

    public static LightningResponse getErrorMessageResponse(int status, String message) {
        LightningResponse res = new LightningResponse();
        res.setStatus(status);
        res.setMessage(message);
        res.setContent(null);
        return res;
    }

    public static LightningResponse getErrorResponse(int status, ReloadableResourceBundleMessageSource messageSource) {
        return getErrorResponse(status, null, messageSource);
    }

    public static LightningResponse getErrorResponse(int status, Object[] args, ReloadableResourceBundleMessageSource messageSource) {
        LightningResponse res = new LightningResponse();
        res.setStatus(status);

        switch (status) {
            case ErrorCode.SYS_SUCCESS: {
                res.setMessage(SdkUtil.getMessage(messageSource, "response.success", args));
                break;
            }
            case ErrorCode.SYS_FAIL: {
                res.setMessage(SdkUtil.getMessage(messageSource, "response.failure.please.contact.customer.service", args));
                break;
            }
            case ErrorCode.API_PARAM_ERROR: {
                res.setMessage(SdkUtil.getMessage(messageSource, "api10001", args));
                break;
            }
            case ErrorCode.API_IMGCODE_ERROR: {
                res.setMessage(SdkUtil.getMessage(messageSource, "api10002", args));
                break;
            }
            case ErrorCode.API_SMS_SEND_ERROR: {
                res.setMessage(SdkUtil.getMessage(messageSource, "api10003", args));
                break;
            }
            case ErrorCode.API_SMSCODE_ERROR: {
                res.setMessage(SdkUtil.getMessage(messageSource, "api10004", args));
                break;
            }
            case ErrorCode.API_USERSTATE_REALNAME: {
                res.setMessage(SdkUtil.getMessage(messageSource, "api10005", args));
                break;
            }
            case ErrorCode.API_USER_NO: {
                res.setMessage(SdkUtil.getMessage(messageSource, "api10006", args));
                break;
            }
            default: {
                res.setMessage(SdkUtil.getMessage(messageSource, "response.failure.please.contact.customer.service"));
                break;
            }
        }
        return res;
    }


    public static LightningResponse getRightResponse(String msg, Object content) {
        LightningResponse res = new LightningResponse();
        res.setStatus(0);
        res.setMessage(msg);
        res.setContent(content);
        return res;
    }

    public static boolean isIDNumber(String IDNumber) {
        if (IDNumber == null || "".equals(IDNumber)) {
            return false;
        }
        // 定义判别用户身份证号的正则表达式（15位或者18位，最后一位可以为字母）
        String regularExpression = "(^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|" +
                "(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)";
        //假设18位身份证号码:41000119910101123X  410001 19910101 123X
        //^开头
        //[1-9] 第一位1-9中的一个      4
        //\\d{5} 五位数字           10001（前六位省市县地区）
        //(18|19|20)                19（现阶段可能取值范围18xx-20xx年）
        //\\d{2}                    91（年份）
        //((0[1-9])|(10|11|12))     01（月份）
        //(([0-2][1-9])|10|20|30|31)01（日期）
        //\\d{3} 三位数字            123（第十七位奇数代表男，偶数代表女）
        //[0-9Xx] 0123456789Xx其中的一个 X（第十八位为校验值）
        //$结尾

        //假设15位身份证号码:410001910101123  410001 910101 123
        //^开头
        //[1-9] 第一位1-9中的一个      4
        //\\d{5} 五位数字           10001（前六位省市县地区）
        //\\d{2}                    91（年份）
        //((0[1-9])|(10|11|12))     01（月份）
        //(([0-2][1-9])|10|20|30|31)01（日期）
        //\\d{3} 三位数字            123（第十五位奇数代表男，偶数代表女），15位身份证不含X
        //$结尾


        boolean matches = IDNumber.matches(regularExpression);

        //判断第18位校验值
        if (matches) {

            if (IDNumber.length() == 18) {
                try {
                    char[] charArray = IDNumber.toCharArray();
                    //前十七位加权因子
                    int[] idCardWi = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
                    //这是除以11后，可能产生的11位余数对应的验证码
                    String[] idCardY = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
                    int sum = 0;
                    for (int i = 0; i < idCardWi.length; i++) {
                        int current = Integer.parseInt(String.valueOf(charArray[i]));
                        int count = current * idCardWi[i];
                        sum += count;
                    }
                    char idCardLast = charArray[17];
                    int idCardMod = sum % 11;
                    if (idCardY[idCardMod].toUpperCase().equals(String.valueOf(idCardLast).toUpperCase())) {
                        return true;
                    } else {
                        System.out.println("身份证最后一位:" + String.valueOf(idCardLast).toUpperCase() +
                                "错误,正确的应该是:" + idCardY[idCardMod].toUpperCase());
                        return false;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("异常:" + IDNumber);
                    return false;
                }
            }

        }
        return matches;
    }

    public static String get32UUID() {
        String[] idd = UUID.randomUUID().toString().split("-");
        return idd[0] + idd[1] + idd[2] + idd[3] + idd[4];
    }

    public static String fmtHms() {
        return FastDateFormat.getInstance("HHmmss").format(new Date());
    }


    public static Map<String, String> getAllRequestValue(HttpServletRequest request) {

        Map<String, String> paramMap = new HashMap<String, String>();

        Enumeration enu = request.getParameterNames();
        while (enu.hasMoreElements()) {
            String paraName = (String) enu.nextElement();
            //System.out.println(paraName + ": " + request.getParameter(paraName));
            paramMap.put(paraName, request.getParameter(paraName));
        }
        return paramMap;
    }

    /**
     * 除去数组中的空值和签名参数
     *
     * @param sArray 签名参数组
     * @return 去掉空值与签名参数后的新签名参数组
     */
    public static Map<String, String> paraFilter(Map<String, String> sArray) {

        Map<String, String> result = new HashMap<String, String>();

        if (sArray == null || sArray.size() <= 0) {
            return result;
        }

        for (String key : sArray.keySet()) {
            String value = sArray.get(key);
            if (value == null || value.equals("")
                    || key.equalsIgnoreCase("sign")
                    || key.equalsIgnoreCase("sign_type")) {
                continue;
            }
            // try {
            // value = URLEncoder.encode(value,charset);
            // } catch (UnsupportedEncodingException e) {
            // e.printStackTrace();
            // }
            result.put(key, value);
        }

        return result;
    }

    public static int IdNOToAge(String IdNO) {
        int leh = IdNO.length();
        String dates = "";
        if (leh == 18) {
            int se = Integer.valueOf(IdNO.substring(leh - 1)) % 2;
            dates = IdNO.substring(6, 10);
            SimpleDateFormat df = new SimpleDateFormat("yyyy");
            String year = df.format(new Date());
            int u = Integer.parseInt(year) - Integer.parseInt(dates);
            return u;
        } else {
            return 0;
        }

    }

    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     *
     * @param params 需要排序并参与字符拼接的参数组
     * @param encode 是否需要urlEncode
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, String> params,
                                          boolean encode) {

        // params = paraFilter(params);

        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        String prestr = "";

        String charset = params.get("Charset");
        if (StringUtils.isBlank(charset)) {
            charset = params.get("InputCharset");
        }

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if (encode) {
                try {
                    value = URLEncoder.encode(value, charset);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }

        return prestr;
    }


    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }


}
