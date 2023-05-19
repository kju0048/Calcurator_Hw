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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defcal);

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
                return true;
            }
        });

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
        if(resultSet && gId == R.id.bt_point){
            Toast.makeText(getApplicationContext(), ". 을 사용할 수 없습니다.", Toast.LENGTH_SHORT).show();
            return;
        }
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
        else if (gId == R.id.bt_point) addPoint(".");
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

    void addPoint (String str){
        if (checkList.isEmpty()) {
            Toast.makeText(getApplicationContext(), ". 을 사용할 수 없습니다.", Toast.LENGTH_SHORT).show();
            return;
        } else if (checkList.get(checkList.size() - 1) != 1) {
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
            double dtemp = Double.parseDouble(tv_Result.getText().toString()) * Double.parseDouble(tv_Result.getText().toString());
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
        double dtemps = Double.parseDouble(temps) * Double.parseDouble(temps);
        if (dtemps % 1 == 0){
            li.add(String.valueOf(Integer.parseInt(temps) * Integer.parseInt(temps)));
        } else{
            li.add(String.valueOf(dtemps));
        }
        tv_Expression.setText(TextUtils.join(" ", li));
        resultSet = false;
    }


    public void btRoot(View v){
        if(tv_Result.length() != 0){
            double dtemp = Double.parseDouble(tv_Result.getText().toString()) * Double.parseDouble(tv_Result.getText().toString());
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
        double dtemps = Double.parseDouble(temps) * Double.parseDouble(temps);
        if (dtemps % 1 == 0){
            li.add(String.valueOf(Integer.parseInt(temps) * Integer.parseInt(temps)));
        } else{
            li.add(String.valueOf(dtemps));
        }
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
            Double.parseDouble(str);
        } catch (NumberFormatException e) {
            result = false;
        }
        return result;
    }

    // 중위 > 후위
    void infixToPostfix () {
        for (String item : infixList) {
            // 피연산자
            if (isNumber(item)) postfixList.add(String.valueOf(Double.parseDouble(item)));
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
        double first = Double.parseDouble(num1);
        double second = Double.parseDouble(num2);
        double result = 0.0;
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
        return String.valueOf(result);
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
        double temp = Double.parseDouble(postfixList.remove(0));
        temp = Double.valueOf(Math.round(temp * 100000) / 100000.0);
        if (temp % 1 == 0){
            tv_Result.setText(Integer.toString((int)temp));
        } else{
            tv_Result.setText(Double.toString(temp));
        }
        //tv_Result.setText(postfixList.remove(0));
        infixList.clear();
        resultSet = true;
    }

}
