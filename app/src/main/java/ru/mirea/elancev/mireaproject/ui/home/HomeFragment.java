package ru.mirea.elancev.mireaproject.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ru.mirea.elancev.mireaproject.CryptFile;
import ru.mirea.elancev.mireaproject.databinding.FragmentHomeBinding;

import javax.crypto.SecretKey;

public class HomeFragment extends Fragment {

    EditText text;
    EditText fileName;
    private FragmentHomeBinding binding;
    private SecretKey secretKey;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        text = binding.editTextTextMultiLine;
        fileName = binding.editFileName;

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        WorkRequest uploadWorkRequest =
                new OneTimeWorkRequest.Builder(HomeWorker.class)
                        .build();
        WorkManager
                .getInstance()
                .enqueue(uploadWorkRequest);
        //Intent serviceIntent = new Intent(HomeFragment.this, HomeService.class);
        //ContextCompat.startForegroundService(HomeFragment.this, serviceIntent);

        FloatingActionButton button = binding.floatingActionButton;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CryptFile dialog = new CryptFile(text, fileName);
                dialog.show(getActivity().getSupportFragmentManager(), "dialog");
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}