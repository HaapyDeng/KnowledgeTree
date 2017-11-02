package com.max_plus.knowledgetree.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NetworkUtils {
    /**
     * 检测当的网络状态
     *
     * @param context
     *            Context
     * @return true 表示网络可用
     */
    /**
     * 网络不可用
     */
    public static final int NONETWORK = 0;
    /**
     * 是wifi连接
     */
    public static final int WIFI = 1;
    /**
     * 不是wifi连接
     */
    public static final int NOWIFI = 2;

    /**
     * 检验网络连接 并判断是否是wifi连接
     *
     * @param context
     * @return <li>没有网络：Network.NONETWORK;</li> <li>wifi 连接：Network.WIFI;</li>
     * <li>mobile 连接：Network.NOWIFI</li>
     */
    public static int checkNetWorkType(Context context) {

        if (!checkNetWork(context)) {
            return NetworkUtils.NONETWORK;
        }
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        // cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .isConnectedOrConnecting())
            return NetworkUtils.WIFI;
        else
            return NetworkUtils.NOWIFI;
    }
/***
 * @param fj
 * @author Administrator fdsfdsd
 * @see dffjidh
 */
    /**
     * 检测网络是否连接
     *
     * @param context
     * @return
     */
    public static boolean checkNetWork(Context context) {
        // 1.获得连接设备管理器
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return false;
        }
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null || !ni.isAvailable()) {
            return false;
        }
        return true;
    }


    //判断手机格式是否正确
    public static boolean isMobileNO(String inputText) {
        String telRegex = "^((13[0-9])|(15[^4])|(18[0-9])|(17[0-8])|(147,145))\\d{8}$";
        return inputText.matches(telRegex);
    }

    //判断email格式是否正确
    public static boolean isEmail(String email) {
        String telRegex = "\\w+@(\\w+.)+[a-z]{2,3}";
        return email.matches(telRegex);
    }

    //判断是否全是数字
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    //服务器端主路径
    public static String returnUrl() {
        String url = "http://zssapi.sostudy.cn";
//        String url = "http://192.168.1.21/api";
        return url;

    }

    //上传图片服务器
    public static String returnUrlForAvatar() {
        return "http://101.201.197.227";
    }

    //登录接口
    public static String returnLoginApi() {
        return "/ghshun/user/login";
    }

    //忘记密码接口
    public static String returnForgPasswordApi() {
        return "/ghshun/user/forget";
    }

    //注册接口
    public static String returnRegistApi() {
        return "/ghshun/user/register";
    }

    //手机验证码接口
    public static String returnPhoneCodeApi() {
        return "/public/setmsg";
    }

    //验证手机号或邮箱是否存在
    public static String returnVerifyUser() {
        return "/ghshun/user/cache";
    }

    //邮箱验证码接口
    public static String returnEmailCodeApi() {
        return "/public/setmail";
    }

    //获取个人详细信息
    public static String returnUserInfoApi() {
        return "/ghshun/member/info";
    }

    //上传头像图片获取地址专门接口
    public static String returnGetPath() {
        return "/upload.php";
    }

    //上传头像接口
    public static String returnUploadActorApi() {
        return "/ghshun/member/update";
    }

    //修改密码接口
    public static String returnFixPassword() {
        return "/ghshun/member/reset";
    }

    //获取自测历史
    public static String returnSelfTestHistory() {
        return "/retrieval/history";
    }

    //获取自测提设置参数
    public static String returnSelfTestParams() {
        return "/self-test/add";
    }

    //自测组卷获取组卷id接口
    public static String returnSelfTestId() {
        return "/self-test/update";
    }

    public static String returnSelfTestQuestion() {
        return "/self-test/question";
    }

    //自測完成最後一次提交
    public static  String returnSelfTestEnd(){
        return "/self-test/end";
    }
}

//    //获取省份列表
//    public static String returnGetProvinceApi() {
//        return "/common/getprovince";
//    }
//
//    //获取省市下面城市列表
//    public static String returnGetCityApi() {
//        return "/common/getcity";
//    }
//
//    //获取区县列表
//    public static String returnGetDistrict() {
//        return "/common/getdistrict";
//    }
//
//    //获取学校名称
//    public static String returnGetSchoolname() {
//        return "/common/getschool";
//    }
//
//    //获取寒暑假课程列表
//    public static String returnHSCourseList() {
//        return "/mycourse/index";
//    }
//
//    //获取课程版本列表
//    public static String returnCourseVsionList() {
//        return "/mycourse/index";
//    }
//
//    //获取测验列表
//    public static String returnQuizList() {
//        return "/mycourse/quizlist";
//    }
//
//    //获取答题题目
//    public static String returnQuestions() {
//        return "/write/index";
//    }
//
//    //获取同步练习列表
//    public static String returnSyncList() {
//        return "/mycourse/practice";
//    }
//
//    //保存答案
//    public static String returnSaveAnswer() {
//        return "/write/set-api-answer";
//    }
//}
