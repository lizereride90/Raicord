package mods.utils;

import mods.constants.URLConstants;

public class Strings {

    public static String getAppName() {
        return URLConstants.IS_BETA ? "Raicord Beta" : "Raicord";
    }
}
