package admin4.techelm.com.techelmtechnologies.utility.pdf;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.hendrix.pdfmyxml.PdfDocument;
import com.hendrix.pdfmyxml.viewRenderer.AbstractViewRenderer;

import java.io.File;

import admin4.techelm.com.techelmtechnologies.R;

/**
 * Created by admin 4 on 28/03/2017.
 * PDF Creation and Customization with Image
 */

public class PDFClass {

    private static final int PDF_PAGE_WIDTH = 1500;
    private static final int PDF_PAGE_HEIGHT = 2115; // 1850
    private int mPageCount = 0;
    private Context mContext;
    private PdfDocument mDoc;

    public PDFClass(Context context) {
        this.mContext = context;
    }

    public AbstractViewRenderer addPDFPage() {
        AbstractViewRenderer page = new AbstractViewRenderer(this.mContext, R.layout.pdf_page_n) {
            @Override
            protected void initView(View view) {
            }
        };
        page.setReuseBitmap(true);

        // Set page Numbers
        View view = page.getView();
        TextView tv_hello = (TextView) view.findViewById(R.id.textViewPageNumber);
        tv_hello.setText(mPageCount+"");

        // you can reuse the bitmap if you want
        return page;
    }

    public AbstractViewRenderer addPDFHeaderPage() {
        AbstractViewRenderer page = new AbstractViewRenderer(this.mContext, R.layout.pdf_page_1) {
            @Override
            protected void initView(View view) {
                /*
                TextView tv_hello = (TextView)view.findViewById(R.id.textViewPageNumber);
                tv_hello.setText(mPageCount+"");
                */
            }

            private String _text;

            public void setText(String text) {
                _text = text;
            }
        };
        page.setReuseBitmap(true);
        // you can reuse the bitmap if you want
        return page;
    }

    /**
     * Uses Builder to Create the PDF File
     * Called only once
     */
    public PDFClass setUpPDF() {
        this.mDoc = new PdfDocument(this.mContext);

        // add as many pages as you have
        // this.mDoc.addPage(page);

        this.mDoc.setRenderWidth(PDF_PAGE_WIDTH);
        this.mDoc.setRenderHeight(PDF_PAGE_HEIGHT);
        this.mDoc.setOrientation(PdfDocument.A4_MODE.PORTRAIT);
        this.mDoc.setProgressTitle(R.string.gen_pdf_file);
        this.mDoc.setProgressMessage(R.string.gen_please_wait);
        this.mDoc.setFileName(getFileName("REPORT"));

        this.mDoc.setSaveDirectory(getDirectoryStorageDir("pdf"));
        this.mDoc.setInflateOnMainThread(false);
        this.mDoc.setListener(new PdfDocument.Callback() {
            @Override
            public void onComplete(File file) {
                Log.i(PdfDocument.TAG_PDF_MY_XML, "Complete");
            }

            @Override
            public void onError(Exception e) {
                Log.i(PdfDocument.TAG_PDF_MY_XML, "Error");
            }
        });
        return this;
    }

    /**
     *
     * @param page - Can be image or a View Layout from addPDFPage
     * @return this class
     */
    public PDFClass addPage(AbstractViewRenderer page) {
        // Increment Page Number
        incrementPageNumber();
        View view = page.getView();
        TextView tv_hello = (TextView) view.findViewById(R.id.textViewPageNumber);
        tv_hello.setText(mPageCount+"");

        this.mDoc.addPage(page);
        return this;
    }

    /**
     * TO Start Creating FIle
     */
    public void startCreatePDF() {
        mDoc.createPdf(this.mContext);
    }

    /**
     * Uses Builder to Create the PDF File
     * @param ctx - the Activity/Context
     * @param page - Can be image or a View Layout from addPDFPage
     */
    public void myCreatePDF_2(Context ctx, AbstractViewRenderer page) {
        new PdfDocument.Builder(ctx).addPage(page).filename("test").orientation(PdfDocument.A4_MODE.LANDSCAPE)
            .progressMessage(R.string.gen_pdf_file).progressTitle(R.string.gen_please_wait).renderWidth(2115).renderHeight(1500)
            .listener(new PdfDocument.Callback() {
                @Override
                public void onComplete(File file) {
                    Log.i(PdfDocument.TAG_PDF_MY_XML, "Complete");
                }

                @Override
                public void onError(Exception e) {
                    Log.i(PdfDocument.TAG_PDF_MY_XML, "Error");
                }
        }).create().createPdf(ctx);
    }

    private void incrementPageNumber() {
        this.mPageCount++;
    }
    /**
     * PDF File Name
     * @param fileName - Filename with Current Time Stamp when PDF created
     * @return String - File Name
     */
    private String getFileName(String fileName) {
        return String.format(fileName +"_%d", System.currentTimeMillis());
    }

    /**
     * Called at other Utility Classes
     * @param directory - Directory Set to TECHELM/pdf
     * @return File
     */
    private File getDirectoryStorageDir(String directory) {
        String mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFilePath += "/TELCHEM/" + directory;
        File file = new File(mFilePath);
        if (!file.mkdirs()) {
            Log.e("PDFClass", "Directory not created, or already existed");
        }
        return file;
    }

}
