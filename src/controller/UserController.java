package controller;

import helper.dbconfig;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import model.DataDiri;
import model.Kas;
import model.User;
import view.EditDataDiri;

public class UserController {

    public static void editDataDiri(User user, EditDataDiri form) {
        try (Connection connection = dbconfig.getConnection()) {
            boolean isTanggalLahirEmpty = form.getTanggalLahir().isEmpty();
            boolean isImagePathEmpty = form.getImagePath().isEmpty();

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

                if (!isImagePathEmpty) {
                    statement.setString(index++, form.getImagePath());
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

    public static void setKas(User user, Kas form) {
        try (Connection connection = dbconfig.getConnection()) {
            String checkSql = "SELECT COUNT(*) FROM kas WHERE nim = ?";
            PreparedStatement checkStatement = connection.prepareStatement(checkSql);
            checkStatement.setInt(1, user.getNIM());
            ResultSet resultSet = checkStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);

            if (count > 0) {
                String updateSql = "UPDATE kas SET november = ?, desember = ?, januari = ?, februari = ?, maret = ?, april = ?, mei = ?, juni = ?, juli = ?, agustus = ?, dies_natalis = ?, bukti_pembayaran = ?, keterangan = ? WHERE nim = ?";
                PreparedStatement updateStatement = connection.prepareStatement(updateSql);

                int index = 1;
                updateStatement.setBoolean(index++, form.november());
                updateStatement.setBoolean(index++, form.desember());
                updateStatement.setBoolean(index++, form.januari());
                updateStatement.setBoolean(index++, form.februari());
                updateStatement.setBoolean(index++, form.maret());
                updateStatement.setBoolean(index++, form.april());
                updateStatement.setBoolean(index++, form.mei());
                updateStatement.setBoolean(index++, form.juni());
                updateStatement.setBoolean(index++, form.juli());
                updateStatement.setBoolean(index++, form.agustus());
                updateStatement.setBoolean(index++, form.diesNatalis());
                updateStatement.setString(index++, form.getBuktiPembayaran());
                updateStatement.setString(index++, "Dicek Admin");
                updateStatement.setInt(index++, user.getNIM());

                updateStatement.executeUpdate();
                JOptionPane.showMessageDialog(form, "Successfully updated", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                String insertSql = "INSERT INTO kas VALUES(NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement insertStatement = connection.prepareStatement(insertSql);

                int index = 1;
                insertStatement.setInt(index++, user.getNIM());
                insertStatement.setBoolean(index++, form.november());
                insertStatement.setBoolean(index++, form.desember());
                insertStatement.setBoolean(index++, form.januari());
                insertStatement.setBoolean(index++, form.februari());
                insertStatement.setBoolean(index++, form.maret());
                insertStatement.setBoolean(index++, form.april());
                insertStatement.setBoolean(index++, form.mei());
                insertStatement.setBoolean(index++, form.juni());
                insertStatement.setBoolean(index++, form.juli());
                insertStatement.setBoolean(index++, form.agustus());
                insertStatement.setBoolean(index++, form.diesNatalis());
                insertStatement.setString(index++, form.getBuktiPembayaran());
                insertStatement.setString(index++, "Dicek Admin");

                insertStatement.executeUpdate();
                JOptionPane.showMessageDialog(form, "Successfully inserted", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(form, "Error updating data", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static void konfirmasiKas(int kas_id) {
        try (Connection connection = dbconfig.getConnection()) {
            String sql = "UPDATE kas SET keterangan = ? WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, "Telah Dikonfirmasi Admin");
            stmt.setInt(2, kas_id);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Pembayaran Telah Dikonfirmasi");
           
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error updating data", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
