package com.example.abdullahrahmn.redditview;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.abdullahrahmn.redditview.model.Issue;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class IssuesFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private MyIssuesRecyclerViewAdapter mAdapter;
    private List<Issue> issues;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public IssuesFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static IssuesFragment newInstance(String issueId) {
        IssuesFragment fragment = new IssuesFragment();
        Bundle args = new Bundle();
        args.putString("issueId",issueId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String test = this.getArguments().getString("issueId");
        Log.d("issueid", getArguments().toString());
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_issues_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

            requestQueue.add(getIssue(getArguments().getString("issueId")));
            issues = new ArrayList<>();
            mAdapter = new MyIssuesRecyclerViewAdapter(issues, mListener);
            recyclerView.setAdapter(mAdapter);
        }
        return view;
    }




    private StringRequest getIssue(final String issueId){
        String url = "https://shielded-waters-41724.herokuapp.com/api/issues";


            StringRequest issueRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Gson gson =  new Gson();
                        Log.d("Response", response);
                        issues.clear();
                        Issue[] unsortedIssue = gson.fromJson(response, Issue[].class);
                        if(unsortedIssue.length > 0) {
                            for (Issue anUnsortedIssue : unsortedIssue) {
                                if (anUnsortedIssue.projects.equals(issueId)) {
                                    issues.add(anUnsortedIssue);
                                }
                            }
                        }

                        mAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders()
            {
                SharedPreferences sharedPref = getActivity().getPreferences(getActivity().MODE_PRIVATE);
                String token = sharedPref.getString("token", "test");
                Log.d("retrieved token", token);

                Map<String, String>  params = new HashMap<String, String>();
                String authorizationParams = "Token " + token;
                params.put("Authorization", authorizationParams);

                return params;
            }
        };
            return issueRequest;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Issue item);
    }
}
