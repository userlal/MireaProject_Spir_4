package ru.mirea.elancev.mireaproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import ru.mirea.elancev.mireaproject.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FragmentProfileBinding binding;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    private EditText editTextName;
    private EditText editTextSurname;
    private EditText editTextPhone;
    private EditText editTextEmail;

    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        sharedPref = this.getActivity().getSharedPreferences("mirea_settings", Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        String name = sharedPref.getString("NAME", "unknown");
        String surname = sharedPref.getString("SURNAME", "unknown");
        String phone = sharedPref.getString("PHONE", "unknown");
        String email = sharedPref.getString("EMAIL", "unknown");
        binding.editTextName.setText(name);
        binding.editTextSurname.setText(surname);
        binding.editTextPhone.setText(phone);
        binding.editTextEmail.setText(email);

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("NAME", binding.editTextName.getText().toString());
                editor.putString("SURNAME", binding.editTextSurname.getText().toString());
                editor.putString("PHONE", binding.editTextPhone.getText().toString());
                editor.putString("EMAIL", binding.editTextEmail.getText().toString());
                editor.apply();
            }
        });

        return root;
    }

}