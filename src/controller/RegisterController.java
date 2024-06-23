/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import javax.swing.JOptionPane;
import model.User;
import view.Register;
import view.Login;

/**
 *
 * @author LENOVO
 */
public class RegisterController {
    private User model;
    private Register view;
    private Connection connection;

    public RegisterController(User model, Register view, Connection connection) {
        this.model = model;
        this.view = view;
        this.connection = connection;

        this.view.setRegisterButtonListener(new RegisterButtonListener());
    }

    class RegisterButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                model.setNIM(Integer.parseInt(view.getNIM()));
                model.setEmail(view.getEmail());
                String password = view.getPassword();
                String confirmPassword = view.getConfirmPassword();

                if (password.equals(confirmPassword)) {
                    model.setPassword(password); // Password akan di-hash di dalam setter
                    if (model.register(connection)) {
                        JOptionPane.showMessageDialog(null, "Registrasi berhasil!");
                        // Tutup form registrasi dan buka form login
                        view.dispose();
                        new Login().setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(null, "Registrasi gagal. Periksa kembali data Anda.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Password dan konfirmasi password tidak cocok.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "NIM harus berupa angka.");
            }
        }
    }
}