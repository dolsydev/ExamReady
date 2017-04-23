package com.example.wolf.testseries.fragmentController;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.example.wolf.testseries.R;

/**
 * Created by parveen on 5/10/2015.
 */
public class Contact_Us extends Fragment
{
//    Button submit;
//    EditText mEditTextcmnt,mEditTextname;
//    String dataname,datacmnt;
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.contact_us, container, false);
//        submit=(Button)rootView.findViewById(R.id.buttonsubmit);
//        mEditTextname=(EditText)rootView.findViewById(R.id.editTextusername);
//        mEditTextcmnt=(EditText)rootView.findViewById(R.id.editTextusercomment);
//        submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dataname= mEditTextname.getText().toString();
//                datacmnt= mEditTextcmnt.getText().toString();
//
//                Intent intent = new Intent(Intent.ACTION_SEND);
//                intent.setType("text/html");
//                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"salony.appdevelopment@gmail.com"});
//                intent.putExtra(Intent.EXTRA_SUBJECT, "Subject:");
//                intent.putExtra(Intent.EXTRA_TEXT,"Name: "+dataname+ "\n"+"Comment: "+datacmnt);
//
//                startActivity(Intent.createChooser(intent, "Send Email"));
//
//            }
//        });
//
//
//
//
//
//        return rootView;
//    }
//
//
//
////    public class CustomDialogClass extends Dialog implements
////            View.OnClickListener {
////
////        public Activity c;
////        public Dialog d;
////        public Button mButtonclose;
////
////        public CustomDialogClass(Activity a) {
////            super(a);
////            // TODO Auto-generated constructor stub
////            this.c = a;
////        }
////
////        @Override
////        protected void onCreate(Bundle savedInstanceState) {
////            super.onCreate(savedInstanceState);
////            requestWindowFeature(Window.FEATURE_NO_TITLE);
////            setContentView(R.layout.contact_us);
////            mButtonclose = (Button) findViewById(R.id.btnClose);
////            mButtonclose.setOnClickListener(this);
////
////        }
////
////        @Override
////        public void onClick(View v) {
////            switch (v.getId()) {
////                case R.id.btnClose:
////                    dismiss();
////                    break;
////
////
////
////                default:
////                    break;
////            }
////
////            dismiss();
////        }
  }

