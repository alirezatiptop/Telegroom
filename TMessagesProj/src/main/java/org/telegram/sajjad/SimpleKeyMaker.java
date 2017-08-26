package org.telegram.sajjad;

import java.util.Random;

/**
 * Created by sajjadlp on 8/24/2017.
 */

public class SimpleKeyMaker {

    public static String makeDigitString() {
        int[] x = new int[11];
        Random random = new Random();
        for (int i=0 ; i<11; i++ )
            x[i] = random.nextInt(9)+1;

        if (random.nextDouble()>0.5){
            x[0]=4;
            x[6]=2;
        } else{
            x[7]=9;
            x[3]=0;
        }

        long result = 0;
        for (int i=10 ; i>=0; i-- )
            result = Math.pow(7,i);

        return result;
    }
}