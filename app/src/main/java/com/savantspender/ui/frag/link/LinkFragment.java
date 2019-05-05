package com.savantspender.ui.frag.link;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.savantspender.R;
import com.savantspender.viewmodel.LinkViewModel;

import java.util.HashMap;

public class LinkFragment extends Fragment {
    private LinkViewModel mViewModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity(), new LinkViewModel.Factory(getActivity().getApplication())).get(LinkViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_link, container, false);

        initWebViewToPlaid(view.findViewById(R.id.web_view));

        return view;
    }


    protected void initWebViewToPlaid(WebView wv) {
        // Initialize Link
        HashMap<String, String> linkInitializeOptions = new HashMap<>();
        linkInitializeOptions.put("key", "efebb105ab905b6e6cbe0a12e4689b");
        linkInitializeOptions.put("product", "auth");
        linkInitializeOptions.put("apiVersion", "v2"); // set this to "v1" if using the legacy Plaid API
        linkInitializeOptions.put("env", "sandbox");
        linkInitializeOptions.put("clientName", "Savant Spender");
        linkInitializeOptions.put("selectAccount", "true");
        linkInitializeOptions.put("webhook", "http://requestb.in");
        linkInitializeOptions.put("baseUrl", "https://cdn.plaid.com/link/v2/stable/link.html");
        // If initializing Link in PATCH / update mode, also provide the public_token
        // linkInitializeOptions.put("token", "PUBLIC_TOKEN")

        // Generate the Link initialization URL based off of the configuration options.
        final Uri linkInitializationUrl = generateLinkInitializationUrl(linkInitializeOptions);

        Log.w("Spender", "Link: " + linkInitializationUrl.toString());

        // Modify Webview settings - all of these settings may not be applicable
        // or necessary for your integration.;
        WebSettings webSettings = wv.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        WebView.setWebContentsDebuggingEnabled(true);


        // Override the Webview's handler for redirects
        // Link communicates success and failure (analogous to the web's onSuccess and onExit
        // callbacks) via redirects.
        wv.setWebViewClient(new WebViewClient() {
            @SuppressWarnings("deprecation")
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // Parse the URL to determine if it's a special Plaid Link redirect or a request
                // for a standard URL (typically a forgotten password or account not setup link).
                // Handle Plaid Link redirects and open traditional pages directly in the  user's
                // preferred browser.
                Uri parsedUri = Uri.parse(url);
                return handleUri(wv, parsedUri);
            }

            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return handleUri(view, request.getUrl());
            }
        });


        // Initialize Link by loading the Link initialization URL in the Webview
        wv.loadUrl(linkInitializationUrl.toString());
    }

    private void onSuccessfulConnection(HashMap<String, String> data) {
        mViewModel.extractLinkDetails(data);
    }

    private boolean handleUri(WebView view, Uri parsedUri) {
        if (parsedUri.getScheme().equals("plaidlink")) {
            String action = parsedUri.getHost();
            HashMap<String, String> linkData = parseLinkUriData(parsedUri);

            if (action.equals("connected")) {
                // User successfully linked
                Log.w("Public token: ", linkData.get("public_token"));
                Log.w("Account ID: ", linkData.get("account_id"));
                Log.w("Account name: ", linkData.get("account_name"));
                Log.w("Institution id: ", linkData.get("institution_id"));
                Log.w("Institution name: ", linkData.get("institution_name"));


                for (HashMap.Entry<String, String> kvp : linkData.entrySet()) {
                    Log.w("Spender", kvp.getKey() + " -> " + kvp.getValue());
                }

                /*
  -26 00:48:02.988 20519-20519/com.savantspender W/Spender: public_token -> public-sandbox-70af9b4e-f4d5-424a-90b5-456be8ee2c7d
04-26 00:48:02.988 20519-20519/com.savantspender W/Spender: account_mask -> 1111
04-26 00:48:02.988 20519-20519/com.savantspender W/Spender: link_session_id -> 53939766-f0cf-4ac3-83e5-d177363655d1
04-26 00:48:02.988 20519-20519/com.savantspender W/Spender: accounts -> [{"_id":"xPj3voMgKKHoMM9BlpM3CZGo3wKqGAfnGQRd5","balance":{"available":200,"current":210},"meta":{"name":"Plaid Saving","number":"1111"},"type":"depository","subtype":"savings"}]
04-26 00:48:02.988 20519-20519/com.savantspender W/Spender: account_subtype -> savings
04-26 00:48:02.988 20519-20519/com.savantspender W/Spender: account_type -> depository
04-26 00:48:02.988 20519-20519/com.savantspender W/Spender: institution_id -> ins_3
04-26 00:48:02.988 20519-20519/com.savantspender W/Spender: institution_name -> Chase
04-26 00:48:02.988 20519-20519/com.savantspender W/Spender: account_name -> Plaid Saving
04-26 00:48:02.988 20519-20519/com.savantspender W/Spender: account_id -> xPj3voMgKKHoMM9BlpM3CZGo3wKqGAfnGQRd5
                 */

                onSuccessfulConnection(linkData);

            } else if (action.equals("exit")) {
                mViewModel.setCancelled();

            } else if (action.equals("event")) {
                // The event action is fired as the user moves through the Link flow
                Log.w("Event name: ", linkData.get("event_name"));
            } else {
                Log.w("Link action detected: ", action);
            }
            // Override URL loading
            return true;
        } else if (parsedUri.getScheme().equals("https") ||
                parsedUri.getScheme().equals("http")) {
            // Open in browser - this is most  typically for 'account locked' or
            // 'forgotten password' redirects
            view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, parsedUri));

            // Override URL loading
            return true;
        } else {
            // Unknown case - do not override URL loading
            return false;
        }
    }

    // Generate a Link initialization URL based on a set of configuration options
    public Uri generateLinkInitializationUrl(HashMap<String,String> linkOptions) {
        Uri.Builder builder = Uri.parse(linkOptions.get("baseUrl"))
                .buildUpon()
                .appendQueryParameter("isWebview", "true")
                .appendQueryParameter("isMobile", "true");
        for (String key : linkOptions.keySet()) {
            if (!key.equals("baseUrl")) {
                builder.appendQueryParameter(key, linkOptions.get(key));
            }
        }
        return builder.build();
    }

    // Parse a Link redirect URL querystring into a HashMap for easy manipulation and access
    public HashMap<String,String> parseLinkUriData(Uri linkUri) {
        HashMap<String,String> linkData = new HashMap<>();
        for(String key : linkUri.getQueryParameterNames()) {
            linkData.put(key, linkUri.getQueryParameter(key));
        }
        return linkData;
    }
}
