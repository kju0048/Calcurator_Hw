package cal.calculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SpeedTrans extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;

    TextView tv_Expression;
    TextView tv_Result;
    List<Integer> checkList; // -1: 이콜, 0: 연산자, 1: 숫자, 2: . / 예외 발생을 막는 리스트

    Spinner sp_Exp, sp_Res;

    int exp = 0, res = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speed_trans);

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
        String[] exp_str = getResources().getStringArray(R.array.time_array);
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
        String[] res_str = getResources().getStringArray(R.array.time_array);
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

    // 0.년 1.주 2.일 3.시간 4.분 5.초 6.밀리초 7.마이크로초
    public void changeSpinner(int exp, int res) {
        double time = Double.parseDouble(tv_Expression.getText().toString());
        switch (exp) {
            case 0:
                switch (res) {
                    case 0: // 마이크로초 -> 마이크로초
                        tv_Result.setText(formatResult(time));
                        break;
                    case 1: // 마이크로초 -> 밀리초
                        tv_Result.setText(String.valueOf(formatResult(Math.round(time * 0.001))));
                        break;
                    case 2: // 마이크로초 -> 초
                        tv_Result.setText(String.valueOf(formatResult(Math.round(time * 1.e-6))));
                        break;
                    case 3: // 마이크로초 -> 분
                        tv_Result.setText(String.valueOf(formatResult(Math.round(time * 1.66667e-08))));
                        break;
                    case 4: // 마이크로초 -> 시간
                        tv_Result.setText(String.valueOf(formatResult(Math.round(time * 2.777778e-10))));
                        break;
                    case 5: // 마이크로초 -> 일
                        tv_Result.setText(String.valueOf(formatResult(Math.round(time * 1.15741e-11))));
                        break;
                    case 6: // 마이크로초 -> 주
                        tv_Result.setText(String.valueOf(formatResult(Math.round(time * 1.65344e-12))));
                        break;
                    case 7: // 마이크로초 -> 년
                        tv_Result.setText(String.valueOf(formatResult(Math.round(time * 3.17098e-14))));
                        break;
                }
                break;
            case 1:
                switch (res) {
                    case 0: // 밀리초 -> 마이크로초
                        tv_Result.setText(String.valueOf(formatResult(Math.round(time * 1000))));
                        break;
                    case 1: // 밀리초 -> 밀리초
                        tv_Result.setText(String.valueOf(formatResult(Math.round(time))));
                        break;
                    case 2: // 밀리초 -> 초
                        tv_Result.setText(String.valueOf(formatResult(Math.round(time * 1.e-6))));
                        break;
                    case 3: // 밀리초 -> 분
                        tv_Result.setText(String.valueOf(formatResult(Math.round(time * 1.66667e-08))));
                        break;
                    case 4: // 밀리초 -> 시간
                        tv_Result.setText(String.valueOf(formatResult(Math.round(time * 2.777778e-10))));
                        break;
                    case 5: // 밀리초 -> 일
                        tv_Result.setText(String.valueOf(formatResult(Math.round(time * 1.15741e-11))));
                        break;
                    case 6: // 밀리초 -> 주
                        tv_Result.setText(formatResult(time * 1.65344e-12));
                        break;
                    case 7: // 밀리초 -> 년
                        tv_Result.setText(String.valueOf(formatResult(Math.round(time * 3.17098e-14))));
                        break;
                }
            case 2:
                switch (res) {
                    case 0: // 초 -> 마이크로초
                        tv_Result.setText(String.valueOf(formatResult(Math.round(time * 1000000))));
                        break;
                    case 1: // 초 -> 밀리초
                        tv_Result.setText(String.valueOf(formatResult(Math.round(time * 1000))));
                        break;
                    case 2: // 초 -> 초
                        tv_Result.setText(String.valueOf(formatResult(Math.round(time))));
                        break;
                    case 3: // 초 -> 분
                        tv_Result.setText(String.valueOf(formatResult(Math.round(time * 0.0166666667))));
                        break;
                    case 4: // 초 -> 시간
                        tv_Result.setText(String.valueOf(formatResult(Math.round(time * 2.777778e-4))));
                        break;
                    case 5: // 초 -> 일
                        tv_Result.setText(formatResult(time * 1.15741e-5));
                        break;
                    case 6: // 초 -> 주
                        tv_Result.setText(String.valueOf(formatResult(Math.round(time * 1.6534e-6))));
                        break;
                    case 7: // 초 -> 년
                        tv_Result.setText(String.valueOf(formatResult(Math.round(time * 3.17e-8))));
                        break;
                }
                break;
            case 3:
                switch (res) {
                    case 0: // 분 -> 마이크로초
                        tv_Result.setText(String.valueOf(formatResult(Math.round(time * 60) * 1000000)));
                        break;
                    case 1: // 분 -> 밀리초
                        tv_Result.setText(String.valueOf(formatResult(Math.round(time * 60) * 1000)));
                        break;
                    case 2: // 분 -> 초
                        tv_Result.setText(String.valueOf(formatResult(Math.round(time * 60))));
                        break;
                    case 3: // 분 -> 분
                        tv_Result.setText(String.valueOf(formatResult(Math.round(time))));
                        break;
                    case 4: // 분 -> 시간
                        tv_Result.setText(formatResult(time * 0.0166666667));
                        break;
                    case 5: // 분 -> 일
                        tv_Result.setText(String.valueOf(formatResult(Math.round(time * 6.944444e-4))));
                        break;
                    case 6: // 분 -> 주
                        tv_Result.setText(String.valueOf(formatResult(Math.round(time * 9.92063e-5))));
                        break;
                    case 7: // 분 -> 년
                        tv_Result.setText(String.valueOf(formatResult(Math.round(time * 1.9026e-6))));
                        break;
                }
                break;
            case 4:
                switch (res) {
                    case 0: // 시간 -> 마이크로초
                        tv_Result.setText(String.valueOf(formatResult(Math.round(time * 60 * 60) * 1000000)));
                        break;
                    case 1: // 시간 -> 밀리초
                        tv_Result.setText(String.valueOf(formatResult(Math.round(time * 60 * 60) * 1000)));
                        break;
                    case 2: // 시간 -> 초
                        tv_Result.setText(String.valueOf(formatResult(Math.round(time * 60 * 60))));
                        break;
                    case 3: // 시간 -> 분
                        tv_Result.setText(formatResult(time * 60));
                        break;
                    case 4: // 시간 -> 시간
                        tv_Result.setText(String.valueOf(formatResult(Math.round(time * 60))));
                        break;
                    case 5: // 시간 -> 일
                        tv_Result.setText(String.valueOf(formatResult(Math.round(time * 0.0416667))));
                        break;
                    case 6: // 시간 -> 주
                        tv_Result.setText(String.valueOf(formatResult(Math.round((time * 5.952375e-3)))));
                        break;
                    case 7: // 시간 -> 년
                        tv_Result.setText(String.valueOf(formatResult(Math.round(time * 1.14155e-4))));
                        break;
                }
                break;
            case 5:
                switch (res) {
                    case 0: // 일 -> 마이크로초
                        tv_Result.setText(String.valueOf(formatResult(Math.round(time * 24 * 60 * 60) * 1000000)));
                        break;
                    case 1: // 일 -> 밀리초
                        tv_Result.setText(String.valueOf(formatResult(Math.round(time * 24 * 60 * 60) * 1000)));
                        break;
                    case 2: // 일 -> 초
                        tv_Result.setText(String.valueOf(formatResult(Math.round(time * 24 * 60 * 60))));
                        break;
                    case 3: // 일 -> 분
                        tv_Result.setText(String.valueOf(formatResult(Math.round(time * 24 * 60))));
                        break;
                    case 4: // 일 -> 시간
                        tv_Result.setText(String.valueOf(formatResult(Math.round(time * 24))));
                        break;
                    case 5: // 일 -> 일
                        tv_Result.setText(formatResult(time));
                        break;
                    case 6: // 일 -> 주
                        tv_Result.setText(String.valueOf(formatResult(Math.round(time * 0.142857))));
                        break;
                    case 7: // 일 -> 년
                        tv_Result.setText(String.valueOf(formatResult(Math.round(time * 2.738e-3))));
                        break;
                }
                break;
            case 6:
                switch (res) {
                    case 0: // 주 -> 마이크로초
                        tv_Result.setText(String.valueOf(formatResult(Math.round(time * 7 * 24 * 60 * 60) * 1000000)));
                        break;
                    case 1: // 주 -> 밀리초
                        tv_Result.setText(String.valueOf(formatResult(Math.round(time * 7 * 24 * 60 * 60) * 1000)));
                        break;
                    case 2: // 주 -> 초
                        tv_Result.setText(String.valueOf(formatResult(Math.round(time * 7 * 24 * 60 * 60))));
                        break;
                    case 3: // 주 -> 분
                        tv_Result.setText(String.valueOf(formatResult(Math.round(time * 7 * 24 * 60))));
                        break;
                    case 4: // 주 -> 시간
                        tv_Result.setText(String.valueOf(formatResult(Math.round(time * 7 * 24))));
                        break;
                    case 5: // 주 -> 일
                        tv_Result.setText(String.valueOf(formatResult(Math.round(time * 7.0192))));
                        break;
                    case 6: // 주 -> 주
                        tv_Result.setText(formatResult(time));
                        break;
                    case 7: // 주 -> 년
                        tv_Result.setText(String.valueOf(formatResult(Math.round(time * 0.019165))));
                        break;
                }
                break;
            case 7:
                switch (res) {
                    case 0: // 년 -> 마이크로초
                        tv_Result.setText(String.valueOf(formatResult(Math.round(time * 365 * 24 * 60 * 60) * 1000000)));
                        break;
                    case 1: // 년 -> 밀리초
                        tv_Result.setText(String.valueOf(formatResult(Math.round(time * 365 * 24 * 60 * 60) * 1000)));
                        break;
                    case 2: // 년 -> 초
                        tv_Result.setText(String.valueOf(formatResult(Math.round(time * 365 * 24 * 60 * 60))));
                        break;
                    case 3: // 년 -> 분
                        tv_Result.setText(String.valueOf(formatResult(Math.round(time * 365 * 24 * 60))));
                        break;
                    case 4: // 년 -> 시간
                        tv_Result.setText(String.valueOf(formatResult(Math.round(time * 365 * 24))));
                        break;
                    case 5: // 년 -> 일
                        tv_Result.setText(String.valueOf(formatResult(Math.round(time * 365.25))));
                        break;
                    case 6: // 년 -> 주
                        tv_Result.setText(String.valueOf(formatResult(Math.round(time * 52.17857))));
                        break;
                    case 7: // 년 -> 년
                        tv_Result.setText(String.valueOf(formatResult(Math.round(time))));
                        break;
                }
                break;
        }
    }

    private String formatResult(double time) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(time);
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