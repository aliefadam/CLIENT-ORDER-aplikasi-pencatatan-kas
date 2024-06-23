package controller;

import helper.dbconfig;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import model.DataDiri;
import model.User;
import view.EditDataDiri;

public class UserController {

    public static void editDataDiri(User user, EditDataDiri form) {
        try (Connection connection = dbconfig.getConnection()) {
            boolean isTanggalLahirEmpty = form.getTanggalLahir().isEmpty();
            boolean isImagePathEmpty = form.getImagePath().isEmpty();

            // Build the SQL query dynamically based on the conditions
            String sql = "UPDATE data_diri SET nama_lengkap = ?,"
                    + " nama_panggilan = ?, tempat_lahir = ?, " 
                    + (isTanggalLahirEmpty ? "" : "tanggal_lahir = ?, ") 
                    + "jenis_kelamin = ?, " + "kelas = ?, " + "suku = ?, " 
                    + "alamat_asal = ?, " + "alamat_kos = ?, " 
                    + "no_hp_pribadi = ?, " + "no_hp_ayah = ?, " 
                    + "no_hp_ibu = ?, " + "no_hp_teman_kos = ? " 
                    + (isImagePathEmpty ? "" : ", foto = ? ") 
                    + "WHERE nim = ?";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                int index = 1;
                statement.setString(index++, form.getNamaLengkap());
                user.setNamaLengkap(form.getNamaLengkap());

                statement.setString(index++, form.getNamaPanggilan());
                user.setNamaPanggilan(form.getNamaPanggilan());

                statement.setString(index++, form.getTempatLahir());
                user.setTempatLahir(form.getTempatLahir());

                if (!isTanggalLahirEmpty) {
                    statement.setDate(index++, java.sql.Date.valueOf(form.getTanggalLahir()));
                    user.setTanggalLahir(java.sql.Date.valueOf(form.getTanggalLahir()));
                }

                statement.setString(index++, form.getJenisKelamin());
                user.setJenisKelamin(form.getJenisKelamin());

                statement.setString(index++, form.getKelas());
                user.setKelas(form.getKelas());

                statement.setString(index++, form.getSuku());
                user.setSuku(form.getSuku());

                statement.setString(index++, form.getAlamatAsal());
                user.setAlamatAsal(form.getAlamatAsal());

                statement.setString(index++, form.getAlamatKos());
                user.setAlamatKos(form.getAlamatKos());

                statement.setLong(index++, form.getNoHpPribadi());
                user.setNoHpPribadi(form.getNoHpPribadi());

                statement.setLong(index++, form.getNoHpAyah());
                user.setNoHpAyah(form.getNoHpAyah());

                statement.setLong(index++, form.getNoHpIbu());
                user.setNoHpIbu(form.getNoHpIbu());

                statement.setLong(index++, form.getNoHpTemanKos());
                user.setNoHpTemanKos(form.getNoHpTemanKos());

                if (!isImagePathEmpty) {
                    statement.setString(index++, form.getImagePath());
                    user.setFoto(form.getImagePath());
                }

                statement.setInt(index++, form.getNim());

                statement.executeUpdate();
                JOptionPane.showMessageDialog(form, "Data updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                new DataDiri(user).setVisible(true);
                form.dispose();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(form, "Error updating data", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
