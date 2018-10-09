package com.example.maheshpujala.dummy.printing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.support.v4.print.PrintHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.example.maheshpujala.dummy.R;

public class PrintingActivity extends AppCompatActivity {
ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printing);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
    }

    public void printBitmap(View view) {
        PrintHelper photoPrinter = new PrintHelper(PrintingActivity.this);
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.droid);
        photoPrinter.printBitmap("droid.png - test print", bitmap);
    }



    public void doWebViewPrint(View view) {
        progressBar.setVisibility(View.VISIBLE);
        // Create a WebView object specifically for printing
        WebView webView = new WebView(PrintingActivity.this);
        webView.loadUrl("http://developer.android.com/about/index.html");

        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.e( "page finished loading " ,"===="+ url);
                progressBar.setVisibility(View.GONE);
                createWebPrintJob(view);
            }
        });
    }


    private void createWebPrintJob(WebView webView) {

        // Get a PrintManager instance
        PrintManager printManager = (PrintManager) PrintingActivity.this
                .getSystemService(Context.PRINT_SERVICE);

        String jobName = getString(R.string.app_name) + " Document";

        // Get a print adapter instance
        PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter(jobName);

        // Create a print job with name and adapter instance
        PrintJob printJob = printManager.print(jobName, printAdapter,
                new PrintAttributes.Builder().build());

    }
}
