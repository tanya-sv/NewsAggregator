package com.news.aggregator.view;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.news.aggregator.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class WebViewFragment extends Fragment {

    private static final String EXTRA_SOURCE = "extraSource";
    private static final String EXTRA_URL = "extraUrl";

    private Unbinder unbinder;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.progressWheel)
    ProgressBar progressWheel;
    @BindView(R.id.webview)
    WebView webView;

    private String source;
    private String url;

    public static WebViewFragment getInstance(String source, String url) {
        Bundle args = new Bundle();
        args.putString(EXTRA_SOURCE, source);
        args.putString(EXTRA_URL, url);
        WebViewFragment fragment = new WebViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        source = args.getString(EXTRA_SOURCE);
        url = args.getString(EXTRA_URL);
        View rootView = inflater.inflate(R.layout.fragment_webview, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar.setTitle(source);
        toolbar.setNavigationIcon(R.drawable.ic_nav_back);
        toolbar.setNavigationOnClickListener(v -> getFragmentManager().popBackStack());

        WebSettings webSettings = webView.getSettings();
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                if (progressWheel != null) {
                    progressWheel.setVisibility(View.GONE);
                }
            }
        });
        webView.loadUrl(url);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
