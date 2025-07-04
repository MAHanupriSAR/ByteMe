package utils;

import java.io.Serializable;

public class Review implements Serializable {
    int rating;
    String description;

    public Review(int rating, String description){
        this.rating = rating;
        this.description = description;
    }

    public String toString() {
        String ratingStr = (rating != -1) ? "Rating: " + rating : "Rating: N/A";
        String descriptionStr = (!description.isEmpty()) ? "Description: " + description : "Description: N/A";
        return ratingStr + " | " + descriptionStr;
    }
}
