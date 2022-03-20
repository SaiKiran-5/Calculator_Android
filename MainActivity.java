package com.example.my_calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    TextView display;
    int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        display = findViewById(R.id.textView);
    }

    public void clear(View view) {
        display.setText("");
    }

    public void delete(View view) {
        String inp = display.getText().toString();
        if (inp.length() == 0)
            return;
        inp = inp.substring(0, inp.length() - 1);
        display.setText(inp);
    }

    public void input(View view) {
        String str = display.getText().toString();
        String inp = ((Button) view).getText().toString();
        str = str + inp;
        display.setText(str);
    }

    public void Calculate(View view) {
        flag = 0;
        String inp = display.getText().toString();
        char[] tokens = inp.toCharArray();
        Stack<Double> values = new Stack<>();
        Stack<Character> ops = new Stack<Character>();

        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i] == ' ')
                continue;
            if (tokens[i] >= '0' && tokens[i] <= '9' || tokens[i] == '.') {
                StringBuffer sbuf = new StringBuffer();
                while ((i < tokens.length && tokens[i] >= '0' && tokens[i] <= '9') || (i < tokens.length && tokens[i] == '.'))
                    sbuf.append(tokens[i++]);
                values.push(Double.parseDouble(sbuf.toString()));
                i--;
            } else if (tokens[i] == '+' || tokens[i] == '-' || tokens[i] == '×' || tokens[i] == '÷' || tokens[i] == '^') {
                while (!ops.empty() && hasPrecedence(tokens[i], ops.peek()))
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                ops.push(tokens[i]);
            }
        }

        while (!ops.empty())
            values.push(applyOp(ops.pop(), values.pop(), values.pop()));
        String out = values.pop().toString();
        if (flag == 0)
            display.setText(out);
    }

    public static boolean hasPrecedence(char op1, char op2) {
        if ((op1 == '^') && (op2 == '×' || op2 == '÷' || op2 == '+' || op2 == '-'))
            return false;
        if ((op1 == '×' || op1 == '÷') && (op2 == '+' || op2 == '-'))
            return false;
        else
            return true;
    }

    public double applyOp(char op, Double b, Double a) {
        switch (op) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '×':
                return a * b;
            case '÷':
                if (b == 0) {
                    display.setTextSize(35);
                    display.setText("Cannot be Divided by 0!");
                    flag = 1;
                    return 0;
                }
                return a / b;
            case '^':
                return Math.pow(a, b);
        }
        return 0;
    }
}