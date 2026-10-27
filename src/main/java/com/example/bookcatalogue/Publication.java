
package com.example.bookcatalogue;

import java.io.Serializable;

public class Publication implements Serializable {

    private static final long serialVersionUID = 1L;

    private String title;
    private int year;

    public Publication(String title, int year) {
        setTitle(title);
        setYear(year);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Назва публікації не може бути порожньою!"
            );
        }
        this.title = title.trim();
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        if (year < 1450 || year > 2100) {
            throw new IllegalArgumentException(
                "Некоректний рік видання!"
            );
        }
        this.year = year;
    }

    @Override
    public String toString() {
        return "Назва: " + title
             + ", рік видання: " + year;
    }
}
