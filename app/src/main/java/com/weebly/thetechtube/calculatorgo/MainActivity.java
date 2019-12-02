package com.weebly.thetechtube.calculatorgo;

/*
 * Copyright 2019 The Tech Tube
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Our current website: https://thetechtube.weebly.com/
 */

import android.app.admin.DeviceAdminInfo;
import android.bluetooth.BluetoothClass;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.util.DeviceProperties;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Objects;

import static java.lang.Math.floorMod;
import static java.lang.Math.sqrt;
import static java.lang.StrictMath.floorDiv;

public class MainActivity extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;
    Button button1;

    TextView mainTextView;
    String firstValue = "";
    String secondValue = "";
    double finalValue = 0;
    boolean firstVal = true;
    boolean firstTime = true;
    String currentOperator = "";
    int TAG;

    TextView buttonClear;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {

            case R.id.advancedMode:
                Toast.makeText(this, "Advanced Mode Coming Soon", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.aboutApp:
                Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
                startActivity(intent);
                return true;

            default:
                return false;
        }

    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        mFirebaseAnalytics.setUserProperty("Manufacturer", Build.MANUFACTURER);
        mFirebaseAnalytics.setUserProperty("Brand", Build.BRAND);
        mFirebaseAnalytics.setUserProperty("Model", Build.MODEL);
        mFirebaseAnalytics.setUserProperty("SDK_Version", String.valueOf(Build.VERSION.SDK_INT));

        // We will be using findViewById to linked back the original
        // activity_main.xml views
        mainTextView = findViewById(R.id.mainTextView);
        buttonClear = findViewById(R.id.buttonClear);
        button1 = findViewById(R.id.button1);

        //BONUS 1
        button1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                mainTextView.setText("Number 1");

                return true;

            }
        });

    }

    public void equals(View view) {

        try {

            if (!firstTime || !firstVal) {

                performTheSelectedSymbol();

            }

            if (firstTime && firstVal) {

                performTheSelectedSymbol();

                //If the first value can be put into an integer than change it
                //For example: 5.0 or else use double
                if (Double.parseDouble(firstValue) - (int) Double.parseDouble(firstValue) == 0) {

                    mainTextView.setText((int) Double.parseDouble(firstValue) + "");

                } else {

                    mainTextView.setText(firstValue);

                }

            } else {

                //Check for which operator is currently using, calculate
                //using the operator amd display the final value to the user
                switch (currentOperator) {

                    case "ADDITION":

                        addition();

                        break;

                    case "SUBTRACTION":

                        subtraction();

                        break;

                    case "MULTIPLICATION":

                        multiplication();

                        break;

                    case "DIVISION":

                        division();

                        break;

                }

                firstValue = "";
                secondValue = "";
                firstTime = false;

                //If the final value can be put into an integer than change it
                //For example: 5.0 or else use double
                if (finalValue - (int) finalValue == 0) {

                    mainTextView.setText((int) finalValue + "");

                } else {

                    mainTextView.setText(finalValue + "");

                }
            }
        } catch (Exception e) {

            e.printStackTrace();
            Log.i("Exception e Message", Objects.requireNonNull(e.getMessage()));
            Toast.makeText(this, "Invalid format used", Toast.LENGTH_SHORT).show();

        }
    }

    public void clear(View view) {

        //Reset all the values back to the default
        firstValue = "";
        secondValue = "";
        finalValue = 0;
        firstVal = true;
        firstTime = true;
        currentOperator = "";
        mainTextView.setText("");

        //BONUS 3
        button1.animate().rotation(360f).setDuration(5000);

    }

    public void delete(View view) {

        //We will be deleting the last letter from a String

        //Try and catch were used to prevent the app to crashed
        //If it receives an error, it will run Catch
        try {

            if (firstVal) {

                firstValue = firstValue.substring(0, firstValue.length() - 1);
                mainTextView.setText(firstValue + "");

            } else {

                secondValue = secondValue.substring(0, secondValue.length() - 1);
                mainTextView.setText(firstValue + "");

            }

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    //When a number is positively valued, change it to negative and vice versa
    public void changeNumberToPositiveOrNegative(View view) {

        if (firstVal) {

            //If negative sign is present, remove it
            if (firstValue.indexOf("-") >= 0) {

                firstValue = firstValue.substring(1);
                mainTextView.setText(firstValue + "");

            } else {

                firstValue = "-" + firstValue;
                mainTextView.setText(firstValue + "");

            }

        } else {

            if (secondValue.indexOf("-") >= 0) {

                secondValue = secondValue.substring(1);
                mainTextView.setText(secondValue + "");

            } else {

                secondValue = "-" + secondValue;
                mainTextView.setText(secondValue + "");

            }

        }

    }

    public void clickButton(View view) {

        //Get current TAG
        TAG = Integer.parseInt((String) view.getTag());

        Log.i("Info", "TAG = " + TAG);


        if (TAG < 10) {

            //Log which numbers are been clicked onto Firebase
            Bundle clickButtonBundle = new Bundle();
            clickButtonBundle.putString(FirebaseAnalytics.Param.ITEM_ID, String.valueOf(view.getId()));
            clickButtonBundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Button" + TAG);
            clickButtonBundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Buttons");
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, clickButtonBundle);

            if (firstVal) {

                //Add the TAG value to the string
                firstValue += TAG;
                //And display it
                mainTextView.setText(firstValue + "");

            } else {

                secondValue += TAG;
                mainTextView.setText(secondValue + "");

            }
        }

        try {

            //Adding a decimal point
            // If indexOf(".") returns 0 or more means there is already
            // a decimal point while if it's a -1, it doesn't have a one
            if (TAG == 10) {

                if (firstVal) {

                    //If decimal point is not present, add it
                    if (!(firstValue.indexOf(".") >= 0)) {

                        firstValue += ".";
                        mainTextView.setText(firstValue + "");

                    }

                } else {

                    if (!(secondValue.indexOf(".") >= 0)) {

                        secondValue += ".";
                        mainTextView.setText(secondValue + "");

                    }

                }

            }

            if (TAG == 11 || TAG == 12 || TAG == 13 || TAG == 14) {

                //Log which operators are been clicked onto Firebase
                Bundle operatorBundle = new Bundle();
                operatorBundle.putString(FirebaseAnalytics.Param.ITEM_ID, String.valueOf(view.getId()));
                operatorBundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Button operator" + TAG);
                operatorBundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Operators");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, operatorBundle);

                //Addition
                if (TAG == 11) {

                    if (firstTime) {

                        if (firstVal) {

                            //If firstValue is not equals to blank
                            if (!firstValue.equals("")) {

                                performTheSelectedSymbol();
                                firstVal = false;

                            }

                        } else {

                            if (!secondValue.equals("")) {

                                performTheSelectedSymbol();
                                checkForLastOperator();

                                addition();
                                firstTime = false;
                                firstValue = "";
                                secondValue = "";
                                firstVal = true;

                            }
                        }

                    } else {

                        checkForLastOperator();

                        if (firstVal) {

                            performTheSelectedSymbol();
                            secondValue = "";
                            addition();
                            firstVal = false;

                        } else {

                            performTheSelectedSymbol();
                            firstValue = "";
                            addition();
                            firstVal = true;

                        }
                    }

                    //The addition button will be tapped so the currentOperator
                    //will be ADDITION
                    currentOperator = "ADDITION";

                }

                //Subtraction
                if (TAG == 12) {

                    if (firstTime) {

                        if (firstVal) {

                            if (!firstValue.equals("")) {

                                performTheSelectedSymbol();
                                firstVal = false;

                            }

                        } else {

                            if (!secondValue.equals("")) {

                                performTheSelectedSymbol();
                                checkForLastOperator();

                                subtraction();
                                firstTime = false;
                                firstValue = "";
                                secondValue = "";
                                firstVal = true;

                            }
                        }

                    } else {

                        checkForLastOperator();

                        if (firstVal) {

                            performTheSelectedSymbol();
                            secondValue = "";
                            subtraction();
                            firstVal = false;

                        } else {

                            performTheSelectedSymbol();
                            firstValue = "";
                            subtraction();
                            firstVal = true;

                        }
                    }

                    currentOperator = "SUBTRACTION";

                }

                //Multiplication
                if (TAG == 13) {

                    if (firstTime) {

                        if (firstVal) {

                            if (!firstValue.equals("")) {

                                performTheSelectedSymbol();
                                firstVal = false;

                            }

                        } else {

                            if (!secondValue.equals("")) {

                                performTheSelectedSymbol();
                                checkForLastOperator();

                                multiplication();
                                firstTime = false;
                                firstValue = "";
                                secondValue = "";
                                firstVal = true;

                            }
                        }

                    } else {

                        checkForLastOperator();

                        if (firstVal) {

                            performTheSelectedSymbol();
                            secondValue = "";
                            multiplication();
                            firstVal = false;

                        } else {

                            performTheSelectedSymbol();
                            firstValue = "";
                            multiplication();
                            firstVal = true;

                        }
                    }

                    currentOperator = "MULTIPLICATION";

                }

                //Division
                if (TAG == 14) {

                    if (firstTime) {

                        if (firstVal) {

                            if (!firstValue.equals("")) {

                                performTheSelectedSymbol();
                                firstVal = false;

                            }

                        } else {

                            if (!secondValue.equals("")) {

                                performTheSelectedSymbol();
                                checkForLastOperator();

                                division();
                                firstTime = false;
                                firstValue = "";
                                secondValue = "";
                                firstVal = true;

                            }
                        }

                    } else {

                        checkForLastOperator();

                        if (firstVal) {

                            performTheSelectedSymbol();
                            secondValue = "";
                            division();
                            firstVal = false;

                        } else {

                            performTheSelectedSymbol();
                            firstValue = "";
                            division();
                            firstVal = true;

                        }
                    }

                    currentOperator = "DIVISION";

                }
            }

            //Percentage
            if (TAG == 15) {

                   // Toast.makeText(this, "This feature is still under testing phase", Toast.LENGTH_SHORT).show();

                    if (firstVal) {

                        if (!firstValue.isEmpty() && !firstValue.contains("%")) {

                            //Add the TAG value to the string
                            firstValue += "%";
                            //And display it
                            mainTextView.setText(firstValue + "");

                        }

                    } else {

                        if (!secondValue.isEmpty() && !secondValue.contains("%")) {

                            secondValue += "%";
                            mainTextView.setText(secondValue + "");

                        }
                    }

            }

            //Square root
            if (TAG == 16) {

                if (firstVal) {

                    if (!firstValue.startsWith("√")) {

                        //Add the TAG value to the string
                        firstValue += "√";
                        //And display it
                        mainTextView.setText(firstValue + "");

                    }

                } else {

                    if (!secondValue.startsWith("√")) {

                        secondValue += "√";
                        mainTextView.setText(secondValue + "");

                    }
                }

            }

        } catch (Exception e) {

            e.printStackTrace();
            Log.i("Exception e Message", Objects.requireNonNull(e.getMessage()));
            Toast.makeText(this, "Invalid format used", Toast.LENGTH_SHORT).show();

        }
    }

    public void performTheSelectedSymbol() {

        if (firstVal) {

            //For Square root
            if (firstValue.startsWith("√") && !firstValue.substring(1).equals("")) {

                firstValue = String.valueOf(sqrt(Double.parseDouble(firstValue.substring(1))));

                Log.i("Square root firstvalue", firstValue);

            } else if (firstValue.indexOf("√") > 0 && !firstValue.substring(firstValue.indexOf("√") + 1).equals("")) {

                firstValue = String.valueOf(Double.parseDouble(firstValue.substring(0, firstValue.indexOf("√"))) * sqrt(Double.parseDouble(firstValue.substring(firstValue.indexOf("√") + 1))));

                Log.i("Square root firstvalue", firstValue);

            }

            //For percentage
            if (firstValue.endsWith("%")) {

                firstValue = String.valueOf(Double.parseDouble(firstValue.substring(0, firstValue.indexOf("%"))) / 100);

            }

        } else {

            if (secondValue.startsWith("√") && !secondValue.substring(1).equals("")) {

                secondValue = String.valueOf(sqrt(Double.parseDouble(secondValue.substring(1))));

                Log.i("Square root secondvalue", secondValue);

            } else if (secondValue.indexOf("√") > 0 && !secondValue.substring(secondValue.indexOf("√") + 1).equals("")) {

                secondValue = String.valueOf(Double.parseDouble(secondValue.substring(0, secondValue.indexOf("√"))) * sqrt(Double.parseDouble(secondValue.substring(secondValue.indexOf("√") + 1))));

                Log.i("Square root secondvalue", secondValue);

            }

            //For percentage
            if (secondValue.endsWith("%")) {

                secondValue = String.valueOf(Double.parseDouble(secondValue.substring(0, secondValue.indexOf("%"))) / 100);

            }


        }

    }

    public void addition() {

        //Check if first or second value are blank then add a 0 to it
        if (!firstTime)  {

            if (firstValue.equals("")) {

                firstValue = "0";

            }

            if (secondValue.equals("")) {

                secondValue = "0";

            }

        }

        try {

            finalValue += Double.parseDouble(firstValue) + Double.parseDouble(secondValue);

            firstValue = "";
            secondValue = "";

        } catch (Exception e) {

            e.printStackTrace();

        }
    }


    public void subtraction() {

        if (!firstTime)  {

            if (firstValue.equals("")) {

                firstValue = "0";

            }

            if (secondValue.equals("")) {

                secondValue = "0";

            }

        }

        try {

            //If the finalValue equals to 0 and the firstTime equals true
            if (finalValue == 0 && firstTime) {

                finalValue = Double.parseDouble(firstValue) - Double.parseDouble(secondValue);

            } else {

                finalValue -= Double.parseDouble(firstValue);

                finalValue -= Double.parseDouble(secondValue);

            }

            firstValue = "";
            secondValue = "";

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    public void multiplication() {

        //Check if first or second value are blank then add a 1 to it
        //If we add 0, the final answer will appear to be 0
        if (!firstTime)  {

            if (firstValue.equals("")) {

                firstValue = "1";

            }

            if (secondValue.equals("")) {

                secondValue = "1";

            }

        }

        try {

            if (finalValue == 0 && firstTime) {

                finalValue = Double.parseDouble(firstValue) * Double.parseDouble(secondValue);

            } else {

                if (firstVal) {
                    finalValue *= Double.parseDouble(firstValue);
                } else {
                    finalValue *= Double.parseDouble(secondValue);
                }
            }

            firstValue = "";
            secondValue = "";

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    public void division() {

        //Check if first or second value are blank then add a 1 to it
        //If we add 0, the final answer will appear to be infinity
        if (!firstTime) {

            if (firstValue.equals("")) {

                firstValue = "1";

            }

            if (secondValue.equals("")) {

                secondValue = "1";

            }

        }

        try {

            if (finalValue == 0 && firstTime) {

                finalValue = Double.parseDouble(firstValue) / Double.parseDouble(secondValue);

            } else {

                if (firstVal) {
                    finalValue /= Double.parseDouble(firstValue);
                } else {
                    finalValue /= Double.parseDouble(secondValue);
                }
            }

            firstValue = "";
            secondValue = "";

        } catch (Exception e) {

            e.printStackTrace();

        }


    }

    //To check if previously used operator exists
    public void checkForLastOperator() {

        if (currentOperator.equals("ADDITION")) {

            addition();
            Log.i("Info addition", String.valueOf(finalValue));

        } else if (currentOperator.equals("SUBTRACTION")) {

            subtraction();
            Log.i("Info subtraction", String.valueOf(finalValue));

        } else if (currentOperator.equals("MULTIPLICATION")) {

            multiplication();
            Log.i("Info multiplication", String.valueOf(finalValue));

        } else if (currentOperator.equals("DIVISION")) {

            division();
            Log.i("Info division", String.valueOf(finalValue));

        }

    }

    //EXTRA FEATURES MAY BE USED FOR FUTURE DEVELOPMENT

    //MODULUS NOT IMPLEENTED
    /**@RequiresApi(api = Build.VERSION_CODES.N)
    public void modulus() {

    //Check if first or second value are blank then add a 0 to it
    if (!firstTime)  {

    if (firstValue.equals("")) {

    firstValue = "1";

    }

    if (secondValue.equals("")) {

    secondValue = "1";

    }

    }

    try {

    if (firstVal && Double.parseDouble(firstValue) - (int) Double.parseDouble(firstValue) == 0) {

    firstValue = String.valueOf((int) Double.parseDouble(firstValue));

    } else if (!firstVal && Double.parseDouble(secondValue) - (int) Double.parseDouble(secondValue) == 0) {

    secondValue = String.valueOf((int) Double.parseDouble(secondValue));

    }

    if (finalValue == 0 && firstTime) {

    finalValue = floorDiv(Integer.parseInt(firstValue), Integer.parseInt(secondValue));

    } else {

    if (firstVal) {
    finalValue = floorDiv((int) finalValue, Integer.parseInt(firstValue));
    } else {
    finalValue = floorDiv((int) finalValue, Integer.parseInt(secondValue));;
    }
    }

    firstValue = "";
    secondValue = "";

    } catch (Exception e) {

    e.printStackTrace();

    }

    }
     **/

}


