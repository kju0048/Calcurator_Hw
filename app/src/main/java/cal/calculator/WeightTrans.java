package cal.calculator;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WeightTrans extends AppCompatActivity {

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
        setContentView(R.layout.activity_weight_trans);

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
        String[] exp_str = getResources().getStringArray(R.array.weight_array);
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
        String[] res_str = getResources().getStringArray(R.array.weight_array);
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

    // 0. 섭씨  1. 화씨  2. 절대 온도
    public void changeSpinner(int exp, int res){
        double weight = Double.parseDouble(tv_Expression.getText().toString());
        switch(exp){
            case 0:
                switch(res){
                    case 0: // 캐럿 -> 캐럿
                        tv_Result.setText(weight + "");
                        break;
                    case 1: // 캐럿 -> 밀리그램
                        tv_Result.setText(String.valueOf(Math.round((weight * 200) * 1000000000) / 1000000000.0));
                        break;
                    case 2: // 캐럿 -> 그램
                        tv_Result.setText(String.valueOf(Math.round((weight * 0.2) * 1000000000) / 1000000000.0));
                        break;
                    case 3: // 캐럿 -> 킬로그램
                        tv_Result.setText(String.valueOf(Math.round((weight * 0.0002) * 1000000000) / 1000000000.0));
                        break;
                    case 4: // 캐럿 -> 파운드
                        tv_Result.setText(String.valueOf(Math.round((weight * 0.000440925) * 1000000000) / 1000000000.0));
                        break;
                    case 5: // 캐럿 -> 톤
                        tv_Result.setText(String.valueOf(Math.round((weight * 2 * 0.0000001) * 1000000000) / 1000000000.0));
                        break;
                }
                break;
            case 1:
                switch(res){
                    case 0: // 밀리그램 -> 캐럿
                        tv_Result.setText(String.valueOf(Math.round((weight * 0.005) * 1000000000) / 1000000000.0));
                        break;
                    case 1: // 밀리그램 -> 밀리그램
                        tv_Result.setText(weight + "");
                        break;
                    case 2: // 밀리그램 -> 그램
                        tv_Result.setText(String.valueOf(Math.round((weight * 0.001) * 1000000000) / 1000000000.0));
                        break;
                    case 3: // 밀리그램 -> 킬로그램
                        tv_Result.setText(String.valueOf(Math.round((weight * 0.000001) * 1000000000) / 1000000000.0));
                        break;
                    case 4: // 밀리그램 -> 파운드
                        tv_Result.setText(String.valueOf(Math.round((weight * 0.00000220462) * 1000000000) / 1000000000.0));
                        break;
                    case 5: // 밀리그램 -> 톤
                        tv_Result.setText(String.valueOf(Math.round((weight * 0.000000001) * 1000000000) / 1000000000.0));
                        break;
                }
                break;
            case 2:
                switch(res){
                    case 0: // 그램 -> 캐럿
                        tv_Result.setText(String.valueOf(Math.round((weight * 5) * 1000000000) / 1000000000.0));
                        break;
                    case 1: // 그램 -> 밀리그램
                        tv_Result.setText(String.valueOf(Math.round((weight * 1000) * 1000000000) / 1000000000.0));
                        break;
                    case 2: // 그램 -> 그램
                        tv_Result.setText(weight + "");
                        break;
                    case 3: // 그램 -> 킬로그램
                        tv_Result.setText(String.valueOf(Math.round((weight * 0.001) * 1000000000) / 1000000000.0));
                        break;
                    case 4: // 그램 -> 파운드
                        tv_Result.setText(String.valueOf(Math.round((weight * 0.00220462) * 1000000000) / 1000000000.0));
                        break;
                    case 5: // 그램 -> 톤
                        tv_Result.setText(String.valueOf(Math.round((weight * 0.000001) * 1000000000) / 1000000000.0));
                        break;
                }
                break;
            case 3:
                switch(res){
                    case 0: // 킬로그램 -> 캐럿
                        tv_Result.setText(String.valueOf(Math.round((weight * 5000) * 1000000000) / 1000000000.0));
                        break;
                    case 1: // 킬로그램 -> 밀리그램
                        tv_Result.setText(String.valueOf(Math.round((weight * 1000000) * 1000000000) / 1000000000.0));
                        break;
                    case 2: // 킬로그램 -> 그램
                        tv_Result.setText(String.valueOf(Math.round((weight * 1000) * 1000000000) / 1000000000.0));
                        break;
                    case 3: // 킬로그램 -> 킬로그램
                        tv_Result.setText(weight + "");
                        break;
                    case 4: // 킬로그램 -> 파운드
                        tv_Result.setText(String.valueOf(Math.round((weight * 2.20462) * 1000000000) / 1000000000.0));
                        break;
                    case 5: // 킬로그램 -> 톤
                        tv_Result.setText(String.valueOf(Math.round((weight * 0.001) * 1000000000) / 1000000000.0));
                        break;
                }
                break;
            case 4:
                switch(res){
                    case 0: // 파운드 -> 캐럿
                        tv_Result.setText(String.valueOf(Math.round((weight * 2267.96) * 1000000000) / 1000000000.0));
                        break;
                    case 1: // 파운드 -> 밀리그램
                        tv_Result.setText(String.valueOf(Math.round((weight * 453592) * 1000000000) / 1000000000.0));
                        break;
                    case 2: // 파운드 -> 그램
                        tv_Result.setText(String.valueOf(Math.round((weight * 453.592) * 1000000000) / 1000000000.0));
                        break;
                    case 3: // 파운드 -> 킬로그램
                        tv_Result.setText(String.valueOf(Math.round((weight * 0.453592) * 1000000000) / 1000000000.0));
                        break;
                    case 4: // 파운드 -> 파운드
                        tv_Result.setText(weight + "");
                        break;
                    case 5: // 파운드 -> 톤
                        tv_Result.setText(String.valueOf(Math.round((weight * 0.00045359) * 1000000000) / 1000000000.0));
                        break;
                }
                break;
            case 5:
                switch(res){
                    case 0: // 톤 -> 캐럿
                        tv_Result.setText(String.valueOf(Math.round((weight * 5000000) * 1000000000) / 1000000000.0));
                        break;
                    case 1: // 톤 -> 밀리그램
                        tv_Result.setText(String.valueOf(Math.round((weight * 1000000000) * 1000000000) / 1000000000.0));
                        break;
                    case 2: // 톤 -> 그램
                        tv_Result.setText(String.valueOf(Math.round((weight * 1000000) * 1000000000) / 1000000000.0));
                        break;
                    case 3: // 톤 -> 킬로그램
                        tv_Result.setText(String.valueOf(Math.round((weight * 1000) * 1000000000) / 1000000000.0));
                        break;
                    case 4: // 톤 -> 파운드
                        tv_Result.setText(String.valueOf(Math.round((weight * 2204.62) * 1000000000) / 1000000000.0));
                        break;
                    case 5: // 톤 -> 톤
                        tv_Result.setText(weight + "");
                        break;
                }
                break;
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