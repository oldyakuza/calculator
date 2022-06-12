package Calculator;

import java.util.Scanner;

public class Main {
    enum RomanNums {
        I(1), II(2), III(3), IV(4), V(5), VI(6), VII(7), VIII(8), IX(9), X(10);

        private final int arabicValue;
        RomanNums(int arabicValue){
            this.arabicValue = arabicValue;
        }
        public int getArabicValue(){
            return arabicValue;
        }
    }
    public static String calc(String inputStr) throws MyException {
        inputStr = inputStr.replace(" ", ""); // Удаляем пробелы

        String[] inputArr = new String[]{inputStr};

        String leftOp = getOperand(inputArr);

        inputStr = inputArr[0];
        if (inputStr.length() == 0) {
            System.out.println(leftOp);
            throw new MyException("Строка не является математической операцией");
        }

        char operator = inputStr.charAt(0);

        inputStr = inputStr.substring(1);
        inputArr[0] = inputStr;
        String rightOp = getOperand(inputArr);
        inputStr = inputArr[0];

        if (leftOp.contains("+") || (leftOp.contains("-") && !(leftOp.charAt(0) == '-'))) { // Прим. (X+Y)/Z
            throw new MyException("Поддерживаются только два операнда и один оператор (+, -, /, *)");
        }
        if (rightOp.contains("+") || (rightOp.contains("-") && !(rightOp.charAt(0) == '-'))) { // Прим. X/(Y-Z)
            throw new MyException("Поддерживаются только два операнда и один оператор (+, -, /, *)");
        }
        if ( !(inputStr.equals("")) ) { // Если остались операции, прим. если задать X+Y+Z, то input == +Z
            throw new MyException("Поддерживаются только два операнда и один оператор (+, -, /, *)");
        }
        // Удалим возможные знаки минуса перед операндом и сохраним их общее значение в переменной sign для дальнейшего расчета
        int sign1 = 1;
        int sign2 = 1;
        if ( leftOp.startsWith("-") ) {
            sign1 = -1;
            leftOp = leftOp.substring(1);
        }
        if ( rightOp.startsWith("-") ) {
            sign2 = -1;
            rightOp = rightOp.substring(1);
        }

        if (isRoman(leftOp) && isInteger(rightOp) || isInteger(leftOp) && isRoman(rightOp)) {
            throw new MyException("Не допускается использование одновременно разных систем исчисления ");
        }
        if ( !(isRoman(leftOp) || isInteger(leftOp)) ) {
            throw new MyException("Первый операнд не является ни арабским числом, ни числом в римской СИ");
        }
        if ( !(isRoman(rightOp) || isInteger(rightOp)) ) {
            throw new MyException("Второй операнд не является ни арабским числом, ни числом в римской СИ");
        }

        String resultStr;
        resultStr = doCalc(leftOp,rightOp,operator,sign1,sign2);
        return resultStr;
    }
    private static String getOperand(String[] inputArr) {
        String str;
        if (inputArr[0].startsWith("(")){
            int opened = 1;
            int i = 1;
            while (opened != 0) {
                if (inputArr[0].charAt(i) == ')') {
                    opened--;
                }
                i++;
            }
            str = inputArr[0].substring(1,i-1);
            inputArr[0] = inputArr[0].substring(i);

        } else {
            int i = 1;
            if (inputArr[0].charAt(0)== '-') {
                i++;
            }
            while (inputArr[0].length() > i && (Character.isDigit(inputArr[0].charAt(i)) || isRoman(Character.toString(inputArr[0].charAt(i))))) {
                i++;
            }
            str = inputArr[0].substring(0, i);
            inputArr[0] = inputArr[0].substring(i);
        }
        return str;
    }
    private static boolean isRoman(String str) {
        for (RomanNums rnum: RomanNums.values()){
            if (rnum.toString().equals(str.toUpperCase())) {
                return true;
            }
        }
        return false;
    }
    private static boolean isInteger(String str){
        for (int i=0; i<str.length(); i++){
            if ( !Character.isDigit(str.charAt(i)) ) {
                return false;
            }
        }
        return true;
    }
    private static int getArabic(String str) {
        for (RomanNums rnum: RomanNums.values()){
            if (rnum.toString().equals(str.toUpperCase())) {
                return rnum.getArabicValue();
            }
        }
        return Integer.parseInt(str);
    }
    private static String doCalc(String opnd1, String opnd2, char operator, int sign1, int sign2) throws MyException{
        boolean expRoman = false;
        int result;
        if (isRoman(opnd1)) {
            expRoman=true;
        }
        switch (operator) {
            case '+':
                result = getArabic(opnd1) * sign1 + getArabic(opnd2) * sign2;
                break;
            case '-':
                result = getArabic(opnd1) * sign1 - getArabic(opnd2) * sign2;
                break;
            case '*':
                result = getArabic(opnd1) * sign1 * getArabic(opnd2) * sign2;
                break;
            case '/':
                if (getArabic(opnd2) == 0) {
                    throw new MyException("На 0 делить нельзя");
                }
                result = getArabic(opnd1) * sign1 / getArabic(opnd2) * sign2;
                break;
            default:
                throw new MyException("Поддерживаются только следующие операции: [+, -, *, /]");
        }
        if ((expRoman) && result <1) {
            throw new MyException("Результатом работы с римскими числами могут быть только положительные числа");
        }
        if (expRoman) {
            return toRoman(result);
        }
        return ""+result;
    }

    private static String toRoman(int num){
        String str = "";
        // Т.к. в нашем калькуляторе используются числа 1-10, то выше результат вычислений не может быть выше 100
        while (num == 100) {
            str += "C";
            num -= 100;
        }
        while (num >= 90) {
            str += "XC";
            num -= 90;
        }
        while (num >= 50) {
               str += "L";
               num -= 50;
        }
        while (num >= 40) {
            str += "XL";
            num -= 40;
        }
        while (num >= 10) {
            str += "X";
            num -= 10;
        }
        while (num >= 9) {
            str += "IX";
            num -= 9;
        }
        while (num >=  5) {
            str += "V";
            num -= 5;
        }
        while (num >= 4) {
            str += "IV";
            num -= 4;
        }
        while (num >= 1) {
            str += "I";
            num -= 1;
        }
        return str;
    }


    public static void main(String[] args) throws MyException{
        while (true) {
            Scanner scan = new Scanner(System.in);
            String inputString = scan.nextLine();
            if (inputString.equals("Стоп")) {
                System.out.println("Завершаю работу");
                break;
            }
            System.out.println(calc(inputString));
        }
    }
}

class MyException extends Exception {
    public MyException(String description){
        super(description);
    }
}