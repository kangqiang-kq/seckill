package com.xxxx.seckill;

import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;

import java.awt.image.BufferedImage;

public class CaptchaExample {
    public static void main(String[] args) {
        // 三个参数分别为宽、高、位数
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 5);
        specCaptcha.setCharType(Captcha.TYPE_ONLY_NUMBER);
        System.out.println(specCaptcha.text());
    }
}
