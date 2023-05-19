package cal.calculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.lang.Integer;

public class ProgrammerCal extends AppCompatActivity {

    //SERVER
    TextView tv_Expression;
    TextView tv_Result;
    List<Integer> checkList; // -1: 이콜, 0: 연산자, 1: 숫자, 2: . / 예외 발생을 막는 리스트
    Stack<String> operatorStack; // 연산자 스택
    List<String> infixList; // 중위 표기
    List<String> postfixList; // 후위 표기

    Boolean resultSet = false; // 마지막 동작이 = 인지


    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    RadioGroup rg;
    RadioButton rb_hex, rb_dec, rb_oct, rb_bin;
    int cal_mode = 1; // 0 : 16진수, 1 : 10진수, 2 : 8진수, 3 : 2진수



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programmer_cal);

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
                //String title = item.getTitle().toString();

                if(id == R.id.menu_tempTrans){
                    Intent in = new Intent(getApplicationContext(), TempTrans.class);
                    startActivity(in);
                    finish();
                }
                if(id == R.id.menu_defCal){
                    Intent in = new Intent(getApplicationContext(), DefaultCal.class);
                    startActivity(in);
                    finish();
                }
                return true;
            }
        });

        rb_hex = findViewById(R.id.bt_hex);
        rb_dec = findViewById(R.id.bt_dec);
        rb_oct = findViewById(R.id.bt_oct);
        rb_bin = findViewById(R.id.bt_bin);

        rg = findViewById(R.id.radioGroup);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if(checkedId == R.id.bt_hex){
                    cal_mode = 0;
                }
                else if(checkedId == R.id.bt_dec){
                    cal_mode = 1;
                }
                else if(checkedId == R.id.bt_oct){
                    cal_mode = 2;
                }
                else{
                    cal_mode = 3;
                }
                radioChange(cal_mode);
            }
        });
    }


    void radioChange(int mode){
        int button_id[] = {R.id.bt_0, R.id.bt_1, R.id.bt_2, R.id.bt_3, R.id.bt_4, R.id.bt_5, R.id.bt_6, R.id.bt_7
                        , R.id.bt_8, R.id.bt_9, R.id.bt_A, R.id.bt_B, R.id.bt_C, R.id.bt_D, R.id.bt_E, R.id.bt_F};
        for (int i : button_id){
            findViewById(i).setEnabled(true);
        }
        switch(mode){
            case 0: // 16진수
                //
                break;
            case 1: // 10진수
                for(int i = 10; i < 16 ; i++){
                    findViewById(button_id[i]).setEnabled(false);
                }
                break;
            case 2: // 8진수
                for(int i = 8; i < 16 ; i++){
                    findViewById(button_id[i]).setEnabled(false);
                }
                break;
            case 3: // 2진수
                for(int i = 2; i < 16 ; i++){
                    findViewById(button_id[i]).setEnabled(false);
                }
                break;
        }
    }

    void init() {
        tv_Expression = findViewById(R.id.tv_expression);
        tv_Result = findViewById(R.id.tv_result);
        checkList = new ArrayList<>();
        operatorStack = new Stack<>();
        infixList = new ArrayList<>();
        postfixList = new ArrayList<>();
    }

    public void btClick(View v) {
        int gId = v.getId();

        if (!checkList.isEmpty() && checkList.get(checkList.size() - 1) == -1) {
            tv_Expression.setText(tv_Result.getText().toString());
            checkList.clear();
            checkList.add(1); // 정수
            checkList.add(2); // .
            checkList.add(1); // 소수점
            tv_Result.setText("");
            resultSet = false;
        }

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
        else if (gId == R.id.bt_A) addNumber("A");
        else if (gId == R.id.bt_B) addNumber("B");
        else if (gId == R.id.bt_C) addNumber("C");
        else if (gId == R.id.bt_D) addNumber("D");
        else if (gId == R.id.bt_E) addNumber("E");
        else if (gId == R.id.bt_F) addNumber("F");
        else if (gId == R.id.bt_div) addOperator("/");
        else if (gId == R.id.bt_mul) addOperator("X");
        else if (gId == R.id.bt_add) addOperator("+");
        else if (gId == R.id.bt_sub) addOperator("-");
    }

    public void clearClick (View v){
        infixList.clear();
        checkList.clear();
        tv_Expression.setText("");
        tv_Result.setText("");
        operatorStack.clear();
        postfixList.clear();
    }

    public void deleteClick (View v){
        if (tv_Expression.length() != 0) {
            checkList.remove(checkList.size() - 1);
            String[] ex = tv_Expression.getText().toString().split(" ");
            List<String> li = new ArrayList<String>();
            Collections.addAll(li, ex);
            li.remove(li.size() - 1);
            // 마지막이 연산자일 때 " " 빈칸 추가
            if (li.size() > 0 && !isNumber(li.get(li.size() - 1)))
                li.add(li.remove(li.size() - 1) + " ");
            tv_Expression.setText(TextUtils.join(" ", li));
        }
        tv_Result.setText("");
        resultSet = false;
    }

    // 숫자 버튼
    void addNumber (String str){
        checkList.add(1); // 숫자가 들어왔는지 체크리스트에 표시
        tv_Expression.append(str); // UI
        resultSet = false;
    }

    // 연산자 버튼
    void addOperator (String str){
        try {
            if (checkList.isEmpty()) { // 처음 연산자 사용 막기
                Toast.makeText(getApplicationContext(), "첫 입력값은 숫자입니다.", Toast.LENGTH_SHORT).show();
                return;
            } else if (checkList.get(checkList.size() - 1) == 0  || checkList.get(checkList.size() - 1) == 2) { // 연산자 두 번 사용, 완벽한 수가 오지 않았을 때 막기

                String[] ex = tv_Expression.getText().toString().split(" ");
                List<String> li = new ArrayList<String>();
                Collections.addAll(li, ex);
                li.remove(li.size() - 1);

                li.add(li.remove(li.size() - 1) + " " + str + " ");
                tv_Expression.setText(TextUtils.join(" ", li));
                resultSet = false;
                return;
            }
            checkList.add(0);
            tv_Expression.append(" " + str + " ");
            resultSet = false;
        } catch (Exception e) {
            Log.e("addOperator", e.toString());
        }
    }

    // 결과 버튼
    public void btResult (View v){
        if (tv_Expression.length() == 0) return;
        if (checkList.get(checkList.size() - 1) != 1) {
            Toast.makeText(getApplicationContext(), "마지막 입력값이 숫자여야 사용가능합니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        Collections.addAll(infixList, tv_Expression.getText().toString().split(" "));
        checkList.add(-1);
        result();
    }

    public void negaClick (View v){
        if(tv_Result.length() != 0){
            String[] rex = tv_Result.getText().toString().split("");

            if(!isNumber(rex[0])){
                String rtemp = tv_Result.getText().toString().substring(1, rex.length);
                tv_Expression.setText(rtemp);
                tv_Result.setText("");
                checkList.add(1);
                resultSet = false;
                return;
            }

            tv_Expression.setText("-" + tv_Result.getText());
            tv_Result.setText("");
            checkList.add(1);
            resultSet = false;
            return;
        }
        if (tv_Expression.length() == 0 || checkList.get(checkList.size() - 1) != 1) {
            Toast.makeText(getApplicationContext(), "마지막 입력값이 숫자여야 사용가능합니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] ex = tv_Expression.getText().toString().split(" ");
        String[] fex = ex[ex.length -1].split("");

        List<String> li = new ArrayList<String>();
        Collections.addAll(li, ex);
        String temps = li.remove(li.size() - 1);

        if(!isNumber(fex[0])){
            List<String> tli = new ArrayList<String>();
            Collections.addAll(tli, fex);
            tli.remove(0);
            li.add(TextUtils.join("", tli));
        }
        else
            li.add("-" + temps);
        tv_Expression.setText(TextUtils.join(" ", li));
        resultSet = true;
    }

    public void btxx(View v){
        if(tv_Result.length() != 0){
            int dtemp = Integer.parseInt(tv_Result.getText().toString()) * Integer.parseInt(tv_Result.getText().toString());
            tv_Expression.setText(String.valueOf(dtemp));
            tv_Result.setText("");
            checkList.add(1);
            resultSet = false;
            return;
        }

        if (tv_Expression.length() == 0 || checkList.get(checkList.size() - 1) != 1) {
            Toast.makeText(getApplicationContext(), "마지막 입력값이 숫자여야 사용가능합니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] ex = tv_Expression.getText().toString().split(" ");
        List<String> li = new ArrayList<String>();
        Collections.addAll(li, ex);
        String temps = li.remove(li.size() - 1);
        li.add(String.valueOf(Integer.parseInt(temps) * Integer.parseInt(temps)));

        tv_Expression.setText(TextUtils.join(" ", li));
        resultSet = false;
    }


    public void btRoot(View v){
        if(tv_Result.length() != 0){
            int dtemp = Integer.parseInt(tv_Result.getText().toString()) * Integer.parseInt(tv_Result.getText().toString());
            tv_Expression.setText(String.valueOf(dtemp));
            tv_Result.setText("");
            checkList.add(1);
            resultSet = false;
            return;
        }

        if (tv_Expression.length() == 0 || checkList.get(checkList.size() - 1) != 1) {
            Toast.makeText(getApplicationContext(), "마지막 입력값이 숫자여야 사용가능합니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] ex = tv_Expression.getText().toString().split(" ");
        List<String> li = new ArrayList<String>();
        Collections.addAll(li, ex);
        String temps = li.remove(li.size() - 1);
        li.add(String.valueOf(Integer.parseInt(temps) * Integer.parseInt(temps)));

        tv_Expression.setText(TextUtils.join(" ", li));
        resultSet = false;
    }


    // 1/x 버튼
    public void fracClick(View v) {
        if(tv_Result.length() != 0){
            tv_Expression.setText("1 / " + tv_Result.getText());
            tv_Result.setText("");
            checkList.add(1);
            resultSet = false;
            return;
        }

        if (tv_Expression.length() == 0 || checkList.get(checkList.size() - 1) != 1) {
            Toast.makeText(getApplicationContext(), "마지막 입력값이 숫자여야 사용가능합니다.", Toast.LENGTH_SHORT).show();
            return;
        }


        String[] ex = tv_Expression.getText().toString().split(" ");
        List<String> li = new ArrayList<String>();
        Collections.addAll(li, ex);
        String temps = li.remove(li.size() - 1);

        li.add("1 / " + temps);
        tv_Expression.setText(TextUtils.join(" ", li));
        resultSet = false;
    }

    // 연산자 가중치
    int getWeight (String operator){
        int weight = 0;
        switch (operator) {
            case "X":
            case "/":
                weight = 3;
                break;
            case "%":
                weight = 2;
                break;
            case "+":
            case "-":
                weight = 1;
                break;
        }
        return weight;
    }

    // 숫자 판별
    boolean isNumber (String str){
        boolean result = true;
        try {
            Integer.parseInt(str);
        } catch (NumberFormatException e) {
            try{
                Integer.parseInt(str, 16);
            } catch (NumberFormatException e2){
                result = false;
            }
        }
        return result;
    }

    // 중위 > 후위
    void infixToPostfix () {
        for (String item : infixList) {
            // 피연산자
            if (isNumber(item)) {
                postfixList.add(item);
            }
                // 연산자
            else { // 5 + 3 * sqrt(56) +
                if (operatorStack.isEmpty()) operatorStack.push(item);
                else {
                    if (getWeight(operatorStack.peek()) >= getWeight(item))
                        postfixList.add(operatorStack.pop());
                    operatorStack.push(item);
                }
            }
        }
        while (!operatorStack.isEmpty()) postfixList.add(operatorStack.pop());
    }

    // 계산
    String calculate (String num1, String num2, String op){
        int first = 0, second = 0, result = 0;
        switch(cal_mode){
            case 0:
                first = Integer.parseInt(num1, 16);
                second = Integer.parseInt(num2, 16);
                break;
            case 1:
                first = Integer.parseInt(num1);
                second = Integer.parseInt(num2);
                break;
            case 2:
                first = Integer.parseInt(num1, 8);
                second = Integer.parseInt(num2, 8);
                break;
            case 3:
                first = Integer.parseInt(num1, 2);
                second = Integer.parseInt(num2, 2);
                break;
        }

        try {
            switch (op) {
                case "X":
                    result = first * second;
                    break;
                case "/":
                    result = first / second;
                    break;
                case "%":
                    result = first % second;
                    break;
                case "+":
                    result = first + second;
                    break;
                case "-":
                    result = first - second;
                    break;
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "연산할 수 없습니다.", Toast.LENGTH_SHORT).show();
        }

        switch(cal_mode){
            case 0:
                return Integer.toHexString(result);
            case 1:
                return String.valueOf(result);
            case 2:
                return Integer.toOctalString(result);
            case 3:
                return Integer.toBinaryString(result);
        }
        return "";
    }

    // 최종 결과
    void result () {
        int i = 0;
        infixToPostfix();
        while (postfixList.size() != 1) {
            if (!isNumber(postfixList.get(i))) {
                postfixList.add(i - 2, calculate(postfixList.remove(i - 2), postfixList.remove(i - 2), postfixList.remove(i - 2)));
                i = -1;
            }
            i++;
        }
        String temp = postfixList.remove(0);
        int dec_temp = 0;
        switch(cal_mode){
            case 0:
                dec_temp = Integer.parseInt(temp, 16);
                break;
            case 1:
                dec_temp = Integer.parseInt(temp);
                break;
            case 2:
                dec_temp = Integer.parseInt(temp, 8);
                break;
            case 3:
                dec_temp = Integer.parseInt(temp, 2);
                break;
            default:
                dec_temp = 0;
        }
        tv_Result.setText(temp);
        rb_hex.setText("HEX\t\t\t" + Integer.toHexString(dec_temp));
        rb_dec.setText("DEC\t\t\t" + Integer.valueOf(dec_temp));
        rb_oct.setText("OCT\t\t\t" + Integer.toOctalString(dec_temp));
        rb_bin.setText("BIN\t\t\t" + Integer.toBinaryString(dec_temp));
        //tv_Result.setText(postfixList.remove(0));
        infixList.clear();
        resultSet = true;
    }
}
