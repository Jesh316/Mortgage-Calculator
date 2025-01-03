package com.example.mortagecalc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import java.text.DecimalFormat
import kotlin.math.pow
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType

class Mortgage {
    val MONEY: DecimalFormat = DecimalFormat("$#,##0.00")
    private var amount = 0f
    private var years = 0
    private var rate = 0f

    init {
        setAmount(100000.0f)
        setYears(30)
        setRate(0.035f)
    }

    fun setAmount(newAmount: Float) {
        if (newAmount >= 0) amount = newAmount
    }

    fun setYears(newYears: Int) {
        if (newYears >= 0) years = newYears
    }

    fun setRate(newRate: Float) {
        if (newRate >= 0) rate = newRate
    }
    fun getAmount() = amount // Getter for amount
    fun getYears() = years // Getter for years
    fun getRate() = rate // Getter for rate


    fun monthlyPayment(): Float {
        val mRate = rate / 12 // Monthly interest rate
        val n = years * 12 // Total number of payments

        return if (rate == 0f) {
            amount / n
        } else {
            // Calculate (1 + r)^n
            val temp = (1 + mRate).pow(n.toFloat())
            val payment = (amount * mRate * temp) / (temp - 1)
            roundToTwoDecimalPlaces(payment) // Round to two decimal places
        }
    }
    private fun roundToTwoDecimalPlaces(value: Float): Float {
        return (Math.round(value * 100).toFloat() / 100) // Round to 2 decimal places
    }


    fun formattedMonthlyPayment(): String {
        return MONEY.format(monthlyPayment())
    }

    fun totalPayment(): Float {
        return monthlyPayment() * (years * 12) // Total number of payments
    }

    fun formattedTotalPayment(): String {
        return MONEY.format(totalPayment())
    }
}

//Implemented view model
class MortgageViewModel : ViewModel() {
    private val mortgage = Mortgage()

    fun getMonthlyPayment() = mortgage.formattedMonthlyPayment()
    fun getTotalPayment() = mortgage.formattedTotalPayment()
    fun getAmount() = mortgage.getAmount() // Access the getter in Mortgage
    fun getYears() = mortgage.getYears() // Access the getter in Mortgage
    fun setYears(years: Int) {
        mortgage.setYears(years)
    }
    fun getRate() = mortgage.getRate() // Access the getter in Mortgage
    fun updateMortgage(amount: Float, years: Int, rate: Float) {
        mortgage.setAmount(amount)
        mortgage.setYears(years)
        mortgage.setRate(rate)
    }
}

@Composable
fun CustomAppBar(title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(color = MaterialTheme.colorScheme.primary),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}

//Screen to input the year, amount, and interest rate. Click done button when finished.
@Composable
fun InputScreen(viewModel: MortgageViewModel, navController: NavHostController) {
    var amount by remember { mutableStateOf(viewModel.getAmount().toString()) }
    var years by remember { mutableStateOf(viewModel.getYears().toString()) }
    var rate by remember { mutableStateOf(viewModel.getRate().toString()) }

    CustomAppBar(title = "MortgageV0")
    Spacer(modifier = Modifier.height(16.dp))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,  // Move content to the top of the screen
        horizontalAlignment = Alignment.Start   // Align content to the left
    ) {


        Spacer(modifier = Modifier.height(60.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp), // Add space below the row
            verticalAlignment = Alignment.CenterVertically, // Align vertically to center
            horizontalArrangement = Arrangement.Start // Align the whole row to start
        ) {
            // "Select Years" text aligned horizontally
            Text("Years", modifier = Modifier.padding(end = 16.dp)) // Add padding to separate from buttons

            // Row for year selection radio buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly, // Space buttons evenly
                verticalAlignment = Alignment.CenterVertically
            ) {
                // RadioButton for 10 years
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = years == "10",
                        onClick = {
                            years = "10"
                            viewModel.setYears(10) // Set years to 10
                        },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Color.Red, // Color for selected state
                            unselectedColor = Color.White // White background when unselected
                        ),
                        modifier = Modifier
                            .size(10.dp)
                            .border(
                                width = 1.dp, // Smaller border
                                color = if (years == "10") Color.Red else Color.Black, // Red for selected, Black outline for unselected
                                shape = CircleShape
                            )
                    )
                    Text("10", modifier = Modifier.padding(start = 25.dp)) // Label for 10 years
                }

                // RadioButton for 15 years
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = years == "15",
                        onClick = {
                            years = "15"
                            viewModel.setYears(15) // Set years to 15
                        },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Color.Red,
                            unselectedColor = Color.White
                        ),
                        modifier = Modifier
                            .size(10.dp)
                            .border(
                                width = 1.dp, // Smaller border
                                color = if (years == "15") Color.Red else Color.Black, // Red for selected, Black outline for unselected
                                shape = CircleShape
                            )
                    )
                    Text("15", modifier = Modifier.padding(start = 25.dp)) // Label for 15 years
                }

                // RadioButton for 30 years
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = years == "30",
                        onClick = {
                            years = "30"
                            viewModel.setYears(30) // Set years to 30
                        },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Color.Red,
                            unselectedColor = Color.White
                        ),
                        modifier = Modifier
                            .size(10.dp)
                            .border(
                                width = 1.dp, // Smaller border
                                color = if (years == "30") Color.Red else Color.Black, // Red for selected, Black outline for unselected
                                shape = CircleShape
                            )
                    )
                    Text("30", modifier = Modifier.padding(start = 25.dp)) // Label for 30 years
                }
            }
        }


        Spacer(modifier = Modifier.height(30.dp))
        // Amount TextField
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp) // Padding for the entire column
        ) {
            // Amount Input
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically // Center items vertically
            ) {
                // Amount Label
                Text(
                    "Amount",
                    //modifier = Modifier
                        //.padding(end = 8.dp) // Padding to separate from TextField
                        //.align(Alignment.CenterVertically) // Align vertically
                )

                Spacer(modifier = Modifier.width(68.dp))

                TextField(
                    value = amount,
                    onValueChange = { newValue -> amount = newValue },

                    modifier = Modifier
                        .weight(2f)
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number // Use numeric keyboard
                    )
                )
            }

            Spacer(modifier = Modifier.height(30.dp)) // Spacer between Amount and Interest Rate sections

            // Interest Rate Input
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically // Center items vertically
            ) {
                // Interest Rate Label
                Box(
                    modifier = Modifier.weight(1f) // Allow label to take available space
                ) {
                    Text(
                        "Interest Rate",
                        modifier = Modifier.align(Alignment.CenterStart) // Align label to the start
                    )
                }

                TextField(
                    value = rate,
                    onValueChange = { newValue ->
                        if (newValue.isEmpty() || Regex("^\\.?\\d*\\.?\\d*$").matches(newValue)) {
                            rate = newValue
                        }
                    },

                    modifier = Modifier
                        .weight(2f)
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Decimal // Use decimal keyboard for rates
                    )
                )
            }

            Spacer(modifier = Modifier.height(24.dp)) // Space before the Done button

            // Done Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp), // Add horizontal padding for the button
                horizontalArrangement = Arrangement.Center // Center the button within the Row
            ) {
                Button(
                    onClick = {
                        val parsedAmount = amount.toFloatOrNull() ?: 0f
                        val parsedYears = years.toIntOrNull() ?: 0
                        val parsedRate = rate.toFloatOrNull() ?: 0f

                        viewModel.updateMortgage(parsedAmount, parsedYears, parsedRate)
                        navController.navigate("results")
                    }
                ) {
                    Text("Done")
                }
            }
        }
}
    }

//Displays monthly payment and total payment.
@Composable
fun ResultsScreen(viewModel: MortgageViewModel, navController: NavHostController) {
    CustomAppBar(title = "MortgageV0")
    Spacer(modifier = Modifier.height(16.dp))
    Column(

        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top, // Align content towards the top
        horizontalAlignment = Alignment.Start // Align the content to the left
    ) {


        Spacer(modifier = Modifier.height(24.dp))  // Increase space below the title

        // Display amount, years, and interest rate
        val interestRate = viewModel.getRate()

        // Label and value for Amount
        Spacer(modifier = Modifier.height(25.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Amount:", modifier = Modifier.weight(1f))
            Text("${viewModel.getAmount()}", modifier = Modifier.weight(1f)) // Take up space proportionally
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Label and value for Years
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Years:", modifier = Modifier.weight(1f))
            Text("${viewModel.getYears()}", modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Label and value for Interest Rate
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Interest Rate:", modifier = Modifier.weight(1f))
            Text("${(interestRate * 100)}%", modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(24.dp))  // Add space before the red divider
        Divider(
            color = MaterialTheme.colorScheme.error,  // Set the line to red
            thickness = 7.dp
        )
        Spacer(modifier = Modifier.height(24.dp))

        // Label and value for Monthly Payment
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Monthly Payment:", modifier = Modifier.weight(1f))
            Text(viewModel.getMonthlyPayment(), modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Label and value for Total Payment
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Total Payment:", modifier = Modifier.weight(1f))
            Text(viewModel.getTotalPayment(), modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center // Center the button horizontally within the row
        ) {
            Button(onClick = { navController.navigate("input") }) {
                Text("MODIFY DATA")
            }
        }
    }

}

@Composable
fun MyApp() {
    val navController = rememberNavController()
    val viewModel: MortgageViewModel = viewModel() // Create the ViewModel instance here
    NavHost(navController, startDestination = "input") {
        composable("input") {
            InputScreen(viewModel = viewModel, navController = navController)
        }
        composable("results") {
            ResultsScreen(viewModel = viewModel, navController = navController)
        }
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                MyApp()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewInputScreen() {
    InputScreen(viewModel = MortgageViewModel(), navController = rememberNavController())
}

@Preview(showBackground = true)
@Composable
fun PreviewResultsScreen() {
    ResultsScreen(viewModel = MortgageViewModel(), navController = rememberNavController())
}