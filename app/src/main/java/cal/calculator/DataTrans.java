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

public class DataTrans extends AppCompatActivity {

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
        setContentView(R.layout.activity_data_trans);

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
        String[] exp_str = getResources().getStringArray(R.array.data_array);
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
        String[] res_str = getResources().getStringArray(R.array.data_array);
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

    // 0.Bit 1.Byte 2.KB 3.MB 4.GB 5.TB 6.PB 7.YB
    public void changeSpinner(int exp, int res) {
        double data = Double.parseDouble(tv_Expression.getText().toString());
        DecimalFormat decimalFormat = new DecimalFormat("#.#####"); // 소수점 이하 5자리까지 표기
        switch (exp) {
            case 0:
                switch (res) {
                    case 0: // Bit -> Bit
                        tv_Result.setText(formatResult(data));
                        break;
                    case 1: // Bit -> Byte
                        tv_Result.setText(String.valueOf(formatResult(Math.round(data / 8))));
                        break;
                    case 2: // Bit -> KB
                        tv_Result.setText(decimalFormat.format(data * 0.0001220703125));
                        break;
                    case 3: // Bit -> MB
                        tv_Result.setText(decimalFormat.format(data * 1.192092895507813e-7));
                        break;
                    case 4: // Bit -> GB
                        tv_Result.setText(decimalFormat.format(data * 1.164153218269348e-10));
                        break;
                    case 5: // Bit -> TB
                        tv_Result.setText(decimalFormat.format(data * 1.13686837721616e-13));
                        break;
                    case 6: // Bit -> PB
                        tv_Result.setText(decimalFormat.format(data * 1.110223024625157e-16));
                        break;
                    case 7: // Bit -> EB
                        tv_Result.setText(decimalFormat.format(data * 1.084202172485504e-19));
                        break;
                    case 8: // Bit -> ZB
                        tv_Result.setText(decimalFormat.format(data * 1.058791184067875e-22));
                        break;
                    case 9: // Bit -> YB
                        tv_Result.setText(decimalFormat.format(data * 1.033975765691285e-25));
                        break;
                }
                break;
            case 1:
                switch (res) {
                    case 0: // Byte -> Bit
                        tv_Result.setText(String.valueOf(formatResult(Math.round(data * 8))));
                        break;
                    case 1: // Byte -> Byte
                        tv_Result.setText(formatResult(data));
                        break;
                    case 2: // Byte -> KB
                        tv_Result.setText(decimalFormat.format(data * 0.0009765625));
                        break;
                    case 3: // Byte -> MB
                        tv_Result.setText(decimalFormat.format(data * 0.000000953675));
                        break;
                    case 4: // Byte -> GB
                        tv_Result.setText(decimalFormat.format(data * 9.5367431640625e-7));
                        break;
                    case 5: // Byte -> TB
                        tv_Result.setText(decimalFormat.format(data * 9.313225746154785e-10));
                        break;
                    case 6: // Byte -> PB
                        tv_Result.setText(decimalFormat.format(data * 9.094947017729282e-13));
                        break;
                    case 7: // Byte -> EB
                        tv_Result.setText(decimalFormat.format(data * 8.881784197001252e-16));
                        break;
                    case 8: // Byte -> ZB
                        tv_Result.setText(decimalFormat.format(data * 8.673617379884035e-19));
                        break;
                    case 9: // Byte -> YB
                        tv_Result.setText(decimalFormat.format(data * 8.470329472543003e-22));
                        break;
                }
                break;
            case 2:
                switch (res) {
                    case 0: // KB -> Bit
                        tv_Result.setText(String.valueOf(formatResult(Math.round(data * 1024 * 8))));
                        break;
                    case 1: // KB -> Byte
                        tv_Result.setText(String.valueOf(formatResult(Math.round(data * 1024))));
                        break;
                    case 2: // KB -> KB
                        tv_Result.setText(formatResult(data));
                        break;
                    case 3: // KB -> MB
                        tv_Result.setText(decimalFormat.format(data * 0.0009765625));
                        break;
                    case 4: // KB -> GB
                        tv_Result.setText(decimalFormat.format(data * 0.000000953675));
                        break;
                    case 5: // KB -> TB
                        tv_Result.setText(decimalFormat.format(data * 9.5367431640625e-7));
                        break;
                    case 6: // KB -> PB
                        tv_Result.setText(decimalFormat.format(data * 9.313225746154785e-10));
                        break;
                    case 7: // KB -> EB
                        tv_Result.setText(decimalFormat.format(data * 9.094947017729282e-13));
                        break;
                    case 8: // KB -> ZB
                        tv_Result.setText(decimalFormat.format(data * 8.881784197001252e-16));
                        break;
                    case 9: // KB -> YB
                        tv_Result.setText(decimalFormat.format(data * 8.673617379884035e-19));
                        break;
                }
                break;
            case 3:
                switch (res) {
                    case 0: // MB -> Bit
                        tv_Result.setText(String.valueOf(formatResult(Math.round(data * 1048576 * 8))));
                        break;
                    case 1: // MB -> Byte
                        tv_Result.setText(String.valueOf(formatResult(Math.round(data * 1048576))));
                        break;
                    case 2: // MB -> KB
                        tv_Result.setText(String.valueOf(formatResult(Math.round(data * 1024))));
                        break;
                    case 3: // MB -> MB
                        tv_Result.setText(formatResult(data));
                        break;
                    case 4: // MB -> GB
                        tv_Result.setText(decimalFormat.format(data * 0.0009765625));
                        break;
                    case 5: // MB -> TB
                        tv_Result.setText(decimalFormat.format(data * 9.5367431640625e-7));
                        break;
                    case 6: // MB -> PB
                        tv_Result.setText(decimalFormat.format(data * 9.313225746154785e-10));
                        break;
                    case 7: // MB -> EB
                        tv_Result.setText(decimalFormat.format(data * 9.094947017729282e-13));
                        break;
                    case 8: // MB -> ZB
                        tv_Result.setText(decimalFormat.format(data * 8.881784197001252e-16));
                        break;
                    case 9: // MB -> YB
                        tv_Result.setText(decimalFormat.format(data * 8.673617379884035e-19));
                        break;
                }
                break;
            case 4:
                switch (res) {
                    case 0: // GB -> Bit
                        tv_Result.setText(String.valueOf(formatResult(Math.round(data * 1073741824 * 8))));
                        break;
                    case 1: // GB -> Byte
                        tv_Result.setText(String.valueOf(formatResult(Math.round(data * 1073741824))));
                        break;
                    case 2: // GB -> KB
                        tv_Result.setText(String.valueOf(formatResult(Math.round(data * 1048576))));
                        break;
                    case 3: // GB -> MB
                        tv_Result.setText(String.valueOf(formatResult(Math.round(data * 1024))));
                        break;
                    case 4: // GB -> GB
                        tv_Result.setText(formatResult(data));
                        break;
                    case 5: // GB -> TB
                        tv_Result.setText(decimalFormat.format(data * 0.0009765625));
                        break;
                    case 6: // GB -> PB
                        tv_Result.setText(decimalFormat.format(data * 0.000000953675));
                        break;
                    case 7: // GB -> EB
                        tv_Result.setText(decimalFormat.format(data * 9.5367431640625e-7));
                        break;
                    case 8: // GB -> ZB
                        tv_Result.setText(decimalFormat.format(data * 9.313225746154785e-10));
                        break;
                    case 9: // GB -> YB
                        tv_Result.setText(decimalFormat.format(data * 9.094947017729282e-13));
                        break;
                }
                break;
            case 5:
                switch (res) {
                    case 0: // TB -> Bit
                        tv_Result.setText(String.valueOf(formatResult(Math.round(data * 1073741824 * 1024 * 8))));
                        break;
                    case 1: // TB -> Byte
                        tv_Result.setText(String.valueOf(formatResult(Math.round(data * 1073741824 * 1024))));
                        break;
                    case 2: // TB -> KB
                        tv_Result.setText(String.valueOf(formatResult(Math.round(data * 1073741824))));
                        break;
                    case 3: // TB -> MB
                        tv_Result.setText(String.valueOf(formatResult(Math.round(data * 1048576))));
                        break;
                    case 4: // TB -> GB
                        tv_Result.setText(String.valueOf(formatResult(Math.round(data * 1024))));
                        break;
                    case 5: // TB -> TB
                        tv_Result.setText(formatResult(data));
                        break;
                    case 6: // TB -> PB
                        tv_Result.setText(decimalFormat.format(data * 0.0009765625));
                        break;
                    case 7: // TB -> EB
                        tv_Result.setText(decimalFormat.format(data * 9.5367431640625e-7));
                        break;
                    case 8: // TB -> ZB
                        tv_Result.setText(decimalFormat.format(data * 9.313225746154785e-10));
                        break;
                    case 9: // TB -> YB
                        tv_Result.setText(decimalFormat.format(data * 9.094947017729282e-13));
                        break;
                }
                break;
            case 6:
                switch (res) {
                    case 0: // PB -> Bit
                        tv_Result.setText(String.valueOf(formatResult(Math.round(data * 1073741824 * 1048576 * 8))));
                        break;
                    case 1: // PB -> Byte
                        tv_Result.setText(String.valueOf(formatResult(Math.round(data * 1073741824 * 1048576))));
                        break;
                    case 2: // PB -> KB
                        tv_Result.setText(String.valueOf(formatResult(Math.round(data * 1073741824 * 1024))));
                        break;
                    case 3: // PB -> MB
                        tv_Result.setText(String.valueOf(formatResult(Math.round(data * 1073741824))));
                        break;
                    case 4: // PB -> GB
                        tv_Result.setText(String.valueOf(formatResult(Math.round(data * 1048576))));
                        break;
                    case 5: // PB -> TB
                        tv_Result.setText(String.valueOf(formatResult(Math.round(data * 1024))));
                        break;
                    case 6: // PB -> PB
                        tv_Result.setText(formatResult(data));
                        break;
                    case 7: // PB -> EB
                        tv_Result.setText(decimalFormat.format(data * 0.0009765625));
                        break;
                    case 8: // PB -> ZB
                        tv_Result.setText(decimalFormat.format(data * 9.5367431640625e-7));
                        break;
                    case 9: // PB -> YB
                        tv_Result.setText(decimalFormat.format(data * 9.313225746154785e-10));
                        break;
                }
                break;
            case 7:
                switch (res) {
                    case 0: // EB -> Bit
                        tv_Result.setText(String.valueOf(formatResult(Math.round(data * 1073741824 * 1073741824 * 8))));
                        break;
                    case 1: // EB -> Byte
                        tv_Result.setText(String.valueOf(formatResult(Math.round(data * 1073741824 * 1073741824))));
                        break;
                    case 2: // EB -> KB
                        tv_Result.setText(String.valueOf(formatResult(Math.round(data * 1073741824 * 1048576))));
                        break;
                    case 3: // EB -> MB
                        tv_Result.setText(String.valueOf(formatResult(Math.round(data * 1073741824 * 1024))));
                        break;
                    case 4: // EB -> GB
                        tv_Result.setText(String.valueOf(formatResult(Math.round(data * 1073741824))));
                        break;
                    case 5: // EB -> TB
                        tv_Result.setText(String.valueOf(formatResult(Math.round(data * 1048576))));
                        break;
                    case 6: // EB -> PB
                        tv_Result.setText(String.valueOf(formatResult(Math.round(data * 1024))));
                        break;
                    case 7: // EB -> EB
                        tv_Result.setText(formatResult(data));
                        break;
                    case 8: // EB -> ZB
                        tv_Result.setText(decimalFormat.format(data * 0.0009765625));
                        break;
                    case 9: // EB -> YB
                        tv_Result.setText(decimalFormat.format(data * 9.5367431640625e-7));
                        break;
                }
                break;
            case 8:
                switch (res) {
                    case 0: // ZB -> Bit
                        tv_Result.setText(String.valueOf(formatResult(Math.round(data * 1073741824 * 1073741824 * 1024 * 8))));
                        break;
                    case 1: // ZB -> Byte
                        tv_Result.setText(String.valueOf(formatResult(Math.round(data * 1073741824 * 1073741824 * 1024))));
                        break;
                    case 2: // ZB -> KB
                        tv_Result.setText(String.valueOf(formatResult(Math.round(data * 1073741824 * 1073741824))));
                        break;
                    case 3: // ZB -> MB
                        tv_Result.setText(String.valueOf(formatResult(Math.round(data * 1073741824 * 1048576))));
                        break;
                    case 4: // ZB -> GB
                        tv_Result.setText(String.valueOf(formatResult(Math.round(data * 1073741824 * 1024))));
                        break;
                    case 5: // ZB -> TB
                        tv_Result.setText(String.valueOf(formatResult(Math.round(data * 1073741824))));
                        break;
                    case 6: // ZB -> PB
                        tv_Result.setText(String.valueOf(formatResult(Math.round(data * 1048576))));
                        break;
                    case 7: // ZB -> EB
                        tv_Result.setText(String.valueOf(formatResult(Math.round(data * 1024))));
                        break;
                    case 8: // ZB -> ZB
                        tv_Result.setText(formatResult(data));
                        break;
                    case 9: // ZB -> YB
                        tv_Result.setText(decimalFormat.format(data * 0.0009765625));
                        break;
                }
                break;
            case 9:
                switch (res) {
                    case 0: // YB -> Bit
                        tv_Result.setText(String.valueOf(formatResult(Math.round(data * 1073741824 * 1073741824 * 1073741824 * 8))));
                        break;
                    case 1: // YB -> Byte
                        tv_Result.setText(String.valueOf(formatResult(Math.round(data * 1073741824 * 1073741824 * 1048576))));
                        break;
                    case 2: // YB -> KB
                        tv_Result.setText(String.valueOf(formatResult(Math.round(data * 1073741824 * 1073741824 * 1024))));
                        break;
                    case 3: // YB -> MB
                        tv_Result.setText(String.valueOf(formatResult(Math.round(data * 1073741824 * 1073741824))));
                        break;
                    case 4: // YB -> GB
                        tv_Result.setText(String.valueOf(formatResult(Math.round(data * 1073741824 * 1048576))));
                        break;
                    case 5: // YB -> TB
                        tv_Result.setText(String.valueOf(formatResult(Math.round(data * 1073741824 * 1024))));
                        break;
                    case 6: // YB -> PB
                        tv_Result.setText(String.valueOf(formatResult(Math.round(data * 1073741824))));
                        break;
                    case 7: // YB -> EB
                        tv_Result.setText(String.valueOf(formatResult(Math.round(data * 1048576))));
                        break;
                    case 8: // YB -> ZB
                        tv_Result.setText(String.valueOf(formatResult(Math.round(data * 1024))));
                        break;
                    case 9: // YB -> YB
                        tv_Result.setText(formatResult(data));
                        break;
                }
                break;
        }
    }

    private String formatResult(double data) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(data);
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