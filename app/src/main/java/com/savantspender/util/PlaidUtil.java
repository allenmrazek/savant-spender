package com.savantspender.util;

import com.plaid.client.PlaidClient;

public class PlaidUtil {
    // id and secret exposed! don't ever do this in a real app
    // this should be on a server that the app contacts after acquiring a valid public token
    public static PlaidClient build() {
        return PlaidClient.newBuilder()
                .clientIdAndSecret("5c54506a47679a00117ebada", "7be0aefd21b235efcb5717101969ca")
                .publicKey("efebb105ab905b6e6cbe0a12e4689b") // optional. only needed to call endpoints that require a public key
                .sandboxBaseUrl() // or equivalent, depending on which environment you're calling into
                .build();
    }
}
