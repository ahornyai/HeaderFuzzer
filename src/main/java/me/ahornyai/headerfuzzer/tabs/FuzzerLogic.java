package me.ahornyai.headerfuzzer.tabs;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.http.message.HttpHeader;
import burp.api.montoya.http.message.requests.HttpRequest;
import me.ahornyai.headerfuzzer.tabs.table.HeaderTableEntry;

import java.util.ArrayList;
import java.util.List;

public class FuzzerLogic {
    private final MontoyaApi api;
    private final HttpRequest request;
    private final List<HeaderTableEntry> entries;
    private final List<HttpRequest> requests;

    public FuzzerLogic(MontoyaApi api, HttpRequest request, List<HeaderTableEntry> entries) {
        this.api = api;
        this.request = request;
        this.entries = entries;
        this.requests = new ArrayList<>();

        createRequests();
    }

    private void createRequests() {
        List<HttpHeader> constantHeaders = new ArrayList<>();
        List<HttpHeader> variationHeaders = new ArrayList<>();

        for (HeaderTableEntry entry : entries) {
            if (entry.isConstant()) {
                constantHeaders.add(entry.getHeader());
            } else {
                variationHeaders.add(entry.getHeader());
            }
        }

        // generate the variations of variationHeaders (size will be 2^n)
        List<HttpRequest> requests = new ArrayList<>();
        int n = variationHeaders.size();

        for (int i = 0; i < (1 << n); i++) {
            List<HttpHeader> variation = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                if ((i & (1 << j)) != 0) {
                    variation.add(variationHeaders.get(j));
                }
            }

            // turn the headers into an HttpRequest, add the constant headers
            variation.addAll(constantHeaders);

            requests.add(HttpRequest.http2Request(request.httpService(), variation, request.body()));
        }
    }

}