package com.example.mobileappdevassignmentone.ui.main;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mobileappdevassignmentone.R;

public class CalculatorFragment extends Fragment {

    public static CalculatorFragment newInstance() {
        return new CalculatorFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        return inflater.inflate(R.layout.calculator_fragment, container, false);
    }

    @Nullable
    @Override
    public void onViewCreated(View mainView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(mainView, savedInstanceState);
        EditText editPrincipal = (EditText) getView().findViewById(R.id.principle);
        EditText editInterest = (EditText) mainView.findViewById(R.id.interest_per_year);
        EditText editAmortization = (EditText) mainView.findViewById(R.id.amortization_period);
        Button doCalculation = (Button) mainView.findViewById(R.id.save_EMI);

        doCalculation.setOnClickListener((view) -> {
            Bundle result = new Bundle();
            if (nullEmpty(editPrincipal.getText().toString()) && nullEmpty(editInterest.getText().toString()) && nullEmpty(editAmortization.getText().toString())) {
                float[] calculationVars = {Float.parseFloat(editPrincipal.getText().toString()),
                        Float.parseFloat(editInterest.getText().toString()),
                        Float.parseFloat(editAmortization.getText().toString())};

                result.putFloatArray("calculationVars", calculationVars);

                getParentFragmentManager().setFragmentResult("calculated", result);

                Fragment calculationFragment = new CalculationFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

                transaction.replace(R.id.main_calculator, calculationFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            } else {
                Toast.makeText(getContext(), "All fields must be filled before proceeding", Toast.LENGTH_LONG).show();
            }
        });
    }

    public boolean nullEmpty(String value) {
        if (value != null && !value.equals("")) {
            return true;
        }
        return false;
    }
}
