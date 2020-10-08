import java.util.Scanner;

public class Iterative {

    /** Do not edit this function */
    public static void main(String[] args) {
        Iterative iter = new Iterative();

        iter.a();

        iter.getInput();

        iter.b(0, 10);

        iter.c();
    }

    /**
     * Part A.
     * The following method does not compile correctly.
     * Identify the source of the error and correct it such
     * that the function performs the desired function.
     */
    public int a() {
        /** ANSWER */

        int i = 0;
        while (i < 10) {
            int i = i+1;
        }

        /** END */
        return checkA(i);
    }

    /**
     * Part B. (i)
     * Fill in the functions, getInput() and b().
     * The getInput() function should prompt the user for a
     * minimum number and a maximum number, and then call
     * the b() function with those two numbers.
     */
    public int getInput() {
        /** ANSWER */

        int min = 0;
        int max = 0;

        /** END */
        return b(min, max);
    }
    /**
     * Part B. (ii)
     * The b() function should sum the numbers between min
     * and max, inclusively, storing the result in a variable
     * called sum.
     *
     * Note: You may assume that max is always greater than min.
     */
    public int b(int min, int max) {
        /** ANSWER */



        /** END */
        return checkB(min, max, sum);
    }

    /**
     * Part C.
     * The code below contains a *semantic* error.
     * Identify the issue and correct it.
     */
    public int c() {
        /** ANSWER */

        int a = 6;
        int b = 9;
        int c = 12;

        // Calculate the average of a, b and c
        int avg = a + b + c / 3;

        /** ANSWER */

        return checkC(a, b, c, avg);

    }

    /** Do not edit the code beyond this point */
    public int checkA(int i) {
        System.out.println("Part A");
        System.out.println("i = " + i);
        return 0;
    }

    public int checkB(int min, int max, int sum) {
        System.out.println("Part B");
        System.out.println("min = " + min);
        System.out.println("max = " + max);
        System.out.println("sum = " + sum);
        return 0;
    }

    public int checkC(int a, int b, int c, int avg) {
        System.out.println("Part C");
        System.out.println("a = " + a);
        System.out.println("b = " + b);
        System.out.println("c = " + c);
        System.out.println("avg = " + avg);
        return 0;
    }
}
