package com.baidu.push;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;

/**
 * //developer.baidu.com/wiki/index.php?title=docs/cplat/push/faq#.E4.B8.
 * BA.E4.BD.95.E9.80.9A.E8.BF.87Server_SDK.E6
 * .8E.A8.E9.80.81.E6.88.90.E5.8A.9F.EF.BC.8CAndroid.E7.AB.AF.E5.8D.B4.E6.94.B6.E4.B8.8D.E5.88.B0.E9.80.9A.E7.9F.A5.EF.
 * B C . 9 F
 */
public class CustomActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Resources resource = this.getResources();
        String pkgName = this.getPackageName();

        setContentView(resource.getIdentifier("custom_activity", "layout",
                pkgName));
    }
}
