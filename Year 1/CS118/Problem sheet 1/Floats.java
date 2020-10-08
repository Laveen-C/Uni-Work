import java.lang.Math;

public class Floats {

    /** Do not edit this function */
    public static void main(String[] args) {
        Floats floats = new Floats();

        floats.a();

        floats.b();

    }

    /**
     * Part A.
     * In Java there are two primitive variable types for storing decimal
     * numbers. Create two variables (named var1 and var2) where one is of
     * each decimal number type. Assign the value 0.0 to each of them.
     *
     * Note: You may need to use "0.0f" for your code to compile
     */
    public int a() {
        /** ANSWER */

        float var1 = 0.0f;
        double var2 = 0.0;

        /** END */
        return checkA(var1, var2);
    }

    /**
     * Part B.
     * Given the following binary number stored in IEEE-754 number format,
     * calculate the exponent and the fraction and create 2 variables,
     * exponent and fraction, that stores each part used in the calculation.
     * Finally, create a floating point variable, result, that stores the
     * result of the computation (i.e. the value of the binary number).
     *
     * 0100 0101 1110 1100 0000 0000 0000 0000
     *
     * NOTE: Using the notation 0.000f forces Java to treat the value
     *       as a float (because of the trailing "f").
     */
    public int b() {
        /** ANSWER */

        /**
         * IEEE-754 format:
         *  - 0 = sign
         *  - 1 to 8 = Exponent
         *  - 9 to 23 = Mantissa
        */

        String num = "01000101111011000000000000000000";

        int sign = Integer.parseInt(num.substring(0,1)); //Converting string value into integer
        String expB = num.substring(1, 9); //Storing the exponent in binary as a string
        String manB = num.substring(9, 32); //Storing the mantissa in binary as a string

        //Converting expB into denary
        int expD = Integer.parseInt(expB, 2) - 127; //Subtracting exponent bias
        System.out.println(expD);

        //Converting manB into denary
        float manD = 1f; //We add 1 to whatever the result of the mantissa conversion gives

        int power = -1;
        for (int i = 0; i < manB.length(); i++) {
            if (manB.substring(i, i+1).equals("1")){
                manD = manD + (float) Math.pow(2, power);
            }
            power--;
        }

        if (sign == 1) {
            manD = -1*manD;
        }

        int exponent = expD;
        float fraction = manD; 
        float result = manD * (float) Math.pow(2, expD);

        /** END */
        return checkB(exponent, fraction, result);
    }

    /** Do not edit the code beyond this point */
    public int checkA(Object var1, Object var2) {
        System.out.println("Part A");
        System.out.println("var1 = " + var1);
        System.out.println("var2 = " + var2);

        return 0;
    }

    public int checkB(int exponent, float fraction, float result) {
        System.out.println("Part B");
        System.out.println("exponent = " + exponent);
        System.out.println("fraction = " + fraction);
        System.out.println("result = " + result);

        return 0;
    }
}
