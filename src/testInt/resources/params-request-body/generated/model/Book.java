/*
 * This class is auto generated by https://github.com/hauner/openapi-generatr-spring.
 * DO NOT EDIT.
 */

package generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Book {

    @JsonProperty("isbn")
    private String isbn;

    @JsonProperty("title")
    private String title;

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
