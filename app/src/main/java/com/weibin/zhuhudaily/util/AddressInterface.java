package com.weibin.zhuhudaily.util;

/**
 * Created by weibinhuang on 18-2-11.
 */

public interface AddressInterface {
    String newsAddress = "https://news-at.zhihu.com/api/4/news/latest";

    String themeAddress = "https://news-at.zhihu.com/api/4/theme/";

    String extraAddress = "https://news-at.zhihu.com/api/4/story-extra/";

    String commentAddress = "https://news-at.zhihu.com/api/4/story/";

    String articalAddress = "https://news-at.zhihu.com/api/4/news/";

    String beforeAddress = "https://news-at.zhihu.com/api/4/news/before/";

    String versionAddress = "https://news-at.zhihu.com/api/4/version/android/2.3.0";

    String checkInAddress = "http://192.168.43.167/insert.php";

    String checkloginAddress = "http://192.168.43.167/check.php";

    String collcectAddress = "http://192.168.43.167/update.php";

    String bingpicAddress = "http://guolin.tech/api/bing_pic";

    void sendRequestWithHttpURLConnection();
}
