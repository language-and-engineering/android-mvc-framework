package com.android_mvc.framework.controller.routing;

import java.io.Serializable;

/**
 * あるオブジェクトを，Intent経由で運搬可能にするためのインタフェース。
 * @author id:language_and_engineering
 *
 */
public interface IntentPortable extends Serializable
{
    // NOTE:「implements Serializable」だと目的がはっきりしないので，
    // Intent経由で運搬可能であることのマークとして設けた。
}
