package com.example.wolf.testseries.fragmentController;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.wolf.testseries.CentralController.CentralController;
import com.example.wolf.testseries.CentralController.GlobalController;
import com.example.wolf.testseries.ParseModelController.SubscriptionInfo;
import com.example.wolf.testseries.ParseModelController.TestInfo;
import com.example.wolf.testseries.R;
import com.example.wolf.testseries.sqliteController.DatabaseController;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

public class Payment_Option extends Fragment implements OnClickListener
{
    int subscriptionDays=0;
    private ArrayList<TestInfo> selectedSubjects;
    private Button btn_1_Week,btn_2_Week,btn_3_Week,btn_4_Week;
    //set the environment for production/sandbox/no netowrk


    /**
     * Please uncomment Environment_production for LIVE payment and comment sandbox.
     * */
//    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_PRODUCTION;
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;


    /*
    * Please provide ClientId for PayPal Registered account
    * **/
    private static final String CONFIG_CLIENT_ID = "AVqZuDO0QL7QqDcCvklrjtSjn-v8WDmBtQGXm_OYegLVggs3Ya9JzDbTzLEBINVykzr2RiRYxHStlwAP";

    private static final int REQUEST_PAYPAL_PAYMENT = 1;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID)
                    // The following are only used in PayPalFuturePaymentActivity.
            .merchantName("Parveen prajapati")
            .merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
            .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.payment_option, container, false);
        btn_1_Week = (Button) rootView.findViewById(R.id.button_1_Month);
        btn_1_Week.setOnClickListener(this);

        btn_2_Week = (Button) rootView.findViewById(R.id.button_2_Month);
        btn_2_Week.setOnClickListener(this);

        btn_3_Week = (Button) rootView.findViewById(R.id.button_3_Month);
        btn_3_Week.setOnClickListener(this);

        btn_4_Week = (Button) rootView.findViewById(R.id.button_4_Month);
        btn_4_Week.setOnClickListener(this);

        Intent intent = new Intent(getActivity(), PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);

        getActivity().startService(intent);
        return rootView;

    }




    /**
     * call pay pal services
     */






    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch(v.getId()){
            case R.id.button_1_Month :
                subscriptionDays=7;
                PayPalPayment thingToBuy = new PayPalPayment(new BigDecimal(1.99),"USD", "ExamMate",
                        PayPalPayment.PAYMENT_INTENT_SALE);

                Intent intent = new Intent(getActivity(), PaymentActivity.class);

                intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

                startActivityForResult(intent, REQUEST_PAYPAL_PAYMENT);
                break;


            case R.id.button_2_Month :
                subscriptionDays=14;
                PayPalPayment thingToBuy2 = new PayPalPayment(new BigDecimal(3.49),"USD", "ExamMate",
                        PayPalPayment.PAYMENT_INTENT_SALE);

                Intent intent2 = new Intent(getActivity(), PaymentActivity.class);

                intent2.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy2);

                startActivityForResult(intent2, REQUEST_PAYPAL_PAYMENT);
                break;

            case R.id.button_3_Month :
                subscriptionDays=30;
                PayPalPayment thingToBuy3 = new PayPalPayment(new BigDecimal(6.99),"USD", "ExamMate",
                        PayPalPayment.PAYMENT_INTENT_SALE);

                Intent intent3 = new Intent(getActivity(), PaymentActivity.class);

                intent3.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy3);

                startActivityForResult(intent3, REQUEST_PAYPAL_PAYMENT);
                break;

            case R.id.button_4_Month :
                subscriptionDays=60;
                PayPalPayment thingToBuy4 = new PayPalPayment(new BigDecimal(21.99),"USD", "ExamMate",
                        PayPalPayment.PAYMENT_INTENT_SALE);

                Intent intent4 = new Intent(getActivity(), PaymentActivity.class);

                intent4.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy4);

                startActivityForResult(intent4, REQUEST_PAYPAL_PAYMENT);
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PAYPAL_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data
                        .getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        System.out.println("Responseeee"+confirm);
                        Log.i("paymentExample", confirm.toJSONObject().toString());
                        JSONObject jsonObj=new JSONObject(confirm.toJSONObject().toString());

                        String paymentId=jsonObj.getJSONObject("response").getString("id");
                        System.out.println("payment id:-==" + paymentId);
                        Toast.makeText(getActivity(), "You have successfully paid for the subscription. Your payment id is "+paymentId, Toast.LENGTH_LONG).show();
                        String userId=GlobalController.getObjectId(getActivity());
                        fetchSubscriptionInfoFromServer(userId, getActivity());
                    } catch (JSONException e) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample", "An invalid Payment was submitted. Please see the docs.");
            }
        }


    }

    public void setSelectedSubjects(ArrayList<TestInfo> selectedSubjects)
    {
        this.selectedSubjects=selectedSubjects;
    }

    public void fetchSubscriptionInfoFromServer(String userId, Activity activity)
    {
        CentralController.showProgressDialog(activity, "Checking subscription.");
        ParseQuery<SubscriptionInfo> query = ParseQuery.getQuery(SubscriptionInfo.class);
// Define our query conditions
        query.whereEqualTo("userId", userId);
//        query.orderByAscending("year");
// Execute the find asynchronously
        query.findInBackground(new FetchSubscriptionInfoResponse());
    }

    private class FetchSubscriptionInfoResponse implements FindCallback<SubscriptionInfo>
    {
        @Override
        public void done(List<SubscriptionInfo> subscriptionInfos, ParseException e)
        {
            CentralController.dismissProgressDialog();
            if (e == null)
            {
                ArrayList<SubscriptionInfo> subscriptionInfoList=(ArrayList<SubscriptionInfo>)subscriptionInfos;
                CentralController.filterValidSubscriptions(getActivity(), subscriptionInfoList);
                saveNewSubscription(subscriptionInfoList);
            }
            else
            {
                Log.d("check", "Exception -->: " + e.getMessage());
            }
        }
    }

    private void saveNewSubscription(ArrayList<SubscriptionInfo> currentSubscriptions)
    {
        ArrayList<SubscriptionInfo> newSubscriptionInfos=new ArrayList<SubscriptionInfo>();
        for(int i=0; i< selectedSubjects.size(); i++)
        {
            TestInfo selectedSubject=selectedSubjects.get(i);
            Boolean isMatched=false;
            for(int j=0; j< currentSubscriptions.size(); j++)
            {
                SubscriptionInfo currentSubscription=currentSubscriptions.get(j);
                if(selectedSubject.getTestId()==currentSubscription.getSubjectId())
                {
                    int subscribedDays=currentSubscription.getSubscriptionDays();
                    currentSubscription.setSubscriptionDays(subscriptionDays+subscribedDays);
                    newSubscriptionInfos.add(currentSubscription);
                    isMatched=true;
                    break;
                }
            }
            if(!isMatched)
            {
                addFreshSubscriptionInfo(selectedSubject.getTestId(), newSubscriptionInfos, currentSubscriptions);
            }
        }
        saveSubscriptionInfoToServer(newSubscriptionInfos, currentSubscriptions);
    }



    private void addFreshSubscriptionInfo(int subjectId, ArrayList<SubscriptionInfo> subscriptionInfos, ArrayList<SubscriptionInfo
            > currentSubscriptionInfos)
    {
        String userId=GlobalController.getObjectId(getActivity());
        String subscriptionDate=CentralController.getCurrentDateTime(getActivity());
        SubscriptionInfo subscriptionInfo=new SubscriptionInfo(userId, subscriptionDate, subscriptionDays, subjectId);
        subscriptionInfos.add(subscriptionInfo);
        currentSubscriptionInfos.add(subscriptionInfo);
    }


    private void saveSubscriptionInfoToServer(ArrayList<SubscriptionInfo> newSubscriptionInfos, ArrayList<SubscriptionInfo> currentSubscriptionInfos)
    {
        ParseObject.saveAllInBackground(newSubscriptionInfos, new SubscriptionCallback(currentSubscriptionInfos));
    }

    private class SubscriptionCallback implements SaveCallback
    {
        private ArrayList<SubscriptionInfo> currentSubscriptionInfos;

        public SubscriptionCallback(ArrayList<SubscriptionInfo> currentSubscriptionInfos)
        {
            this.currentSubscriptionInfos=currentSubscriptionInfos;
        }

        @Override
        public void done(ParseException e)
        {
            CentralController.dismissProgressDialog();
            if (e == null) {
                // Hooray! Let them use the app now.
                DatabaseController databaseController=new DatabaseController(getActivity());
                databaseController.insertSubscriptionInfos(currentSubscriptionInfos);

            } else
            {
                // Sign up didn't succeed. Look at the ParseException
                // to figure out what went wrong
                Log.d("check", "check you" + e.getMessage());
            }
        }
    }





}
