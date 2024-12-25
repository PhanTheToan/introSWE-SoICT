package com.example.citizenmanagement.views;

import com.example.citizenmanagement.controllers.feecontrollers.FeeHoKhauCell4Controller;
import com.example.citizenmanagement.controllers.feecontrollers.FeeHoKhauCell5Controller;
import com.example.citizenmanagement.models.FeeHoKhauCellDot;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;

import java.io.IOException;

public class FeeHoKhauCell5Factory extends ListCell<FeeHoKhauCellDot> {
    @Override
    public void updateItem(FeeHoKhauCellDot feeHoKhauCell, boolean empty) {
        super.updateItem(feeHoKhauCell, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        }
        else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/fee/FeeHoKhauCell5.fxml"));
            loader.setController(new FeeHoKhauCell5Controller(feeHoKhauCell));
            setText(null);
            try {
                setGraphic(loader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}