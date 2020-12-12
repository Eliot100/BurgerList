package com.example.burgerlist;

import java.util.ArrayList;

public class RestMenuUnit {
    private String title;
    private ArrayList<RestMenuProduct> Products;

    public RestMenuUnit(String title){
        this.title = title;
        Products = new ArrayList<RestMenuProduct>();
    }

    public void addUnit(String productName, String productDescription, double price){
        Products.add(new RestMenuProduct(productName, productDescription, price));
    }

    public void removeUnit(String productName){
        for (RestMenuProduct product : Products){
            if(product.productName.equals(productName)) {
                Products.remove(product);
                return;
            }
        }
    }

}
