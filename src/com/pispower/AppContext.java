package com.pispower;

import com.pispower.video.sdk.VideoSDK;

import static com.pispower.R.*;
import static com.pispower.R.string.*;

/**
 * Created by kinghai on 8/21/15.
 */
public class AppContext {

    public static String accessKey; //input your accessKey, This property can be found in video.pispower.com
    public static String accessSecret;  //input your accessSecret, This property can be found in video.pispower.com

    public static VideoSDK getSDK() {
        if (null == accessKey) {
            throw new RuntimeException("accessKey is missing");
        }

        if(null == accessSecret) {
            throw new  RuntimeException("accessSecret is missing");
        }

        return new VideoSDK(accessKey, accessSecret);
    }
}
