package com.example.helper.ui.currency;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.helper.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CurrencyConverterFragment extends Fragment implements View.OnClickListener {
    private TextView currency_usd, currency_eur, currency_rub, dateOfUpdate, timeOfOpenedConverter;

    private JsonPlaceHolderApi jsonPlaceHolderApi;

    private ArrayList<Post> currency = new ArrayList<>();
    private ArrayList<RadioButton> radio = new ArrayList<>();
    private ArrayList<EditText> number = new ArrayList<>();

    private EditText converter_byn, converter_rub, converter_eur, converter_usd;

    private RadioButton converter_rBYN, converter_rRUB, converter_rEUR, converter_rUSD;

    private double eur, usd, rub = 0;
    private Boolean isCalc = true;
    private ImageButton calendar;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_currency_converter, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dateOfUpdate = view.findViewById(R.id.currency_dateOfUpdate);
        currency_eur = view.findViewById(R.id.currency_eur);
        currency_usd = view.findViewById(R.id.currency_usd);
        currency_rub = view.findViewById(R.id.currency_rub);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.nbrb.by/api/exrates/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        getPosts();

        converter_byn = view.findViewById(R.id.converter_byn);
        converter_byn.setOnClickListener(this);
        converter_rub = view.findViewById(R.id.converter_rub);
        converter_rub.setOnClickListener(this);
        converter_eur = view.findViewById(R.id.converter_eur);
        converter_eur.setOnClickListener(this);
        converter_usd = view.findViewById(R.id.converter_usd);
        converter_usd.setOnClickListener(this);

        timeOfOpenedConverter = view.findViewById(R.id.time_opened_converter);

        calendar = view.findViewById(R.id.calendar);
        calendar.setOnClickListener(this);


        converter_rBYN = view.findViewById(R.id.converter_rBYN);
        converter_rBYN.setOnClickListener(this);
        converter_rRUB = view.findViewById(R.id.converter_rRUB);
        converter_rRUB.setOnClickListener(this);
        converter_rEUR = view.findViewById(R.id.converter_rEUR);
        converter_rEUR.setOnClickListener(this);
        converter_rUSD = view.findViewById(R.id.converter_rUSD);
        converter_rUSD.setOnClickListener(this);
        fillListRadioButton();
        getCurrentTime();
        checkRadioButton(converter_rBYN);



        converter_byn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    if (isCalc){
                        calculateBYN(Double.parseDouble(converter_byn.getText().toString()));
                    }


                }catch (Exception e){

                }

            }
        });

        converter_rub.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    if (isCalc){
                        calculateRUB(Double.parseDouble(converter_rub.getText().toString()));
                    }


                }catch (Exception e){

                }

            }
        });

        converter_eur.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    if(isCalc){
                        calculateEUR(Double.parseDouble(converter_eur.getText().toString()));
                    }


                }catch (Exception e){

                }

            }
        });

        converter_usd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    if(isCalc){
                        calculateUSD(Double.parseDouble(converter_usd.getText().toString()));
                    }


                }catch (Exception e){

                }

            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.converter_rBYN:
                checkRadioButton(converter_rBYN);
                break;
            case R.id.converter_rRUB:
                checkRadioButton(converter_rRUB);
                break;
            case R.id.converter_rEUR:
                checkRadioButton(converter_rEUR);
                break;
            case R.id.converter_rUSD:
                checkRadioButton(converter_rUSD);
                break;

            case R.id.calendar:
                callDatePicker();
                break;

        }
    }

    private void fillListRadioButton() {
        radio.add(converter_rBYN);
        radio.add(converter_rRUB);
        radio.add(converter_rEUR);
        radio.add(converter_rUSD);

        number.add(converter_byn);
        number.add(converter_rub);
        number.add(converter_eur);
        number.add(converter_usd);
    }

    private void checkRadioButton(RadioButton view) {
        isCalc = false;
        for (int i = 0; i < radio.size(); i++) {
            if (radio.get(i).equals(view)) {
                radio.get(i).setChecked(true);

                number.get(i).setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                number.get(i).setTextIsSelectable(true);
            } else if (!radio.get(i).equals(view)) {
                radio.get(i).setChecked(false);

                number.get(i).setInputType(InputType.TYPE_NULL);
                number.get(i).setTextIsSelectable(false);
            }
        }
        isCalc = true;
    }



    private void getPosts() {
        Call<List<Post>> call = jsonPlaceHolderApi.getPosts();

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getContext(), "Code: " + response.code(), Toast.LENGTH_SHORT).show();
                }
                String date = "";
                currency = (ArrayList<Post>) response.body();
                for (Post post : currency) {
                    if (post.getCur_abbreviation().equals("EUR")) {
                        date = post.getDate();
                        double price = post.getCur_officialRate();
                        currency_eur.setText(String.valueOf(price));
                    } else if (post.getCur_abbreviation().equals("USD")) {
                        double price = post.getCur_officialRate();
                        currency_usd.setText(String.valueOf(price));
                    } else if (post.getCur_abbreviation().equals("RUB")) {
                        double price = post.getCur_officialRate();
                        currency_rub.setText(String.valueOf(price));
                    }
                }

                dateOfUpdate.setText(date.substring(0, 10));
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getCurrentTime (){
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String dateText = "Время открытия: " + dateFormat.format(date);
        timeOfOpenedConverter.setText(dateText);
    }

    private void getValueCurrency(){
        eur = Double.parseDouble(currency_eur.getText().toString());
        usd = Double.parseDouble(currency_usd.getText().toString());
        rub = Double.parseDouble(currency_rub.getText().toString());
    }

    private void calculateBYN (double valueOfField){
        if (!converter_rBYN.isChecked()){
            return;
        }
        getValueCurrency();
        converter_rub.setText(String.format("%.3f",valueOfField * 100 / rub));
        converter_eur.setText(String.format("%.3f",valueOfField  / eur));
        converter_usd.setText(String.format("%.3f",valueOfField  / usd));

    }

    private void calculateRUB (double valueOfField){
        if (!converter_rRUB.isChecked()){
            return;
        }
        getValueCurrency();

        double bynFromRub = valueOfField * rub / 100;
        converter_byn.setText(String.format("%.3f",bynFromRub));
        converter_eur.setText(String.format("%.3f",bynFromRub  / eur));
        converter_usd.setText(String.format("%.3f",bynFromRub  / usd));

    }

    private void calculateEUR (double valueOfField){
        if (!converter_rEUR.isChecked()){
            return;
        }
        getValueCurrency();

        double bynFromEUR = valueOfField * eur ;
        converter_byn.setText(String.format("%.3f",bynFromEUR));
        converter_rub.setText(String.format("%.3f",bynFromEUR * 100  / rub));
        converter_usd.setText(String.format("%.3f",bynFromEUR  / usd));

    }

    private void calculateUSD (double valueOfField){
        if (!converter_rUSD.isChecked()){
            return;
        }
        getValueCurrency();
        double bynFromUSD = valueOfField * usd ;
        converter_byn.setText(String.format("%.3f",bynFromUSD));
        converter_rub.setText(String.format("%.3f",bynFromUSD * 100  / rub));
        converter_eur.setText(String.format("%.3f",bynFromUSD  / eur));

    }

    private void callDatePicker() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), R.style.DialogTheme,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        String date = year + "-" + (monthOfYear+1)+"-"+ dayOfMonth;
                        try {
                            getDatePosts(date);
                        }catch (Exception e){
                        }
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void getDatePosts(String date) {
        Call<List<Post>> call = jsonPlaceHolderApi.getDatePosts(date);

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getContext(), "Code: " + response.code(), Toast.LENGTH_SHORT).show();
                }


                String date = "";
                currency = (ArrayList<Post>) response.body();
                for (Post post : currency) {
                    if (post.getCur_abbreviation().equals("EUR")) {
                        date = post.getDate();
                        double price = post.getCur_officialRate();
                        currency_eur.setText(String.valueOf(price));
                    } else if (post.getCur_abbreviation().equals("USD")) {
                        double price = post.getCur_officialRate();
                        currency_usd.setText(String.valueOf(price));
                    } else if (post.getCur_abbreviation().equals("RUB")) {
                        double price = post.getCur_officialRate();
                        currency_rub.setText(String.valueOf(price));
                    }
                }
                try {
                    dateOfUpdate.setText(date.substring(0, 10));
                }catch (Exception e){
                    Toast.makeText(getContext(), "Не правильно выбрана дата!", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
