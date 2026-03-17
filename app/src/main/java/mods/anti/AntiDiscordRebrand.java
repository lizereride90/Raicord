package mods.anti;

import android.annotation.SuppressLint;

import mods.constants.PreferenceKeys;
import mods.preference.Prefs;
import mods.utils.LogUtils;

@SuppressWarnings("unused")
public class AntiDiscordRebrand {

    public static boolean isEnabled() {
        return Prefs.getBoolean(PreferenceKeys.REMOVE_DISCORD_REBRAND_V2, false);
    }

    @SuppressLint("NonConstantResourceId")
    public static int overrideTheme(int current) {
        if (!isEnabled()) return current;

        switch (current) {
            case com.raicord.R.style.AppTheme_Light: return com.raicord.R.style.AppTheme_Light_NoRebrand;
            case com.raicord.R.style.AppTheme_Dark:  return com.raicord.R.style.AppTheme_Dark_NoRebrand;
            case com.raicord.R.style.AppTheme_Dark_Evil:  return com.raicord.R.style.AppTheme_Dark_Evil_NoRebrand;
            default: {
                LogUtils.log("Raicord", "unknown style res (" + current + ")");
                return current;
            }
        }
    }
}
