package me.ahornyai;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
import me.ahornyai.menu.FuzzerContextMenuProvider;
import me.ahornyai.tabs.FuzzerTab;

public class HeaderFuzzer implements BurpExtension {

    @Override
    public void initialize(MontoyaApi api) {
        api.extension().setName("HeaderFuzzer");

        final FuzzerTab fuzzerTab = new FuzzerTab(api);

        api.userInterface().registerSuiteTab("Header Fuzzer", fuzzerTab);
        api.userInterface().registerContextMenuItemsProvider(new FuzzerContextMenuProvider(api, fuzzerTab));
    }

}