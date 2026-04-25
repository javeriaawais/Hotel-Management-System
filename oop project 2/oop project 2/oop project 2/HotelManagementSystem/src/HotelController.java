
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;

import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class HotelController implements Initializable {

    @FXML private TextField tfGuest, tfNights, tfDiscount, tfCheckoutDiscount, searchField;
    @FXML private ComboBox<RoomType> cbRoomType;
    @FXML private CheckBox cbSpa, cbRoomService, cbMiniBar, cbLaundry, cbBreakfast, cbAirportPickup;
    @FXML private ComboBox<Room> cbOccupiedRoom, cbCheckoutRoom, cbNeedsCleaningRoom;
    @FXML private CheckBox cbSpa2, cbRoomService2, cbMiniBar2, cbLaundry2, cbBreakfast2, cbAirportPickup2;
    @FXML private TextArea logArea, billArea;
    @FXML private TableView<Room> roomTable;
    @FXML private TableColumn<Room, String> colRoomNumber, colRoomType, colRoomStatus, colGuestNights;
    @FXML private FlowPane roomStatusPane;

    private final ObservableList<Room> rooms = FXCollections.observableArrayList();
    private final ObservableList<Activity> activities = FXCollections.observableArrayList();
    private final DecimalFormat money = new DecimalFormat("$#,##0.00");

    public static class Activity {
        private final String date, guest, room, action, amount;
        public Activity(String date, String guest, String room, String action, String amount) {
            this.date = date; this.guest = guest; this.room = room; this.action = action; this.amount = amount;
        }
        public String getDate() { return date; }
        public String getGuest() { return guest; }
        public String getRoom() { return room; }
        public String getAction() { return action; }
        public String getAmount() { return amount; }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbRoomType.setItems(FXCollections.observableArrayList(RoomType.values()));

        for (int i = 0; i < 5; i++) rooms.add(new Room(101 + i, RoomType.SIMPLE, 100));
        for (int i = 0; i < 5; i++) rooms.add(new Room(201 + i, RoomType.DELUXE, 160));
        for (int i = 0; i < 5; i++) rooms.add(new Room(301 + i, RoomType.SUITE, 220));

        roomTable.setItems(rooms);

        roomStatusPane.setHgap(25);
        roomStatusPane.setVgap(20);
        roomStatusPane.setPadding(new Insets(15));
        roomStatusPane.setAlignment(Pos.CENTER);

        colRoomNumber.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Room, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Room, String> p) {
                return new SimpleStringProperty(String.valueOf(p.getValue().getRoomNumber()));
            }
        });
        colRoomType.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Room, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Room, String> p) {
                return new SimpleStringProperty(p.getValue().getType().name());
            }
        });
        colRoomStatus.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Room, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Room, String> p) {
                return new SimpleStringProperty(p.getValue().getStatus().name());
            }
        });
        colGuestNights.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Room, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Room, String> p) {
                Room r = p.getValue();
                String g = (r.getGuestName() == null) ? "-" : r.getGuestName();
                String n = (r.getNights() == 0) ? "-" : String.valueOf(r.getNights());
                return new SimpleStringProperty(g + " / " + n);
            }
        });

        refreshLists();
        updateRoomStatusBoxes();
    }

    @FXML
    private void onCheckout() {
        Room r = cbCheckoutRoom.getValue();
        if (r == null) {
            alert("Error", "Please select a room for checkout.");
            return;
        }

        double discPercent;
        try {
            discPercent = Double.parseDouble(text(tfCheckoutDiscount));
        } catch (Exception e) {
            discPercent = 0;
        }

        StringBuilder bill = new StringBuilder();
        bill.append("--- HOTEL RECEIPT ---\n");
        bill.append("Guest: ").append(r.getGuestName()).append("\n");
        bill.append("Room: ").append(r.getRoomNumber()).append(" (").append(r.getType()).append(")\n");
        bill.append("----------------------------\n");

        double roomCostTotal = r.getBasePrice() * r.getNights();
        bill.append("Stay: ").append(r.getNights()).append(" night(s) x ").append(money.format(r.getBasePrice())).append("\n");
        bill.append("Room Subtotal: ").append(money.format(roomCostTotal)).append("\n\n");

        bill.append("Additional Services:\n");
        double servicesTotal = 0;
        List<Service> sList = r.getServices();
        if (sList.isEmpty()) {
            bill.append("- No extra services\n");
        } else {
            for (int i = 0; i < sList.size(); i++) {
                Service s = sList.get(i);
                bill.append("- ").append(s.getName()).append(": ").append(money.format(s.getPrice())).append("\n");
                servicesTotal += s.getPrice();
            }
        }
        bill.append("Services Subtotal: ").append(money.format(servicesTotal)).append("\n\n");

        double grandTotalBeforeDisc = roomCostTotal + servicesTotal;
        double discountAmount = grandTotalBeforeDisc * (discPercent / 100.0);
        double finalTotal = grandTotalBeforeDisc - discountAmount;

        bill.append("----------------------------\n");
        bill.append("Gross Total: ").append(money.format(grandTotalBeforeDisc)).append("\n");
        bill.append("Discount (").append(discPercent).append("%): -").append(money.format(discountAmount)).append("\n");
        bill.append("NET TOTAL: ").append(money.format(finalTotal)).append("\n");
        bill.append("----------------------------\n");
        bill.append("Thank you for staying with us!");

        billArea.setText(bill.toString());

        activities.add(new Activity(LocalDate.now().toString(), r.getGuestName(), String.valueOf(r.getRoomNumber()), "Check-Out", money.format(finalTotal)));

        r.setStatus(RoomStatus.NEEDS_CLEANING);
        r.setGuestName(null);
        r.setNights(0);
        r.getServices().clear();

        refreshLists();
        updateRoomStatusBoxes();
    }

    @FXML
    private void onCheckIn() {
        String guest = text(tfGuest);
        RoomType type = cbRoomType.getValue();
        int nights;
        try {
            nights = Integer.parseInt(text(tfNights));
        } catch (Exception e) {
            alert("Error", "Enter number of nights.");
            return;
        }

        Room selected = null;
        for (int i = 0; i < rooms.size(); i++) {
            Room r = rooms.get(i);
            if (r.getType() == type && r.getStatus() == RoomStatus.AVAILABLE) {
                selected = r;
                break;
            }
        }

        if (selected != null && !guest.isEmpty()) {
            selected.setGuestName(guest);
            selected.setNights(nights);
            selected.setStatus(RoomStatus.OCCUPIED);
            selected.getServices().addAll(selectedServices(cbSpa, cbRoomService, cbMiniBar, cbLaundry, cbBreakfast, cbAirportPickup));

            activities.add(new Activity(LocalDate.now().toString(), guest, String.valueOf(selected.getRoomNumber()), "Check-In", "In Progress"));

            refreshLists();
            updateRoomStatusBoxes();
            clearCheckInInputs();
        } else {
            alert("Error", "Check name or room availability.");
        }
    }

    @FXML
    private void onAddService() {
        Room r = cbOccupiedRoom.getValue();
        if (r == null) return;
        List<Service> sList = selectedServices(cbSpa2, cbRoomService2, cbMiniBar2, cbLaundry2, cbBreakfast2, cbAirportPickup2);
        for (int i = 0; i < sList.size(); i++) {
            r.getServices().add(sList.get(i));
        }
        activities.add(new Activity(LocalDate.now().toString(), r.getGuestName(), String.valueOf(r.getRoomNumber()), "Add Service", "Updated"));
        refreshLists();
    }

    @FXML
    private void onMarkCleaned() {
        Room r = cbNeedsCleaningRoom.getValue();
        if (r != null) {
            r.setStatus(RoomStatus.AVAILABLE);
            refreshLists();
            updateRoomStatusBoxes();
        }
    }

    @FXML
    private void onRefresh() {
        String q = text(searchField).toLowerCase();
        StringBuilder report = new StringBuilder();
        for (int i = 0; i < activities.size(); i++) {
            Activity a = activities.get(i);
            if (q.isEmpty() || a.getGuest().toLowerCase().contains(q)) {
                report.append(a.getDate()).append(" | ").append(a.getGuest()).append(" | ").append(a.getAction()).append(" | ").append(a.getAmount()).append("\n");
            }
        }
        logArea.setText(report.toString());
    }

    private void refreshLists() {
        ObservableList<Room> occ = FXCollections.observableArrayList();
        ObservableList<Room> clean = FXCollections.observableArrayList();
        for (int i = 0; i < rooms.size(); i++) {
            Room r = rooms.get(i);
            if (r.getStatus() == RoomStatus.OCCUPIED) occ.add(r);
            else if (r.getStatus() == RoomStatus.NEEDS_CLEANING) clean.add(r);
        }
        cbOccupiedRoom.setItems(occ);
        cbCheckoutRoom.setItems(occ);
        cbNeedsCleaningRoom.setItems(clean);
        roomTable.refresh();
        onRefresh();
    }

    private void updateRoomStatusBoxes() {
        roomStatusPane.getChildren().clear();
        for (int i = 0; i < rooms.size(); i++) {
            Room r = rooms.get(i);
            VBox box = new VBox(5);
            box.setAlignment(Pos.CENTER);
            Rectangle rect = new Rectangle(40, 40);
            rect.setArcWidth(8); rect.setArcHeight(8);

            if (r.getStatus() == RoomStatus.AVAILABLE) rect.setFill(Color.web("#2ECC71"));
            else if (r.getStatus() == RoomStatus.OCCUPIED) rect.setFill(Color.web("#E74C3C"));
            else rect.setFill(Color.web("#F1C40F"));

            box.getChildren().addAll(new Label(String.valueOf(r.getRoomNumber())), rect);
            roomStatusPane.getChildren().add(box);
        }
    }

    private List<Service> selectedServices(CheckBox... boxes) {
        List<Service> list = new ArrayList<Service>();
        if (boxes[0].isSelected()) list.add(Service.spa());
        if (boxes[1].isSelected()) list.add(Service.roomService());
        if (boxes[2].isSelected()) list.add(Service.miniBar());
        if (boxes[3].isSelected()) list.add(Service.laundry());
        if (boxes[4].isSelected()) list.add(Service.breakfast());
        if (boxes[5].isSelected()) list.add(Service.airportPickup());
        return list;
    }

    private String text(TextField f) { return (f.getText() == null) ? "" : f.getText().trim(); }

    private void alert(String t, String c) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(t); a.setHeaderText(null); a.setContentText(c); a.showAndWait();
    }

    private void clearCheckInInputs() {
        tfGuest.clear(); tfNights.clear(); tfDiscount.setText("0");
        cbRoomType.getSelectionModel().clearSelection();
        cbSpa.setSelected(false); cbRoomService.setSelected(false);
        cbMiniBar.setSelected(false); cbLaundry.setSelected(false);
        cbBreakfast.setSelected(false); cbAirportPickup.setSelected(false);
    }
}