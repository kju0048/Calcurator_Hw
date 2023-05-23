package cal.calculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class ExchangeTrans extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;

    TextView tv_Expression; // 입력 값
    TextView tv_Result; // 결과 값
    List<Integer> checkList; // -1: 이콜, 0: 연산자, 1: 숫자, 2: . / 예외 발생을 막는 리스트

    Spinner sp_Exp, sp_Res;

    int exp = 0, res = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_trans);

        this.init();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawerLayout = findViewById(R.id.layout_drawer);
        navigationView = findViewById(R.id.nav);
        navigationView.setItemIconTintList(null);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerToggle.syncState();
        drawerLayout.addDrawerListener(drawerToggle);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                String title = item.getTitle().toString();

                if(id == R.id.menu_defCal){
                    Intent in = new Intent(getApplicationContext(), DefaultCal.class);
                    startActivity(in);
                    finish();
                }

                if(id == R.id.menu_proCal){
                    Intent in = new Intent(getApplicationContext(), ProgrammerCal.class);
                    startActivity(in);
                    finish();
                }

                if(id == R.id.menu_tempTrans){
                    Intent in = new Intent(getApplicationContext(), TempTrans.class);
                    startActivity(in);
                    finish();
                }

                if(id == R.id.menu_weightTrans){
                    Intent in = new Intent(getApplicationContext(), WeightTrans.class);
                    startActivity(in);
                    finish();
                }

                if(id == R.id.menu_areaTrans){
                    Intent in = new Intent(getApplicationContext(), AreaTrans.class);
                    startActivity(in);
                    finish();
                }

                if(id == R.id.menu_timeTrans){
                    Intent in = new Intent(getApplicationContext(), TimeTrans.class);
                    startActivity(in);
                    finish();
                }

                if(id == R.id.menu_AngleTrans){
                    Intent in = new Intent(getApplicationContext(), AngleTrans.class);
                    startActivity(in);
                    finish();
                }

                if(id == R.id.menu_DataTrans){
                    Intent in = new Intent(getApplicationContext(), DataTrans.class);
                    startActivity(in);
                    finish();
                }

                if(id == R.id.menu_ExchangeTrans){
                    Intent in = new Intent(getApplicationContext(), ExchangeTrans.class);
                    startActivity(in);
                    finish();
                }

                if(id == R.id.menu_SpeedTrans){
                    Intent in = new Intent(getApplicationContext(), SpeedTrans.class);
                    startActivity(in);
                    finish();
                }

                if(id == R.id.menu_lengTrans){
                    Intent in = new Intent(getApplicationContext(), LengthTrans.class);
                    startActivity(in);
                    finish();
                }
                return true;
            }
        });

        // 입력값 스피너 구현
        sp_Exp = (Spinner) findViewById(R.id.exp_select);
        String[] exp_str = getResources().getStringArray(R.array.exchange_array);
        ArrayAdapter<String> exp_adapter = new ArrayAdapter<String>(this, R.layout.spinner_dropdown_item, exp_str);
        exp_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_Exp.setAdapter(exp_adapter);

        sp_Exp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                exp = position;
                changeSpinner(exp, res);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // 출력값 스피너 구현
        sp_Res = (Spinner) findViewById(R.id.res_select);
        String[] res_str = getResources().getStringArray(R.array.exchange_array);
        ArrayAdapter<String> res_adapter = new ArrayAdapter<String>(this, R.layout.spinner_dropdown_item, res_str);
        res_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_Res.setAdapter(res_adapter);

        sp_Res.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                res = position;
                changeSpinner(exp, res);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void negaClick (View v){
        if (tv_Expression.length() == 0 || tv_Expression.getText().toString().equals("0")) {
            Toast.makeText(getApplicationContext(), "마지막 입력값이 숫자여야 사용가능합니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] ex = tv_Expression.getText().toString().split("");

        List<String> li = new ArrayList<String>();
        Collections.addAll(li, ex);

        if(!isNumber(ex[0])){
            li.remove(0);
            tv_Expression.setText(TextUtils.join("", li));
        }
        else {
            tv_Expression.setText("-" + TextUtils.join("", li));
        }
        changeSpinner(exp, res);
    }

    // 0. 원(한국)  1. 달러(미국)  2. 유로(유럽)  3. 옌(일본)  4. 루블(러시아)  5. 위안(중국)
    public void changeSpinner(int exp, int res) {
        double amount = Double.parseDouble(tv_Expression.getText().toString());
        double result = 0.0;

        String apiKey = getResources().getString(R.string.exchange_rate_api_key);
        String baseUrl = "https://v6.exchangerate-api.com/v6/988834e83c3aa05762d89b5c/latest/";

        String targetCurrency = ""; // Initialize targetCurrency variable

        switch (exp) {
            case 0: // 한국 원화 (KRW)
                baseUrl += "KRW";
                switch (res) {
                    case 0: // 한국 원화 -> 한국 원화
                        result = amount;
                        break;
                    case 1: // 한국 원화 -> 달러(미국)
                        targetCurrency = "USD";
                        break;
                    case 2: // 한국 원화 -> 유로(유럽)
                        targetCurrency = "EUR";
                        break;
                    case 3: // 한국 원화 -> 옌(일본)
                        targetCurrency = "JPY";
                        break;
                    case 4: // 한국 원화 -> 루블(러시아)
                        targetCurrency = "RUB";
                        break;
                    case 5: // 한국 원화 -> 위안(중국)
                        targetCurrency = "CNY";
                        break;
                }
                break;
            case 1: // 달러(미국)
                baseUrl += "USD";
                switch (res) {
                    case 0: // 달러(미국) -> 원화(한국)
                        targetCurrency = "KRW";
                        break;
                    case 1: // 달러(미국) -> 달러(미국)
                        result = amount;
                        break;
                    case 2: // 달러(미국) -> 유로(유럽)
                        targetCurrency = "EUR";
                        break;
                    case 3: // 달러(미국) -> 옌(일본)
                        targetCurrency = "JPY";
                        break;
                    case 4: // 달러(미국) -> 루블(러시아)
                        targetCurrency = "RUB";
                        break;
                    case 5: // 달러(미국) -> 위안(중국)
                        targetCurrency = "CNY";
                        break;
                }
                break;
            case 2: // 유로(유럽)
                baseUrl += "EUR";
                switch (res) {
                    case 0: // 유로(유럽) -> 원화(한국)
                        targetCurrency = "KRW";
                        break;
                    case 1: // 유로(유럽) -> 달러(미국)
                        targetCurrency = "USD";
                        break;
                    case 2: // 유로(유럽) -> 유로(유럽)
                        result = amount;
                        break;
                    case 3: // 유로(유럽) -> 옌(일본)
                        targetCurrency = "JPY";
                        break;
                    case 4: // 유로(유럽) -> 루블(러시아)
                        targetCurrency = "RUB";
                        break;
                    case 5: // 유로(유럽) -> 위안(중국)
                        targetCurrency = "CNY";
                        break;
                }
                break;
            case 3: // 옌(일본)
                baseUrl += "JPY";
                switch (res) {
                    case 0: // 옌(일본) -> 원화(한국)
                        targetCurrency = "KRW";
                        break;
                    case 1: // 옌(일본) -> 달러(미국)
                        targetCurrency = "USD";
                        break;
                    case 2: // 옌(일본) -> 유로(유럽)
                        targetCurrency = "EUR";
                        break;
                    case 3: // 옌(일본) -> 옌(일본)
                        result = amount;
                        break;
                    case 4: // 옌(일본) -> 루블(러시아)
                        targetCurrency = "RUB";
                        break;
                    case 5: // 옌(일본) -> 위안(중국)
                        targetCurrency = "CNY";
                        break;
                }
                break;
            case 4: // 루블(러시아)
                baseUrl += "RUB";
                switch (res) {
                    case 0: // 루블(러시아) -> 원화(한국)
                        targetCurrency = "KRW";
                        break;
                    case 1: // 루블(러시아) -> 달러(미국)
                        targetCurrency = "USD";
                        break;
                    case 2: // 루블(러시아) -> 유로(유럽)
                        targetCurrency = "EUR";
                        break;
                    case 3: // 루블(러시아) -> 옌(일본)
                        targetCurrency = "JPY";
                        break;
                    case 4: // 루블(러시아) -> 루블(러시아)
                        result = amount;
                        break;
                    case 5: // 루블(러시아) -> 위안(중국)
                        targetCurrency = "CNY";
                        break;
                }
                break;
            case 5: // 위안(중국)
                baseUrl += "CNY";
                switch (res) {
                    case 0: // 위안(중국) -> 원화(한국)
                        targetCurrency = "KRW";
                        break;
                    case 1: // 위안(중국) -> 달러(미국)
                        targetCurrency = "USD";
                        break;
                    case 2: // 위안(중국) -> 유로(유럽)
                        targetCurrency = "EUR";
                        break;
                    case 3: // 위안(중국) -> 옌(일본)
                        targetCurrency = "JPY";
                        break;
                    case 4: // 위안(중국) -> 루블(러시아)
                        targetCurrency = "RUB";
                        break;
                    case 5: // 위안(중국) -> 위안(중국)
                        result = amount;
                        break;
                }
                break;
        }

        if (!targetCurrency.isEmpty()) {
            final String finalTargetCurrency = targetCurrency;
            final double finalAmount = amount;

            String url = baseUrl + "?apiKey=" + apiKey;
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONObject conversionRates = response.getJSONObject("conversion_rates");
                                if (conversionRates.has(finalTargetCurrency)) {
                                    double exchangeRate = conversionRates.getDouble(finalTargetCurrency);
                                    double finalResult = finalAmount * exchangeRate;
                                    tv_Result.setText(String.valueOf(finalResult));
                                } else {
                                    // 대상 통화에 대한 환율 정보가 없을 경우 에러 처리
                                    tv_Result.setText("환율 정보를 찾을 수 없습니다.");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    });

            // Add the request to the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(jsonObjectRequest);
        } else {
            tv_Result.setText(String.valueOf(result));
        }
    }



    void init() {
        tv_Expression = findViewById(R.id.tv_expression);
        tv_Result = findViewById(R.id.tv_result);
        checkList = new ArrayList<>();
        checkList.add(1);
    }

    public void btClick(View v) {
        int gId = v.getId();


        if (gId == R.id.bt_1) addNumber("1");
        else if (gId == R.id.bt_2) addNumber("2");
        else if (gId == R.id.bt_3) addNumber("3");
        else if (gId == R.id.bt_4) addNumber("4");
        else if (gId == R.id.bt_5) addNumber("5");
        else if (gId == R.id.bt_6) addNumber("6");
        else if (gId == R.id.bt_7) addNumber("7");
        else if (gId == R.id.bt_8) addNumber("8");
        else if (gId == R.id.bt_9) addNumber("9");
        else if (gId == R.id.bt_0) addNumber("0");
        else if (gId == R.id.bt_point) addPoint(".");
    }

    public void clearClick (View v){
        checkList.clear();
        checkList.add(1);
        tv_Expression.setText("0");
        tv_Result.setText("");
        changeSpinner(exp, res);
    }

    public void deleteClick (View v){
        if (tv_Expression.length() != 0) {
            if(checkList.isEmpty())
                return;
            checkList.remove(checkList.size() - 1);
            String[] ex = tv_Expression.getText().toString().split("");
            List<String> li = new ArrayList<String>();
            Collections.addAll(li, ex);
            li.remove(li.size() - 1);

            if (li.size() > 0 && !isNumber(li.get(li.size() - 1)))
                li.remove(li.size() - 1);
            tv_Expression.setText(TextUtils.join("", li));
        }

        if (tv_Expression.length() == 0){
            tv_Expression.setText("0");
            checkList.clear();
            checkList.add(1);
        }
        changeSpinner(exp, res);
    }

    // 숫자 버튼
    void addNumber (String str){
        if(tv_Expression.getText().toString().equals("0")){
            tv_Expression.setText("");
            checkList.clear();
        }
        checkList.add(1); // 숫자가 들어왔는지 체크리스트에 표시
        tv_Expression.append(str); // UI
        changeSpinner(exp, res);
    }

    void addPoint (String str){
        if (checkList.isEmpty()) {
            Toast.makeText(getApplicationContext(), ". 을 사용할 수 없습니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        // 하나의 수에 . 이 여러 개 오는 것을 막기
        for (int i = checkList.size() - 2; i >= 0; i--) {
            int check = checkList.get(i);
            if (check == 2) {
                Toast.makeText(getApplicationContext(), ". 을 사용할 수 없습니다.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (check == 0) break;
            if (check == 1) continue;
        }
        checkList.add(2);
        tv_Expression.append(str); // UI
    }

    boolean isNumber (String str){
        boolean result = true;
        try {
            Double.parseDouble(str);
        } catch (NumberFormatException e) {
            result = false;
        }
        return result;
    }
}