package admin4.techelm.com.techelmtechnologies.service_report;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.view.ViewGroup.LayoutParams;

import admin4.techelm.com.techelmtechnologies.R;

public class SigningOff_4 extends AppCompatActivity {

    private RelativeLayout mRelativeLayout;
    private ImageButton imageButtonViewSignature;

    private PopupWindow mPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signing_off_end_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initButton();

        initSignaturePadPopUp();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
        // TODO: Notify user Here that he is already finished submitting the data
    }

    private void initButton() {
        /** BUTTON BACK */
        Button button_back = (Button) findViewById(R.id.button_back);
        button_back.setVisibility(View.GONE);

        /** BUTTON NEXT */
        Button button_next = (Button) findViewById(R.id.button_next);
        button_next.setText("END TASK");
        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SigningOff_4.this, ServiceReport_TaskCompleted_5.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });

        /** BUTTON VIEW DETAILS */
        ImageButton buttonViewDetails = (ImageButton) findViewById(R.id.buttonViewDetails);
        buttonViewDetails.setVisibility(View.GONE);
    }

    private void initSignaturePadPopUp() {
        imageButtonViewSignature = (ImageButton) findViewById(R.id.imageButtonViewSignature);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.rl);
        // Set a click listener for the text view
        imageButtonViewSignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layFlator = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);

                View popUp = layFlator.inflate(R.layout.i_pop_up_signature, null);

                final PopupWindow popupWindow = new PopupWindow(popUp, 700, 680, true);
                // popupWindow.setAnimationStyle(R.style.PopupAnimation);
                popupWindow.showAtLocation(mRelativeLayout, Gravity.CENTER, 0, 0);
                popupWindow.showAsDropDown(popUp, 50, -30);

                ImageButton btnHybrid = (ImageButton) popUp.findViewById(R.id.ib_close);
                btnHybrid.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("hybrid", "called");
                        popupWindow.dismiss();
                    }
                });

            }
        });
    }

    private void initSignaturePadPopUp2() {
        // Get the widgets reference from XML layout
        final LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        final View customView = inflater.inflate(R.layout.i_pop_up_signature, null);

        mRelativeLayout = (RelativeLayout) findViewById(R.id.rl);
        imageButtonViewSignature = (ImageButton) findViewById(R.id.imageButtonViewSignature);

        // Set a click listener for the text view
        imageButtonViewSignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Initialize a new instance of LayoutInflater service


                // Inflate the custom layout/view


                // Initialize a new instance of popup window
                mPopupWindow = new PopupWindow(
                        customView,
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT
                );

                // Set an elevation value for popup window
                // Call requires API level 21
                if(Build.VERSION.SDK_INT>=21){
                    mPopupWindow.setElevation(5.0f);
                }

                // Get a reference for the custom view close button
                ImageButton closeButton = (ImageButton) customView.findViewById(R.id.ib_close);

                // Set a click listener for the popup window close button
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Dismiss the popup window
                        mPopupWindow.dismiss();
                    }
                });

                // Finally, show the popup window at the center location of root relative layout
                mPopupWindow.showAtLocation(mRelativeLayout, Gravity.CENTER,0,0);
            }
        });
    }


}
