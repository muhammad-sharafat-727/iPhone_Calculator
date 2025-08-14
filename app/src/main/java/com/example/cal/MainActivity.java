package com.example.cal;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText display;
    private String currentInput = "0";
    private double memoryValue = 0;
    private boolean isRadianMode = true;
    private boolean isResultShown = false;
    private boolean isSecondFunction = false;
    private static final String MATH_ERROR = "Math Error";
    private static final String SYNTAX_ERROR = "Syntax Error";
    private static final MathContext PRECISION = new MathContext(15, RoundingMode.HALF_UP);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setupClickListeners();

        if (savedInstanceState != null) {
            restoreState(savedInstanceState);
        }
    }

    private void initializeViews() {
        display = findViewById(R.id.display);
        display.setText(currentInput);
        display.setSelection(display.getText().length());
    }

    private void setupClickListeners() {
        // Number buttons
        setClickListener(R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4,
                R.id.btn_5, R.id.btn_6, R.id.btn_7, R.id.btn_8, R.id.btn_9);

        // Basic operations
        setClickListener(R.id.btn_add, R.id.btn_subtract, R.id.btn_multiply, R.id.btn_divide);

        // Special buttons
        setClickListener(R.id.btn_equals, R.id.btn_decimal, R.id.btn_ac, R.id.btn_sign,
                R.id.btn_percent, R.id.btn_open_bracket, R.id.btn_close_bracket);

        // Scientific functions
        setClickListener(R.id.btn_square, R.id.btn_cube, R.id.btn_power, R.id.btn_sqrt,
                R.id.btn_cube_root, R.id.btn_y_root, R.id.btn_reciprocal, R.id.btn_factorial);

        // Trigonometric functions
        setClickListener(R.id.btn_sin, R.id.btn_cos, R.id.btn_tan,
                R.id.btn_sinh, R.id.btn_cosh, R.id.btn_tanh);

        // Logarithmic and exponential
        setClickListener(R.id.btn_ln, R.id.btn_log, R.id.btn_exp, R.id.btn_ten_power);

        // Constants and special functions
        setClickListener(R.id.btn_pi, R.id.btn_e, R.id.btn_rand, R.id.btn_ee, R.id.btn_rad);

        // Memory functions
        setClickListener(R.id.btn_mc, R.id.btn_m_plus, R.id.btn_mr);

        // Additional buttons
        setClickListener(R.id.btn_second);
    }

    private void setClickListener(int... ids) {
        for (int id : ids) {
            View view = findViewById(id);
            if (view != null) {
                view.setOnClickListener(this);
            }
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        try {
            if (id == R.id.btn_0 || id == R.id.btn_1 || id == R.id.btn_2 ||
                    id == R.id.btn_3 || id == R.id.btn_4 || id == R.id.btn_5 ||
                    id == R.id.btn_6 || id == R.id.btn_7 || id == R.id.btn_8 || id == R.id.btn_9) {
                handleNumber(((Button) view).getText().toString());

            } else if (id == R.id.btn_add || id == R.id.btn_subtract ||
                    id == R.id.btn_multiply || id == R.id.btn_divide) {
                handleOperator(((Button) view).getText().toString());

            } else if (id == R.id.btn_decimal) {
                handleDecimal();

            } else if (id == R.id.btn_equals) {
                handleEquals();

            } else if (id == R.id.btn_ac) {
                handleAllClear();

            } else if (id == R.id.btn_sign) {
                handleSignChange();

            } else if (id == R.id.btn_percent) {
                handlePercent();

            } else if (id == R.id.btn_open_bracket) {
                handleInput("(");

            } else if (id == R.id.btn_close_bracket) {
                handleInput(")");

            } else if (id == R.id.btn_square) {
                handleFunctionInput("square");

            } else if (id == R.id.btn_cube) {
                handleFunctionInput("cube");

            } else if (id == R.id.btn_power) {
                handleOperator("^");

            } else if (id == R.id.btn_sqrt) {
                handleFunctionInput(isSecondFunction ? "cbrt" : "sqrt");

            } else if (id == R.id.btn_cube_root) {
                handleFunctionInput("cbrt");

            } else if (id == R.id.btn_y_root) {
                handleOperator("√");

            } else if (id == R.id.btn_reciprocal) {
                handleFunctionInput("reciprocal");

            } else if (id == R.id.btn_factorial) {
                handleFunctionInput("factorial");

            } else if (id == R.id.btn_sin) {
                handleFunctionInput(isSecondFunction ? "asin" : "sin");

            } else if (id == R.id.btn_cos) {
                handleFunctionInput(isSecondFunction ? "acos" : "cos");

            } else if (id == R.id.btn_tan) {
                handleFunctionInput(isSecondFunction ? "atan" : "tan");

            } else if (id == R.id.btn_sinh) {
                handleFunctionInput(isSecondFunction ? "asinh" : "sinh");

            } else if (id == R.id.btn_cosh) {
                handleFunctionInput(isSecondFunction ? "acosh" : "cosh");

            } else if (id == R.id.btn_tanh) {
                handleFunctionInput(isSecondFunction ? "atanh" : "tanh");

            } else if (id == R.id.btn_ln) {
                handleFunctionInput(isSecondFunction ? "exp" : "ln");

            } else if (id == R.id.btn_log) {
                handleFunctionInput(isSecondFunction ? "pow10" : "log");

            } else if (id == R.id.btn_exp) {
                handleFunctionInput("exp");

            } else if (id == R.id.btn_ten_power) {
                handleFunctionInput("pow10");

            } else if (id == R.id.btn_pi) {
                handleConstant(String.valueOf(Math.PI));

            } else if (id == R.id.btn_e) {
                handleConstant(String.valueOf(Math.E));

            } else if (id == R.id.btn_rand) {
                handleConstant(String.valueOf(new Random().nextDouble()));

            } else if (id == R.id.btn_ee) {
                handleInput("E");

            } else if (id == R.id.btn_rad) {
                toggleAngleMode();

            } else if (id == R.id.btn_second) {
                toggleSecondFunction();

            } else if (id == R.id.btn_mc) {
                memoryValue = 0;

            } else if (id == R.id.btn_m_plus) {
                handleMemoryAdd();

            } else if (id == R.id.btn_mr) {
                handleMemoryRecall();
            }

            isSecondFunction = false; // Reset after any operation

        } catch (Exception e) {
            displayError(MATH_ERROR);
        }
    }

    private void handleNumber(String number) {
        if (isResultShown || currentInput.equals("0") || currentInput.equals(MATH_ERROR) || currentInput.equals(SYNTAX_ERROR)) {
            currentInput = number;
            isResultShown = false;
        } else {
            currentInput += number;
        }
        updateDisplay();
    }

    private void handleOperator(String operator) {
        if (isResultShown) {
            isResultShown = false;
        }

        if (currentInput.equals(MATH_ERROR) || currentInput.equals(SYNTAX_ERROR)) {
            return;
        }

        // Convert display operators to calculation operators
        String calcOperator = operator;
        if (operator.equals("×")) calcOperator = "*";
        else if (operator.equals("÷")) calcOperator = "/";

        // Remove trailing operators before adding new one
        currentInput = removeTrailingOperators(currentInput);

        // Don't add operator if the last character is already an operator (except for negative numbers)
        if (!currentInput.isEmpty() && !isOperator(currentInput.charAt(currentInput.length() - 1))) {
            currentInput += calcOperator;
        } else if (operator.equals("-") && (currentInput.isEmpty() || currentInput.endsWith("(") || isOperator(currentInput.charAt(currentInput.length() - 1)))) {
            currentInput += "-";
        }

        updateDisplay();
    }

    private void handleDecimal() {
        if (isResultShown) {
            currentInput = "0.";
            isResultShown = false;
        } else {
            // More robust decimal handling
            String lastToken = getLastToken(currentInput);

            if (!lastToken.contains(".")) {
                if (currentInput.isEmpty() || isOperator(currentInput.charAt(currentInput.length() - 1)) ||
                        currentInput.endsWith("(")) {
                    currentInput += "0.";
                } else {
                    currentInput += ".";
                }
            }
        }
        updateDisplay();
    }

    private void handleEquals() {
        if (currentInput.equals(MATH_ERROR) || currentInput.equals(SYNTAX_ERROR)) {
            return;
        }

        try {
            String result = evaluateExpression(currentInput);
            currentInput = result;
            isResultShown = true;
            updateDisplay();
        } catch (Exception e) {
            displayError(MATH_ERROR);
        }
    }

    private void handleAllClear() {
        currentInput = "0";
        isResultShown = false;
        isSecondFunction = false;
        updateDisplay();
    }

    private void handleSignChange() {
        if (currentInput.equals("0") || currentInput.equals(MATH_ERROR) || currentInput.equals(SYNTAX_ERROR)) {
            return;
        }

        try {
            // Handle sign change more intelligently
            if (isResultShown || isNumeric(currentInput)) {
                double value = Double.parseDouble(currentInput);
                value = -value;
                currentInput = formatResult(value);
                updateDisplay();
            } else {
                // For complex expressions, wrap in brackets and negate
                currentInput = "-(" + currentInput + ")";
                updateDisplay();
            }
        } catch (NumberFormatException e) {
            // For complex expressions, add/remove negative at the beginning
            if (currentInput.startsWith("-")) {
                currentInput = currentInput.substring(1);
            } else {
                currentInput = "-" + currentInput;
            }
            updateDisplay();
        }
    }

    private void handlePercent() {
        try {
            if (isResultShown || isNumeric(currentInput)) {
                double value = Double.parseDouble(currentInput);
                value = value / 100;
                currentInput = formatResult(value);
                isResultShown = true;
                updateDisplay();
            } else {
                // Handle percent in complex expressions
                String result = evaluateExpression(currentInput);
                double value = Double.parseDouble(result) / 100;
                currentInput = formatResult(value);
                isResultShown = true;
                updateDisplay();
            }
        } catch (Exception e) {
            displayError(MATH_ERROR);
        }
    }

    private void handleInput(String input) {
        if (isResultShown && !input.equals("(") && !input.equals(")")) {
            currentInput = input;
            isResultShown = false;
        } else {
            if (currentInput.equals("0") && !input.equals(")")) {
                currentInput = input;
            } else {
                // Improved bracket handling
                if (input.equals(")")) {
                    // Only add closing bracket if there are unmatched opening brackets
                    if (getOpenBracketCount(currentInput) > getCloseBracketCount(currentInput)) {
                        currentInput += input;
                    }
                } else {
                    currentInput += input;
                }
            }
        }
        updateDisplay();
    }

    // NEW: Handle function input (function first, then value)
    private void handleFunctionInput(String function) {
        if (isResultShown) {
            // If showing result, wrap it in the function
            try {
                double value = Double.parseDouble(currentInput);
                double result = calculateFunction(function, value);
                currentInput = formatResult(result);
                updateDisplay();
            } catch (Exception e) {
                displayError(MATH_ERROR);
            }
        } else {
            // Add function to current expression
            if (currentInput.equals("0")) {
                currentInput = function + "(";
            } else {
                // Add multiplication if needed before function
                char lastChar = currentInput.charAt(currentInput.length() - 1);
                if (Character.isDigit(lastChar) || lastChar == ')') {
                    currentInput += "*" + function + "(";
                } else {
                    currentInput += function + "(";
                }
            }
            updateDisplay();
        }
    }

    private void handleConstant(String constant) {
        String formattedConstant = formatResult(Double.parseDouble(constant));

        if (isResultShown || currentInput.equals("0")) {
            currentInput = formattedConstant;
            isResultShown = false;
        } else {
            // Add multiplication if needed before constant
            char lastChar = currentInput.charAt(currentInput.length() - 1);
            if (Character.isDigit(lastChar) || lastChar == ')') {
                currentInput += "*" + formattedConstant;
            } else {
                currentInput += formattedConstant;
            }
        }
        updateDisplay();
    }

    private void handleMemoryAdd() {
        try {
            double value = isResultShown ? Double.parseDouble(currentInput) :
                    Double.parseDouble(evaluateExpression(currentInput));
            memoryValue += value;
        } catch (Exception e) {
            displayError(MATH_ERROR);
        }
    }

    private void handleMemoryRecall() {
        if (currentInput.equals("0") || isResultShown) {
            currentInput = formatResult(memoryValue);
        } else {
            // Add multiplication if needed
            char lastChar = currentInput.charAt(currentInput.length() - 1);
            if (Character.isDigit(lastChar) || lastChar == ')') {
                currentInput += "*" + formatResult(memoryValue);
            } else {
                currentInput += formatResult(memoryValue);
            }
        }
        isResultShown = false;
        updateDisplay();
    }

    private void toggleAngleMode() {
        isRadianMode = !isRadianMode;
        Button radButton = findViewById(R.id.btn_rad);
        if (radButton != null) {
            radButton.setText(isRadianMode ? "Rad" : "Deg");
        }
    }

    private void toggleSecondFunction() {
        isSecondFunction = !isSecondFunction;
        // Update button labels to show second functions
        updateSecondFunctionLabels();
    }

    private void updateSecondFunctionLabels() {
        // Update button text to show second functions
        Button sinBtn = findViewById(R.id.btn_sin);
        Button cosBtn = findViewById(R.id.btn_cos);
        Button tanBtn = findViewById(R.id.btn_tan);
        Button lnBtn = findViewById(R.id.btn_ln);
        Button logBtn = findViewById(R.id.btn_log);

        if (sinBtn != null) sinBtn.setText(isSecondFunction ? "asin" : "sin");
        if (cosBtn != null) cosBtn.setText(isSecondFunction ? "acos" : "cos");
        if (tanBtn != null) tanBtn.setText(isSecondFunction ? "atan" : "tan");
        if (lnBtn != null) lnBtn.setText(isSecondFunction ? "exp" : "ln");
        if (logBtn != null) logBtn.setText(isSecondFunction ? "10^x" : "log");
    }

    private double calculateFunction(String function, double value) throws Exception {
        switch (function) {
            case "square":
                return value * value;
            case "cube":
                return value * value * value;
            case "sqrt":
                if (value < 0) throw new Exception("Square root of negative number");
                return Math.sqrt(value);
            case "cbrt":
                return Math.cbrt(value);
            case "reciprocal":
                if (value == 0) throw new Exception("Division by zero");
                return 1.0 / value;
            case "factorial":
                return calculateFactorial(value);
            case "sin":
                return Math.sin(isRadianMode ? value : Math.toRadians(value));
            case "cos":
                return Math.cos(isRadianMode ? value : Math.toRadians(value));
            case "tan":
                double angle = isRadianMode ? value : Math.toRadians(value);
                double result = Math.tan(angle);
                // Check for tan approaching infinity (near π/2, 3π/2, etc.)
                if (Math.abs(result) > 1e14) {
                    throw new Exception("Tan undefined");
                }
                return result;
            case "asin":
                if (value < -1 || value > 1) throw new Exception("asin domain error");
                double asinResult = Math.asin(value);
                return isRadianMode ? asinResult : Math.toDegrees(asinResult);
            case "acos":
                if (value < -1 || value > 1) throw new Exception("acos domain error");
                double acosResult = Math.acos(value);
                return isRadianMode ? acosResult : Math.toDegrees(acosResult);
            case "atan":
                double atanResult = Math.atan(value);
                return isRadianMode ? atanResult : Math.toDegrees(atanResult);
            case "sinh":
                return Math.sinh(value);
            case "cosh":
                return Math.cosh(value);
            case "tanh":
                return Math.tanh(value);
            case "asinh":
                return Math.log(value + Math.sqrt(value * value + 1));
            case "acosh":
                if (value < 1) throw new Exception("acosh domain error");
                return Math.log(value + Math.sqrt(value * value - 1));
            case "atanh":
                if (Math.abs(value) >= 1) throw new Exception("atanh domain error");
                return 0.5 * Math.log((1 + value) / (1 - value));
            case "ln":
                if (value <= 0) throw new Exception("Logarithm of non-positive number");
                return Math.log(value);
            case "log":
                if (value <= 0) throw new Exception("Logarithm of non-positive number");
                return Math.log10(value);
            case "exp":
                if (value > 700) throw new Exception("Exponential overflow");
                return Math.exp(value);
            case "pow10":
                if (value > 300) throw new Exception("10^x overflow");
                return Math.pow(10, value);
            default:
                throw new Exception("Unknown function");
        }
    }

    private double calculateFactorial(double value) throws Exception {
        if (value < 0 || value != Math.floor(value)) {
            throw new Exception("Factorial of non-integer or negative number");
        }
        if (value > 170) {
            throw new Exception("Factorial too large");
        }

        double result = 1;
        for (int i = 2; i <= value; i++) {
            result *= i;
        }
        return result;
    }

    private String evaluateExpression(String expression) throws Exception {
        if (expression == null || expression.trim().isEmpty()) {
            return "0";
        }

        // Preprocess expression
        expression = preprocessExpression(expression);

        // Remove trailing operators
        expression = removeTrailingOperators(expression);

        // Check for balanced brackets - IMPROVED VERSION
        BracketValidation validation = validateBrackets(expression);
        if (!validation.isValid) {
            throw new Exception(validation.error);
        }

        try {
            double result = evaluateWithShuntingYard(expression);
            return formatResult(result);
        } catch (Exception e) {
            throw new Exception("Invalid expression: " + e.getMessage());
        }
    }

    // NEW: Preprocess expression to handle functions and scientific notation
    private String preprocessExpression(String expression) {
        // Handle scientific notation (E)
        expression = expression.replaceAll("E([+-]?\\d+)", "*10^$1");

        // Handle implicit multiplication before functions
        expression = expression.replaceAll("(\\d)([a-zA-Z])", "$1*$2");

        // Handle function calls - convert function(x) to function x
        Pattern functionPattern = Pattern.compile("(sin|cos|tan|sinh|cosh|tanh|asin|acos|atan|asinh|acosh|atanh|ln|log|exp|sqrt|cbrt|square|cube|reciprocal|factorial|pow10)\\s*\\(");
        Matcher matcher = functionPattern.matcher(expression);

        while (matcher.find()) {
            String function = matcher.group(1);
            int start = matcher.start();
            int openParenPos = matcher.end() - 1;

            // Find matching closing parenthesis
            int closeParenPos = findMatchingCloseParen(expression, openParenPos);
            if (closeParenPos != -1) {
                String innerExpression = expression.substring(openParenPos + 1, closeParenPos);
                String replacement = function + " " + innerExpression;
                expression = expression.substring(0, start) + replacement + expression.substring(closeParenPos + 1);
                matcher = functionPattern.matcher(expression);
            }
        }

        return expression;
    }

    private int findMatchingCloseParen(String expression, int openPos) {
        int count = 1;
        for (int i = openPos + 1; i < expression.length(); i++) {
            char c = expression.charAt(i);
            if (c == '(') count++;
            else if (c == ')') {
                count--;
                if (count == 0) return i;
            }
        }
        return -1;
    }

    // IMPROVED: Better bracket validation
    private static class BracketValidation {
        boolean isValid;
        String error;

        BracketValidation(boolean valid, String err) {
            isValid = valid;
            error = err;
        }
    }

    private BracketValidation validateBrackets(String expression) {
        int openCount = 0;
        int closeCount = 0;

        for (char c : expression.toCharArray()) {
            if (c == '(') {
                openCount++;
            } else if (c == ')') {
                closeCount++;
                // Check if we have more closing than opening at any point
                if (closeCount > openCount) {
                    return new BracketValidation(false, "Unexpected closing bracket");
                }
            }
        }

        if (openCount > closeCount) {
            return new BracketValidation(false, "Missing closing bracket(s)");
        } else if (closeCount > openCount) {
            return new BracketValidation(false, "Too many closing brackets");
        }

        return new BracketValidation(true, "");
    }

    private double evaluateWithShuntingYard(String expression) throws Exception {
        Stack<Double> operands = new Stack<>();
        Stack<String> operators = new Stack<>();

        List<String> tokens = tokenizeExpression(expression);

        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i);

            if (isNumber(token)) {
                operands.push(Double.parseDouble(token));
            } else if (isFunction(token)) {
                // Handle functions with their arguments
                if (i + 1 < tokens.size()) {
                    String nextToken = tokens.get(i + 1);
                    if (isNumber(nextToken)) {
                        double value = Double.parseDouble(nextToken);
                        double result = calculateFunction(token, value);
                        operands.push(result);
                        i++; // Skip the next token as we've consumed it
                    } else {
                        throw new Exception("Function " + token + " requires a numeric argument");
                    }
                } else {
                    throw new Exception("Function " + token + " requires an argument");
                }
            } else if (token.equals("(")) {
                operators.push(token);
            } else if (token.equals(")")) {
                while (!operators.isEmpty() && !operators.peek().equals("(")) {
                    processOperator(operands, operators.pop());
                }
                if (!operators.isEmpty()) {
                    operators.pop(); // Remove the '('
                }
            } else if (isOperator(token)) {
                while (!operators.isEmpty() && !operators.peek().equals("(") &&
                        hasHigherPrecedence(operators.peek(), token)) {
                    processOperator(operands, operators.pop());
                }
                operators.push(token);
            }
        }

        while (!operators.isEmpty()) {
            processOperator(operands, operators.pop());
        }

        if (operands.size() != 1) {
            throw new Exception("Invalid expression");
        }

        return operands.pop();
    }

    // IMPROVED: Better tokenization
    private List<String> tokenizeExpression(String expression) {
        List<String> tokens = new ArrayList<>();
        StringBuilder currentToken = new StringBuilder();

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            if (Character.isWhitespace(c)) {
                if (currentToken.length() > 0) {
                    tokens.add(currentToken.toString());
                    currentToken.setLength(0);
                }
            } else if (isOperatorChar(c) || c == '(' || c == ')') {
                if (currentToken.length() > 0) {
                    tokens.add(currentToken.toString());
                    currentToken.setLength(0);
                }
                tokens.add(String.valueOf(c));
            } else {
                currentToken.append(c);
            }
        }

        if (currentToken.length() > 0) {
            tokens.add(currentToken.toString());
        }

        return tokens;
    }

    private boolean isFunction(String token) {
        return token.equals("sin") || token.equals("cos") || token.equals("tan") ||
                token.equals("sinh") || token.equals("cosh") || token.equals("tanh") ||
                token.equals("asin") || token.equals("acos") || token.equals("atan") ||
                token.equals("asinh") || token.equals("acosh") || token.equals("atanh") ||
                token.equals("ln") || token.equals("log") || token.equals("exp") ||
                token.equals("sqrt") || token.equals("cbrt") || token.equals("square") ||
                token.equals("cube") || token.equals("reciprocal") || token.equals("factorial") ||
                token.equals("pow10");
    }

    private void processOperator(Stack<Double> operands, String operator) throws Exception {
        if (operands.size() < 2) {
            throw new Exception("Insufficient operands for operator: " + operator);
        }

        double b = operands.pop();
        double a = operands.pop();
        double result;

        switch (operator) {
            case "+":
                result = a + b;
                break;
            case "-":
                result = a - b;
                break;
            case "*":
                result = a * b;
                break;
            case "/":
                if (b == 0) throw new Exception("Division by zero");
                result = a / b;
                break;
            case "^":
                result = Math.pow(a, b);
                if (Double.isInfinite(result)) throw new Exception("Power overflow");
                break;
            case "√":
                if (b == 0) throw new Exception("Division by zero in root");
                if (a < 0 && (b % 2 == 0)) throw new Exception("Even root of negative number");
                result = Math.pow(a, 1.0 / b);
                break;
            default:
                throw new Exception("Unknown operator: " + operator);
        }

        operands.push(result);
    }

    private boolean hasHigherPrecedence(String op1, String op2) {
        int prec1 = getPrecedence(op1);
        int prec2 = getPrecedence(op2);
        return prec1 >= prec2;
    }

    private int getPrecedence(String operator) {
        switch (operator) {
            case "+":
            case "-":
                return 1;
            case "*":
            case "/":
                return 2;
            case "^":
            case "√":
                return 3;
            default:
                return 0;
        }
    }

    private boolean isOperator(String token) {
        return token.equals("+") || token.equals("-") || token.equals("*") ||
                token.equals("/") || token.equals("^") || token.equals("√");
    }

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^' || c == '√';
    }

    private boolean isOperatorChar(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^' || c == '√';
    }

    private boolean isNumber(String token) {
        if (token == null || token.isEmpty()) return false;
        try {
            Double.parseDouble(token);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) return false;
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private String getLastToken(String expression) {
        if (expression == null || expression.isEmpty()) return "";

        // Find the last continuous numeric token
        StringBuilder token = new StringBuilder();
        for (int i = expression.length() - 1; i >= 0; i--) {
            char c = expression.charAt(i);
            if (Character.isDigit(c) || c == '.') {
                token.insert(0, c);
            } else {
                break;
            }
        }
        return token.toString();
    }

    private int getOpenBracketCount(String expression) {
        int count = 0;
        for (char c : expression.toCharArray()) {
            if (c == '(') count++;
        }
        return count;
    }

    private int getCloseBracketCount(String expression) {
        int count = 0;
        for (char c : expression.toCharArray()) {
            if (c == ')') count++;
        }
        return count;
    }

    private String removeTrailingOperators(String expression) {
        if (expression == null || expression.isEmpty()) return expression;

        while (!expression.isEmpty() && isOperatorChar(expression.charAt(expression.length() - 1))) {
            expression = expression.substring(0, expression.length() - 1);
        }
        return expression;
    }

    private String formatResult(double result) {
        if (Double.isNaN(result) || Double.isInfinite(result)) {
            return MATH_ERROR;
        }

        // Handle very small numbers close to zero
        if (Math.abs(result) < 1e-15) {
            result = 0;
        }

        // Use BigDecimal for precise formatting
        BigDecimal bd = new BigDecimal(result, PRECISION);
        bd = bd.stripTrailingZeros();

        // Use scientific notation for very large or small numbers
        if (Math.abs(result) >= 1e12 || (Math.abs(result) < 1e-6 && result != 0)) {
            return String.format("%.6E", result);
        }

        // For normal numbers, use plain string representation
        String plain = bd.toPlainString();

        // Limit the length of the result to prevent display overflow
        if (plain.length() > 12 && !plain.contains("E")) {
            return String.format("%.6E", result);
        }

        return plain;
    }

    private void displayError(String error) {
        currentInput = error;
        isResultShown = true;
        updateDisplay();
    }

    private void updateDisplay() {
        // Ensure display shows readable format
        String displayText = currentInput;

        // Replace operators with display-friendly versions
        displayText = displayText.replace("*", "×");
        displayText = displayText.replace("/", "÷");

        display.setText(displayText);
        display.setSelection(display.getText().length());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("currentInput", currentInput);
        outState.putDouble("memoryValue", memoryValue);
        outState.putBoolean("isRadianMode", isRadianMode);
        outState.putBoolean("isResultShown", isResultShown);
        outState.putBoolean("isSecondFunction", isSecondFunction);
    }

    private void restoreState(Bundle savedInstanceState) {
        currentInput = savedInstanceState.getString("currentInput", "0");
        memoryValue = savedInstanceState.getDouble("memoryValue", 0);
        isRadianMode = savedInstanceState.getBoolean("isRadianMode", true);
        isResultShown = savedInstanceState.getBoolean("isResultShown", false);
        isSecondFunction = savedInstanceState.getBoolean("isSecondFunction", false);

        updateDisplay();

        Button radButton = findViewById(R.id.btn_rad);
        if (radButton != null) {
            radButton.setText(isRadianMode ? "Rad" : "Deg");
        }

        updateSecondFunctionLabels();
    }
}









