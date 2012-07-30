package com.android_mvc.sample_project.db.entity.lib;

import com.android_mvc.framework.db.entity.BaseLogicalEntity;

/**
 * AP内での論理エンティティを表す，ユーザ定義の基底クラス。
 * DBへの登録値を直接表すのではないので注意。
 * @author id:language_and_engineering
 *
 */
public abstract class LogicalEntity<T> extends BaseLogicalEntity<T>
{
    private static final long serialVersionUID = 1L;
}
