public class Integers {

    /** Do not edit this function */
    public static void main(String[] args) {
        Integers ints = new Integers();

        ints.a();

        ints.b();

        ints.c();

        ints.d();
    }

    /**
     * Part A.
     * Besides "int", there are 3 primitive Integer types in Java.
     * Create 3 variables (named var1, var2, and var3) where there
     * is one of each *integer* type (not including int). Assign them
     * each the value 0.
     **/
    public int a() {
        /** ANSWER */




        /** END */
        return checkA(var1, var2, var3);
    }

    /**
     * Part B.
     * The primitive types all have a fixed expressible range.
     * Create 2 variables of each type (names typename_min and typename_max, etc)
     * that contain the values of the minimum and the maximum value that type can hold.
     * The minimum and maximum values for an int have been provided for you, along with
     * an example of the checkB function. Complete the remainder of the check functions.
     **/
    public int b() {
        int int_min = -2147483648;
        int int_max = 2147483647;
        /** ANSWER */


        /** END */
        int sum = 0;
        sum += checkB(int_min, int_max);
        sum += checkB(/** COMPLETE ME */);
        sum += checkB(/** COMPLETE ME */);
        sum += checkB(/** COMPLETE ME */);
        return sum;
    }

    /**
     * Part C.
     * Integers in Java are stored using two's complement.
     * Given the binary number 1000 0000 0000 0000 0000 0000 1100 1101
     * modify the code below to return the correct number in decimal.
     * Note: You are not expected to write code to convert the number;
     * you simply need to calculate the integer value for the binary number
     * above and modify the code to store that number.
     */
    public int c() {
        /** ANSWER */

        // For example:
        int ans = -10;

        /** END */
        return checkC(ans);
    }

    /**
     * Part D.
     * Modify the code below to return the number that represents the binary
     * representation of a 8-bit two's complement number with a value of -32.
     * Note: again, you are not expected to write code to convert the number,
     * simply write in the correct answer in place of the current example.
     */
    public int d() {
        /** ANSWER */

        // For example:
        int ans = 11000110;

        /** END */
        return checkD(ans);
    }

    /** Do not edit the code beyond this point */
    public int checkA(Object var1, Object var2, Object var3) {
        System.out.println("Part A");
        System.out.println("var1 = " + var1);
        System.out.println("var2 = " + var2);
        System.out.println("var3 = " + var3);
        return 0;
    }

    public int checkB(Object var_min, Object var_max) {
        System.out.println("Part B");
        System.out.println("var_min = " + var_min);
        System.out.println("var_max = " + var_max);
        return 0;
    }

    public int checkC(int ans) {
        System.out.println("Part C");
        System.out.println("ans = " + ans);
        return 0;
    }

    public int checkD(int ans) {
        System.out.println("Part D");
        System.out.println("ans = " + ans);
        return 0;
    }
}
