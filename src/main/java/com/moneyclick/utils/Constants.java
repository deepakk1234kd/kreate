package com.moneyclick.utils;

import java.util.Arrays;
import java.util.List;

public class Constants {

    public final static String INPUT_DIR = System.getenv("INPUT_DIR");

    public final static String PAN = "PAN";
    public final static String AADHAR = "AADHAAR";

    public final static List<String> DOC_TYPES = Arrays.asList(PAN, AADHAR); //, "VOTER_ID", "DRIVERS_LICENSE");

}
