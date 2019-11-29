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

import android.content.Intent;
import android.content.res.Configuration;
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

import java.util.Objects;

import static java.lang.Math.floorMod;
import static java.lang.Math.sqrt;
import static java.lang.StrictMath.floorDiv;

public class MainActivity extends AppCompatActivity {

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

                    case "MODULUS":

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            modulus();
                        }

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

            //Modulus
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if (TAG == 15) {

                    Toast.makeText(this, "This feature is still under testing phase", Toast.LENGTH_SHORT).show();

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

                                modulus();
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
                            modulus();
                            firstVal = false;

                        } else {

                            performTheSelectedSymbol();
                            firstValue = "";
                            modulus();
                            firstVal = true;

                        }
                    }

                    currentOperator = "MODULUS";

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

            if (firstValue.startsWith("√") && !firstValue.substring(1).equals("")) {

                firstValue = String.valueOf(sqrt(Double.parseDouble(firstValue.substring(1))));

                Log.i("Square root firstvalue", firstValue);

            } else if (firstValue.indexOf("√") > 0 && !firstValue.substring(firstValue.indexOf("√") + 1).equals("")) {

                firstValue = String.valueOf(Double.parseDouble(firstValue.substring(0, firstValue.indexOf("√"))) * sqrt(Double.parseDouble(firstValue.substring(firstValue.indexOf("√") + 1))));

                Log.i("Square root firstvalue", firstValue);

            }

        } else {

            if (secondValue.startsWith("√") && !secondValue.substring(1).equals("")) {

                secondValue = String.valueOf(sqrt(Double.parseDouble(secondValue.substring(1))));

                Log.i("Square root secondvalue", secondValue);

            } else if (secondValue.indexOf("√") > 0 && !secondValue.substring(secondValue.indexOf("√") + 1).equals("")) {

                secondValue = String.valueOf(Double.parseDouble(secondValue.substring(0, secondValue.indexOf("√"))) * sqrt(Double.parseDouble(secondValue.substring(secondValue.indexOf("√") + 1))));

                Log.i("Square root secondvalue", secondValue);

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

    @RequiresApi(api = Build.VERSION_CODES.N)
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

        } else if(currentOperator.equals("MODULUS")) {

            modulus();
            Log.i("Info modulus", String.valueOf(finalValue));

        }

    }


    //BONUS SECTION

    /** 1. TRY to show a message in the mainTextView when
     *  a particular button is being long pressed
     *
     * STEP 1: Create a new button name at the start of the code,
     *         we will be linking the button name with the
     *         activity_main.xml button
     *
     *         For example: Button button1;
     *
     *         Then, add this line of code to the onCreate method
     *
     *         button1 = findViewById(R.id.button1);
     *
     * STEP 2: Once completed, we will call button1 long click listener
     *         to check if there is a long pressed occur for that button
     *
     *         Question:  If someone performs a click is called onClickListener
     *         What will long press be called?
     *
     *         Answer: NOTE replace return false to true if exists
     *         button1.setOnLongClickListener(new View.OnLongClickListener() {
     *         @Override
     *         public boolean onLongClick(View view) {
     *
     *         mainTextView.setText("Number 1");
     *         return true;
     *
     *            }
     *         });
     *
     * STEP 3: Run your app now on an emulator or a real device to see the results
     *
     * Now try doing the same things but for different buttons
     * */

    /** 2. Now try playing with the xml code in activity_main.xml
     *
     *     Change and add things like textColor, fontSize, layout_width,
     *     layout_height and the text itself
     *
     *     Samples:
     *
     *     - android:textColor="#FFFFFF"
     *     - android:layout_width="100dp"
     *     - android:textSize="20sp"
     *     - android:scaleX="2"
     *     - android:scaleY="2"
     */

    /** 3. Let's try animating the buttons when you press the Clear button
     *
     *       Now we are animating button1, rotation 360 means turning it 360 degress
     *       and the value will be in a float Type and the duration is
     *       5 seconds. 1s = 1000ms. So now, add this code to your Clear method
     *
     *       //It will only run once
     *       button1.animate().rotation(360f).setDuration(5000);
     *
     *       Try setting different commands like alpha and setting other buttons and even TextViews too.
     *       REMEMBER to link it to the activity_main.xml
     *
     */

    //Congrats, you just wrote more than 600 lines of code.
    //Now, it's time to show the world what you had build
    //Once again, thanks for trying out our Android app course

}


