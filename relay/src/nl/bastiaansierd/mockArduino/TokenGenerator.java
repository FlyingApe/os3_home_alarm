package nl.bastiaansierd.mockArduino;

import java.util.Random;

class TokenGenerator {
    private static TokenGenerator instance = null;

    static TokenGenerator getInstance(){
        if (instance == null){
            instance = new TokenGenerator();
        }
        return instance;
    }

    String getToken(){
        int numchars = 16;

        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        while(sb.length() < numchars){
            sb.append(Integer.toHexString(r.nextInt()));
        }

        return sb.toString().substring(0, numchars);
    }
}
