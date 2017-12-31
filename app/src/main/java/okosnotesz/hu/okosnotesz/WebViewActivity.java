package okosnotesz.hu.okosnotesz;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view);
        WebView wv = (WebView) findViewById(R.id.webview_layout);
        ProgressBar pBar = (ProgressBar) wv.findViewById(R.id.webview_progbar);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress)
            {
                if(progress < 100 && pBar.getVisibility() == ProgressBar.GONE){
                    pBar.setVisibility(ProgressBar.VISIBLE);
                }
                pBar.setProgress(progress);
                if(progress == 100) {
                    pBar.setVisibility(ProgressBar.GONE);
                }
            }
        });
        wv.loadUrl("http://okosnotesz.hu");
    }
}
