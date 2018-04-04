package sad.zzq.com.selectaddressdemo.tool;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import org.xutils.common.util.LogUtil;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author zhouzhengquan 字符串工具类
 */
public class StringUtils {

	/**
     * 正则表达式：验证密码
     */
    public static final String REGEX_PASSWORD = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$";
	/**
     * 正则表达式：验证手机号
     */
    public static final String REGEX_MOBILE = "^1(3|5|7|8)/d{9}$";
	
	 /**
     * 正则表达式：验证身份证
     */
    public static final String REGEX_ID_CARD = "^(\\d{6})(\\d{4})(\\d{2})(\\d{2})(\\d{3})([0-9]|X)$";
	 /**
     * 正则表达式：验证URL
     */
    public static final String REGEX_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";
    /**
     * 验证邮箱
     */
    public static final String REGEX_MAIL="^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";

	/**
	 * 银行卡
	 */
    public static final String REGEX_CARD="^(\\d{10,20})$";
    /**
     * 姓名"[\u4e00-\u9fa5]{2,4}
     */
    public static final String REGEX_NAME="[\u4e00-\u9fa5]{2,}";
	/**
	 * 判断是否是中午
	 */
	public static final String REGEX_ISChinese="[\u4e00-\u9fa5]{1,}";
    /**
     * 正整数
     */
    public static final String REGEX_MY_PHPONE="^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
    public static final String REGEX_NUMBER="^[0-9]*[1-9][0-9]*$";
    /**
	 * 判断字符串是否为空 空返回false，反之返回true
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isNoEmpty(String s) {
		if ("".equals(s) || "null".equals(s) || "NULL".equals(s)
				|| "[]".equals(s) || "<null>".equals(s) || "<NULL>".equals(s) ||null == s) {
			return false;
		}
		return true;
	}

	/**
	 * 验证手机格式
	 */
	public static boolean isMobileNO(String mobiles) {
		if (!isNoEmpty(mobiles)) {
			return false;
		}
		Pattern p = null;  
        Matcher m = null;  
        boolean b = false;   
        p = Pattern.compile("^1(\\d)\\d{9}$"); // 验证手机号
        m = p.matcher(mobiles);  
        b = m.matches();   
        return b;  
	}

	/**
	 * 使用正则表达式检查邮箱地址格式
	 * 
	 * @param email
	 * @return
	 */
	public static boolean checkEmailAddress(String email) {
		boolean isValid = false;
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);
		if (m.matches()) {
			isValid = true;
		}

		return isValid;
	}
	/**
	 * uri编码
	 * @param paramString
	 * @return
	 */
	public static String toURLEncoded(String paramString) {
		if (paramString == null || paramString.equals("")) {
			LogUtil.e("toURLEncoded error:"+paramString);
			return "";
		}
		
		try
		{
			String str = new String(paramString.getBytes(), "UTF-8");
			str = URLEncoder.encode(str, "UTF-8");
			return str;
		}
		catch (Exception localException)
		{
			LogUtil.e("toURLEncoded error:"+paramString+localException);
		}
		
		return "";
	}
	
	 /**
     * 校验身份证
     * 
     * @param idCard
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isIDCard(String idCard) {
        return Pattern.matches(REGEX_ID_CARD, idCard);
    }
    /**
     * 截取url获取code
     * @param url
     * @return
     */
    public static String subStrCode(String url){
    	String code = "";
        String arg = url.substring(url.indexOf("?")+1,url.length());
        String[] strs = arg.split("&");
        HashMap<String, String> map = new HashMap<String, String>();
        for(int x=0;x<strs.length;x++){
            String[] strs2 = strs[x].split("=");
            if(strs2.length==2){
                map.put(strs2[0], strs2[1]);
            }
        }
        
        for (String key : map.keySet()) {  
            if (TextUtils.equals("code", key)) {
            	code = map.get(key);
            	break;
			} 
        } 
        return code;
    }
    
    /**

    * 检测String是否全是中文

    * 

    * @param name

    * @return

    */

    public static boolean checkNameChese(String name) {
	    boolean res = true;
	    char[] cTemp = name.toCharArray();
	    for (int i = 0; i < name.length(); i++) {
		    if (!isChinese(cTemp[i])) {
			    res = false;
			    break;
		    }
	    }
	    return res;

    }
    /**

    * 判定输入汉字

    * 

    * @param c

    * @return

    */

    public static boolean isChinese(char c) {
	    Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
	    if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
	    	return true;
	    }
	    return false;

    }
    /**
     * 校验URL
     * 
     * @param url
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isUrl(String url) {
        return Pattern.matches(REGEX_URL, url);
    }
    
    /**
     * 校验密码
     * 
     * @param password
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isPassword(String password) {
        return Pattern.matches(REGEX_PASSWORD, password);
    }
    /**
     * null返回空字符串
     * @param s
     * @return
     */
    public static String getNoNullString(String s){
    	if (isNoEmpty(s)) {
			return s;
		}else{
			return "";
		}
    	
    }
    /**
     * 设置部分字体颜色
     * @param str
     * @param color
     * @param fstart
     * @param fend
     * @return
     */
    public static SpannableStringBuilder setTextViewSpanColor(String str,int color,int fstart,int fend){
    	SpannableStringBuilder style=new SpannableStringBuilder(str);   
        style.setSpan(new ForegroundColorSpan(color),fstart,fend,Spannable.SPAN_EXCLUSIVE_INCLUSIVE); 
		return style;
    }

	/**
	 * 过滤特殊字符
	 * @param str
	 * @return
	 */
	public static String filter(String str){
		if (!isNoEmpty(str)){return "";}
		String regEx="[`~!@#$%^&*()+=|{}':;',\\\\[\\\\]<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}

	public static double priceFilter(String str){
		return Double.valueOf(filter(str));
	}
}
