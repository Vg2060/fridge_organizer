package frontend;

import java.util.ArrayList;

interface Report{
    ArrayList<Integer> fridgeId();
    ArrayList<String>  fridgeName();
    void generateReport();
}