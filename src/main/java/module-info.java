module com.equiel1703dev.ponte {
  requires javafx.controls;
  requires javafx.fxml;
  requires javafx.base;
  requires javafx.graphics;
  requires com.github.oshi;
  requires org.slf4j.nop;

  exports com.equiel1703dev.ponte to javafx.graphics;

  opens com.equiel1703dev.ponte.controllers to javafx.fxml;
}
