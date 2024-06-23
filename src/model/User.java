/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author LENOVO
 */
public class User {

    private int id;
    private int NIM;
    private String email;
    private String password;
    private boolean isAdmin;
    private String namaLengkap;
    private String namaPanggilan;
    private String tempatLahir;
    private Date tanggalLahir;
    private String jenisKelamin;
    private String kelas;
    private String suku;
    private String alamatAsal;
    private String alamatKos;
    private int noHpPribadi;
    private int noHpAyah;
    private int noHpIbu;
    private int noHpTemanKos;

    public User(int NIM, String email, String password) {
        this.NIM = NIM;
        this.email = email;
        this.password = password;
        this.isAdmin = false;
    }

    // Tambahkan konstruktor tanpa parameter
    public User() {
        this.isAdmin = false;
    }

    // Getter dan setter...
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNIM(int NIM) {
        this.NIM = NIM;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public int getNIM() {
        return NIM;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }

    public String getNamaPanggilan() {
        return namaPanggilan;
    }

    public void setNamaPanggilan(String namaPanggilan) {
        this.namaPanggilan = namaPanggilan;
    }

    public String getTempatLahir() {
        return tempatLahir;
    }

    public void setTempatLahir(String tempatLahir) {
        this.tempatLahir = tempatLahir;
    }

    public Date getTanggalLahir() {
        return tanggalLahir;
    }

    public void setTanggalLahir(Date tanggalLahir) {
        this.tanggalLahir = tanggalLahir;
    }

    public String getJenisKelamin() {
        return jenisKelamin;
    }

    public void setJenisKelamin(String jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }

    public String getKelas() {
        return kelas;
    }

    public void setKelas(String kelas) {
        this.kelas = kelas;
    }

    public String getSuku() {
        return suku;
    }

    public void setSuku(String suku) {
        this.suku = suku;
    }

    public String getAlamatAsal() {
        return alamatAsal;
    }

    public void setAlamatAsal(String alamatAsal) {
        this.alamatAsal = alamatAsal;
    }

    public String getAlamatKos() {
        return alamatKos;
    }

    public void setAlamatKos(String alamatKos) {
        this.alamatKos = alamatKos;
    }

    public int getNoHpPribadi() {
        return noHpPribadi;
    }

    public void setNoHpPribadi(int noHpPribadi) {
        this.noHpPribadi = noHpPribadi;
    }

    public int getNoHpAyah() {
        return noHpAyah;
    }

    public void setNoHpAyah(int noHpAyah) {
        this.noHpAyah = noHpAyah;
    }

    public int getNoHpIbu() {
        return noHpIbu;
    }

    public void setNoHpIbu(int noHpIbu) {
        this.noHpIbu = noHpIbu;
    }

    public int getNoHpTemanKos() {
        return noHpTemanKos;
    }

    public void setNoHpTemanKos(int noHpTemanKos) {
        this.noHpTemanKos = noHpTemanKos;
    }

    public String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashInBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashInBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean authenticate(Connection connection) {
        String sql = "SELECT * FROM users WHERE NIM = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, this.NIM);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String storedPassword = resultSet.getString("password");
                boolean isAdmin = resultSet.getBoolean("is_admin");
                setAdmin(isAdmin);

                if (isAdmin) {
                    return storedPassword.equals(this.password); // Untuk admin, password tidak di-hash
                } else {
                    return storedPassword.equals(hashPassword(this.password)); // Untuk user biasa, password di-hash
                }
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean register(Connection connection) {
        String sql = "INSERT INTO users (NIM, email, password, is_admin) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, this.NIM);
            statement.setString(2, this.email);
            statement.setString(3, hashPassword(this.password)); // Gunakan hash password di sini
            statement.setBoolean(4, this.isAdmin); // Set nilai is_admin ke false atau sesuai kebutuhan
            int rowsInserted = statement.executeUpdate();

            String sqlDataDiri = "INSERT INTO data_diri (NIM, email) VALUES (?, ?)";
            PreparedStatement statementDataDiri = connection.prepareStatement(sqlDataDiri);
            statementDataDiri.setInt(1, this.NIM);
            statementDataDiri.setString(2, this.email);
            statementDataDiri.executeUpdate();

            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    public boolean updatePassword(Connection connection, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE NIM = ? AND email = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, hashPassword(newPassword));
            statement.setInt(2, this.NIM);
            statement.setString(3, this.email);
            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean savePersonalData(Connection connection) {
        String sql = "INSERT INTO data_diri (nim, email, nama_lengkap, nama_panggilan, tempat_lahir, tanggal_lahir, jenis_kelamin, kelas, suku, alamat_asal, alamat_kos, no_hp_pribadi, no_hp_ayah, no_hp_ibu, no_hp_teman_kos) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE email=?, nama_lengkap=?, nama_panggilan=?, tempat_lahir=?, tanggal_lahir=?, jenis_kelamin=?, kelas=?, suku=?, alamat_asal=?, alamat_kos=?, no_hp_pribadi=?, no_hp_ayah=?, no_hp_ibu=?, no_hp_teman_kos=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, this.NIM);
            statement.setString(2, this.email);
            statement.setString(3, this.namaLengkap);
            statement.setString(4, this.namaPanggilan);
            statement.setString(5, this.tempatLahir);
            statement.setDate(6, this.tanggalLahir);
            statement.setString(7, this.jenisKelamin);
            statement.setString(8, this.kelas);
            statement.setString(9, this.suku);
            statement.setString(10, this.alamatAsal);
            statement.setString(11, this.alamatKos);
            statement.setInt(12, this.noHpPribadi);
            statement.setInt(13, this.noHpAyah);
            statement.setInt(14, this.noHpIbu);
            statement.setInt(15, this.noHpTemanKos);
            // Update fields
            statement.setString(16, this.email);
            statement.setString(17, this.namaLengkap);
            statement.setString(18, this.namaPanggilan);
            statement.setString(19, this.tempatLahir);
            statement.setDate(20, this.tanggalLahir);
            statement.setString(21, this.jenisKelamin);
            statement.setString(22, this.kelas);
            statement.setString(23, this.suku);
            statement.setString(24, this.alamatAsal);
            statement.setString(25, this.alamatKos);
            statement.setInt(26, this.noHpPribadi);
            statement.setInt(27, this.noHpAyah);
            statement.setInt(28, this.noHpIbu);
            statement.setInt(29, this.noHpTemanKos);

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
