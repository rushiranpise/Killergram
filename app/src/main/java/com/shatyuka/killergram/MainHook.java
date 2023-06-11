package com.shatyuka.killergram;

import java.util.Arrays;
import java.util.List;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class MainHook implements IXposedHookLoadPackage {

    public final static List<String> hookPackages = Arrays.asList(
            "app.nicegram",
            "com.blxueya.gugugram",
            "com.cool2645.nekolite",
            "com.evildayz.code.telegraher",
            "com.exteragram.messenger.beta",
            "com.exteragram.messenger",
            "com.iMe.android",
            "com.jasonkhew96.pigeongram",
            "ellipi.messenger",
            "ir.ilmili.telegraph",
            "it.owlgram.android",
            "me.ninjagram.messenger",
            "nekox.messenger",
            "icu.ketal.yunigram.beta",
            "icu.ketal.yunigram.lspatch.beta",
            "icu.ketal.yunigram.lspatch",
            "icu.ketal.yunigram",
            "me.luvletter.nekox",
            "nekox.messenger",
            "org.aka.messenger",
            "org.forkclient.messenger.beta",
            "org.forkclient.messenger",
            "org.forkgram.messenger",
            "org.nift4.catox",
            "org.ninjagram.messenger",
            "org.telegram.BifToGram",
            "org.telegram.mdgram",
            "org.telegram.mdgramyou",
            "org.telegram.messenger.beta",
            "org.telegram.messenger.web",
            "org.telegram.messenger",
            "org.telegram.plus",
            "top.qwq2333.nullgram",
            "tw.nekomimi.nekogram",
            "ua.itaysonlab.messenger",
            "xyz.nextalone.nnngram"
    );

    @Override
    public void handleLoadPackage(final LoadPackageParam lpparam) {

        if (!hookPackages.contains(lpparam.packageName)) return;

        XposedClass messagesController = new XposedClass("org.telegram.messenger.MessagesController", lpparam.classLoader);
        messagesController.hookAllMethods("getSponsoredMessages", XC_MethodReplacement.returnConstant(null));
        messagesController.hookAllMethods("isChatNoForwards", XC_MethodReplacement.returnConstant(false));

        XposedClass messagesController = new XposedClass("org.telegram.messenger.MessageObject", lpparam.classLoader);
        messagesController.hookAllMethods("canForwardMessage", XC_MethodReplacement.returnConstant(true));
        messagesController.hookAllMethods("isSecretMedia", XC_MethodReplacement.returnConstant(false));

        XposedClass chatUIActivity = new XposedClass("org.telegram.ui.ChatActivity", lpparam.classLoader);
        chatUIActivity.hookAllMethods("addSponsoredMessages", XC_MethodReplacement.returnConstant(null));
        chatUIActivity.hookAllMethods("openForward", XC_MethodReplacement.returnConstant(true));

        XposedClass sponsoredMessages = new XposedClass("org.telegram.tgnet.TLRPC$messages_SponsoredMessages", lpparam.classLoader);
        sponsoredMessages.hookAllMethods("TLdeserialize", XC_MethodReplacement.returnConstant(null));

        XposedClass getSponsoredMessages = new XposedClass("org.telegram.tgnet.TLRPC$TL_channels_getSponsoredMessages", lpparam.classLoader);
        getSponsoredMessages.hookAllMethods("a", XC_MethodReplacement.returnConstant(null));

        XposedClass chat = new XposedClass("org.telegram.tgnet.TLRPC$Chat", lpparam.classLoader);
        chat.hookAllMethods("TLdeserialize", new NoForwardsHook(false));

        XposedClass sharedConfig = new XposedClass("org.telegram.messenger.SharedConfig", lpparam.classLoader);
        sharedConfig.hookAllMethods("getDevicePerformanceClass", XC_MethodReplacement.returnConstant(2));

        XposedClass userConfig = new XposedClass("org.telegram.messenger.UserConfig", lpparam.classLoader);
        userConfig.hookAllMethods("getMaxAccountCount", XC_MethodReplacement.returnConstant(999));
        userConfig.hookAllMethods("hasPremiumOnAccounts", XC_MethodReplacement.returnConstant(true));
        userConfig.hookAllMethods("isPremium", XC_MethodReplacement.returnConstant(true));
    }
}
