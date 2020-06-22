module cz.osu
{
    requires javafx.controls;
    requires javafx.fxml;
    requires okhttp3;
    requires com.google.gson;
    requires java.sql;

    opens cz.osu.controllers;
    opens cz.osu.data;
    opens images;
    opens cz.osu;
    opens cz.osu.cells;
    exports cz.osu;
}