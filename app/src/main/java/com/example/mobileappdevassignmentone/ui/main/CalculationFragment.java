package com.example.mobileappdevassignmentone.ui.main;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mobileappdevassignmentone.R;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalculationFragment extends Fragment {

    public static CalculationFragment newInstance() {
        return new CalculationFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View mainView = inflater.inflate(R.layout.calculation_fragment, container, false);

        getParentFragmentManager().setFragmentResultListener("calculated", this, (requestKey, bundle) -> {
            float[] calculationVars = bundle.getFloatArray("calculationVars");

            TextView total_value = (TextView) mainView.findViewById(R.id.total_value);
            TextView principal_value = (TextView) mainView.findViewById(R.id.principle_value);
            TextView interest_value = (TextView) mainView.findViewById(R.id.interest_per_year_value);
            TextView amortization_value = (TextView) mainView.findViewById(R.id.amortization_period_value);

            total_value.setText(String.format("%.2f", calculateEMI(calculationVars)));
            principal_value.setText(Float.toString(calculationVars[0]));
            interest_value.setText(Float.toString(calculationVars[1]));
            amortization_value.setText(Float.toString(calculationVars[2]));
        });
        return mainView;
    }


    @Nullable
    @Override
    public void onViewCreated(View mainView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(mainView, savedInstanceState);
        ImageView btn_back = (ImageView) mainView.findViewById(R.id.btn_back);

        btn_back.setOnClickListener((view) -> {
            Fragment calculatorFragment = new CalculatorFragment();
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

            transaction.replace(R.id.main_calculation, calculatorFragment);
            transaction.disallowAddToBackStack();
            transaction.commit();
        });
    }

    private double calculateEMI(float[] values) {
        float principle = values[0];
        float interestRate = values[1]/100;
        interestRate = interestRate/12;
        float amortization = values[2];

        // calculate numerator
        double result_numerator = interestRate * Math.pow(1 + interestRate, amortization);
        // calculate denominator
        double result_denominator = (Math.pow(1 + interestRate, amortization)) - 1;

        return (principle * (result_numerator / result_denominator));
    }
}