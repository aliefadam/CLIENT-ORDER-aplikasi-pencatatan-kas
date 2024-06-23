package controller;

import helper.dbconfig;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import model.DataDiri;
import model.User;
import view.EditDataDiri;

public class UserController {

    public static void editDataDiri(User user, EditDataDiri form) {
        try (Connection connection = dbconfig.getConnection()) {
            boolean isTanggalLahirEmpty = form.getTanggalLahir().isEmpty();

            String sql = "UPDATE data_diri SET "
                    + "nama_lengkap = ?, "
                    + "nama_panggilan = ?, "
                    + "tempat_lahir = ?, "
                    + (isTanggalLahirEmpty ? "" : "tanggal_lahir = ?, ")
                    + "jenis_kelamin = ?, "
                    + "kelas = ?, "
                    + "suku = ?, "
                    + "alamat_asal = ?, "
                    + "alamat_kos = ?, "
                    + "no_hp_pribadi = ?, "
                    + "no_hp_ayah = ?, "
                    + "no_hp_ibu = ?, "
                    + "no_hp_teman_kos = ?, "
                    + "foto = ? "
                    + "WHERE nim = ?";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                int index = 1;
                statement.setString(index++, form.getNamaLengkap());
                statement.setString(index++, form.getNamaPanggilan());
                statement.setString(index++, form.getTempatLahir());
                if (!isTanggalLahirEmpty) {
                    statement.setDate(index++, java.sql.Date.valueOf(form.getTanggalLahir()));
                }
                statement.setString(index++, form.getJenisKelamin());
                statement.setString(index++, form.getKelas());
                statement.setString(index++, form.getSuku());
                statement.setString(index++, form.getAlamatAsal());
                statement.setString(index++, form.getAlamatKos());
                statement.setLong(index++, form.getNoHpPribadi());
                statement.setLong(index++, form.getNoHpAyah());
                statement.setLong(index++, form.getNoHpIbu());
                statement.setLong(index++, form.getNoHpTemanKos());
                statement.setString(index++, form.getImagePath());
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
