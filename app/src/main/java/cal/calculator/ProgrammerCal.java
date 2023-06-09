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
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.lang.Integer;

public class ProgrammerCal extends AppCompatActivity {

    //SERVER
    TextView tv_Expression;
    TextView tv_Result;
    List<Integer> checkList; // -1: 이콜, 0: 연산자, 1: 숫자, 2: ., 3: (, 4: ) 예외 발생을 막는 리스트
    Stack<String> operatorStack; // 연산자 스택
    Stack<String> bracketOperatorStack;
    List<String> bracket_post;
    List<String> infixList; // 중위 표기
    List<String> postfixList; // 후위 표기

    Boolean resultSet = false; // 마지막 동작이 = 인지

    int result = 0;


    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    RadioGroup rg;
    RadioButton rb_hex, rb_dec, rb_oct, rb_bin;
    int cal_mode = 1; // 0 : 16진수, 1 : 10진수, 2 : 8진수, 3 : 2진수
    int bracket_count = 0; // ( 개수 카운트



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
                    tv_Result.setText(Integer.toHexString(result));
                }
                else if(checkedId == R.id.bt_dec){
                    cal_mode = 1;
                    tv_Result.setText(String.valueOf(result));
                }
                else if(checkedId == R.id.bt_oct){
                    cal_mode = 2;
                    tv_Result.setText(Integer.toOctalString(result));
                }
                else{
                    cal_mode = 3;
                    tv_Result.setText(Integer.toBinaryString(result));
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
        tv_Result.setText("0");
        checkList = new ArrayList<>();
        operatorStack = new Stack<>();
        infixList = new ArrayList<>();
        postfixList = new ArrayList<>();
        bracketOperatorStack = new Stack<>();
        bracket_post = new ArrayList<>();
    }

    public void btClick(View v) {
        int gId = v.getId();

        if (!checkList.isEmpty() && checkList.get(checkList.size() - 1) == -1) {
            if(gId != R.id.bt_add && gId != R.id.bt_mul && gId != R.id.bt_sub && gId != R.id.bt_div && gId != R.id.bt_percent
            && gId != R.id.bt_leftShift && gId != R.id.bt_rightShift){
                Toast.makeText(getApplicationContext(), "숫자를 입력할 수 없습니다.", Toast.LENGTH_SHORT).show();
                return;
            }
            tv_Expression.setText(tv_Result.getText().toString());
            checkList.clear();
            checkList.add(1); // 정수
            tv_Result.setText("");
            resultSet = false;
        }

        if(checkList.isEmpty())
            tv_Result.setText("");

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
        else if (gId == R.id.bt_A) addNumber("a");
        else if (gId == R.id.bt_B) addNumber("b");
        else if (gId == R.id.bt_C) addNumber("c");
        else if (gId == R.id.bt_D) addNumber("d");
        else if (gId == R.id.bt_E) addNumber("e");
        else if (gId == R.id.bt_F) addNumber("f");
        else if (gId == R.id.bt_div) addOperator("/");
        else if (gId == R.id.bt_mul) addOperator("X");
        else if (gId == R.id.bt_add) addOperator("+");
        else if (gId == R.id.bt_sub) addOperator("-");
        else if (gId == R.id.bt_percent) addOperator("%");
        else if (gId == R.id.bt_leftShift) addOperator("<<");
        else if (gId == R.id.bt_rightShift) addOperator(">>");
    }

    public void btBracket(View v){
        int gId = v.getId();
        if(gId == R.id.bt_leftBracket){
            if(resultSet){
                Toast.makeText(getApplicationContext(), "괄호를 입력할 수 없습니다.", Toast.LENGTH_SHORT).show();
                return;
            }
            if(checkList.isEmpty()){
                checkList.add(3);
                tv_Expression.setText("( ");
            }
            else if(checkList.get(checkList.size() - 1) == 1){
                checkList.add(0);
                checkList.add(3);
                tv_Expression.setText(tv_Expression.getText() + " X ( ");
            }
            else if(checkList.get(checkList.size() - 1) == 0){
                checkList.add(3);
                tv_Expression.setText(tv_Expression.getText() + "( ");
            }
            else if(checkList.get(checkList.size() - 1) == 3){
                checkList.add(3);
                tv_Expression.setText(tv_Expression.getText() + "( ");
            }
            else if(checkList.get(checkList.size() - 1) == 4){
                checkList.add(3);
                tv_Expression.setText(tv_Expression.getText() + " X ( ");
            }
            else{
                Toast.makeText(getApplicationContext(), "Left Bracket Error", Toast.LENGTH_SHORT).show();
                return;
            }
            bracket_count++;
        }
        else if(gId == R.id.bt_rightBracket){
            if(bracket_count == 0 || checkList.get(checkList.size() - 1) == 0){
                Toast.makeText(getApplicationContext(), "괄호를 입력할 수 없습니다.", Toast.LENGTH_SHORT).show();
                return;
            } else if(checkList.get(checkList.size() - 1) == 3){
                checkList.add(1);
                checkList.add(4);
                tv_Expression.setText(tv_Expression.getText() + "0 )");
            }

            else{
                checkList.add(4);
                tv_Expression.setText(tv_Expression.getText() + " )");
            }
            bracket_count--;
        }
    }

    public void clearClick (View v){
        infixList.clear();
        checkList.clear();
        tv_Expression.setText("");
        tv_Result.setText("0");
        operatorStack.clear();
        postfixList.clear();
        resultSet = false;
        result = 0;
        bracket_count = 0;

        rb_hex.setText("HEX\t\t\t0");
        rb_dec.setText("DEC\t\t\t0");
        rb_oct.setText("OCT\t\t\t0");
        rb_bin.setText("BIN\t\t\t0");
    }

    public void deleteClick (View v){
        if (tv_Expression.length() != 0) {
            if(checkList.get(checkList.size() - 1) == 4){
                bracket_count--;
                return;
            }
            if(checkList.get(checkList.size() - 1) == -1){
                checkList.remove(checkList.size() - 1);
            }
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
        rb_hex.setText("HEX\t\t\t0");
        rb_dec.setText("DEC\t\t\t0");
        rb_oct.setText("OCT\t\t\t0");
        rb_bin.setText("BIN\t\t\t0");
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
            } else if (checkList.get(checkList.size() - 1) == 3){
                Toast.makeText(getApplicationContext(), "괄호 뒤에 연산자가 올 수 없습니다.", Toast.LENGTH_SHORT).show();
                return;
            }
                else if (checkList.get(checkList.size() - 1) == 0  || checkList.get(checkList.size() - 1) == 2) { // 연산자 두 번 사용, 완벽한 수가 오지 않았을 때 막기

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
    public void btResult (View v) {

        if (bracket_count != 0) {
            Toast.makeText(getApplicationContext(), "수식이 정확하지 않습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (tv_Expression.length() == 0) return;
        if (checkList.get(checkList.size() - 1) != 1 && checkList.get(checkList.size() - 1) != 4) {
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

    // 연산자 가중치
    int getWeight (String operator){
        int weight = 0;
        switch (operator) {
            case "<<":
            case ">>":
                weight = 4;
                break;
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
                case ">>":
                    if(first % second == 1) first--;
                    for(int i = 0 ; i < second ; i++){
                        if(first == 1) {
                            first = 0;
                            break;
                        }
                        first /= 2;
                    }
                    result = first;
                    break;
                case "<<":
                    for(int i = 0 ; i < second ; i++){
                        first *= 2;
                    }
                    result = first;
                    break;
            }
        }
        catch (Exception e) {
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

    // 중위 > 후위 () ver
    List<String> infixToPostfix (List<String> t) {
        for (String item : t) {
            // 피연산자
            if (isNumber(item)) {
                bracket_post.add(item);
            }
            // 연산자
            else { // 5 + 3 * sqrt(56) +
                if (bracketOperatorStack.isEmpty()) bracketOperatorStack.push(item);
                else {
                    if (getWeight(bracketOperatorStack.peek()) >= getWeight(item))
                        bracket_post.add(bracketOperatorStack.pop());
                    bracketOperatorStack.push(item);
                }
            }
        }
        while (!bracketOperatorStack.isEmpty()) bracket_post.add(bracketOperatorStack.pop());

        return bracket_post;
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


    // 최종 결과
    void result () {
        int i = 0, j = 0;

        int leftBra = 0, rightBra = 0;
        List<String> postfix_bracket;

        while (infixList.contains("(")){
            leftBra = infixList.lastIndexOf("(");
            rightBra = infixList.subList(leftBra, infixList.size()).indexOf(")") + leftBra;
            postfix_bracket = infixToPostfix(infixList.subList(leftBra + 1, rightBra));
            while (postfix_bracket.size() != 1) {
                if (!isNumber(postfix_bracket.get(j))) {
                    postfix_bracket.add(j - 2, calculate(postfix_bracket.remove(j - 2), postfix_bracket.remove(j - 2), postfix_bracket.remove(j - 2)));
                    j = -1;
                }
                j++;
            }
            String brac_temp = postfix_bracket.remove(0);
            for(int t = leftBra; t <= rightBra; t++){
                infixList.remove(leftBra);
            }
            infixList.add(leftBra, brac_temp);
        }




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

        result = dec_temp;
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
