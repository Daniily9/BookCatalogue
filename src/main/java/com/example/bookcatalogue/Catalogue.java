
package com.example.bookcatalogue;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Catalogue {

    private ArrayList<Publication> publications;

    public Catalogue() {
        publications = new ArrayList<>();
    }

    // Додавання публікації
    public void addPublication(Publication p) {
        if (p == null) {
            throw new IllegalArgumentException(
                "Публікація не може бути null!"
            );
        }

        publications.add(p);
    }

    // Видалення за назвою
    public void removePublicationByTitle(String title)
            throws BookNotFoundException {

        Publication publication =
                findPublicationByTitle(title);

        if (publication == null) {
            throw new BookNotFoundException(
                "Книгу з назвою \"" + title
                + "\" не знайдено!"
            );
        }

        publications.remove(publication);
    }

    // Пошук за точною назвою
    public Publication findPublicationByTitle(String title) {

        if (title == null) {
            return null;
        }

        for (Publication p : publications) {
            if (p.getTitle().equalsIgnoreCase(title.trim())) {
                return p;
            }
        }

        return null;
    }

    // Отримання всіх публікацій
    public List<Publication> getAllPublications() {
        return new ArrayList<>(publications);
    }

    // Збереження каталогу у файл
    public void saveToFile(String filename)
            throws IOException {

        try (ObjectOutputStream out =
                new ObjectOutputStream(
                    new FileOutputStream(filename))) {

            out.writeObject(publications);
        }
    }

    // Завантаження каталогу з файлу
    public void loadFromFile(String filename)
            throws IOException, ClassNotFoundException {

        try (ObjectInputStream in =
                new ObjectInputStream(
                    new FileInputStream(filename))) {

            Object data = in.readObject();

            if (!(data instanceof ArrayList<?> list)) {
                throw new IOException(
                    "Некоректний формат файлу каталогу!"
                );
            }

            ArrayList<Publication> loaded =
                    new ArrayList<>();

            for (Object item : list) {
                if (!(item instanceof Publication p)) {
                    throw new IOException(
                        "Файл містить некоректні дані!"
                    );
                }

                loaded.add(p);
            }

            publications = loaded;
        }
    }
}
