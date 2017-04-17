package admin4.techelm.com.techelmtechnologies.utility.pdf;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hendrix.pdfmyxml.viewRenderer.AbstractViewRenderer;

/**
 * Created by admin 4 on 28/03/2017.
 * Testing PDF output, this take some time almost 2mins...
 * Should this be implemented on the backend, and then to be downloaded on the app
 */

public class PDFClass_Activity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PDFClass _pdf = new PDFClass(this).setUpPDF();
        AbstractViewRenderer headerPage = _pdf.addPDFHeaderPage();
        AbstractViewRenderer page2 = _pdf.addPDFPage();
        AbstractViewRenderer page3 = _pdf.addPDFPage();
        AbstractViewRenderer page4 = _pdf.addPDFHeaderPage();
        _pdf.addPage(headerPage)
            .addPage(page2)
            .addPage(page3)
            .addPage(page4)
            .startCreatePDF();
    }

}
