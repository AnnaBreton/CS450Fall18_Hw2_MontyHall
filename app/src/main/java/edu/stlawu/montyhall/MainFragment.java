package edu.stlawu.montyhall;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

public class MainFragment extends Fragment {

    public static final String PREF_NAME = "MontyHall";
    public static final String NEW_CLICKED = "NEWCLICKED";

    private OnFragmentInteractionListener FIListener;

    public MainFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        View aboutButton = rootView.findViewById(R.id.about_button);
        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.about_title_text);
                builder.setMessage(R.string.about);
                AlertDialog.Builder builder1 = builder.setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                            }
                        });
                builder.show();
            }
        });

        View newButton = rootView.findViewById(R.id.new_button);
        newButton.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {
                                             SharedPreferences.Editor pref_ed =
                                                     Objects.requireNonNull(getActivity()).getSharedPreferences(
                                                             PREF_NAME, Context.MODE_PRIVATE).edit();
                                             pref_ed.putBoolean(NEW_CLICKED, true).apply();

                                             Intent intent = new Intent(
                                                     getActivity(), GameActivity.class);
                                             getActivity().startActivity(intent);
                                         }
                                     }

        );


        //continue button should launch but set variable to false for newClick so it loads old data
        View continueButton = rootView.findViewById(R.id.continue_button);
        continueButton.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View view) {
                                                  SharedPreferences.Editor pref_ed =
                                                          Objects.requireNonNull(getActivity()).getSharedPreferences(
                                                                  PREF_NAME, Context.MODE_PRIVATE).edit();
                                                  pref_ed.putBoolean(NEW_CLICKED, false).apply();

                                                  Intent intent = new Intent(
                                                          getActivity(), GameActivity.class);
                                                  getActivity().startActivity(intent);
                                              }
                                          }

        );

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            FIListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
    @Override
    public void onDetach() {
        super.onDetach();
        FIListener = null;
    }
}