package dto;

import com.google.gson.JsonArray;

public class OrderData {
    private final JsonArray basketItems;
    private final double basketTotalPrice;
    private final String name;
    private final String city;
    private final String street;
    private final String houseNumber;
    private final String phone;
    private final String orderNotes;
    private final String arrivalDate;
    private final String arrivalTime;
    private final String deliveryOption;


    public OrderData(JsonArray basketItems, double basketTotalPrice, String name, String city, 
            String street, String houseNumber, String phone, String orderNotes, String arrivalDate, 
            String arrivalTime, String deliveryOption) {
        this.basketItems = basketItems;
        this.basketTotalPrice = basketTotalPrice;
        this.name = name;
        this.city = city;
        this.street = street;
        this.houseNumber = houseNumber;
        this.phone = phone;
        this.orderNotes = orderNotes;
        this.arrivalDate = arrivalDate;
        this.arrivalTime = arrivalTime;
        this.deliveryOption = deliveryOption;
    }

    public JsonArray getBasketItems() {
        return basketItems;
    }

    public double getBasketTotalPrice() {
        return basketTotalPrice;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public String getPhone() {
        return phone;
    }

    public String getOrderNotes() {
        return orderNotes;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public String getDeliveryOption() {
        return deliveryOption;
    }
}
