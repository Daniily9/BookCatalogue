
package com.example.bookcatalogue;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class BookGUI extends Application {

    private final Catalogue catalogue = new Catalogue();

    private final ObservableList<Book> bookItems =
            FXCollections.observableArrayList();

    private TableView<Book> table;

    private TextField titleField;
    private TextField authorField;
    private TextField publisherField;
    private TextField yearField;
    private TextField genreField;
    private TextField searchField;

    private Label countLabel;

    private String selectedOriginalTitle = null;
    private String activeSearch = "";

    @Override
    public void start(Stage stage) {

        titleField = new TextField();
        authorField = new TextField();
        publisherField = new TextField();
        yearField = new TextField();
        genreField = new TextField();
        searchField = new TextField();

        titleField.setPromptText("Введіть назву");
        authorField.setPromptText("Введіть автора");
        publisherField.setPromptText("Введіть видавництво");
        yearField.setPromptText("Наприклад, 2020");
        genreField.setPromptText("Введіть жанр");
        searchField.setPromptText("Пошук за назвою...");

        Label heading = new Label("Каталог книг");
        heading.setStyle(
            "-fx-font-size: 26px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: white;"
        );

        Label subtitle = new Label(
            "Система керування бібліотекою"
        );
        subtitle.setStyle("-fx-text-fill: #cbd5e1;");

        VBox header = new VBox(5, heading, subtitle);
        header.setPadding(new Insets(22));
        header.setStyle("-fx-background-color: #182d4a;");

        GridPane form = new GridPane();
        form.setHgap(12);
        form.setVgap(10);

        form.add(new Label("Назва книги:"), 0, 0);
        form.add(titleField, 1, 0);

        form.add(new Label("Автор:"), 2, 0);
        form.add(authorField, 3, 0);

        form.add(new Label("Видавництво:"), 0, 1);
        form.add(publisherField, 1, 1);

        form.add(new Label("Рік видання:"), 2, 1);
        form.add(yearField, 3, 1);

        form.add(new Label("Жанр:"), 0, 2);
        form.add(genreField, 1, 2);

        ColumnConstraints labelColumn =
                new ColumnConstraints();
        labelColumn.setMinWidth(110);

        ColumnConstraints inputColumn =
                new ColumnConstraints();
        inputColumn.setHgrow(Priority.ALWAYS);

        form.getColumnConstraints().addAll(
            labelColumn, inputColumn,
            new ColumnConstraints(110),
            new ColumnConstraints(220)
        );

        titleField.setMaxWidth(Double.MAX_VALUE);
        publisherField.setMaxWidth(Double.MAX_VALUE);

        Button addButton = new Button("Додати книгу");
        Button updateButton = new Button("Оновити книгу");
        Button deleteButton = new Button("Видалити книгу");
        Button clearButton = new Button("Очистити поля");

        styleButton(addButton, "#2563eb", "white");
        styleButton(updateButton, "#e2e8f0", "#1e293b");
        styleButton(deleteButton, "#fee2e2", "#b91c1c");
        styleButton(clearButton, "#e2e8f0", "#1e293b");

        HBox actionButtons = new HBox(
            10, addButton, updateButton,
            deleteButton, clearButton
        );

        VBox formCard = new VBox(
            14,
            sectionTitle("Дані книги"),
            form,
            actionButtons
        );
        styleCard(formCard);

        table = new TableView<>(bookItems);
        table.setPlaceholder(
            new Label("У каталозі поки немає книг")
        );
        table.setColumnResizePolicy(
            TableView.CONSTRAINED_RESIZE_POLICY
        );
        table.setPrefHeight(350);

        TableColumn<Book, String> titleColumn =
                new TableColumn<>("Назва");
        titleColumn.setCellValueFactory(data ->
            new ReadOnlyStringWrapper(
                data.getValue().getTitle()
            )
        );

        TableColumn<Book, String> authorColumn =
                new TableColumn<>("Автор");
        authorColumn.setCellValueFactory(data ->
            new ReadOnlyStringWrapper(
                data.getValue().getAuthor()
            )
        );

        TableColumn<Book, String> publisherColumn =
                new TableColumn<>("Видавництво");
        publisherColumn.setCellValueFactory(data ->
            new ReadOnlyStringWrapper(
                data.getValue().getPublisher()
            )
        );

        TableColumn<Book, Integer> yearColumn =
                new TableColumn<>("Рік");
        yearColumn.setCellValueFactory(data ->
            new ReadOnlyObjectWrapper<>(
                data.getValue().getYear()
            )
        );

        TableColumn<Book, String> genreColumn =
                new TableColumn<>("Жанр");
        genreColumn.setCellValueFactory(data ->
            new ReadOnlyStringWrapper(
                data.getValue().getGenre()
            )
        );

        table.getColumns().addAll(
            titleColumn, authorColumn,
            publisherColumn, yearColumn, genreColumn
        );

        table.getSelectionModel()
             .selectedItemProperty()
             .addListener((obs, oldBook, book) -> {
                 if (book != null) {
                     fillFields(book);
                 }
             });

        Button searchButton = new Button("Пошук");
        Button resetButton = new Button(
            "Скинути параметри пошуку"
        );

        styleButton(searchButton, "#2563eb", "white");
        styleButton(resetButton, "#e2e8f0", "#1e293b");

        HBox searchBar = new HBox(
            10, searchField, searchButton, resetButton
        );
        searchBar.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(searchField, Priority.ALWAYS);

        Button saveButton = new Button("Зберегти у файл");
        Button loadButton = new Button("Завантажити з файлу");

        styleButton(saveButton, "#0f766e", "white");
        styleButton(loadButton, "#e2e8f0", "#1e293b");

        countLabel = new Label("Усього книг: 0");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox footer = new HBox(
            10, countLabel, spacer,
            saveButton, loadButton
        );
        footer.setAlignment(Pos.CENTER_LEFT);

        VBox tableCard = new VBox(
            12,
            sectionTitle("Список книг"),
            searchBar,
            table,
            footer
        );
        styleCard(tableCard);

        VBox content = new VBox(
            16, formCard, tableCard
        );
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: #f5f7fb;");

        VBox.setVgrow(tableCard, Priority.ALWAYS);
        VBox.setVgrow(table, Priority.ALWAYS);

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle(
            "-fx-background-color: #f5f7fb;"
        );

        BorderPane root = new BorderPane();
        root.setTop(header);
        root.setCenter(scrollPane);

        addButton.setOnAction(e -> addBook());
        updateButton.setOnAction(e -> updateBook());
        deleteButton.setOnAction(e -> deleteBook());
        clearButton.setOnAction(e -> clearFields());

        searchButton.setOnAction(e -> searchBooks());
        searchField.setOnAction(e -> searchBooks());
        resetButton.setOnAction(e -> resetSearch());

        saveButton.setOnAction(e -> saveBooks(stage));
        loadButton.setOnAction(e -> loadBooks(stage));

        Scene scene = new Scene(root, 1050, 750);

        stage.setTitle("Каталог книг — JavaFX");
        stage.setScene(scene);
        stage.setMinWidth(850);
        stage.setMinHeight(650);
        stage.show();

        refreshTable();
    }

    // Додавання нової книги
    private void addBook() {
        try {
            Book book = readBookFromFields();

            if (catalogue.findPublicationByTitle(
                    book.getTitle()) != null) {
                showError(
                    "Книга з такою назвою вже існує!"
                );
                return;
            }

            catalogue.addPublication(book);
            refreshTable();
            clearFields();

            showInfo("Книгу успішно додано!");

        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage());
        }
    }

    // Видалення книги
    private void deleteBook() {
        try {
            String title = getTargetTitle();

            catalogue.removePublicationByTitle(title);

            refreshTable();
            clearFields();
            showInfo("Книгу успішно видалено!");

        } catch (BookNotFoundException |
                 IllegalArgumentException ex) {
            showError(ex.getMessage());
        }
    }

    // Оновлення даних книги
    private void updateBook() {
        try {
            String oldTitle = getTargetTitle();

            Publication existing =
                catalogue.findPublicationByTitle(oldTitle);

            if (existing == null) {
                throw new BookNotFoundException(
                    "Книгу \"" + oldTitle + "\" не знайдено!"
                );
            }

            Book updated = readBookFromFields();

            Publication duplicate =
                catalogue.findPublicationByTitle(
                    updated.getTitle()
                );

            if (duplicate != null && duplicate != existing) {
                showError(
                    "Інша книга з такою назвою вже існує!"
                );
                return;
            }

            if (!(existing instanceof Book book)) {
                showError(
                    "Обрана публікація не є книгою!"
                );
                return;
            }

            book.setTitle(updated.getTitle());
            book.setYear(updated.getYear());
            book.setAuthor(updated.getAuthor());
            book.setPublisher(updated.getPublisher());
            book.setGenre(updated.getGenre());

            refreshTable();
            clearFields();
            showInfo("Дані книги успішно оновлено!");

        } catch (BookNotFoundException |
                 IllegalArgumentException ex) {
            showError(ex.getMessage());
        }
    }

    // Пошук за повною або частковою назвою
    private void searchBooks() {
        activeSearch = searchField.getText().trim();
        refreshTable();
    }

    // Скидання пошуку
    private void resetSearch() {
        activeSearch = "";
        searchField.clear();
        refreshTable();
    }

    // Збереження каталогу
    private void saveBooks(Stage stage) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Зберегти каталог");
        chooser.setInitialFileName("catalogue.dat");

        chooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter(
                "Файли каталогу (*.dat)", "*.dat"
            )
        );

        File file = chooser.showSaveDialog(stage);

        if (file == null) {
            return;
        }

        try {
            catalogue.saveToFile(
                file.getAbsolutePath()
            );
            showInfo("Каталог успішно збережено!");

        } catch (Exception ex) {
            showError(
                "Помилка збереження: " + ex.getMessage()
            );
        }
    }

    // Завантаження каталогу
    private void loadBooks(Stage stage) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Завантажити каталог");

        chooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter(
                "Файли каталогу (*.dat)", "*.dat"
            )
        );

        File file = chooser.showOpenDialog(stage);

        if (file == null) {
            return;
        }

        try {
            catalogue.loadFromFile(
                file.getAbsolutePath()
            );

            resetSearch();
            clearFields();
            refreshTable();

            showInfo("Каталог успішно завантажено!");

        } catch (Exception ex) {
            showError(
                "Помилка завантаження: " + ex.getMessage()
            );
        }
    }

    // Читання і перевірка даних форми
    private Book readBookFromFields() {
        String title = titleField.getText().trim();
        String author = authorField.getText().trim();
        String publisher = publisherField.getText().trim();
        String genre = genreField.getText().trim();
        String yearText = yearField.getText().trim();

        if (title.isEmpty() || author.isEmpty()
                || publisher.isEmpty()
                || genre.isEmpty()
                || yearText.isEmpty()) {
            throw new IllegalArgumentException(
                "Заповніть усі поля!"
            );
        }

        int year;

        try {
            year = Integer.parseInt(yearText);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(
                "Рік повинен бути цілим числом!"
            );
        }

        return new Book(
            title, year, author, publisher, genre
        );
    }

    // Отримання назви для видалення/оновлення
    private String getTargetTitle() {
        String title = selectedOriginalTitle;

        if (title == null || title.isBlank()) {
            title = titleField.getText().trim();
        }

        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException(
                "Введіть назву або виберіть книгу!"
            );
        }

        return title;
    }

    // Оновлення таблиці
    private void refreshTable() {
        List<Book> books = catalogue
            .getAllPublications()
            .stream()
            .filter(p -> p instanceof Book)
            .map(p -> (Book) p)
            .filter(book ->
                book.getTitle().toLowerCase()
                    .contains(activeSearch.toLowerCase())
            )
            .collect(Collectors.toList());

        bookItems.setAll(books);

        countLabel.setText(
            "Показано: " + books.size()
            + " | Усього публікацій: "
            + catalogue.getAllPublications().size()
        );

        table.refresh();
    }

    // Заповнення полів обраною книгою
    private void fillFields(Book book) {
        selectedOriginalTitle = book.getTitle();

        titleField.setText(book.getTitle());
        authorField.setText(book.getAuthor());
        publisherField.setText(book.getPublisher());
        yearField.setText(
            String.valueOf(book.getYear())
        );
        genreField.setText(book.getGenre());
    }

    // Очищення полів
    private void clearFields() {
        selectedOriginalTitle = null;

        table.getSelectionModel().clearSelection();

        titleField.clear();
        authorField.clear();
        publisherField.clear();
        yearField.clear();
        genreField.clear();
    }

    // Інформаційне повідомлення
    private void showInfo(String message) {
        Alert alert = new Alert(
            Alert.AlertType.INFORMATION
        );
        alert.setTitle("Успішно");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Повідомлення про помилку
    private void showError(String message) {
        Alert alert = new Alert(
            Alert.AlertType.ERROR
        );
        alert.setTitle("Помилка");
        alert.setHeaderText("Не вдалося виконати дію");
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Стилізація кнопок
    private void styleButton(
            Button button, String bg, String fg) {

        button.setStyle(
            "-fx-background-color: " + bg + ";" +
            "-fx-text-fill: " + fg + ";" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 7;" +
            "-fx-padding: 10 15;" +
            "-fx-cursor: hand;"
        );
    }

    // Стилізація карток
    private void styleCard(VBox box) {
        box.setPadding(new Insets(18));
        box.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 12;" +
            "-fx-border-color: #e2e8f0;" +
            "-fx-border-radius: 12;"
        );
    }

    // Заголовок секції
    private Label sectionTitle(String text) {
        Label label = new Label(text);
        label.setStyle(
            "-fx-font-size: 17px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #233854;"
        );
        return label;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
