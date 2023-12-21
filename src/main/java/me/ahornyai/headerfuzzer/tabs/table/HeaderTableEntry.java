package me.ahornyai.headerfuzzer.tabs.table;

import burp.api.montoya.http.message.HttpHeader;
import lombok.Data;

@Data
public final class HeaderTableEntry {
    private boolean constant;
    private HttpHeader header;

    /**
     * @param constant It makes the header present in every request sent by the fuzzer.
     * @param header HttpHeader
     */
    public HeaderTableEntry(boolean constant, HttpHeader header) {
        this.constant = constant;
        this.header = header;
    }

}
