module cz.osu {
    requires javafx.controls;
    requires javafx.fxml;
    requires okhttp3;
    requires gson;
    requires java.sql;

    opens cz.osu.Controllers;
    opens cz.osu.data;
    opens images;
    opens cz.osu;
    exports cz.osu;
}