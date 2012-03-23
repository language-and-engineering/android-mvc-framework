package com.android_mvc.framework.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * 特定のクラス内でのデバッグログの出力を抑止するアノテーション。引数は省略可能。
 * @author id:language_and_engineering
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SuppressDebugLog
{
    boolean value() default true;
}

// @see http://d.hatena.ne.jp/language_and_engineering/20120209/p1

