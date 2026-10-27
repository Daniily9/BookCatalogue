
package com.example.bookcatalogue;

public class Book extends Publication {

    private String author;
    private String publisher;
    private String genre;

    public Book(String title, int year,
                String author, String publisher,
                String genre) {

        super(title, year);

        setAuthor(author);
        setPublisher(publisher);
        setGenre(genre);
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Автор книги не може бути порожнім!"
            );
        }
        this.author = author.trim();
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        if (publisher == null || publisher.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Видавництво не може бути порожнім!"
            );
        }
        this.publisher = publisher.trim();
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        if (genre == null || genre.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Жанр книги не може бути порожнім!"
            );
        }
        this.genre = genre.trim();
    }

    @Override
    public String toString() {
        return "Назва: " + getTitle()
             + "\nАвтор: " + author
             + "\nВидавництво: " + publisher
             + "\nЖанр: " + genre
             + "\nРік видання: " + getYear();
    }
}
