package com.desafiolatam.desafioface.views.main;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.desafiolatam.desafioface.R;
import com.desafiolatam.desafioface.network.GetUsers;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Pedro on 20-07-2017.
 */

public class FinderDialogFragment extends DialogFragment {

    private FinderCallback callback;
    private String name;

    public static FinderDialogFragment newInstance() {
        return new FinderDialogFragment();
    }

    private class SearchUsers extends GetUsers{


        public SearchUsers(int additionalPages, Context context) {
            super(additionalPages, context);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            callback.queryDone(name);
            dismiss();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_fragment_finder, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final EditText searchEt = (EditText) view.findViewById(R.id.searchEt);
        final ImageButton searchButton = (ImageButton) view.findViewById(R.id.searchBtn);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = searchEt.getText().toString();

                if (name.trim().length() > 0)
                {
                    setCancelable(false);
                    /*Map<String, String> queryParams = new HashMap<>();
                        queryParams.put("page", String.valueOf(total/10));

                        new ScrollRequest(3).execute(queryParams);*/

                    Map<String, String> queryParams = new HashMap<>();
                    queryParams.put("page", "1");
                    queryParams.put("name", name);
                    searchEt.setVisibility(View.INVISIBLE);
                    searchButton.setVisibility(View.INVISIBLE);

                    new SearchUsers(-1, getContext()).execute(queryParams);
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
        );
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        callback = (FinderCallback) context;

    }
}
