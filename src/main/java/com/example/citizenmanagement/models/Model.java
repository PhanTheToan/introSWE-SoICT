package com.example.citizenmanagement.models;

import com.example.citizenmanagement.views.ViewFactory;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Model {
    private int tam;
    private static Model model;
    private final ViewFactory viewFactory;
    private final DatabaseConnection databaseConnection;

    //citizen manager section
    private final CitizenManager citizenManager;
    private boolean citizenManagerLoginSuccessFlag;
    private ObjectProperty<Image> imageObjectProperty;
    // nhan khau
    private static List_nhan_khau nhanKhauDuocChon;
    private static MainTamVangCell nhanKhauTamVangDuocChon;

    //ho khau section
    private static MainHoKhauCell hoKhauDuocChon;
    private static thanh_vien_cua_ho_cell thanhVienCuaHoCell;

    // nhan khau section
    private static List_nhan_khau nhanKhauTamTruDuocChon;

    private final ObservableList<List_nhan_khau> danhsachnhankhau;

    public static List_nhan_khau getNhanKhauDuocChon() {
        return nhanKhauDuocChon;
    }

    public static void setNhanKhauDuocChon(List_nhan_khau nhanKhauDuocChon) {
        Model.nhanKhauDuocChon = nhanKhauDuocChon;
    }

    //thu phi
    private final DanhSachThuPhiModel danhSachThuPhiModel;
    private final FeeKhoanThuModel feeKhoanThuModel;
    private final FeeKhoanThuDotModel feeKhoanThuDotModel;
    private final HoaDonModel hoaDonModel;

    private final ObservableList<FeeKhoanThuCell> danhSachKhoanThu;
    private final ObservableList<FeeKhoanThuDotCell> danhSachKhoanThuDot;

    private final ObservableList<FeeHoKhauCell> danhSachDaDongPhi;
    private final ObservableList<FeeHoKhauCell> danhSachChuaDongPhi;

    private final ObservableList<FeeHoKhauCellDot> danhSachDaDongPhiDot;
    private final ObservableList<FeeHoKhauCellDot> danhSachChuaDongPhiDot;


    private Model() {
//        this.luuTruNhanKhau = new luuTruNhanKhau();
        this.danhsachnhankhau = FXCollections.observableArrayList();
        this.viewFactory = new ViewFactory();
        this.databaseConnection = new DatabaseConnection();
        this.hoaDonModel = new HoaDonModel();
        this.citizenManager = new CitizenManager();
        this.feeKhoanThuDotModel = new FeeKhoanThuDotModel();
        this.feeKhoanThuModel = new FeeKhoanThuModel();
        this.danhSachThuPhiModel = new DanhSachThuPhiModel();
        this.danhSachKhoanThuDot = FXCollections.observableArrayList();
        this.danhSachKhoanThu = FXCollections.observableArrayList();
        this.danhSachDaDongPhi = FXCollections.observableArrayList();
        this.danhSachChuaDongPhi = FXCollections.observableArrayList();
        this.danhSachDaDongPhiDot = FXCollections.observableArrayList();
        this.danhSachChuaDongPhiDot = FXCollections.observableArrayList();
        initDanhSachKhoanThu();
        innitDanhSachThuPhiDot();

        imageObjectProperty = new SimpleObjectProperty<>();

        citizenManagerLoginSuccessFlag = false;


    }


    public static synchronized Model getInstance() {
        return model;
    }

    /*****************************************************************************/
    //Citizen Manager Method
    public void setCitizenManagerLoginSuccessFlag(boolean flag) {
        this.citizenManagerLoginSuccessFlag = flag;
    }
    public boolean getCitizenManagerLoginSuccessFlag() {return citizenManagerLoginSuccessFlag;}
    public CitizenManager getCitizenManager() {return citizenManager;}
    public void verifyManagerAccount(String tenDangNhap, String matKhau) {
        ResultSet resultSet = databaseConnection.getCitizenManagerData(tenDangNhap, matKhau);

        try {
            if(resultSet.isBeforeFirst()) {
                resultSet.next();
                this.citizenManager.setHoTen(resultSet.getString(1));
                this.citizenManager.setTenDangNhap(resultSet.getString(2));
                this.citizenManager.setMatKhau(resultSet.getString(3));
                this.citizenManager.setSoDienThoai(resultSet.getString(4));
                this.citizenManager.setVaiTro(resultSet.getInt(5));
                this.citizenManagerLoginSuccessFlag = true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean checkManagerUsernameExisted(String tenDangNhap) {
        ResultSet resultSet = databaseConnection.checkCitizenManagerUsernameExisted(tenDangNhap);

        try {
            if (resultSet.isBeforeFirst()){
                return true;
            }
            else return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean checkManagerAccountExisted(String hoTen, String tenDangNhap, String soDienThoai, int vaiTro) {
        ResultSet resultSet = databaseConnection.checkCitizenManagerAccountExisted(hoTen, tenDangNhap, soDienThoai, vaiTro);

        try {
            if (resultSet.isBeforeFirst()) return true;
            else return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void updateManagerAccountPassword(String hoTen, String tenDangNhap, String soDienThoai, int vaiTro, String matKhau) {
        databaseConnection.updateCitizenManagerAccountPassword(hoTen, tenDangNhap, soDienThoai, vaiTro, matKhau);
    }
    public void registerManagerAccount(String hoTen, String tenDangNhap, String matKhau, String soDienThoai, int vaiTro) {
        databaseConnection.setCitizenManagerData(hoTen, tenDangNhap, matKhau, soDienThoai, vaiTro);
    }

    // su dung de tao moi 1 Model khi dang xuat va khoi tao lan dau tien.
    public static void createNewInstance() {
        model = new Model();
    }
    public ViewFactory getViewFactory() {return viewFactory;}
    public DatabaseConnection getDatabaseConnection() {return databaseConnection;}

    public ObjectProperty<Image> getImageObjectProperty() {return imageObjectProperty;}
    /*************************************************************************************************/

    public int getNumberOfNhanKhau() {
        ResultSet resultSet = databaseConnection.getNumberOfNhanhKhau();
        int res = 0;
        try {
            if(resultSet.isBeforeFirst()) {
                resultSet.next();
                res =  resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }
    public int getNumberOfHoKhau(){
        ResultSet resultSet = databaseConnection.getNumberOfHoKhau();
        int res = 0;
        try {
            if(resultSet.isBeforeFirst()){
                resultSet.next();
                res = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }
    public int getNumberOfTamTru(){
        ResultSet resultSet = databaseConnection.getNumberOfTamTru();
        int res = 0;
        try {
            if(resultSet.isBeforeFirst()){
                resultSet.next();
                res = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }
    public int getNumberOfTamVang(){
        ResultSet resultSet = databaseConnection.getNumberOfTamVang();
        int res = 0;
        try {
            if(resultSet.isBeforeFirst()){
                resultSet.next();
                res = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }
    public int getNumberOfNhanKhauNam(){
        ResultSet resultSet = databaseConnection.getNumberOfNhanKhauNam();
        int res = 0;
        try {
            if(resultSet.isBeforeFirst()){
                resultSet.next();
                res = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }
    public int getNumberOfNhanKhauNu(){
        ResultSet resultSet = databaseConnection.getNumberOfNhanKhauNu();
        int res = 0;
        try {
            if(resultSet.isBeforeFirst()){
                resultSet.next();
                res = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    public int getNumberOfNhanKhauDuoi3Tuoi(){
        ResultSet resultSet = databaseConnection.getNumberOfNhanKhauDuoi3Tuoi();
        int res = 0;
        try {
            if(resultSet.isBeforeFirst()){
                resultSet.next();
                res = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }
    public int getNumberOfNhanKhauTu3Den10Tuoi(){
        ResultSet resultSet = databaseConnection.getNumberOfNhanKhauTu3Den10Tuoi();
        int res = 0;
        try {
            if(resultSet.isBeforeFirst()){
                resultSet.next();
                res = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }
    public int getNumberOfNhanKhauTu10Den18Tuoi(){
        ResultSet resultSet = databaseConnection.getNumberOfNhanKhauTu10Den18Tuoi();
        int res = 0;
        try {
            if(resultSet.isBeforeFirst()){
                resultSet.next();
                res = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }
    public int getNumberOfNhanKhauTu18Den60Tuoi(){
        ResultSet resultSet = databaseConnection.getNumberOfNhanKhauTu18Den60Tuoi();
        int res = 0;
        try {
            if(resultSet.isBeforeFirst()){
                resultSet.next();
                res = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    public int getNumberOfNhanKhauTren60Tuoi(){
        ResultSet resultSet = databaseConnection.getNumberOfNhanKhauTren60Tuoi();
        int res = 0;
        try {
            if(resultSet.isBeforeFirst()){
                resultSet.next();
                res = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    public int getNamHienTai(){
        ResultSet resultSet = databaseConnection.getNamHienTai();
        int res = 0;
        try {
            if(resultSet.isBeforeFirst()){
                resultSet.next();
                res = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    public int getHoKhauOfNamHienTai(){
        ResultSet resultSet = databaseConnection.getHoKhauOfNamHienTai();
        int res = 0;
        try {
            if(resultSet.isBeforeFirst()){
                resultSet.next();
                res = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }
    public int getHoKhauOfNam(int r){
        ResultSet resultSet = databaseConnection.getHoKhauOfNam(r);
        int res = 0;
        try {
            if(resultSet.isBeforeFirst()){
                resultSet.next();
                res = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    public int getTamTruOfThangVaNam(int thang, int nam){
        ResultSet resultSet = databaseConnection.getTamTruOfThangVaNam(thang,nam);
        int res = 0;
        try {
            if(resultSet.isBeforeFirst()){
                resultSet.next();
                res = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }
    public int getTamTruViLyDoHocTap(int nam){
        ResultSet resultSet = databaseConnection.getTamTruViLyDoHocTap(nam);
        int res = 0;
        try {
            if(resultSet.isBeforeFirst()){
                resultSet.next();
                res = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }
    public int getTamTruViLyDoLamViec(int nam){
        ResultSet resultSet = databaseConnection.getTamTruViLyDoLamViec(nam);
        int res = 0;
        try {
            if(resultSet.isBeforeFirst()){
                resultSet.next();
                res = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    public int getTamTruViLyDoSucKhoe(int nam){
        ResultSet resultSet = databaseConnection.getTamTruViLyDoSucKhoe(nam);
        int res = 0;
        try {
            if(resultSet.isBeforeFirst()){
                resultSet.next();
                res = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    public int getTamTruViLyDoKhac(int nam){
        return  getNumberOfTamTru(nam) - getTamTruViLyDoHocTap(nam) - getTamTruViLyDoSucKhoe(nam) - getTamTruViLyDoLamViec(nam);

    }
    public int getTamVangOfThangVaNam(int thang, int nam){
        ResultSet resultSet = databaseConnection.getTamVangOfThangVaNam(thang, nam);
        int res = 0;
        try {
            if(resultSet.isBeforeFirst()){
                resultSet.next();
                res = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    public int getTamVangViLyDoSucKhoe(int nam){
        ResultSet resultSet = databaseConnection.getTamVangViLyDoSucKhoe(nam);
        int res = 0;
        try {
            if(resultSet.isBeforeFirst()){
                resultSet.next();
                res = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    public int getTamVangViLyDoHocTap(int nam){
        ResultSet resultSet = databaseConnection.getTamVangViLyDoHocTap(nam);
        int res = 0;
        try {
            if(resultSet.isBeforeFirst()){
                resultSet.next();
                res = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    public int getTamVangViLyDoLamViec(int nam){
        ResultSet resultSet = databaseConnection.getTamVangViLyDoLamViec(nam);
        int res = 0;
        try {
            if(resultSet.isBeforeFirst()){
                resultSet.next();
                res = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    public int getTamVangViLyDoKhac(int nam){
        return  getNumberOfTamVang(nam) - getTamVangViLyDoHocTap(nam) - getTamVangViLyDoLamViec(nam) - getTamVangViLyDoSucKhoe(nam);
    }

    public int getNumberOfTamTru(int nam){
        ResultSet resultSet = databaseConnection.getNumberOfTamTru(nam);
        int res = 0;
        try {
            if(resultSet.isBeforeFirst()){
                resultSet.next();
                res = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    public int getNumberOfTamVang(int nam){
        ResultSet resultSet = databaseConnection.getNumberOfTamVang(nam);
        int res = 0;
        try {
            if(resultSet.isBeforeFirst()){
                resultSet.next();
                res = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    public boolean KiemTraXemMaNhanKhauDaTonTaiTrongTamVang(int manhankhau){
        ResultSet resultSet = databaseConnection.KiemTraXemMaNhanKhauDaTonTaiTrongTamVang(manhankhau);
        int res = 0;
        boolean kiemtra = false;
        try {
            if(resultSet.isBeforeFirst()){
                resultSet.next();
                res = resultSet.getInt(1);
                if(res > 0) kiemtra = true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return kiemtra;
    }

    public boolean KiemTraMaNhanKhauCoTonTaiHayKhong(int manhankhau){
        ResultSet resultSet = databaseConnection.KiemTraMaNhanKhauCoTonTaiHayKhong(manhankhau);
        int res = 0;
        boolean tontai = false;
        try {
            if(resultSet.isBeforeFirst()){
                resultSet.next();
                res = resultSet.getInt(1);
                if(res > 0) tontai = true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return tontai;
    }



    // ho khau

    public static MainHoKhauCell getHoKhauDuocChon() {
        return hoKhauDuocChon;
    }

    public static void setHoKhauDuocChon(MainHoKhauCell hoKhauDuocChon) {
        Model.hoKhauDuocChon = hoKhauDuocChon;
    }
    // lam cai hien thi nhankhautamvang

    public static MainTamVangCell getNhanKhauTamVangDuocChon(){
        return nhanKhauTamVangDuocChon;
    }

    public static void setNhanKhauTamVangDuocChon(MainTamVangCell nhanKhauTamVangDuocChon){
        Model.nhanKhauTamVangDuocChon = nhanKhauTamVangDuocChon;
    }

    public static thanh_vien_cua_ho_cell getThanhVienCuaHoCell() {
        return thanhVienCuaHoCell;
    }

    public static void setThanhVienCuaHoCell(thanh_vien_cua_ho_cell thanhVienCuaHoCell) {
        Model.thanhVienCuaHoCell = thanhVienCuaHoCell;
    }



    /*************************************************************************************************/
    //Thu phí
    public HoaDonModel getHoaDonModel() {
        return hoaDonModel;
    }

    public DanhSachThuPhiModel getDanhSachThuPhiModel() {
        return danhSachThuPhiModel;
    }

    public FeeKhoanThuModel getFeeKhoanThuModel() {
        return feeKhoanThuModel;
    }

    public FeeKhoanThuDotModel getFeeKhoanThuDotModel() {
        return feeKhoanThuDotModel;
    }

    public ObservableList<FeeKhoanThuCell> getDanhSachKhoanThu() {return danhSachKhoanThu;}

    public ObservableList<FeeKhoanThuDotCell> getDanhSachKhoanThuDot() {
        return danhSachKhoanThuDot;
    }

    public ObservableList<FeeHoKhauCell> getDanhSachDaDongPhi() {
        return danhSachDaDongPhi;
    }

    public ObservableList<FeeHoKhauCell> getDanhSachChuaDongPhi() {
        return danhSachChuaDongPhi;
    }

    public ObservableList<FeeHoKhauCellDot> getDanhSachDaDongPhiDot() {
        return danhSachDaDongPhiDot;
    }

    public ObservableList<FeeHoKhauCellDot> getDanhSachChuaDongPhiDot() {
        return danhSachChuaDongPhiDot;
    }

    private void initDanhSachKhoanThu() {
        ResultSet resultSet = databaseConnection.getDanhSachKhoanThu();
        try {
            if (resultSet.isBeforeFirst()) {
                while (resultSet.next()) {
                    int maKhoanThu = resultSet.getInt(1);
                    String tenKhoanThu = resultSet.getNString(2);
                    int batBuoc = resultSet.getInt(3);
                    int soTienCanDong = resultSet.getInt(4);
                    String ngayTao = resultSet.getString(5);
                    int id = resultSet.getInt(7);
                    danhSachKhoanThu.add(new FeeKhoanThuCell(id,maKhoanThu, tenKhoanThu, ngayTao));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void innitDanhSachThuPhiDot(){
        ResultSet resultSet = databaseConnection.getDanhSachKhoanThuDot();
        try {
            if (resultSet.isBeforeFirst()) {
                while (resultSet.next()) {
                    int maDotThu = resultSet.getInt(1);
                    String tenKhoanThu = resultSet.getNString(2);
//                    int batBuoc = resultSet.getInt(3);
//                    int soTienCanDong = resultSet.getInt(4);
                    String ngayTao = resultSet.getString(4);

                    danhSachKhoanThuDot.add(new FeeKhoanThuDotCell(maDotThu, tenKhoanThu, ngayTao));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public int getTongSoTienDaThuPhi(){
        int res = 0;
        ResultSet resultSet = databaseConnection.getTongSoTienDaThuPhi();
        try {
            if(resultSet.isBeforeFirst()){
                resultSet.next();
                res = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }
    /*********************************************************/
    public int getNumberOfCacLoaiPhi(){
        int res = 0;
        ResultSet resultSet = databaseConnection.getNumberOfCacLoaiPhi();
        try {
            if(resultSet.isBeforeFirst()){
                resultSet.next();
                res = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }
    public ArrayList<Integer> getTongSoDotThu(){
        ResultSet resultSet = databaseConnection.getTongDotThu();
        ArrayList<Integer> res = new ArrayList<>();
        try {
            while (resultSet.next()) { // Lặp qua các dòng kết quả
                res.add(resultSet.getInt(1)); // Thêm giá trị vào danh sách
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }
    public int getFeeUngHoByDot(int makhoanthu){
        ResultSet resultSet = databaseConnection.getFeeUngHoByDot(makhoanthu);
        int res = 0;
        try {
            if(resultSet.isBeforeFirst()){
                resultSet.next();
                res = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }
    public int getFeeChungCuByDot(int madotthu){
        ResultSet resultSet = databaseConnection.getFeeChungCuByDot(madotthu);
        int res = 0;
        try {
            if(resultSet.isBeforeFirst()){
                resultSet.next();
                res = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }
    public String getFeeTenDotThu(int madotthu){
        ResultSet resultSet = databaseConnection.getFeeTenDotThu(madotthu);
        String res = null;
        try {
            if(resultSet.isBeforeFirst()){
                resultSet.next();
                res = resultSet.getString(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }
    public int getSoLuongLoaiPhi(int madotthu){
        ResultSet resultSet = databaseConnection.getSoLuongLoaiPhi(madotthu);
        int res = 0;
        try {
            while (resultSet.next()) { // Lặp qua các dòng kết quả
                res =(resultSet.getInt(1)); // Thêm giá trị vào danh sách
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }
    public int getTongTienDongGop(int madotthu){
        ResultSet resultSet = databaseConnection.getTongTienDongGop(madotthu);
        int res = 0;
        try {
            while (resultSet.next()) { // Lặp qua các dòng kết quả
                res =(resultSet.getInt(1)); // Thêm giá trị vào danh sách
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }
    public int getSumAllFee(){
        ResultSet resultSet = databaseConnection.getSumAllFee();
        int res = 0;
        try {
            while (resultSet.next()) { // Lặp qua các dòng kết quả
                res+=(resultSet.getInt(1)); // Thêm giá trị vào danh sách
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }
    public int getTongTienDongGop(){
        ResultSet resultSet = databaseConnection.getTongTienDongGop();
        int res = 0;
        try {
            while (resultSet.next()) { // Lặp qua các dòng kết quả
                res =(resultSet.getInt(1)); // Thêm giá trị vào danh sách
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }

}
