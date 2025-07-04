package canteenUtils;

import utils.Review;

import java.io.Serializable;
import java.util.ArrayList;

public class MenuItem implements Serializable {
    private String name;
    private String category;
    private int price;
    private int stock;
    private ArrayList<Review> reviews;
    private int numberOfBuyings;

    public MenuItem(String name, String category, int price, int stock){
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.reviews = new ArrayList<>();
        numberOfBuyings = 0;
    }

    public MenuItem(MenuItem other) {
        this.name = other.name;
        this.category = other.category;
        this.price = other.price;
        this.stock = other.stock;
        this.reviews = other.reviews;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }

    public int getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
    }

    public int stocksAvailable() {
        return stock;
    }
    public void setStocks(int stock) {
        this.stock = stock;
    }
    public void decreaseStocks(int val){
        this.stock -= val;
    }

    public ArrayList<Review> getReview() {
        return reviews;
    }
    public void addReview(int rating, String description) {
        reviews.add(new Review(rating, description));
    }
    public void showReviews(){
        if(reviews.isEmpty()){
            System.out.println("No review available for "+this.name);
        }
        for(Review review : reviews){
            System.out.println(review);
        }
    }

    public void setNumberOfBuyings(int numberOfBuyings) {
        this.numberOfBuyings = numberOfBuyings;
    }
    public void increaseNumberOfBuyings(int value) {
        this.numberOfBuyings += value;
    }
    public int getNumberOfBuyings() {
        return numberOfBuyings;
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof MenuItem other)) return false;
        return this.name.equals(other.name) &&
                this.category.equals(other.category) &&
                this.price == other.price &&
                this.stock == other.stock;
    }

    public String toString() {
        return "Name: "+name+" | Price: ₹"+price+" | Stock Available: "+ stock;
    }
}
