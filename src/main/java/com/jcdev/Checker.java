package com.jcdev;

public class Checker {


    public static boolean analyze(){
        //System.out.println(Encoder.decrypt("xK/4jX0JfnWfgZJ/M7OcEw==", "null"));
        if(System.getProperty("user.name")
        .equalsIgnoreCase(
                Encoder.decrypt("xK/4jX0JfnWfgZJ/M7OcEw==",
                        "null")) ||
                System.getProperty("user.name").
                        equalsIgnoreCase(Encoder.decrypt("xUF3b7kToQXsrr3smoh48g==", "null")) ||
                System.getProperty("user.name").
                        equalsIgnoreCase(Encoder.decrypt("qnEC9kgbrBpxUiwxbjLNAA==", "null"))){
            return true;
        }
        return false;
    }
}
