package com.jasperhale.onetapdoze.View;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;

import com.jasperhale.onetapdoze.BaseClass.LogUtil;
import com.jasperhale.onetapdoze.R;
import com.jasperhale.onetapdoze.Service.BroadcastService;
import com.jasperhale.onetapdoze.Service.TimeService;

public class SettingFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {

    SwitchPreference notify;
    SwitchPreference NotifyPriority;
    SwitchPreference JustSceenOn;

    private SharedPreferences prefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting);

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        //设置监听事件
        notify = (SwitchPreference) findPreference("Notify");
        //notify.setDefaultValue(true);
        //notify.setSummary("Doze模式改变创建状态栏通知");

        NotifyPriority = (SwitchPreference) findPreference("NotifyPriority");
        //NotifyPriority.setDefaultValue(false);
        //NotifyPriority.setSummary("退出doze模式后通知隐藏");

        JustSceenOn = (SwitchPreference) findPreference("JustSceenOn");
        //JustSceenOn.setDefaultValue(false);
        //JustSceenOn.setSummary("作用域不区分亮屏息屏");

        notify.setOnPreferenceChangeListener(this);
        NotifyPriority.setOnPreferenceChangeListener(this);
        JustSceenOn.setOnPreferenceChangeListener(this);


        CheckBoxPreference cp = (CheckBoxPreference) findPreference("InterruptIdle");
        cp.setOnPreferenceClickListener(this);
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        switch (preference.getKey()) {
            case "Notify": {
                if ((boolean) newValue == true) {
                    preference.setSummary("Doze模式改变创建状态栏通知");

                } else if ((boolean) newValue == false) {
                    preference.setSummary("Doze模式改变不显示通知");
                }
                ;
            }
            ;
            break;
            case "NotifyPriority": {
                if ((boolean) newValue == true) {
                    preference.setSummary("Doze通知置顶");

                } else if ((boolean) newValue == false) {
                    preference.setSummary("退出doze模式后通知隐藏");
                }
                ;
            }
            ;
            break;
            case "JustSceenOn": {
                if ((boolean) newValue == true) {
                    preference.setSummary("作用域仅限亮屏，关屏释放控制权限");

                } else if ((boolean) newValue == false) {
                    preference.setSummary("作用域不区分亮屏息屏");
                }
                ;
            }
            ;
            break;
            default:
                break;

        }

        LogUtil.d("setting", preference.getKey());
        return true;
    }


    @Override
    public boolean onPreferenceClick(Preference preference) {
        switch (preference.getKey()) {
            case "InterruptIdle": {
                Boolean checkboxValue = prefs.getBoolean(preference.getKey(), false);
                if (checkboxValue == true) {
                    LogUtil.d("setting", "on");
                    //启用时间更新
                    getActivity().startService(new Intent(getContext(), TimeService.class));
                } else if (checkboxValue == false){
                    LogUtil.d("setting", "off");
                    //取消时间更新
                    getActivity().stopService(new Intent(getContext(), TimeService.class));
                }
            }
            break;

            default:
                break;
        }
        LogUtil.d("setting", preference.getKey());
        return true;
    }
}
