package com.example.cupcake.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
// Single cupcake Price
private const val PRICE_PER_CUPCAKE = 2.00
// Additional cost for same day pickup.
private const val PRICE_FOR_SAME_DAY_PICKUP = 3.00

/**
 * [OrderViewModel] holds information about a cupcake order in terms of quantity, flavor, and
 * pickup date. It also knows how to calculate the total price based on these order details.
 */

class OrderViewModel: ViewModel() {
    //Possible date options for the order
    val dateOptions = getPickUpOptions()

    //Quantity in the order
    private val _quantity = MutableLiveData<Int>()
    val quantity: LiveData<Int> = _quantity

    //Flavor for the order
    private val _flavor = MutableLiveData<String>()
    val flavor: LiveData<String> = _flavor

    //Pickup date for the order
    private val _date = MutableLiveData<String>()
    val date: LiveData<String> = _date

    private val _price = MutableLiveData<Double>()
    // Setting up the price based on different currencies
    val price: LiveData<String> = Transformations.map(_price){
        NumberFormat.getCurrencyInstance().format(it)
    }

    init{
        //Setting initial values
        resetOrder()
    }

    /**
     * Set the quantity of cupcakes for this order.
     *
     * @param numberCupcakes to order
     */

    fun setQuantity(numberCupcakes: Int) {
        _quantity.value = numberCupcakes
        updatePrice()
    }
    /**
     * Set the flavor of cupcakes for this order. Only 1 flavor can be selected for the whole order.
     *
     * @param desiredFlavor is the cupcake flavor as a string
     */

    fun setFlavor(desiredFlavor: String) {
        _flavor.value = desiredFlavor
    }
    /**
     * Set the pickup date for this order.
     *
     * @param pickupDate is the date for pickup as a string
     */

    fun setDate(pickupDate: String) {
        _date.value = pickupDate
        updatePrice()
    }

    /**
     * Returns true if a flavor has not been selected for the order yet. Returns false otherwise.
     */
    fun hasNoFlavorSet(): Boolean {
        return _flavor.value.isNullOrEmpty()
    }

    //Returns a list of date options starting with current day, and next 3 days.
    private fun getPickUpOptions(): List<String>{
        val options = mutableListOf<String>()
        val formatter = SimpleDateFormat("E MMM d", Locale.getDefault())
        val calendar = Calendar.getInstance()
        repeat(4){
            options.add(formatter.format(calendar.time))
            calendar.add(Calendar.DATE, 1)
        }
        return options
    }

    //Reset Order by using initial default values.
    fun resetOrder(){
        _quantity.value = 0
        _flavor.value = ""
        _date.value = dateOptions[0]
        _price.value = 0.0


    }

    //Updating Price based on the order details.
    private fun updatePrice(){
        var calculatedPrice = (quantity.value ?: 0) * PRICE_PER_CUPCAKE
        _price.value = (quantity.value ?: 0 ) * PRICE_PER_CUPCAKE
        //Updating Price if user selects current day.
        if(dateOptions[0] == _date.value){
            calculatedPrice += PRICE_FOR_SAME_DAY_PICKUP
        }
        _price.value = calculatedPrice

    }

}