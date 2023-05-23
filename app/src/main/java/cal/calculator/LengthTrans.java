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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class LengthTrans extends AppCompatActivity {

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
        setContentView(R.layout.activity_length_trans);

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
        String[] exp_str = getResources().getStringArray(R.array.length_array);
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
        String[] res_str = getResources().getStringArray(R.array.length_array);
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

    // 0. 나노미터(nm)  1. 미크론(μm)  2. 밀리미터(mm)  3. 센티미터(cm)  4. 미터(m)  5. 킬로미터(km)
    // 6. 인치(in)  7. 피트(ft)  8. 야드(yd)  9. 마일(mi)  10. 해리(league)
    public void changeSpinner(int exp, int res){
        double length = Double.parseDouble(tv_Expression.getText().toString());
        double result = 0;

        switch(exp){
            case 0: // 나노미터(nm)
                switch(res){
                    case 0: // 나노미터(nm) -> 나노미터(nm)
                        result = length;
                        break;
                    case 1: // 나노미터(nm) -> 미크론(μm)
                        result = length / 1000;
                        break;
                    case 2: // 나노미터(nm) -> 밀리미터(mm)
                        result = length / 1e+6;
                        break;
                    case 3: // 나노미터(nm) -> 센티미터(cm)
                        result = length / 1e+7;
                        break;
                    case 4: // 나노미터(nm) -> 미터(m)
                        result = length / 1e+9;
                        break;
                    case 5: // 나노미터(nm) -> 킬로미터(km)
                        result = length / 1e+12;
                        break;
                    case 6: // 나노미터(nm) -> 인치(in)
                        result = length / 2.54e+7;
                        break;
                    case 7: // 나노미터(nm) -> 피트(ft)
                        result = length / 3.048e+8;
                        break;
                    case 8: // 나노미터(nm) -> 야드(yd)
                        result = length / 9.144e+8;
                        break;
                    case 9: // 나노미터(nm) -> 마일(mi)
                        result = length / 1.609e+12;
                        break;
                    case 10: // 나노미터(nm) -> 해리(league)
                        result = length / 1.852e+12;
                        break;
                }
                break;
            case 1: // 미크론(μm)
                switch(res){
                    case 0: // 미크론(μm) -> 나노미터(nm)
                        result = length * 1000;
                        break;
                    case 1: // 미크론(μm) -> 미크론(μm)
                        result = length;
                        break;
                    case 2: // 미크론(μm) -> 밀리미터(mm)
                        result = length / 1000;
                        break;
                    case 3: // 미크론(μm) -> 센티미터(cm)
                        result = length / 10000;
                        break;
                    case 4: // 미크론(μm) -> 미터(m)
                        result = length / 1e+6;
                        break;
                    case 5: // 미크론(μm) -> 킬로미터(km)
                        result = length / 1e+9;
                        break;
                    case 6: // 미크론(μm) -> 인치(in)
                        result = length / 25400;
                        break;
                    case 7: // 미크론(μm) -> 피트(ft)
                        result = length / 304800;
                        break;
                    case 8: // 미크론(μm) -> 야드(yd)
                        result = length / 914400;
                        break;
                    case 9: // 미크론(μm) -> 마일(mi)
                        result = length / 1.609e+9;
                        break;
                    case 10: // 미크론(μm) -> 해리(league)
                        result = length / 1.852e+9;
                        break;
                }
                break;
            case 2: // 밀리미터(mm)
                switch(res){
                    case 0: // 밀리미터(mm) -> 나노미터(nm)
                        result = length * 1e+6;
                        break;
                    case 1: // 밀리미터(mm) -> 미크론(μm)
                        result = length * 1000;
                        break;
                    case 2: // 밀리미터(mm) -> 밀리미터(mm)
                        result = length;
                        break;
                    case 3: // 밀리미터(mm) -> 센티미터(cm)
                        result = length / 10;
                        break;
                    case 4: // 밀리미터(mm) -> 미터(m)
                        result = length / 1000;
                        break;
                    case 5: // 밀리미터(mm) -> 킬로미터(km)
                        result = length / 1e+6;
                        break;
                    case 6: // 밀리미터(mm) -> 인치(in)
                        result = length / 25.4;
                        break;
                    case 7: // 밀리미터(mm) -> 피트(ft)
                        result = length / 304.8;
                        break;
                    case 8: // 밀리미터(mm) -> 야드(yd)
                        result = length / 914.4;
                        break;
                    case 9: // 밀리미터(mm) -> 마일(mi)
                        result = length / 1.609e+6;
                        break;
                    case 10: // 밀리미터(mm) -> 해리(league)
                        result = length / 1.852e+6;
                        break;
                }
                break;

            case 3: // 센티미터(cm)
                switch (res) {
                    case 0: // 센티미터(cm) -> 나노미터(nm)
                        result = length * 1e+7;
                        break;
                    case 1: // 센티미터(cm) -> 미크론(μm)
                        result = length * 10000;
                        break;
                    case 2: // 센티미터(cm) -> 밀리미터(mm)
                        result = length * 10;
                        break;
                    case 3: // 센티미터(cm) -> 센티미터(cm)
                        result = length;
                        break;
                    case 4: // 센티미터(cm) -> 미터(m)
                        result = length / 100;
                        break;
                    case 5: // 센티미터(cm) -> 킬로미터(km)
                        result = length / 100000;
                        break;
                    case 6: // 센티미터(cm) -> 인치(in)
                        result = length / 2.54;
                        break;
                    case 7: // 센티미터(cm) -> 피트(ft)
                        result = length / 30.48;
                        break;
                    case 8: // 센티미터(cm) -> 야드(yd)
                        result = length / 91.44;
                        break;
                    case 9: // 센티미터(cm) -> 마일(mi)
                        result = length / 160900;
                        break;
                    case 10: // 센티미터(cm) -> 해리(league)
                        result = length / 185200;
                        break;
                }
                break;

            case 4: // 미터(m)
                switch (res) {
                    case 0: // 미터(m) -> 나노미터(nm)
                        result = length * 1e+9;
                        break;
                    case 1: // 미터(m) -> 미크론(μm)
                        result = length * 1e+6;
                        break;
                    case 2: // 미터(m) -> 밀리미터(mm)
                        result = length * 1000;
                        break;
                    case 3: // 미터(m) -> 센티미터(cm)
                        result = length * 100;
                        break;
                    case 4: // 미터(m) -> 미터(m)
                        result = length;
                        break;
                    case 5: // 미터(m) -> 킬로미터(km)
                        result = length / 1000;
                        break;
                    case 6: // 미터(m) -> 인치(in)
                        result = length * 39.37;
                        break;
                    case 7: // 미터(m) -> 피트(ft)
                        result = length * 3.281;
                        break;
                    case 8: // 미터(m) -> 야드(yd)
                        result = length * 1.094;
                        break;
                    case 9: // 미터(m) -> 마일(mi)
                        result = length / 1609;
                        break;
                    case 10: // 미터(m) -> 해리(league)
                        result = length / 1852;
                        break;
                }
                break;

            case 5: // 킬로미터(km)
                switch (res) {
                    case 0: // 킬로미터(km) -> 나노미터(nm)
                        result = length * 1e+12;
                        break;
                    case 1: // 킬로미터(km) -> 미크론(μm)
                        result = length * 1e+9;
                        break;
                    case 2: // 킬로미터(km) -> 밀리미터(mm)
                        result = length * 1e+6;
                        break;
                    case 3: // 킬로미터(km) -> 센티미터(cm)
                        result = length * 100000;
                        break;
                    case 4: // 킬로미터(km) -> 미터(m)
                        result = length * 1000;
                        break;
                    case 5: // 킬로미터(km) -> 킬로미터(km)
                        result = length;
                        break;
                    case 6: // 킬로미터(km) -> 인치(in)
                        result = length * 39370;
                        break;
                    case 7: // 킬로미터(km) -> 피트(ft)
                        result = length * 3281;
                        break;
                    case 8: // 킬로미터(km) -> 야드(yd)
                        result = length * 1094;
                        break;
                    case 9: // 킬로미터(km) -> 마일(mi)
                        result = length / 1.609;
                        break;
                    case 10: // 킬로미터(km) -> 해리(league)
                        result = length / 1.852;
                        break;
                }
                break;

            case 6: // 인치(in)
                switch (res) {
                    case 0: // 인치(in) -> 나노미터(nm)
                        result = length * 2.54e+7;
                        break;
                    case 1: // 인치(in) -> 미크론(μm)
                        result = length * 25400;
                        break;
                    case 2: // 인치(in) -> 밀리미터(mm)
                        result = length * 25.4;
                        break;
                    case 3: // 인치(in) -> 센티미터(cm)
                        result = length * 2.54;
                        break;
                    case 4: // 인치(in) -> 미터(m)
                        result = length / 39.37;
                        break;
                    case 5: // 인치(in) -> 킬로미터(km)
                        result = length / 39370;
                        break;
                    case 6: // 인치(in) -> 인치(in)
                        result = length;
                        break;
                    case 7: // 인치(in) -> 피트(ft)
                        result = length / 12;
                        break;
                    case 8: // 인치(in) -> 야드(yd)
                        result = length / 36;
                        break;
                    case 9: // 인치(in) -> 마일(mi)
                        result = length / 63360;
                        break;
                    case 10: // 인치(in) -> 해리(league)
                        result = length / 72910;
                        break;
                }
                break;

            case 7: // 피트(ft)
                switch (res) {
                    case 0: // 피트(ft) -> 나노미터(nm)
                        result = length * 3.048e+8;
                        break;
                    case 1: // 피트(ft) -> 미크론(μm)
                        result = length * 304800;
                        break;
                    case 2: // 피트(ft) -> 밀리미터(mm)
                        result = length * 304.8;
                        break;
                    case 3: // 피트(ft) -> 센티미터(cm)
                        result = length * 30.48;
                        break;
                    case 4: // 피트(ft) -> 미터(m)
                        result = length / 3.281;
                        break;
                    case 5: // 피트(ft) -> 킬로미터(km)
                        result = length / 3281;
                        break;
                    case 6: // 피트(ft) -> 인치(in)
                        result = length * 12;
                        break;
                    case 7: // 피트(ft) -> 피트(ft)
                        result = length;
                        break;
                    case 8: // 피트(ft) -> 야드(yd)
                        result = length / 3;
                        break;
                    case 9: // 피트(ft) -> 마일(mi)
                        result = length / 5280;
                        break;
                    case 10: // 피트(ft) -> 해리(league)
                        result = length / 6076;
                        break;
                }
                break;

            case 8: // 야드(yd)
                switch (res) {
                    case 0: // 야드(yd) -> 나노미터(nm)
                        result = length * 9.144e+8;
                        break;
                    case 1: // 야드(yd) -> 미크론(μm)
                        result = length * 914400;
                        break;
                    case 2: // 야드(yd) -> 밀리미터(mm)
                        result = length * 914.4;
                        break;
                    case 3: // 야드(yd) -> 센티미터(cm)
                        result = length * 91.44;
                        break;
                    case 4: // 야드(yd) -> 미터(m)
                        result = length / 1.094;
                        break;
                    case 5: // 야드(yd) -> 킬로미터(km)
                        result = length / 1094;
                        break;
                    case 6: // 야드(yd) -> 인치(in)
                        result = length * 36;
                        break;
                    case 7: // 야드(yd) -> 피트(ft)
                        result = length * 3;
                        break;
                    case 8: // 야드(yd) -> 야드(yd)
                        result = length;
                        break;
                    case 9: // 야드(yd) -> 마일(mi)
                        result = length / 1760;
                        break;
                    case 10: // 야드(yd) -> 해리(league)
                        result = length / 2025;
                        break;
                }
                break;

            case 9: // 마일(mi)
                switch (res) {
                    case 0: // 마일(mi) -> 나노미터(nm)
                        result = length * 1.609e+12;
                        break;
                    case 1: // 마일(mi) -> 미크론(μm)
                        result = length * 1.609e+9;
                        break;
                    case 2: // 마일(mi) -> 밀리미터(mm)
                        result = length * 1.609e+6;
                        break;
                    case 3: // 마일(mi) -> 센티미터(cm)
                        result = length * 160900;
                        break;
                    case 4: // 마일(mi) -> 미터(m)
                        result = length * 1609;
                        break;
                    case 5: // 마일(mi) -> 킬로미터(km)
                        result = length * 1.609;
                        break;
                    case 6: // 마일(mi) -> 인치(in)
                        result = length * 63360;
                        break;
                    case 7: // 마일(mi) -> 피트(ft)
                        result = length * 5280;
                        break;
                    case 8: // 마일(mi) -> 야드(yd)
                        result = length * 1760;
                        break;
                    case 9: // 마일(mi) -> 마일(mi)
                        result = length;
                        break;
                    case 10: // 마일(mi) -> 해리(league)
                        result = length / 1.151;
                        break;
                }
                break;

            case 10: // 해리(league)
                switch (res) {
                    case 0: // 해리(league) -> 나노미터(nm)
                        result = length * 1.852e+12;
                        break;
                    case 1: // 해리(league) -> 미크론(μm)
                        result = length * 1.852e+9;
                        break;
                    case 2: // 해리(league) -> 밀리미터(mm)
                        result = length * 1.852e+6;
                        break;
                    case 3: // 해리(league) -> 센티미터(cm)
                        result = length * 185200;
                        break;
                    case 4: // 해리(league) -> 미터(m)
                        result = length * 1852;
                        break;
                    case 5: // 해리(league) -> 킬로미터(km)
                        result = length * 1.852;
                        break;
                    case 6: // 해리(league) -> 인치(in)
                        result = length * 72910;
                        break;
                    case 7: // 해리(league) -> 피트(ft)
                        result = length * 6076;
                        break;
                    case 8: // 해리(league) -> 야드(yd)
                        result = length * 2025;
                        break;
                    case 9: // 해리(league) -> 마일(mi)
                        result = length * 1.151;
                        break;
                    case 10: // 해리(league) -> 해리(league)
                        result = length;
                        break;
                }
                break;

        }
        result = Math.round(result * 10000000 ) / 10000000.0;
        tv_Result.setText(String.valueOf(result));
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