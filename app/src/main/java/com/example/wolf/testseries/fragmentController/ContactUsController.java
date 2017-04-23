package com.example.wolf.testseries.fragmentController;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wolf.testseries.CentralController.CentralController;
import com.example.wolf.testseries.ParseModelController.ContactUs;
import com.example.wolf.testseries.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ContactUsController.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ContactUsController#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactUsController extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private View rootView;
    private TextView email, phone, website;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContactUsController.
     */
    // TODO: Rename and change types and number of parameters
    public static ContactUsController newInstance(String param1, String param2) {
        ContactUsController fragment = new ContactUsController();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ContactUsController() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView= inflater.inflate(R.layout.contact_us, container, false);
        initialization();
//        fetchDetailsFromServer();
        return rootView;
    }

    private void initialization()
    {
        email=(TextView)rootView.findViewById(R.id.email);
        phone=(TextView)rootView.findViewById(R.id.phone);
        website=(TextView)rootView.findViewById(R.id.website);
    }






    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    private void fetchDetailsFromServer()
    {
        CentralController.showProgressBar(getActivity());
        ParseQuery<ContactUs> query = ParseQuery.getQuery(ContactUs.class);
// Define our query conditions
        query.whereContains("objectId", "uqvnYI5u9s");
// Execute the find asynchronously
        query.findInBackground(new FetchResponse());
    }





    private class FetchResponse implements FindCallback<ContactUs>
    {
        @Override
        public void done(List<ContactUs> contactUsInfo, ParseException e)
        {
            CentralController.hideProgressBar();
            if (e == null) {
                // Access the array of results here
                ArrayList<ContactUs> contactUsList=(ArrayList<ContactUs>)contactUsInfo;
                if(contactUsList.size()>0)
                {
                    ContactUs contactUs=contactUsList.get(0);
                    email.setText(contactUs.getEmail());
                    phone.setText(contactUs.getPhone());
                    website.setText(contactUs.getWebsite());
//                    return contactUsList.get(0);
                }

            } else {
                Log.d("check", "Exception -->: " + e.getMessage());
            }
        }
    }

}
