module com.equiel1703.ponte {
  requires javafx.controls;
  requires javafx.fxml;
  requires javafx.base;
  requires javafx.graphics;
  requires com.github.oshi;
  requires org.slf4j.nop;

  exports com.equiel1703.ponte to javafx.graphics;

  opens com.equiel1703.ponte.controllers to javafx.fxml;
}
