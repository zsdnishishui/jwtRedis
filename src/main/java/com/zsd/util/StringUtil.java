package com.zsd.util;

import java.util.UUID;
/**
 * 
 * @ClassName:  StringUtil   
 * @Description:TODO 字符串工具类
 * @author: lining 
 * @date:   2019年2月21日 上午10:00:28   
 *     
 * @Copyright: 2019 Inc. All rights reserved. 
 * 注意：本内容仅限于济南百草园文化传媒有限公司内部传阅，禁止外泄以及用于其他的商业目
 */
public class StringUtil {
	/**
	 * 检测字符串是否不为空(null,"","null")
	 * @param s
	 * @return 不为空则返回true，否则返回false
	 */
	public static boolean notEmpty(String s){
		return s!=null && !"".equals(s) && !"null".equals(s);
	}
	/**
	 * 
	 * @Title: isEmpty   
	 * @Description: TODO   * 判断是否是空字符串null和""
	 * @param: @param str
	 * @param: @return      
	 * @return: boolean      
	 * @throws
	 */
    public static boolean isEmpty(String str){
        if (str != null  && !str.equals("")) {
            return false;
        }
        return true;
    }
    /**
     * 
     * @Title: equals   
     * @Description: TODO  判断两个字符串是否相等 如果都为null则判断为相等,一个为null另一个not null则判断不相等 否则如果s1=s2则相等
     * @param: @param s1
     * @param: @param s2
     * @param: @return      
     * @return: boolean      
     * @throws
     */
    public static boolean equals(String s1, String s2) {
        if (StringUtil.isEmpty(s1) && StringUtil.isEmpty(s2)) {
            return true;
        } else if (!StringUtil.isEmpty(s1) && !StringUtil.isEmpty(s2)) {
            return s1.equals(s2);
        }
        return false;
    }
 
    /**
     * 
     * @Title: getUUID   
     * @Description: TODO(这里用一句话描述这个方法的作用)   
     * @param: @return      
     * @return: String      
     * @throws
     */
    public static String getUUID(){
        return UUID.randomUUID().toString();
    }
    /**
	 * 得到32位的uuid
	 * @return
	 */
	public static String get32UUID(){
		
		return UUID.randomUUID().toString().trim().replaceAll("-", "");
	}

}
